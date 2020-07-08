package com.incomm.cclp.constants;

public class ValueObjectKeys {

	private ValueObjectKeys(){
		throw new IllegalStateException("Value Object Keys class");
	}
	public static final String REQUEST="request";
	public static final String REQUEST_METHOD_TYPE="RequestMethodType";
	public static final String API_NAME="API_NAME";
	public static final String REQ_PARSE = "reqParse";
	public static final String BUSINESS_TIME = "LocalTranTime";
	public static final String BUSINESSTIMEZONE = "LocalTranTimeZone";
	public static final String DATE_TIME = "transactionDateTime";
	public static final String BUSINESS_DATE = "LocalTranDate";
	public static final String BUSINESS_DATE_TIME = "LocalTranDateTime";

	public static final String TXN_MODE = "reversalFlag";
	public static final String IS_FINANCIAL = "isFinacial";
	public static final String CREDIT_DEBIT_INDICATOR = "CreditDebitIndicator";
	public static final String DELIVERY_CHANNEL_SHORT_NAME = "deliveryChannelShortName";
	public static final String TRANSACTIONDESC = "Transdesc";

	public static final String LOG_EXEMPTION = "logExemption";
	public static final String DELIVERYCHNL = "DeliveryChnl";
	public static final String MEMBERNO = "CardSequenceNumber";
	public static final String ORIGINAL_MSGTYPE = "OrigMSGTYPE";
	public static final String PASSIVE_SUPPORT_FLAG = "PASSIVE_SUPPORT_FLAG";
	public static final String REVERSAL_FLAG = "reversalFlag";

	public static final String CHANNEL_CODE = "channelCode";
	public static final String MSGTYPE = "MsgType";
	public static final String TRANS_CODE = "TranCode";
	public static final String DAO_CLASSNAME = "DAOClassName";
	public static final String VALIDATION_CLASSNAME = "validationClassName";
	public static final String VERIFICATION_CLASSNAME = "verificationClassName";
	public static final String REVERSAL_CODE = "reversalCode";
	public static final String APIREVERSALFLAG = "Apireversalflag";

	public static final String LOG_TXN = "logTxn";
	public static final String REGEX_API_NAME = "regexApiName";
	public static final String REGEX_VALIDATION_BYPASS = "RegexValidationBypass";
	public static final String RRN = "RRN";

	public static final String REQUEST_ENCR_TYPE = "RequestEncrType";
	public static final String INCOM_RRN_NUMBER = "RRN";

	//added for headers
	public static final String X_INCFS_CORRELATIONID = "x-incfs-correlationid";
	public static final String X_INCFS_CHANNEL = "x-incfs-channel";
	public static final String X_INCFS_CHANNEL_IDENTIFIER = "x-incfs-channel-identifier";
	public static final String X_INCFS_DATE = "x-incfs-date";
	public static final String X_INCFS_PARTNERID = "x-incfs-partnerid";

	//added for response
	public static final String FSAPIRESPONSECODE = "responseCode";
	public static final String RESPONSEMESSAGE = "responseMessage";

	public static final String CARDSTATUS="CardStatus";

	public static final String CARDNO="PrimaryAccountNumber";
	public static final String AVAILABLE_BALACE = "availableBalance";

	public static final String CARDNUMBER="CardNumber";
	public static final String ENCR_CARDNO="encryptCardNumber";
	public static final String HASH_CARDNO="hashCardNumber";
	public static final String EXPIRATIONDATE="expirationDate";
	public static final String CUST_CODE="custcode";
	public static final String EXPIREDFLAG ="EXPIREDFLAG";
	public static final String PROD_CODE="prodcode";
	public static final String PROD_CATG="cardtype";
	public static final String CARD_TYPE="cardtype";
	public static final String TERMINALID="CardAcceptTermID";
	public static final String INSTCODE="InstitutionCode";
	public static final String IPADDRESS="IPAddress";
	public static final String CURRENCY_CODE="TranCurrCode";
	public static final String HASH_KEY = "HashKey";
	public static final String USERID="UserId";   
	public static final String DEVICE_MOBILENUMBER="DeviceMobileNumber"; 
	public static final String DEVICE_ID="DeviceID";
	public static final String TIME_STAMP = "Time_Stamp";
	public static final String ACTIVATION_CODE="ActivationCode";
	public static final String MERCHANT_NAME="MerchantName";
	public static final String POSTBACKRESPONSE="postBackResponse";
	public static final String POSTBACKURL="postBackURL";
	public static final String TRANAMOUNT="TranAmt";
	public static final String TYPE="type";
	public static final String VALUE="value";
	public static final String AVAILABLEBALANCE="availableBalance";
	public static final String RELOAD_ID="Reload_ID";
	public static final String DENOM="denomination";
	public static final String COMMENTS="comments";
	public static final String MERCHANT_ID="merchantId";
	public static final String LAST_TXN_DATE = "LastTransactionDate";
	public static final String PARTIAL_PREAUTH_INDICATOR="PartialAuthIndicator";
	public static final String FIRST_TIMETOPUP_FLAG="TopupFlag";
	public static final String REQTIME_ZONE = "TimeZone";
	public static final String CARD_ACTIVATION_DATE = "CardActivationDate";
	public static final String PROXYNUMBER = "proxyNumber";
	public static final String SERIALNUMBER = "serialNumber";
	public static final String EXPIRY_DATE="expiryDate";
	public static final String CARD_EXPIRY_DATE="cardExpiryDate";
	public static final String SERIAL_NUMBER="serialnumber";

