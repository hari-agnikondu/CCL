  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_CARD_TOPUP" (
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
    p_rrn                 IN VARCHAR2,
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
    p_zip_code            IN VARCHAR2,
    P_CORRELATION_ID      IN VARCHAR2,
    p_expiry_date         IN VARCHAR2 default sysdate,
    p_resp_code           OUT VARCHAR2,
    p_err_msg             OUT VARCHAR2,
    p_auth_id             OUT VARCHAR2,
    p_authorizedamt       OUT VARCHAR2
) AS

/*****************************************************************************
	* Modified by          : Sampath Kumar L
    * Modified Date        : 13-FEB-19
    * Modified For         : In Transaction_Log Business date added.
    * Reviewer             : cramu
    * Build Number         :  R05.B3
  ******************************************************************************/

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
    --v_err_msg                   VARCHAR2(100);
	v_err_msg                   VARCHAR2(500);
    v_business_date             DATE;
    --ADDED ON 13062018
    v_account_number            VARCHAR2(20);
     v_partner_id                VARCHAR2(50);
    v_issuer_id                 VARCHAR2(50);
    v_purse                     VARCHAR2(50);
    v_pur_accountPurseId_in      NUMBER(38);
    v_pur_purseId_in             VARCHAR2(100);
    v_pur_TranAmount_in          NUMBER(38);
    v_pur_Currency_in           VARCHAR2(100);
    v_pur_SkuCode_in            VARCHAR2 (100);
    l_exception EXCEPTION;
    
  
BEGIN

    BEGIN
        p_err_msg:='OK';
        p_resp_code:=trans_const.success;
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
                p_resp_code := trans_const.CARD_PIN_ESN_INVALID;
                p_err_msg := 'Error while converting card number to HASH '
                 || substr(sqlerrm,1,200);
            --RAISE l_exception;
        END;

    --Getting encr pan

        BEGIN
            v_card_num_encr := fn_emaps_main(p_card_number);
        EXCEPTION
            WHEN OTHERS THEN
                p_resp_code := trans_const.CARD_PIN_ESN_INVALID;
                p_err_msg := 'Error while converting card number to ENCR '
                 || substr(sqlerrm,1,200);
            --RAISE l_exception;
        END;

        BEGIN
--        SELECT (CASE p.PURSE_TYPE_ID
--            WHEN 1 THEN (SELECT currency_code from currency_code where currency_id=p.CURRENCY_CODE)
--            WHEN 2 THEN 'LOYALTY'
--            WHEN 3 THEN P.UPC
--            ELSE '000'
--        END) as result INTO v_purse
--            FROM product_purse pd,PURSE p where pd.purse_id=p.purse_id and pd.product_id=p_prod_id
--            and pd.is_default='Y';
    SELECT (SELECT CURRENCY_CODE FROM CURRENCY_CODE WHERE CURRENCY_ID=AP.CURRENCY_CODE)
    INTO v_purse FROM CARD C,ACCOUNT_PURSE AP WHERE
        C.ACCOUNT_ID=AP.ACCOUNT_ID AND C.CARD_NUM_HASH=v_card_num_hash;
            EXCEPTION
              WHEN OTHERS THEN
                        p_resp_code := trans_const.system_error;
                        p_err_msg := 'ERROR WHILE GETTING PURSE ' || substr(sqlerrm,1,200);
                        RAISE l_exception;

        END;



    --Getting CARD last 4 digit

        v_card_last4digit := ( substr(
            p_card_number,
            length(p_card_number) - 3,
            length(p_card_number)
        ) );


  --ADDED ON 13062018
   --Getting Account Number from Account


        BEGIN
        select account_number
        INTO v_account_number
        from account
        where account_id=p_account_id;

        EXCEPTION
            WHEN no_data_found THEN
                p_resp_code := trans_const.system_error;
                v_trans_desc := 'Transaction type' || p_txn_code;
                p_err_msg := 'ERROR WHILE Getting ACCOUNT NUMBER';
            --RAISE l_exception;
            WHEN OTHERS THEN
                p_resp_code := trans_const.system_error;
                v_trans_desc := 'Transaction type ' || p_txn_code;
                p_err_msg := 'ERROR WHILE Getting ACCOUNT NUMBER';
            --RAISE l_exception;
        END;

     BEGIN
        SELECT
            nvl(available_balance,0),
            nvl(ledger_balance,0),
            b.purse_id
        INTO
            v_avail_opening_balance,v_ledger_opening_balance,v_purse_id
        FROM
            product_purse a,
            account_purse b,
            card c
        WHERE
                a.purse_id = b.purse_id
            AND
                a.is_default = 'Y'
            AND
                a.product_id = p_prod_id
            AND
                b.account_id = c.account_id
            AND
                b.account_id = p_account_id
            AND
                c.card_num_hash = v_card_num_hash;


    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.CARD_PIN_ESN_INVALID;
            p_err_msg := 'Error no data as while getting available balance'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Error while getting available balance'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;



        ---getting partner_id and issuer_id

