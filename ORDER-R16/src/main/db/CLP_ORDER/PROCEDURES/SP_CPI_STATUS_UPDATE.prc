create or replace PROCEDURE CLP_ORDER.sp_cpi_status_update (
    p_err_msg      OUT VARCHAR2,
    prm_rows_upd   OUT NUMBER
) AS

    CURSOR 
   --cur_file_data

     c1 IS
        SELECT
            ROWID row_id,
            magic_number,
            serial_number
	  --VIRTUALACCT_NUMBER
        FROM
            clp_order.cpifile_data_temp;

    v_card_hash          card.card_num_hash%TYPE;
    v_card_encr          card.card_num_encr%TYPE;
    v_resp_code          VARCHAR2(5);
    v_serial_count       NUMBER;
    V_COUNT              NUMBER;
    v_err_msg            VARCHAR2(1000);
    v_bulk_coll_limit    NUMBER := 1000;
    v_order_id           VARCHAR2(50 CHAR) ;
    v_partner_id         NUMBER;
    v_line_item_id       VARCHAR2(100 CHAR);
    
    TYPE type_cpi_file IS
        TABLE OF c1%rowtype;
    v_cpi_file_data      type_cpi_file;
    l_exception EXCEPTION;
    v_startercard_flag   card.startercard_flag%TYPE;
    v_customer_code      card.customer_code%TYPE;
    v_account_id         card.account_id%TYPE;
    v_product_id         card.product_id%TYPE;
    v_defaultcardstatus  card.card_status%TYPE;
BEGIN
    p_err_msg := 'OK';
    BEGIN

	  --Cursor OPENED	
        OPEN c1;
        LOOP
            FETCH c1 BULK COLLECT INTO v_cpi_file_data LIMIT v_bulk_coll_limit;
            EXIT WHEN v_cpi_file_data.count () = 0;
            FOR i IN 1..v_cpi_file_data.count () LOOP
                BEGIN
                    v_err_msg := 'OK';
                    v_resp_code := trans_const.success;
                    BEGIN
                        SELECT
                            card_num_hash,
                            card_num_encr,
                            startercard_flag,
                            customer_code,
                            account_id,product_id
                        INTO
                            v_card_hash,v_card_encr,v_startercard_flag,v_customer_code,v_account_id,v_product_id
                        FROM
                            card
                        WHERE
                            proxy_number = v_cpi_file_data(i).magic_number 
                            and
                            card_status IN (
                                '99'
                            );

                    EXCEPTION
                        WHEN no_data_found THEN
                            v_err_msg := 'Proxy number not found inactive card with application status printer sent';
                            v_resp_code := trans_const.system_error;
                            RAISE l_exception;
                        WHEN OTHERS THEN
                            v_resp_code := trans_const.system_error;
                            v_err_msg := 'Error while selecting proxy number'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                        END;

                    IF
                            v_cpi_file_data(i).serial_number IS NOT NULL
                        AND v_cpi_file_data(i).serial_number <> ' '
                    THEN
                        BEGIN
                            SELECT
                                COUNT(1)
                            INTO
                                v_serial_count
                            FROM
                                card
                            WHERE
                                    proxy_number <> v_cpi_file_data(i).magic_number
                                AND
                                    serial_number = v_cpi_file_data(i).serial_number;

                            IF
                                v_serial_count <> 0
                            THEN
                                v_err_msg := 'Duplicate Serial Number not allowed for Proxy number '
                                 || v_cpi_file_data(i).magic_number;
                                RAISE l_exception;
                            END IF;

                        EXCEPTION
                            WHEN l_exception THEN
                                RAISE;
                            WHEN OTHERS THEN
                                v_err_msg := 'Error while selecting serial number count-'
                                 || substr(sqlerrm,1,200);
                                RAISE l_exception;
                        END;
                    ELSE
                        v_err_msg := 'Serial number is empty for Proxy number '
                         || v_cpi_file_data(i).magic_number;
                        RAISE l_exception;
                    END IF;
                    
                      BEGIN
        SELECT
            
            po.attributes.General.defaultCardStatus
           
        INTO
            v_defaultcardstatus
        FROM
            product po
        WHERE
            product_id = v_product_id;

       
    EXCEPTION
        WHEN no_data_found THEN
           p_err_msg := 'NO DATA FOUND DEFAULT CARD STATUS ';
        WHEN OTHERS THEN
            p_err_msg := 'ERROR WHILE GETTING DEFAULT CARD STATUS '
             || substr(sqlerrm,1,200);
            RAISE l_exception;
    END;

                    BEGIN
                        UPDATE card
                            SET
                                serial_number = v_cpi_file_data(i).serial_number,
                                card_status=v_defaultcardstatus
                     --,virtual_acc_num=v_cpi_file_data (i).VIRTUALACCT_NUMBER  (need to check)
                        WHERE
                            card_num_hash = v_card_hash;

                        IF
                            SQL%rowcount = 0
                        THEN
                            v_err_msg := 'Serial number not updated';
                            RAISE l_exception;
                        END IF;
                    EXCEPTION
                        WHEN l_exception THEN
                            RAISE;
                        WHEN OTHERS THEN
                            v_err_msg := 'Error while updating serial number-'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    END;
--------------------------
                    BEGIN 
                       SELECT ORDER_ID,PARTNER_ID,ORDER_LINE_ITEM_ID INTO V_order_id,v_partner_id,v_line_item_id FROM ORDER_LINE_ITEM_DTL 
                       WHERE CARD_NUM_HASH=v_card_hash;
                       
                    EXCEPTION
                      
                        WHEN OTHERS THEN
                            v_err_msg := 'Error while Selecting  Order_id,Line_item_id,Partner_id'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    END;
