create or replace PROCEDURE sp_spil_transaction_history (
    p_card_number         IN VARCHAR2,
    p_prod_id             IN NUMBER,
    p_account_id          IN NUMBER,
    p_card_status         IN VARCHAR2,
    p_old_cardstatus      IN VARCHAR2,
    p_proxynumber         IN VARCHAR2,
    p_activation_date     IN DATE,
    p_last_trandate       IN DATE,
    p_firsttime_topup     IN VARCHAR2,
    p_upc                 IN VARCHAR2,
    p_sp_numbertype       IN VARCHAR2,
    p_msg_type            IN VARCHAR2,
    p_delivery_channel    IN VARCHAR2,
    p_txn_code            IN VARCHAR2,
    p_trandate            IN VARCHAR2,
    p_trantime            IN VARCHAR2,
    p_tran_timezone       IN VARCHAR2,
    p_rrn                 IN NUMBER,
    p_merchant_name       IN VARCHAR2,
    p_store_id            IN VARCHAR2,
    p_terminalid          IN VARCHAR2,
    p_store_address1      IN VARCHAR2,
    p_store_address2      IN VARCHAR2,
    p_store_city          IN VARCHAR2,
    p_store_state         IN VARCHAR2,
    p_merch_refnum        IN VARCHAR2,
    p_locale_country      IN VARCHAR2,
    p_locale_currency     IN VARCHAR2,
    p_locale_language     IN VARCHAR2,
    p_pos_entrymode       IN VARCHAR2,
    p_pos_conditioncode   IN VARCHAR2,
    p_source_info         IN VARCHAR2,
    p_fee_amount          IN NUMBER,
    p_txn_amt             IN NUMBER,
    p_currcode            IN VARCHAR2,
    p_party_supported     IN VARCHAR2,
    p_partial_auth        IN VARCHAR2,
    p_mdm_id              IN VARCHAR2,
    p_startdate           IN NUMBER,
    p_enddate             IN NUMBER,
    p_row_count           IN NUMBER,
    p_resp_code           OUT VARCHAR2,
    p_err_msg             OUT VARCHAR2,
    p_auth_id             OUT VARCHAR2,
    p_authorizedamt       OUT NUMBER,
    p_hist                OUT CLOB,
    p_rec_cnt             OUT NUMBER
    
) AS

    v_trans_desc                transaction.transaction_description%TYPE;
    v_dr_cr_flag                transaction.credit_debit_indicator%TYPE;
    v_tran_type                 transaction.is_financial%TYPE;
    v_card_num_encr             card.card_num_encr%TYPE;
    v_card_num_hash             card.card_num_hash%TYPE;
    v_last_txndate              card.last_txndate%TYPE;
    v_closing_avail_balance     NUMBER;
    v_closing_ledger_balance    NUMBER;
    v_auth_id                   NUMBER;
    v_tran_seq_id               NUMBER;
    v_txn_date                  DATE := SYSDATE;
    v_purse_id                  purse.purse_id%TYPE;
    v_avail_opening_balance     NUMBER;
    v_ledger_opening_balance    NUMBER;
    v_fee_flag                  VARCHAR2(1);
    v_card_last4digit           VARCHAR2(10);
    v_tran_fee                  NUMBER;
    v_flat_fee                  NUMBER;
    v_per_fee                   NUMBER;
    v_min_fee                   NUMBER;
    v_total_amt                 NUMBER;
    v_fee_condition             VARCHAR2(1);
    v_free_txncount_flag        VARCHAR2(1);
    v_max_txncount_flag         VARCHAR2(1);
    v_closing_balance           NUMBER;
    v_fee_closing_balance       NUMBER;
    v_percent_closing_balance   NUMBER;
    v_fee_cr_dr_flag            VARCHAR2(1);
    v_resp_code                 VARCHAR2(100);
    v_err_msg                   VARCHAR2(100);
    v_merchant_id               VARCHAR2(50);
    V_LOCATION_ID               VARCHAR2(50);
    v_hist                   VARCHAR2 (6000);
    v_currcode                 VARCHAR2 (4);
    v_business_date             DATE;
    l_exception EXCEPTION;


    CURSOR cur_hist_date
    IS
        SELECT z.* FROM (
        SELECT  TO_CHAR (TO_DATE (   sl.BUSINESS_DATE,
                                            'DD-MM-YYYY'
                                           ),
                                   'YYYYMMDD'
                                  )
                       || NVL (RPAD (tl.TRANSACTION_CODE, 3, ' '), '   ')
                       || LPAD (sl.TRANSACTION_AMOUNT * 100, 12, 0)
                       || LPAD (sl.CLOSING_BALANCE * 100, 12, 0)
                       || NVL (RPAD (sl.MERCHANT_NAME, 15, ' '),
                               '               '
                              )
                       || NVL (LPAD (TO_CHAR (tl.STORE_ID), 15, '0'),
                               '               '
                              )
                       || '|' FROM  TRANSACTION_LOG tl, STATEMENTS_LOG sl, transaction t
                 WHERE  sl.transaction_sqid = tl.transaction_sqid
                   AND tl.DELIVERY_CHANNEL = p_delivery_channel
                   AND tl.TRANSACTION_CODE = t.TRANSACTION_CODE
                   AND tl.MSG_TYPE = p_msg_type
                   AND sl.TRANSACTION_CODE  = t.TRANSACTION_CODE
                   AND sl.RRN = tl.RRN
                   AND TO_DATE (TRIM (sl.BUSINESS_DATE),'DD-MM-YYYY') = TO_DATE(TRIM (tl.BUSINESS_DATE),'DD-MM-YYYY') -- BUSSINESS DATE
                   AND sl.CARD_NUM_HASH = v_card_num_hash
                   AND tl.RESPONSE_ID = trans_const.SUCCESS
                   AND (sl.DELIVERY_CHANNEL,sl.TRANSACTION_CODE) NOT IN (('01', '004'),('01', '006'))
                   AND sl.CREDIT_DEBIT_FLAG in ('C', 'D')
                   AND tl.TRAN_REVERSE_FLAG ='N'
                   AND TO_DATE (TRIM (sl.TRANSACTION_DATE),'YYYYMMDD') BETWEEN TO_DATE (p_startdate,   -- START DATE
                                                              'YYYYMMDD'
                                                             )
                                                 AND TO_DATE (p_enddate,                             -- END DATE
                                                              'YYYYMMDD'
                                                             )
              ORDER BY sl.INS_DATE DESC) z
        WHERE ROWNUM <= p_row_count;       -- TRANSACTION COUNT
        
        
       CURSOR cur_hist_not_date
       IS
        SELECT z.* FROM (
        SELECT  TO_CHAR (TO_DATE (   sl.BUSINESS_DATE,
                                            'DD-MM-YYYY'
                                           ),
                                   'YYYYMMDD'
                                  )
                       || NVL (RPAD (tl.TRANSACTION_CODE, 3, ' '), '   ')
                       || LPAD (sl.TRANSACTION_AMOUNT * 100, 12, 0)
                       || LPAD (sl.CLOSING_BALANCE * 100, 12, 0)
                       || NVL (RPAD (sl.MERCHANT_NAME, 15, ' '),
                               '               '
                              )
                       || NVL (LPAD (TO_CHAR (tl.STORE_ID), 15, '0'),
                               '               '
                              )
                       || '|' FROM  TRANSACTION_LOG tl, STATEMENTS_LOG sl, transaction t
                 WHERE  sl.transaction_sqid = tl.transaction_sqid
                   AND tl.DELIVERY_CHANNEL = p_delivery_channel
                   AND tl.TRANSACTION_CODE = t.TRANSACTION_CODE
                   AND tl.MSG_TYPE = p_msg_type
                   AND sl.TRANSACTION_CODE  = t.TRANSACTION_CODE
                   AND sl.RRN = tl.RRN
                   AND TO_DATE (TRIM (sl.BUSINESS_DATE),'DD-MM-YYYY') = TO_DATE(TRIM (tl.BUSINESS_DATE),'DD-MM-YYYY') -- BUSSINESS DATE
                   AND sl.CARD_NUM_HASH = v_card_num_hash
                   AND tl.RESPONSE_ID = trans_const.SUCCESS
                   AND (sl.DELIVERY_CHANNEL,sl.TRANSACTION_CODE) NOT IN (('01', '004'),('01', '006'))
                   AND sl.CREDIT_DEBIT_FLAG in ('C', 'D')
                   AND tl.TRAN_REVERSE_FLAG ='N'
--                   AND TO_DATE (TRIM (sl.TRANSACTION_DATE),'YYYYMMDD') BETWEEN TO_DATE ('20180530',   -- START DATE
--                                                              'YYYYMMDD'
--                                                             )
--                                                 AND TO_DATE ('20180531',                             -- END DATE
--                                                              'YYYYMMDD'
--                                                             )
              ORDER BY sl.INS_DATE DESC) z
        WHERE ROWNUM <= p_row_count;       -- TRANSACTION COUNT
        
        
