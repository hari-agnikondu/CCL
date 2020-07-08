create or replace PROCEDURE sp_dup_txn (
    p_delivery_channel   IN VARCHAR2,
    p_transaction_code   IN VARCHAR2,
    p_card_number_hash   IN VARCHAR2,
    p_rrn                VARCHAR2,
    p_err_msg            OUT VARCHAR2,
    p_resp_code          OUT VARCHAR2
) AS
    v_count   NUMBER;
    l_exception EXCEPTION;
BEGIN
    BEGIN
        SELECT
            COUNT(1)
        INTO
            v_count
        FROM
            transaction_log
        WHERE
                delivery_channel = p_delivery_channel
            AND
                transaction_code = p_transaction_code               
            
            AND
                card_number = p_card_number_hash
            AND
                rrn = p_rrn
            AND
                msg_type = '0200'
            AND
                response_id = trans_const.success;

    EXCEPTION
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE CHECKING DUPLICATE RRN'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    IF
        v_count > 0
    THEN
        p_resp_code := trans_const.duplicate_rrn_request;
        p_err_msg := 'DUPLICATE RRN FOUND';
        RAISE l_exception;
    ELSE
        p_resp_code := trans_const.success;
        p_err_msg := 'OK';
    END IF;

EXCEPTION
    WHEN l_exception THEN
        ROLLBACK;
    WHEN OTHERS THEN
        p_err_msg := 'ERROR WHILE CHECKING DUBLICATE RRN'
         || substr(sqlerrm,1,200);
        p_resp_code := trans_const.system_error;
END;