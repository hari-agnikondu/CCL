package com.incomm.cclp.constants;

public class CardStatusConstants {

	/**
	 * Constants class should not be instantiated.
	 */
	private CardStatusConstants() {
		throw new IllegalStateException("CardStatusConstants class");
	}

	public static final String INACTIVE = "0";
	public static final String ACTIVE = "1";
	public static final String LOST_STOLEN = "2";
	public static final String DAMAGE = "3";
	public static final String RESTRICTED = "4";
	public static final String MONITORED = "5";
	public static final String ON_HOLD = "6";
	public static final String EXPIRED_CARD = "7";
	public static final String PASSIVE = "8";
	public static final String CLOSED = "9";
	public static final String HOT_CARDED = "11";
	public static final String SUSPENDED_CREDIT = "12";
	public static final String ACTIVE_UNREGISTERED = "13";
	public static final String SUSPENDED_DEBIT = "14";
	public static final String FRAUD_HOLD = "15";
	public static final String CONSUMED = "17";
	public static final String BADCREDIT = "18";
	public static final String HOTLIST = "19";
	public static final String EXPIRED_PRODUCT = "20";
	public static final String PRINTER_PENDING = "98";
	public static final String PRINTER_SENT = "99";
	public static final String DIGITAL_IN_PROCESS = "97";
}
