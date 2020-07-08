  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SP_SERIALNUMBER_FILE_PROCESS" (
                                                p_issuerid in NUMBER,
                                                P_RESP_MSG OUT VARCHAR2)

                                                AS

                minserialno  SERIAL_DETAILS_STG.SERIAL_NUMBER%TYPE;
                maxserialno SERIAL_DETAILS_STG.SERIAL_NUMBER%TYPE;
                 V_COUNT NUMBER;
                 l_exception EXCEPTION;

         CURSOR C1 IS

                SELECT
                DISTINCT PRODUCT_ID productid
                FROM
                SERIAL_DETAILS_STG WHERE ERROR_DESC='COMPLETED';
       BEGIN

            BEGIN

              P_RESP_MSG:='Ok';

               FOR X  IN c1
                LOOP

            BEGIN
               SELECT
                MIN(SERIAL_NUMBER) ,MAX(SERIAL_NUMBER)
                INTO minserialno,maxserialno
                FROM
                SERIAL_DETAILS_STG  WHERE product_id=x.productid;

                 exception
                    WHEN OTHERS THEN
                      P_RESP_MSG:='Error while getting  SERIAL_DETAILS_TEMP '||substr(sqlerrm,1,200);
                      raise l_exception;

            END;


             BEGIN
                  SELECT NVL(SUM (CASE WHEN (START_SERIAL BETWEEN minserialno  AND maxserialno) OR
                   (END_SERIAL between minserialno   and maxserialno) OR
                   (minserialno   BETWEEN START_SERIAL + 1 AND END_SERIAL - 1) OR
                   (maxserialno  BETWEEN START_SERIAL + 1 AND END_SERIAL - 1)
                   THEN 1  else 0 end) ,0) into  V_COUNT from  PRODUCT_SERIAL_CONTROL ;
                 exception
                    WHEN OTHERS THEN
                      P_RESP_MSG:='Error while checking PRODUCT_SERIAL_CONTROL '||substr(sqlerrm,1,200);
                      raise l_exception;
             END;
              dbms_output.put_line('BEFORE UDPATE ERROR LOG: '||V_COUNT);
              if V_COUNT <> 0 then
                 P_RESP_MSG:='Serial Number should not be over lapped-'||minserialno ||'-'||maxserialno;
               BEGIN
                UPDATE SERIAL_DETAILS_STG
                 SET ERROR_DESC=P_RESP_MSG
                WHERE product_id=x.productid;
                exception
                WHEN OTHERS THEN
                      P_RESP_MSG:='Error while update SERIAL_DETAILS_TEMP '||substr(sqlerrm,1,200);
                      raise l_exception;
              END;


             ELSE

               begin
                insert into PRODUCT_SERIAL_CONTROL(PRODUCT_ID,START_SERIAL,END_SERIAL,SERIAL_NUMBER,SEQ_NO)
                values(x.productid,minserialno ,maxserialno,minserialno,NVL((SELECT MAX(SN.SEQ_NO)+1  FROM PRODUCT_SERIAL_CONTROL SN where SN.PRODUCT_ID=x.productid),1));

            exception
                WHEN OTHERS THEN
                P_RESP_MSG:='Error while insert PRODUCT_SERIAL_CONTROL'||substr(sqlerrm,1,200);
                --raise l_exception;
            end;



              BEGIN

                    INSERT INTO SERIAL_DETAILS(FILE_NAME,PRODUCT_ID,
                     SERIAL_NUMBER,VAN,PROCESS_FLAG,ROW_ID,ERROR_DESC,
                    INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE)
                      SELECT FILE_NAME,PRODUCT_ID,
                      SERIAL_NUMBER,VAN,PROCESS_FLAG,ROW_ID,ERROR_DESC,
                      INS_USER,sysdate,LAST_UPD_USER,sysdate
                    from SERIAL_DETAILS_STG
                     WHERE product_id=x.productid AND ERROR_DESC='COMPLETED';
                      exception
                       WHEN OTHERS THEN
                          P_RESP_MSG:='Error while moving data from SERIAL_DETAILS_STG to
                          SERIAL_DETAILS table'||substr(sqlerrm,1,200);
                          raise l_exception;

              END;
                 end if;

                END LOOP;




        END;
                BEGIN

                    INSERT INTO SERIALFILE_ERROR_DATA(FILE_NAME,PRODUCT_ID,
                     SERIAL_NUMBER,VAN,ERROR_MESSAGE,
                    INS_DATE)
                      SELECT FILE_NAME,PRODUCT_ID,
                      SERIAL_NUMBER,VAN,ERROR_DESC,
                      sysdate
                    from SERIAL_DETAILS_STG
                     WHERE  ERROR_DESC <> 'COMPLETED';
                      exception
                       WHEN OTHERS THEN
                          P_RESP_MSG:='Error while moving data from SERIAL_DETAILS_TEMP to
                          SERIAL_DETAILS table'||substr(sqlerrm,1,200);
                         -- raise l_exception;

                 END;


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
                            P_RESP_MSG := 'Error while updating  ORDER_LINE_ITEM Table'
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
                            P_RESP_MSG := 'Error while updating  ORDER_DETAILS Table'
                             || substr(sqlerrm,1,200);
                            RAISE l_exception;
                    END;
            EXCEPTION
            WHEN l_exception THEN
                P_RESP_MSG:=P_RESP_MSG||' Main Exception occured '  || substr(sqlerrm,1,200);
                ROLLBACK;

        END;

/
SHOW ERROR