package com.incomm.scheduler.utils;

public class ScriptUtils
{
	private ScriptUtils() {
		
	}
	public static final String RETAIL_SHIPMENT_INSERT_TEMP ="insert into  cpifile_data_temp(" +
	    		          " PARTNER_ID ,MAGIC_NUMBER ,FILE_NAME ,STATUS, "+
	    		          " CARRIER ,CPI_FILE_DATE,TRACKING_NUMBER,MERCHANT_ID, " +
	    		          " MERCHANT_NAME ,STORE_LOCATION_ID, BATCH_NUMBER,CASE_NUMBER ," +
	    		          " PALLET_NUMBER ,SERIAL_NUMBER,SHIP_TO,STREET_ADDR1, "+
	    		          " STREET_ADDR2 ,CITY ,STATE ,ZIP, "+
	    		          " DC_ID, PROD_ID ,CONTACT_NAME ,CONTACT_PHONE, " +
	    		          " PROCESS_FLAG , INS_USER ,INS_DATE, " +
	    		          " LAST_UPD_USER ,LAST_UPD_DATE)" +
                          " values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,SYSDATE )";
	
	public static final String RETAIL_SHIPMENT_PROC_CALL ="{ call sp_cpi_status_update(?, ?) }";
	
	public static final String ORDER_PROC_CALL ="{ call SCHEDULE_ORDER(?,?,?) }";
	public static final String EMB_NAME = "{call sp_create_embfname(?,?,?,?,?,?,?)}";
	public static final String GET_HEADERFILE_NUMBER = "{call SP_CREATE_HEADERFILENUMBER(?,?,?,?)}";
	
	
	public static final String GET_APPROVED_ORDER_LIST = "select Order_id,LINE_ITEM_ID From ORDER_LINE_ITEM Where ORDER_STATUS='APPROVED'";
	
