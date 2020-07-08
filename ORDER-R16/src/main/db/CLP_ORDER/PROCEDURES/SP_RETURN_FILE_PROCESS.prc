create or replace PROCEDURE           CLP_ORDER.SP_RETURN_FILE_PROCESS (
    P_ISSUERID      IN NUMBER,
    P_RES_MSG   OUT VARCHAR2
) AS

    CURSOR 
   --cur_file_data

     c1 IS
        SELECT
            ROWID row_id,
             SERIAL_NUMBER serialno,PARENT_ORDER_ID parentid,REJECT_REASON rejectreason,
                REJECT_CODE rejectcde,ERROR_DESC errorDesc,FILE_NAME filename
        FROM
            CLP_ORDER.RETURNFILE_DATA_STG WHERE ERROR_DESC='COMPLETED';

     l_SUCCESS_FAILURE_FLAG CLP_ORDER.FILEPROCESS_REJECT_REASON.SUCCESS_FAILURE_FLAG%TYPE;
     l_FILE_REJECT_CODE CLP_ORDER.FILEPROCESS_REJECT_REASON.REJECT_CODE%TYPE;
     l_SERIALEXIST VARCHAR(1) DEFAULT 'N';
     l_card_hash          CLP_TRANSACTIONAL.CARD.card_num_hash%TYPE;
     l_ORDER_ID           CLP_ORDER.ORDER_LINE_ITEM_DTL.ORDER_ID%TYPE;
     l_ORDER_LINE_ITEM_ID  CLP_ORDER.ORDER_LINE_ITEM_DTL.ORDER_ID%TYPE;
     l_bulk_coll_limit    NUMBER := 1000;
     l_PARTNER_ID     CLP_ORDER.ORDER_LINE_ITEM_DTL.PARTNER_ID%type;
     l_FILE_NAME CLP_ORDER.RETURNFILE_DATA_STG.FILE_NAME%TYPE;
     l_REJECT_CODE  CLP_ORDER.ORDER_LINE_ITEM_DTL.REJECT_CODE%TYPE;
     l_REJECT_REASON  CLP_ORDER.ORDER_LINE_ITEM_DTL.REJECT_REASON%TYPE;
     l_CARD_NUM_ENCR  CLP_TRANSACTIONAL.CARD.CARD_NUM_ENCR%TYPE;
     l_PARENT_OID CLP_ORDER.ORDER_LINE_ITEM_DTL.PARENT_OID%TYPE;
     l_PRINTER_RESPONSE CLP_ORDER.ORDER_LINE_ITEM_DTL.PRINTER_RESPONSE%TYPE;
     l_STATUS CLP_ORDER.ORDER_LINE_ITEM_DTL.STATUS%TYPE;
     l_PACKAGE_ID CLP_ORDER.ORDER_LINE_ITEM.PACKAGE_ID%TYPE;
     l_B2B_VENDOR_CNFILE_REQ CLP_CONFIGURATION.fulfillment_vendor.B2B_VENDOR_CNFILE_REQ%TYPE;
     --VP_PACKAGE_ID CLP_ORDER.ORDER_LINE_ITEM.PACKAGE_ID%TYPE;
     type_pan_array clp_order.shuffle_array_type:=clp_order.shuffle_array_type();
     l_CCF_FILE_NAME CLP_ORDER.ORDER_LINE_ITEM_DTL.CCF_FILE_NAME%TYPE;
     L_INDEX NUMBER :=0;
     l_index1 number:=0;
     L_INDEX2 NUMBER:=0;
     l_COUNT NUMBER;
     l_cnt NUMBER;
	
    TYPE type_return_file IS TABLE OF c1%rowtype;
    l_return_file_data      type_return_file;
    l_RESP_MSG  CLP_ORDER.RETURNFILE_DATA_STG.ERROR_DESC%TYPE;
    l_exception EXCEPTION;

