package com.incomm.scheduler.utils;

public class JobConstants 
{
	private JobConstants() {
	}
	public static final String MESSAGE_DELIMITER = "-";
	
	public static final String RETAIL_SHIPMENT_JOB_ID = "retailShipmentJob";
	public static final String RETAIL_SHIP_INITIAL_TASKLET = "initialStep";	
	public static final String RETAIL_SHIP_FINAL_TASKLET = "finalStep";
	public static final String RETAIL_SHIP_MASTER_STEP = "masterStep";
	public static final String RETAIL_SHIP_SLAVE_STEP = "slaveStep";
	public static final String PARTITION_KEY = "partition";
	public static final String DEFAULT_KEY_NAME = "fileName";
	public static final String DEFAULT_THREAD_NAME = "threadName";
    
	//Retail Shipment Job Parameters
	public static final String DATETIME = "dateTime";
	public static final String DIRECTORY_PATH= "directoryPath";
	public static final String JOBNAME ="JobName";
	public static final String RETAIL_SHIPEMENT_TEMP_TABLE = "CPIFILE_DATA_TEMP";
	public static final String RETAIL_SHIPEMENT_MAST_TABLE = "CPIFILE_DATA";
	public static final String CN_FILE_STATUS = "CPIFILE_DATA";
	
	
	//Order Processing
	public static final String ORDER_PROCESS_JOB_ID = "OrderJob";
	public static final String ORDER_MASTER_STEP = "masterStep";
	public static final String ORDER_SLAVE_STEP = "slaveStep";
	
	
	//CCF Generation
		public static final String CCF_GENERATION_JOB_ID = "CCFGenerationJob";
		public static final String CCF_INITIAL_TASKLET = "initialStepCCF";
		public static final String CCF_FINAL_TASKLET = "finalStepCCF";
		public static final String CCF_MASTER_STEP = "masterStepCCF";
		public static final String CCF_SLAVE_STEP = "slaveStepCCF";
		public static final String CCF_GENERATION_RETAIL_JOB_ID = "RetailCCFGenerationJob";
		public static final String CCF_RETAIL_INITIAL_TASKLET = "initialStepRetailCCF";
		public static final String CCF_RETAIL_FINAL_TASKLET = "finalStepRetailCCF";
		public static final String CCF_RETAIL_MASTER_STEP = "masterStepRetailCCF";
		public static final String CCF_RETAIL_SLAVE_STEP = "slaveStepRetailCCF";
		public static final String CCF_HEADER_GROSS_RECCOUNT = "headerGrossRecCount";
		public static final String CCF_TRAILER_GROSS_RECCOUNT = "trailerGrossRecCount";
		public static final String CCF_HEADER_FILE_NUMBER = "headerFileNumber";
		public static final String CCF_SIMPLE_DATE_FORMAT_DDMMYY = "DDMMYYYY";
		public static final String CCF_SIMPLE_DATE_FORMAT_MMHHDDMMYYYY = "mmHHddMMyyyy";
		public static final String CCF_CCF_DETAILS = "ccfDetails";
		public static final String CCF_PAN = "pan";
		public static final String CCF_EXPIRY_DATE = "expDate";
		public static final String CCF_SRV_CODE = "srvCode";
		
		
		public static final String CCF_ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
		public static final String CCF_ORDER_TYPE = "orderType";
		public static final String CCF_CARD_STATUS = "CARD_STATUS";
		public static final String CCF_ORDER_ID = "ORDER_ID";
		public static final String CCF_CARD_NUM_HASH = "CARD_NUM_HASH";
		public static final String CCF_CARD_NUM_ENC = "CARD_NUM_ENCR";
		public static final String CCF_CUSTOMER_CODE = "CUSTOMER_CODE";
		public static final String CCF_PROXY_NUMBER = "proxyNumber";
		public static final String CCF_PRODUCT_ID = "PRODUCT_ID";
		public static final String CCF_FULFILLMENT_VENDOR_ID = "FULFILLMENT_VENDOR_ID";
		public static final String CHILD_ORDER_ID = "childOrderId";
		
		
		
		
		public static final String CCF_VERSION_ID = "CCFversionId";
		public static final String CCF_PRFL_CODE = "PRFL_CODE";
		public static final String CCF_CARD_RECORD_COUNT = "cardRecordCount";
		public static final String CCF_FULL_NAME = "fullName";
		public static final String CCF_EMB_NAME = "embName";
		public static final String CCF_EMB_NAME_TWO = "embNameTwo";
		public static final String CCF_ADDRESS_CITY = "addressCity";
		public static final String CCF_STATE_CODE = "stateCode";
		public static final String CCF_PIN_CODE = "pinCode";
		
		
		
		
		public static final String CCF_ADDRESS_ONE = "addressOne";
		public static final String CCF_ADDRESS_TWO = "addressTwo";
		public static final String CCF_CITY_STATE = "cityState";
		public static final String CCF_PAN_LENGTH = "panLength";
		  public static final String CCF_CVV_1 = "cvv1";
       		 public static final String CCF_CVV_2 = "cvv2";
       		 
