create or replace PROCEDURE sp_authorize_transaction (
    p_prod_id                  IN NUMBER,
    v_last_txndate             DATE,
    p_mdm_id                   IN VARCHAR2,
    p_transaction_code         IN VARCHAR2,
    p_account_id               IN NUMBER,
    p_txn_amt                  IN NUMBER,
    p_partial_auth             IN VARCHAR2,
    p_delivery_channel         IN VARCHAR2,
    p_card_number              IN VARCHAR2,
    p_rrn                      IN VARCHAR2,
    p_card_status              IN VARCHAR2,
    p_card_num_hash            IN VARCHAR2,
    p_card_num_encr            IN VARCHAR2,
    p_msg_type                 IN VARCHAR2,
    p_currency_code            IN VARCHAR2,
    p_party_supported          IN VARCHAR2,
    p_dr_cr_flag               IN VARCHAR2,
    p_closing_avail_balance    OUT NUMBER,
    p_closing_ledger_balance   OUT NUMBER,
    p_resp_code                OUT VARCHAR2,
    p_err_msg                  OUT VARCHAR2,
    p_purse_id                 OUT NUMBER,
    p_avail_opening_balance    OUT NUMBER,
    p_ledger_opening_balance   OUT NUMBER,
    p_tran_fee                 OUT NUMBER,
    p_flat_fee                 OUT NUMBER,
    p_per_fee                  OUT NUMBER,
    p_min_fee                  OUT NUMBER,
    p_total_amt                OUT NUMBER,
    p_fee_condition            OUT VARCHAR2,
    p_free_txncount_flag       OUT VARCHAR2,
    p_max_txncount_flag        OUT VARCHAR2,
    p_business_date            OUT DATE
) AS

    v_purse_id                 NUMBER;
    v_avail_opening_balance    NUMBER;
    v_ledger_opening_balance   NUMBER;
    l_exception EXCEPTION;
    v_tran_fee                 NUMBER;
    v_flat_fee                 NUMBER;
    v_per_fee                  NUMBER;
    v_min_fee                  NUMBER;
    v_total_amt                NUMBER;
    v_fee_condition            VARCHAR2(1);
    v_free_txncount_flag       VARCHAR2(1);
    v_max_txncount_flag        VARCHAR2(1);
    v_resp_code                VARCHAR2(100);
    v_err_msg                  VARCHAR2(100);
    
    CURSOR c1 IS
        SELECT
            channel_code,
            transaction_code,
            message_type,
            check_name,
            auth_order
        FROM
            auth_check
        WHERE
                transaction_code = p_transaction_code
            AND
                channel_code = p_delivery_channel
            AND
                message_type = p_msg_type
        ORDER BY auth_order;

BEGIN
  --  p_err_msg := 'OK';
  --  p_resp_code := TRANS_CONST.SUCCESS;
   
   
    --DUPLICATE RRN check (common validation for all trans)
    BEGIN
        sp_dup_txn(
            p_delivery_channel,
            p_transaction_code,
            p_card_num_hash,
            p_rrn,
            v_err_msg,
            v_resp_code
        );
        p_resp_code := v_resp_code;
        p_err_msg := v_err_msg;
        IF
            p_err_msg <> 'OK' OR p_resp_code <> trans_const.success
        THEN
            p_resp_code := p_resp_code;
            p_err_msg := v_err_msg;
            RAISE l_exception;
        END IF;

    EXCEPTION
        WHEN l_exception THEN
            RAISE;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE CHECKING DUPLICATE RRN '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    ----------Getting Opening balance

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
                c.card_num_hash = p_card_num_hash;

        p_purse_id := v_purse_id;
        p_avail_opening_balance := v_avail_opening_balance;
        p_ledger_opening_balance := v_ledger_opening_balance;
    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.card_not_found;
            p_err_msg := 'Error no data as while getting available balance'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'Error while getting available balance'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    --Loop started

    BEGIN
        FOR x IN c1 LOOP
           ---- dbms_output.put_line(x.check_name);

    --sp_first_party_third_party_check
            IF
                x.check_name = 'FIRST_PARTY_THIRD_PARTY_CHECK'
            THEN
                BEGIN
                    sp_first_party_third_party_check(
                        p_prod_id,
                        p_mdm_id,
                        p_transaction_code,
                        p_delivery_channel,
                        p_msg_type,
                        p_party_supported,
                        v_err_msg,
                        v_resp_code
                    );

                    p_resp_code := v_resp_code;
                    p_err_msg := v_err_msg;
                    dbms_output.put_line('response..' || p_resp_code);
                    dbms_output.put_line('p_err_msg..' || p_err_msg);
                    IF
                        p_resp_code <> trans_const.success OR p_err_msg <> 'OK'
                    THEN
                        p_err_msg := p_err_msg;
                        RAISE l_exception;
                    END IF;

                EXCEPTION
                    WHEN l_exception THEN
                        RAISE;
                    WHEN OTHERS THEN
                        p_resp_code := trans_const.system_error;
                        p_err_msg := 'ERROR WHILE CHECKING AUTHORIZE TRANSACTION- FIRST_PARTY_THIRD_PARTY_CHECK '
                         || substr(sqlerrm,1,200);
                        RAISE l_exception;
                END;

	--sp_authorize_balance_debit    	

            ELSIF x.check_name = 'BALANCE_DEBIT' THEN
                BEGIN
                    -----dbms_output.put_line('INSIDE balance_auth_debit....');
                    sp_balance_auth_debit(
                        p_prod_id,
                        v_last_txndate,
                        p_transaction_code,
                        p_delivery_channel,
                        p_msg_type,
                        p_account_id,
                        p_purse_id,
                        p_txn_amt,
                        p_partial_auth,
                        p_currency_code,
                        p_closing_avail_balance,
                        p_closing_ledger_balance,
                        v_err_msg,
                        v_resp_code,
                        v_total_amt,
                        v_tran_fee,
                        v_flat_fee,
                        v_per_fee,
                        v_min_fee,
                        v_fee_condition,
                        v_free_txncount_flag,
                        v_max_txncount_flag
                    );

    --OUT parameters for fees calc
