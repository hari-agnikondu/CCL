package com.incomm.cclp.fsapi.constants;

/**
 * 
 *
 */
public class FSAPIConstants {
	
	private FSAPIConstants() {}
	public static final String B2B_API_URL = "/b2b";
	public static final String B2B_API_URL_V1 = "/b2b/v1";
	public static final String ORDER_API_URL = "/orders";
	public static final String ORDER_STATUS_API_URL = "/orders/{OrderID}/status";
	public static final String ORDER_API = "ORDER";
	public static final String ORDER_STATUS_API = "ORDERSTATUS";
	public static final String MANDATORY_FIELD = "M";
	public static final String NONMANDATORY_FIELD = "N";
	public static final String ORDER_LINE_ITEM = "lineItem";
	public static final String ORDER_ACCEPT_PARTIAL_ORDERS = "acceptPartialOrders";
	public static final String ORDER_QUANTITY_ERRMSG = "Quantity exceeded the configured quantity";
	public static final String ORDER_PRODUCT_ID = "productID";
	public static final String ORDER_PACKAGE_ID = "packageID";
	public static final String VIRTUAL_PACKAGE_ID = "PackageID";
	public static final String ORDER_QUANTITY = "quantity";
	public static final String ORDER_OFFER_CODE = "offerCode";
	public static final String ORDER_EMBOSSED_LINE = "embossedLine";
	public static final String ORDER_DENOMINATION = "denomination";
	public static final String ORDER_LINE_ITEM_ID = "lineItemID";
	public static final String ORDER_SHIPPING_FEE = "shippingFee";
	public static final String ORDER_SHIP_TO_COMPANY_NAME = "shipToCompanyName";
	public static final String ORDER_EMAIL = "email";
	public static final String ORDER_PHONE = "phone";
	public static final String ORDER_LAST_NAME = "lastName";
	public static final String ORDER_MIDDLE_INITIAL = "middleInitial";
	public static final String ORDER_FIRST_NAME = "firstName";
	public static final String ORDER_COUNTRY = "country";
	public static final String ORDER_POSTAL_CODE = "postalCode";
	public static final String ORDER_STATE = "state";
	public static final String ORDER_CITY = "city";
	public static final String ORDER_ADDRESS_LINE3 = "addressLine3";
	public static final String ORDER_ADDRESS_LINE2 = "addressLine2";
	public static final String ORDER_ADDRESS_LINE1 = "addressLine1";
	public static final String ORDER_SHIPPING_METHOD = "shippingMethod";
	public static final String ORDER_PROGRAM_ID = "programID";
	public static final String ORDER_ACTIVATION_CODE = "activationCode";
	public static final String ORDER_ORDPOST_BACK_URL = "postBackURL";
	public static final String ORDER_POST_BACK_RESPONSE = "postBackResponse";
	public static final String ORDER_ORDER_SHIP_STATE = "orderShipState";
	public static final String ORDER_MERCHANT_ID = "merchantID";
	public static final String ORDER_ORDER_ID = "orderID";
	public static final String ORDER_PAN_CHECK = "panCheck";
	public static final String ORDER_FSAPI_Y = "Y";
	public static final String ORDER_FSAPI_F = "F";
	public static final String ORDER_FSAPI_TRUE = "TRUE";
	public static final String ORDER_PROD_PACK_IDCOUNT = "prodPackIdcount";
	public static final String ORDER_PROD_IDCOUNT = "prodIdcount";
	public static final String ORDER_PARTNERID = "x-incfs-partnerid";
	public static final String ORDER_LINE_ITEMS = "lineItem";
	public static final String ORDER_EMPTY_STRING = "";
	public static final String ORDER_ERRMSG = "errorMsg";
	public static final String ORDER_COUNT = "count";
	public static final StringBuilder ORDER_FULFILLMENT_VENDOR_QRY = new StringBuilder();
	public static final StringBuilder ORDER_PRODUCT_PACKAGE_QRY = new StringBuilder();
	public static final StringBuilder ORDER_PANAVAIL_CHECK_QRY = new StringBuilder();
	public static final StringBuilder ORDER_INSERT_ORDER_QRY = new StringBuilder();
	public static final StringBuilder ORDER_INSERT_ORDER_LINEITEM_QRY = new StringBuilder();
	public static final StringBuilder ORDER_INSERT_LOGGER_QRY = new StringBuilder();
	public static final StringBuilder ORDER_INSERT_LOGGER_QRY1 = new StringBuilder();
	public static final StringBuilder ORDER_PARTNERID_QRY = new StringBuilder();
	public static final String ORDER_FSAPI_T = "T";
	public static final String ORDER_PARTNER_IDCNT = "partnerIdCnt";
	public static final String ORDER_SHIP_ADDRESS = "shipToAddress";
	public static final String ORDER_PRODPACK_ERRMSG = "Invalid ProductID and Package ID Combination";
	public static final String ORDER_PRODUCTID_ERRMSG = "Invalid Retail sale product";
	public static final String ORDER_FULFILMENT_TYPEERRMSG = "Invalid Product Type";
	public static final String ORDER_CHANNEL_EERMSG = "Transaction not supported for this channel";
	public static final String ORDER_PANCHK_ERRMSG = "Required card numbers not available";
	public static final String ORDER_LINEITEM_STAT = "A";
	public static final String ORDER_ACCEPT_STATUS = "APPROVED";
	public static final String ORDER_REJECT_STATUS = "REJECTED";
	public static final String ORDER_PARTNERID_ERRMSG = "Invalid Partner ID";
	public static final String PACK_ID = "PACK_ID";
	public static final String ORDER_RESPONSE_MSG = "responseMessage";
	public static final String ORDER_RESPONSE_CODE = "responseCode";
	public static final String LINEITEM_RESPONSE_CODE = "responseCode";
	public static final String LINEITEM_RESPONSE_MSG = "responseMessage";
	public static final String LINEITEM_STATUS = "lineItemStatus";
	public static final String ORDER_RESPONSE_STATUS = "status";
	public static final String ORDER_RESPONSE_REJSTATUS = "Failure";
	public static final String SEMICOLON_SEPARATOR = ";";
	public static final String COLON_SEPARATOR = ":";
	public static final String COMMA_SEPARATOR = ",";
	public static final String ORDER_VENDORTYPE_IND = "IND";
	public static final String ORDER_VENDORTYPE_BLK = "BLK";
	public static final String B2B_ORDER_CHANNELS_BOL_IDS = "BOL,IDS";
	