   		  public static final String THREE_DIGIT_CSC = "threeDigitCSC";
    	  public static final String FOUR_DIGIT_CSC = "cvv2";
    		 public static final String FIVE_DIGIT_CSC = "fiveDigitCSC"; 
		public static final String CCF_DTL_PART = "D";
		public static final String CCF_HEADER_PART = "H";
		public static final String CCF_TRAILER_PART = "T";
		public static final String CCF_SLASH_N = "\n";
		public static final String COMMA_SEPARATOR = ",";
		public static final String CCF_TILT_SEPARATOR = "~";
		
		public static final char EMPTY_SPACE = ' ';
		public static final String TRIPLE_ZERO ="000";
		public static final String EMPTY_STRING ="";
		public static final String DISP_NAME ="fullName";
		public static final String MS_STARTER_FILES ="C";
		public static final String CCF_ENABLE ="enable";
		public static final String CCF_ERROR ="error";
		public static final String CCF_EMB_FILE_NAME ="embfname";
		public static final String CCF_HEADER_FILE_NO ="headfileno";
		
		public static final String CCF_OK ="ok";
		
		public static final String CCF_SUCCESS ="success";
		
		
		
		
		
		public static final String CCF_ORDER_STATUS_INPROGRESS ="CCF-IN-PROGRESS";
		public static final String CCF_ORDER_STATUS_CCF_GENERATED ="CCF-GENERATED";
		public static final String ORDER_STATUS_GENERATED ="GENERATED";
		public static final String PRINTERSENT ="99";
		public static final String ORDER_STATUS_PROCESSED ="ORDER-GENERATED";
		
       
        public static final String CCF_FORWARD_SLASH ="/";
        
        public static final String CCF_ERROR_CREATE_FOLDER ="Error while creating ccf folder";
		public static final String NO_ORDER_TO_PROCESS ="No Order to process";
		public static final String ERROR_CCF_FILE_CREATE ="No Order to process";
		 public static final String ERROR_WRITE_DETAILS ="Error occured while writing details to ccf file";
		public static final String ERROR_CVV_GENERATION ="Error While generating CVV";
		public static final String GENERIC_ERROR_MESSAGE ="";
		public static final String NO_VENDOR_LINKED_TO_ORDER ="No vendor linked to order";
		public static final String ERR_FILE_NAME ="Error occured while creating CCF file name";
		
		public static final String CCF_HSM_IP_ADDRESS ="10.22.66.200";
		public static final int CCF_HSM_PORT =1000;
		public static final String CCF_DIR_PATH = "C:\\Users\\svinoth\\Desktop\\CCFFILES\\";
		
		/*CN File upload*/
		public static final String CN_DEL_FILE_DIR = "DelDir";
		public static final String TEMP_FILE_DIR = "temp";
		public static final String CN_FILE_SEPERATOR = ":";
		public static final String CN_FILE_NAMES = "fileNames";
		public static final String CN_FILE_SUCCESS = "SUCCESS";
		public static final String CN_FILE_NAME = "fileName";
		
		
		public static final String FILE_LIST="fileList";
		public static final String PARTITION="partition";
//Daily Balance job changes-Start
		public static final String DAILY_BALANCE_JOB_ID = "DailyBalanceJob";
		public static final String DAILY_BALANCE_INITIAL_TASKLET = "initialStepBal";
		public static final String DAILY_BALANCE_FINAL_TASKLET = "finalStepBal";
		