	public static final String UPDATE_ORDER_STATUS_IN_ORDER_DETAILS ="update ORDER_DETAILS set order_status='ORDER-IN-PROGRESS' where order_id=? and partner_id=?";
	public static final String UPDATE_ORDER_STATUS_IN_ORDER_LINE_ITEM ="update order_line_item set order_status='ORDER-IN-PROGRESS' where order_id=? and partner_id=? and line_item_id=?";
	public static final String GET_ALL_ORDERS_TO_GENERATE_CCF = "select case when ord.order_type = 'RETAIL' THEN 'RTL'  WHEN ord.order_type = 'B2B' THEN FULFILLMENT_TYPE ELSE 'IND' END as ORDERTYPE,'1' AS deliveryMethod, ord.order_id AS childOrderId,lineDtl.CARD_NUM_HASH, ord.PARENT_OID AS parentOrderId, ord.address_line1 AS shiptoaddress1, ord.address_line2 AS shiptoaddress2, ord.city AS city, ord.state AS state, ord.postal_code AS postalcode, ord.country AS country, ord.ship_to_company_name AS shiptocompanyname, lineDtl.CARD_NUM_ENCR,clp_transactional.fn_dmaps_main(lineDtl.CARD_NUM_ENCR) AS pan, TO_CHAR (lineDtl.PAN_GENERATION_DATE, 'YYMM') AS effectiveDate ,TO_CHAR (lineDtl.EXPIRY_DATE,'YYMM') AS expryDate , lineDtl.DISP_NAME AS fullName,lineDtl.STATUS,acct.ACCOUNT_NUMBER AS acctNo,to_char(lineDtl.EXPIRY_DATE,'dd-MM-yyyy') AS expDate,cards.SERIAL_NUMBER AS serialNo,cards.PRFL_CODE,lineDtl.PRODUCT_ID productId,to_char(lineItem.PACKAGE_ID) AS packageId, cards.PROXY_NUMBER AS proxyNumber, ord.ORDER_DEFAULT_CARD_STATUS, lineDtl.CUSTOMER_CODE, (select LPAD(NVL(ATTRIBUTE_VALUE,'0'),6,'0')  from PACKAGE_ATTRIBUTES PA where PA.PACKAGE_ID=packDef.PACKAGE_ID and ATTRIBUTE_NAME='logoId') logoId,ord.PROGRAM_ID as programId,(ord.FIRST_NAME||' '||LAST_NAME) as shipToName,lineItem.EMBOSSED_LINE as embName,lineItem.EMBOSSED_LINE1 as embNameTwo "
			+ "from ORDER_DETAILS ord,ORDER_LINE_ITEM_DTL lineDtl, CARD cards, ACCOUNT acct, ORDER_LINE_ITEM lineItem, PACKAGE_DEFINITION packDef  where lineDtl.ORDER_ID=ord.ORDER_ID and acct.ACCOUNT_ID=lineDtl.ACCOUNT_ID and lineDtl.PARTNER_ID= ord.PARTNER_ID and  cards.CARD_NUM_HASH=lineDtl.CARD_NUM_HASH and lineItem.LINE_ITEM_ID=lineDtl.ORDER_LINE_ITEM_ID and lineItem.ORDER_ID=ord.ORDER_ID and  lineItem.PARTNER_ID= ord.PARTNER_ID and packDef.PACKAGE_ID=lineItem.PACKAGE_ID and packDef.FULFILLMENT_VENDOR_ID=? and lineItem.ORDER_STATUS = 'ORDER-GENERATED' and ord.order_type=? order by ord.order_id,cards.SERIAL_NUMBER";	
	public static final String GET_ALL_MANUAL_ORDERS_TO_GENERATE_CCF = "\r\n" + 
			"select case when ord.order_type = 'RETAIL' THEN 'RTL'  WHEN ord.order_type = 'B2B' \r\n" + 
			"THEN FULFILLMENT_TYPE ELSE 'IND' END as ORDERTYPE,'1' AS deliveryMethod,\r\n" + 
			"custProfile.customer_id customerId, \r\n" + 
			"ord.order_id AS childOrderId,lineDtl.CARD_NUM_HASH, \r\n" + 
			"ord.PARENT_OID AS parentOrderId, ord.address_line1 AS shiptoaddress1, ord.\r\n" + 
			"address_line2 AS shiptoaddress2, ord.city AS city, ord.state AS state, \r\n" + 
			"ord.postal_code AS postalcode, ord.country AS country, \r\n" + 
			"ord.ship_to_company_name AS shiptocompanyname,\r\n" + 
			"lineDtl.CARD_NUM_ENCR,clp_transactional.fn_dmaps_main(lineDtl.CARD_NUM_ENCR) AS pan, \r\n" + 
			"TO_CHAR (lineDtl.PAN_GENERATION_DATE, 'YYMM') AS effectiveDate ,\r\n" + 
			"TO_CHAR (lineDtl.EXPIRY_DATE,'YYMM') AS expryDate , lineDtl.DISP_NAME AS fullName,lineDtl.STATUS,\r\n" + 
			"acct.ACCOUNT_NUMBER AS acctNo,to_char(lineDtl.EXPIRY_DATE,'dd-MM-yyyy') AS expDate,\r\n" + 
			"cards.SERIAL_NUMBER AS serialNo,cards.PRFL_CODE,lineDtl.PRODUCT_ID productId,  \r\n" + 
			"to_char(lineItem.PACKAGE_ID) AS packageId, cards.PROXY_NUMBER AS proxyNumber, \r\n" + 
			"ord.ORDER_DEFAULT_CARD_STATUS, lineDtl.CUSTOMER_CODE, (select LPAD(NVL(ATTRIBUTE_VALUE,'0'),6,'0')  \r\n" + 
			"from PACKAGE_ATTRIBUTES PA where PA.PACKAGE_ID=packDef.PACKAGE_ID and ATTRIBUTE_NAME='logoId') logoId,\r\n" + 
			"ord.PROGRAM_ID as programId,(ord.FIRST_NAME||' '||ord.LAST_NAME) as shipToName,\r\n" + 
			"lineItem.EMBOSSED_LINE as embName,lineItem.EMBOSSED_LINE1 as embNameTwo,\r\n" + 
			"(select fn_dmaps_main(addr.ADDRESS_ONE) || '|' || fn_dmaps_main(addr.ADDRESS_TWO) || '|' || fn_dmaps_main(addr.CITY_NAME) || ',' || addr.STATE_CODE || ',' ||fn_dmaps_main(addr.PIN_CODE) || ' ' from address addr where addr.address_id =  lineDtl.address_id and rownum < 2) as address \r\n" + 
			"from ORDER_DETAILS ord,ORDER_LINE_ITEM_DTL lineDtl, CARD cards, \r\n" + 
			"ACCOUNT acct,ORDER_LINE_ITEM lineItem, PACKAGE_DEFINITION packDef ,product p, Customer_profile custProfile \r\n" + 
			"where lineDtl.ORDER_ID=ord.ORDER_ID and acct.ACCOUNT_ID=lineDtl.ACCOUNT_ID and lineDtl.PARTNER_ID= ord.PARTNER_ID \r\n" + 
			"and  cards.CARD_NUM_HASH=lineDtl.CARD_NUM_HASH \r\n" + 
			"and cards.customer_code=custProfile.customer_code\r\n" + 
			"and lineItem.LINE_ITEM_ID=lineDtl.ORDER_LINE_ITEM_ID \r\n" + 
			"and lineItem.ORDER_ID=ord.ORDER_ID and  lineItem.PARTNER_ID= ord.PARTNER_ID\r\n" + 
			"and packDef.PACKAGE_ID=lineItem.PACKAGE_ID\r\n" + 
			"and p.product_id=lineDtl.PRODUCT_ID \r\n" + 
			"and packDef.FULFILLMENT_VENDOR_ID=? and ord.ORDER_ID=?";
	public static final String GET_COUNT_ALL_MANUAL_ORDERS_TO_GENERATE_CCF = "select COUNT(1) as cnt,max(length(cards.card_num_mask)) as panLength "
			+ "from ORDER_DETAILS ord,ORDER_LINE_ITEM_DTL lineDtl, CARD cards, ACCOUNT acct, ORDER_LINE_ITEM lineItem, PACKAGE_DEFINITION packDef  where lineDtl.ORDER_ID=ord.ORDER_ID and acct.ACCOUNT_ID=lineDtl.ACCOUNT_ID and lineDtl.PARTNER_ID= ord.PARTNER_ID and  cards.CARD_NUM_HASH=lineDtl.CARD_NUM_HASH and lineItem.LINE_ITEM_ID=lineDtl.ORDER_LINE_ITEM_ID and lineItem.ORDER_ID=ord.ORDER_ID and  lineItem.PARTNER_ID= ord.PARTNER_ID and packDef.PACKAGE_ID=lineItem.PACKAGE_ID and packDef.FULFILLMENT_VENDOR_ID=? and ord.ORDER_ID=? ";
	public static final String GET_ALL_MANUAL_ORDERS_INFO_TO_GENERATE_CCF = "select case when ord.order_type = 'RETAIL' THEN 'RTL'  WHEN ord.order_type = 'B2B' THEN FULFILLMENT_TYPE ELSE 'IND' END as ORDERTYPE\r\n" + 
			"			,'1' AS deliveryMethod, ord.order_id AS childOrderId,\r\n" + 
			"			ord.PARENT_OID AS parentOrderId, ord.address_line1 AS shiptoaddress1, ord.address_line2 AS shiptoaddress2, ord.city AS city, ord.state AS state, ord.postal_code AS postalcode, ord.country AS country, ord.ship_to_company_name AS shiptocompanyname, lineItem.PRODUCT_ID ,to_char(lineItem.PACKAGE_ID) AS packageId, (select LPAD(NVL(ATTRIBUTE_VALUE,'0'),6,'0')  from PACKAGE_ATTRIBUTES PA where PA.PACKAGE_ID=packDef.PACKAGE_ID and ATTRIBUTE_NAME='logoId') logoId,ord.PROGRAM_ID as programId,(ord.FIRST_NAME||' '||ord.LAST_NAME) as shipToName,lineItem.EMBOSSED_LINE as embName,lineItem.EMBOSSED_LINE1 as embNameTwo\r\n" + 
			"			 ,po.attributes.CVV.cvkA||po.attributes.CVV.cvkB cvk,po.attributes.Product.cvvSupported cvvSupported,po.attributes.Product.ccfFormatVersion ccfFormatversion, \r\n" + 
			"             po.attributes.General.serviceCode srvCode,\r\n" + 
			"              CASE\r\n" + 
			"                WHEN ord.order_type = 'RETAIL' THEN \r\n" + 
			"                po.attributes.\"Product\".retailUPC \r\n" + 
			"                WHEN ord.order_type = 'B2B'    THEN po.attributes.\"Product\".b2bUpc\r\n" + 
			"                END\r\n" + 
			"                as upc\r\n" + 
			"           	from ORDER_DETAILS ord,ORDER_LINE_ITEM lineItem, PACKAGE_DEFINITION packDef,product po   \r\n" + 
			"			where lineItem.ORDER_ID=ord.ORDER_ID and  lineItem.PARTNER_ID= ord.PARTNER_ID and lineItem.product_id = po.product_id  \r\n" + 
			"			and packDef.PACKAGE_ID=lineItem.PACKAGE_ID  and packDef.FULFILLMENT_VENDOR_ID=? and ord.ORDER_ID=?";
	public static final String GET_CCF_VERSION_DETAILS= "select version_name||':'||record_type AS v1,record_type AS v2, format_type As v3,value_Key AS v4,data_title As v5,data_value As v6,data_length As v7,data_format AS v8, data_filler As v9,data_filler_side v10 from CCF_CONF_DETAIL where VERSION_NAME=? AND RECORD_TYPE=? order by data_seqno";
	
