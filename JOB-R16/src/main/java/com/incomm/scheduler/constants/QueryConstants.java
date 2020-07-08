/**
 * 
 */
package com.incomm.scheduler.constants;

/**
 * QueryConstants class defines all the sql queries used by the Scheduler
 * Service.
 *
 */
public class QueryConstants {

	/**
	 * Constants class should not be instantiated.
	 */
	private QueryConstants() {
		throw new IllegalStateException("Constants class");
	}
	
	
	/**
	 *  Card Number Inventory 
	 */
	public static final String CARD_INVENTORY_GENERATION_PROCEDURE = "clp_inventory.SCHEDULE_INVENTORY";
	
	public static final String GET_ALL_CARD_INVNTRY_LIST= "select cardrange from CardRange cardrange"
			+ " where cardrange.status= 'APPROVED'";
	
	public static final String GET_ALL_CARD_INVNTRY_LIST_BY_SCHEDULER= "select cardrange from CardRange cardrange"
			+ " where cardrange.status= 'APPROVED'";
	
	
	public static final String PAUSE_CARD_NUMBER_GENERATION = "update CardRangeInventory cardRangeInventory "
			+ "set cardRangeInventory.inventoryStatus= 'PAUSED' where cardRangeInventory.cardRangeId =:cardRangeId";
	
	public static final String DAILY_BALANCE_ALERT_MSG = "update SMS_EMAIL_DAILY_ALERT_MSG SET PROCESS_STATUS=?,END_INTERVAL=SYSDATE WHERE CARD_NUM_HASH=clp_transactional.FN_HASH(?)";

	public static final String  GET_DAILY_BALANCE_ALERT_QUERY = "SELECT FN_DMAPS_MAIN(CARD_NUM_ENCR),MOBILE_NUMBER,EMAIL,CARD_NUM_HASH,ACCOUNT_ID,PROCESS_STATUS,CURRENCY_CODE FROM SMS_EMAIL_DAILY_ALERT_MSG";
	
	public static final String  GET_POSTBACK_REQ_DETAILS_QUERY = "SELECT FP.ORDER_ID, FP.API_NAME, FP.SEQ_NO,FP.REQ_MSG,FP.REQ_HEADER,FP.RES_HEADER,FP.RESPONSE_CODE,FP.RESPONSE_MSG,FP.RESPONSE_COUNT,FP.RESPONSE_FLAG,FP.POSTBACK_STATUS,FP.POSTBACK_URL FROM FSAPI_POSTBACK_LOG FP,PROCESS_SCHEDULE PS where  NVL(RESPONSE_COUNT,0)< NVL(PS.RETRY_CNT,0) and FP.POSTBACK_STATUS='N'";
	
	public static final String GET_ORDER_STATUS="SELECT OD.ORDER_ID ,OD.ORDER_STATUS oStatus,OD.SHIPPING_METHOD shippingMethod, \r\n" + 
			"		 OL.LINE_ITEM_ID , OL.ORDER_STATUS lStatus,OL.RETURN_FILE_MSG RESP_REASON,\r\n" + 
			"		 (select reject_code from fileprocess_rjreason\r\n" + 
			"		    where reject_reason=upper(RETURN_FILE_MSG)) AS RESP_CODE\r\n" + 
			"		 FROM ORDER_DETAILS OD,order_line_item  OL WHERE\r\n" + 
			"         OD.ORDER_ID=OL.ORDER_ID AND  OD.PARTNER_ID=OL.PARTNER_ID AND OL.ORDER_ID=? AND OD.PARTNER_ID=?";
	
	
	public static final String GET_ORDER_LINE_ITEM_DTLS=" SELECT C.PROXY_number,C.serial_number,(select STATUS_DESC from CARD_STATUS WHERE STATUS_CODE =C.CARD_STATUS),OLD.PIN, OLD.PROXY_PIN_ENCR,OLD.TRACKING_NBR,OLD.SHIPPING_DATE, \r\n" + 
			"		 C.CUSTOMER_CODE,C.PRODUCT_ID,FN_DMAPS_MAIN(C.CARD_NUM_ENCR) CARDNUMBER,C.CARD_NUM_HASH,TO_CHAR(C.EXPIRY_DATE, 'YYYY-MM-DD') EXPIRATIONDATE,\r\n" + 
			"         C.CARD_NUM_ENCR,OLD.printer_response \r\n" + 
			"		 FROM ORDER_DETAILS OD ,ORDER_LINE_ITEM OL, ORDER_LINE_ITEM_DTL OLD,CARD C \r\n" + 
			"		 WHERE OD.ORDER_ID  =OL.ORDER_ID AND OD.PARTNER_ID  =OL.PARTNER_ID \r\n" + 
			"		 AND OD.ORDER_ID    =OLD.ORDER_ID AND OD.PARTNER_ID  =OLD.PARTNER_ID \r\n" + 
			"		 AND OL.LINE_ITEM_ID=OLD.ORDER_LINE_ITEM_ID AND C.CARD_NUM_HASH(+)=OLD.CARD_NUM_HASH \r\n" + 
			"		 AND OD.ORDER_ID    =?  AND OD.PARTNER_ID  =? AND OL.LINE_ITEM_ID=?";
	
	public static final String AUTO_REPLENISH_DETAILS = "SELECT PRODUCT_ID,location_id,reorder_level,reorder_value,auto_replenish,max_inventory, NVL (curr_inventory, 0) curr_inventory,merchant_id FROM LOCATION_INVENTORY WHERE auto_replenish = 'Y' AND curr_inventory <= reorder_level";
	
	public static final String UPDATE_POSTBACKSTATUS = "UPDATE FSAPI_POSTBACK_LOG SET POSTBACK_STATUS= ? , RESPONSE_COUNT= NVL(RESPONSE_COUNT,0)+1,RES_MSG=? WHERE SEQ_NO=? ";
	
	public static final String GET_PRODUCT_ATTRIBUTES = "select attributes from product where product_id=?";
	public static final String CURR_SERIAL_NUMBER = "select nvl(sum(end_serial)-sum(serial_number),-1) as pending from product_serial_control where product_id=?";
	
	public static final String GET_PRODUCT_IDS = "select p.product_id from product p where upper(p.attributes.Product.productType)=upper('B2B')";
	public static final String LOG_RESPONSE = "insert into SERIAL_NUM_AUDIT(product_id,serial_request,serial_response,serial_request_status,ins_date,LAST_UPD_DATE) values(?,?,?,?,sysdate,sysdate)";
	
	public static final String GET_GLOBAL_PARAMETETS ="select ATTRIBUTE_NAME,ATTRIBUTE_VALUE from ATTRIBUTE_DEFINITION where ATTRIBUTE_GROUP ='Global Parameters'";

	public static final String GET_AVAILABLE_INVENTORY = "select count(*) from card where card_status = '99' and product_id=?";
	
	public static final String GET_ISSUED_INVENTORY = "select count(*) from card where product_id=?";
}
