create or replace PROCEDURE SP_SPIL_BAL_ENQRY (
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
    p_resp_code           OUT VARCHAR2,
    p_err_msg             OUT VARCHAR2,
    p_auth_id             OUT VARCHAR2,
    p_authorizedamt       OUT NUMBER
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
    v_location_id               VARCHAR2(50);
    v_business_date             DATE;
     v_partner_id                VARCHAR2(50);
    v_issuer_id                 VARCHAR2(50);		
    l_exception EXCEPTION;
BEGIN
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

    --Getting CARD last 4 digit

        v_card_last4digit := ( substr(
            p_card_number,
            length(p_card_number) - 3,
            length(p_card_number)
        ) );

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
                dbms_output.put_line('v_avail_opening_balance ..' || v_avail_opening_balance); 
                 dbms_output.put_line(' v_ledger_opening_balance  ..' ||  v_ledger_opening_balance ); 
                
   
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

            --Getting CLOSING Balance

        SELECT
            round(
                DECODE(
                    v_dr_cr_flag,
                    'D',
                    nvl(v_ledger_opening_balance,0) - nvl(p_txn_amt,0),
                    'C',
                    nvl(v_ledger_opening_balance,0) + nvl(p_txn_amt,0),
                    'NA',
                    v_ledger_opening_balance
                ),
                2
            )
        INTO
            v_closing_balance
        FROM
            dual;

--added by mukitha            
            
            p_authorizedamt:=v_closing_balance;

 --Calling sp_statements_log procedure

        IF
            p_err_msg = 'OK' AND v_dr_cr_flag <> 'NA'
        THEN
            BEGIN
                v_fee_flag := 'N';
                sp_statements_log(
                    v_card_num_hash,
                    v_card_num_encr,
                    v_ledger_opening_balance,
                    v_closing_balance,
                    p_txn_amt,
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
        END IF;

    --FLAT and PERCENT fee insert into statements log

        IF
                v_fee_condition IS NOT NULL
            AND v_fee_condition = 'A' AND p_err_msg = 'OK' AND v_dr_cr_flag <> 'NA'
        THEN
        --FLAT fees insert
            BEGIN
                v_fee_flag := 'Y';
                v_fee_closing_balance := v_closing_balance - v_flat_fee;
                v_fee_cr_dr_flag := 'D';
                sp_statements_log(
                    v_card_num_hash,
                    v_card_num_encr,
                    v_closing_balance,
                    v_fee_closing_balance,
                    v_flat_fee,
                    v_fee_cr_dr_flag,
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
                    p_err_msg := 'ERROR WHILE INSERTING STATEMENT LOG FEE FLAT AMOUNT  '
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

           --PERCENTAGE fees insert   

            BEGIN
                v_fee_flag := 'Y';
                v_fee_closing_balance := v_closing_balance - v_flat_fee;
                v_percent_closing_balance := v_fee_closing_balance - v_per_fee;
                v_fee_cr_dr_flag := 'D';
                sp_statements_log(
                    v_card_num_hash,
                    v_card_num_encr,
                    v_fee_closing_balance,
                    v_percent_closing_balance,
                    v_per_fee,
                    v_fee_cr_dr_flag,
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
                    p_err_msg := 'ERROR WHILE INSERTING STATEMENT LOG FEE PERCENT AMOUNT  '
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

        ELSE
            BEGIN
                IF
                        v_tran_fee IS NOT NULL
                    AND p_err_msg = 'OK' AND v_dr_cr_flag <> 'NA' AND v_tran_fee > 0
                THEN
                    v_fee_flag := 'Y';
                    v_fee_closing_balance := v_closing_balance - v_tran_fee;
                    v_fee_cr_dr_flag := 'D';
                    sp_statements_log(
                        v_card_num_hash,
                        v_card_num_encr,
                        v_closing_balance,
                        v_fee_closing_balance,
                        v_tran_fee,
                        v_fee_cr_dr_flag,
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

        p_auth_id := v_auth_id;
      --  v_total_amt := nvl(p_txn_amt,0);
       -- p_authorizedamt := NVL(v_total_amt,0); BY NAWAZ
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
            partner_id,
            issuer_id,
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
            p_currcode,
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
            v_partner_id,
            v_issuer_id,
            v_business_date
        );

    EXCEPTION
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Error while INSERTING INTO TRANSACTION LOG'
             || substr(sqlerrm,1,200);
    END;

    COMMIT;
END;