	public static final String GET_CCF_FORMAT_VERSION= "SELECT  po.attributes.Product.ccfFormatVersion ccfFormatversion FROM product po  WHERE PRODUCT_ID=?";
	public static final String GET_SERVICE_CODE="SELECT po.attributes.General.serviceCode srvCode FROM product po  WHERE PRODUCT_ID=?";
			public static final String UPDATE_CARD_STATUS="update order_line_item_dtl set ccf_file_name=?, status=?,CCF_GEN_DATE=sysdate where card_num_hash=?";
			public static final String UPDATE_CARD="update card set card_status=? where card_num_hash=?";
			public static final String UPDATE_ORDER_STATUS_ORDER_DETAILS="update order_details set order_status=? where order_id=?";
			public static final String UPDATE_ORDER_STATUS_ORDER_LINE_ITEM="update order_line_item set order_status=? where order_id=?";
			public static final String UPDATE_ORDER_STATUS_ORDER_LINE_ITEM_ORDER_ID="update order_line_item set order_status=?, CCF_FLAG=? where order_id=? and LINE_ITEM_ID=?";
			
			public static final String GET_ADDRESS_DETAILS = "select fn_dmaps_main(addr.ADDRESS_ONE) addressOne,fn_dmaps_main(addr.ADDRESS_TWO) addressTwo,fn_dmaps_main(addr.CITY_NAME) addressCity,fn_dmaps_main(addr.PIN_CODE) pinCode,addr.STATE_CODE stateCode from address addr ,ORDER_LINE_ITEM_DTL orderDetail where addr.ADDRESS_ID=orderDetail.ADDRESS_ID AND orderDetail.CARD_NUM_HASH=?";
			public static final String GET_CVK = "SELECT po.attributes.CVV.cvkA||po.attributes.CVV.cvkB cvk FROM product po  WHERE PRODUCT_ID=?";
			public static final String GET_CVV_SUPPORTED_FLAG = "SELECT  po.attributes.Product.cvvSupported cvvSupported FROM product po  WHERE PRODUCT_ID=?";
			//modified query for non-individual ccf
			public static final String GET_ALL_COMPLETED_ORDERS = "SELECT distinct ol.order_ID,ol.LINE_ITEM_ID,od.individual_ccf FROM order_line_item ol,order_details od,clp_configuration.product p where ol.order_id=od.order_id and ol.product_id=p.product_id and ol.order_status='ORDER-GENERATED' and upper(od.order_type)=upper(?) and upper(p.attributes.Product.formFactor)=upper('Physical') and (od.individual_ccf is null OR upper(od.individual_ccf)!=upper('true'))";
	public static final String GET_ALL_COMPLETED_ORDERS_LIST = "SELECT distinct ol.order_ID FROM order_line_item ol,order_details od,product p where ol.order_id=od.order_id and ol.product_id=p.product_id and ol.order_status='ORDER-GENERATED' and upper(od.order_type)=upper(?) and upper(p.attributes.Product.formFactor)=upper('Physical') order by ol.order_id";
	public static final String GET_ALL_VENDOR_LINKED_TO_ORDER = "select distinct pd.FULFILLMENT_VENDOR_ID AS FULFILLMENT_VENDOR_ID from package_definition pd, order_line_item oli where oli.PACKAGE_ID=pd.PACKAGE_ID and oli.order_id=?";
			
			
			public static final String GET_VENDOR_NAME = "select FULFILLMENT_VENDOR_NAME from fulfillment_vendor where FULFILLMENT_VENDOR_ID=?";
			
			
			public static final String GET_PRODUCT_ID_AND_ORDER_TYPE = "select od.ORDER_TYPE as orderType, to_char(oli.PRODUCT_ID) as productId from order_details od, order_line_item oli, package_definition pd where od.order_id=oli.order_id and od.partner_id=oli.partner_id and oli.package_id=pd.package_id and od.order_id=? and pd.FULFILLMENT_VENDOR_ID=?";
			public static final String CN_FILE_STATUS_INSERT = "insert into cn_file_status(file_name,status,error_msg,ins_date)values(?,?,?,SYSDATE)";
			public static final String CN_FILE_STATUS_UPDATE = "update cn_file_status set status=?,error_msg=?,ins_date=sysdate where upper(file_name)=upper(?)";
			public static final String CHECK_DUPLICATE_FILE = "SELECT COUNT(1) FROM cn_file_status WHERE UPPER(FILE_NAME) = UPPER(?) ";
			public static final String CHECK_RETURN_FILE_DUPLICATE_FILE = "SELECT COUNT(1) FROM return_fileupload_dtls WHERE UPPER(FILE_NAME) = UPPER(?) ";
			public static final String CHECK_SERIAL_FILE_DUPLICATE_FILE = "SELECT COUNT(1) FROM serial_fileupload_dtls WHERE UPPER(FILE_NAME) = UPPER(?) ";
			public static final String CHECK_SHIPMENT_FILE_DUPLICATE_FILE = "SELECT COUNT(1) FROM shipment_fileupload_dtls WHERE UPPER(FILE_NAME) = UPPER(?) ";
			public static final String CHECK_RESPONSE_FILE_DUPLICATE_FILE = "SELECT COUNT(1) FROM response_fileupload_dtls WHERE UPPER(FILE_NAME) = UPPER(?) ";
			
