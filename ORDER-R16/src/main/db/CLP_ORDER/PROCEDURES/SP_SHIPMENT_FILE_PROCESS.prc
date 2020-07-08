create or replace PROCEDURE           CLP_ORDER.SP_SHIPMENT_FILE_PROCESS (
    P_ISSUER_ID      IN NUMBER,
    P_RES_MSG   OUT VARCHAR2
) AS

    CURSOR 
   --cur_file_data

     c1 IS
        SELECT
            ROWID row_id,
            serial_number,
            TRACKING_NUMBER,
            FILE_DATE
	  --VIRTUALACCT_NUMBER
        FROM
            CLP_ORDER.SHIPMENTFILE_DATA_STG WHERE ERROR_DESC='COMPLETED';

     l_card_hash          CLP_TRANSACTIONAL.card.card_num_hash%TYPE;
     l_ORDER_ID           CLP_ORDER.ORDER_LINE_ITEM_DTL.ORDER_ID%TYPE;
     l_ORDER_LINE_ITEM_ID  CLP_ORDER.ORDER_LINE_ITEM_DTL.ORDER_ID%TYPE;
     l_bulk_coll_limit    NUMBER := 1000;
     l_PARTNER_ID     CLP_ORDER.ORDER_LINE_ITEM_DTL.PARTNER_ID%type;
     l_FILE_NAME CLP_ORDER.SHIPMENTFILE_DATA_STG.FILE_NAME%TYPE;
     l_REJECT_CODE  CLP_ORDER.ORDER_LINE_ITEM_DTL.REJECT_CODE%TYPE;
     l_REJECT_REASON  CLP_ORDER.ORDER_LINE_ITEM_DTL.REJECT_REASON%TYPE;
     l_CARD_NUM_ENCR  CLP_TRANSACTIONAL.card.CARD_NUM_ENCR%TYPE;
     l_PARENT_OID CLP_ORDER.ORDER_LINE_ITEM_DTL.PARENT_OID%TYPE;
     l_PRINTER_RESPONSE CLP_ORDER.ORDER_LINE_ITEM_DTL.PRINTER_RESPONSE%TYPE;
     l_STATUS CLP_ORDER.ORDER_LINE_ITEM_DTL.STATUS%TYPE;
     --l_PACKAGE_ID CLP_ORDER.ORDER_LINE_ITEM.PACKAGE_ID%TYPE;
     l_B2B_VENDOR_CNFILE_REQ CLP_CONFIGURATION.fulfillment_vendor.B2B_VENDOR_CNFILE_REQ%TYPE;
     l_PACKAGE_ID CLP_ORDER.ORDER_LINE_ITEM.PACKAGE_ID%TYPE;
     type_pan_array clp_order.shuffle_array_type:=clp_order.shuffle_array_type();
     L_INDEX NUMBER :=0;
     l_index1 number:=0;
      L_INDEX2 NUMBER:=0;
    l_COUNT NUMBER;
	
    TYPE type_shipment_file IS
        TABLE OF c1%rowtype;
    l_shipment_file_data      type_shipment_file;
    l_RESP_MSG  CLP_ORDER.SHIPMENTFILE_DATA_STG.ERROR_DESC%TYPE;
    l_SUCCESS_FAILURE_FLAG CLP_ORDER.FILEPROCESS_REJECT_REASON.SUCCESS_FAILURE_FLAG%TYPE;
    l_exception EXCEPTION;

