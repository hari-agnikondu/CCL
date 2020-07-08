create or replace PROCEDURE             "SCHEDULE_PROCESS_DIGITAL" (P_ORDER_ID_IN in TYPE_DIGITAL,P_LINE_ITEM_ID_IN in
VARCHAR2,P_ERR_MSG_OUT OUT VARCHAR2)
AS
l_order_status VARCHAR(20);
l_exception exception;


BEGIN
    for i in P_ORDER_ID_IN.FIRST..P_ORDER_ID_IN.last 
LOOP

BEGIN
BEGIN
     SELECT ORDER_STATUS INTO l_order_status FROM CLP_ORDER.ORDER_LINE_ITEM
     WHERE order_id=P_ORDER_ID_IN(i) AND line_item_id=P_LINE_ITEM_ID_IN;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
    P_ERR_MSG_OUT:='ERROR NO_DATA_FOUND FOR GIVEN ORDER_ID AND LINE_ITEM_ID'||SUBSTR(SQLERRM,1,200);
    raise l_exception;
    WHEN OTHERS THEN
    P_ERR_MSG_OUT:='ERROR WHILE UPDATING ORDER STATUS ' ||SUBSTR(sqlerrm,1,200);
    raise l_exception;
END;

  IF l_order_status='ORDER-IN-PROGRESS' OR l_order_status='ORDER-GENERATED' THEN
   P_ERR_MSG_OUT:='GIVEN ORDER_ID ALREADY PROCESSED';
  RAISE l_exception;

   ELSE
   BEGIN
     UPDATE CLP_ORDER.ORDER_LINE_ITEM SET ORDER_STATUS='ORDER-IN-PROGRESS'
     WHERE order_id=P_ORDER_ID_IN(i) AND line_item_id=P_LINE_ITEM_ID_IN
     AND ORDER_STATUS IN ('APPROVED','FAILED');
     COMMIT;
   EXCEPTION
    WHEN NO_DATA_FOUND THEN
    P_ERR_MSG_OUT:='ERROR NO_DATA_FOUND FOR GIVEN ORDER_ID AND LINE_ITEM_ID'||SUBSTR(SQLERRM,1,200);
    raise l_exception;
    WHEN OTHERS THEN
    P_ERR_MSG_OUT:='ERROR WHILE UPDATING ORDER STATUS ' ||SUBSTR(sqlerrm,1,200);
    raise l_exception;
   END;

DBMS_SCHEDULER.CREATE_JOB
(
   job_name             => 'ONE_TIME_job_ORDER_'||P_ORDER_ID_IN(i),
   job_type             => 'PLSQL_BLOCK',
   job_action           =>
                             'BEGIN SP_ORDER_PROCESS_DIGITAL(''' || P_ORDER_ID_IN(i) ||''','''|| P_LINE_ITEM_ID_IN|| '''); END;',

   start_date           => sysdate,
   enabled              => TRUE,
   auto_drop            => TRUE,
   comments             => 'Job will run one time'
);
P_ERR_MSG_OUT:='OK';
 END IF;
EXCEPTION
     WHEN l_exception THEN
     ROLLBACK;
    WHEN OTHERS THEN
    --NULL;
        P_ERR_MSG_OUT:= 'Error while processing JOB ...'||substr(sqlerrm,1,200);
        dbms_output.put_line(P_ERR_MSG_OUT);

    END;
END loop;
END;