/**
 * 
 */
package com.incomm.scheduler.constants;

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


	public static final String MESSAGE_DELIMITER = "-";

	public static final String GENERIC_ERR_MESSAGE = "999";
	public static final String VALIDATION_HEADER_MSG = "003";
	public static final String SUCCESS = "000";
	public static final String FAILURE = "999";
	public static final String ALREADY_EXISTS = "001";
	public static final String DOESNOT_EXISTS = "002";
	public static final String ALREADY_APPR_REJ = "003";
	public static final String CONFIGSERVICE_EXCEPTION = "004";	
	public static final String BAD_REQUEST = "400";	
	
	/**
	 * CARD INVENTORY
	 */
	public static final String ALL_SUCCESS_CARDINVENTORYDTLS_RETRIEVE="CARDINVENTORYDTLS009";
	public static final String SUCCESS_INITIATE_CARDGEN="CARDINVENTORGEN000";
	public static final String ERR_CARDNUMGEN_NOT_YET_STARTED="CARDINVENTORYGEN005";
	public static final String FAILED_CARDNUMGEN_PAUSE="CARDINVENTORYGEN006";
	public static final String ERR_CARDNUMGEN_ALERYPAUSED="CARDINVENTORYGEN007";
	public static final String FAILED_TO_START_CARDNUMGEN="CARDINVENTORYGEN008";
	public static final String ERR_CARDNUMGEN_ALERYSTARTED="CARDINVENTORYGEN009";
	public static final String ERR_CARDNUMGEN_ALERY_PAUSED="CARDINVENTORYGEN010";
	public static final String ERR_CARDNUMGEN_ALERY_COMPLETED="CARDINVENTORYGEN011";
	public static final String FAILED_TO_RESUME_CARDNUMGEN="CARDINVENTORYGEN012";
	public static final String SUCCESS_RESUME_CARDGEN="CARDINVENTORGEN013";
	public static final String SUCCESS_PAUSE_CARDGEN="CARDINVENTORGEN014";
	public static final String SUCCESS_REGEN_CARDGEN="CARDINVENTORGEN015";
	public static final String ERR_CARDRANGE_ID = "CARDRANGE_021";
	public static final String CARDRANGE_DOESNOT_EXIT = "CARDRANGE_002";
	
}