--

                    p_tran_fee := v_tran_fee;
                    dbms_output.put_line('DEBIT response..p_tran_fee' || p_tran_fee);
                    p_flat_fee := v_flat_fee;
                    p_per_fee := v_per_fee;
                    p_min_fee := v_min_fee;
                    p_total_amt := v_total_amt;
                    p_fee_condition := v_fee_condition;
                    p_free_txncount_flag := v_free_txncount_flag;
                    p_max_txncount_flag := v_max_txncount_flag;
                    p_resp_code := v_resp_code;
                    p_err_msg := v_err_msg;
                    dbms_output.put_line('DEBIT response..' || p_resp_code);
                    dbms_output.put_line('DEBIT p_err_msg..' || p_err_msg);
                    IF
                        p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
                    THEN
                        dbms_output.put_line('DEBIT p_err_msg..' || p_err_msg);
                        RAISE l_exception;
                    END IF;

                EXCEPTION
                    WHEN l_exception THEN
                        RAISE;
                    WHEN OTHERS THEN
                        p_resp_code := trans_const.system_error;
                        p_err_msg := 'ERROR WHILE CHECKING AUTHORIZE TRANSACTION - authorize_balance_debit '
                         || substr(sqlerrm,1,200);
                        RAISE l_exception;
                END;
            ELSIF x.check_name = 'BALANCE_LOCK' THEN
                BEGIN
                    -----dbms_output.put_line('INSIDE balance_auth_lock....');
                    sp_balance_auth_lock(
                        p_prod_id,
                        v_last_txndate,
                        p_transaction_code,
                        p_delivery_channel,
                        p_msg_type,
                        p_account_id,
                        p_purse_id,
                        p_txn_amt,
                        p_partial_auth,
                        p_currency_code,
                        p_closing_avail_balance,
                        p_closing_ledger_balance,
                        v_err_msg,
                        v_resp_code,
                        v_total_amt,
                        v_tran_fee,
                        v_flat_fee,
                        v_per_fee,
                        v_min_fee,
                        v_fee_condition,
                        v_free_txncount_flag,
                        v_max_txncount_flag
                    );

    --OUT parameters for fees calc
--

                    p_tran_fee := v_tran_fee;
                    dbms_output.put_line('LOCK response..p_tran_fee' || p_tran_fee);
                    p_flat_fee := v_flat_fee;
                    p_per_fee := v_per_fee;
                    p_min_fee := v_min_fee;
                    p_total_amt := v_total_amt;
                    p_fee_condition := v_fee_condition;
                    p_free_txncount_flag := v_free_txncount_flag;
                    p_max_txncount_flag := v_max_txncount_flag;
                    p_resp_code := v_resp_code;
                    p_err_msg := v_err_msg;
                    dbms_output.put_line('LOCK response..' || p_resp_code);
                    dbms_output.put_line('LOCK p_err_msg..' || p_err_msg);
                    IF
                        p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
                    THEN
                        dbms_output.put_line('LOCK p_err_msg..' || p_err_msg);
                        RAISE l_exception;
                    END IF;

                EXCEPTION
                    WHEN l_exception THEN
                        RAISE;
                    WHEN OTHERS THEN
                        p_resp_code := trans_const.system_error;
                        p_err_msg := 'ERROR WHILE CHECKING AUTHORIZE TRANSACTION - authorize_balance_lock '
                         || substr(sqlerrm,1,200);
                        RAISE l_exception;
                END;
            ELSIF x.check_name = 'BALANCE_UNLOCK' THEN
                BEGIN
                    -----dbms_output.put_line('INSIDE balance_auth_unlock....');
                    sp_balance_auth_unlock(
                        p_card_num_hash,
                        p_rrn,
                        p_prod_id,
                        v_last_txndate,
                        p_transaction_code,
                        p_delivery_channel,
                        p_msg_type,
                        p_account_id,
                        p_purse_id,
                        p_txn_amt,
                        p_partial_auth,
                        p_currency_code,
                        p_closing_avail_balance,
                        p_closing_ledger_balance,
                        v_err_msg,
                        v_resp_code,
                        v_total_amt,
                        v_tran_fee,
                        v_flat_fee,
                        v_per_fee,
                        v_min_fee,
                        v_fee_condition,
                        v_free_txncount_flag,
                        v_max_txncount_flag
                    );

    --OUT parameters for fees calc
