/**
 * 
 */
package com.incomm.cclp.constants;

/**
 * QueryConstants class defines all the sql queries used by the Configuration
 * Service.
 * 
 * @author abutani
 *
 */
public class QueryConstants {

	/**
	 * Constants class should not be instantiated.
	 */
	private QueryConstants() {
		throw new IllegalStateException("Constants class");
	}

	/* Order */

	public static final String GET_ALL_MERCHANT_BY_PRODUCTID = "select * from clp_configuration.merchant where merchant_id in(select merchant_id from clp_configuration.merchant_product where product_id=:productId)";

	public static final String GET_ALL_LOCATION_BY_MERCHANTID = "select * from clp_configuration.location where Merchant_id=:merchantId";

	public static final String GET_ORDER_BY_ID = "select order from Order order "
			+ "where UPPER(order.orderId) like :orderId";

	public static final String GET_AVAILABLE_INVENTORY = "select NVL (sum(available_inventory), 0) from clp_inventory.card_range_inventory where card_range_id in :cardRanges";
	public static final String GET_ALL_ORDERS = "select order from Order order order by order.orderId";
	public static final String GET_ORDERS_BY_ORDERID_AND_PRODUCTID = "select O.ORDER_ID,NVL(O.PARTNER_ID,0),P.PARTNER_NAME,NVL(O.ISSUER_ID,0), I.ISSUER_NAME,NVL(O.MERCHANT_ID,0),(select MERCHANT_NAME from clp_configuration.merchant where merchant_id=O.MERCHANT_ID) as merchant_name,NVL(O.LOCATION_ID,0),(select LOCATION_NAME from clp_configuration.location where location_id=O.LOCATION_ID) as location_name,NVL(ol.PRODUCT_ID,0),prod.PRODUCT_NAME,to_date(O.INS_DATE,'dd-MM-yy'), prod.attributes.General.defaultCardStatus,O.ORDER_STATUS,O.ORDER_TYPE,ol.PACKAGE_ID,NVL(OL.QUANTITY,0),NVL(O.INS_USER,1),ol.LINE_ITEM_ID,ol.ORDER_STATUS as lineItemIdStatus,(select CCF_FILE_NAME  FROM ORDER_LINE_ITEM_DTL WHERE ORDER_ID=O.ORDER_ID AND PARTNER_ID=O.PARTNER_ID AND ORDER_LINE_ITEM_ID = ol.LINE_ITEM_ID AND Rownum<=1 ) as ccf_file_name,(select * from(select  SERIAL_NUMBER FROM ORDER_LINE_ITEM_DTL WHERE ORDER_ID=O.ORDER_ID AND PARTNER_ID=O.PARTNER_ID AND ORDER_LINE_ITEM_ID = ol.LINE_ITEM_ID  ORDER BY SERIAL_NUMBER) where Rownum<=1) as startSerialNo,(select * from(select  SERIAL_NUMBER FROM ORDER_LINE_ITEM_DTL WHERE ORDER_ID=O.ORDER_ID AND PARTNER_ID=O.PARTNER_ID AND ORDER_LINE_ITEM_ID = ol.LINE_ITEM_ID  ORDER BY SERIAL_NUMBER DESC) where Rownum<=1) as endSerialNo from ORDER_DETAILS O, clp_configuration.ISSUER I,clp_configuration.PARTNER P, order_line_item ol,clp_configuration.PRODUCT prod WHERE O.ISSUER_ID=I.ISSUER_ID AND O.PARTNER_ID=P.PARTNER_ID and ol.product_id=prod.PRODUCT_ID AND ol.PARTNER_ID=O.PARTNER_ID and ol.ORDER_ID=O.ORDER_ID ";

	public static final String UPDATE_ORDER_STATUS = "update ORDER_DETAILS set ORDER_STATUS=:status, CHECKER_REMARKS=:checkerRemarks, LAST_UPD_DATE=sysdate, LAST_UPD_USER=:insUser "
			+ "where ORDER_ID=:orderId and PARTNER_ID=:partnerId";

	public static final String UPDATE_ORDER_LINE_ITEM_STATUS = "update ORDER_LINE_ITEM set ORDER_STATUS=:status "
			+ "where ORDER_ID=:orderId and PARTNER_ID=:partnerId";

	public static final String GET_ORDER_BY_ORDERID = "select order from Order order where order.orderId like :orderId";

	public static final String GET_ALL_ORDERS_FOR_APPROVAL = "select O.ORDER_ID,O.PARTNER_ID,P.PARTNER_NAME,O.ISSUER_ID, I.ISSUER_NAME,NVL(O.MERCHANT_ID,0),(select MERCHANT_NAME from clp_configuration.merchant where merchant_id=O.MERCHANT_ID) as merchant_name,NVL(O.LOCATION_ID,0),(select LOCATION_NAME from clp_configuration.location where location_id=O.LOCATION_ID) as location_name,ol.PRODUCT_ID,prod.PRODUCT_NAME,O.INS_DATE, prod.attributes.General.defaultCardStatus card_status ,O.ORDER_STATUS,O.ORDER_TYPE,ol.PACKAGE_ID,OL.QUANTITY,CU.USER_NAME,ol.LINE_ITEM_ID,O.INS_USER from ORDER_DETAILS O, clp_configuration.ISSUER I,clp_configuration.PARTNER P, order_line_item ol,clp_configuration.PRODUCT prod,clp_configuration.CLP_USER CU WHERE CU.USER_ID= O.INS_USER AND  O.ISSUER_ID=I.ISSUER_ID AND O.PARTNER_ID=P.PARTNER_ID and ol.product_id=prod.PRODUCT_ID AND ol.PARTNER_ID=O.PARTNER_ID and ol.ORDER_ID=O.ORDER_ID and O.ORDER_STATUS='PENDING'";

