create or replace PROCEDURE sp_balance_auth_credit (
    p_prod_id                  IN NUMBER,
    p_last_txndate             DATE,
    p_transaction_code         IN VARCHAR2,
    p_delivery_channel         IN VARCHAR2,
    p_msgtype                  IN VARCHAR2,
    p_account_id               IN NUMBER,
    p_purse_id                 IN NUMBER,
    p_txn_amt                  IN NUMBER,
    p_partial_auth             IN VARCHAR2,
    p_currency_code            IN VARCHAR2,
    p_closing_avail_balance    OUT NUMBER,
    p_closing_ledger_balance   OUT NUMBER,
    p_err_msg                  OUT VARCHAR2,
    p_resp_code                OUT VARCHAR2,
    p_tran_fee                 OUT NUMBER,
    p_flat_fee                 OUT NUMBER,
    p_per_fee                  OUT NUMBER,
    p_min_fee                  OUT NUMBER,
    p_total_amt                OUT NUMBER,
    p_fee_condition            OUT VARCHAR2,
    p_free_txncount_flag       OUT VARCHAR2,
    p_max_txncount_flag        OUT VARCHAR2
) AS

    v_ledger_balance       NUMBER;
    v_available_balance    NUMBER;
    v_auth_amt             NUMBER;
    --v_closing_ledger_balance NUMBER;
    v_tran_fee             NUMBER;
    v_flat_fee             NUMBER;
    v_per_fee              NUMBER;
    v_min_fee              NUMBER;
    v_total_amt            NUMBER;
    v_fee_condition        VARCHAR2(50);
    v_free_txncount_flag   VARCHAR2(50);
    v_max_txncount_flag    VARCHAR2(50);
    l_exception EXCEPTION;
    v_opening_balance      NUMBER;
    v_purse_id             NUMBER;
    v_curr_id              NUMBER;
    v_maxcardbalance       NUMBER;
    v_resp_code            VARCHAR2(50);
    v_err_msg              VARCHAR2(100);
BEGIN
    BEGIN
   		--Fetching available_balance
        SELECT
            a.purse_id,
            nvl(a.available_balance,0),
            nvl(a.ledger_balance,0),
            a.currency_code
        INTO
            v_purse_id,v_available_balance,v_ledger_balance,v_curr_id
        FROM
            account_purse a,
            product_purse b
        WHERE
                a.product_id = b.product_id
            AND
                a.purse_id = b.purse_id
            AND
                b.is_default = 'Y'
            AND
                a.account_id = p_account_id
            AND
                a.currency_code = (
                    SELECT
                        currency_id
                    FROM
                        currency_code
                    WHERE
                        currency_code = p_currency_code
                );

    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.card_not_found;
            p_err_msg := 'ERROR NO DATA FOUND WHILE GETTING BALANCE ON CREDIT'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING BALANCE ON CREDIT'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    v_auth_amt := p_txn_amt;
   dbms_output.put_line('OUTside Fee'||v_auth_amt);
    --FEE transaction
    BEGIN
    dbms_output.put_line('INside Fee'||v_auth_amt);
        spil_trans_fee_calc(
            p_prod_id,
            p_transaction_code,
            p_delivery_channel,
            p_msgtype,
            p_txn_amt,
            p_account_id,
            p_last_txndate,
            p_resp_code,
            p_err_msg,
            p_tran_fee,
            p_flat_fee,
            p_per_fee,
            p_min_fee,
            p_fee_condition,
            p_free_txncount_flag,
            p_max_txncount_flag
        );
--                    p_tran_fee := v_tran_fee;
--                    p_flat_fee := v_flat_fee;
--                    p_per_fee := v_per_fee;
--                    p_min_fee := v_min_fee;

        v_total_amt := p_total_amt;
--                    p_fee_condition := v_fee_condition;
--                    p_free_txncount_flag := v_free_txncount_flag;
--                    p_max_txncount_flag := v_max_txncount_flag;
                    ---p_resp_code := v_resp_code;
                    ---p_err_msg := v_err_msg;
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
            p_err_msg := 'ERROR WHILE CHECKING BALANCE AUTH CREDIT- FEE CALC '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;
    --calculating closing avail and ledger balanace
      v_total_amt := v_auth_amt - p_tran_fee;
      p_closing_avail_balance := v_available_balance + v_total_amt;
      p_closing_ledger_balance := v_ledger_balance + v_total_amt;
      
      
    --selecting maxcardbalance from product

    BEGIN
        SELECT
            po.attributes.General.maxCardBalance
        INTO
            v_maxcardbalance
        FROM
            product po
        WHERE
            po.product_id = p_prod_id;
          
        IF
            p_closing_avail_balance > nvl(v_maxcardbalance,0)
        THEN
            p_resp_code := trans_const.maximum_balance_limitation;
            p_err_msg := 'ERROR WHILE MAXIMUM BALANCE LIMITATION exceeds';
            RAISE l_exception;
        END IF;

    EXCEPTION
        WHEN l_exception THEN
            RAISE;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING BALANCE ON CREDIT'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;
    --Updating LEDGER_BALANCE and AVAILABLE_BALANCE

    BEGIN
        UPDATE account_purse
            SET
                ledger_balance = p_closing_ledger_balance,
                available_balance = p_closing_avail_balance
        WHERE
                account_id = p_account_id
            AND
                purse_id = p_purse_id;

    EXCEPTION
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Updating LEDGER_BALANCE and AVAILABLE_BALANCE ON CREDIT '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;
    
        --OUT parameters for fees calc

    p_total_amt := v_auth_amt;
EXCEPTION
    WHEN l_exception THEN
        ROLLBACK;
    WHEN OTHERS THEN
        p_resp_code := trans_const.system_error;
        p_err_msg := 'ERROR WHILE BALANCE AUTHENTICATION CREDIT'
         || substr(sqlerrm,1,200);
END;