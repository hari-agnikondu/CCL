create or replace PROCEDURE spil_trans_fee_calc (
    p_productid                IN VARCHAR2,
    p_transaction_code         IN VARCHAR2,
    p_delivery_channel         IN VARCHAR2,
    p_msgtype                  IN VARCHAR2,
    p_txn_amt                  IN INTEGER,
    p_accountid                IN NUMBER,
    p_last_txndate             IN DATE,
    p_resp_code                OUT VARCHAR2,
    p_err_msg                  OUT VARCHAR2,
    p_tran_fee                 OUT VARCHAR2,
    p_flat_fee_out             OUT NUMBER,
    p_per_fee_out              OUT NUMBER,
    p_min_fee_out              OUT NUMBER,
    p_fee_condition_out        OUT VARCHAR2,
    p_free_txncount_flag_out   OUT VARCHAR2,
    p_max_txncount_flag_out    OUT VARCHAR2
) IS

    v_query_str                  VARCHAR2(1000);
    v_feeamt                     NUMBER;
    v_minfeeamt                  NUMBER;
    v_maxcountfreq               VARCHAR2(100);
    v_feepercent                 INTEGER;
    v_feecondition               VARCHAR2(100);
    v_freecount                  NUMBER;
    v_maxcount                   NUMBER;
    v_freecountfreq              VARCHAR2(100);
    v_tranfee_applicable_flag    INTEGER;
    v_tran_amt                   INTEGER := 0;
    v_perc_fee                   INTEGER := 0;
    v_free_txn_out               VARCHAR2(1);
    ---v_free_fee_check_resp       VARCHAR2(1000);
    v_max_txn_out                VARCHAR2(1);
    v_max_fee_check_resp         VARCHAR2(1000);
    v_channel_transaction_code   VARCHAR2(100);
    card_attributes              CLOB;
    l_exception                  EXCEPTION;
    card_attributes_obj          json_object_t;
    attribute_group_obj          json_object_t;