	public static final String GET_ALL_ORDERS_FOR_ORDER = "select O.ORDER_ID,O.PARTNER_ID,P.PARTNER_NAME,O.ISSUER_ID, I.ISSUER_NAME,NVL(O.MERCHANT_ID,0),(select MERCHANT_NAME from clp_configuration.merchant where merchant_id=O.MERCHANT_ID) as merchant_name,NVL(O.LOCATION_ID,0),(select LOCATION_NAME from clp_configuration.location where location_id=O.LOCATION_ID) as location_name,ol.PRODUCT_ID,prod.PRODUCT_NAME,O.INS_DATE, prod.attributes.General.defaultCardStatus card_status ,O.ORDER_STATUS,O.ORDER_TYPE,ol.PACKAGE_ID,OL.QUANTITY,CU.USER_NAME,ol.LINE_ITEM_ID from ORDER_DETAILS O, clp_configuration.ISSUER I,clp_configuration.PARTNER P, order_line_item ol,clp_configuration.PRODUCT prod,clp_configuration.CLP_USER CU WHERE CU.USER_ID= O.INS_USER AND  O.ISSUER_ID=I.ISSUER_ID AND O.PARTNER_ID=P.PARTNER_ID and ol.product_id=prod.PRODUCT_ID AND ol.PARTNER_ID=O.PARTNER_ID and prod.attributes.Product.productType <>'B2B' and ol.ORDER_ID=O.ORDER_ID and O.ORDER_STATUS='APPROVED'";

	public static final String GET_ALL_ORDERS_FOR_CCF = "select O.ORDER_ID,O.PARTNER_ID,P.PARTNER_NAME,O.ISSUER_ID, I.ISSUER_NAME,NVL(O.MERCHANT_ID,0),(select MERCHANT_NAME from clp_configuration.merchant where merchant_id=O.MERCHANT_ID) as merchant_name,NVL(O.LOCATION_ID,0),(select LOCATION_NAME from clp_configuration.location where location_id=O.LOCATION_ID) as location_name,ol.PRODUCT_ID,prod.PRODUCT_NAME,O.INS_DATE, prod.attributes.General.defaultCardStatus card_status ,O.ORDER_STATUS,O.ORDER_TYPE,ol.PACKAGE_ID,OL.QUANTITY,CU.USER_NAME,ol.LINE_ITEM_ID from ORDER_DETAILS O, clp_configuration.ISSUER I,clp_configuration.PARTNER P, order_line_item ol,clp_configuration.PRODUCT prod,clp_configuration.CLP_USER CU WHERE CU.USER_ID= O.INS_USER AND  O.ISSUER_ID=I.ISSUER_ID AND O.PARTNER_ID=P.PARTNER_ID and ol.product_id=prod.PRODUCT_ID and prod.attributes.Product.productType <>'B2B' AND ol.PARTNER_ID=O.PARTNER_ID and ol.ORDER_ID=O.ORDER_ID and ol.ORDER_STATUS='ORDER-GENERATED'";

	public static final String GET_ALL_PRODUCT_PACKAGES = "SELECT PACKAGE_ID FROM clp_configuration.PRODUCT_PACKAGE WHERE PRODUCT_ID=:productId";

	public static final String GET_AVAILABLE_INVENTORY_FOR_LOCATION = "select max_inventory-curr_inventory from location_inventory where merchant_id=:merchantId and location_id=:locationId and product_id=:productId";

	public static final String UPDATE_LOCATION_INVENTORY = "update location_inventory set curr_inventory=curr_inventory+:quantityValue where merchant_id=:merchantId and location_id=:locationId and product_id=:productId";

	public static final String UPDATE_LOCATION_INVENTORY_FOR_REJECTED_ORDER = "update location_inventory set curr_inventory="
			+ "curr_inventory-(select quantity from order_line_item where order_id=:orderId and partner_id=:partnerId)  "
			+ "where merchant_id =(select merchant_id from order_details where order_id=:orderId and partner_id=:partnerId) "
			+ "and location_id = (select location_id from order_details where order_id=:orderId and partner_id=:partnerId)";

	public static final String SCHEDULER_TRIGGER_TEMP = " update SCHEDULER_TRIGGER_TEMP set  where  ";

	/* STOCK */

	public static final String GET_STOCKS_BY_MERCHANTID_AND_LOCATIONID = "select stock from Stock stock";

	public static final String GET_LOCATIONS_AND_PRODUCTS_BY_MERCHANTID = "select loc.location_id, loc.location_name , mer.product_id , prod.product_name "
			+ "from clp_configuration.location loc,clp_configuration.merchant_product mer,clp_configuration.product prod where loc.merchant_id = mer.merchant_id and mer.product_id = prod.product_id and loc.merchant_id =:merchant_id";

	public static final String GET_ALL_MERCHANTS = "select merchant from Merchant merchant";

	public static final String GET_STOCKS_BY_IDS = "select stock from Stock stock where merchant_id=:merchant_id and location_id=:location_id and "
			+ "product_id=:productId";