			public static final String GET_CN_FILE_STATUS_LIST = "select file_name,status,error_msg,to_char(ins_date,'DD-MM-YYYY') as ins_date from cn_file_status order by ins_date desc";
			
			
			public static final String UPDATE_ERROR_LOG_TBL="insert into FILEUPLOAD_ERROR_DATA(FILE_NAME,JOB_NAME,FILE_RECORD_DATA,ERROR_DESC,LAST_UPD_DATE) values(?,?,?,?,sysdate)";
			
			public static final String GET_SCHEDULER_JOB_CONFIG_BY_JOBID="select PROCESS_ID,PROCESS_NAME,PROCESS_INTERVAL,SCHEDULE_DAYS,START_HOUR,START_MIN,FILE_ID,PROCESS_TYPE,PROCINTERVAL_TYPE,RETRY_CNT,SCHEDULER_STAT,MAIL_SUCCESS,MAIL_FAIL,PROC_RUNNING,PROCRETRY_DATE,DAYOF_MONTH,MULTIRUN_INTERVAL,MULTIRUN_INTERVAL_TYPE,MULTIRUN_FLAG,PROCESS_CLASS FROM PROCESS_SCHEDULE where PROCESS_ID=?";
			public static final String GET_SCHEDULER_JOB_CONFIG="select PROCESS_ID,PROCESS_NAME,PROCESS_INTERVAL,SCHEDULE_DAYS,START_HOUR,START_MIN,FILE_ID,PROCESS_TYPE,PROCINTERVAL_TYPE,RETRY_CNT,SCHEDULER_STAT,MAIL_SUCCESS,MAIL_FAIL,PROC_RUNNING,PROCRETRY_DATE,DAYOF_MONTH,MULTIRUN_INTERVAL,MULTIRUN_INTERVAL_TYPE,MULTIRUN_FLAG,PROCESS_CLASS FROM PROCESS_SCHEDULE";
			
			//Return File
			public static final String RETURN_FILE_STATUS_INSERT = "insert into return_fileupload_dtls(file_name,file_status,failure_desc,ins_user,ins_date,last_upd_user,last_upd_date)values(?,?,?,1,SYSDATE,1,SYSDATE)";
			public static final String RETURN_FILE_STATUS_UPDATE = "update return_fileupload_dtls set file_status=?,failure_desc=?,ins_date=sysdate,ins_user=1,last_upd_user=1,last_upd_date=sysdate where upper(file_name)=upper(?)";
			
	public static final String RETURN_FILE_INSERT_TEMP = "insert into  RETURNFILE_DATA_STG(" +
  		          " FILE_NAME ,CUSTOMER_DESC ,SHIP_SUFFIX_NO ,PARENT_ORDER_ID, "+
  		          " CHILD_ORDER_ID ,SERIAL_NUMBER,REJECT_CODE,REJECT_REASON, " +
  		          " FILE_DATE ,CARD_TYPE, CLIENT_ORDER_ID,PROCESS_FLAG ," +
  		          " ROW_ID ,ERROR_DESC, INS_USER ,LAST_UPD_USER,INS_DATE, " +
  		          " LAST_UPD_DATE)" +
                    " values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,SYSDATE )";
			
			//ublic static final String RETURN_FILE_PROC_CALL ="SP_RETURN_FILE_PROCESS";
			public static final String RETURN_FILE_PROC_CALL ="{ call SP_RETURN_FILE_PROCESS(?,?) }";
			
			//Response File
			
			public static final String RESPONSE_FILE_STATUS_INSERT = "insert into response_fileupload_dtls(file_name,file_status,failure_desc,ins_user,ins_date,last_upd_user,last_upd_date)values(?,?,?,1,SYSDATE,1,SYSDATE)";
			public static final String RESPONSE_FILE_STATUS_UPDATE = "update response_fileupload_dtls set file_status=?,failure_desc=?,ins_date=sysdate,ins_user=1,last_upd_user=1,last_upd_date=sysdate where upper(file_name)=upper(?)";
			
	public static final String RESPONSE_FILE_INSERT_TEMP = "insert into  RESPONSEFILE_DATA_STG(" +
	    		          " MAGIC_NUMBER ,FILE_NAME ,STATUS, "+
	    		          " CARRIER ,FILE_DATE,TRACKING_NUMBER,MERCHANT_ID, " +
	    		          " MERCHANT_NAME ,STORELOCATIONID, BATCH_NUMBER,CASE_NUMBER ," +
	    		          " PALLET_NUMBER ,SERIAL_NUMBER,SHIP_TO,STREET_ADDR1, "+
	    		          " STREET_ADDR2 ,CITY ,STATE ,ZIP, "+
	    		          " DC_ID, PROD_ID ,ORDER_ID ,PARENT_SERIAL_NUMBER, " +
	    		          " PROCESS_FLAG ,ROW_ID,ERROR_DESC, INS_USER ,INS_DATE, " +
	    		          " LAST_UPD_USER ,LAST_UPD_DATE)" +
                          " values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,SYSDATE,1,SYSDATE )";
			
			//public static final String RESPONSE_FILE_PROC_CALL ="SP_RESPONSE_FILE_PROCESS";
			
			public static final String RESPONSE_FILE_PROC_CALL ="{ call SP_RESPONSE_FILE_PROCESS(?,?) }";
			
			//Serial Number File Upload

			public static final String SERIALNUMBER_FILE_STATUS_INSERT = "insert into serial_fileupload_dtls(file_name,file_status,failure_desc,ins_user,ins_date,last_upd_user,last_upd_date)values(?,?,?,1,SYSDATE,1,SYSDATE)";
			public static final String SERIALNUMBER_FILE_STATUS_UPDATE = "update serial_fileupload_dtls set file_status=?,failure_desc=?,ins_date=sysdate,ins_user=1,last_upd_user=1,last_upd_date=sysdate where upper(file_name)=upper(?)";
			