BEGIN
    P_RES_MSG := 'OK';
    BEGIN

	  --Cursor OPENED	
        OPEN c1;
        LOOP
            FETCH c1 BULK COLLECT INTO l_return_file_data LIMIT l_bulk_coll_limit;
            EXIT WHEN l_return_file_data.count () = 0;
            FOR i IN 1..l_return_file_data.count () LOOP
                BEGIN
                    l_RESP_MSG := 'OK';
                    l_SERIALEXIST:='Y';

                    BEGIN

                       SELECT
                            C.card_num_hash,C.CARD_NUM_ENCR,
                            OLD.PARTNER_ID,OLD.ORDER_ID,
                            OLD.PARENT_OID,OLD.ORDER_LINE_ITEM_ID,
                            REJECT_CODE,REJECT_REASON,
                            PRINTER_RESPONSE,STATUS,CCF_FILE_NAME
                            INTO
                            l_card_hash,l_CARD_NUM_ENCR,
                            l_PARTNER_ID,l_ORDER_ID,
                            l_PARENT_OID,l_ORDER_LINE_ITEM_ID,
                            l_REJECT_CODE,l_REJECT_REASON,
                            l_PRINTER_RESPONSE,l_STATUS,l_CCF_FILE_NAME
                        FROM
                            CLP_ORDER.ORDER_LINE_ITEM_DTL OLD,CLP_TRANSACTIONAL.CARD C
                        WHERE
                            OLD.SERIAL_NUMBER=C.SERIAL_NUMBER
                          --  AND  C.MBR_NUMB='000' 
                            AND C.CARD_STATUS <> '9' AND

                            C.SERIAL_NUMBER = l_return_file_data(i).serialno; 


                         EXCEPTION
                            WHEN no_data_found THEN
                             l_SERIALEXIST:='N';
                            l_RESP_MSG := ' while selecting base Serial number not found';
                            --RAISE l_exception;
                             WHEN OTHERS THEN
                            l_RESP_MSG := 'Error while selecting Serial number'
                             || substr(sqlerrm,1,200);
                           -- RAISE l_exception;
                        END;
                        DBMS_OUTPUT.PUT_LINE('l_RESP_MSG = ' || l_RESP_MSG);
                        BEGIN

                                 SELECT  SUCCESS_FAILURE_FLAG,REJECT_CODE
                                 INTO l_SUCCESS_FAILURE_FLAG,l_FILE_REJECT_CODE
                                 FROM CLP_ORDER.FILEPROCESS_REJECT_REASON 
                                 WHERE UPPER(REJECT_REASON)=UPPER(l_return_file_data(i).rejectreason);
                             exception
                                when no_data_found then
                                      l_SUCCESS_FAILURE_FLAG :='N';
                                      l_return_file_data(i).rejectreason:='Rejected Reason'||'-'||l_return_file_data(i).rejectreason;
                                when others then
                                     l_RESP_MSG:='Error while getting reject reason code'||substr(sqlerrm,1,200);
                                    --  raise l_exception;


                        END;

                     if upper(l_PRINTER_RESPONSE)  like 'FAILED%'  then
                         l_RESP_MSG:=l_PRINTER_RESPONSE;
                      end if;
                      
                      if l_CCF_FILE_NAME is null then 
                       l_RESP_MSG:='CCF File process not done';
                      end if;
                      
                DBMS_OUTPUT.PUT_LINE('l_SERIALEXIST = ' || l_SERIALEXIST);
                DBMS_OUTPUT.PUT_LINE('l_SUCCESS_FAILURE_FLAG = ' || l_SUCCESS_FAILURE_FLAG);
                DBMS_OUTPUT.PUT_LINE('l_return_file_data(i).rejectcde = ' || l_return_file_data(i).rejectcde);
                      IF l_SERIALEXIST='Y' AND l_RESP_MSG='OK' THEN

                        BEGIN 
                         DBMS_OUTPUT.PUT_LINE('l_return_file_data(i).serialno = ' || l_return_file_data(i).serialno);
                            UPDATE CLP_ORDER.ORDER_LINE_ITEM_DTL  SET REJECT_CODE=l_return_file_data(i).rejectcde,
                            REJECT_REASON=l_return_file_data(i).rejectreason,
                            PRINTER_RESPONSE=decode(l_SUCCESS_FAILURE_FLAG,'Y','SUCCESS'||'-'||l_return_file_data(i).rejectreason,'FAILED'||'-'||l_return_file_data(i).rejectreason)
                            WHERE SERIAL_NUMBER=l_return_file_data(i).serialno; 
                            --and  PARENT_OID=l_return_file_data(i).parentid;
                            exception
                                WHEN OTHERS THEN
                                  l_RESP_MSG:='Error while updating vms_line_item_dtl'||substr(sqlerrm,1,200);
                                 -- raise l_exception;
                        END;




                        BEGIN 

                         INSERT INTO CLP_ORDER.RETURNFILE_DATA(FILE_NAME,CUSTOMER_DESC,SHIP_SUFFIX_NO,PARENT_ORDER_ID,CHILD_ORDER_ID,
                        SERIAL_NUMBER,REJECT_CODE,REJECT_REASON,FILE_DATE,CARD_TYPE,CLIENT_ORDER_ID,
                        PROCESS_FLAG,ROW_ID,ERROR_DESC,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE,ORDER_LINE_ITEM_ID) 
                        SELECT FILE_NAME,CUSTOMER_DESC,SHIP_SUFFIX_NO,PARENT_ORDER_ID,CHILD_ORDER_ID,
                        SERIAL_NUMBER,REJECT_CODE,REJECT_REASON,FILE_DATE,CARD_TYPE,CLIENT_ORDER_ID,
                        PROCESS_FLAG,ROW_ID,ERROR_DESC,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE,
                        (SELECT ORDER_LINE_ITEM_ID FROM CLP_ORDER.ORDER_LINE_ITEM_DTL WHERE SERIAL_NUMBER=l_return_file_data(i).serialno) 
                        FROM CLP_ORDER.RETURNFILE_DATA_STG WHERE SERIAL_NUMBER=l_return_file_data(i).serialno;
                          EXCEPTION    
                          WHEN OTHERS THEN
                              l_RESP_MSG:='Error while moving data from RETURNFILE_DATA_TEMP to
                             RETURNFILE_DATA table'||substr(sqlerrm,1,200);
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
                    
                    

                    IF l_RESP_MSG <> 'OK' THEN
                    BEGIN

                          INSERT INTO CLP_ORDER.RETURNFILE_ERROR_DATA(
                           FILE_NAME, 
                           PARENT_ORDER_ID, 
                           SERIAL_NUMBER, 
                           REJECT_CODE, 
                           REJECT_REASON, 
                           ERROR_MESSAGE)
                           VALUES(
                           l_return_file_data(i).filename,
                           l_return_file_data(i).parentid,
                           l_return_file_data(i).serialno,
                           l_return_file_data(i).rejectcde,
                           l_return_file_data(i).rejectreason,
                           l_RESP_MSG
                            );
                         EXCEPTION    
                          WHEN OTHERS THEN
                              l_RESP_MSG:='Error while insert data from RETURNFILE_ERROR_DATA to'
                             ||substr(sqlerrm,1,200);
                              --  RAISE l_exception;
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



    P_RES_MSG :=l_RESP_MSG;
    --------

     EXCEPTION
            WHEN l_exception THEN
                P_RES_MSG:=P_RES_MSG||' Main Exception occured '  || substr(sqlerrm,1,200);
                ROLLBACK;
  END;

    for loop_cur in (select count(case when LOWER(REJECT_REASON) in
                (select LOWER(reject_reason) from CLP_ORDER.FILEPROCESS_REJECT_REASON where
                success_failure_flag='Y' ) then 1 end ) as cnt,
                max(reject_reason) as reject_reasonorder_id,order_id,partner_id,ORDER_LINE_ITEM_ID
                from CLP_ORDER.ORDER_LINE_ITEM_DTL
                where CARD_NUM_HASH member of type_pan_array group by order_id,partner_id,ORDER_LINE_ITEM_ID)
                 loop

                     if loop_cur.cnt=0 then
                          update CLP_ORDER.order_line_item
                          set order_status='Rejected',
                          return_file_msg= loop_cur.reject_reasonorder_id
                          where order_id=loop_cur.order_id
                          and line_item_id=loop_cur.ORDER_LINE_ITEM_ID
                          and partner_id=loop_cur.partner_id;
                     else
                          update CLP_ORDER.order_line_item
                          set order_status='PRINTER_ACKNOWLEDGED',
                          return_file_msg= loop_cur.reject_reasonorder_id
                          where order_id=loop_cur.order_id
                          and line_item_id=loop_cur.ORDER_LINE_ITEM_ID
                          and partner_id=loop_cur.partner_id;
                     end if;

                     select count(1) into l_cnt
                     from CLP_ORDER.order_line_item
                     where order_status<>'Rejected'
                     and order_id=loop_cur.order_id
                         and LINE_ITEM_ID=loop_cur.ORDER_LINE_ITEM_ID
                          and partner_id=loop_cur.partner_id;


                    if l_cnt=0 then
                          update CLP_ORDER.order_details
                          set order_status='Rejected'
                          where order_id=loop_cur.order_id
                          and partner_id=loop_cur.partner_id;
                     else 
                          update CLP_ORDER.order_details
                          set order_status='PRINTER_ACKNOWLEDGED'
                          where order_id=loop_cur.order_id
                          and partner_id=loop_cur.partner_id;
                    end if;

                 END LOOP;


                BEGIN 

                         INSERT INTO CLP_ORDER.RETURNFILE_ERROR_DATA(FILE_NAME,PARENT_ORDER_ID,CHILD_ORDER_ID,SERIAL_NUMBER,REJECT_CODE,
                         REJECT_REASON,ERROR_MESSAGE) 
                        SELECT  FILE_NAME, 
                           PARENT_ORDER_ID, CHILD_ORDER_ID,
                           SERIAL_NUMBER, 
                           REJECT_CODE, 
                           REJECT_REASON, 
                           ERROR_DESC 
                        FROM CLP_ORDER.RETURNFILE_DATA_STG WHERE ERROR_DESC <>'COMPLETED';
                          EXCEPTION    
                          WHEN OTHERS THEN
                              P_RES_MSG:='Error while moving data from RETURNFILE_DATA_TEMP to
                               RETURNFILE_ERROR_DATAtable'||substr(sqlerrm,1,200);
                                --RAISE l_exception;
                        END;


END;
/
SHOW ERRORS;

--------------------------------------------------------------------------------------------------------------------------------