	public static final String SYSDATE = "SYSDATE";
	public static final String ORDER_SUCCESS_RESPMSG = "APPROVED";
	public static final String SUCCESS_RESP_CODE = "00";
	public static final String ORDER_RESPONSE_REJRESCODE = "02";
	public static final String ORDER_RESP_DECRESCODE = "89";
	public static final String ORDER_STATUS = "orderStatus";
	public static final String ORDER_STATUS_ERRMSG = "orderStatusErrMsg";
	public static final String ORDER_LINE_ITEMSTATUS = "lineItemStatus";
	public static final String ORDER_LINE_ITEMERRMSG = "lineItemErrMsg";
	public static final String ORDER_LINE_ITEMDTLS = "lineItem";
	public static final String ORDER_LINEITEM_SUSCMSG = "success";
	public static final String OK = "OK";
	public static final String ORRDER_ERROR_LIST = "errorList";
	public static final String ORDER_LINEITEM_ERROR_LIST = "lineItemErrorList";
	public static final String ERROR_MSG = "errorMsg";
	public static final String ORDER_EMBOSSED_LINE1 = "embossedLine1";
	public static final StringBuilder ORDER_FORMFACTOR_QRY = new StringBuilder();
	public static final StringBuilder ORDER_CRADTYPE_QRY = new StringBuilder();
	public static final String PRODUCT_ID = "PRODUCT_ID";
	public static final String ORDER_FORMFACTOR_ERRMSG = "Multiple form factor in the single order";
	public static final String B2B_CARD_COUNT = "B2B_CARD_COUNT";
	public static final String ORDER_ZERO = "0";
	public static final String KYC_STATUS_NOTREQUIRED = "A";
	public static final String VIRTUALCARD_VALIDATION_API = "VCVALIDATION";
	public static final String ACTIVATION_API = "ACTIVATION";
	public static final String RELOAD_API = "RELOAD";
	public static final String RELOAD_POSTBACK_API = "RELOAD_POSTBACK";
	public static final String CARD_REPLACE_RENEWAL_API = "CARDREPLACERENEWAL";
	
	