BEGIN
    P_RES_MSG := 'OK';
    BEGIN

	  --Cursor OPENED	
        OPEN c1;
        LOOP
            FETCH c1 BULK COLLECT INTO l_shipment_file_data LIMIT l_bulk_coll_limit;
            EXIT WHEN l_shipment_file_data.count () = 0;
            FOR i IN 1..l_shipment_file_data.count () LOOP
                BEGIN
                    l_RESP_MSG := 'OK';

                    BEGIN

                       SELECT
                            C.card_num_hash,C.CARD_NUM_ENCR,
                            OLD.PARTNER_ID,OLD.ORDER_ID,
                            OLD.PARENT_OID,OLD.ORDER_LINE_ITEM_ID,
                            REJECT_CODE,REJECT_REASON,
                            PRINTER_RESPONSE,STATUS
                            INTO
                            l_card_hash,l_CARD_NUM_ENCR,
                            l_PARTNER_ID,l_ORDER_ID,
                            l_PARENT_OID,l_ORDER_LINE_ITEM_ID,
                            l_REJECT_CODE,l_REJECT_REASON,
                            l_PRINTER_RESPONSE,l_STATUS
                        FROM
                            CLP_ORDER.ORDER_LINE_ITEM_DTL OLD,CLP_TRANSACTIONAL.CARD C
                        WHERE
                            OLD.SERIAL_NUMBER=C.SERIAL_NUMBER
                          --  AND  C.MBR_NUMB='000' 
                            AND C.CARD_STATUS <> '9' AND

                            C.SERIAL_NUMBER = l_shipment_file_data(i).serial_number; 


                         EXCEPTION
                            WHEN no_data_found THEN
                            l_RESP_MSG := ' while selecting base Serial number not found';
                            --RAISE l_exception;
                             WHEN OTHERS THEN
                            l_RESP_MSG := 'Error while selecting Serial number'
                             || substr(sqlerrm,1,200);
                           -- RAISE l_exception;
                        END;
                        if upper(l_STATUS)  = 'COMPLETED' or upper(l_STATUS)  = 'SHIPPED' then
                          l_RESP_MSG:='Order already Processed';
                        end if;
                        
                        

                    IF   l_PRINTER_RESPONSE IS  NULL   OR LENGTH(TRIM (l_PRINTER_RESPONSE))=0 THEN
                         l_RESP_MSG:='RETURN FILE PROCESS NOT DONE';
                    ELSIF upper(l_PRINTER_RESPONSE)  like '%FAILED%'  THEN
                         l_RESP_MSG:=l_PRINTER_RESPONSE;
                    END IF; 

                     BEGIN 
                      SELECT PACKAGE_ID INTO l_PACKAGE_ID FROM CLP_ORDER.ORDER_LINE_ITEM
                      WHERE ORDER_ID =l_ORDER_ID AND
                      PARTNER_ID=l_PARTNER_ID AND LINE_ITEM_ID=l_ORDER_LINE_ITEM_ID;
                      EXCEPTION
                            WHEN no_data_found THEN
                            l_RESP_MSG := 'While select PACKAGE_ID not found ';
                            --RAISE l_exception;
                             WHEN OTHERS THEN
                            l_RESP_MSG := 'Error while selecting PACKAGE_ID'
                             || substr(sqlerrm,1,200);
                     END;


                     BEGIN
                      select  DISTINCT p.PACKAGE_ID,F.B2B_VENDOR_CNFILE_REQ
                         into l_PACKAGE_ID,l_B2B_VENDOR_CNFILE_REQ
                      from CLP_CONFIGURATION.fulfillment_vendor f,CLP_CONFIGURATION.PACKAGE_DEFINITION p

                      WHERE p.FULFILLMENT_VENDOR_ID = f.FULFILLMENT_VENDOR_ID AND 
                        p.PACKAGE_ID=l_PACKAGE_ID;
                 EXCEPTION
                 when others then
                      l_RESP_MSG:='Error while getting B2B_VENDOR_CNFILE_REQ from
                      fulfillment_vendor'||substr(sqlerrm,1,200);

                 end;

                    IF
                            l_shipment_file_data(i).serial_number IS NOT NULL
                        AND l_shipment_file_data(i).serial_number <> ' '
                         THEN
                    BEGIN
                            SELECT
                               COUNT(*)

                                INTO l_COUNT

                            FROM
                                CLP_ORDER.ORDER_LINE_ITEM_DTL OLD,CARD C
                            WHERE
                                OLD.SERIAL_NUMBER=C.SERIAL_NUMBER AND
                                C.SERIAL_NUMBER = l_shipment_file_data(i).serial_number; 

                                IF
                                    l_COUNT > 1
                                THEN
                                    l_RESP_MSG := 'Duplicate Serial Number not allowed '
                                     || l_shipment_file_data(i).serial_number;

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
                                l_RESP_MSG := 'Serial number is empty '
                                 || l_shipment_file_data(i).serial_number;
                               -- RAISE l_exception;
                    END IF;

                --DBMS_OUTPUT.PUT_LINE('l_B2B_VENDOR_CNFILE_REQ = ' || l_B2B_VENDOR_CNFILE_REQ);
                    IF l_RESP_MSG = 'OK'  THEN
                    
                    
                    
                    BEGIN
                        UPDATE CLP_ORDER.ORDER_LINE_ITEM_DTL
                            SET
                                STATUS=decode(l_B2B_VENDOR_CNFILE_REQ,'disabled','COMPLETED','enabled','SHIPPED'),
                                TRACKING_NBR=l_shipment_file_data(i).TRACKING_NUMBER,
                                SHIPPING_DATE=l_shipment_file_data(i).FILE_DATE

                        WHERE
                            SERIAL_NUMBER = l_shipment_file_data(i).serial_number;

                        IF
                            SQL%rowcount = 0
                        THEN
                            l_RESP_MSG := 'Serial number not updated';

                        END IF;
                    EXCEPTION
                        WHEN l_exception THEN
                            RAISE;
                        WHEN OTHERS THEN
                            P_RES_MSG := 'Error while updating serial number-'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    END;
                    BEGIN 
                    
                    UPDATE CLP_TRANSACTIONAL.CARD SET CARD_STATUS=(SELECT DECODE(UPPER(SHIP_STATUS),'ACTIVE','1','INACTIVE','0',0) 
                    FROM CLP_ORDER.ORDER_DETAILS WHERE 
                    ORDER_ID=l_ORDER_ID) WHERE CARD_NUM_HASH=l_card_hash;
                     IF
                            SQL%rowcount = 0
                        THEN
                            l_RESP_MSG := 'Card status number not updated';

                        END IF;
                    EXCEPTION
                        WHEN l_exception THEN
                            RAISE;
                        WHEN OTHERS THEN
                            P_RES_MSG := 'Card status Exception -'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    
                    END;
                    
                   BEGIN
                          INSERT INTO CLP_ORDER.SHIPMENTFILE_DATA(FILE_NAME,CUSTOMER_DESC,SOURCEONE_BATCH_NO,
                          PARENT_ORDER_ID,CHILD_ORDER_ID,FILE_DATE,SERIAL_NUMBER,CARD,
                          PACKAGE_ID,CARD_TYPE,CONTACT_NAME,SHIP_TO,ADDRESS_ONE,
                          ADDRESS_TWO,CITY,STATE,ZIP,TRACKING_NUMBER,SHIP_DATE,
                          SHIPMENT_ID,SHIPMENT_METHOD,PROCESS_FLAG,ROW_ID,ERROR_DESC,
                            INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE) select FILE_NAME,CUSTOMER_DESC,SOURCEONE_BATCH_NO,
                          PARENT_ORDER_ID,CHILD_ORDER_ID,FILE_DATE,SERIAL_NUMBER,CARD,
                          PACKAGE_ID,CARD_TYPE,CONTACT_NAME,SHIP_TO,ADDRESS_ONE,
                          ADDRESS_TWO,CITY,STATE,ZIP,TRACKING_NUMBER,SHIP_DATE,
                          SHIPMENT_ID,SHIPMENT_METHOD,PROCESS_FLAG,ROW_ID,ERROR_DESC,
                            INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE from CLP_ORDER.SHIPMENTFILE_DATA_STG
                         where serial_number=l_shipment_file_data(i).serial_number;

                         EXCEPTION
                         WHEN OTHERS THEN
                            l_RESP_MSG := 'Error while updating serial number-'
                             || substr(sqlerrm,1,200);
                           -- RAISE l_exception; 
                      END; 

                       BEGIN
                           SELECT COUNT(*) INTO l_COUNT FROM CLP_ORDER.ORDER_UPDATE 
                             WHERE ORDER_ID=l_order_id AND LINE_ITEM_ID=l_ORDER_LINE_ITEM_ID AND PARTNER_ID=l_partner_id;

                      IF  l_COUNT=0 THEN
                         INSERT INTO CLP_ORDER.ORDER_UPDATE(ORDER_ID,LINE_ITEM_ID,PARTNER_ID)
                         VALUES(l_order_id,l_ORDER_LINE_ITEM_ID,l_partner_id);
                      END IF;
                      EXCEPTION
                         WHEN OTHERS THEN
                            l_RESP_MSG := 'Error while INSERTING DATA INTO GLOBAL TEMPORARY TABLE OF ORDER_UPATE'
                             || substr(sqlerrm,1,200);
                           -- RAISE l_exception;
                        END;



                    END IF;
                     BEGIN
                            type_pan_array.extend;
                            l_index:=l_index+1;
                            type_pan_array(l_index):=l_card_hash;
                        exception
                            when others then
                            l_RESP_MSG:='Error while adding pan to type_pan_array'||substr(sqlerrm,1,200);
                      --raise exp_reject_record;
                    END;


                      IF l_RESP_MSG <> 'OK'  THEN
                      BEGIN 
                        UPDATE CLP_ORDER.SHIPMENTFILE_DATA_STG
                        SET ERROR_DESC=l_RESP_MSG
                         where serial_number=l_shipment_file_data(i).serial_number;
                         exception
                         WHEN OTHERS THEN
                           l_RESP_MSG:='Error while checking RESPONSEFILE_DATA_TEMP '||substr(sqlerrm,1,200);
                     -- raise l_exception;
                      END;

                    END IF;
                 END;  
            END LOOP;

            COMMIT;
        END LOOP;

        CLOSE c1;



    P_RES_MSG :=l_RESP_MSG;
    --------

     EXCEPTION
            WHEN l_exception THEN
                P_RES_MSG:=P_RES_MSG||' Main Exception occured '  || substr(sqlerrm,1,200);
                ROLLBACK;
  END;

    for loop_cur in (select count(*) as total_count,
                      count(case when status<>'PRINTER_ACKNOWLEDGED' and status is not null then 1 end) as not_processed_count,  --status<>'CCF-GENERATED' or 
                      count(case when status='COMPLETED' then 1 end) as  complete_count,
                      count(case when status='SHIPPED' then 1 end) as shipped_count,
                     order_id,ORDER_LINE_ITEM_ID,PARTNER_ID
                     from CLP_ORDER.ORDER_LINE_ITEM_DTL where (ORDER_ID,ORDER_LINE_ITEM_ID,PARTNER_ID)in
                     (select ORDER_ID,ORDER_LINE_ITEM_ID,PARTNER_ID from CLP_ORDER.ORDER_LINE_ITEM_DTL where CARD_NUM_HASH 
                     member of type_pan_array) group by ORDER_ID,ORDER_LINE_ITEM_ID,PARTNER_ID )

       loop
       if loop_cur.total_count=loop_cur.not_processed_count then
            if loop_cur.complete_count=0 then
                  update CLP_ORDER.ORDER_LINE_ITEM
                  set order_status='SHIPPED'
                  where order_id=loop_cur.order_id
                  and LINE_ITEM_ID=loop_cur.ORDER_LINE_ITEM_ID
                  and partner_id=loop_cur.partner_id
                  and order_status<>'Rejected';
            end if;

            if loop_cur.shipped_count=0 then
                update CLP_ORDER.ORDER_LINE_ITEM
                  set order_status='COMPLETED'
                  where order_id=loop_cur.order_id
                  and line_item_id=loop_cur.ORDER_LINE_ITEM_ID
                  and partner_id=loop_cur.partner_id
                  and order_status<>'Rejected';
            end if;
      end if;

      end loop;

          for loop_cur in (select order_id,partner_id ,
          count(*) as total_count,
          count(case when order_status<>'PRINTER_ACKNOWLEDGED' then 1 end) as not_processed_count,   --or order_status<>'CCF-GENERATED' 
          count(case when order_status='COMPLETED' then 1 end) as  complete_count,
          count(case when order_status='SHIPPED' then 1 end) as shipped_count
          from CLP_ORDER.ORDER_LINE_ITEM where (order_id,line_item_id,partner_id) in    
            (select ORDER_ID,ORDER_LINE_ITEM_ID,PARTNER_ID from ORDER_LINE_ITEM_DTL where CARD_NUM_HASH 
                     member of type_pan_array) group by order_id,partner_id )

          loop
         if loop_cur.total_count=loop_cur.not_processed_count then
            if loop_cur.complete_count=0 then
                  update CLP_ORDER.order_details
                  set order_status='SHIPPED'
                  where order_id=loop_cur.order_id
                  and partner_id=loop_cur.partner_id
                  and order_status<>'Rejected';
            end if;

            if loop_cur.shipped_count=0 then
                  update CLP_ORDER.order_details
                  set order_status='COMPLETED'
                  where order_id=loop_cur.order_id
                  and partner_id=loop_cur.partner_id
                  and order_status<>'Rejected';
            end if;
        end if;

       END LOOP;


  BEGIN
                INSERT INTO CLP_ORDER.SHIPMENTFILE_ERROR_DATA(TRACKING_NO,
                                        SERIAL_NUMBER,
                                        PARENT_ORDER_ID,
                                        CHILD_ORDER_ID,
                                        SHIP_DATE,
                                        FILE_NAME,
                                        ERROR_MSG) SELECT TRACKING_NUMBER,
                                        SERIAL_NUMBER,
                                        PARENT_ORDER_ID,
                                        CHILD_ORDER_ID,
                                        SHIP_DATE,
                                        FILE_NAME,
                                        ERROR_DESC 
                                        FROM CLP_ORDER.SHIPMENTFILE_DATA_STG WHERE ERROR_DESC <> 'COMPLETED';
                                    exception
                                    WHEN OTHERS THEN
                                     P_RES_MSG:='Error while checking RESPONSEFILE_DATA_TEMP '||substr(sqlerrm,1,200);  

    END;  




END;
/
show errors;

---------------------------------------------------------------------------------------------------------------------