	/* B2B transaction queries */
	public static final String B2B_DUPLLICATE_ORDER_CHECK = "SELECT NVL(count(*),0) FROM ORDER_DETAILS  where ORDER_ID=:orderId and PARTNER_ID=:partnerId ";
	public static final String CHECK_PRODUCT = "SELECT COUNT(*) from clp_configuration.PRODUCT WHERE PRODUCT_ID = ?";
	public static final String GET_PRODUCT_ATTRIBUTES = "select ATTRIBUTES from clp_configuration.PRODUCT where PRODUCT_ID = ?";
	public static final String CHECK_PRODUCT_PACKAGEID = "select count(1) from clp_configuration.product_package where product_id=? and UPPER(package_id)=UPPER(?)";
	public static final String CHECK_PARTNERID = "select count(1) from clp_configuration.product where product_id=? and partner_id=?";
	public static final String CHECK_PARTNERID_GROUP_ACCESS = "select count(1) from clp_configuration.group_access_product where product_id=? and partner_id=?";
	public static final String CHECK_PAN_COUNT = "select NVL (sum(available_inventory), 0) from clp_inventory.card_range_inventory where card_range_id in (select card_range_id from clp_configuration.product_card_range where product_id=?)";
	public static final String CHECK_SERIAL_NUMBER = "SELECT count(1) as serialCount  from product_serial_control  where product_id = ? AND serial_number + nvl(?,0) <= end_serial";

	public static final String ORDER_FULFILLMENT_VENDOR = "select attribute_value from  clp_configuration.package_attributes where package_id in (:packageIdList) and attribute_name='fulfillmentMethod'";
	public static final String ORDER_FULFILLMENT_VENDOR_KEYPAIR = "select package_id,attribute_value from  clp_configuration.package_attributes where package_id in (:packageIdList) and attribute_name='fulfillmentMethod'";
	//public static final String ORDER_FULFILLMENT_VENDOR_KEYPAIR = "select package_id,attribute_value from  package_attributes where package_id in (:packageIdList) and attribute_name='fulfillmentMethod'";
	public static final String ORDER_INSERT_ORDER_LINEITEM = "insert into order_line_item (ORDER_ID,PARTNER_ID,LINE_ITEM_ID,PRODUCT_ID,DENOMINATION,EMBOSSED_LINE,OFFER_CODE,QUANTITY,\r\n"
			+ "ORDER_STATUS,INS_DATE,ERROR_MSG,EMBOSSED_LINE1,PACKAGE_ID,PRODUCT_FUNDING,FUND_AMOUNT,FUNDING_OVERRIDE,PURSE_ID) values (?,?,?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?)";

	public static final String GET_ISSUER_ID = "select issuer_id from clp_configuration.product where product_id= ?";

	public static final String ORDER_INSERT_ORDER = "insert into order_details (ORDER_ID,PARTNER_ID,ISSUER_ID,MERCHANT_ID,POSTBACK_RESPONSE,POSTBACK_URL,ACTIVATION_CODE,SHIPPING_METHOD,ORDER_STATUS,ADDRESS_LINE1,ADDRESS_LINE2,ADDRESS_LINE3,CITY,STATE,POSTAL_CODE,COUNTRY,FIRST_NAME,MIDDLE_INITIAL,LAST_NAME,PHONE,EMAIL,SHIP_TO_COMPANY_NAME,SHIPPING_FEE,ERROR_MSG,CHANNEL_ID,ACCEPT_PARTIAL,ORDER_TYPE,FULFILLMENT_TYPE,CHANNEL,CLIENT_ID,CLIENT_NAME,INS_USER,LAST_UPD_USER,SHIP_STATUS,PROGRAM_ID,INS_DATE,LAST_UPD_DATE,INDIVIDUAL_CCF) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,sysdate,?)";
	public static final String B2B_ORDERID_PARTNERID_CHECK = "SELECT count(1) count  FROM ORDER_DETAILS  where ORDER_ID=:orderId and PARTNER_ID=:partnerId ";
	public static final String B2B_LINEITEMID_CHECK = "SELECT LINE_ITEM_ID FROM order_line_item  where ORDER_ID=:orderId and PARTNER_ID=:partnerId ";
	public static final String B2B_ORDER_STATUS = "SELECT o.order_id,decode(upper(o.order_status),'ORDER-GENERATED','Processing','CCF-GENERATED','Shipping','PRINTER_ACKNOWLEDGED','Shipping','SHIPPED','Completed','REJECTED','Rejected','APPROVED','Received',o.order_status) as order_status,o.shipping_method,o.error_msg,l.line_item_id,decode(upper(l.order_status),'ORDER-GENERATED','Processing','ORDER-IN-PROGRESS','Processing','CCF-GENERATED','Shipping','PRINTER_ACKNOWLEDGED','Shipping','SHIPPED','Completed','REJECTED','Rejected','APPROVED','Received',l.order_status) AS line_item_status ,l.return_file_msg, (SELECT reject_code FROM clp_order.FILEPROCESS_REJECT_REASON WHERE reject_reason=upper(RETURN_FILE_MSG)) AS RESP_CODE,o.POSTBACK_URL   FROM ORDER_DETAILS o,ORDER_LINE_ITEM l WHERE o.order_id = l.order_id AND o.partner_id = l.partner_id AND o.order_id = ? AND o.partner_id = ? ";