	public static final String SERIALNUMBER_VALIDATION_API = "SERIALNOACTIVATION";
	public static final String PROXYNUMBER_VALIDATION_API = "PROXYNOACTIVATION";
	public static final String EXPIRYCHKFLAG = "ExpiryDate Regeneration Activation";
	public static final String FSAPI_VIRTUAL_SEQ_NAME = "SEQ_FSAPI_VIRTUAL_RRN";
	public static final String FSAPI_SERIALACTIVATION_SEQ_NAME = "SEQ_FSAPI_SERIALACTIVATION_RRN";
	public static final String FSAPI_PROXYACTIVATION_SEQ_NAME = "SEQ_FSAPI_PROXYACTIVATION_RRN";
	public static final String FSAPI_RPLRENEWAL_SEQ = "seq_cardrplrenewal";
	
	public static final String VIRTUALCARD_VALIDATION_API_URL = "/cards/validation";
	public static final String SERIALNUMBER_ACTIVATION_API_URL = "/cards/{serialnumber}/serialnumberactivation";
	public static final String PROXYNUMBER_ACTIVATION_API_URL = "/cards/{proxynumber}/proxynumberactivation";
	public static final String SUCCESS_RESPONSE = "00";
	public static final String SUCCESS_RESPONSE1 = "R0001";
	public static final String SUCCESS_MSG = "success";
	public static final String FAILURE_RESP_CODE = "89";
	public static final String ACTIVE_CARDSTAT = "1";
	public static final String FSAPI_RELOAD_URI = "/reload";
	public static final String FSAPI_WEB_CHANNEL_CODE = "17";
	public static final String FSAPI_CHW_CHANNEL_CODE = "10"; // this is used
																// for getting
																// response
																// message in
																// resp master
																// table.
	
