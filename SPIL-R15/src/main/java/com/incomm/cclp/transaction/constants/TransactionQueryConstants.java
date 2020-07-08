/**
 * 
 */
package com.incomm.cclp.transaction.constants;

/**
 * TransactionQueryConstants class defines all the sql queries used by the Transaction Service.
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

	public static final String TRANSACTION_INPUT_VALIDATION = "SELECT CHANNEL_CODE,TRANSACTION_CODE,MESSAGE_TYPE,FIELD_NAME,VALIDATION_TYPE,VALIDATION_PATTERN,PARENT_TAG FROM TRANSACTION_INPUT_VALIDATION WHERE CHANNEL_CODE in ('01','06', '03','08')";

	public static final String TRANSACTION_SPIL_MESSAGE_TYPE = "SELECT dct.CHANNEL_CODE,dct.MESSAGE_TYPE,dct.CHANNEL_TRANSACTION_CODE,dct.TRANSACTION_CODE, dct.PARTY_SUPPORTED, dct.AUTH_JAVA_CLASS,tran.IS_FINANCIAL,tran.CREDIT_DEBIT_INDICATOR, dl.CHANNEL_SHORT_NAME, dl.PASSIVE_SUPPORTED,tran.TRANSACTION_DESCRIPTION,tran.TRANSACTION_SHORT_NAME FROM DELIVERY_CHANNEL_TRANSACTION dct,TRANSACTION tran,"
			+ " DELIVERY_CHANNEL dl WHERE dct.TRANSACTION_CODE=tran.TRANSACTION_CODE"
			+ " AND dct.CHANNEL_CODE=dl.CHANNEL_CODE AND dct.CHANNEL_CODE in ('01','06', '03','08') ";

	public static final String TRANSACTION_AUTH_DEFINITION = "SELECT TRANSACTION_CODE, MSG_TYPE, PROCESS_KEY, PROCESS_TYPE, EXECUTION_ORDER, VALIDATION_TYPE,CHANNEL_CODE "
			+ "FROM TRANSACTION_AUTH_DEFINITION WHERE CHANNEL_CODE in ('01','06', '03','08') ORDER BY EXECUTION_ORDER";

	public static final String SPIL_RESPONSE_CODE = "select t1.response_code,channel_response_code,t2.delivery_response_description from RESPONSE_CODE t1,DELIVERY_CHANNEL_RESPONSE_CODE t2 where t1.RESPONSE_CODE=t2.RESPONSE_CODE AND t2.CHANNEL_CODE=:channelCode";

	public static final String CARD_STATUS = "SELECT STATUS_CODE, STATUS_DESC FROM CARD_STATUS ";

	public static final String RULE_PARAMETERS = "SELECT param_id, param_type FROM rule_parameters ";

	public static final String RULE_PARAMETERS_MAPPING = " select param_id||delivery_channel||tran_code, tran_obj_name from RULE_PARAM_TRANOBJ_MAPPING";

	public static final String DUPLICATE_CHECK_QRY = "select tran_reverse_flag,orgnl_tran_amount,auth_amount,tranfee_amount,cr_dr_flag,max_fee_flag,free_fee_flag,conversion_rate,transaction_amount from Transaction_Log where rrn=? and delivery_channel = ?  and msg_type = ? and response_id= ? AND transaction_code=? AND card_number=?"; // VMSCL-729
	public static final String SPIL_TRANSACTIONS = "SELECT TRANSACTION_CODE,TRANSACTION_DESCRIPTION,TRANSACTION_SHORT_NAME,IS_FINANCIAL,CREDIT_DEBIT_INDICATOR FROM TRANSACTION ";

	public static final String TRANSACION_COUNT_BY_RRN_DELIVERY_CHANNEL_MSG_TYPE_MDM_ID = "select count(1) from Transaction_Log where rrn=? and delivery_channel=? and msg_type=? and mdm_id=?";

	public static final String TRANSACION_INFO_BY_RRN_DELIVERY_CHANNEL_MSG_TYPE_MDM_ID = "select request_xml from Transaction_Log where rrn=? and delivery_channel=? and msg_type=? and mdm_id=? and response_id=? and rownum <=1 ";

}