	public static final String ORDER_PROC_CALL = "{ call SCHEDULE_ORDER_B2B(?,?,?) }";
	public static final String B2B_CARD_DETAILS = "SELECT lineitemdtl.PRINTER_RESPONSE,card.PROXY_NUMBER,lineitemdtl.PIN,lineitemdtl.PROXY_PIN_ENCR,card.SERIAL_NUMBER,(select status_desc from card_status where status_code = card.CARD_STATUS) as CARD_STATUS,lineitemdtl.TRACKING_NBR,lineitemdtl.SHIPPING_DATE,card.CARD_NUM_HASH\r\n"
			+ "FROM ORDER_DETAILS o,ORDER_LINE_ITEM lineitem, ORDER_LINE_ITEM_DTL lineitemdtl,CARD card\r\n"
			+ " WHERE o.ORDER_ID = lineitem.ORDER_ID AND o.PARTNER_ID = lineitem.PARTNER_ID AND o.ORDER_ID = lineitemdtl.ORDER_ID \r\n"
			+ "        AND o.PARTNER_ID = lineitemdtl.PARTNER_ID  AND lineitem.LINE_ITEM_ID = lineitemdtl.ORDER_LINE_ITEM_ID \r\n"
			+ "        AND card.CARD_NUM_HASH = lineitemdtl.CARD_NUM_HASH AND o.ORDER_ID = ? AND lineitem.LINE_ITEM_ID = ? AND o.PARTNER_ID = ? ";

	public static final String FSAPI_POSTBACK_LOG = "INSERT INTO FSAPI_POSTBACK_LOG (SEQ_NO ,API_NAME, ORDER_ID, REQ_HEADER, REQ_MSG, RESPONSE_CODE, RESPONSE_MSG, RES_MSG, SERVER_ID, TIME_TAKEN, POSTBACK_STATUS,INS_DATE,POSTBACK_URL) VALUES(FSAPI_POSTBACK_SEQ.nextval,?,?,?,?,?,?,?,?,?,?,sysdate,?)";

	public static final String FSAPI_REQ_RES_LOG = "INSERT INTO FSAPI_REQ_RES_LOG (API_NAME, REQ_HEADER, REQ_MSG, RESP_CODE, RESP_MSG, RES_MSG, SERVER_ID, TIME_TAKEN, INS_DATE,SEQ_NO) VALUES(?,?,?,?,?,?,?,?,sysdate,CLP_TRANSACTIONAL.SEQ_FSAPI_REQ_RES_LOG.nextval)";

	public static final String GET_DELCHN_TXNCODE = "select channel_code,tran_code from fsapi_transaction where channel_desc=? and request_type=?";

	public static final String GET_PROXY_SERIAL_LIST = "select distinct proxy_number from card where to_number(proxy_number) between to_number(?) and to_number(?)";

	public static final String GET_ORDER_ID = "select o.order_id from order_line_item_dtl o,card c where c.card_num_hash=o.CARD_NUM_HASH  and o.order_id not like 'ROID%' and";

	public static final String GET_SERIAL_DETAILS = "SELECT p.attributes.Product.formFactor CardType,c.PRODUCT_ID,CUSTOMER_CODE,CARD_NUM_HASH,to_char(EXPIRY_DATE,'DD-MM-YYYY') EXPDATE,TO_CHAR(EXPIRY_DATE, 'YYYY-MM-DD') EXPIRATIONDATE,EXPIRY_DATE,CARD_STATUS,fn_dmaps_main(CARD_NUM_ENCR) cardNo,CARD_NUM_ENCR,ACTIVATION_CODE FROM  CARD c,clp_configuration.product p  WHERE c.product_id=p.product_id and c.SERIAL_NUMBER=?";
	public static final String GET_SERIAL_DETAILS_USING_HASH = "SELECT p.attributes.Product.formFactor CardType,c.PRODUCT_ID,CUSTOMER_CODE,CARD_NUM_HASH,to_char(EXPIRY_DATE,'DD-MM-YYYY') EXPDATE,TO_CHAR(EXPIRY_DATE, 'YYYY-MM-DD') EXPIRATIONDATE,EXPIRY_DATE,CARD_STATUS,fn_dmaps_main(CARD_NUM_ENCR) cardNo,CARD_NUM_ENCR,ACTIVATION_CODE FROM  CARD c,clp_configuration.product p  WHERE c.product_id=p.product_id and  c.SERIAL_NUMBER=? and c.card_num_hash=?";
	public static final String GET_PROXY_DETAILS = "SELECT p.attributes.Product.formFactor CardType, c.PRODUCT_ID,CUSTOMER_CODE,CARD_NUM_HASH,to_char(EXPIRY_DATE,'DD-MM-YYYY') EXPDATE,TO_CHAR(EXPIRY_DATE, 'YYYY-MM-DD') EXPIRATIONDATE,EXPIRY_DATE,CARD_STATUS,fn_dmaps_main(CARD_NUM_ENCR) cardNo,CARD_NUM_ENCR,ACTIVATION_CODE FROM CARD c,clp_configuration.product p WHERE c.product_id=p.product_id and  c.PROXY_NUMBER=?";
	public static final String FSAPI_PROXY_ACTIVATION_LOG = "INSERT INTO CLP_TRANSACTIONAL.FSAPI_PROXY_ACTIVATION(RRN,PROXY_NUMBER,ACTIVATION_CODE,RESPONSE_STATUS,INS_DATE)VALUES(?,?,?,?,sysdate)";
	public static final String FSAPI_SERIAL_NUMBER_ACTIVATION_LOG = "INSERT INTO FSAPI_SERIAL_ACTIVATION(RRN,SERIAL_NUMBER,ACTIVATION_CODE,RESPONSE_STATUS,INST_DATE)VALUES(?,?,?,?,sysdate)";
	/* B2B query Constants starts */
	public static final String B2B_ORDER_ACTIVATION_QRY = "update card set CARD_STATUS=? ,DATE_OF_ACTIVATION=sysdate where CARD_NUM_HASH=?";
	public static final String B2B_GET_CARD_NUMBER_BY_SERIAL_NUMBER = "select card_num_hash from card where serial_number=? and card_status='0'";
	public static final String B2B_GET_ORDER_DETAILS_BY_CARD = "SELECT ORDER_ID,PARTNER_ID,ORDER_LINE_ITEM_ID FROM ORDER_LINE_ITEM_DTL WHERE CARD_NUM_HASH=?";
	public static final String B2B_CHECK_ORDERID_PARTNERID = "SELECT * FROM ORDER_DETAILS WHERE ORDER_ID=? AND PARTNER_ID=?";
	public static final String B2B_GET_PRODUCTID_BY_ORDERID = "select PRODUCT_ID from order_line_item where order_id=? and partner_id=? and LINE_ITEM_ID=?";
	public static final String B2B_GET_LINE_ITEM_DETAILS_BY_SERIAL_NUMBER = " select distinct OLID.ORDER_LINE_ITEM_ID lineItemID,OLI.ORDER_STATUS OLI_ORDER_STATUS,OD.ORDER_STATUS OD_ORDER_STATUS"
			+ " from ORDER_DETAILS OD,order_line_item OLI,ORDER_LINE_ITEM_DTL OLID,card c where OLID.CARD_NUM_HASH=C.CARD_NUM_HASH"
			+ " AND OLI.ORDER_ID=OLID.ORDER_ID AND OLID.ORDER_LINE_ITEM_ID=OLI.LINE_ITEM_ID AND OLID.PARTNER_ID=OLI.PARTNER_ID AND "
			+ "OD.ORDER_ID=OLI.ORDER_ID AND OD.PARTNER_ID=OLI.PARTNER_ID AND OD.ORDER_ID=OLID.ORDER_ID AND OD.PARTNER_ID=OLID.PARTNER_ID"
			+ " AND OLI.ORDER_ID=? AND OLI.PARTNER_ID=? AND TO_NUMBER(C.SERIAL_NUMBER) BETWEEN TO_NUMBER(?) and TO_NUMBER(?) ";

