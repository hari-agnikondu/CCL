/**
 * 
 */
package com.incomm.cclpvms.constants;

/**
 * ResponseMessages class defines all the response message keys for the 
 * response messages returned by the Configuration Service.
 * The keys are mapped to key-value pairs in messages.properties
 * 
 * @author abutani
 *
 */
public class ResponseMessages {
	
	/**
	 * Constants class should not be instantiated.
	 */
	private ResponseMessages() 
	{
		throw new IllegalStateException("Constants class");
	}

	public static final String GENERIC_ERR_MESSAGE = "999";
	public static final String MESSAGE_DELIMITER = "-";
	public static final String VALIDATION_HEADER_MSG = "003";
	
	
	public static final String SUCCESS_ISSUER_RETRIEVE = "ISS002";
	public static final String SUCCESS_ISSUER_UPDATE = "ISS003";
	public static final String SUCCESS_ISSUER_DELETE = "ISS004";
	
	public static final String ERR_ISSUER_NULL = "ISS005";
	public static final String ERR_ISSUER_NAME = "ISS006";
	public static final String ERR_ISSUER_ID = "ISS007";
	public static final String ERR_ISSUER_INS_USER = "ISS008";
	public static final String ERR_ISSUER_UPD_USER = "ISS009";
	public static final String ERR_CARD_RANGE_EXISTS = "ISS014";
	public static final String SERVER_DOWN = " Configuration Server is Down, Please Try Later !!!";

	public static final String ERR_PARTNER_FETCH="PARTNER001";
	public static final String PARTNER="PARTNER002";
	public static final String RECORD_FETCH="004";
	public static final String ADD_SUCCESS="005";
	public static final String UPDATE_SUCCESS="006";
	public static final String DELETE_SUCCESS="007";
	public static final String ADD_FAIL="008";
	public static final String UPDATE_FAIL="009";
	public static final String DELETE_FAIL="010";
	
	
	
	public static final String SUCCESS = "000";
	public static final String ALREADY_EXISTS = "001";
	public static final String DOESNOT_EXISTS = "002";
	public static final String ALREADY_APPR_REJ = "003";
	public static final String CONFIGSERVICE_EXCEPTION = "004";
	public static final String FAILURE = "999";
	
	public static final String ISSUER = "ISSUER_002";
	
		
	public static final String NEW_STATUS="NEW";
	/**
	 *  PRODUCT */
	public static final String SUCCESS_PRODUCT_CREATE = "PRS001";
	public static final String SUCCESS_PRODUCT_RETRIEVE = "PRS002";
	public static final String SUCCESS_PRODUCT_UPDATE = "PRS003";
	public static final String SUCCESS_PRODUCT_DELETE = "PRS004";
	
	public static final String ERR_PRODUCT_NULL = "PRS005";
	public static final String ERR_PRODUCT_NAME = "PRS006";
	public static final String ERR_PRODUCT_ID = "PRS007";
	public static final String ERR_PRODUCT_INS_USER = "PRS008";
	public static final String ERR_PRODUCT_UPD_USER = "PRS009";
	public static final String ERR_PRODUCT_EXISTS = "PRS010";
	
	public static final String REC_NOT_EXISTS = "010";
	
	/*CardRange*/
	public static final String CARD_RANGE="CR002";
	
	
	public static final String APPROVE_SUCCESS="CR011";
	public static final String REJECT_SUCCESS="CR012";
	
	public static final String PRODUCT_LIMIT_UPDATE_FAIL="PRODLIMIT009";
	public static final String PRODUCT_TXNFEE_FETCH_FAIL="PRODTXNFEE001";
	public static final String PRODUCT_MONTHLYFEECAP_UPDATE_FAIL="PRODMONTHLYFEECAP009";
	
	
	/**
	 * Alerts*/
	public static final String SUCCESS_PRODUCT_ALERT_UPDATE = "PRODUCTALERT000";
	public static final String FAILED_PRODUCT_ALERT_UPDATE = "PRODUCTALERT001";
	public static final String ERR_PRODUCT_ALERT_ATTRIBUTE_EMPTY = "PRODUCTALERT002";
	public static final String ERR_PRODUCT_ALERT_LANGUAGE_ATTRIBUTE_EMPTY = "PRODUCTALERT003";
	public static final String EMPTY_PRODUCT_ALERT_RETRIEVE = "PRODUCTALERT004";
	public static final String SUCCESS_PRODUCT_ALERT_RETRIEVE = "PRODUCTALERT005";
	public static final String EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE = "PRODUCTALERT006";
	
	/**
	 * User*/
	public static final String ERR_USER_NOT_FOUND = "USER_002";
	
	public static final String ERR_JOB_SCHEDULER_FETCH = "JOBSCHEDULER001";

	public static final String USER_NOT_FOUND = "USR_NOT_FOUND_008";
	/*	stock*/
	public static final String MERCHANT_FETCH_001= "MERCHANT_FETCH_001";
	public static final String LOCATION_FETCH_001= "LOCATION_FETCH_001";
	/** 
	 * Fulfillment
	 */
	public static final String ERR_FULFILLMENT_FETCH="FULFILLMENT_001";
	public static final String ERR_B2B_CN_FILE_REQUIRED="FULFILLMENT_002";
	
}
