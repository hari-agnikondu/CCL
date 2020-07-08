package com.incomm.cclp.constants;

public class CCLPConstants {
	/**
	 * Constants class should not be instantiated.
	 */
	private CCLPConstants() {
		throw new IllegalStateException("Constants class");
	}

	public static final String ENTER = "ENTER";

	public static final String EXIT = "EXIT";

	public static final String USER_STATUS_APPROVED = "APPROVED";

	public static final String USER_STATUS_REJECTED = "REJECTED";

	public static final String USER_STATUS_NEW = "NEW";
	
	public static final String GROUP_NAME = "GroupName";

	public static final String LOCATION_NAME = "LocationName";
	
	public static final String PARTNER_NAME = "PartnerName";
	public static final String ROLE_NAME = "RoleName";
	public static final String RULE_NAME = "RuleName";
	public static final String USER_NAME = "UserName";
	public static final String ALERTS = "Alerts";
	public static final String PRODUCT = "Product";
	public static final String PRODUCT_ID = "productId";
	public static final String PARTNER_ID = "partnerId";
	public static final String PRODUCT_ID_NEGATIVE = "Product Id is negative {}";
	
	public static final String ACTION = "Action";
	public static final String CARDRANGE_RETRIEVE = "CARDRANGE_RETRIEVE_";
	public static final String COUNTRY_CODE_RETRIVE = "COUNTRY_CODE_RETRIVE_";
	public static final String FUL_RETRIVE = "FUL_RETRIVE_";
	public static final String GROUP_RETRIVE = "GROUP_RETRIVE_";
	public static final String ISSUER_NAME = "IssuerName";
	public static final String MERCHANT_NAME = "MerchantName";
	public static final String PRODUCT_U = "PRODUCT_";
	public static final String PAC_ERR = "PAC_ERR_";
	public static final String PRODUCT_ACTION_UPDATE = "PRODUCT_ACTION_UPDATE_";
	public static final String PRS_RETRIVE = "PRS_RETRIVE_";
	public static final String SLASH = "\\\\_";
	public static final String SLASH_S = "\\s*,\\s*";
	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String ALERT_EMAIL_BODY = "alertEmailBody_";
	public static final String ALERT_EMAIL_SUB = "alertEmailSub_";
	public static final String ALERT_LANGUAGE = "alertLanguage";
	public static final String ALERT_MODE_EMAIL = "alertMode_EMAIL_";
	public static final String ALERT_MODE_SMS = "alertMode_SMS_";
	public static final String ALERT_SMS = "alertSMS_";
	public static final String CARD_NUMBER = "cardNumber";
	public static final String CARD_RANGE_ID = "cardRangeId";
	public static final String FULFILLMENT_ID = "fulfillmentID";
	public static final String GROUP_ACCESS_ID = "groupAccessId";
	public static final String GROUP_ACCESS_NAME = "groupAccessName";
	public static final String ISACTIVE = "isActive";
	public static final String ISSUER_ID = "issuerId";
	public static final String MERCHANT_ID = "merchantId";
	public static final String MERCHANTNAME = "merchantName";
	public static final String PACKAGE_ID = "packageId";
	public static final String PROFILE_ID = "profileId";
	public static final String PROXY_NUMBER = "proxyNumber";
	public static final String RULESET_ID = "ruleSetId";
	public static final String USER_LOGIN_ID = "userLoginId";
	public static final String USERNAME = "userName";
	
	
	// Added By Hari for Update Program Parameters
	public static final String MAX_AMT_PER_TX = "maxAmtPerTx";
	public static final String MIN_AMT_PER_TX = "minAmtPerTx";
	
	public static final String DAILY_MAX_AMT = "dailyMaxAmt";
	public static final String WEEKLY_MAX_AMT = "weeklyMaxAmt";
	public static final String MONTHLY_MAX_AMT = "monthlyMaxAmt";
	public static final String YEARLY_MAX_AMT = "yearlyMaxAmt";
		
	public static final String DAILY_MAX_COUNT = "dailyMaxCount";
	public static final String WEEKLY_MAX_COUNT = "weeklyMaxCount";
	public static final String MONTHLY_MAX_COUNT = "monthlyMaxCount";
	public static final String YEARLY_MAX_COUNT = "yearlyMaxCount";
		