	public static final String CARD_NEW_EXPIRYDATE = "CardNewExpiryDate";
	public static final String CARD_CARDSTAT="DBCardStatus";
	public static final String BUSINESSDATE="LocalTranDate";
	public static final String BUSINESSTIME="LocalTranTime";
	public static final String TRANS_SHORT_NAME = "TranshrtName";

	public static final String CARD_ORDER_DTL_STATUS="cardDTLStatus";
	public static final String CARD_ACCOUNT_ID="cardAccountID";
	public static final String B2B_PRODUCT_FUND="productFunding";
	public static final String B2B_SOURCE_FUNDING="sourceFunding";
	
	public static final String ISACTIVE="true";
	public static final String REQUEST_ACTIVATION_CODE="activationCode";
	public static final String PRODUCT_GENERAL="General";
	
	public static final String ORIGINAL_HEADER="originalHeader";
	public static final String 	ACCOUNT_NUMBER="AccountNumber";
	
	public static final String CARD_USAGE_LIMIT = "cardUsageLimit";
	public static final String ORIGINAL_TRANSACTION_SHORT_NAME="originaltransactionShortName";
	public static final String LEDGER_BALANCE = "ledgerBalance";

	public static final String EXPIRYDATE="ExpiryDate";
	public static final String ISSUER_ID="issuerID";
	public static final String PARTNER_ID="partnerID";
	
	public static final String DECRYPT_REQUEST = "decryptRequest";	
	public static final String PARTNER_VALIDATION = "partnerValidation";
	public static final String ENCRCPT_REQUEST = "request";
	public static final String AUTH_ID="authId";
	public static final String SEQ_ID="seqID";
	
	public static final String PRODUCT_FUNDING = "productFund";
	public static final String FUND_AMOUNT = "fundAmount";
	public static final String FUNDING_OVERRIDE="fundOverride";
	public static final String DENOMINATION="denomination";
	
	public static final String TRAN_LOG_UPDATE="isTranlogUpdated";
	public static final String UPDATE_YES="yes";
	
	
	public static final String ISHAVECARDQUERYRESULTNO="no";
	public static final String ISHAVECARDQUERYRESULTYES="yes";
	public static final String ISHAVEESULT="haveResult";
	
	
	
	//FEE_CALC CONSTANTS
	public static final String P_PRODUCTID="p_productid";
	public static final String P_TRANSACTION_CODE="p_transaction_code";
	public static final String P_DELIVERY_CHANNEL="p_delivery_channel";
	public static final String P_MSGTYPE="p_msgtype";
	public static final String P_TXN_AMT="p_txn_amt";
	public static final String P_ACCOUNTID="p_accountid";
	public static final String P_LAST_TXNDATE="p_last_txndate";
	public static final String P_RESP_CODE="P_RESP_CODE";
	//Statement log