	public static final String PRINTER_RESPONSE = "printerResponse";
	public static final String INST_CODE = "1";
	public static final String RELOADRRNSEQ = "SEQ_FSAPI_RELOAD_RRN";
	public static final String POSTBACKURL = "POSTBACKURL";
	public static final String DENOMINATION = "denomination";
	public static final String CARDS = "cards";
	public static final String RELOAD_POST_BACK_RESPONSE = "postBackResponse";
	public static final String RESP_HERDER_POSTBACKURL = "postBackURL";
	public static final String PROXYNUMBER = "proxyNumber";
	public static final String SERIALNUMBER = "serialNumber";
	public static final String RELOAD_MERCHANTID = "merchantId";
	public static final StringBuilder ORDER_ID_QRY = new StringBuilder();
	public static final StringBuilder ORDER_LINEITEM_IDQRY = new StringBuilder();
	public static final String LINE_ITEMID = "LINE_ITEMID";
	public static final String LINE_ITEMS = "lineItems";
	public static final String ORDER_LINEITEM_ERRMSG = "Invalid Line Item ID";
	public static final String ORDER_IDERRMSG = "Invalid Order ID";
	public static final String VALID_FAILRESPCDE = "49";
	public static final StringBuilder DUPLICATE_ORDER_IDQRY = new StringBuilder();
	public static final String ORDER_DUPLICATE_ERRMSG = "Duplicate Order";
	public static final String ORDER_DUPLICATE_CHECK = "orderDupCheck";
	public static final String ORDER_DUPLICATE_LINEITEM = "orderLintItemDupCheck";
	public static final StringBuilder ORDER_STATUS_QRY = new StringBuilder();
	public static final StringBuilder ORDER_LINEITEM_DTLSQRY = new StringBuilder();
	public static final String ORDER_CHNL_ID = "x-incfs-channel";
	public static final StringBuilder ORDER_DENOM_QRY = new StringBuilder();
	public static final StringBuilder PROD_FUND_QRY = new StringBuilder();
	public static final String ORDER_PRCESSS_QRY = "{call VMSB2BAPI.process_order_request(?,?,?,?,?)}";
	public static final String CANCEL_ORDER_PRCESSS_QRY = "{call VMSB2BAPI.cancel_order_process(?,?,?,?,?,?)}";
	public static final String CANCEL_ORDER_QRY = "{call VMSB2BAPI.cancel_order_request(?,?,?,?,?)}";
	public static final StringBuilder CVKI_SCHEME_QRY = new StringBuilder();
	public static final String POST_BACK_RESP = "postBackResponse";
	public static final String APINAME = "APINAME";
	public static final String POSTBACK_APINAME = "POSTBACK_APINAME";
	public static final String ORDER_POSTBACK_APINAME = "ORDER_POSTBACK";
	public static final String POSTBACK_URL = "postBackURL";
	public static final String POSTBACK_URL_KEY = "API_POSTBACK_KEY";
	public static final String TILDE_OPER = "~";
	public static final String STATUS = "status";
	public static final String ORDER_FSAPI_FALSE = "FALSE";
	public static final String ORDER_FSAPI_TRUE1 = "1";
	public static final String ORDER_FSAPI_ONE = "1";
	public static final String HEADER_FIELD = "order";
	public static final String ORDERID = "orderID";
	public static final String LINEITEMID = "lineItemID";
	public static final String Y = "Y";
	public static final String N = "N";
	public static final String ERROR_LIST = "errorList";
	public static final StringBuilder RELOAD_PARTNERID_QRY = new StringBuilder();

	public static final String MANDATORY_FIELD_FAILURE = "Required Property Not Present";
	public static final String MANDATORY_FIELD_FAILURE_RESPCODE = "R0146";
	public static final StringBuilder ORDER_STATE_QRY = new StringBuilder();
	public static final String ORDER_STATE_ERRMSG = "Invalid State";
	public static final String NON_MANDATORY_FIELD_FAILURE_RESPCODE = "16";
	public static final String ORDER_PRODPACK_RESCODE = "33";
	public static final String ORDER_INVALIDPROD_RESCODE = "07";
	public static final String ORDER_DUPLICATE_RESPCODE = "06";
	public static final String ORDER_PARTNERID_RESPCODE = "R0057";
	public static final String ORDER_LINEITEM_RESPCODE = "04";
	public static final String ORDER_ID_RESPCODE = "03";
	public static final String ORDER_FULFILMENT_TYPRESCODE = "09";
	public static final String ORDER_STATE_RESPCDE = "08";
	public static final String ORDER_DUPLICATE_RESPCDE = "06";
	public static final String ACCEPT_RESP_CODE = "00";
	public static final StringBuilder ORDER_UPDAET_PANCOUNT_QRY = new StringBuilder();
	public static final String FINANCIAL_MESSAGE_TYPE = "0200";
	public static final String MEMBER_NUMBER = "000";
	public static final String TRANS_MODE = "0";
	public static final String REVERSAL_CODE = "00";
	public static final String NON_FINANCIAL_MSGTYPE = "0100";

	public static final String INVALID_PARTNERID = "05";

	public static final String CARD_ACTIVATION_ALREADY_DONE = "Card Activation Already Done";
	public static final String CARD_ACTIVATION_ALREADY_DONE_ID = "17";
	public static final String CARDNOTFOUND = "Invalid Card Number";
	public static final String INVALID_PROXY_NUMBER_CODE = "12";
	public static final String INVALID_PRXOY_NUMBER = "Invalid Proxy Number";

