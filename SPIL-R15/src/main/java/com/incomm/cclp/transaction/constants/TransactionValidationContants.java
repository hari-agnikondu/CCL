package com.incomm.cclp.transaction.constants;

public class TransactionValidationContants {

	private TransactionValidationContants() {
		throw new IllegalStateException("Constants class");
	}

	public static final String CARD_STATUS_CHECK = "CARD_STATUS_CHECK";

	public static final String EXPIRY_DATE_CHECK = "EXPIRY_DATE_CHECK";

	public static final String PIN_VERIFICATION = "PIN_VERIFICATION";

	public static final String CVV2_VERIFICATION = "CVV2_VERIFICATION";

	public static final String TRACK1_VERIFICATION = "TRACK1_VERIFICATION";

	public static final String TRACK2_VERIFICATION = "TRACK2_VERIFICATION";

	public static final String PASSIVE_CARD_UPDATE = "PASSIVE_CARD_UPDATE";

	public static final String CARD_NUMBER_LENGTH = "CARD_NUMBER_LENGTH";

	public static final String LIMIT_CHECK = "LIMIT_CHECK";

	public static final String TRANSACTION_DATE_CHECK = "TRANSACTION_DATE_CHECK";

	public static final String CURRENCY_CHECK = "CURRENCY_CHECK";

	public static final String ALREADY_ACTIVATED = "ALREADY_ACTIVATED";

	public static final String DEACTIVATED = "DEACTIVATED";

	public static final String CASHED_OUT = "CASHED_OUT";

	public static final String UNLOCKED = "UNLOCKED";

	public static final String REDEEMED = "REDEEMED";

	public static final String SUBSEQUENT_ACTIVATION_AS_RELOAD = "SUBSEQUENT_ACTIVATION_AS_RELOAD";

	public static final String ALREADY_REVERSED = "ALREADY_REVERSED";

	public static final String DENOM_CHECK = "DENOM_CHECK";

	public static final String INSTRUMENT_CHECK = "INSTRUMENTCHECK";

	public static final String UPC_CHECK = "UPC_CHECK";

	public static final String SP_NUMBER_TYPE_CHECK = "SP_NUMBER_TYPE_CHECK";

	public static final String RELOADABLE_FLAG_CHECK = "RELOADABLE_FLAG_CHECK";

	/** R07 changes added by venkateshgaddam starts **/
	public static final String REDEMPTION_DELAY_CHECK = "REDEMPTION_DELAY_CHECK";
	public static final String FIRST_PARTY_THIRD_PARTY_CHECK = "FIRST_PARTY_THIRD_PARTY_CHECK";
	public static final String FEE_CHECK = "FEE_CHECK";
	public static final String PARTIAL_AUTH_CHECK = "PARTIAL_AUTH_CHECK";
	/** R07 changes added by venkateshgaddam ends **/

	public static final String PURSE_VALIDITY_CHECK = "PURSE_VALIDITY_CHECK";

}