	public static final String SERIALNUMBER_FILE_INSERT_TEMP = "insert into  SERIAL_DETAILS_STG ("
			+ " FILE_NAME,PRODUCT_ID,SERIAL_NUMBER,VAN,PROCESS_FLAG,ROW_ID,ERROR_DESC,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE)"
			+
                    " values ( ?,?,?,?,?,?,?,1,SYSDATE,1,SYSDATE )";
			
			//public static final String SERIALNUMBER_FILE_PROC_CALL ="SP_SERIALNUMBER_FILE_PROCESS";
			
			public static final String SERIALNUMBER_FILE_PROC_CALL ="{ call SP_SERIALNUMBER_FILE_PROCESS(?,?) }";
			
			public static final String UPDATE_PROCESS_SCHEDULE_STATUS ="update PROCESS_SCHEDULE set PROC_RUNNING=?,PROCCOMPLETE_FLAG=?,PROCCOMPLETE_DATE=? where PROCESS_ID=?";		

			//Shipment File Upload
			
	public static final String SHIPMENT_FILE_INSERT_TEMP = "insert into  SHIPMENTFILE_DATA_STG (" +
	  		          " FILE_NAME,CUSTOMER_DESC,SOURCEONE_BATCH_NO,PARENT_ORDER_ID,CHILD_ORDER_ID,FILE_DATE,SERIAL_NUMBER,"
			+ "CARD,PACKAGE_ID,CARD_TYPE,CONTACT_NAME,SHIP_TO,ADDRESS_ONE,ADDRESS_TWO,CITY,STATE,"
	  		          + "ZIP,TRACKING_NUMBER,SHIP_DATE,SHIPMENT_ID,SHIPMENT_METHOD,PROCESS_FLAG,ROW_ID,ERROR_DESC,INS_USER,"
	  		          + "INS_DATE,LAST_UPD_USER,LAST_UPD_DATE)" +
	                    " values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,1,SYSDATE,1,SYSDATE )";
			
			//public static final String SHIPMENT_FILE_PROC_CALL ="SP_SHIPMENT_FILE_PROCESS";
			public static final String SHIPMENT_FILE_PROC_CALL ="{ call SP_SHIPMENT_FILE_PROCESS(?,?) }";
			
			public static final String SHIPMENT_FILE_STATUS_INSERT = "insert into shipment_fileupload_dtls(file_name,file_status,failure_desc,ins_user,ins_date,last_upd_user,last_upd_date)values(?,?,?,1,SYSDATE,1,SYSDATE)";
			public static final String SHIPMENT_FILE_STATUS_UPDATE = "update shipment_fileupload_dtls set file_status=?,failure_desc=?,ins_date=sysdate,ins_user=1,last_upd_user=1,last_upd_date=sysdate where upper(file_name)=upper(?)";
			
			
			public static final String GET_ALL_VENDOR_LINKED_TO_ORDER_LINE_ITEM = "select pd.FULFILLMENT_VENDOR_ID AS FULFILLMENT_VENDOR_ID from package_definition pd, order_line_item oli where oli.PACKAGE_ID=pd.PACKAGE_ID and oli.order_id=? and oli.LINE_ITEM_ID=?";
			public static final String GET_REPL_ALL_VENDOR_LINKED_TO_ORDER_LINE_ITEM = "select fulfillment_vendor_id from package_definition where package_id = (select pd.replacment_package_id from package_definition pd, order_line_item oli where pd.package_id = oli.package_id and oli.order_id=? and oli.line_item_id=?)";
			public static final String GET_PRODUCT_ID_AND_ORDER_TYPE_BY_ORDER_LINE_ITEM = "select od.ORDER_TYPE as orderType, to_char(oli.PRODUCT_ID) as productId from order_details od, order_line_item oli, package_definition pd where od.order_id=oli.order_id and od.partner_id=oli.partner_id and oli.package_id=pd.package_id and od.order_id=? and pd.FULFILLMENT_VENDOR_ID=? and oli.LINE_ITEM_ID=?";
			public static final String GET_ALL_ORDERS_TO_GENERATE_CCF_ORDER_LINE_ITEM = "select  NVL(TO_CHAR (ord.LOCATION_ID), '*'),'BLK' orderType,'1' AS deliveryMethod, lineDtl.CARD_NUM_HASH, lineDtl.CARD_NUM_ENCR,clp_transactional.fn_dmaps_main(lineDtl.CARD_NUM_ENCR) AS pan, TO_CHAR (lineDtl.PAN_GENERATION_DATE, 'YYMM') AS effectiveDate ,TO_CHAR (lineDtl.EXPIRY_DATE,'YYMM') AS expryDate , lineDtl.DISP_NAME AS fullName,lineDtl.STATUS,acct.ACCOUNT_NUMBER AS acctNo,to_char(lineDtl.EXPIRY_DATE,'dd-MM-yyyy') AS expDate,cards.SERIAL_NUMBER AS serialNo,cards.PRFL_CODE,lineDtl.PRODUCT_ID ,to_char(lineItem.PACKAGE_ID) AS packageId, cards.PROXY_NUMBER AS proxyNumber, ord.ORDER_DEFAULT_CARD_STATUS, lineDtl.CUSTOMER_CODE, (select LPAD(NVL(ATTRIBUTE_VALUE,'0'),6,'0')  from PACKAGE_ATTRIBUTES PA where PA.PACKAGE_ID=packDef.PACKAGE_ID and ATTRIBUTE_NAME='logoId') logoId  from ORDER_DETAILS ord,ORDER_LINE_ITEM_DTL lineDtl, CARD cards, ACCOUNT acct, ORDER_LINE_ITEM lineItem, PACKAGE_DEFINITION packDef  where lineDtl.ORDER_ID=ord.ORDER_ID and acct.ACCOUNT_ID=lineDtl.ACCOUNT_ID and lineDtl.PARTNER_ID= ord.PARTNER_ID and  cards.CARD_NUM_HASH=lineDtl.CARD_NUM_HASH and lineItem.LINE_ITEM_ID=lineDtl.ORDER_LINE_ITEM_ID and lineItem.ORDER_ID=ord.ORDER_ID and  lineItem.PARTNER_ID= ord.PARTNER_ID and packDef.PACKAGE_ID=lineItem.PACKAGE_ID and packDef.FULFILLMENT_VENDOR_ID=? and ord.ORDER_ID=? AND lineDtl.ORDER_LINE_ITEM_ID=?";		
			public static final String GET_REPL_ALL_ORDERS_TO_GENERATE_CCF_ORDER_LINE_ITEM = "SELECT clp_transactional.fn_dmaps_main(cards.card_num_encr) AS pan, cards.serial_number AS serialno, 'IND' ordertype, linedtl.card_num_encr,"
					+ " linedtl.card_num_hash, nvl(TO_CHAR(cards.replace_exprydt,'YYMM'),TO_CHAR(cards.expiry_date,'YYMM') ) AS exprydate,acct.account_number AS acctno,TO_CHAR(linedtl.pan_generation_date,'YYMM') AS effectivedate,linedtl.product_id,"
					+ "cards.prfl_code,TO_CHAR(lineitem.package_id) AS packageid,cards.proxy_number AS proxynumber,cards.card_status cardstat,linedtl.customer_code,pa.attribute_value AS deliverymethod,(SELECT lpad( nvl(attribute_value,'0'),6,'0') FROM "
					+ "package_attributes pa WHERE pa.package_id = packdef.package_id  AND attribute_name = 'logoId') logoid,nvl( TO_CHAR(ord.location_id), '*'),ord.order_id AS childorderid, ord.parent_oid AS parentorderid,ord.address_line1 AS shiptoaddress1,"
					+ "ord.address_line2 AS shiptoaddress2,ord.city AS city,ord.state AS state,ord.postal_code AS postalcode,ord.country AS country,ord.ship_to_company_name AS shiptocompanyname,ord.order_type AS ordertype,linedtl.disp_name AS fullname,"
					+ "linedtl.status,TO_CHAR(linedtl.expiry_date,'dd-MM-yyyy') AS expdate,ord.order_default_card_status,cards.repl_flag,ap.AVAILABLE_BALANCE acctBal FROM order_details ord, order_line_item_dtl linedtl, card cards, account acct, order_line_item lineitem,"
					+ " package_definition packdef, package_attributes pa,ACCOUNT_PURSE ap WHERE  linedtl.order_id = ord.order_id AND acct.account_id = linedtl.account_id  AND ap.account_id = acct.account_id  AND linedtl.partner_id = ord.partner_id AND "
					+ "cards.card_num_hash = linedtl.card_num_hash AND lineitem.line_item_id = linedtl.order_line_item_id  AND  lineitem.order_id = ord.order_id AND   lineitem.partner_id = ord.partner_id AND  packdef.package_id = lineitem.package_id  AND pa.package_id = lineitem.package_id"
					+ " AND  pa.attribute_name = 'replacementshipMethods' AND packdef.fulfillment_vendor_id =? AND ord.order_id =? AND  linedtl.order_line_item_id = ? AND nvl(cards.repl_flag,0) <> 0 AND linedtl.ccf_file_name IS NULL AND linedtl.ccf_gen_date IS NULL";
			public static final String MAIL_ID_LIST = "SELECT user_email FROM clp_user where user_id in (select regexp_substr(?,'[^|]+', 1, level) from dual connect by regexp_substr (?, '[^|]+', 1, level) is not null)";

