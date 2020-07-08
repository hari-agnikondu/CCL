create or replace PROCEDURE sp_handle_suspended_credit_debit (
    p_transaction_code   IN VARCHAR2,
    p_prod_id            IN NUMBER,
    p_card_status        IN VARCHAR2,
    p_card_num_hash      IN VARCHAR2,
    p_closing_balance    IN NUMBER,
    p_dr_cr_flag         IN VARCHAR2,
    p_err_msg            OUT VARCHAR2,
    p_resp_code          OUT VARCHAR2
) AS
    v_max_card_balance   NUMBER;
    l_exception EXCEPTION;
BEGIN
   
    --getting maxCardBalance
    BEGIN
        SELECT
            prod.attributes.General.maxCardBalance
        INTO
            v_max_card_balance
        FROM
            product prod
        WHERE
            product_id = p_prod_id;

    EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.maximum_balance_limitation;
            p_err_msg := 'ERROR NO DATA FOUND WHILE GETTING MAXIMUM CARD BALANCE ON SUSPENDED CREDIT AND DEBIT '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING MAXIMUM CARD BALANCE ON SUSPENDED CREDIT AND DEBIT'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    IF
        p_dr_cr_flag = 'C'
    THEN
        IF
            p_card_status <> trans_const.suspended_credit AND p_closing_balance > nvl(v_max_card_balance,0)
        THEN
            BEGIN
                UPDATE card
                    SET
                        old_cardstat = p_card_status,
                        card_status = trans_const.suspended_credit
                WHERE
                    card_num_hash = p_card_num_hash;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_code := trans_const.system_error;
                    p_err_msg := 'ERROR WHILE UPDATING CARD STATUS ON SUSPENDED CREDIT AND DEBIT BLOCK-1'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;
        END IF;

        IF
            p_card_status = trans_const.suspended_debit AND
                p_closing_balance BETWEEN 0 AND nvl(v_max_card_balance,0)
        THEN
            BEGIN
                UPDATE card
                    SET
                        card_status = p_card_status,
                        old_cardstat = trans_const.suspended_debit --NEEd to add in card status and package const
                WHERE
                    card_num_hash = p_card_num_hash;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_code := trans_const.system_error;
                    p_err_msg := 'ERROR WHILE UPDATING CARD STATUS ON SUSPENDED CREDIT AND DEBIT BLOCK-2'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

        END IF;

    ELSIF p_dr_cr_flag = 'D' THEN
        IF
            p_card_status = trans_const.suspended_credit AND
                p_closing_balance BETWEEN 0 AND nvl(v_max_card_balance,0)
        THEN
            BEGIN
                UPDATE card
                    SET
                        card_status = p_card_status,
                        old_cardstat = trans_const.suspended_credit
                WHERE
                    card_num_hash = p_card_num_hash;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_code := trans_const.system_error;
                    p_err_msg := 'ERROR WHILE UPDATING CARD STATUS ON SUSPENDED CREDIT AND DEBIT BLOCK-3'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;
        END IF;

        IF
            p_card_status <> trans_const.suspended_debit AND p_closing_balance < 0
        THEN
            BEGIN
                UPDATE card
                    SET
                        old_cardstat = p_card_status,
                        card_status = trans_const.suspended_debit
                WHERE
                    card_num_hash = p_card_num_hash;

            EXCEPTION
                WHEN OTHERS THEN
                    p_resp_code := trans_const.system_error;
                    p_err_msg := 'ERROR WHILE UPDATING CARD STATUS ON SUSPENDED CREDIT AND DEBIT BLOCK-4'
                     || substr(sqlerrm,1,200);
                    RAISE l_exception;
            END;

        END IF;

    END IF;

    p_resp_code := trans_const.success;
    p_err_msg := 'OK';
EXCEPTION
    WHEN l_exception THEN
        ROLLBACK;
    WHEN OTHERS THEN
        p_resp_code := trans_const.system_error;
        p_err_msg := 'ERROR WHILE GETTING MAXIMUM CARD BALANCE'
         || substr(sqlerrm,1,200);
        ROLLBACK;
END;