	public static final String P_CARD_NUM_HASH ="p_card_num_hash"; 
	public static final String P_CARD_NUM_ENCR ="p_card_num_encr"; 
	public static final String P_OPENING_BALANCE ="p_opening_balance";
	public static final String P_CLOSING_BALANCE ="p_closing_balance";
	public static final String P_TRANSACTION_AMOUNT ="p_transaction_amount";
	public static final String P_CREDIT_DEBIT_FLAG ="p_credit_debit_flag";
	public static final String P_TRANSACTION_NARRATION="p_transaction_narration";
	public static final String P_RRN  ="p_rrn"; 
	public static final String P_AUTH_ID="p_auth_id";
	public static final String P_TRANSACTION_DATE="p_transaction_date";
	public static final String P_TRANSACTION_TIME="p_transaction_time";
	public static final String P_FEE_FLAG="p_fee_flag"; 
	public static final String P_DELIVERY_CHANNEL_STATNENT="p_delivery_channel"; 
	public static final String P_TRANSACTION_CODE_STATNENT="p_transaction_code"; 
	public static final String P_ACCOUNT_ID="p_account_id";  
	public static final String P_TO_ACCOUNT_ID="p_to_account_id";
	public static final String P_MERCHANT_NAME="p_merchant_name";
	public static final String P_MERCHANT_CITY="p_merchant_city";
	public static final String P_MERCHANT_STATE="p_merchant_state";
	public static final String P_CARD_LAST4DIGIT="p_card_last4digit";
	public static final String P_PRODUCT_ID="p_product_id";
	public static final String P_RECORD_SEQ="p_record_seq";
	public static final String P_PURSE_ID="p_purse_id"; 
	public static final String P_TO_PURSE_ID="p_to_purse_id";
	public static final String P_TRANSACTION_SQID="p_transaction_sqid"; 
	public static final String P_BUSINESS_DATE="p_business_date";
	public static final String P_STORE_ID="p_store_id"; 
	public static final String EXPRYFLAGTRUE="T"; 
	public static final String EXPRYFLAGFALSE="F"; 
	public static final String REPLACERENEWTYPE="REPLACERENEW_TYPE"; 
	public static final String REPLACERENEWVALUE="REPLACERENEW_VALUE"; 
	
	
	public static final String P_FIRST_NAME_IN ="p_first_name_in"; 
	public static final String P_MIDDLEINITIAL_IN ="p_middleinitial_in"; 
	public static final String P_LAST_NAME_IN ="p_last_name_in";
	public static final String P_EMAIL_IN ="p_email_in";
	public static final String P_PHONE_IN ="p_phone_in";
	public static final String P_ADDRESSLINE_ONE_IN ="p_addressline_one_in";
	public static final String P_ADDRESSLINE_TWO_IN="p_addressline_two_in";
	public static final String P_ADDRESSLINE_THREE_IN  ="p_addressline_three_in"; 
	public static final String P_STATE_IN="p_state_in";
	public static final String P_CITY_IN="p_city_in";
	public static final String P_COUNTRY_IN="p_country_in";
	public static final String P_POSTAL_CODE_IN="p_postal_code_in"; 
	public static final String P_COMMENTS_IN="p_comments_in"; 
	public static final String P_REQUEST_REASON_IN="p_request_reason_in"; 
	public static final String P_SHIPPINGMETHOD_IN="p_shippingmethod_in";  
	public static final String P_ISFEEWAIVED_IN="p_isfeewaived_in";
	public static final String P_SHIP_COMPANYNAME_IN="p_ship_companyname_in";
	
	public static final String P_CARD_EXPIRTY_DATE_OUT="P_CARD_EXPIRTY_DATE_OUT";
	public static final String P_AVAILABLE_BALANCE_OUT="P_AVAILABLE_BALANCE_OUT";
	public static final String P_LAST4DIGITS_PAN_OUT="P_LAST4DIGITS_PAN_OUT";
	public static final String P_CARD_FEE_OUT="P_CARD_FEE_OUT";
	public static final String P_RESP_CODE_OUT="P_RESP_CODE_OUT";
	public static final String P_RESP_MESSGE_OUT="P_RESP_MESSGE_OUT";
	
	public static final String STATUS="status";
	public static final String PRODUCT ="Product";
	public static final String SERLNUMBER ="SerialNumber";
	public static final String DENOMINATION_TYPE ="denominationType";
	public static final String ORDERID ="orderId";
	public static final String PARTNERID ="partnerId";
	public static final String PRODUCTID ="productId";
	/**
	 * Relodable Flag for reload transaction
	 */
	public static final String RELOADABLE_FLAG = "reloadableFlag";
	public static final String DISABLE="Disable";
	public static final String CARD_STATUS_CLOSED = "9";
	public static final String CARD_STATUS_DAMAGED = "3";
	public static final String CARD_STATUS_INACTIVE = "0";
	
	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";
	public static final String CARD_STATUS_CHANGED = "cardStatusChanged";
	
	
	public static final String PURSE_ID = "purseId";
	
	
	
	
	 
	
	
	
}