package com.incomm.cclp.constants;

public final class CurrConvConstants {

	private CurrConvConstants() {
	}

	public static final String CURR_CODE_EXPR = "^[a-zA-Z]{3}$";
	public static final String CURR_EXPR = "^(?=.*[1-9])[0-9]{1,6}|(?=.*[1-9])([0-9]{1,6}.[0-9]{1,4})$";
	public static final String SUCCESS_RESPONSE_CODE = "0";
	public static final String FAILURE_RESPONSE_CODE = "1";
	public static final String SUCCESS_RESPONSE_MSG = "success";
	public static final String MISSING_CHANNEL_RESPONSE_MSG = "Header Validation Failed:Missing x-incfs-channel";
	public static final String MISSING_USER_RESPONSE_MSG = "Header Validation Failed:Missing x-incfs-username";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String INVALID_MDM_ID = "MdmId Validation Failed:Invalid MdmId";
	public static final String MISSING_INVALID_ISSUE_CURR = "Missing or Invalid Issuing Currency";
	public static final String MISSING_INVALID_TRAN_CURR = "Missing or Invalid Transaction Currency";
	public static final String INVALID_EFF_DATE = "Invalid Effective DateTime";
	public static final String MISSING_INVALID_EXCHG_RATE = "Missing or Invalid Exchange Rate";

}
