package com.incomm.cclpvms.constants;

public class CCLPConstants {
	/**
	 * Constants class should not be instantiated.
	 */
	private CCLPConstants() 
	{
		throw new IllegalStateException("Constants class");
	}
	public static final String ENTER="ENTER";
	
	public static final String EXIT="EXIT";
	
	public static final String INSUER="insUser";
	
	public static final String GROUP_CONFIG = "groupConfig";
	
	public static final String ADD_GROUP = "addGroup";
	
	public static final String STATUS = "status";
	
	public static final String STATUS_FLAG = "statusFlag";
	
	public static final String SUCCESS = "success";

	public static final String FAILURE = "failure";
	
	public static final String STATUS_MESSAGE = "statusMessage";
	
	public static final String GROUP_SEARCH = "groupSearch";
	
	public static final String GROUP_NAME = "groupName";
	
	public static final String CARD_RANGE_CONFIG = "CardRangeConfig";
	
	public static final String CARD_RANGE_SEARCH = "cardRangeSearch";
	
	public static final String ADD_CARD_RANGE = "addCardRange";
	
	public static final String ISSUER_LIST = "issuerList";
	
	public static final String EDIT_CARD_RANGE = "editCardRange";
	
	public static final String VIEW_CARD_RANGE = "viewCardRange";

	public static final String ISSUERS_NAME = "issuersName";
	
	public static final String GROUP_ACCESS_ID_CONFIG = "groupAccessIdConfig";
	
	public static final String GROUP_ACCESS_ID_SEARCH = "groupAccessIdSearch";
	
	public static final String ADD_GROUP_ACCESS_ID = "addGroupAccessId";
	
	public static final String GROUP_ACCESS_MAP = "groupAccessMap";
	
	public static final String PARTNER_LIST = "partnerList";
	
	public static final String SELECTED_PARTNER_LIST = "selectedPartnerList";
	
	public static final String ADD_ON_LOAD_FLAG = "addonloadFlag";
	
	public static final String EDIT_GROUP_ACCESS_ID = "editGroupAccessId";
	
	public static final String EDIT_ASSIGN_GROUP_ACCESS = "editAssignGroupAccess";
	
	public static final String SRV_URL = "srvUrl";
	
	public static final String HTTP = "http://";
	
	public static final String CONFIG_VIEW = "forward:/config/groupAccessId/groupAccessIdConfig";
	
	public static final String ASSIGN_GROUP_ACCESS_ID = "assignGroupAccessId";
	
	public static final String TXN_FINANCIAL_FLAG_MAP = "txnFinancialFlagMap";
	
	public static final String DELIVERY_CHANNEL_LIST = "deliverChannelList";
	
	public static final String CUSTOMER_PROFLE_FORM = "customerProfileForm";
	
	public static final String CUSTOMER_PROFLE_CONFIG = "customerProfileConfig";
	
	public static final String ACCT_NUM_HIDDEN = "accountNumber_hidden";
	
	public static final String CUST_PFL_ID = "customerProfileId";
	
	//Stock Related Constants
	public static final String MERCHANT_MAP = "merchantMap";
	public static final String STOCK_CONFIG = "stockConfig";
	public static final String LOCATION_MAP = "locationMap";
	public static final String STOCK_FORM = "stockForm";
	public static final String EDIT_STOCK = "editStock";
	public static final String PRODUCT_MAP = "productMap";
	public static final String ORDER_FORM = "orderForm";
	public static final String ORDER_LIST = "orderList";
	public static final String ORDER_AND_CCF_PROCESS = "OrderAndCCFProcess";
	public static final String PRODUCT_NAME = "productName";
	public static final String USER_ID = "userId";
	public static final String CN_FILE_LIST = "cnFileList";
	public static final String CN_FILE_UPLOAD = "CNFileUpload";
	public static final String FROM_DATE = "fromDate";
	public static final String TO_DATE = "toDate";
	public static final String SHOW_GRID = "showGrid";
	
	public static final String BLOCK_LIST = "blockList";
	public static final String BLOCK_LIST_FORM = "blockListForm";
	public static final String BLOCKLIST_DELIVERY_CHANNEL_LIST = "deliveryChannelList";
	public static final String DELIVERY_CHANNELS = "deliveryChannels";
	
	public static final String SCHEDULER = "scheduler";
	public static final String SWICHOVER_SCHEDULER_CONFIG = "switchOverSchedulerConfig";
	
	public static final String RULE_FORM = "ruleForm";
	
	public static final String PARTNER_FORM = "partnerForm";
	public static final String PARTNER_CONFIG = "partnerConfig";
	public static final String SHOW_EDIT_PARTNER = "showEditPartner";
	public static final String SHOW_VIEW_PARTNER = "showViewPartner";
	public static final String MIN_AMT_PER_TX = "minAmtPerTx";
	public static final String MAX_AMT_PER_TX = "maxAmtPerTx";
	public static final String DAILY_MAX_AMT = "dailyMaxAmt";
	public static final String DAILY_MAX_COUNT = "dailyMaxCount";
	public static final String WEEKLY_MAX_AMT = "weeklyMaxAmt";
	public static final String WEEKLY_MAX_COUNT = "weeklyMaxCount";
	public static final String MONTHLY_MAX_AMT = "monthlyMaxAmt";
	public static final String MONTHLY_MAX_COUNT = "monthlyMaxCount";
	public static final String YEARLY_MAX_AMT = "yearlyMaxAmt";
	public static final String YEARLY_MAX_COUNT = "yearlyMaxCount";
	
	public static final String RULE_SET_CONFIG = "ruleSetConfig";
	public static final String MIN_FEE_AMT = "minFeeAmt";
	public static final String FEE_PERCENT = "feePercent";
	public static final String FEE_CONDITION = "feeCondition";
	