---COUNT CHECK
                  BEGIN
                     SELECT COUNT(*) INTO V_COUNT FROM ORDER_UPDATE 
                     WHERE ORDER_ID=V_order_id AND LINE_ITEM_ID=v_line_item_id AND PARTNER_ID=v_partner_id;
                   
                IF  V_COUNT=0 THEN
                    INSERT INTO ORDER_UPDATE(ORDER_ID,LINE_ITEM_ID,PARTNER_ID)
                    VALUES(V_order_id,V_line_item_id,V_partner_id);
                END IF;
                EXCEPTION
                        WHEN OTHERS THEN
                            v_err_msg := 'Error while INSERTING DATA INTO GLOBAL TEMPORARY TABLE OF ORDER_UPATE'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                END;

                 
--------------------------
			   --SN Added for stock issu performance changes

                    IF
                        v_startercard_flag = 'Y'
                    THEN
                        BEGIN
                            INSERT INTO sms_email_alert (
                                card_num_hash,
                                card_num_encr,
                                lowbal_flag,
                                negbal_flag,
                                highauthamt_flag,
                                dailybal_flag,
                                insuff_flag,
                                incorrpin_flag,
                                fast50_flag,
                                fedtax_refund_flag,
                                deppending_flag,
                                depaccepted_flag,
                                deprejected_flag,
                                ins_user,
                                ins_date
                            ) VALUES (
                                v_card_hash,
                                v_card_encr,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                1,
                                SYSDATE
                            );

                        EXCEPTION
                            WHEN OTHERS THEN
                                v_err_msg := 'Error while inserting records into SMS_EMAIL ALERT '
                                 || substr(sqlerrm,1,200);
                                RAISE l_exception;
                        END;
                    END IF;
               --EN Added for stock issu performance changes

                    BEGIN
                        UPDATE cpifile_data_temp
                            SET
                                process_flag = 'P',
                                error_desc = 'SUCCESS',
                                partner_id=v_partner_id
                        WHERE
                            ROWID = v_cpi_file_data(i).row_id;

                    EXCEPTION
                        WHEN OTHERS THEN
                            p_err_msg := 'Error while updating flag p for successful record-'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    END;

                    sp_log_cardstat_chnge(
                        v_card_hash,
                        v_card_encr,
                        NULL,
                        '009',
                        NULL,
                        NULL,
                        NULL,
                        v_resp_code,
                        v_err_msg
                    );

                    IF
                        v_resp_code <> trans_const.success AND v_err_msg <> 'OK'
                    THEN
                        --NEED to check
                        INSERT INTO status_upd_log_failure (
                            card_num_encr,
                            card_num_hash,
                            ins_date,
                            failure_reason
                        ) VALUES (
                            v_card_hash,
                            v_card_encr,
                            SYSDATE,
                            v_err_msg
                        );

                    END IF;

                EXCEPTION
                    WHEN l_exception THEN
                        UPDATE cpifile_data_temp
                            SET
                                process_flag = 'E',
                                error_desc = v_err_msg
                        WHERE
                            ROWID = v_cpi_file_data(i).row_id;

                    WHEN OTHERS THEN
                        v_err_msg := 'Error while updating flag card status-'
                         || substr(sqlerrm,1,200);
                        UPDATE cpifile_data_temp
                            SET
                                process_flag = 'E',
                                error_desc = v_err_msg
                        WHERE
                            ROWID = v_cpi_file_data(i).row_id;

                END;
            END LOOP;

          -- COMMIT;
        END LOOP;

        CLOSE c1;
    END;
    --------
    BEGIN
                        UPDATE ORDER_LINE_ITEM 
                            SET
                                ORDER_STATUS = 'SHIPPED'
                  
                        WHERE
                            (ORDER_ID,PARTNER_ID,LINE_ITEM_ID) IN 
                            (SELECT ORDER_ID,PARTNER_ID,LINE_ITEM_ID FROM ORDER_UPDATE);

                       
                    EXCEPTION
                        WHEN l_exception THEN
                            RAISE;
                        WHEN OTHERS THEN
                            v_err_msg := 'Error while updating  ORDER_LINE_ITEM Table'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    END;
                    
                    BEGIN
                        UPDATE ORDER_DETAILS 
                            SET
                                ORDER_STATUS = 'SHIPPED'                 
                        WHERE
                              (ORDER_ID,PARTNER_ID) IN 
                              (SELECT ORDER_ID,PARTNER_ID FROM ORDER_UPDATE);                      
                    EXCEPTION
                        WHEN l_exception THEN
                            RAISE;
                        WHEN OTHERS THEN
                            v_err_msg := 'Error while updating  ORDER_DETAILS Table'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    END;
    --------

    BEGIN
        INSERT INTO cpifile_data ( SELECT
            *
        FROM
            cpifile_data_temp
        );

    EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'Error while moving data to history table-'
             || substr(sqlerrm,1,200);
    END;

      --NEED to check

    BEGIN
        UPDATE batch_upload_param
            SET
                param_value = 'Y'
        WHERE
            param_key = 'CPIUPLOAD_PROCESS_STATUS';

    EXCEPTION
        WHEN OTHERS THEN
            p_err_msg := 'Error while updating cms_batchupload_param-'
             || substr(sqlerrm,1,200);
    END;
    
    COMMIT;
EXCEPTION
      WHEN l_exception THEN
       RAISE;
      WHEN OTHERS THEN
        p_err_msg := ' Main Exception '
         || substr(sqlerrm,1,200);
END;
/

---------------------------------------------------------------------------