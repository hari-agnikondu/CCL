create or replace PROCEDURE        CLP_ORDER.SCHEDULE_ORDER_B2B (P_ORDER_ID in VARCHAR2,p_partner_id in NUMBER,P_ERR_MSG OUT VARCHAR2)
AS
l_ORDER_STATUS VARCHAR(20);
l_exception exception;
l_line_item_id       VARCHAR2(100); 
l_count         NUMBER:=0;
l_orderid       VARCHAR2(50 CHAR);

        /**
        *   Modified by: Ravi
        *   Purpose:Job name convention purpose order_id replace as '_' if any special characters received
        *    Modified Date:08/Feb/19
        *    Reviewed By:
        *     Reviewed Date:
        *     Build : R04.2 B1
        **/

CURSOR line_items IS
        SELECT 
            line_item_id
                FROM
                    ORDER_LINE_ITEM L, ORDER_DETAILS O
                 where 
                    L.order_id = O.order_id AND L.partner_id=O.partner_id AND O.order_id=p_order_id AND L.ORDER_STATUS = O.ORDER_STATUS AND O.ORDER_STATUS='APPROVED'
                 AND 
                     O.partner_id=p_partner_id;
 procedure p_orderstatus_update(p_order_id in VARCHAR2,P_line_item_id IN VARCHAR2)
as
pragma autonomous_transaction;
begin
     UPDATE ORDER_LINE_ITEM SET ORDER_STATUS='ORDER-IN-PROGRESS'
     WHERE order_id=p_order_id AND line_item_id=l_line_item_id
     AND ORDER_STATUS IN ('APPROVED','FAILED'); 
	 IF SQL%ROWCOUNT=0 THEN
	 RAISE l_exception;
	 END IF;
      COMMIT;
end;

BEGIN

FOR y IN line_items LOOP
    l_line_item_id := y.line_item_id; 

BEGIN
     SELECT ORDER_STATUS INTO l_ORDER_STATUS FROM ORDER_LINE_ITEM 
     WHERE order_id=p_order_id AND line_item_id=l_line_item_id ;
   EXCEPTION 
    WHEN NO_DATA_FOUND THEN 
    p_err_msg:='ERROR NO_DATA_FOUND FOR GIVEN ORDER_ID AND LINE_ITEM_ID'||SUBSTR(SQLERRM,1,200);
    raise l_exception;
    WHEN OTHERS THEN
    p_err_msg:='ERROR WHILE UPDATING ORDER STATUS ' ||SUBSTR(sqlerrm,1,200);
    raise l_exception;
   END;


   IF l_ORDER_STATUS='ORDER-IN-PROGRESS' OR l_ORDER_STATUS='ORDER-GENERATED'
   THEN
   p_err_msg:='GIVEN ORDER_ID ALREADY PROCESSED';
   RAISE l_exception;

   ELSE
   l_count := l_count+1;
   p_orderstatus_update(P_ORDER_ID,l_line_item_id);
  
   END if;
   end loop;
   if l_count > 0
   then
   --Job name convention purpose order_id replace as '_' if any special characters received
   l_orderid := regexp_replace(P_ORDER_ID, '( *[[:punct:]])', '_'); 
DBMS_SCHEDULER.CREATE_JOB 
(
   job_name             => 'ONE_TIME_job_ORDER_'||l_orderid,
    
   job_type             => 'PLSQL_BLOCK',
   job_action           =>   
                             'BEGIN sp_card_order_b2b(''' || P_ORDER_ID ||''','''|| p_partner_id || '''); END;',

   start_date           => sysdate,
   enabled              => TRUE,
   auto_drop            => TRUE,
   comments             => 'Job will run one time'
);
P_ERR_MSG:='OK';
    ELSE
     p_err_msg:='ORDER REJECTED';
   RAISE l_exception;
   End if;
EXCEPTION
     WHEN l_exception THEN
     ROLLBACK;
    WHEN OTHERS THEN
    --NULL;
        p_err_msg:= 'Error while processing JOB ...'||substr(sqlerrm,1,200);
        dbms_output.put_line(p_err_msg);
    END;
/
show errors;

----------------------------------------------------------------------