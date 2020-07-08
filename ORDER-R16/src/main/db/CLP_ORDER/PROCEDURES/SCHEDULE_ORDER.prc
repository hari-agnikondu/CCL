  CREATE OR REPLACE  PROCEDURE "CLP_ORDER"."SCHEDULE_ORDER" (P_ORDER_ID in VARCHAR2,p_line_item_id in VARCHAR2,P_ERR_MSG OUT VARCHAR2)
AS
V_ORDER_STATUS VARCHAR(20);
l_exception exception;
BEGIN

BEGIN
     SELECT ORDER_STATUS INTO V_ORDER_STATUS FROM ORDER_LINE_ITEM
     WHERE order_id=p_order_id AND line_item_id=p_line_item_id;
   EXCEPTION
    WHEN NO_DATA_FOUND THEN
    p_err_msg:='ERROR NO_DATA_FOUND FOR GIVEN ORDER_ID AND LINE_ITEM_ID'||SUBSTR(SQLERRM,1,200);
    raise l_exception;
    WHEN OTHERS THEN
    p_err_msg:='ERROR WHILE UPDATING ORDER STATUS ' ||SUBSTR(sqlerrm,1,200);
    raise l_exception;
   END;

   IF V_ORDER_STATUS='ORDER-IN-PROGRESS' OR V_ORDER_STATUS='ORDER-GENERATED' THEN
   p_err_msg:='GIVEN ORDER_ID ALREADY PROCESSED';
   RAISE l_exception;

   ELSE
   BEGIN
     UPDATE ORDER_LINE_ITEM SET ORDER_STATUS='ORDER-IN-PROGRESS'
     WHERE order_id=p_order_id AND line_item_id=p_line_item_id
     AND ORDER_STATUS IN ('APPROVED','FAILED');
     COMMIT;
   EXCEPTION
    WHEN NO_DATA_FOUND THEN
    p_err_msg:='ERROR NO_DATA_FOUND FOR GIVEN ORDER_ID AND LINE_ITEM_ID'||SUBSTR(SQLERRM,1,200);
    raise l_exception;
    WHEN OTHERS THEN
    p_err_msg:='ERROR WHILE UPDATING ORDER STATUS ' ||SUBSTR(sqlerrm,1,200);
    raise l_exception;
   END;
DBMS_SCHEDULER.CREATE_JOB
(
   job_name             => 'ONE_TIME_job_ORDER_'||P_ORDER_ID,
   job_type             => 'PLSQL_BLOCK',
   job_action           =>
                             'BEGIN sp_card_order(''' || P_ORDER_ID ||''','''|| p_line_item_id|| '''); END;',

   start_date           => sysdate,
   enabled              => TRUE,
   auto_drop            => TRUE,
   comments             => 'Job will run one time'
);
P_ERR_MSG:='OK';
 END IF;
EXCEPTION
     WHEN l_exception THEN
     ROLLBACK;
    WHEN OTHERS THEN
    --NULL;
        p_err_msg:= 'Error while processing JOB ...'||substr(sqlerrm,1,200);
        dbms_output.put_line(p_err_msg);
    END;

/
SHOW ERROR