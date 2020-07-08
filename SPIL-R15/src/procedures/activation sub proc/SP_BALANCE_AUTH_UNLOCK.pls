create or replace PROCEDURE sp_balance_auth_unlock (
    p_card_num_hash            IN VARCHAR2,
    p_rrn                      IN VARCHAR2,
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
    p_total_amt                OUT NUMBER,
    p_tran_fee                 OUT NUMBER,
    p_flat_fee                 OUT NUMBER,
    p_per_fee                  OUT NUMBER,
    p_min_fee                  OUT NUMBER,
    p_fee_condition            OUT VARCHAR2,
    p_free_txncount_flag       OUT VARCHAR2,
    p_max_txncount_flag        OUT VARCHAR2
) AS

    v_ledger_balance       NUMBER;
    v_available_balance    NUMBER;
    v_auth_amt             NUMBER;
    v_tran_fee             NUMBER;
    v_flat_fee             NUMBER;
    v_per_fee              NUMBER;
    v_min_fee              NUMBER;
    v_total_amt            NUMBER;
    v_fee_condition        VARCHAR2(50);
    v_free_txncount_flag   VARCHAR2(50);
    v_max_txncount_flag    VARCHAR2(50);
    v_purse_id             NUMBER;
    v_curr_id              NUMBER;
    v_resp_code            VARCHAR2(50);
    v_err_msg              VARCHAR2(100);
    v_transaction_code     VARCHAR2(50);
    v_orgn_txn_amt         NUMBER;
    v_orgn_auth_amt        NUMBER;
    v_orgn_fee_amt         NUMBER;
    v_orgn_lock_amount     NUMBER;
    l_exception EXCEPTION;
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

        dbms_output.put_line('v_available_balance  ' || v_available_balance);
        dbms_output.put_line('v_ledger_balance  ' || v_ledger_balance);
    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.card_not_found;
            p_err_msg := 'ERROR NO DATA FOUND WHILE GETTING UNLOCK BALANCE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING ACCOUNT BALANCE ON UNLOCK'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    v_auth_amt := p_txn_amt;
    IF
        ( v_available_balance <= 0 OR v_ledger_balance <= 0 )
    THEN
        p_resp_code := trans_const.insufficient_funds;
        p_err_msg := 'insufficient balance';
        RAISE l_exception;
    END IF;
    
    -- Redemption Lock Transaction check

    BEGIN
        SELECT
            transaction_amount,
            auth_amount,
            tranfee_amount,
            max_fee_flag,
            free_fee_flag
        INTO
            v_orgn_txn_amt,v_orgn_auth_amt,v_orgn_fee_amt,v_max_txncount_flag,v_free_txncount_flag
        FROM
            redemption_lock
        WHERE
                lock_flag = 'Y'
            AND
                card_num_hash = p_card_num_hash
            AND
                rrn = p_rrn;

    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.original_transaction_not_found;
            p_err_msg := 'ORIGINAL TRANSACTION NOT FOUND'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING REDEMTION UNLOCK RECORD'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;    
    --FEE transaction
    
    IF
        v_free_txncount_flag = 'Y' AND v_free_txncount_flag IS NOT NULL ---a free transaction
    THEN
      
         
        BEGIN
            fee_free_calc.fee_freecnt_reverse(
                p_account_id,
                p_transaction_code,
                p_delivery_channel,
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
                p_err_msg := 'ERROR WHILE REVERSE FREE COUNT ON REVERSAL '
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
   
ELSE
    IF
        v_max_txncount_flag = 'N' AND  v_max_txncount_flag IS NOT NULL ---max has not reached
    THEN
        BEGIN
            fee_max_calc.fee_maxcnt_reverse(
                p_account_id,
                p_transaction_code,
                p_delivery_channel,
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
                p_err_msg := 'ERROR WHILE REVERSE MAX COUNT ON REVERSAL '
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;
    END IF;
 END IF;


    BEGIN
      --  v_transaction_code := '008'; -- Calculating Redemption unlock fee for Redemption lock Transaction
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
                   -- p_tran_fee := v_tran_fee;
                   -- p_flat_fee := v_flat_fee;
                   -- p_per_fee := v_per_fee;
                   -- p_min_fee := v_min_fee;
                     -- p_total_amt := v_total_amt;
                   -- p_fee_condition := v_fee_condition;
                   -- p_free_txncount_flag := v_free_txncount_flag;
                   -- p_max_txncount_flag := v_max_txncount_flag;
                   -- p_resp_code := v_resp_code;
                   -- p_err_msg := v_err_msg;

        dbms_output.put_line('p_flat_fee_out  ' || p_flat_fee);
        dbms_output.put_line('p_tran_fee  ' || p_tran_fee);
        dbms_output.put_line('p_per_fee_out  ' || p_per_fee);
        dbms_output.put_line('p_min_fee_out  ' || p_min_fee);
        dbms_output.put_line('p_fee_condition_out  ' || p_fee_condition);
        dbms_output.put_line('p_free_txncount_flag_out  ' || p_free_txncount_flag);
        dbms_output.put_line('p_max_txncount_flag_out  ' || p_max_txncount_flag);
        dbms_output.put_line('p_resp_code DEBIT  ' || p_resp_code);
        dbms_output.put_line('p_err_msg DEBIT  ' || p_err_msg);
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
            p_err_msg := 'ERROR WHILE CHECKING BALANCE AUTH UNLOCK- FEE CALC '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    dbms_output.put_line('p_tran_fee..' || p_tran_fee);
    IF
        p_txn_amt + p_tran_fee > v_orgn_auth_amt + v_orgn_fee_amt
    THEN
        p_resp_code := trans_const.invalid_request;
        p_err_msg := 'ERROR WHILE CHECKING REDEMPTION UNLOCK AMOUNT GREATER THAN LOCK AMOUNT '
         || substr(sqlerrm,1,200);
        RAISE l_exception;
    END IF;

    v_orgn_lock_amount := v_available_balance + v_orgn_auth_amt + v_orgn_fee_amt;
    IF
        p_txn_amt + p_tran_fee > v_orgn_lock_amount
    THEN
        dbms_output.put_line('p_txn_amt..' || p_txn_amt);
        dbms_output.put_line('p_tran_fee..' || p_tran_fee);
        dbms_output.put_line('v_orgn_lock_amount..' || v_orgn_lock_amount);
        IF
            p_partial_auth = 'Y'
        THEN
            v_auth_amt := v_orgn_lock_amount - p_tran_fee;
             v_total_amt := v_auth_amt + p_tran_fee;
             p_closing_avail_balance := v_orgn_lock_amount - v_total_amt;
             p_closing_ledger_balance := v_ledger_balance - v_total_amt;
            dbms_output.put_line('v_auth_amt..' || v_auth_amt);
        ELSE
            p_resp_code := trans_const.insufficient_funds;
            p_err_msg := 'insufficient balance';
            RAISE l_exception;
        END IF;

    ELSE
        v_auth_amt := p_txn_amt;
        dbms_output.put_line('v_auth_amt' || v_auth_amt);
        v_total_amt := v_auth_amt + p_tran_fee;
        p_closing_avail_balance := v_orgn_lock_amount - v_total_amt;
        p_closing_ledger_balance := v_ledger_balance - v_total_amt;
        dbms_output.put_line('v_total_Amt..' || v_total_amt);
    END IF;

    dbms_output.put_line('purse id.......' || p_purse_id);
    dbms_output.put_line('ACCOUNT.......' || p_account_id);
    dbms_output.put_line('p_closing_avail_balance.......' || p_closing_avail_balance);
    dbms_output.put_line('p_closing_ledger_balance.......' || p_closing_ledger_balance);
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

        dbms_output.put_line('v_total_Amt.......' || v_total_amt);
    EXCEPTION
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Updating LEDGER_BALANCE and AVAILABLE_BALANCEt ON BALANCE AUTH LOCK '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
            dbms_output.put_line('p_resp_code..' || p_resp_code);
            dbms_output.put_line('p_err_msg..' || p_err_msg);
    END;

    BEGIN
        UPDATE redemption_lock
            SET
                lock_flag = 'N'
        WHERE
                lock_flag = 'Y'
            AND
                card_num_hash = p_card_num_hash
            AND
                rrn = p_rrn;

        dbms_output.put_line('v_total_Amt.......' || v_total_amt);
    EXCEPTION
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Updating LOCK FLAG FOR REDEMPTION LOCK ON UNLOCK'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
            dbms_output.put_line('p_resp_code..' || p_resp_code);
            dbms_output.put_line('p_err_msg..' || p_err_msg);
    END;

    p_total_amt := v_auth_amt;
EXCEPTION
    WHEN l_exception THEN
        ROLLBACK;
    WHEN OTHERS THEN
        p_resp_code := trans_const.system_error;
        p_err_msg := 'ERROR WHILE CHECKING BALANCE AUTHENTICATION UNLOCK'
         || substr(sqlerrm,1,200);
END;