--

                    p_tran_fee := v_tran_fee;
                    dbms_output.put_line('UNLOCK response..p_tran_fee' || p_tran_fee);
                    p_flat_fee := v_flat_fee;
                    p_per_fee := v_per_fee;
                    p_min_fee := v_min_fee;
                    p_total_amt := v_total_amt;
                    p_fee_condition := v_fee_condition;
                    p_free_txncount_flag := v_free_txncount_flag;
                    p_max_txncount_flag := v_max_txncount_flag;
                    p_resp_code := v_resp_code;
                    p_err_msg := v_err_msg;
                    dbms_output.put_line('UNLOCK response..' || p_resp_code);
                    dbms_output.put_line('UNLOCK p_err_msg..' || p_err_msg);
                    IF
                        p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
                    THEN
                        dbms_output.put_line('UNLOCK p_err_msg..' || p_err_msg);
                        RAISE l_exception;
                    END IF;

                EXCEPTION
                    WHEN l_exception THEN
                        RAISE;
                    WHEN OTHERS THEN
                        p_resp_code := trans_const.system_error;
                        p_err_msg := 'ERROR WHILE CHECKING AUTHORIZE TRANSACTION - authorize_balance_unlock '
                         || substr(sqlerrm,1,200);
                        RAISE l_exception;
                END;

    --sp_authorize_balance_credit 
            ELSIF x.check_name = 'BALANCE_CREDIT' THEN
                BEGIN
                    ----dbms_output.put_line('INSIDE balance_auth_credit....');
                    sp_balance_auth_credit(
                        p_prod_id,
                        v_last_txndate,
                        p_transaction_code,
                        p_delivery_channel,
                        p_msg_type,
                        p_account_id,
                        p_purse_id,
                        p_txn_amt,
                        p_partial_auth,
                        p_currency_code,
                        p_closing_avail_balance,
                        p_closing_ledger_balance,
                        v_err_msg,
                        v_resp_code,
                        v_tran_fee,
                        v_flat_fee,
                        v_per_fee,
                        v_min_fee,
                        v_total_amt,
                        v_fee_condition,
                        v_free_txncount_flag,
                        v_max_txncount_flag
                    );
                --OUT parameters for fees calc

                    p_tran_fee := v_tran_fee;
                    p_flat_fee := v_flat_fee;
                    p_per_fee := v_per_fee;
                    p_min_fee := v_min_fee;
                    p_total_amt := v_total_amt;
                    p_fee_condition := v_fee_condition;
                    p_free_txncount_flag := v_free_txncount_flag;
                    p_max_txncount_flag := v_max_txncount_flag;
                    p_resp_code := v_resp_code;
                    p_err_msg := v_err_msg;
                    IF
                        p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
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

	--sp_handle_suspended_credit_debit 
            ELSIF x.check_name = 'SUSPENDED_CREDIT_DEBIT' THEN
                BEGIN
                    sp_handle_suspended_credit_debit(
                        p_transaction_code,
                        p_prod_id,
                        p_card_status,
                        p_card_num_hash,
                        p_closing_avail_balance,
                        p_dr_cr_flag,
                        p_err_msg,
                        p_resp_code
                    );

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
                        p_err_msg := 'ERROR WHILE CHECKING AUTHORIZE TRANSACTION - suspended_credit_debit  '
                         || substr(sqlerrm,1,200);
                        RAISE l_exception;
                END;
               
           -- ELSIF x.check_name = 'CUTOFF' THEN
            END IF;
        END LOOP;
         --sp_get_cutoff

        BEGIN
            sp_get_cutoff(p_prod_id,p_business_date,p_err_msg,p_resp_code);
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

    END;

EXCEPTION
    WHEN l_exception THEN
        ROLLBACK;
    WHEN OTHERS THEN
        p_resp_code := trans_const.system_error;
        p_err_msg := 'ERROR WHILE VALIDATION PROCESS - AUTHORIZE TRANSACTION'
         || substr(sqlerrm,1,200);
        ROLLBACK;
END;