	public static final String INVALID_SERIAL_NUMBER_CODE = "13";
	public static final String INVALID_SERIAL_NUMBER = "Invalid Serial Number";
	public static final String RESP_PRXOY_NUMBER = "RespProxyNumber";
	public static final String RESP_SERIAL_NUMBER = "RespSerialNumber";
	public static final String SYSTEM_ERROR = "System Error";
	public static final String PAN_NOT_AVAIL_RESPCDE = "18";
	public static final String EXPIRYDATEEXCEPTION = "Exception while getting new expiry date";
	public static final String ACTIVATIONCODES_NOTMATCHED_MSG = "Activation Codes Not Matched";
	public static final String CVV_GENERATION_FAILED = "CVV Generation Failed";
	public static final String INVALID_PARTNERID_MSG = "Invalid Partner ID";
	public static final String NEWEXPIRY_DATE_DECLINE = "89";
	public static final String ACTIVATIONCODES_NOTMATCHED = "14";
	public static final String CVV_GENERATION_ERROR = "89";
	public static final String PARTNERID_NOT_PRESENT = "05";
	public static final String PROFILENOTFOUND = "Card Profile Not Found";
	public static final String PROFILE_NOTFOUND = "89";
	public static final String B2B_MANDATORY_FAILURE_ACTIVATIONCODE = "Required Property Not Present :ActivationCode";
	public static final String REQ_PARSING_FAILED = "43";
	public static final String CARDNOTFOUND_CODE = "11";
	public static final String ORDER_QUANTITY_RESCDE = "22";
	public static final String INVALID_TYPE = "Invalid  Field type";
	public static final String ORDER_ACTIVATION_API_URL = "/orders/{OrderID}/activation";
	public static final String SERIALNUMBER_RANGE_ACTIVATION_API_URL = "/serialnumberactivation";
	public static final String ORDER_ACTIVATION_API = "ORDERACTIVATION";
	public static final String SERIALNUMBER_RANGE_VALIDATION_API = "SERIALNUMBERRANGEACTIVATION";
	public static final String INVALID_LINEITEM_STATUS_CODE = "20";
	public static final String INVALID_ORDER_STATUS_CODE = "19";
	public static final String INVALID_LINEITEM_STATUS = "Invalid Line Item Status";
	public static final String INVALID_ORDER_STATUS = "Invalid Order Status";
	public static final String FSAPI_BULKORDER_ACTIVATION_SEQ_NAME = "SEQ_FSAPI_BULKACTIVATION_RRN";
	public static final String FSAPI_SERIALNUMBERRANGE_ACTIVATION_SEQ_NAME = "SEQ_FSAPI_SERIALRANGE_RRN";
	public static final String STARTSERIALNUMBER = "startSerialNumber";
	public static final String ENDSERIALNUMBER = "endSerialNumber";
	public static final String STARTEND_SAMEPRODUCTID_COMBINATION = "Start and End Serial Numbers not Belongs to Same Product";
	public static final String STARTEND_SAMEPRODUCTID_COMBINATION_CODE = "21";
	public static final String ORDER_LINEITEM_STATUS = "CCF-GENERATED";
	public static final String ORDER_LINEITEM_STATUS_COMPLETED = "COMPLETED";
	public static final String ORDER_LINEITEM_STATUS_SHIPPED = "SHIPPED";
	public static final String STARTEND_SAMEORDERID_COMBINATION = "Start and End Serial Numbers not Belongs to Same Order";
	public static final String STARTEND_SAMEORDERID_COMBINATION_RESCDE = "34";
	

