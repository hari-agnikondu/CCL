package com.incomm.cclp.transaction.constants;

public class TransactionConstant {

	private TransactionConstant() {
		throw new IllegalStateException("TransactionConstants class");
	}

	public static final String ACT = "act";
	public static final String PREAUTH_ACT = "preauthact";
	public static final String DEACT = "deact";
	public static final String BAL_TRANSFER = "baltransfer";
	public static final String BALINQ = "balinq";
	public static final String REDEEM = "redeem";
	public static final String STORE_CREDIT = "storecredit";
	public static final String CREDIT = "credit";
	public static final String REDEMPTION_AUTH_UNLOCK = "redemptionauthlock";
	public static final String REDEMPTION_COMP_UNLOCK = "redemptioncompunlock";
	public static final String CASHOUT = "cashout";
	public static final String CARD_HISTORY = "cardhistory";
	public static final String RECHARGE = "recharge";
	public static final String VALINS = "valins";
	public static final String PREVALINS = "prevalins";
	public static final String SALE_ACTIVE_CODE = "saleactivecode";

	public static final String MSG_TYPE_POSITIVE = "0200";
	public static final String MSG_TYPE_REVERSAL = "0400";

	public static final String INACTIVE_CARD = "0";
	public static final String ACTIVE_CARD = "1";
	public static final String HOT_CARD = "11";
	public static final String SUSPENDED_CREDIT = "12";
	public static final String ACTIVE_UNREGISTERED = "13";
	public static final String SUSPENDED_DEBIT = "14";
	public static final String FRAUD_HOLD = "15";
	public static final String CONSUMED = "17";
	public static final String BAD_CREDIT = "18";
	public static final String HOTLIST = "19";
	public static final String LOST_STOLEN = "2";
	public static final String DAMAGE = "3";
	public static final String RESTRICTED = "4";
	public static final String MONITORED = "5";
	public static final String ON_HOLD = "6";
	public static final String EXPIRED_CARD = "7";
	public static final String PASSIVE_CARD = "8";
	public static final String PRINTER_PENDING = "98";
	public static final String PRINTER_SENT = "99";
	public static final String CLOSED = "9";

	public static final String SYS_ERROR_MSG = "System Error";

	public static final String TRUE = "true";

	public static final String TXN_ACTIVATION_CODE = "002";
	public static final String TXN_UPDATE_INACTIVE_CODE = "080";
	public static final String CREDIT_CARD = "C";
	public static final String DEBIT_CARD = "D";
	public static final String NONFINANCIAL_CARD = "NA";

	public static final String ACTIVATION_REVERSAL_TXN_DESC = "Activation Reversal";
	public static final String DEACTIVATION_REVERSAL_TXN_DESC = "Deactivation Reversal";
}
