  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_RESPONSE_FILE_PROCESS" (
    P_ISSUER_ID      in NUMBER,
    P_RES_MSG   OUT VARCHAR2
) AS

    CURSOR
     c1 IS
        SELECT
            ROWID row_id,
            magic_number,
            serial_number,
            TRACKING_NUMBER,
            FILE_DATE
	  --VIRTUALACCT_NUMBER
        FROM
            RESPONSEFILE_DATA_STG WHERE ERROR_DESC='COMPLETED';

    v_card_hash          card.card_num_hash%TYPE;
    v_card_encr          card.card_num_encr%TYPE;
    V_ORDER_ID           ORDER_LINE_ITEM_DTL.ORDER_ID%TYPE;
   V_ORDER_LINE_ITEM_ID  ORDER_LINE_ITEM_DTL.ORDER_ID%TYPE;
     v_bulk_coll_limit    NUMBER := 1000;
     V_PARTNER_ID     ORDER_LINE_ITEM_DTL.PARTNER_ID%type;
     V_FILE_NAME RESPONSEFILE_DATA.FILE_NAME%TYPE;
     V_COUNT NUMBER;
    TYPE type_res_file IS
        TABLE OF c1%rowtype;
    v_res_file_data      type_res_file;
    V_RESP_MSG  RESPONSEFILE_DATA.ERROR_DESC%TYPE;
    V_RES_CODE RESPONSEFILE_DATA.ERROR_DESC%TYPE;
    type_pan_array  shuffle_array_type:=shuffle_array_type();
     L_INDEX NUMBER :=0;
     l_index1 number:=0;
      L_INDEX2 NUMBER:=0;
    l_exception EXCEPTION;

BEGIN
    P_RES_MSG := 'OK';
    BEGIN

	  --Cursor OPENED
        OPEN c1;
        LOOP
            FETCH c1 BULK COLLECT INTO v_res_file_data LIMIT v_bulk_coll_limit;
            EXIT WHEN v_res_file_data.count () = 0;
            FOR i IN 1..v_res_file_data.count () LOOP
                BEGIN
                    V_RESP_MSG := 'OK';

                    BEGIN
                        SELECT
                            C.card_num_hash,C.CARD_NUM_ENCR,OLD.ORDER_ID,OLD.ORDER_LINE_ITEM_ID,OLD.PARTNER_ID
                            INTO
                            v_card_hash,v_card_encr,V_ORDER_ID,V_ORDER_LINE_ITEM_ID,V_PARTNER_ID
                        FROM
                            ORDER_LINE_ITEM_DTL OLD,CARD C
                        WHERE
                            OLD.SERIAL_NUMBER=C.SERIAL_NUMBER
                            --AND  C.MBR_NUMB='000'
                            AND C.CARD_STATUS <> '9' AND

                            C.SERIAL_NUMBER = v_res_file_data(i).serial_number;


                         EXCEPTION
                            WHEN no_data_found THEN
                            V_RESP_MSG := 'Serial number not found inactive card with application status printer sent';
                            --RAISE l_exception;
                             WHEN OTHERS THEN
                            V_RESP_MSG := 'Error while selecting Serial number'
                             || substr(sqlerrm,1,200);
                           -- RAISE l_exception;
                        END;

                    IF
                            v_res_file_data(i).serial_number IS NOT NULL
                        AND v_res_file_data(i).serial_number <> ' '
                         THEN
                    BEGIN
                            SELECT
                               COUNT(*)

                                INTO V_COUNT

                            FROM
                                ORDER_LINE_ITEM_DTL OLD,CARD C
                            WHERE
                                OLD.SERIAL_NUMBER=C.SERIAL_NUMBER AND
                                C.SERIAL_NUMBER = v_res_file_data(i).serial_number;

                                IF
                                   -- V_COUNT > 0
                                    V_COUNT <> 1
                                THEN
                                    V_RESP_MSG := 'Duplicate Serial Number not allowed / No Data found'
                                     || v_res_file_data(i).serial_number;

                                END IF;

                                EXCEPTION
                                    WHEN l_exception THEN
                                        RAISE;
                                    WHEN OTHERS THEN
                                        P_RES_MSG := 'Error while selecting serial number count-'
                                         || substr(sqlerrm,1,200);
                                --RAISE l_exception;
                        END;
                        ELSE
                                V_RESP_MSG := 'Serial number is empty '
                                 || v_res_file_data(i).serial_number;
                               -- RAISE l_exception;
                    END IF;
                     BEGIN
                            type_pan_array.extend;
                            l_index:=l_index+1;
                            type_pan_array(l_index):=v_card_hash;
                        exception
                            when others then
                            V_RESP_MSG:='Error while adding pan to type_pan_array'||substr(sqlerrm,1,200);
                      --raise exp_reject_record;
                    END;

