package com.incomm.cclp.fsapi.constants;

public class B2BResponseCode {

	private B2BResponseCode() 
	{
		throw new IllegalStateException("Constants class");
	}

	public static final String ERR_JSON_VALUE = "B2B005";
	
	public static final String SQLEXCEPTION_DAO="B2B006";
	
	public static final String INVALID_DATA_ELEMENT="B2B007";
	
	public static final String SYSTEM_ERROR="B2B008";
	
	public static final String SUCCESS="R0001";
	public static final String INVALID_PARTNER_ID="R0057";
	public static final String INVALID_ORDER_ID="R0059";
	public static final String INVALID_CARD_STATUS="R0049";
	
	public static final String INVALID_CARD_NUMBER="R0019";
	public static final String INVALID_PROXY_NUMBER="R0069";
	public static final String INVALID_SERIAL_NUMBER="R0075";
	public static final String ACTIVATION_CODE_NOT_MATCHED="R0081";
	public static final String INVALID_ORDER_STATUS="R0091";
	public static final String INVALID_LINE_ITEM_STATUS="R0097";
		
	public static final String START_AND_END_SERIAL_NUMBERS_NOT_BELONGS_TO_SAME_PRODUCT="R0104";
	public static final String REQUIRED_MESSAGE_ELEMENTS_NOT_PRESENT="R0002";
	public static final String TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR="R0012";
	public static final String INVALID_LINE_ITEM_ID="R0142";
	public static final String DUPLICATE_ORDER="R0143";
	
	
	public static final String INVALID_PRODUCTID_AND_PACKAGE_ID_COMBINATION="R0144";
	public static final String INVALID_PRODUCT_TYPE="R0145";
	public static final String INVALID_FIELD="R0146";
	public static final String START_AND_END_SERIAL_OR_PROXY_NUMBERS_NOT_BELONGS_TO_SAME_ORDER="R0147";
	public static final String CARD_ALREADY_REPLACED="R0148";
	public static final String CARD_REPLACEMENT_IS_NOT_ALLOWED_TO_CUSTOMER="R0149";
	public static final String STATE_CODE_IS_NOT_VALID="R0150";
	public static final String REPLACEMENT_NOT_ALLOWED_FOR_VIRTUAL_PRODUCT="R0151";
	
	public static final String EXPIRED_CARD = "R0022";
	public static final String STOLEN_CARD = "R0023";
	public static final String LOST_CARD = "R0024";
	public static final String DAMAGED_CARD = "R0025";
	public static final String ACTIVE_CARD = "R0014";
	public static final String INACTIVE_CARD = "R0015";
	public static final String CLOSED_CARD = "R0026";
	public static final String INVALID_CARD_STATE = "R0049";
	public static final String ACTIVATION_ALEREDY_DONE="R0154";
	public static final String DAILY_MAXIMUM_TRANSACTION_AMOUNT_REACHED = "R0012";
	public static final String DAILY_MAXIMUM_TRANSACTION_COUNT_REACHED = "R0012";
	public static final String WEEKLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED = "R0012";
	public static final String WEEKLY_MAXIMUM_TRANSACTION_COUNT_REACHED = "R0012";
	public static final String MONTHLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED = "R0012";
	public static final String MONTHLY_MAXIMUM_TRANSACTION_COUNT_REACHED = "R0012";
	public static final String YEARLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED = "R0012";
	public static final String YEARLY_MAXIMUM_TRANSACTION_COUNT_REACHED = "R0012";
	
	public static final String TRANSACTION_AMOUNT_IS_GREATER_THAN_MAXIMUM_PER_TXN_AMOUNT = "R0012";
	public static final String TRANSACTION_AMOUNT_IS_LESS_THAN_MINIMUM_PER_TXN_AMOUNT = "R0012";
	public static final String TRANSACTION_NOT_SUPPORTED_ON_PARTNER = "R0012";
	public static final String INVALID_ORDER_ID_PARTNER_ID_COMBINATION = "R0184";
	
	
	/**
	 * Reloadable_check
	 */
	
	public static final String TOP_UP_NOT_SUPPORTED="R0187";
	public static final String REQUIRED_SERIAL_NUMBERS_ARE_NOT_AVAILABLE="R0188";
	
	public static final String INVALID_DENOMINATION = "R0189";
	public static final String INVALID_SHIPPINGMETHOD = "R0190";
	/*public static final String REQUIRED_CARD_NUMBERS_NOT_AVAILABLE = "R0191";*/
	public static final String REQUIRED_REQUIRED_PROPERTY = "R0192";
	public static final String DUPLICATE_ORDER_LINE_ITEM="R0193";
	public static final String QUANTITY_SHOULD_NOT_BE_ZERO="R0194";
	
	public static final String REQUIRED_QUANTITY_ZERO = "R0194";
	public static final String REQUIRED_QUANTITY_EXCEED = "R0195";
	public static final String REQUIRED_CARD_NUMBERS_NOT_AVAILABLE = "R0196";
	
	public static final String EXPIRED_PRODUCT = "R0200";
	
}