BEGIN
select partner_id,issuer_id
INTO v_partner_id,v_issuer_id
from product where product_id=p_prod_id;

  EXCEPTION
            WHEN no_data_found THEN
                p_resp_code := trans_const.system_error;
                v_trans_desc := 'Transaction type' || p_txn_code;
                p_err_msg := 'ERROR WHILE Getting Partner and Issuer Details ';
            --RAISE l_exception;
            WHEN OTHERS THEN
                p_resp_code := trans_const.system_error;
                v_trans_desc := 'Transaction type ' || p_txn_code;
                p_err_msg := 'ERROR WHILE Getting Getting Partner and Issuer Details ';
            --RAISE l_exception;
        END;


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



--Last transact date and topup flag update
    IF p_err_msg='OK' THEN

          BEGIN
	 sp_balance_auth_credit(
                        p_prod_id,
                        p_last_trandate,
                        p_txn_code,
                        p_delivery_channel,
                        p_msg_type,
                        p_rrn,
                        p_merchant_name,
                        p_account_id,
                        v_purse_id,
                        p_txn_amt,
                        p_partial_auth,
                        v_purse,
                        v_closing_avail_balance,
                        v_closing_ledger_balance,
                        p_err_msg,
                        p_resp_code,
                        v_tran_fee,
                        v_flat_fee,
                        v_per_fee,
                        v_min_fee,
                        v_total_amt,
                        v_fee_condition,
                        v_free_txncount_flag,
                        v_max_txncount_flag,
                        v_pur_purseId_in,
                        v_pur_accountPurseId_in,
                         v_pur_TranAmount_in ,
                          v_pur_Currency_in ,
                          v_pur_SkuCode_in,
                         v_dr_cr_flag
                    );
                --OUT parameters for fees calc

                    IF
                        p_err_msg <> 'OK' AND v_err_msg <> trans_const.success
                    THEN
                        dbms_output.put_line('CREDIT p_err_msg..' || p_err_msg);
                        RAISE l_exception;
                    END IF;

                EXCEPTION
                    WHEN l_exception THEN
                        RAISE;
                    WHEN OTHERS THEN
                        p_resp_code := trans_const.system_error;
                        p_err_msg := 'ERROR WHILE CHECKING AUTHORIZE TRANSACTION - authorize_balance_credit '
                         || substr(sqlerrm,1,200);
                        RAISE l_exception;
                END;

     BEGIN
       IF  p_firsttime_topup='N'      THEN
                update CARD set
                FIRSTTIME_TOPUP= 'Y',
                LAST_TXNDATE=SYSDATE
                where CARD_NUM_HASH =V_CARD_NUM_HASH;
              --  AND FIRSTTIME_TOPUP='N';
        ELSE
              update CARD set
                 LAST_TXNDATE=SYSDATE
                where CARD_NUM_HASH =V_CARD_NUM_HASH;

        END IF;
        EXCEPTION
         WHEN no_data_found THEN
             p_resp_code := trans_const.CARD_PIN_ESN_INVALID;
             P_ERR_MSG   := 'CARD NOT FOUND FOR UDPATE LAST TRANSDATE OR FIRSTTIME_TOPUP FLAG ' || V_CARD_NUM_HASH;
            RAISE l_exception;
         WHEN OTHERS THEN
            RAISE l_exception;
     END;

   --Last transact date and topup flag update

   END IF;
   
   --getting the business dete based on product cutoff time. 
     BEGIN
            clp_transactional.sp_get_cutoff(p_prod_id,v_business_date,p_err_msg,p_resp_code);

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
                p_err_msg := 'ERROR WHILE CHECKING CUTOFF '
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
   
   
   




    --(CREATE SEQUENCE auth_id_seq START WITH 1 increment by 1;)

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


        v_dr_cr_flag:='C';

         IF
            p_err_msg = 'OK' AND v_dr_cr_flag <> 'NA'
        THEN
	--passing variable for business date from product cutoff time
            BEGIN
                v_fee_flag := 'N';
                v_closing_balance:=v_avail_opening_balance+p_txn_amt;
                sp_statements_log(
                    v_card_num_hash,
                    v_card_num_encr,
                    nvl(v_avail_opening_balance,0),
                    nvl(v_closing_balance,0),
                    nvl(p_txn_amt,0),
                    v_dr_cr_flag,
                    v_trans_desc,
                    v_txn_date,--passng variable for last updated date
                    v_txn_date,--passng variable for ins date
                    p_rrn,
                    v_auth_id,--(CREATE SEQUENCE auth_id_seq START WITH 1 increment by 1;)
                    p_trandate,
                    p_trantime,
                    v_fee_flag,
                    p_delivery_channel,
                    p_txn_code,
                    p_account_id,
                    p_account_id,--passng variable for to_account ID
                    p_merchant_name,
                    p_store_city,
                    p_store_state,
                    v_card_last4digit,
                    p_prod_id,
                    record_id_seq.nextval,--(CREATE SEQUENCE record_id_seq START WITH 1 increment by 1;_
                    v_purse_id,
                    v_purse_id,--passng variable for TO_PURSE ID
                    v_tran_seq_id,--passng variable for trans seq_id
                    v_business_date,--passng variable for business date
                    p_store_id,
                    p_resp_code,
                    p_err_msg
                );

                IF
                    p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
                THEN
                    RAISE l_exception;
                END IF;

            EXCEPTION
                WHEN l_exception THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_code := trans_const.system_error;
                    p_err_msg := 'ERROR WHILE INSERTING STATEMENT LOG TRANSACTION AMOUNT  '
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

            --FLAT and PERCENT fee insert into statements log

        IF
           v_fee_condition IS NOT NULL AND   v_fee_condition = 'A'
        THEN
        --FLAT fees insert
	--passing variable for business date from product cutoff time
            BEGIN
                v_fee_flag := 'Y';
                v_closing_balance:=v_avail_opening_balance+p_txn_amt;
                v_fee_closing_balance := v_closing_balance - v_flat_fee;
                v_closing_balance:=v_fee_closing_balance;
                v_fee_cr_dr_flag := 'D';
                sp_statements_log(
                    v_card_num_hash,
                    v_card_num_encr,
                    nvl(v_closing_avail_balance+p_txn_amt,0),
                    nvl(v_fee_closing_balance,0),
                    nvl(v_flat_fee,0),
                    v_fee_cr_dr_flag,
                    v_trans_desc ||' FEE',
                    v_txn_date,--passng variable for last updated date
                    v_txn_date,--passng variable for ins date
                    p_rrn,
                    v_auth_id,--(CREATE SEQUENCE auth_id_seq START WITH 1 increment by 1;)
                    p_trandate,
                    p_trantime,
                    v_fee_flag,
                    p_delivery_channel,
                    p_txn_code,
                    p_account_id,
                    p_account_id,--passng variable for to_account ID
                    p_merchant_name,
                    p_store_city,
                    p_store_state,
                    v_card_last4digit,
                    p_prod_id,
                    record_id_seq.nextval,--(CREATE SEQUENCE record_id_seq START WITH 1 increment by 1;_
                    v_purse_id,
                    v_purse_id,--passng variable for TO_PURSE ID
                    v_tran_seq_id,--passng variable for trans seq_id
                    v_business_date,--passng variable for business date
                    p_store_id,
                    p_resp_code,
                    p_err_msg
                );

                IF
                    p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
                THEN
                    RAISE l_exception;
                END IF;

            EXCEPTION
                WHEN l_exception THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_code := trans_const.system_error;
                    p_err_msg := 'ERROR WHILE INSERTING STATEMENT LOG FEE FLAT AMOUNT  '
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

           --PERCENTAGE fees insert

            BEGIN
                v_fee_flag := 'Y';
                 v_closing_balance:=v_avail_opening_balance+p_txn_amt;
                v_fee_closing_balance := v_closing_balance - v_flat_fee;
                v_percent_closing_balance := v_fee_closing_balance - v_per_fee;
                v_closing_balance:=v_fee_closing_balance;
                v_fee_cr_dr_flag := 'D';
                sp_statements_log(
                    v_card_num_hash,
                    v_card_num_encr,
                    nvl(v_fee_closing_balance,0),
                    nvl(v_percent_closing_balance,0),
                    nvl(v_per_fee,0),
                    v_fee_cr_dr_flag,
                    v_trans_desc || ' FEE',
                    v_txn_date,--passng variable for last updated date
                    v_txn_date,--passng variable for ins date
                    p_rrn,
                    v_auth_id,--(CREATE SEQUENCE auth_id_seq START WITH 1 increment by 1;)
                    p_trandate,
                    p_trantime,
                    v_fee_flag,
                    p_delivery_channel,
                    p_txn_code,
                    p_account_id,
                    p_account_id,--passng variable for to_account ID
                    p_merchant_name,
                    p_store_city,
                    p_store_state,
                    v_card_last4digit,
                    p_prod_id,
                    record_id_seq.nextval,--(CREATE SEQUENCE record_id_seq START WITH 1 increment by 1;_
                    v_purse_id,
                    v_purse_id,--passng variable for TO_PURSE ID
                    v_tran_seq_id,--passng variable for trans seq_id
                    v_business_date,--passng variable for business date
                    p_store_id,
                    p_resp_code,
                    p_err_msg
                );

                IF
                    p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
                THEN
                    RAISE l_exception;
                END IF;

            EXCEPTION
                WHEN l_exception THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_code := trans_const.system_error;
                    p_err_msg := 'ERROR WHILE INSERTING STATEMENT LOG FEE PERCENT AMOUNT  '
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

        ELSE
            BEGIN
	    --passing variable for business date from product cutoff time
              IF v_tran_fee IS NOT NULL AND p_err_msg = 'OK' AND v_dr_cr_flag <> 'NA' AND v_tran_fee > 0 THEN
                v_fee_flag := 'Y';
                v_closing_balance:=v_avail_opening_balance+p_txn_amt;
                v_fee_closing_balance := v_closing_balance - v_tran_fee;
                v_closing_balance:=v_fee_closing_balance;
                v_fee_cr_dr_flag := 'D';
                sp_statements_log(
                    v_card_num_hash,
                    v_card_num_encr,
                    nvl(v_avail_opening_balance+p_txn_amt,0),
                    nvl(v_fee_closing_balance,0),
                    nvl(v_tran_fee,0),
                    v_fee_cr_dr_flag,
                    v_trans_desc || ' FEE',
                    v_txn_date,--passng variable for last updated date
                    v_txn_date,--passng variable for ins date
                    p_rrn,
                    v_auth_id,--(CREATE SEQUENCE auth_id_seq START WITH 1 increment by 1;)
                    p_trandate,
                    p_trantime,
                    v_fee_flag,
                    p_delivery_channel,
                    p_txn_code,
                    p_account_id,
                    p_account_id,--passng variable for to_account ID
                    p_merchant_name,
                    p_store_city,
                    p_store_state,
                    v_card_last4digit,
                    p_prod_id,
                    record_id_seq.nextval,--(CREATE SEQUENCE record_id_seq START WITH 1 increment by 1;_
                    v_purse_id,
                    v_purse_id,--passng variable for TO_PURSE ID
                    v_tran_seq_id,--passng variable for trans seq_id
                    v_business_date,--passng variable for business date
                    p_store_id,
                    p_resp_code,
                    p_err_msg
                );

                IF
                    p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
                THEN
                    RAISE l_exception;
                END IF;
 END IF;
            EXCEPTION
                WHEN l_exception THEN
                    RAISE;
                WHEN OTHERS THEN
                    p_resp_code := trans_const.system_error;
                    p_err_msg := 'ERROR WHILE INSERTING STATEMENT LOG FEE PERCENT AMOUNT  '
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;
        END IF;

        END IF;





        p_auth_id := v_auth_id;
       p_authorizedamt := TRIM(TO_CHAR(NVL(v_closing_avail_balance,0), '999999999999999990.99'));
    --    p_out_currcode := p_currcode;
    EXCEPTION
         --<< MAIN EXCEPTION >>
        WHEN l_exception THEN
            ROLLBACK;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Error while card redemtion exception '
             || substr(sqlerrm,1,200);
            ROLLBACK;
    END;





	--INSERT into transaction_log table
	--removed unwanted foields like orgnl_rrn,orgnl_transaction_date,orgnl_transaction_time,orgnl_terminal_id...etc

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
            reversal_code,
            ins_date,
            last_upd_date,
            tran_curr,
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
           -- spil_fee,
            spil_upc,
            spil_merref_num,
            spil_loc_cntry,
            spil_loc_crcy,
            spil_loc_lang,
            spil_pos_entry,
            spil_pos_cond,
            TRAN_REVERSE_FLAG,
            STORE_ZIP,
            CORRELATION_ID,
            EXPIRATION_DATE,
            ACCOUNT_NUMBER,
             ACCOUNT_ID,
             PURSE_ID,
             MDM_ID,
            partner_id,
            issuer_id,
            CUSTOMER_CARD_NBR_ENCR,
            business_date
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
            ),        --Transaction status
            p_terminalid,
            p_rrn,
            v_card_num_hash,
            p_proxynumber,
            p_trandate,
            p_trantime,
            p_tran_timezone,
            p_partial_auth,
            v_auth_id,
            TRIM(TO_CHAR(NVL(p_txn_amt,0), '999999999999999990.99')),
            TRIM(TO_CHAR(NVL(p_txn_amt,0), '999999999999999990.99')),--need to check (AUTH_AMOUNT)
            TRIM(TO_CHAR(NVL(v_tran_fee,0), '999999999999999990.99')),
            00,
            SYSDATE,
            SYSDATE,
            p_currcode,
              DECODE(
                p_err_msg,
                'OK',
                TRIM(TO_CHAR(NVL(v_closing_ledger_balance,0), '999999999999999990.99')),
                0
            ),
            DECODE(
                p_err_msg,
                'OK',
                 TRIM(TO_CHAR(NVL(v_closing_avail_balance,0), '999999999999999990.99')),
                0
            ),

             TRIM(TO_CHAR(NVL(v_ledger_opening_balance,0), '999999999999999990.99')),
             TRIM(TO_CHAR(NVL(v_avail_opening_balance,0), '999999999999999990.99')),
            p_card_status,
            p_locale_country,
            p_merchant_name,
            p_store_id,
            p_store_address1,
            p_store_address2,
            p_store_city,
            p_store_state,
          --  NVL(p_fee_amount,0),
            p_upc,
            p_merch_refnum,
            p_locale_country,
            p_locale_currency,
            p_locale_language,
            p_pos_entrymode,
            p_pos_conditioncode,
            'N',
            p_zip_code,
            P_CORRELATION_ID,
            p_expiry_date,
            --added on 13062018
            v_account_number,--ACCOUNT_NUMBER,
             p_account_id,
             v_purse_id,
              NVL(p_mdm_id,0),
            v_partner_id,
            v_issuer_id,
            v_card_num_encr,
            v_business_date
        );

    EXCEPTION
        WHEN OTHERS THEN
	rollback;
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Error while INSERTING INTO TRANSACTION LOG'
             || substr(sqlerrm,1,200);
    END;

END;
/
SHOW ERRORS;
----------------------------------------------------------------------