	public static final String FREE_COUNT = "freeCount";
	public static final String MAX_COUNT = "maxCount";
	public static final String FEE_DESC = "feeDesc";
	public static final String FEE_AMT = "feeAmt";
	public static final String FEE_CONDITION = "feeCondition";
	public static final String FEE_PERCENT = "feePercent";
	public static final String MIN_FEE_AMT = "minFeeAmt";
	public static final String FREE_COUNT_FREQ = "freeCountFreq";
	public static final String MAX_COUNT_FREQ = "maxCountFreq";
	public static final String MONTHLY_FEECAP_AVAIL = "monthlyFeeCapAvail";
	public static final String FIRST_MONTH_FEE_ASS_DAYS = "firstMonthFeeAssessedDays";
	public static final String CAP_FEE_AMT = "capFeeAmt";
	public static final String ASSESSMENT_DATE = "assessmentDate";
	public static final String FEECAP_AMT = "feeCapAmt";
	public static final String TIME_PERIOD = "timePeriod";

	public static final String CLAWBACK = "clawback";

	public static final String CLAWBACK_COUNT = "clawbackCount";

	public static final String CLAWBACK_OPTION = "clawbackOption";
	
	public static final String CLAW_BACK_MAX_AMT= "clawbackMaxAmt";
	
	public static final String FEE_CAP_AVAIL="FeeCapAvail";
	
	public static final String CSS_AUTH_TYPES_RETRIVE = "CSS_AUTH_TYPES_RETRIVE_";
	
	public static final String ATTRIBUTES_GROUP_ENV_PARAMETERS = "Environment";
	
	public static final String ENV_TYPES_RETRIVE = "ENV_TYPES_RETRIVE_";
	
	public static final String PARTY_TYPE="PARTY_TYPE";
	
	public static final String REDEMPTION_DELAY="REDEMPTION_CONFIG";
	
	public static final String ISSUERID="ISSUER_ID";
	
	public static final String SUPPORT_PURSES="support_purses";
	
	public static final String PRODUCT_CURRENCY="product_currency";
	
	public static final String PURSE_ID="purseId";
	
	public static final String DEFAULT_Y ="Y";
	
	public static final String PRODUCT_TAB_GENERAL = "General";
	public static final String PRODUCT_TAB_PIN = "PIN";
	public static final String PRODUCT_TAB_CVV = "CVV";
	public static final String PRODUCT_TAB_LIMITS= "Limits";
	public static final String PRODUCT_TAB_TRANSACTION_FEES = "Transaction Fees";
	public static final String PRODUCT_TAB_CARD_STATUS = "Card Status";
	public static final String PRODUCT_TAB_MONTHLY_FEE_CAP = "Monthly Fee Cap";
	public static final String PRODUCT_TAB_MAINTENANCE_FEES = "Maintenance Fees";
	public static final String PRODUCT_ATTRIBUTUES = "productattributes";
	public static final String PRODUCT_PURSE_ATTRIBUTUES = "productpurseattributes";
	public static final String FROM_UI = "ui";
	public static final String FROM_CACHE = "cache";
	public static final String PRODUCT_TAB_PURSE = "Purse";
	
	public static final String GENERAL_PARTIAL_AUTH_IND = "partialAuthIndicator";
	public static final String REDEEM_SPLIT_TENDER = "redeemSplitTender";
	public static final String REDEEM_LOCK_SPLIT_TENDER = "redeemLockSplitTender";
	public static final String SPLIT_TENDER = "splitTender";
	public static final String PURSE_REDEEM_SPLIT_TENDER = "purseRedeemSplitTender";
	public static final String PURSE_REDEEM_LOCK_SPLIT_TENDER = "purseRedeemLockSplitTender";
	public static final String FALSE = "false";

	public static final String ACCESS_STATUS_ACTIVE = "ACTIVE";
	public static final String USD_ONE_BUMP_PUMP_TXN = "usdOneBumpPumpTxn";
	
	public static final String PARTNER_CACHE = "partnerCache";
	public static final String PRODUCT_CACHE = "productCache";
	public static final String PRODUCTPURSE_CACHE = "productPurseCache";
	public static final Long THREAD_SLEEP = 1000L; 
	
}