			public static final String GET_REPL_CCF_FORMAT_VERSION = "SELECT  po.attributes.Product.ccfFormatVersion ccfFormatversion FROM product po WHERE PRODUCT_ID=?  AND po.attributes.Product.productType = 'B2B'";

			/*
			 * Date: 12 Feb 2019
			 * Modified procedure name to SP_MAINTENANCE_FEE_SCHEDULER from SP_MAINTANENANCE_FEE_SCHEDULER
			 */
			public static final String SP_MAINTANENANCE_FEE_SCHEDULER = "{ call SP_MAINTENANCE_FEE_SCHEDULER(?,?) }";
			public static final String SP_CALC_WEEKLY_FEES = "{ call SP_CALC_WEEKLY_FEES(?,?) }";
			public static final String SP_CALC_MONTHLY_FEES = "{ call SP_CALC_MONTHLY_FEES(?,?) }";
			public static final String SP_CALC_ANNUAL_FEES = "{ call SP_CALC_ANNUAL_FEES(?,?) }";
			public static final String SP_CALC_INACTIVE_FEES = "{ call PKG_DAILY_JOBS.sp_passive_calc(?,?) }";
			public static final String SP_CALC_PASSIVE_PERIOD = "{ call SP_EOD_PASSIVEPERIOD_CALC(?,?) }";
			public static final String SP_CALC_CHANGE_CARD_STATUS = "{ call sp_chng_cardstat_shipped(?,?,?,?) }";


			public static final String UPDATE_REPL_CARD = "update card set card_status=? where card_num_hash=?";

			public static final String GET_CCF_FILES_TO_DELETE = "select EMB_FNAME,VENDOR_NAME from CCF_FILE_CTRL where to_date(CREATE_DATE,'dd-mm-yy') = to_date((sysdate-?),'dd-mm-yy')";

			public static final String GET_PRODUCTNAME_BY_ORDERID = "select p.PRODUCT_NAME from  clp_order.ORDER_LINE_ITEM olt,clp_order.ORDER_DETAILS ol,product p where ol.order_id = olt.order_id and olt.product_id=p.product_id and ol.order_id=?";

			public static final String GET_USERID_BY_ORDERID = "select INS_USER from ORDER_DETAILS where ORDER_ID = ? ";
			
			//added query for individual ccf required for orders
			public static final String GET_ALL_COMPLETED_ORDERS_TO_SEPARATE_CCF = "SELECT distinct ol.order_ID,ol.LINE_ITEM_ID,ol.package_id,ol.product_id,od.individual_ccf FROM order_line_item ol,order_details od,clp_configuration.product p where ol.order_id=od.order_id and ol.product_id=p.product_id and ol.order_status='ORDER-GENERATED' and upper(od.order_type)=upper(?) and upper(p.attributes.Product.formFactor)=upper('Physical') and upper(od.individual_ccf)=upper('true')";
			
