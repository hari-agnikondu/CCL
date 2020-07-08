package com.incomm.cclp.fsapi.constants;

public class B2BResponseMessage {

	private B2BResponseMessage() 
	{
		throw new IllegalStateException("Constants class");
	}

	public static final String ERR_JSON_VALUE = "Invalid json input";
	
	public static final String  API_DELIVERYCHNL_NOTFOUND = "Delivery Channel Details Not Found";
	
	public static final String SYSTEM_ERROR = "System Error";
	
	
	public static final String SUCCESS="Success";
	public static final String INVALID_PARTNER_ID="Invalid Partner ID";
	public static final String INVALID_ORDER_ID="Invalid Order ID";
	public static final String INVALID_CARD_STATUS="Invalid Card Status";
	
	public static final String INVALID_CARD_NUMBER="Invalid Card Number";
	public static final String INVALID_PROXY_NUMBER="Invalid Proxy Number";
	public static final String INVALID_SERIAL_NUMBER="Invalid Serial Number";
	public static final String ACTIVATION_CODE_NOT_MATCHED="Activation Codes Not Matched";
	public static final String INVALID_ORDER_STATUS="Invalid Order Status";
	public static final String INVALID_LINE_ITEM_STATUS="Invalid Line Item Status";
		
	public static final String START_AND_END_SERIAL_NUMBERS_NOT_BELONGS_TO_SAME_PRODUCT="Start and End Serial Numbers not Belongs to Same Product";
	public static final String REQUIRED_MESSAGE_ELEMENTS_NOT_PRESENT="Invalid Request";
	public static final String TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR="Transaction Declined Due to System Error";
	public static final String INVALID_LINE_ITEM_ID="Invalid Line Item ID";
	public static final String DUPLICATE_ORDER="Duplicate Order";
	public static final String INVALID_PRODUCTID_AND_PACKAGE_ID_COMBINATION="Invalid ProductID and Package ID Combination";
	public static final String INVALID_PRODUCT_TYPE="Invalid Product Type";
	public static final String INVALID_FIELD="Invalid input Field";
	public static final String START_AND_END_SERIAL_OR_PROXY_NUMBERS_NOT_BELONGS_TO_SAME_ORDER="Start and End Serial/Proxy Numbers not Belongs to Same Order";
	public static final String CARD_ALREADY_REPLACED="Card already Replaced";
	public static final String CARD_REPLACEMENT_IS_NOT_ALLOWED_TO_CUSTOMER="Card replacement is not allowed to customer who changed address in last 24 hr";
	public static final String STATE_CODE_IS_NOT_VALID="State code is not valid";
	public static final String REPLACEMENT_NOT_ALLOWED_FOR_VIRTUAL_PRODUCT="Replacement Not Allowed For Virtual product";
	public static final String RRN_GENERATING_EXCEPTION_MSG="Exception in generating the RRN";
	public static final String MANDATORY_FIELD_FAILURE = "Required Property Not Present";
	public static final String DAILY_MAX_CNT_REACHED = "Daily maximum transaction count reached";
	public static final String WEEKLY_MAX_CNT_REACHED = "Weekly maximum transaction count reached";
	public static final String MONTHLY_MAX_CNT_REACHED = "Monthly maximum transaction count reached";
	public static final String YEARLY_MAX_CNT_REACHED = "Yearly maximum transaction count reached";
	public static final String LESS_THAN_MIN_AMT_PER_TXN = "Transaction Amount Is Less Than Minimum Per Txn Amount";
	public static final String GREATER_THAN_MAX_AMT_PER_TXN = "Transaction Amount Is Greater Than Maximum Per Txn Amount";
	public static final String EXCP_IN_LIMIT_VALIDATION = "Exception occurred in limit validation";
	public static final String LIMIT_UPDATION_FAILED = "Limit updation failed";
	public static final String LIMIT_REVERT_FAILED = "Failed to revert Limit";
	public static final String INVALID_INPUT_DATA = "Invalid input data";
	public static final String PASSIVE_STATUS_UPDATION_FAILED = "Exception in updating passive status";
	public static final String DAILY_MAX_AMT_REACHED = "Daily maximum transaction amount reached";
	public static final String WEEKLY_MAX_AMT_REACHED = "Weekly maximum transaction amount reached";
	public static final String MONTHLY_MAX_AMT_REACHED = "Monthly maximum transaction amount reached";
	public static final String YEARLY_MAX_AMT_REACHED = "Yearly maximum transaction amount reached";
	public static final String INVALID_ORDER_ID_PARTNER_ID_COMBINATION = "NO ORDER EXISTS for ORDER ID AND PARTNER ID Combination";
	
	/**
	 * RELOADABLE CHECK
	 */
	public static final String TOP_UP_NOT_SUPPORTED="Top up is not applicable on this card number";
	public static final String QUANTITY_SHOULD_NOT_BE_ZERO="Quantity should not be Zero";
	public static final String QUANTITY_SHOULD_NOT_EXCEED_BE_ZERO ="Quantity exceeded the configured quantity";
	public static final String FAILURE="Failure";
}