	public static final String FSAPI_CARDSTATUS = "/cards/status";
	public static final String FSAPI_CARDSTATUS_INQUIRY = "CARDSTATUS";
	public static final String CARDSTAT_START = "start";
	public static final String CARDSTAT_END = "end";
	public static final String PANLASTFOUR = "PANLastFour";
	public static final String CARDSTATUS_MSGTYPE = "0200";
	public static final String START_END_INVALID = "end value should be greater than or equal to start value";
	public static final String FORM_FACT_RESPCDE = "23";
	public static final String ORDER_TYPE = "orderType";
	public static final String FSAPIKEY = "FSAPIKEY";
	public static final String FAILURE_STATUS_CODE = "FAILURE_STATUS_CODE";
	public static final int SUCCESS_STATUS_CODE = 200;
	public static final String DENOMERRMSG = "Invalid Denommination";
	public static final String DENOM_RESCDE = "30";
	public static final String ORDERFAIL_ERRMSG = "Order Processing Failed";
	public static final String ORDER_SHIPPING_METHOD_ERRMSG = "Invalid shipping method";
	public static final String ORDER_SHIPPING_METHOD_RESPCDE = "31";
	public static final String ORDER_LINEITEM_DUPLICATE_RESPCDE = "32";
	public static final String ORDER_LINEITEM_DUPLICATE_ERRMSG = "Lineitem Id should be unique in order";
	public static final StringBuilder ORDER_PRODUCT_IDQRY = new StringBuilder();
	public static final String ORDER_RESPONSE = "response";
	public static final String VIRTUAL_PRODUCT = "V";
	public static final String VIRTUAL_PRODUCT_CARDTYPE = "cardType";
	public static final String LINEITEM_ERR_LIST = "lineItemErrList";
	public static final StringBuilder ORDER_SERIALAVAIL_CHECK_QRY = new StringBuilder();
	public static final String SERIAL_CHECK = "serialCount";
	public static final String ORDER_SERIALNOCHK_ERRMSG = "Required Serial Numbers are not available";
	public static final String SERIALNO_NOT_AVAIL_RESPCDE = "35";
	public static final String FULFILLMENT_VALUE = "value";
	public static final String STARTEND_SAMEORDER_COMBINATION = "Start and End Serial/Proxy Numbers not Belongs to Same Order";
	public static final String STARTEND_SAMEORDER_COMBINATION_RESCDE = "R0147";
	public static final String US_COUNTRYCODE = "US";
	public static final String CA_COUNTRYCODE = "CA";
	public static final String ORDER_ZIPCODE_ERRMSG = "Invalid ZipCode";
	public static final String ORDER_ZIPCODE_RESPCDE = "36";
	public static final String ORDER_COUNTRY_ERRMSG = "Invalid Country Code";
	public static final String ORDER_COUNTRY_RESPCDE = "37";
	public static final StringBuilder ORDER_PRODUCTCAT_PACKAGE_QRY = new StringBuilder();
	public static final String PACKAGEID = "packId";
	public static final String ORDER_NULL_STRING = "null";
	public static final String QUANTITY_ERROR_MSG = "Quantity should not be Zero";
	public static final String QUANTITY_RESPCDE = "38";
	public static final StringBuilder ORDER_PRODUCT_ACTIVATION_QRY = new StringBuilder();
	public static final String ORDER_ACTIVATION_CODERESPMSG = "Activation Code is Required.";
	public static final String ORDER_ACTIVATION_RESCDE = "39";
	public static final String API_VERSION_V1 = "V1";
	public static final String CARDSTAT_CRITERIA = "criteria";
	public static final String CARDSTAT_VALUE = "value";
	public static final String CARDSTAT_CARDS = "cards";
	public static final String CARDSTAT_EXPIRES_IN = "EXPIRES_IN";
	public static final String CARDSTAT_EXPIRES_IN_X_DAYS = "EXPIRES_IN_X_DAYS";
	public static final String CARDSTAT_LIST_OF_CARDS = "LIST_OF_CARDS";
	public static final String CARDSTAT_RANGE = "RANGE";
	public static final String CARDSTAT_DATEFORMAT_MMYY="(0[1-9]|1[012])\\d{2}";
	public static final String INVALID_FIELD = "Invalid";
	public static final String FSAPI_REPLACEMENTRENWAL_URI="/cards/replacement";
	public static final String REPLACEMENTAPI="REPLACEMENT";
	public static final String   REPLACEMENT_ORDERID="replacement_orderid";
	public static final String   REPLACEMENT_TYPE="type";
	public static final String   REPLACEMENT_VALUE="value";
	public static final String   REPLACEMENT_ACTION="action";
	public static final String   REPLACEMENT_SHIPPINGMETHOD="shippingMethod";
	public static final String   REPLACEMENT_POSTBACKRESPONSE="postBackResponse";
	public static final String   REPLACEMENT_POSTBACKURL="postBackURL";
	public static final String   REPLACEMENT_REQUESTREASON="requestReason";
	public static final String   REPLACEMENT_ISFEEWAIVED="isFeeWaived";
	public static final String   REPLACEMENT_COMMENTS="comments";
	public static final String   REPLACEMENT_SHIPTOCOMPANYNAME="shipToCompanyName";
	public static final String   REPLACEMENT_FIRSTNAME="firstName";
	public static final String   REPLACEMENT_MIDDLEINITIAL="middleInitial";
	public static final String   REPLACEMENT_LASTNAME="lastName";
	public static final String   REPLACEMENT_PHONE="phone";
	public static final String   REPLACEMENT_EMAIL="email";
	public static final String   REPLACEMENT_ADDRESSLINE1="addressLine1";
	public static final String   REPLACEMENT_ADDRESSLINE2="addressLine2";
	public static final String   REPLACEMENT_ADDRESSLINE3="addressLine3";
	public static final String   REPLACEMENT_CITY="city";
	public static final String   REPLACEMENT_STATE="state";
	public static final String   REPLACEMENT_POSTALCODE="postalCode";
	public static final String   REPLACEMENT_COUNTRY="country";
	public static final String   REPLACEMENT_CARDNUMBER="CardNumber";
	public static final String   STAN="Stan";
	
