/**
 * 
 */
package com.incomm.cclp.constants;

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
		
	
			/*ORDER*/
		public static final String SUCCESS_ORDER_CREATE = "ORDER001";
		public static final String SUCCESS_ORDER_RETRIEVE = "ORDER002";
		public static final String SUCCESS_ORDERS_APPROVED = "ORDERS_APPROVE_000";
		public static final String SUCCESS_ORDER_APPROVED = "ORDER_APPROVE_000";
		public static final String SUCCESS_ORDERS_REJECTED = "ORDERS_REJECT_000";
		public static final String SUCCESS_ORDER_REJECTED = "ORDER_REJECT_000";
		public static final String FAILED_ORDER_RETRIEVE = "ORDER005";
		public static final String ERR_ORDER_EXISTS = "ORDER003";
		
		public static final String ERR_MERCHANT_ID = "MER007";
		public static final String FAILED_MERCHANT_RETRIEVE = "MER005";
		
		public static final String FAILED_LOCATION_RETRIEVE = "LOC005";
		public static final String ERR_ORDER_APPROVED = "ORDER_006";
		public static final String ERR_ORDER_REJECTED = "ORDER_007";
		public static final String ERR_PRODUCT_ID = "ORDER008";
		public static final String ERR_MERCHANT_ID_OR_LOCATION_ID = "ORDER009";
		public static final String FAILED_AVAILABLE_INVENTORY = "ORDER010";
		public static final String ERR_NEGATIVE_AVAILABLE_INVENTORY = "ORDER011";
		public static final String ERR_AVAILABLE_INVENTORY = "ORDER012";
		public static final String INVALID_PRODUCT_VALIDITY_DATE = "ORDER013";
		
/*STOCK*/		
		public static final String ERR_MERCH_LOCATION_PRODUCT_ID = "STOCK001";
		public static final String FAILED_STOCK_RETRIEVE = "STOCK002";
		public static final String ERR_STOCK_NOT_EXISTS = "STOCK003";
		public static final String ALL_SUCCESS_STOCK_RETRIEVE = "STOCK004";
		public static final String ERR_MERCHANT_NOT_EXISTS = "STOCK005";
		public static final String ERR_STOCK_EXISTS = "STOCK006";
		public static final String ERR_REORDER_LEVEL = "STOCK007";
		public static final String ERR_REORDER_VALUE = "STOCK008";
		public static final String ERR_INSERTING_ORDER_FOR_STOCK = "STOCK009";

		
		
		public static final String ERR_JSON_VALUE = "B2B005";
		
	
		
		
		
		
		
}