	//Fulfillment constants
	public static final String FULFILLMENT_CONFIG = "fulFillmentConfig";
	public static final String FULFILLMENT_FORM = "fulFillmentForm";
	public static final String EDIT_FULFILLMENT = "editFulfillment";
	public static final String DELETE_BOX = "deleteBox";
	public static final String EDIT_FULFILLMENT_ID = "editFulfillmentID";
	public static final String ADD_FULLFILMENT = "addFullfilment";
	public static final String ISAUTOMATIC_SHIPPED_LIST = "isAutomaticShippedList";
	public static final String FULFILLMENT_SEQ_ID = "fulFillmentSEQID";
	
	// Customer_Profile Constants
	public static final String ADD_CUSTOMERPROFILE = "addCustomerProfile";
	public static final String EDIT_CUSTOMERPROFILE = "editCustomerProfile";
	public static final String CUSTOMERPROFILE_MONTHLYFEE_CAP = "customerProfileMonthlyFeeCap";
	public static final String CUSTOMERPROFILE_LIMIT = "customerProfileLimit";
	public static final String FEESSUPPORTED_CARD = "feesSupportedCard";
	public static final String LIMITSUPPORTED_CARD = "limitSupportedCard";
	public static final String CUSTOMERPROFILE_TXN_FEE = "customerProfileTxnFee";
	public static final String CUSTOMERPROFILE_MAINTENANCE_FEE = "customerProfileMaintenanceFee";
	public static final String MAINTAINANCEFEE_SUPPORTED_CARD = "maintainanceFeeSupportedCard";
	
	public static final String ALERT_CARD_STATUS="alertCardStatus";
	public static final String SEL_CARD_STATUS="selCardStatus";
	
	
	public static final String APPROVE_BOX="approveBox";
	public static final String REJECT_BOX="rejectBox";
	public static final String FAIL_STATUS="failstatus";
	public static final String SUCCESS_STATUS="successstatus";
	public static final String CONFIRM_BOX="confirmBox";
	public static final String ERR_MSG="Failed to process. Please try again later.";

	public static final String VIEW_FULFILLMENT = "viewFulfillment";

	public static final String VIEW_STOCK = "viewStock";
	
	public static final String ERROR = "error";
	
	
	public static final String FEE_DESC = "feeDesc";
	public static final String MONTHLY_FEE_CAP = "Monthly Fee Cap";
	public static final String MAINTENANCE_FEES = "Maintenance Fees";
	public static final String ALERTS = "Alerts";
	public static final String PDF_NAME = "Name";
	public static final String PDF_VALUE = "Value";
	public static final String PDF_SUBJECT = "Subject";
	public static final String PDF_BODY = "Body";
	public static final String ENABLE = "Enable";
	public static final String PRODUCT ="Product";
	public static final String PURSE ="Purse";
	public static final String CURRENCY_CODES = "currencyData";
	public static final String PURSES = "purseData";
	public static final String PROGRAM_PARTNER_PURSE_LIST = "programPartnerPursesList";
	
	public static final String PURSE_LIST = "purseList";
	public static final String PRODUCT_PURSE = "productPurse";
	public static final String ACTIVATION_ID = "activationId";
	public static final String OTHER_TXN_ID = "otherTxnId";
	
	public static final String SLASH = "/";
	public static final long DROPDOWN_DEFAULTVALUE = -1;
	public static final String TRUE = "true";
	public static final String SHOW_COPY_FROM = "showCopyFrom";
	public static final String SELECTED_PURSE_ID = "selectedpurseId";

	public static final String TIME_ZONE = "timeZone";
	public static final String PURSE_UI_VALIDITY_PERIOD = "purseUIValidityPeriod";
	public static final String PURSE_VALIDITY_TIME = "purseValidityTime";
	public static final String PURSE_VALIDITY_TIMEZONE = "purseValidityTimeZone";
	public static final String TIMEZONE_TIME_FORMAT ="MM/dd/yyyy HH:mm:ss";
	
	public static final String ACCESS_STATUS_BOX="accessStatusBox";
		public static final String ATTRIBUTE_AUTO_TOPUP_ENABLE ="autoTopupEnable";
	public static final String ATTRIBUTE_AUTO_TOPUP_FREQUENCY ="autoTopupFrequency";
	public static final String ATTRIBUTE_AUTO_TOPUP_CRON ="autoTopupCron";
	public static final String ATTRIBUTE_AUTO_TOPUP_NXT_RUN ="autoTopupNextRun";
	
	public static final String ATTRIBUTE_AUTO_RELOAD_ENABLE ="autoReloadEnable";
	public static final String ATTRIBUTE_AUTO_RELOAD_FREQUENCY ="autoReloadFrequency";
	public static final String ATTRIBUTE_AUTO_RELOAD_CRON ="autoReloadCron";
	public static final String ATTRIBUTE_AUTO_RELOAD_NXT_RUN ="autoReloadNextRun";
	
	public static final String TOPUP_FREQ_DAY ="topupFreqDay";
	public static final String TOPUP_FREQ_DAYOFMONTH_MULTI ="topupFreqDayOfMonthMulti";
	public static final String TOPUP_FREQ_DOM ="topupFreqDOM";
	public static final String TOPUP_FREQ_MONTH ="topupFreqMonth";
	
	public static final String RELOAD_FREQ_DAY ="reloadFreqDay";
	public static final String RELOAD_FREQ_DAYOFMONTH_MULTI ="reloadFreqDayOfMonthMulti";
	public static final String RELOAD_FREQ_DOM ="reloadFreqDOM";
	public static final String RELOAD_FREQ_MONTH ="reloadFreqMonth";
}
