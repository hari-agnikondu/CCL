package com.incomm.cclp.constants;

public class MsgTypeConstants {

	/**
	 * Constants class should not be instantiated.
	 */
	private MsgTypeConstants() {
		throw new IllegalStateException("MsgTypeConstants class");
	}

	public static final String MSG_TYPE_NORMAL = "0200";
	public static final String MSG_TYPE_REVERSAL = "0400";

}