	public static final String   REPLACEMENT_PANLASTFOUR="PANLastFour";
	public static final String   REPLACEMENT_EXPDATE="expDate";
	public static final String   REPLACEMENT_FEE="fee";
	public static final String  REPLACERELOAD_PTBACK="REPLACERELOAD_PTBACK";
	public static final StringBuilder ORDER_PRODUCT_ENCRYPT_QRY = new StringBuilder();
	public static final String PRODUCTS_API="PRODUCTS";
	public static final String GET=":GET";
	public static final String POST=":POST";
	public static final String PUT=":PUT";
	public static final String SHIPPINGMETHOD = "shippingMethod";
	public static final String PIN = "pin";
	public static final String CARD_NUM_ENCR = "encryptedString";
	public static final String CARD_STATUS = "status";
	public static final String TRACKING_NUMBER = "trackingNumber";
	public static final String SHIPPING_DATE = "shippingDateTime";
	public static final String HASH_CARDNO="hashCardNumber";
	public static final String PROXY_PIN_ENCR="encryptedString";
	
	
	
	public static final StringBuilder POSTBACKSET_FLAG_QRY = new StringBuilder();
	public static final StringBuilder POSTBACKRESET_FLAG_QRY = new StringBuilder();
	public static final StringBuilder POSTBACKSET_FLAGUPDATE_QRY = new StringBuilder();
	public static final StringBuilder GET_FAILURE_POSTBACKRESP_QRY = new StringBuilder();
	public static final StringBuilder UPDATE_POSTBACKRESP_COUNT_QRY = new StringBuilder();
	public static final String RETRY_COUNT = "retryCount";
	public static final String CANCEL_ORDER_URL = "/orders/{orderid}/cancellation";
	public static final String CANCEL_ORDER_API = "CANCELORDER";
	public static final String CANCEL_ORDER_V1API = "CANCELORDERV1";
	public static final String POSTBACK_RESP_FLAG = "POSTBACK_RESP_FLAG";
	public static final String CLIENTNAME = "clientName";
	public static final String CLIENTID = "clientID";
	public static final String CHANNEL = "channel";
	public static final String FUNDING_OPTION = "fundingOption";
	public static final String INITIAL_LOAD_OPTION = "initialLoadOption";
	public static final String ORDER_FULFILLMENT = "ORDER_FULFILLMENT";
	public static final String CARD_ACTIVATION = "CARD_ACTIVATION";
	public static final String ORDER_AMOUNT = "ORDER_AMOUNT";
	public static final String CARD_ACTIVATION_AMOUNT = "CARD_ACTIVATION_AMOUNT";
	public static final String FUNDING_OVERRIDE = "FUNDING_OVERRIDE";
	public static final String FULFILLMENT_TYPE = "fulFillmentType";
	public static final String OUT_RESP_CODE = "outResponseCode";
	public static final String OUT_RESP_MSG = "outResponseMsg";
	public static final String OUT_RESP_STR = "respStr";
	public static final String INVALID_PRODUCT= "Invalid productId";
	public static final String EXPIRED_PRODUCT= "Expired Product";
	
