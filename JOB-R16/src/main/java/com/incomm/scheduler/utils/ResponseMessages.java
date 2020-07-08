/**
 * 
 */
package com.incomm.scheduler.utils;

/**
 * ResponseMessages class defines all the response message keys for the response messages returned by the Configuration
 * Service. The keys are mapped to key-value pairs in messages.properties
 * 
 * @author abutani
 *
 */
public class ResponseMessages {

	/**
	 * Constants class should not be instantiated.
	 */
	private ResponseMessages() {
		throw new IllegalStateException("Constants class");
	}

	public static final String RESULT = "ORDER_SUCCESS";
	public static final String CCF_RESULT = "CCF_SUCCESS";
	public static final String CN_RESULT = "CN_SUCCESS";

	public static final String MESSAGE_DELIMITER = "-";

	public static final String GENERIC_ERR_MESSAGE = "999";
	public static final String VALIDATION_HEADER_MSG = "003";

	public static final String SUCCESS = "000";
	public static final String FAILURE = "999";
	public static final String ALREADY_EXISTS = "001";
	public static final String DOESNOT_EXISTS = "002";
	public static final String ALREADY_APPR_REJ = "003";
	public static final String CONFIGSERVICE_EXCEPTION = "004";

	/* Ccf generation */
	public static final String CCF_GENERIC_ERROR_MESSAGE = "CCFGEN005";
	public static final String CCF_ERROR_CREATE_FOLDER = "CCFGEN006";
	public static final String CCF_NO_ORDER_TO_PROCESS = "CCFGEN007";
	public static final String CCF_ERROR_CCF_FILE_CREATE = "CCFGEN008";
	public static final String CCF_ERROR_WRITE_DETAILS = "CCFGEN009";
	public static final String CCF_ERROR_CVV_GENERATION = "CCFGEN010";
	public static final String CCF_NO_VENDOR_LINKED_TO_ORDER = "CCFGEN011";
	public static final String CCF_ERR_FILE_NAME = "CCFGEN012";
	public static final String CCF_ERR_FILE_NUMBER = "CCFGEN013";

	public static final String CN_RETRIEVED_SUCCESS = "CN005";
	public static final String CN_RETRIEVED_FAILED = "CN006";

	public static final String SERIAL_NUMBER_REQUEST_SUCCESS = "SRNUM_000";
	public static final String SERIAL_NUMBER_REQUEST_FAILURE = "failed";

}