BEGIN
        
        --(CREATE SEQUENCE TRANSACTION_ID_SEQ START WITH 1 increment by 1);
        SELECT
            transaction_id_seq.NEXTVAL
        INTO
            v_tran_seq_id
        FROM
            dual;

        dbms_output.put_line('v_tran_seq_id ..' || v_tran_seq_id);     
        
        
        --Getting hash pan
        BEGIN
            v_card_num_hash := fn_hash(p_card_number);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code := trans_const.card_not_found;
                p_err_msg := 'Error while converting card number to HASH '
                 || substr(sqlerrm,1,200);
            --RAISE l_exception;
        END;
        
        
        
        --Getting encr pan

        BEGIN
            v_card_num_encr := fn_emaps_main(p_card_number);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code := trans_const.card_not_found;
                p_err_msg := 'Error while converting card number to ENCR '
                 || substr(sqlerrm,1,200);
            --RAISE l_exception;
        END;
        
        
        
        
        SELECT
            lpad(
                auth_id_seq.NEXTVAL,
                6,
                '0'
            )
        INTO
            v_auth_id
        FROM
            dual;  
        
        
            --Getting Transaction description and cr_dr indicator

        BEGIN
            SELECT
                transaction_description,
                credit_debit_indicator,
                is_financial
            INTO
                v_trans_desc,v_dr_cr_flag,v_tran_type
            FROM
                transaction
            WHERE
                transaction_code = p_txn_code;

        EXCEPTION
            WHEN no_data_found THEN
                p_resp_code := trans_const.system_error;
                v_trans_desc := 'Transaction type' || p_txn_code;
                p_err_msg := 'ERROR WHILE Getting Transaction description and cr_dr indicator ';
            --RAISE l_exception;
            WHEN OTHERS THEN
                p_resp_code := trans_const.system_error;
                v_trans_desc := 'Transaction type ' || p_txn_code;
                p_err_msg := 'ERROR WHILE Getting Transaction description and cr_dr indicator ';
            --RAISE l_exception;
        END;
        
        
    ---Calling sp_authorize_transaction_new procedure

        BEGIN
            sp_authorize_transaction(
                p_prod_id,
                v_last_txndate,
                p_mdm_id,
                p_txn_code,
                p_account_id,
                p_txn_amt,
                p_partial_auth,
                p_delivery_channel,
                p_card_number,
                p_rrn,
                p_card_status,
                v_card_num_hash,
                v_card_num_encr,
                p_msg_type,
                p_currcode,
                p_party_supported,
                v_dr_cr_flag,
                v_closing_avail_balance,
                v_closing_ledger_balance,
                v_resp_code,
                v_err_msg,
                v_purse_id,
                v_avail_opening_balance,
                v_ledger_opening_balance,
                v_tran_fee,
                v_flat_fee,
                v_per_fee,
                v_min_fee,
                v_total_amt,
                v_fee_condition,
                v_free_txncount_flag,
                v_max_txncount_flag,
                v_business_date
            );

            p_resp_code := v_resp_code;
            p_err_msg := v_err_msg;
            IF
                p_resp_code <> trans_const.success OR p_err_msg <> 'OK'
            THEN
                RAISE l_exception;
            END IF;

        EXCEPTION
            WHEN l_exception THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_code := trans_const.system_error;
                p_err_msg := 'ERROR WHILE GETTING AUTHORIZE TRANSACTION '
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
        
        
       IF p_startdate IS NULL AND p_enddate IS NULL
       THEN
            BEGIN
                OPEN cur_hist_not_date;
                p_rec_cnt:=0;
                LOOP
                    FETCH cur_hist_not_date
                    INTO v_hist;

                    EXIT WHEN cur_hist_not_date%NOTFOUND;
                    p_hist:= p_hist || v_hist;
                    p_rec_cnt := p_rec_cnt + 1;
                END LOOP;

                CLOSE cur_hist_not_date;
            EXCEPTION
            WHEN OTHERS
            THEN
                p_err_msg :=
                    'Problem while selecting data from CUR_HIST_NOT_DATE cursor'
                || SUBSTR (SQLERRM, 1, 300);
                p_resp_code := trans_const.card_not_found;
