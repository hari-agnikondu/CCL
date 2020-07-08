create or replace PROCEDURE sp_statements_log (
    p_card_num_hash           IN VARCHAR2,
    p_card_num_encr           IN VARCHAR2,
    p_opening_balance         IN NUMBER,
    p_closing_balance         IN NUMBER,
    p_transaction_amount      IN NUMBER,
    p_credit_debit_flag       IN VARCHAR2,
    p_transaction_narration   IN VARCHAR2,
    p_last_upd_date           TIMESTAMP,
    p_ins_date                TIMESTAMP,
    p_rrn                     IN VARCHAR2,
    p_auth_id                 IN NUMBER,
    p_transaction_date        IN VARCHAR2,
    p_transaction_time        IN VARCHAR2,
    p_fee_flag                IN VARCHAR2,
    p_delivery_channel        IN VARCHAR2,
    p_transaction_code        IN VARCHAR2,
    p_account_id              IN NUMBER,
    p_to_account_id           IN VARCHAR2,
    p_merchant_name           IN VARCHAR2,
    p_merchant_city           IN VARCHAR2,
    p_merchant_state          IN VARCHAR2,
    p_card_last4digit         IN VARCHAR2,
    p_product_id              IN VARCHAR2,
    p_record_seq              IN NUMBER,
    p_purse_id                IN NUMBER,
    p_to_purse_id             IN NUMBER,
    p_transaction_sqid        IN NUMBER,
    p_business_date           DATE,
     p_store_id               IN VARCHAR2,
    p_resp_code               OUT VARCHAR2,
    p_err_msg                 OUT VARCHAR2
)
    AS
    l_exception EXCEPTION;
BEGIN
	
    --STATEMENT logs
    BEGIN
        
         dbms_output.put_line('p_closing_balance..Exception' || p_closing_balance);
        INSERT INTO statements_log (
            card_num_hash,
            card_num_encr,
            opening_balance,
            closing_balance,
            transaction_amount,
            credit_debit_flag,
            transaction_narration,
            last_upd_date,
            ins_date,
            rrn,
            auth_id,
            transaction_date,
            transaction_time,
            fee_flag,
            delivery_channel,
            transaction_code,
            account_id,
            to_account_id,
            merchant_name,
            merchant_city,
            merchant_state,
            card_last4digit,
            product_id,
            record_seq,
            purse_id,
            to_purse_id,
            transaction_sqid,
            business_date,
            store_id
        ) VALUES (
            p_card_num_hash,
            p_card_num_encr,
            p_opening_balance,
            p_closing_balance,
            p_transaction_amount,
            p_credit_debit_flag,
            p_transaction_narration,
            SYSDATE,
            SYSDATE,
            p_rrn,
            p_auth_id,             --(CREATE SEQUENCE auth_id_seq START WITH 1 increment by 1;)
            p_transaction_date,
            p_transaction_time,
            p_fee_flag,
            p_delivery_channel,
            p_transaction_code,
            p_account_id,
            p_to_account_id,
            p_merchant_name,
            p_merchant_city,
            p_merchant_state,
            p_card_last4digit,
            p_product_id,
            p_record_seq,
            p_purse_id,
            p_to_purse_id,
            p_transaction_sqid,
            p_business_date,
            p_store_id
        );

    EXCEPTION
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Problem while inserting data into statement log '
             || substr(sqlerrm,1,300);
              dbms_output.put_line('p_err_msg..Exception' || p_err_msg);
            RAISE l_exception;
    END;
     dbms_output.put_line('p_err_msg..error ' || p_err_msg);
     COMMIT;
 p_resp_code := TRANS_CONST.SUCCESS;
 p_err_msg := 'OK';
 
 EXCEPTION
       
    WHEN l_exception THEN
    ROLLBACK;
END;