	public static final String B2B_GET_CARD_DETAILS_BY_SERIAL_NUMBER = "select c.PROXY_NUMBER,c.SERIAL_NUMBER,c.CARD_STATUS,OLID.PIN"
			+ ",OLID.PROXY_PIN_ENCR,OLI.TRACKING_NO,OLI.SHIPPING_DATETIME,c.CUSTOMER_CODE,"
			+ "c.PRODUCT_ID,FN_DMAPS_MAIN(c.CARD_NUM_ENCR) CARDNUMBER,c.CARD_NUM_HASH"
			+ ",TO_CHAR(c.EXPIRY_DATE, 'YYYY-MM-DD') EXPIRATIONDATE,c.CARD_NUM_ENCR,"
			+ "OLI.LINE_ITEM_ID,OLI.ORDER_STATUS from ORDER_DETAILS OD,order_line_item "
			+ "OLI,ORDER_LINE_ITEM_DTL OLID,card c where OD.ORDER_ID=OLI.ORDER_ID AND "
			+ "OD.PARTNER_ID=OLI.PARTNER_ID AND OD.ORDER_ID=OLID.ORDER_ID AND OD.PARTNER_ID=OLID.PARTNER_ID "
			+ "AND OLI.LINE_ITEM_ID=OLID.ORDER_LINE_ITEM_ID AND OLID.CARD_NUM_HASH=C.CARD_NUM_HASH AND "
			+ "OD.ORDER_ID=? AND OD.PARTNER_ID=? AND OLI.LINE_ITEM_ID=? AND TO_NUMBER(c.SERIAL_NUMBER)"
			+ " BETWEEN TO_NUMBER(?) and TO_NUMBER(?)";

	public static final String B2B_QUERY_DELIVERY_CHANNEL = "select FT.CHANNEL_CODE,FT.TRAN_CODE,DT.CHANNEL_TRANSACTION_CODE,t.IS_FINANCIAL,t.CREDIT_DEBIT_INDICATOR,t.TRANSACTION_DESCRIPTION from FSAPI_TRANSACTION FT,DELIVERY_CHANNEL_TRANSACTION DT,Transaction t where DT.TRANSACTION_CODE=FT.TRAN_CODE AND FT.CHANNEL_CODE=DT.CHANNEL_CODE AND t.TRANSACTION_CODE=DT.TRANSACTION_CODE and MESSAGE_TYPE='0200' AND FT.CHANNEL_CODE='17' AND FT.CHANNEL_DESC=? and FT.REQUEST_TYPE=? ";
	public static final String GET_EXPIRYDATE = "get_expiry_date_card";
	public static final String GET_TRNSACTION_TYPE = " SELECT TO_NUMBER(DECODE(IS_FINANCIAL, 'N', '0', 'F', '1')),TRANSACTION_DESCRIPTION,CREDIT_DEBIT_INDICATOR FROM TRANSACTION WHERE TRANSACTION_CODE=?  ";