--            RAISE exp_reject_record;
            END;
        END IF;
        
        
        IF p_startdate IS NOT NULL AND p_enddate IS NOT NULL
        THEN
            BEGIN
                OPEN cur_hist_date;
                p_rec_cnt:=0;
                LOOP
                    FETCH cur_hist_date
                    INTO v_hist;

                    EXIT WHEN cur_hist_date%NOTFOUND;
                    p_hist := p_hist || v_hist;
                    p_rec_cnt := p_rec_cnt + 1;
                END LOOP;

            CLOSE cur_hist_date;
            EXCEPTION
            WHEN OTHERS
            THEN
                p_err_msg :=
                    'Problem while selecting data from cur_hist_DATE cursor'
                    || SUBSTR (SQLERRM, 1, 300);
                p_resp_code := '21';
--                RAISE exp_reject_record;
            END;
        END IF;
        
        
       --GET PURSE ID

        BEGIN
            SELECT p.purse_id
            INTO
                v_purse_id
            FROM purse p,
                 product_purse pp,
                 currency_code c
            WHERE p.purse_id=pp.purse_id AND p.currency_code=c.currency_id AND pp.product_id=p_prod_id AND c.currency_code=nvl(p_currcode,'USD');
   -- and PP.IS_DEFAULT='Y';

        EXCEPTION
            WHEN no_data_found THEN
     -- V_RESP_CODE:='R0008';
                v_resp_code:=trans_const.invalid_product_identifier;
                p_err_msg:='PURSE NOT FOUND';
                RAISE l_exception;
            WHEN OTHERS THEN
                v_resp_code:=trans_const.invalid_request;
                p_err_msg:='Invalid Purse '
                ||substr(sqlerrm,1,255);
                RAISE l_exception;
        END;
        
        
        
        
       BEGIN
        --balance_auth_credit
           select 
                    ledger_balance,
                    available_balance
            into   
                    v_ledger_opening_balance,
                    v_avail_opening_balance
            from
                    account_purse
            WHERE account_id=p_account_id AND product_id=p_prod_id AND purse_id=v_purse_id;    


       EXCEPTION
       WHEN NO_DATA_FOUND THEN
            V_RESP_CODE := 'R0008';
            P_ERR_MSG   := 'Invalid Product Identifier for card ' || v_card_num_hash;

       RAISE l_exception;
       WHEN OTHERS THEN
            V_RESP_CODE := 'R0008';
            P_ERR_MSG   := 'Invalid Product Identifier for card ' || v_card_num_hash;

      RAISE l_exception;

      END;
      
      p_auth_id := v_auth_id;
      p_authorizedamt :=NVL (TO_CHAR (v_avail_opening_balance, '99999999999999990.99'), 0.00);
      v_currcode:='USD';

      BEGIN
        INSERT INTO transaction_log (
            transaction_sqid,
            product_id,
            delivery_channel,
            transaction_code,
            msg_type,
            is_financial,
            cr_dr_flag,
            transaction_desc,
            response_id,
            error_msg,
            transaction_status,
            terminal_id,
            rrn,
            card_number,
            proxy_number,
            transaction_date,
            transaction_time,
            transaction_timezone,
            partial_preauth_ind,
            auth_id,
            transaction_amount,
            auth_amount,
            tranfee_amount,
            tran_reverse_flag,
            reversal_code,
            ins_date,
            last_upd_date,
            tran_curr,
            orgnl_card_nbr,
            orgnl_rrn,
            orgnl_transaction_date,
            orgnl_transaction_time,
            orgnl_terminal_id,
            ledger_balance,
            account_balance,
            opening_ledger_balance,
            opening_available_balance,
            card_status,
            country_code,
            merchant_name,
            store_id,
            store_address1,
            store_address2,
            store_city,
            store_state,
            spil_fee,
            spil_upc,
            spil_merref_num,
            spil_loc_cntry,
            spil_loc_crcy,
            spil_loc_lang,
            spil_pos_entry,
            spil_pos_cond,
            BUSINESS_DATE
        ) VALUES (
            v_tran_seq_id,
            p_prod_id,
            p_delivery_channel,
            p_txn_code,
            p_msg_type,
            v_tran_type,
            v_dr_cr_flag,
            v_trans_desc,
            p_resp_code,
            p_err_msg,
            DECODE(
                p_err_msg,
                'OK',
                'C',
                'F'
            ),      --Transaction status
            p_terminalid,
            p_rrn,
            v_card_num_hash,
            p_proxynumber,
            p_trandate,
            p_trantime,
            p_tran_timezone,
            p_partial_auth,
            v_auth_id,
            p_txn_amt,
            v_total_amt,--need to check (AUTH_AMOUNT)
            v_tran_fee,
            'N',
            00,
            SYSDATE,
            SYSDATE,
            v_currcode,
            v_card_num_hash,--ORIGINAL card no
            p_rrn,   --ORIGINAL RRN
            p_trandate,
            p_trantime,
            p_terminalid,
            v_closing_ledger_balance,
            v_closing_avail_balance,
            v_ledger_opening_balance,
            v_avail_opening_balance,
            p_card_status,
            p_locale_country,
            p_merchant_name,
            p_store_id,
            p_store_address1,
            p_store_address2,
            p_store_city,
            p_store_state,
            p_fee_amount,
            p_upc,
            p_merch_refnum,
            p_locale_country,
            p_locale_currency,
            p_locale_language,
            p_pos_entrymode,
            p_pos_conditioncode,
            v_business_date
        );

    EXCEPTION
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Error while INSERTING INTO TRANSACTION LOG'
             || substr(sqlerrm,1,200);
            RAISE l_exception;  
            
    END;

            
        
        
        
        