		public static final String ALERT_ID = "AlertId";
		public static final String DELIVERY_CHNL = "DeliveryChnl";
		public static final String TRAN_CDE = "TranCde";
		public static final String CARD_NUMBER = "CardNumber";
		public static final String PRODUCT_ID = "productID";
		public static final String ACCOUNT_ID = "accountId";
		public static final String CARD_HASH = "cardHash";
		public static final String CURRENCY_CODE = "CurrencyCode";
		public static final String CUSTOMER_ALERT = "customerAlert";
		public static final String RESPONSE_CODE = "responseCode";
		public static final String PROCESS_STATUS = "processStatus";
		
		public static final String NOTIFICATION_SMSSTATUS="smsStatus";
		public static final String NOTIFICATION_EMAILSTATUS="emailStatus";
//Daily Balance job changes-End

		//Return File Upload
		public static final String RETURN_FILE_JOB_ID = "returnFileJob";
		
	public static final String RETURN_FILE_TEMP_TABLE = "RETURNFILE_DATA_STG";
		public static final String RETURN_FILE_MAST_TABLE = "RETURNFILE_DATA";
		
		//Response File Upload
		public static final String RESPONSE_FILE_JOB_ID = "responseFileJob";
				
	public static final String RESPONSE_FILE_TEMP_TABLE = "RESPONSEFILE_DATA_STG";
		public static final String RESPONSE_FILE_MAST_TABLE = "RESPONSEFILE_DATA";
		
		
		//Serial Number File Upload
		public static final String SERIALNUMBER_FILE_JOB_ID = "serialNumberFileJob";
						
	public static final String SERIALNUMBER_FILE_TEMP_TABLE = "SERIAL_DETAILS_STG";
		public static final String SERIALNUMBER_FILE_MAST_TABLE = "SERIAL_DETAILS";
		
		//Shipment File Upload
		
		public static final String SHIPMENT_FILE_JOB_ID = "shipmentFileJob";
		
	public static final String SHIPMENT_FILE_TEMP_TABLE = "SHIPMENTFILE_DATA_STG";
		public static final String SHIPMENT_FILE_MAST_TABLE = "SHIPMENTFILE_DATA";
		
		
		//POST-BACK-JOB
		
		
		public static final String  ORDER_ORDPOST_BACK_URL = "postBackURL";
		public static final String  ORDER_ID	= "orderId";
		public static final String  API_NAME	="apiName" ;
		public static final String  SEQ_NO="seqNo";
		public static final String  REQ_MESSAGE="reqMsg";
		public static final String  REQ_HEADER="reqHeader";
		public static final String  RES_HEADER="resHeader";
		public static final String  RES_CODE="responseCode";
		public static final String  RES_MSG="responseMsg";
		public static final String  RES_COUNT="responseCount";
		public static final String  RES_FLAG="responseFlag";
		public static final String  POST_BACK_STATUS="postBackStatus";
		public static final String  POST_BACK_URL="postBackUrl";
		public static final String  PARTNER_ID="partnerId";
		public static final String ORDER_STATUS ="orderStatus";
		public static final String SHIPPING_METHOD ="shippingMethod";
		public static final String LINE_ITEM_ID="lineItemId";
		public static final String LINE_ITEM_ORDER_STATUS="lineItemOrderStatus";
		public static final String RETURN_FILE_MSG="returnFileMsg";
		public static final String REJECT_CODE="rejectCode";
		public static final String REJECT_REASON="rejectReason";
		
		public static final String PROXY_NUMBER="proxyNumber";
		public static final String SERIAL_NUMBER="serialNumber";
		public static final String CARD_STATUS="cardStatus";
		public static final String PIN="pin";
		public static final String PROXY_PIN_ENCR="proxyPinEncr";
		public static final String TRACKING_NBR="trackingNumber";
		public static final String SHIPPING_DATE="shippingDate";
		public static final String CUSTOMER_CODE="customerCode";
		public static final String CARDNUMBER="cardNumber";
		public static final String CARD_NUM_HASH="cardNumHash";
		public static final String EXPIRY_DATE="expirationDate";
		public static final String CARD_NUM_ENCR="cardNumEncr";
		public static final String PRINTER_RESPONSE="printerResponse";
		public static final String RESP_CODE="respCode";
		
		public static final String ORDER_LINE_ITEMDTLS = "lineItem";
		
		public static final String CARDS = "cards";
		public static final String LINE_ITEM = "lineItem";

		public static final String ORDER = "order";

		public static final String POSTBACK_APINAME = "POSTBACK_APINAME";
		public static final String POSTBACK_JOB_ID = "PostBackJob";
		