	public static final String GET_ACCT_BALANCE = "select ap.AVAILABLE_BALANCE,ap.LEDGER_BALANCE,ap.CURRENCY_CODE,a.ACCOUNT_NUMBER  from card c,account_purse ap,ACCOUNT a where c.account_id=ap.account_id and c.account_id=a.account_id and card_num_hash=? ";

	public static final String TRANSCTION_LOG = " INSERT INTO transaction_log (" + "MSG_TYPE," + "RRN," + "ERROR_MSG,"
			+ "REQ_RESP_CODE," + "TERMINAL_ID," + "BUSINESS_DATE," + "TRANSACTION_CODE," + "DELIVERY_CHANNEL,"
			+ "RESPONSE_ID," + "IS_FINANCIAL," + "TRANSACTION_DESC," + "CUSTOMER_CARD_NBR_ENCR," + "CARD_NUMBER,"
			+ "IP_ADDRESS," + " TRANSACTION_TIMEZONE," + "TRANSACTION_STATUS," + "CR_DR_FLAG," + "ACCOUNT_NUMBER,"
			+ "OPENING_LEDGER_BALANCE," + "OPENING_AVAILABLE_BALANCE," + "TRAN_CURR," + "REVERSAL_CODE," + "AUTH_ID,"
			+ "PRODUCT_ID," + "TRANSACTION_SQID," + "INS_DATE," + "HASHKEY_ID," + "CORRELATION_ID," + "CARD_STATUS,"
			+ "LAST_UPD_DATE," + "TRANSACTION_DATE," + " TRANSACTION_TIME," + "ISSUER_ID," + "PARTNER_ID,"
			+ "TRANFEE_AMOUNT," + "AUTH_AMOUNT," + "TRANSACTION_AMOUNT," + "PROXY_NUMBER," + "ACCOUNT_ID," + "PURSE_ID," + "LEDGER_BALANCE," + "ACCOUNT_BALANCE" + ")"
			+ " values (?,?,?,?,?,TO_CHAR(to_date(?),'DD-MON-YY'),?,?,?,?,?,?,?,?,?,?,?,?,"
			+ "?,?,?,?,?,?,?,SYSDATE,?,?,?,SYSDATE,?,?,?,?,?,0,0,?,?,?,?,?)";

	public static final String TIMS_STAMP_QUERY = "select to_char(systimestamp,'YYYYMMDDHH24MISSFF5') timestamp,systimestamp format from dual";

	public static final String UPDATE_PASIVE_CARD = "update card set card_status=?,DATE_OF_ACTIVATION=sysdate where "
			+ "CARD_NUM_HASH=?  ";

	public static final String CARD_STATUS_CHANGE = "sp_log_cardstat_chnge";
	public static final String GET_PACKAGE_ID = "SELECT PACKAGE_ID FROM ORDER_LINE_ITEM WHERE ORDER_ID=? and LINE_ITEM_ID=? and PARTNER_ID=? ";

	public static final String GET_CVK = "SELECT po.attributes.CVV.cvkA||po.attributes.CVV.cvkB cvk FROM clp_configuration.product po  WHERE PRODUCT_ID=?";
	public static final String GET_CVV_SUPPORTED_FLAG = "SELECT  po.attributes.Product.cvvSupported cvvSupported FROM clp_configuration.product po  WHERE PRODUCT_ID=?";
	public static final String GET_SERVICE_CODE = "SELECT po.attributes.General.serviceCode srvCode FROM clp_configuration.product po  WHERE PRODUCT_ID=?";
	public static final String B2B_RELOAD_ORDER_INSERT = "insert into RELOAD_ORDER(reload_id,proxyorserial_type,proxyorserial_number,load_amnt,comments,ins_user,ins_date) values(?,?,?,?,?,?,sysdate)";
	public static final String B2B_RELOAD_ORDER_DETAILS_INSERT = "insert into reload_order_dtl(reload_id,merchant_id,postback_response,postback_url,ins_user,channel_code,inst_date) values(?,?,?,?,?,?,sysdate)";
	public static final String B2B_ORDER_ID_SEQUENCE_QRY = "select lpad(seq_orederid.nextval,6,'0')  orderid from dual";

	public static final String SP_CARD_TOPUP = "{call SP_CARD_TOPUP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String B2B_GET_RELOAD_ORDER_BY_RELOADID = "select PROXYORSERIAL_TYPE,PROXYORSERIAL_NUMBER from RELOAD_ORDER where RELOAD_ID=?";
	public static final String RELOAD_PARTNERID_QRY = "select count(1) as partnerIdCNT from clp_configuration.GROUP_ACCESS_PARTNER gapar,clp_configuration.GROUP_ACCESS_PRODUCT gapro,card c where  gapar.GROUP_ACCESS_ID = gapro.GROUP_ACCESS_ID AND C.PRODUCT_ID = gapro.PRODUCT_ID and gapar.PARTNER_ID=? ";
	public static final String PROXY_NUMBER_CHECK = "select count(1) from card where  proxy_number=?";
	public static final String SERIAL_NUMBER_CHECK = "select count(1) from card where  serial_number=?";
	public static final String GET_CARD_INFO = "SELECT C.CUSTOMER_CODE,C.CARD_NUM_ENCR,C.ACCOUNT_ID,C.PRODUCT_ID,A.ACCOUNT_NUMBER,ORDL.STATUS,C.CARD_STATUS,C.PROXY_NUMBER,"
			+ " (SELECT CURRENCY_CODE FROM CURRENCY_CODE WHERE CURRENCY_ID=AP.CURRENCY_CODE)  AS CURRENCY ,P.ISSUER_ID,AP.LEDGER_BALANCE,AP.AVAILABLE_BALANCE,FN_DMAPS_MAIN(C.CARD_NUM_ENCR),AP.PURSE_ID  "
			+ " FROM clp_configuration.PRODUCT P,CARD C,ACCOUNT A, ACCOUNT_PURSE AP," + "ORDER_LINE_ITEM_DTL ORDL "
			+ "WHERE ORDL.CARD_NUM_HASH=C.CARD_NUM_HASH"
			+ " AND A.PRODUCT_ID=C.PRODUCT_ID AND ORDL.PRODUCT_ID=C.PRODUCT_ID"
			+ " AND A.ACCOUNT_ID=C.ACCOUNT_ID AND AP.ACCOUNT_ID=A.ACCOUNT_ID AND P.PRODUCT_ID=C.PRODUCT_ID AND C.CARD_NUM_HASH=? ";
	public static final String FSAPI_BULKACT_INFO = " INSERT INTO FSAPI_BULKACT_INFO(RRN,ORDER_ID,LINEITEM_ID,POSTBACK_RESPONSE,POSTBACK_URL,RESPONSE_STATUS,INS_DATE) "
			+ " VALUES(?,?,?,?,?,?,SYSDATE)";