BEGIN
    BEGIN
        SELECT
            (
                SELECT
                    channel_short_name
                FROM
                    delivery_channel
                WHERE
                    channel_code = p_delivery_channel
            )
             || '_'
             || channel_transaction_code
        INTO
            v_channel_transaction_code
        FROM
            delivery_channel_transaction
        WHERE
                transaction_code = p_transaction_code
            AND
                channel_code = p_delivery_channel
            AND
                message_type = p_msgtype;
      EXCEPTION
        WHEN no_data_found THEN
            p_resp_code := trans_const.card_not_found;
            p_err_msg := 'ERROR NO DATA FOUND WHILE GETTING CHANNEL SHORT NAME ON FEE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING CHANNEL SHORT NAME ON FEE'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

    BEGIN
        v_query_str := 'SELECT po.attributes."Transaction Fees".'
         || v_channel_transaction_code
         || '_feeAmt,po.attributes."Transaction Fees".'
         || v_channel_transaction_code
         || '_feeCondition,po.attributes."Transaction Fees".'
         || v_channel_transaction_code
         || '_feePercent,po.attributes."Transaction Fees".'
         || v_channel_transaction_code
         || '_minFeeAmt,po.attributes."Transaction Fees".'
         || v_channel_transaction_code
         || '_freeCount,po.attributes."Transaction Fees".'
         || v_channel_transaction_code
         || '_freeCountFreq,po.attributes."Transaction Fees".'
         || v_channel_transaction_code
         || '_maxCount,po.attributes."Transaction Fees".'
         || v_channel_transaction_code
         || '_maxCountFreq FROM product po  WHERE PRODUCT_ID=:p_productid';

        EXECUTE IMMEDIATE v_query_str INTO
            v_feeamt,v_feecondition,v_feepercent,v_minfeeamt,v_freecount,v_freecountfreq,v_maxcount,v_maxcountfreq
            USING p_productid;
        dbms_output.put_line(v_feeamt||v_feecondition||v_feepercent||v_minfeeamt||v_freecount||v_freecountfreq||v_maxcount||
   v_maxcountfreq);      

    EXCEPTION
        WHEN no_data_found THEN
        dbms_output.put_Line('no_data_found');
            p_resp_code := trans_const.card_not_found;
            p_err_msg := 'ERROR NO DATA FOUND WHILE GETTING PRODUCT LEVEL FEE ATTRIBUTES'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
        WHEN OTHERS THEN
        dbms_output.put_Line('others');
            p_resp_code := trans_const.system_error;
            p_err_msg := 'ERROR WHILE GETTING PRODUCT LEVEL FEE ATTRIBUTES'
             || substr(sqlerrm,1,200);
            RAISE l_exception;
            
               END;

    SELECT
        nvl(v_feeamt,0),
        DECODE(
            v_feecondition,
            NULL,
            'N',
            v_feecondition
        ),
        nvl(v_feepercent,0),
        nvl(v_minfeeamt,0),
        nvl(v_freecount,0),
        DECODE(
            v_freecountfreq,
            NULL,
            'NA',
            v_freecountfreq
        ),
        nvl(v_maxcount,0),
        DECODE(
            v_maxcountfreq,
            NULL,
            'NA',
            v_maxcountfreq
        )
    INTO
        v_feeamt,v_feecondition,v_feepercent,v_minfeeamt,v_freecount,v_freecountfreq,v_maxcount,v_maxcountfreq
    FROM
        dual;

 
  
    --check free count and max exceeded count

    IF
        ( v_freecount > 0 AND v_freecountfreq <> 'NA' )
    THEN
        BEGIN
            fee_free_calc.fee_freecnt_check(
                p_accountid,
                v_freecountfreq,
                v_channel_transaction_code,
                v_freecount,
                p_last_txndate,
                v_free_txn_out,
                p_resp_code,
                p_err_msg
            );

            IF
                p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
            THEN
                dbms_output.put_line('FEE FREE COUNT CHECK  ON FEE CALC p_err_msg..' || p_err_msg);
                RAISE l_exception;
            END IF;

        EXCEPTION
            WHEN l_exception THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_code := trans_const.system_error;
                p_err_msg := 'ERROR WHILE CHECKING FREE COUNT-FEE CALC '
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

        IF
            (
                v_free_txn_out IS NOT NULL
            AND v_free_txn_out = 'Y' )
        THEN
            p_tran_fee := 0;
            p_free_txncount_flag_out := v_free_txn_out;
            p_err_msg := 'OK';
            p_resp_code := trans_const.success;
            p_flat_fee_out := v_feeamt;
            p_per_fee_out := v_feepercent;
            p_min_fee_out := v_minfeeamt;
            p_fee_condition_out := v_feecondition;
            dbms_output.put_line('p_error_Free_count  ' || p_err_msg);
            return;
        END IF;

    END IF;

    IF
        ( (
            v_free_txn_out IS NULL
        OR v_free_txn_out = 'N' ) AND ( v_maxcount > 0 AND v_maxcountfreq <> 'NA' ) )
    THEN
        BEGIN
            fee_max_calc.fee_maxcnt_check(
                p_accountid,
                v_maxcountfreq,
                v_channel_transaction_code,
                v_maxcount,
                p_last_txndate,
                v_max_txn_out,
                p_resp_code,
                p_err_msg
            );
        DBMS_OUTPUT.PUT_LINE('v_max_txn_out'||v_max_txn_out);
            IF
                p_err_msg <> 'OK' AND p_resp_code <> trans_const.success
            THEN
                dbms_output.put_line('FEE MAX COUNT CHECK  ON FEE CALC p_err_msg..' || p_err_msg);
                RAISE l_exception;
            END IF;

        EXCEPTION
            WHEN l_exception THEN
                RAISE;
            WHEN OTHERS THEN
                p_resp_code := trans_const.system_error;
                p_err_msg := 'ERROR WHILE CHECKING MAX COUNT-FEE CALC '
                 || substr(sqlerrm,1,200);
                RAISE l_exception;
        END;

        IF
            ( p_err_msg = 'OK' AND
                v_max_txn_out IS NOT NULL
            AND v_max_txn_out = 'Y' )
        THEN
            p_tran_fee := 0;
            p_max_txncount_flag_out := v_max_txn_out;
            p_free_txncount_flag_out := v_free_txn_out;
            p_err_msg := 'OK';
            p_resp_code := trans_const.success;
            p_flat_fee_out := v_feeamt;
            p_per_fee_out := v_feepercent;
            p_min_fee_out := v_minfeeamt;
            p_fee_condition_out := v_feecondition;
            return;
        ELSE
      --fee has to be calculated as maxCount has not reached
            v_tranfee_applicable_flag := 1;
        END IF;

    ELSE
        v_tranfee_applicable_flag := 1;
    END IF;

    dbms_output.put_line('p_err_msg FEE ' || p_err_msg);
    dbms_output.put_line('p_resp_code FEE  ' || p_resp_code);
    dbms_output.put_line('v_tranfee_applicable_flag  ' || v_tranfee_applicable_flag);
    IF
        v_tranfee_applicable_flag = 1
    THEN
        IF
            ( v_feeamt > 0 ) OR (
                v_feepercent IS NOT NULL
            AND v_feepercent <> 0 )
        THEN
            IF
                p_txn_amt IS NULL
            THEN
                v_tran_amt := 0;
            ELSE
                v_tran_amt := p_txn_amt;
            END IF;
        ELSE
            p_tran_fee := 0;
        END IF;

        IF
            v_feecondition = 'A'
        THEN
            dbms_output.put_line('v_feeCondition  ' || v_tran_amt);
            p_tran_fee := v_tran_amt * ( v_feepercent / 100 );
            dbms_output.put_line('p_tran_fee1  ' || p_tran_fee);
            v_feepercent := p_tran_fee;
            p_tran_fee := p_tran_fee + v_feeamt;
            dbms_output.put_line('p_tran_fee2 ' || p_tran_fee);
            IF
                p_tran_fee < v_minfeeamt
            THEN
                p_tran_fee := v_minfeeamt;
                v_feecondition := 'M';
            ELSE
                v_feecondition := 'A';
            END IF;

        ELSIF v_feecondition = 'O' THEN
            dbms_output.put_line('v_feeCondition  ' || v_feecondition);
            v_perc_fee := v_tran_amt * ( v_feepercent / 100 );
            IF
                v_perc_fee > v_feeamt
            THEN
                p_tran_fee := v_perc_fee;
            ELSE
                p_tran_fee := v_feeamt;
            END IF;

        ELSIF v_feecondition = 'N' THEN
            p_tran_fee := v_feeamt;
        END IF;

    ELSE
        p_tran_fee := 0;
    END IF;
 --set out parameter

    p_flat_fee_out := v_feeamt;
    p_per_fee_out := v_feepercent;
    p_min_fee_out := v_minfeeamt;
    p_fee_condition_out := v_feecondition;
    p_max_txncount_flag_out := v_max_txn_out;
    p_free_txncount_flag_out := v_free_txn_out;
    dbms_output.put_line('p_flat_fee_out  ' || p_flat_fee_out);
    dbms_output.put_line('p_per_fee_out  ' || p_per_fee_out);
    dbms_output.put_line('p_min_fee_out  ' || p_min_fee_out);
    dbms_output.put_line('p_fee_condition_out  ' || p_fee_condition_out);
    dbms_output.put_line('p_free_txncount_flag_out  ' || p_free_txncount_flag_out);
    dbms_output.put_line('p_max_txncount_flag_out  ' || p_max_txncount_flag_out);
    --- No Free and Max count configured
    IF
            p_resp_code IS NULL
        AND
            p_err_msg IS NULL
    THEN
        p_resp_code := trans_const.success;
        p_err_msg := 'OK';
    END IF;

    dbms_output.put_line('p_err_msg FEE ' || p_err_msg);
    dbms_output.put_line('p_resp_code FEE  ' || p_resp_code);
EXCEPTION
    WHEN l_exception THEN
        ROLLBACK;
    WHEN OTHERS THEN
        p_err_msg := 'Main Excp from transaction fees calculation-'
         || substr(sqlerrm,1,200);
        ROLLBACK;
END spil_trans_fee_calc;