--                     sp_log_cardstat_chnge(
--                        v_card_hash,
--                        v_card_encr,
--                        NULL,
--                        '009',
--                        NULL,
--                        NULL,
--                        NULL,
--                       V_RES_CODE,
--                        V_RESP_MSG
--                    );

                    IF V_RESP_MSG = 'OK'  THEN
                    BEGIN
                        UPDATE ORDER_LINE_ITEM_DTL
                            SET
                                STATUS='SHIPPED',
                                TRACKING_NBR=v_res_file_data(i).TRACKING_NUMBER,
                                SHIPPING_DATE=v_res_file_data(i).FILE_DATE

                        WHERE
                            SERIAL_NUMBER = v_res_file_data(i).serial_number;

                        IF
                            SQL%rowcount = 0
                        THEN
                            V_RESP_MSG := 'status is  not updated for '||v_res_file_data(i).serial_number;

                        END IF;
                    EXCEPTION
                        WHEN l_exception THEN
                            RAISE;
                        WHEN OTHERS THEN
                            P_RES_MSG := 'Error while updating serial number-'
                             || substr(sqlerrm,1,200);
                          --  RAISE l_exception;
                    END;

                     BEGIN
                        UPDATE CARD
                            SET
                                CARD_STATUS='0'
                        WHERE
                            CARD_NUM_HASH = v_card_hash;

                        IF
                            SQL%rowcount = 0
                        THEN
                            V_RESP_MSG := 'status is  not updated for '||v_res_file_data(i).serial_number;

                        END IF;
                    EXCEPTION
                        WHEN l_exception THEN
                            RAISE;
                        WHEN OTHERS THEN
                            P_RES_MSG := 'Error while updating serial number-'
                             || substr(sqlerrm,1,200);
                          --  RAISE l_exception;
                    END;


                    BEGIN
                     insert into RESPONSEFILE_DATA(BATCH_NUMBER,CARRIER,
                          CASE_NUMBER,ORDER_ID,CITY,FILE_DATE,DC_ID,
                          ERROR_DESC,FILE_NAME,INS_DATE,
                          INS_USER,LAST_UPD_DATE,LAST_UPD_USER,MAGIC_NUMBER,
                          MERCHANT_ID,MERCHANT_NAME,PALLET_NUMBER,
                          PARENT_SERIAL_NUMBER,PROCESS_FLAG,
                          PROD_ID,ROW_ID,SERIAL_NUMBER,SHIP_TO,STATE,
                          STATUS,STORELOCATIONID,STREET_ADDR1,STREET_ADDR2,
                          TRACKING_NUMBER,ZIP) select BATCH_NUMBER,CARRIER,
                          CASE_NUMBER,ORDER_ID,CITY,FILE_DATE,DC_ID,
                          ERROR_DESC,FILE_NAME,INS_DATE,
                          INS_USER,LAST_UPD_DATE,LAST_UPD_USER,MAGIC_NUMBER,
                          MERCHANT_ID,MERCHANT_NAME,PALLET_NUMBER,
                          PARENT_SERIAL_NUMBER,PROCESS_FLAG,
                          PROD_ID,ROW_ID,SERIAL_NUMBER,SHIP_TO,STATE,
                          STATUS,STORELOCATIONID,STREET_ADDR1,STREET_ADDR2,
                          TRACKING_NUMBER,ZIP from RESPONSEFILE_DATA_STG
                          where serial_number=v_res_file_data(i).serial_number;
                         EXCEPTION
                         WHEN OTHERS THEN
                            V_RESP_MSG := 'Error while updating serial number-'
                             || substr(sqlerrm,1,200);
                           -- RAISE l_exception;
                      END;

                       BEGIN
                           SELECT COUNT(*) INTO V_COUNT FROM ORDER_UPDATE
                             WHERE ORDER_ID=V_order_id AND LINE_ITEM_ID=V_ORDER_LINE_ITEM_ID AND PARTNER_ID=v_partner_id;

                      IF  V_COUNT=0 THEN
                         INSERT INTO ORDER_UPDATE(ORDER_ID,LINE_ITEM_ID,PARTNER_ID)
                         VALUES(V_order_id,V_ORDER_LINE_ITEM_ID,V_partner_id);
                      END IF;
                      EXCEPTION
                         WHEN OTHERS THEN
                            V_RESP_MSG := 'Error while INSERTING DATA INTO GLOBAL TEMPORARY TABLE OF ORDER_UPATE'
                             || substr(sqlerrm,1,200);
                           -- RAISE l_exception;
                        END;

                    END IF;



                      IF V_RESP_MSG <> 'OK'  THEN
                      BEGIN
                        UPDATE RESPONSEFILE_DATA_STG
                        SET ERROR_DESC=V_RESP_MSG
                         where serial_number=v_res_file_data(i).serial_number;
                         exception
                         WHEN OTHERS THEN
                           V_RESP_MSG:='Error while checking RESPONSEFILE_DATA_TEMP '||substr(sqlerrm,1,200);
                     -- raise l_exception;
                      END;

                    END IF;