	public static final String ORDER_CANCEL_PROC = "{ call SP_ORDER_CANCEL_REQUEST(?,?,?,?) }";
	public static final String ORDER_CANCEL_PROCESS_PROC = "{ call SP_CANCEL_ORDER_PROCESS(?,?,?,?,?,?) }";

	public static final String STATEMENT_LOG = "sp_statements_log";

	public static final String PRODUCT_B2B_FUNDING_CHECK = "select prd.ATTRIBUTES.Product.b2bSourceOfFunding,prd.ATTRIBUTES.Product.b2bProductFunding from clp_configuration.product prd where product_id=?";

	public static final String GET_CARD_TYPE = "SELECT  po.attributes.Product.formFactor formFactor FROM clp_configuration.product po  WHERE product_id in (:productIdSet)";

	//public static final String FEE_CALC = "SPIL_TRANS_FEE_CALC";
	public static final String FEE_CALC = "{ call SPIL_TRANS_FEE_CALC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }";

	/* Limit check related queries starts */
	public static final String UPDATE_CARD_USAGE_LIMITS = "UPDATE card  SET USAGE_LIMIT = ? WHERE CARD_NUM_HASH=CLP_TRANSACTIONAL.FN_HASH(?) AND account_id = ? ";
	public static final String GET_DEL_CHANNEL_SHORTNAME = "select CHANNEL_SHORT_NAME from DELIVERY_CHANNEL where CHANNEL_CODE = ?";
	public static final String GET_TRANSACTION_SHORT_NAME = "select TRANSACTION_SHORT_NAME from TRANSACTION where TRANSACTION_CODE = ?";
	public static final String GET_CARD_LIMIT_ATTRIBUTES = "select USAGE_LIMIT from CARD where PRODUCT_ID = ? and CARD_NUM_HASH=?";
	/* Limit check related queries ends */
	public static final String REPLACERENEWAL_DTLS = "insert into REPLACERENEWAL_DTLS(REPLACERENEW_ID,REPLACERENEW_TYPE,REPLACERENEW_VALUE,REPLACERENEW_ACTION,SHIPING_METHOD,REQUEST_REASON,FEE_WAIVED,COMMENTS,SHIP_COMPANYNAME,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,EMAIL,ADDRESS_LINEONE,ADDRESS_LINETWO,ADDRESS_LINETHREE,CITY,STATE,POSTAL_CODE,COUNTRY,INS_DATE,LAST_UPD_DATE)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate,sysdate)";

	public static final String QUERYORDERDETAILS = "SELECT REPLACERENEW_TYPE,REPLACERENEW_VALUE from REPLACERENEWAL_DTLS where REPLACERENEW_ID=?";

	public static final String QUERYPROXYCOUNT = " select count(1) from CARD where  PROXY_NUMBER=?";
	public static final String QUERYSERIALCOUNT = "SELECT count(1) from CARD where  SERIAL_NUMBER=?";
	public static final String CARDREPLACEORDERID = "SELECT TO_CHAR (TO_CHAR (SYSDATE, 'YYMMDDHH24MISS')|| LPAD (CARDREPLACEID_SEQ.NEXTVAL,3,'0')) cardreplacId FROM DUAL";
	public static final String QUERYFORPARTNERID = "select count(*) as partnerIdCNT from clp_configuration.GROUP_ACCESS_PARTNER , clp_configuration.GROUP_ACCESS_PRODUCT,CARD where  GROUP_ACCESS_PARTNER.GROUP_ACCESS_ID = GROUP_ACCESS_PRODUCT.GROUP_ACCESS_ID AND GROUP_ACCESS_PRODUCT.PRODUCT_ID= CARD.PRODUCT_ID  and GROUP_ACCESS_PARTNER.PARTNER_ID=?";
	public static final String QRYFORPROCESS = "select REPLACERENEW_TYPE,REPLACERENEW_VALUE,REPLACERENEW_ACTION,SHIPING_METHOD,REQUEST_REASON,FEE_WAIVED,COMMENTS,SHIP_COMPANYNAME,FIRST_NAME,MIDDLE_NAME,LAST_NAME,PHONE,EMAIL,ADDRESS_LINEONE,ADDRESS_LINETWO,ADDRESS_LINETHREE,CITY,STATE,POSTAL_CODE,COUNTRY from REPLACERENEWAL_DTLS where REPLACERENEW_ID=?";