		public static final String CCF_ORDER_LINE_ITEM_ID = "LINE_ITEM_ID";
	public static final String PACKAGE_ID = "package_id";

		public static final String POSTBACK_STATUS="POSTBACK_STATUS";

		public static final String X_INCFS_DATE = "x-incfs-date";

		public static final String X_INCFS_IP = "x-incfs-ip";

		public static final String CARD_NUM_INVENTORY_GEN_ID = "CardNumberInventoryGenJob";

		public static final String CARD_NUM_INVENTORY_INITIAL_TASKLET = "initialStepCardInventory";

		public static final String CARD_NUM_INVENTORY_FINAL_TASKLET = "finalStepCardInventory";

		public static final String AUTO_REPL_INVENTORY_ID = "AutoReplenishmentInventoryGenJob";

		public static final String ORDER_LESS_NO_OF_CARDS_FOR_THIS_LOCATION_ID ="Order Less No Of Cards For This Location ID";
		
		public static final String AUTO_REPLENISHMENT_OF_INVENTORY_GEN_ID = "AutoReplenishmentInventoryGenJob";

		public static final String AUTO_REPLENISH_INVENTORY_INITIAL_TASKLET = "initialStepAutoReplenishmentInventory";

		public static final String AUTO_REPLENISH_INVENTORY_FINAL_TASKLET = "finalStepAutoReplenishmentInventory";

		public static final String ORDER_CONNECTION_ERROR ="Unable to Connect to CCLP-Order Service";
				
			public static final String CCF_B2B_REPL_GENERATION_JOB_ID = "B2BREPLCCFGenerationJob";
		
		
		//B2B REPLACEMENT
		
		public static final String CCF_B2B_REP_INITIAL_TASKLET = "initialStepB2BREPCCF";
		public static final String CCF_B2B_REP_FINAL_TASKLET = "finalStepB2BREPCCF";

		public static final String CCF_REP_ADDRESS_ONE = "shipToAddress1";

		public static final String CCF_REP_ADDRESS_TWO = "shipToAddress2";

		public static final String CCF_REP_ORDER_STATUS_INPROGRESS = "CCF-REPL-IN-PROGRESS";

		public static final String CCF_REPL_ORDER_STATUS_CCF_GENERATED = "CCF-REPL-GENERATED";
		public static final String SHIP_TO_ADDRESS1 = "SHIPTOADDRESS1";
		public static final String SHIP_TO_ADDRESS2 = "SHIPTOADDRESS2";
		
		
		// WeeklyFee Job 
		public static final String WEEKLY_FEE_JOB = "WeeklyFeeJob";
		public static final String WEEKLY_FEE_JOB_INITIAL_TASKLET = "initialStepWeeklyFee";
		public static final String WEEKLY_FEE_JOB_FINAL_TASKLET = "finalStepWeeklyFee";
		
		public static final String MONTHLY_FEE_JOB = "MonthlyFeeJob";		
		public static final String MONTHLY_FEE_JOB_INITIAL_TASKLET = "initialStepMonthlyFee";
		public static final String MONTHLY_FEE_JOB_FINAL_TASKLET = "finalStepMonthlyFee";
		
		public static final String YEARLY_FEE_JOB = "YearlyFeeJob";		
		public static final String YEARLY_FEE_JOB_INITIAL_TASKLET = "initialStepYearlyFee";
		public static final String YEARLY_FEE_JOB_FINAL_TASKLET = "finalStepYearlyFee";
		
		public static final String DORMANCY_FEE_JOB = "DormancyFeeJob";		
		public static final String DORMANCY_FEE_JOB_INITIAL_TASKLET = "initialStepDormancyFee";
		public static final String DORMANCY_FEE_JOB_FINAL_TASKLET = "finalStepDormancyFee";
		
		public static final String PASSIVE_PRIOD_CALC_JOB = "PassivePeriodCalculationJob";		
		public static final String PASSIVE_PERIOD_CALCULATION_INITIAL_TASKLET = "initialStepPassivePeriodCalculation";
		public static final String PASSIVE_PERIOD_CALCULATION_FINAL_TASKLET = "finalStepPassivePeriodCalculation";

		public static final String CHANGE_CARD_STATUS_JOB = "ChangeCardStatusJob";		
		public static final String CHANGE_CARD_STATUS_INITIAL_TASKLET = "initialStepChangeCardStatus";
		public static final String CHANGE_CARD_STATUS_FINAL_TASKLET = "finalStepChangeCardStatus";

