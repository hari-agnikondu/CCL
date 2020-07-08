package com.incomm.cclp.constants;

public class ResponseCodes {

	private ResponseCodes() {
		throw new IllegalStateException("ResponseCodes class");
	}

	public static final String SUCCESS = "R0001";
	public static final String INVALID_REQUEST = "R0002";
	public static final String SPIL_ACTIVATION_NOT_DONE_FOR_THIS_CARD = "R0003";
	public static final String INVALID_TERMINAL = "R0004";
	public static final String INVALID_RETAILER = "R0005";
	public static final String INVALID_LOCATION = "R0006";
	public static final String INVALID_TRACK1 = "R0007";
	public static final String INVALID_PRODUCT_IDENTIFIER = "R0008";
	public static final String INVALID_UPC = "R0009";
	public static final String INVALID_PIN = "R0010";

	public static final String INVALID_AMOUNT = "R0011";
	public static final String SYSTEM_ERROR = "R0012";
	public static final String TRANSACTION_DECLINED_BY_RULE = "R0013";
	public static final String ACTIVE_CARD = "R0014";
	public static final String INACTIVE_CARD = "R0015";

	public static final String CARD_IS_REDEEMED = "R0016";
	public static final String INVALID_TRACK2 = "R0017";
	public static final String INVALID_USERID_OR_PASSWORD = "R0018";
	public static final String INVALID_CARD = "R0019";
	public static final String CARD_NOT_FOUND = "R0020";

	public static final String CVV_VERIFICATION_FAILED = "R0021";
	public static final String EXPIRED_CARD = "R0022";
	public static final String STOLEN_CARD = "R0023";
	public static final String LOST_CARD = "R0024";
	public static final String DAMAGED_CARD = "R0025";

	public static final String CLOSED_CARD = "R0026";
	public static final String INSUFFICIENT_FUNDS = "R0027";
	public static final String NOT_REVERSIBLE_OR_ALREADY_REVERSED = "R0028";
	public static final String ORIGINAL_TRANSACTION_NOT_FOUND = "R0029";
	public static final String EXCEEDING_MAXIMUM_CARD_BALANCE = "R0030";

	public static final String DAILY_MAXIMUM_TRANSACTION_AMOUNT_REACHED = "R0031";
	public static final String DAILY_MAXIMUM_TRANSACTION_COUNT_REACHED = "R0032";
	public static final String WEEKLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED = "R0033";
	public static final String WEEKLY_MAXIMUM_TRANSACTION_COUNT_REACHED = "R0034";
	public static final String MONTHLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED = "R0035";
	public static final String MONTHLY_MAXIMUM_TRANSACTION_COUNT_REACHED = "R0036";
	public static final String YEARLY_MAXIMUM_TRANSACTION_AMOUNT_REACHED = "R0037";
	public static final String YEARLY_MAXIMUM_TRANSACTION_COUNT_REACHED = "R0038";

	// Maximum Redemptions Exceeded

	public static final String INVALID_CURRENCY_CODE = "R0047";
	public static final String DUPLICATE_RRN_OR_REQUEST = "R0048";
	public static final String INVALID_SOURCE_CARD_STATUS = "R0049";
	public static final String INVALID_TARGET_CARD_STATUS = "R0050";
	public static final String CARD_LOCKED = "R0051";
	public static final String CARD_NOT_LOCKED = "R0052";

	public static final String TRANSACTION_AMOUNT_IS_GREATER_THAN_MAXIMUM_PER_TXN_AMOUNT = "R0054";
	public static final String TRANSACTION_AMOUNT_IS_LESS_THAN_MINIMUM_PER_TXN_AMOUNT = "R0055";
	public static final String TRANSACTION_NOT_SUPPORTED_ON_PARTNER = "R0056";

	public static final String INVALID_CARD_STATE = "R0053";
	public static final String DECLINED_BY_TRANSACTION_FILTER = "R0013";

	/**
	 * RElODABLE FLAG
	 */

	public static final String RELOADABLE_CHECK = "R0187";
	public static final String START_DATE_IS_LESS_THAN_END_DATE = "R0186";

	public static final String API_RESPONSE_SUCCESS = "000";
	public static final String API_RESPONSE_INVALID_UPC = "UPCNOTEXIST002";
	public static final String SUCCESS_EXT_RESPONSE = "00";

	// Currency Conversion
	public static final String INVALID_TXN_CURRENCY_CODE = "R0201";
	public static final String ACTION_NOT_SUPPORTED = "R0202";
	public static final String PURSE_NOT_LINKED_WITH_PRODUCT = "R0204";
	public static final String INVALID_PURSE = "R0205";
	public static final String MULTIPURSE_DISABLED = "R0206";
	public static final String PROD_PURSE_VALIDITY = "R0207";

	public static final String ACCOUNT_PURSE_NOT_ACTIVE = "R0208";

	public static final String PRODUCT_MISMATCH = "R0209";
	public static final String CARD_BALANCE_LOCKED = "R0210";

}