--------------------------



---COUNT CHECK



--------------------------

                 END;
            END LOOP;

            COMMIT;
        END LOOP;

        CLOSE c1;

    P_RES_MSG :=V_RESP_MSG;
    --------

     EXCEPTION
            WHEN l_exception THEN
                P_RES_MSG:=P_RES_MSG||' Main Exception occured '  || substr(sqlerrm,1,200);
                ROLLBACK;
  END;

   for loop_cur in (select count(case when status<>'SHIPPED' then 1 end) as cnt,
                      ORDER_ID,ORDER_LINE_ITEM_ID,PARTNER_ID
                      from ORDER_LINE_ITEM_DTL where CARD_NUM_HASH member of type_pan_array
                        group by ORDER_ID,ORDER_LINE_ITEM_ID,PARTNER_ID )
       loop
          if loop_cur.cnt=0 then
                update order_line_item
                set order_status='SHIPPED'
                where order_id=loop_cur.order_id
                and line_item_id=loop_cur.ORDER_LINE_ITEM_ID
                and partner_id=loop_cur.partner_id;
          end if;
        end loop;

        for loop_cur in (select count(case when order_status<>'Shipped' then 1 end) as cnt,order_id,
        partner_id from order_line_item
        group by order_id,partner_id)
        loop


          if loop_cur.cnt=0 then
                update order_details
                set order_status='SHIPPED'
                where order_id=loop_cur.order_id
                and partner_id=loop_cur.partner_id;
          end if;

       END LOOP;

  BEGIN
                INSERT INTO RESPONSEFILE_ERROR_DATA(TRACKING_NUMBER,
                                        SERIAL_NUMBER,
                                        ORDER_ID,
                                        FILE_NAME,
                                        FILE_DATE,
                                        ERROR_MSG) SELECT TRACKING_NUMBER,SERIAL_NUMBER,ORDER_ID,
                                        FILE_NAME,FILE_DATE,ERROR_DESC
                                        FROM RESPONSEFILE_DATA_STG WHERE ERROR_DESC <> 'COMPLETED';
                                    exception
                                    WHEN OTHERS THEN
                                     V_RESP_MSG:='Error while checking RESPONSEFILE_DATA_TEMP '||substr(sqlerrm,1,200);

    END;


END;

/
SHOW ERROR