			public static final String GET_ALL_ORDERS_TO_GENERATE_SEPARATE_CCF = "select case when ord.order_type = 'RETAIL' THEN 'RTL'  WHEN ord.order_type = 'B2B' THEN FULFILLMENT_TYPE ELSE 'IND' END as ORDERTYPE,'1' AS deliveryMethod, ord.order_id AS childOrderId,lineDtl.CARD_NUM_HASH, ord.PARENT_OID AS parentOrderId, ord.address_line1 AS shiptoaddress1, ord.address_line2 AS shiptoaddress2, ord.city AS city, ord.state AS state, ord.postal_code AS postalcode, ord.country AS country, ord.ship_to_company_name AS shiptocompanyname, lineDtl.CARD_NUM_ENCR,clp_transactional.fn_dmaps_main(lineDtl.CARD_NUM_ENCR) AS pan, TO_CHAR (lineDtl.PAN_GENERATION_DATE, 'YYMM') AS effectiveDate ,TO_CHAR (lineDtl.EXPIRY_DATE,'YYMM') AS expryDate , lineDtl.DISP_NAME AS fullName,lineDtl.STATUS,acct.ACCOUNT_NUMBER AS acctNo,to_char(lineDtl.EXPIRY_DATE,'dd-MM-yyyy') AS expDate,cards.SERIAL_NUMBER AS serialNo,cards.PRFL_CODE,lineDtl.PRODUCT_ID ,to_char(lineItem.PACKAGE_ID) AS packageId, cards.PROXY_NUMBER AS proxyNumber, ord.ORDER_DEFAULT_CARD_STATUS, lineDtl.CUSTOMER_CODE, (select LPAD(NVL(ATTRIBUTE_VALUE,'0'),6,'0')  from PACKAGE_ATTRIBUTES PA where PA.PACKAGE_ID=packDef.PACKAGE_ID and ATTRIBUTE_NAME='logoId') logoId,ord.PROGRAM_ID as programId,(ord.FIRST_NAME||' '||LAST_NAME) as shipToName,lineItem.EMBOSSED_LINE as embName,lineItem.EMBOSSED_LINE1 as embNameTwo from ORDER_DETAILS ord,ORDER_LINE_ITEM_DTL lineDtl, CARD cards, ACCOUNT acct, ORDER_LINE_ITEM lineItem, PACKAGE_DEFINITION packDef  where lineDtl.ORDER_ID=ord.ORDER_ID and acct.ACCOUNT_ID=lineDtl.ACCOUNT_ID and lineDtl.PARTNER_ID= ord.PARTNER_ID and  cards.CARD_NUM_HASH=lineDtl.CARD_NUM_HASH and lineItem.LINE_ITEM_ID=lineDtl.ORDER_LINE_ITEM_ID and lineItem.ORDER_ID=ord.ORDER_ID and  lineItem.PARTNER_ID= ord.PARTNER_ID and packDef.PACKAGE_ID=lineItem.PACKAGE_ID and packDef.FULFILLMENT_VENDOR_ID=? and lineItem.ORDER_STATUS = 'ORDER-GENERATED' and ord.order_type=? and ord.order_id=? order by ord.order_id,cards.SERIAL_NUMBER";

			//Added by Hari for truncating the table dynamically using procedure call
			public static final String TRUNCATE = "{ call sp_call_truncate(?,?,?) }";
			
			public static final String UPDATE_CARD_STATUS_ALL="update order_line_item_dtl set ccf_file_name=?, status=?, CCF_GEN_DATE=sysdate where order_id = ?";
			public static final String UPDATE_CARD_ALL="update card set card_status=? where card_num_hash in (select card_num_hash from order_line_item_dtl where order_id = ? )";

			public static final String GET_ALL_VENDOR_LINKED_TO_ORDER_AND_PACKAGE = "select distinct pd.FULFILLMENT_VENDOR_ID AS FULFILLMENT_VENDOR_ID from package_definition pd, order_line_item oli where oli.PACKAGE_ID=pd.PACKAGE_ID and oli.order_id=? and oli.PACKAGE_ID=?";
			
			public static final String SP_DIGITAL_CARD_PROCESSING = "{ call clp_order.PKG_DIGITAL_ORDER_PROCESS.SCHEDULE_PROCESS_DIGITAL(?,?,?) }";

			public static final String GET_ALL_DIGITAL_PRODUCTS = "SELECT  po.product_id productId,po.attributes.Product.digitalInvAutoReplLvl reorderLevel,po.attributes.Product.initialDigitalInvQty InitialOrder,po.attributes.Product.digitalInvAutoReplQty reorderValue FROM product po WHERE po.attributes.Product.formFactor = 'Digital'  and po.attributes.General.maxCardBalance is not null and length(po.attributes.General.maxCardBalance) > 0";
			
			public static final String CHECK_DUPLICATE_BLK_TXN_FILE = "SELECT COUNT(1) FROM bulk_file_upload_dtls WHERE UPPER(FILE_NAME) = UPPER(?) ";
			
			public static final String Get_RECORD_COUNT_BATCH_ID = "SELECT COUNT(1) FROM Bulk_req_resp_logs WHERE Batch_id = ? ";
			
			public static final String INSERT_BLK_TXN_FILE = "insert into bulk_file_upload_dtls (error_msg,file_name,ins_date,last_upd_date,status,batch_id) values ('OK',?,sysdate,sysdate,'IN-PROGRESS',?) ";
			public static final String UPDATE_BLK_TXN_FILE = "update Bulk_req_resp_logs set response_code=?,response_message=?,transaction_desc=?,card_status=?,available_balance=?,last_upd_date=sysdate,transaction_date=?,transaction_time=? where batch_id=? and record_num=? ";
			public static final String INSERT_BLK_TXN = "insert into Bulk_req_resp_logs(batch_id,record_num,Source_Reference_Number,file_name,sp_number_encr,action,amount,mdm_id,store_id,terminal_id) values (?,CLP_TRANSACTIONAL.BULK_RECORD_NUM_SEQ.NEXTVAL,?,?,clp_transactional.fn_emaps_main(?),?,?,?,?,?)";
			public static final String GET_ALL_REQUEST_RECORDS = "SELECT SOURCE_REFERENCE_NUMBER,clp_transactional.fn_dmaps_main(SP_NUMBER_ENCR) AS SP_NUMBER,ACTION,AMOUNT,mdm_id,STORE_ID,TERMINAL_ID,BATCH_ID,FILE_NAME from BULK_REQ_RESP_LOGS WHERE ";
			public static final String GET_THREAD_POOL_CONFIG = "select attribute_name, attribute_value from attribute_definition where attribute_name in ('chunkSize','threadPoolSize','maxThreadPoolSize') ";
			public static final String GET_ALL_RESPONSE_RECORDS = "SELECT batch_id,SOURCE_REFERENCE_NUMBER,clp_transactional.fn_dmaps_main(SP_NUMBER_ENCR) AS SP_NUMBER,AMOUNT,available_balance,mdm_id,STORE_ID,TERMINAL_ID,response_code,response_message,TRANSACTION_DATE,TRANSACTION_TIME,status_desc as cardStatus,TRANSACTION_DESC from BULK_REQ_RESP_LOGS bq, card_status c WHERE c.status_code = bq.card_status and ";
			public static final String GET_COMPLETED_RECORD_COUNT = "SELECT COUNT(1) FROM Bulk_req_resp_logs WHERE UPPER(FILE_NAME) = UPPER(?) and response_code is null";
			public static final String BLK_TXN_FILE_STATUS_UPDATE = "update bulk_file_upload_dtls set status=?,error_msg=?,last_upd_date=sysdate where upper(file_name)=upper(?)";
			