-----update location inventory 
--
--  BEGIN
--
--  SELECT MERCHANT_ID
--  INTO v_merchant_id
--  FROM MERCHANT_PRODUCT WHERE PRODUCT_ID=p_prod_id;
--  EXCEPTION  
--    WHEN NO_DATA_FOUND THEN
--        v_resp_code := trans_const.invalid_request;
--        p_err_msg := 'Card is in an invalid state ' || substr(sqlerrm,1,255);
--        RAISE l_exception;
--    WHEN OTHERS THEN
--        v_resp_code := trans_const.invalid_request;
--        p_err_msg := 'Card is in an invalid state '|| substr(sqlerrm,1,255);
--        RAISE l_exception;
--  END;
--
--  BEGIN
--
--  SELECT LOCATION_ID 
--  INTO V_LOCATION_ID 
--  FROM LOCATION_INVENTORY WHERE PRODUCT_ID=p_prod_id AND MERCHANT_ID=v_merchant_id;
--  EXCEPTION  
--    WHEN NO_DATA_FOUND THEN
--        v_resp_code := trans_const.invalid_request;
--        p_err_msg := 'Card is in an invalid state ' || substr(sqlerrm,1,255);
--        RAISE l_exception;
--    WHEN OTHERS THEN
--        v_resp_code := trans_const.invalid_request;
--        p_err_msg := 'Card is in an invalid state '|| substr(sqlerrm,1,255);
--        RAISE l_exception;
--  END;
--
--
--    BEGIN
--
--    update location_inventory set
--    curr_inventory=curr_inventory+1
--    where product_id=p_prod_id
--    and merchant_id=v_merchant_id AND LOCATION_ID=V_LOCATION_ID;
--    EXCEPTION  
--    WHEN NO_DATA_FOUND THEN
--        v_resp_code := trans_const.invalid_request;
--        p_err_msg := 'Card is in an invalid state ' || substr(sqlerrm,1,255);
--        RAISE l_exception;
--    WHEN OTHERS THEN
--        v_resp_code := trans_const.invalid_request;
--        p_err_msg := 'Card is in an invalid state '|| substr(sqlerrm,1,255);
--        RAISE l_exception;
--    END;
--
-- --LOCATION INVENTORY UPDATE ENDS   

END;