	public static final String TYPE = "type";
	
	public static final String EXPDATE = "expDate";
	
	public static final String  DAO_SQL_EXEC = "SQL Exception in DAO Class";
	public static final String SQLEXCEPTION_DAO="89";
	public static final String DDMMYYYY = "yyyy-MM-dd";
	
	
	public static final String STOLEN_CARD = "Stolen Card";
	public static final String DAMAGED_CARD = "Damaged Card";
	public static final String EXPIRED_CARD = "Expired Card";
	public static final String ACTIVE_CARD = "Active Card";

	public static final String INACTIVE_CARD = "Inactive Card";
	public static final String LOST_CARD = "Lost Card";
	public static final String CLOSED_CARD = "Closed Card";
	
	public static final String INVALID_CARD_STATE = "Card is in an invalid state";
	public static final String INVALID_CARD = "Invalid Card";
	public static final String INVALID_PROXY_NUMBER="Invalid Proxy Number";
	public static final String RRN_GENERATING_EXCEPTION="Exception in generating the RRN";
	
	public static final char EMPTY_SPACE = ' ';
	public static final String TRIPLE_ZERO ="000";
	public static final String ERROR_CVV_GENERATION="CVV Generation Error";
	public static final String CVV1="cvv1";
	public static final String CVV2="cvv2";
	
	public static final String ORDER_STATE_INVALID_ORDER = "08";
	public static final String B2B_PRODUCT_ACT_VALUE="CARD_ACTIVATION_AMOUNT";
	public static final String B2B_PRODUCT_ORDER_VALUE="Order Amount";
	public static final String CORRELATIONID="correlationId";
	public static final String B2B_CARD_STATUS="Card is not in INACTIVE status";
	
	public static final String PROXY_NUMBER="proxy_number";
	public static final String SERIAL_NUMBER="serial_number";
	
	public static final String P_RESP_CODE_OUT="P_RESP_CODE_OUT";
	public static final String P_RESP_MSG_OUT="P_RESP_MSG_OUT";
	public static final String P_TRAN_FEE="P_TRAN_FEE";
	
	public static final String DENOMINATIONTYPE="denominationType";
	public static final String PRODUCTID="ProductID";
	
	public static final String CUSTOMERID="customerID";
	public static final String EXPIRATIONDATE="expirationDate";
	public static final String CARD_STATUS_CLOSED = "9";
	public static final String CARD_STATUS_DAMAGED = "3";
	public static final String CARD_STATUS_INACTIVE = "0";
	
	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";
	public static final String REVERT = "revert";
	public static final String UPDATE = "update";
	public static final String VENDOR_FULFILLMENT_TYPE="isBulkOrIND";
	
	
	public static final String DELIVERY_CHANNEL_SUPPORT = "Invalid Channel"; 
	
	public static final String IS_SEPARATE_CCF_REQUIRED = "isSeparateCCFRequired";
	
}