	// Added by Hari for Product validity Check
	public static final String CHECK_PRODUCT_VALIDITY = "SELECT COUNT(*) from clp_configuration.Product p where p.product_id=:productId and SYSDATE between to_date(p.attributes.Product.activeFrom,'mm/dd/yyyy') and nvl(to_date(p.attributes.Product.productValidityDate,'mm/dd/yyyy'),sysdate)";

	public static final String AUTH_ID = "select auth_id_seq.NEXTVAL  from dual";
	public static final String SEQ_ID = "select transaction_id_seq.NEXTVAL  from dual";
	public static final String FUNDING_CHECK_QUERY = "SELECT PRODUCT_FUNDING,FUND_AMOUNT,FUNDING_OVERRIDE,DENOMINATION  from ORDER_LINE_ITEM ORD,ORDER_LINE_ITEM_DTL ORDL WHERE ORDL.ORDER_LINE_ITEM_ID=ORD.LINE_ITEM_ID AND "
			+ "ORD.ORDER_ID=ORDL.ORDER_ID AND CARD_NUM_HASH=?";
	public static final String GET_HASH_VAL_QRY = "select fn_hash(?) hashValue from dual";
	public static final String FSAPI_SERIAL_RANGE_INFO = " INSERT INTO FSAPI_SERIAL_RANGE_INFO(RRN,FROM_SERIAL,TO_SERIAL,POSTBACK_RESPONSE,POSTBACK_URL,RESPONSE_STATUS,INST_DATE) "
			+ " VALUES(?,?,?,?,?,?,sysdate) ";
	public static final String HASHCARDQUERYFORCARDNO = "SELECT CARD_NUM_HASH,ORDER_ID,ORDER_LINE_ITEM_ID from ORDER_LINE_ITEM_DTL where PROXY_PIN_ENCR= ?";

	public static final String HASHCARDQUERYFORCARDDTLS = "SELECT PRODUCT_ID,CUSTOMER_CODE,CASE WHEN EXPIRY_DATE < SYSDATE THEN 'T' ELSE 'F' END EXPIREDFLAG,TO_CHAR(EXPIRY_DATE,'YYYY-MM-DD') EXPIRATIONDATE,EXPIRY_DATE,CARD_STATUS,fn_dmaps_main(CARD_NUM_ENCR) cardno,CARD_NUM_ENCR FROM CARD WHERE CARD_NUM_HASH=?";
	public static final String AUDITQRY = " INSERT INTO FSAPI_VIRTUALCARD_INFO(RRN,ENCRYPTED_MSG,RESPONSE_STATUS,INS_DATE)VALUES(?,?,?,sysdate)";
	public static final String POST_BACK_URL_DELETE_QRY = "DELETE FROM POSTBACK_RESTSERVICE";
	public static final String GET_CHNL_FROM_GLOBAL_PARAM = "select ATTRIBUTE_VALUE from clp_configuration.ATTRIBUTE_DEFINITION where ATTRIBUTE_NAME ='Channel' and ATTRIBUTE_GROUP ='Global Parameters'";
	public static final String POST_BACK_URL_UPDATE_QRY = "INSERT INTO POSTBACK_RESTSERVICE(NAME,URL) VALUES(?,?)";
	public static final String UPDATE_SMTP_HOST_DETAILS = "insert into EMAIL_HOST(NAME,URL) VALUES(?,?)";

	public static final String DELETE_SMTP_HOST_DETAILS = "delete from EMAIL_HOST";
	
	public static final String CHECK_DAMAGED_CARD_SERIAL ="select count(*) count from card where card_status='3' and serial_number=?";
	public static final String CHECK_DAMAGED_CARD_CARD_HASH ="select count(*) count from card where card_status='3' and account_id=(select account_id from card where card_num_hash=?)";
	public static final String B2B_CLOSE_DAMAGE_CARD = "update card set CARD_STATUS=? where card_status='3' and account_id=(select account_id from card where card_num_hash=?)";
	
	public static final String CHECK_DAMAGED_CARD_PROXY ="select count(*) count from card where card_status='3' and proxy_number=?";
 
	
	//added for getting package attributes
	public static final String GET_PACKAGE_ATTRIBUTES = "select ATTRIBUTE_NAME,ATTRIBUTE_VALUE from PACKAGE_ATTRIBUTES where UPPER(PACKAGE_ID)=UPPER(?)";
	public static final String GET_CARD_DETAILS_USING_SERIAL_NO = "SELECT p.attributes.Product.formFactor CardType,c.PRODUCT_ID,CUSTOMER_CODE,CARD_NUM_HASH,to_char(EXPIRY_DATE,'DD-MM-YYYY') EXPDATE,TO_CHAR(EXPIRY_DATE, 'YYYY-MM-DD') EXPIRATIONDATE,EXPIRY_DATE,CARD_STATUS,fn_dmaps_main(CARD_NUM_ENCR) cardNo,CARD_NUM_ENCR,ACTIVATION_CODE FROM  CARD c,clp_configuration.product p  WHERE c.product_id=p.product_id and c.SERIAL_NUMBER=?";
	
	public static final String UPDATE_CARD_STATUS = "UPDATE CARD SET CARD_STATUS= ? WHERE card_status != '20' and CARD_NUM_HASH=?";
}