		public static final String POST_BACK_RESP_MSG="postbackResp";

		public static final String SERIAL_NUMBER_REQUEST_JOB="serialNumberRequest";
		public static final String SERIAL_NUMBER_REQUEST_INITIAL_TASKLET = "initialStepSerialNumberRequest";
		public static final String SERIAL_NUMBER_REQUEST_FINAL_TASKLET = "finalStepSerialNumberRequest";
		
		public static final String CCF_FILE_DIR_DEL_JOB_ID = "CCFFileDirDelJob";

		public static final String CCF_FILE_DIR_DEL_INTL_TASKLET = "initialStepDelDir";

		public static final String CCF_FILE_DIR_DEL_FINAL_TASKLET = "finalStepDelDir";

		public static final String ERR_JSON_VALUE = "Invalid json input";

		public static final String TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR = "R0012";
		
		public static final String DIGITAL_CARD_PROCESSING_JOB = "DigitalCardProcessingJob";
		public static final String DIGITAL_CARD_PROCESSING_JOB_INITIAL_TASKLET = "initialStepDigitalCardProcessing";
		public static final String DIGITAL_CARD_PROCESSING_JOB_FINAL_TASKLET = "finalStepDigitalCardProcessing";
		
		public static final String BULK_TRANSACTION_JOB_ID = "bulkTransactionJob";
		public static final String BULK_TRANSACTION_INITIAL_TASKLET = "initialStepBlkTxn";	
		public static final String BULK_TRANSACTION_FINAL_TASKLET = "finalStepBlkTxn";
		public static final String BULK_TRANSACTION_MASTER_STEP = "masterStepBlkTxn";
		public static final String BULK_TRANSACTION_SLAVE_STEP = "slaveStepBlkTxn";
		public static final String BULK_TRANSACTION_MAST_TABLE = "Bulk_transaction";
		public static final String BULK_TRANSACTION_MASTER_STEP_PROCESS = "masterStepBlkTxnProcess";
		public static final String BULK_TRANSACTION_MASTER_STEP_WRITE = "masterStepBlkTxnWrite";
		
		public static final String CURRENCY_RATE_JOB_ID = "currencyRateUploadJob";
		public static final String CURRENCY_RATE_INITIAL_TASKLET = "initialStepCurrencyRate";	
		public static final String CURRENCY_RATE_FINAL_TASKLET = "finalStepCurrencyRate";
		public static final String CURRENCY_RATE_MASTER_STEP = "masterStepCurrencyRate";
		public static final String CURRENCY_RATE_SLAVE_STEP = "slaveStepCurrencyRate";
		public static final String CURRENCY_RATE_MASTER_STEP_PROCESS = "masterStepCurrencyRateProcess";
		public static final String CURRENCY_RATE_MASTER_STEP_WRITE = "masterStepCurrencyRateWrite";
		
		public static final String BATCH_LOAD_ACCOUNT_PURSE_JOB_ID = "loadAccountPurseBatchJob";
		public static final String BATCH_LOAD_ACCOUNT_PURSE_REQUEST_CREATER_STEP = "accountPurseLoadRequestCreater";
		public static final String BATCH_LOAD_ACCOUNT_PURSE_REQUEST_SLAVE_STEP = "slaveStepLoadAccountPurse";
		public static final String BATCH_LOAD_ACCOUNT_PURSE_REQUEST_PROCESSOR = "accountPurseLoadRequestProcessor";
		public static final String BATCH_LOAD_ACCOUNT_PURSE_FINAL_STEP = "finalStepLoadAccountPurse";

		public static final String CUSTOMER_ID = "CUSTOMER_ID";
		public static final String UPC = "upc";
		
		
		public static final String EOD_PREAUTH_RELEASE_JOB = "EODPreAuthReleaseJob";		
		public static final String EOD_PREAUTH_RELEASE_JOB_INITIAL_TASKLET = "initialStepEODPreAuthRelease";
		public static final String EOD_PREAUTH_RELEASE_JOB_FINAL_TASKLET = "finalStepEODPreAuthRelease";
		
		public static final String PURSE = "Purse";		
    	public static final String ENABLE = "Enable";	
		public static final String DATETIME_FORMAT  = "MM/dd/yyyy HH:mm:ss";		
		public static final String DATE_FORMAT  = "MM/dd/yyyy";


		
		
 }
		