			public static final String GET_BATCH_ID  = "select CLP_TRANSACTIONAL.batch_id_seq.NEXTVAL from dual";
			public static final String GET_UNPROCESSED_RECORDS  = "(SELECT SOURCE_REFERENCE_NUMBER,RECORD_NUM,clp_transactional.fn_dmaps_main(SP_NUMBER_ENCR) AS SP_NUMBER,ACTION,AMOUNT,mdm_id,STORE_ID,TERMINAL_ID,BATCH_ID,FILE_NAME,ROW_NUMBER() OVER(ORDER BY RECORD_NUM) AS rownumber FROM BULK_REQ_RESP_LOGS WHERE BATCH_ID =:batchid AND RESPONSE_CODE IS NULL)";
			public static final String GET_UNPROCESSED_RECORDS_WHERE_CLAUSE  =" WHERE rownumber BETWEEN :fromRow AND :toRow";
			
			
			//Currency Converstion
			public static final String INSERT_CURRENCY_RATE_FILE = "insert into file_upload_status (error_msg,file_name,ins_date,last_upd_date,status,batch_id,file_type,ins_user) values ('OK',?,sysdate,sysdate,'IN-PROGRESS',?,'CurrRate',1) ";
			public static final String UPDATE_CURRENCY_CONVERSION_STAGE = "update Currency_Conversion_Stage set STATUS=?,ERROR_MESSAGE=?,LAST_UPD_DATE=sysdate,LAST_UPD_USER='cclBatch' where batch_id=? and RECORD_NUM=? ";
			public static final String INSERT_CURRENCY_CONVERSION_STAGE = "insert into Currency_Conversion_Stage(File_Name,RECORD_NUM,MDM_ID,ISSUING_CURRENCY,TRANSACTION_CURRENCY,CONVERSION_RATE,EFFECTIVE_DATE,ACTION,STATUS,INS_DATE,INS_USER,LAST_UPD_DATE,LAST_UPD_USER,CHANNEL,batch_id,external_user) values (?,CLP_TRANSACTIONAL.BULK_RECORD_NUM_SEQ.NEXTVAL,?,?,?,?,?,?,'N',sysdate,'1',sysdate,'cclBatch','Job',?,'cclBatch')";
			public static final String GET_COMPLETED_CURRENCY_RATE_RECORD_COUNT = "SELECT COUNT(1) FROM Currency_Conversion_Stage WHERE UPPER(FILE_NAME) = UPPER(?) and Status='N'";
			public static final String CURRENCY_RATE_FILE_STATUS_UPDATE = "update file_upload_status set status=?,error_msg=?,last_upd_date=sysdate where upper(file_name)=upper(?) and file_type='CurrRate'";
			
			public static final String GET_CURRENCY_RATE_BATCH_ID  = "select CLP_TRANSACTIONAL.batch_id_seq.NEXTVAL from dual";
			public static final String GET_UNPROCESSED_CURRENCY_RATE_RECORDS  = "SELECT TRANSACTION_CURRENCY as \"transactionCurrency\", ISSUING_CURRENCY as \"issuingCurrency\",CONVERSION_RATE as \"exchangeRate\",EFFECTIVE_DATE as \"effectiveDateTime\",record_num as \"recordNumber\",mdm_id as mdmId,action from Currency_Conversion_Stage where batch_id=?";
			public static final String CHECK_DUPLICATE_CURRENCY_RATE_FILE = "SELECT COUNT(1) FROM file_upload_status WHERE UPPER(FILE_NAME) = UPPER(?) and file_type='CurrRate'";
			public static final String Get_RECORD_COUNT_CURRENCY_RATE_BATCH_ID = "SELECT COUNT(1) FROM Currency_Conversion_Stage WHERE Batch_id = ? ";
			public static final String UPDATE_CURRENCY_CONVERSION_STAGE_RESP_BY_ACTION = "update Currency_Conversion_Stage set STATUS=?,ERROR_MESSAGE=?,LAST_UPD_DATE=sysdate,LAST_UPD_USER='cclBatch' where batch_id=? and action=? and ERROR_MESSAGE is null ";
			public static final String INSERT_CURRENCY_RATE_FILE_ERROR = "insert into file_upload_status (error_msg,file_name,ins_date,last_upd_date,status,batch_id,file_type,ins_user) values (?,?,sysdate,sysdate,?,?,'CurrRate',1) ";
			public static final String VERIFY_MDMID = "SELECT COUNT(1) FROM PARTNER WHERE MDM_ID = ? ";
			public static final String VERIFY_CURRENCY = "SELECT COUNT(1) FROM CURRENCY_CODE WHERE CURRENCY_CODE = ? ";

			public static final String GET_CCF_FORMAT_VERSION_BY_ORDER_ID = " select p.attributes.Product.ccfFormatVersion ccfFormatversion from clp_order.ORDER_LINE_ITEM olt,clp_order.ORDER_DETAILS ol,product p where ol.order_id = olt.order_id and olt.product_id=p.product_id and ol.order_id=?";

			public static final String SP_EOD_PREAUTH_RELEASE = "{ call SP_EOD_PREAUTH_HOLD_RELEASE(?) }";
}


