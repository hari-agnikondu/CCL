/**
 * 
 */
package com.incomm.cclp.fsapi.constants;

/**
 * TransactionQueryConstants class defines all the sql queries used by the
 * Transaction Service.
 * 
 * @author venkateshgaddam
 *
 */
public class TransactionQueryConstants {

	/**
	 * Constants class should not be instantiated.
	 */
	private TransactionQueryConstants() {
		throw new IllegalStateException("Transaction Constants class");
	}

	public static final String B2B_RESPONSE_CODE = "select t1.response_code,t2.channel_response_code,t2.delivery_response_description, t2.CHANNEL_CODE "
			+ "from RESPONSE_CODE t1,DELIVERY_CHANNEL_RESPONSE_CODE t2 where t1.RESPONSE_CODE=t2.RESPONSE_CODE "
			+ "AND t2.CHANNEL_CODE IN ('17','05','02','04')";

	public static final String FSAPI_TRANSACTION = "SELECT ft.CHANNEL_CODE, ft.MSG_TYPE, ft.TRAN_CODE, ft.DAO_CNAME, ft.REQUEST_TYPE, "
			+ "ft.REQUEST_METHOD, ft.VALIDATION_CNAME, ft.VERIFY_CNAME, ft.REVERSAL_TXN, ft.ACTION_NAME,"
			+ "ft.LOG_EXEMPTION, ft.CHANNEL_DESC, tran.IS_FINANCIAL, tran.CREDIT_DEBIT_INDICATOR, dl.CHANNEL_SHORT_NAME,"
			+ "dl.PASSIVE_SUPPORTED, tran.TRANSACTION_DESCRIPTION, tran.TRANSACTION_SHORT_NAME FROM FSAPI_TRANSACTION ft, "
			+ "TRANSACTION tran, DELIVERY_CHANNEL dl WHERE ft.TRAN_CODE = tran.TRANSACTION_CODE AND "
			+ "ft.CHANNEL_CODE=dl.CHANNEL_CODE AND dl.CHANNEL_CODE IN ('02', '04', '05') ";

	public static final String FSAPI_API = "SELECT API_NAME, REQ_ENCRYPT, RES_ENCRYPT, USER_IDENTIFY_TYPE, PARTNER_VALID, CUSTTYPE_VALID, REQ_METHOD, REQ_PARSE, VALIDATION_BYPASS, API_DESC FROM FSAPI_API";

	public static final String FSAPI_DETAILS = "SELECT API_NAME, REQ_FIELDS, RES_FIELDS, API_URL, SUBTAG_RESPFIELD, SUBTAG_REQFIELD, REQUEST_METHOD FROM FSAPI_DETAILS";

	public static final String FSAPI_VALIDATION_DTLS = "SELECT API_NAME, FIELD_NAME, PARENT_TAG, DATA_TYPE, FIELD_VALUES, REGEX_EXPRESSION, VALIDATION_TYPE, VALIDATION_ERRMSG, RESPONSE_CODE, SUB_TAG, SUB_TAGFIELD, REQUEST_METHOD FROM FSAPI_VALIDATION_DTLS";
	
	public static final String CARD_STATUS = "SELECT STATUS_CODE, STATUS_DESC FROM CARD_STATUS ";

}
