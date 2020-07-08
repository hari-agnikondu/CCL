package com.incomm.cclp.constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RuleConstants implements Serializable {

	private static final long serialVersionUID = 643219707386196122L;

	public static final String EQUALS_TO = "equal";

	public static final String LESS_THAN = "less";

	public static final String LESS_OR_EQUALS_TO = "less_or_equal";

	public static final String GREATER_THAN = "greater";

	public static final String GREATER_OR_EQUALS_TO = "greater_or_equal";

	public static final String NOT_EQUALS_TO = "not_equal";

	public static final String CONTAINS = "contains";

	public static final String NOT_CONTAINS = "not_contains";

	public static final String START_WITH = "begins_with";

	public static final String END_WITH = "ends_with";

	public static final String PATTERN_MATCHES = "patternmatches";

	public static final String PATTERN_MATCH_FIND = "patternmatchfind";

	public static final String DATE_BEFORE = "datebefore";

	public static final String DATE_AFTER = "dateafter";

	public static final String DATE_EQUALS = "dateequals";

	public static final String IS_NOT_NULL = "is_not_null";
	public static final String IS_NULL = "is_null";
	public static final String BETWEEN = "between";
	public static final String NOT_BETWEEN = "not_between";
	public static final String DI_RAN = "di_ran";

	protected static final String[] OPRERATOR_LIST = { EQUALS_TO, LESS_THAN, LESS_OR_EQUALS_TO, GREATER_THAN, GREATER_OR_EQUALS_TO,
			NOT_EQUALS_TO, CONTAINS, NOT_CONTAINS, START_WITH, END_WITH, PATTERN_MATCHES, PATTERN_MATCH_FIND, DATE_BEFORE, DATE_AFTER,
			DATE_EQUALS, IS_NOT_NULL, IS_NULL, BETWEEN, NOT_BETWEEN, DI_RAN, "amex_ran", "&&", "||", "cardConsumeFlag", "kycCheckFlag",
			"cvv2Check", "avCheck", "expiryDateVal", "Count Equal", "Count Greater", "Count Greater Equal", "Count  Lesser",
			"Count Lesser Equal", "Count Not Equal", "G_Merchant" };

	public static final String[] EXCLUDE_LOGICAL_OPR = { EQUALS_TO, LESS_THAN, LESS_OR_EQUALS_TO, GREATER_THAN, GREATER_OR_EQUALS_TO,
			NOT_EQUALS_TO, CONTAINS, NOT_CONTAINS, START_WITH, END_WITH, PATTERN_MATCHES, PATTERN_MATCH_FIND, DATE_BEFORE, DATE_AFTER,
			DATE_EQUALS, IS_NOT_NULL, IS_NULL, BETWEEN, NOT_BETWEEN, DI_RAN, "amex_ran", "countEqual", "countGreater", "countGreaterEqual",
			"countLesser", "countLesserEqual", "countNotEqual", "G_Merchant" };

	public static final String DECLINE_IF_TRUE = "DECLINE_IF_TRUE";

	public static final String DECLINE_IF_FALSE = "DECLINE_IF_FALSE";

	public static final String CONDITIONAL_IF_FALSE = "CONDITIONAL_IF_FALSE";

	public static final String CONDITIONAL_IF_TRUE = "CONDITIONAL_IF_TRUE";

	// For future use
	public static final String PROCEED_IF_TRUE = "proceediftrue";
	// For future use
	public static final String PROCEED_IF_FALSE = "proceediffalse";

	protected static final List<String> ADDRVERIFY_CHECK_FLAG = new ArrayList<>();

	static {
		ADDRVERIFY_CHECK_FLAG.add("ADDR_VERIFI_BOTH");
		ADDRVERIFY_CHECK_FLAG.add("ADDR_VERIFI_ADDR");
		ADDRVERIFY_CHECK_FLAG.add("ADDR_VERIFI_ZIP");
	}
	public static final String LAST_ACTIVE_PERIOD = "LAST_ACTIVE_PERIOD";

	public static final String ENABLED_STATUS = "1";
	public static final String DISABLED_STATUS = "0";

	public static final String WALLET_IDENTIFIER_KEY = "WALLET_ID";

	public static final String DEVICE_ID_KEY = "DEVICE_ID";

	public static final String CHARGE_BACK_KEY = "CHARGE_BACK_COUNT";

	public static final String MERCHANT_ID_NAME_KEY = "MERCHANT_ID_NAME";

	public static final String RULE_PARAM_TYPE_05 = "5";

	public static final String DEVICE_LOCATION_COUNTRY = "DEVICE_LOCATION_COUNTRY";
	public static final String DEVICE_LOCATION_DISTANCE = "DEVICE_LOCATION_DISTANCE";
	public static final String VISA_INTERCHANGE_ID = "VISA_INCHGE_ID";
	public static final String MASTER_INTERCHANGE_ID = "MASTER_INCHGE_ID";
	public static final String AMEX_INTERCHANGE_ID = "AMEX_INCHGE_ID";
	public static final String RULE_LOC_DTLS_SEPARATOR = "/";

	public static final String BLACKLIST_PAN = "BLACKLIST_PAN";
	public static final String DECLINED_BY_TRANSACTION_FILTER = "Declined By Transaction Filter";

}
