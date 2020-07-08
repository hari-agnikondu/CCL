/**
 * 
 */
package com.incomm.cclp.constants;

/**
 * ResponseMessages class defines all the response message keys for the 
 * response messages returned by the Configuration Service.
 * The keys are mapped to key-value pairs in messages.properties
 * 
 * @author abutani
 *
 */
public class ResponseMessages {

	/**
	 * Constants class should not be instantiated.
	 */
	private ResponseMessages() 
	{
		throw new IllegalStateException("Constants class");
	}


	public static final String MESSAGE_DELIMITER = "-";


	public static final String GENERIC_ERR_MESSAGE = "999";
	public static final String VALIDATION_HEADER_MSG = "003";



	public static final String SUCCESS = "000";
	public static final String FAILURE = "999";
	public static final String ALREADY_EXISTS = "001";
	public static final String DOESNOT_EXISTS = "002";
	public static final String ALREADY_APPR_REJ = "003";
	public static final String CONFIGSERVICE_EXCEPTION = "004";

	public static final String ISSUER = "ISSUER";
	public static final String CARDRANGE_EXISTS = "CARD_RANGE_001";




	public static final String ERR_ISSUER_NULL = "ISS005";
	public static final String ERR_ISSUER_NAME = "ISS006";
	public static final String ERR_ISSUER_ID = "ISS007";
	public static final String ERR_ISSUER_INS_USER = "ISS008";
	public static final String ERR_ISSUER_UPD_USER = "ISS009";


	/* PARTNER */

	public static final String ERR_PARTNER_NULL = "PARTNER005";
	public static final String ERR_PARTNER_NAME = "PARTNER006";
	public static final String ERR_PARTNER_ID = "PARTNER007";
	public static final String ERR_PARTNER_INS_USER = "PARTNER008";
	public static final String ERR_PARTNER_UPD_USER = "PARTNER009";
	public static final String ERR_PARTNER_EXISTS = "PARTNER010";
	public static final String ERR_MDMID_EXISTS = "PARTNER015";	
	public static final String ALL_SUCCESS_PARTNER_RETRIEVE = "PARTNER011";	
	public static final String FAIL_PARTNER_DELETE = "PARTNER012";
	public static final String FAILED_PARTNER_RETRIEVE = "PARTNER013";
	public static final String FAIL_PARTNER_ATTACHED_TO_PRODUCT_DELETE = "PARTNER014";
	public static final String ERR_CURRENCY_ATTACHED = "PARTNER016";
	public static final String FAIL_PARTNER_PURSE_DELETE = "PARTNER017";
	public static final String FAIL_PARTNER_PROGRAM_DELETE = "PARTNER018";
	public static final String FAIL_PARTNER_CURRENCY_DELETE = "PARTNER019";
	public static final String ERR_PURSE_ATTACHED_TO_PRODUCT = "PARTNER020";

	/* CARD RANGE */
	public static final String CARDRANGE_ADD_000 = "CARDRANGE_ADD_000";
	public static final String CARDRANGE_RETRIEVE_000  = "CARDRANGE_RETRIEVE_000";
	public static final String CARDRANGE_UPDATED_000 = "CARDRANGE_UPDATED_000";
	public static final String CARDRANGE_DELETE_000  = "CARDRANGE_DELETE_000";

	public static final String ERR_CARDRANGE_NULL = "CARDRANGE_019";
	public static final String ERR_CARDRANGE_ID = "CARDRANGE_021";
	public static final String ERR_CARDRANGE_INS_USER = "CARDRANGE_022";
	public static final String ERR_CARDRANGE_UPD_USER = "CARDRANGE_023";
	public static final String ERR_CARDRANGE_EXISTS = "CARDRANGE_001";  //Overlapping scenario
	public static final String ERR_PREFIX_NULL = "CARDRANGE_025";
	public static final String ERR_CARDRANGE_UPD_STATUS = "CARDRANGE_026";  
	public static final String ERR_ISSUER_PREFIX_NULL = "CARDRANGE_027";
	public static final String SUCCESS_CARDRANGE_APPROVED = "CARDRANGE_APPROVE_000";
	public static final String SUCCESS_CARDRANGE_REJECTED = "CARDRANGE_REJECT_000";
	public static final String ERR_CARDRANGE_APPROVED = "CARDRANGE_030";
	public static final String ERR_CARDRANGE_REJECTED = "CARDRANGE_031";
	public static final String ERR_CARDRANGE_START_CARDRANGE = "CARDRANGE_032";
	public static final String ERR_CARDRANGE_END_CARDRANGE = "CARDRANGE_033";
	public static final String ERR_CARDRANGE_INVENTORY = "CARDRANGE_034";
	public static final String ERR_CARDRANGE_CHECKDIGIT = "CARDRANGE_035";
	public static final String CARDRANGE_DOESNOT_EXIT = "CARDRANGE_002";
	public static final String CARDRANGE_ALREADY_APPROVED = "CARDRANGE_APPROVE_003";
	public static final String CARDRANGE_ALREADY_REJECTED = "CARDRANGE_REJECT_003";
	public static final String CHECKER_NOT_AUTHORIZED = "CARDRANGE_AUTHORIZE_036";


	/*PRODUCT */


	public static final String SUCCESS_PRODUCT_CREATE = "PRS001";
	public static final String SUCCESS_PRODUCT_RETRIEVE = "PRS002";
	public static final String SUCCESS_PRODUCT_UPDATE = "PRS003";
	public static final String SUCCESS_PRODUCT_DELETE = "PRS004";
	public static final String PRODUCT_UPDATE_FAIL = "PRODUCT_UPDATE_FAIL";
	public static final String ERR_PRODUCT_NULL = "PRS005";
	public static final String ERR_PRODUCT_NAME = "PRS006";
	public static final String ERR_PRODUCT_ID = "PRS007";
	public static final String ERR_PRODUCT_INS_USER = "PRS008";
	public static final String ERR_PRODUCT_UPD_USER = "PRS009";
	public static final String ERR_PRODUCT_EXISTS = "PRS010";
	public static final String ERR_PRODUCT_SHORT_NAME = "PRS011";
	public static final String ERR_WHILE_PARSING_ATTRIBUTES = "PRS060";
	public static final String ERR_WHILE_UPDATING_GENERAL_ATTRIBUTES = "PRS061";
	public static final String ERR_WHILE_UPDATING_CVV_ATTRIBUTES = "PRS062";
	public static final String ERR_ATTRIBUTES_MAP_ISEMPTY = "PRS063";
	public static final String ERR_WHILE_UPDATING_CARD_STATUS_ATTRIBUTES = "PRS064";
	/*  Pin*/
	public static final String ERR_WHILE_UPDATING_PIN_ATTRIBUTES ="PRS065";
	/*RuleSet */
	public static final String SUCCESS_RULESET_RETRIEVE = "RS002";


	/*Limit Attributes   */
	public static final String ERR_PRODUCT_ID_NEGATIVE = "PRODUCTIDERR002";

	public static final String SUCCESS_PRODUCT_LIMITS_UPDATE = "PRODUCTLIMIT004";
	public static final String SUCCESS_PRODUCT_LIMITS_RETRIEVE = "PRODUCTLIMIT005";

	public static final String FAILED_PRODUCT_LIMITS_UPDATE = "PRODUCTLIMIT006";

	public static final String EMPTY_PRODUCT_LIMITS_RETRIEVE = "PRODUCTLIMIT007";	
	public static final String EMPTY_PARENT_PRODUCTS_RETRIEVE = "PARENTPRODUCT001";
	public static final String SUCCESS_PARENT_PRODUCTS_RETRIEVE = "PARENTPRODUCT000";

	public static final String EMPTY_TRANSACTION_LIST = "TRANSACTION002";
	public static final String SUCCESS_TRANSACTION_LIST_RETRIEVE = "TRANSACTION000";

	/*TRANSACTION FEES*/
	public static final String SUCCESS_PRODUCT_TXN_FEES_UPDATE = "PRODUCTTXNFEE004";
	public static final String SUCCESS_PRODUCT_TXN_FEES_RETRIEVE = "PRODUCTTXNFEE005";

	public static final String FAILED_PRODUCT_TXN_FEES_UPDATE = "PRODUCTTXNFEE006";

	public static final String EMPTY_PRODUCT_TXN_FEES_RETRIEVE = "PRODUCTTXNFEE007";
	/*MONTHLY FEE CAP*/
	public static final String SUCCESS_PRODUCT_MONTHLYFEECAP_UPDATE = "PRODUCTMONTHLYFEECAP004";
	public static final String SUCCESS_PRODUCT_MONTHLYFEECAP_RETRIEVE = "PRODUCTMONTHLYFEECAP005";

	public static final String FAILED_PRODUCT_MONTHLYFEECAP_UPDATE = "PRODUCTMONTHLYFEECAP006";

	public static final String EMPTY_PRODUCT_MONTHLYFEECAP_RETRIEVE = "PRODUCTMONTHLYFEECAP007";

	/*MAINTENANCE FEE*/
	public static final String SUCCESS_PRODUCT_MAINTENANCE_FEE_UPDATE = "PRODUCTMAINTENANCEFEE004";
	public static final String SUCCESS_PRODUCT_MAINTENANCE_FEE_RETRIEVE = "PRODUCTMAINTENANCEFEE005";

	public static final String FAILED_PRODUCT_MAINTENANCE_FEE_UPDATE = "PRODUCTMAINTENANCEFEE006";

	public static final String EMPTY_PRODUCT_MAINTENANCE_FEE_RETRIEVE = "PRODUCTMAINTENANCEFEE007";


	/*Card status */
	public static final String SUCCESS_CARD_STATUS_RETRIEVE = "CS002";

	/*Alerts*/
	public static final String SUCCESS_PRODUCT_ALERT_UPDATE = "PRODUCTALERT000";
	public static final String FAILED_PRODUCT_ALERT_UPDATE = "PRODUCTALERT001";
	public static final String ERR_PRODUCT_ALERT_ATTRIBUTE_EMPTY = "PRODUCTALERT002";
	public static final String ERR_PRODUCT_ALERT_LANGUAGE_ATTRIBUTE_EMPTY = "PRODUCTALERT003";
	public static final String EMPTY_PRODUCT_ALERT_RETRIEVE = "PRODUCTALERT004";
	public static final String SUCCESS_PRODUCT_ALERT_RETRIEVE = "PRODUCTALERT005";
	public static final String EMPTY_PRODUCT_ATTRIBUTE_RETRIEVE = "PRODUCTALERT006";
	public static final String EMPTY_PURSE_ATTRIBUTE_RETRIEVE = "PRODUCTALERT007";


	/*Purse*/

	public static final String ERR_PURSE_NULL = "PURSE001";
	public static final String ERR_CURRENCYCODE_NULL= "PURSE002";
	public static final String ERR_CURRENCYCODEALPHA_NULL= "PURSE003";
	public static final String ERR_PURSETYPEID_NULL= "PURSE004";
	public static final String ERR_PURSETYPENAME_NULL= "PURSE005";
	public static final String ERR_UPC_NULL= "PURSE006";
	public static final String ERR_INSUSER_NULL= "PURSE007";
	public static final String ERR_INSDATE_NULL= "PURSE008";
	public static final String ERR_UPDUSER_NULL= "PURSE009";
	public static final String ERR_PURSE_NOT_EXIST= "PURSE010";
	public static final String ERR_PRODUCT_PURSE_MAPPING= "PURSE011";
	public static final String ERR_LOYALTY_NULL= "PURSE012";
	public static final String ERR_PURSEID_INVALID= "PURSE013";
	public static final String ERR_PURSETYPE_NULL= "PURSE014";
	public static final String ERR_UPC_SHOULD_BE_UNIQUE= "PURSE015";
	public static final String ERR_LOYALTY_SHOULD_BE_UNIQUE= "PURSE016";
	public static final String ERR_CURRENCY_SHOULD_BE_UNIQUE= "PURSE017";
	public static final String ERR_PURSE_EXISTS = "PURSE018";
	public static final String ERR_EXTPURSEID_EXISTS="PURSE019";
	public static final String ERR_PARTNER_PURSE_MAPPING= "PURSE020";
	
	/* User Management */
	public static final String ERR_USER_NAME = "USER006";


	/*Group Access starts */
	public static final String ERR_GROUP_ACCESS_NULL = "GA001";
	public static final String ERR_GROUP_ACCESS_ID_NULL = "GA002";
	public static final String ERR_PRODUCT_ID_NULL = "GA003";	
	public static final String GROUP_ACCESS_ADD_000 = "GA_ADD_000";
	public static final String GROUP_ACCESS_RETRIEVE_000  = "GA_RETRIEVE_000";
	public static final String GROUP_ACCESS_UPDATED_000 = "GA_UPDATED_000";
	public static final String GROUP_ACCESS_PRODUCT_DELETE_000  = "GAP_DELETE_000";
	public static final String ERR_GROUP_ACCESS_DOESNT_EXIST = "GA004";	
	public static final String ERR_GROUP_ACCESS_NAME_ALREADY_EXIST = "GA005";	
	public static final String ERR_PRODUCT_GROUP_ACCESS_ALREADY_EXIST = "GA006";
	public static final String ERR_DELETE_PRODUCT_GROUP_ACCESS = "GA007";
	public static final String ERR_GROUP_ACCESS_NAME_NULL = "GA008";
	public static final String ERR_PRODUCT_NAME_NULL = "GA009";	
	public static final String ERR_PARTNER_LIST_IS_NULL = "GA010";
	public static final String ERR_PARTNER_INST_USER_IS_NULL = "GA011";
	public static final String ERR_CANNOT_REMOVE_PARTNERS_LINKED_TO_PRODUCT = "GA012";
	public static final String GROUP_ACCESS_PRODUCT_ADD_000 = "GA_ADD_GAP_000";
	public static final String GROUP_ACCESS_PRODUCT_UPDATED_000 = "GA_UPDATED_GAP_000";


	/*Group Access ends */
	/*Global Parameters*/
	public static final String ERR_GLOBAL_PARAMETERS_NULL = "GLOBALPARAMETERS001";
	public static final String LENGTH = "GLOBALPARAMETERS002";
	public static final String ERR_ALLOWABLE_WRONG_LOGINS = "GLOBALPARAMETERS003";

	public static final String ERR_MASKING_CHAR_VALUE = "GLOBALPARAMETERS007";
	public static final String ERR_HSM = "GLOBALPARAMETERS008";
	public static final String ERR_DATE_FORMAT = "GLOBALPARAMETERS009";
	public static final String FAILED_GLOBAL_PARAMETERS_UPDATE = "GLOBALPARAMETERS010";

	public static final String JOBS_DONOT_EXIST= "JOB002";
	public static final String FAILED_USER_MAIL_DETAIL_RETRIEVE= "JOB003";
	public static final String JOB_RETRIEVE_SUCCESS= "JOBS000";
	public static final String SCHEDULERCONFIG_RETRIEVE_SUCCESS= "SCHEDULERCONFIG000";
	public static final String JOBID_NEGATIVE_ORZERO="JOB007";
	public static final String SCHEDULERCONFIG_RETRIEVE_FAIL="SCHEDULERCONFIG999";
	public static final String SCHEDULERCONFIG_UPDATE_FAIL="SCHEDULERCONFIGUPDATE999";
	public static final String SCHEDULERCONFIG_UPDATE_SUCCESS="SCHEDULERCONFIGUPDATE000";
	public static final String SERVERS_DOESNOT_EXIST="SOS002";
	public static final String SERVERS_RETRIEVE_SUCCESS="SWITCHOVERSCHEDULER000";
	public static final String SWITCHOVERSCHEDULER_ALREADY_RUNNING="SWITCHOVERSCHEDULER001";
	public static final String SWITCHOVERSCHEDULER_UPDATE_SUCCESS="SWITCHOVERSCHEDULERUPDATE000";
	public static final String SWITCHOVERSCHEDULER_UPDATE_FAIL ="SWITCHOVERSCHEDULER004";
	/*job Scheduler*/
	public static final String SUCCESS_JOB_SCHEDULER_JOBS_RETRIEVE = "JOBSCHEDULER000";
	public static final String FAILED_JOB_SCHEDULER_JOBS_RETRIEVE = "JOBSCHEDULER001";
	/* PRM Starts */
	public static final String SUCCESS_PRM_UPDATE = "PRM004";
	public static final String FAILED_PRM_UPDATE = "PRM005";

	/* PRM Ends */


	/* ROLE */

	
	public static final String ERR_ROLE_NULL = "ROLE005";

	public static final String ERR_ROLE_INS_USER = "ROLE008";
	public static final String ERR_ROLE_UPD_USER = "ROLE009";

	public static final String FAIL_ROLE_ATTACHED_TO_PRODUCT_DELETE = "ROLE014";

	/*BLOCK LIST*/
	public static final String ERR_CHANNEL_CODE = "BLOCKLIST006";
	public static final String ERR_DELIVERYCHANNEL_RETRIEVE = "BLOCKLIST007";
	public static final String SUCCESS_DELIVERYCHANNEL_RETRIEVE = "BLOCKLIST008";
	public static final String ALL_SUCCESS_BLOCKLIST_RETRIEVE = "BLOCKLIST009";
	public static final String FAILED_BLOCKLIST_RETRIEVE = "BLOCKLIST010"; 
	public static final String ERR_BLOCKLIST_EXISTS = "BLOCKLIST011";
	public static final String FAIL_BLOCKLIST_DELETE = "BLOCKLIST012";

	/**
	 * CLP_USER Constants
	 */
	public static final String ERR_CLP_USER_NULL = "CLPUSER001";
	public static final String ERR_CLP_USER_ID = "CLPUSER002";
	public static final String ERR_CLP_USER_LOGINID = "CLPUSER003";
	public static final String ERR_CLP_USER_USERNAME = "CLPUSER004";
	public static final String ERR_CLP_USER_STATUS = "CLPUSER005";
	public static final String ERR_CLP_USER_EMAIL = "CLPUSER006";
	public static final String ERR_CLP_USER_CONTACTNO = "CLPUSER007";
	public static final String ERR_CLP_USER_INS_USER = "CLPUSER008";
	public static final String ERR_CLP_USER_UPD_USER = "CLPUSER009";
	public static final String FAILED_CLP_USER_RETRIEVE = "CLPUSER010";
	public static final String ERR_CLP_USER_GROUPS = "CLPUSER011";
	public static final String USER_ALREADY_APPROVED = "USER_APPROVE_003";
	public static final String USER_ALREADY_REJECTED = "USER_REJECT_003";
	public static final String SUCCESS_USER_APPROVED = "USER_APPROVE_000";
	public static final String SUCCESS_USER_REJECTED = "USER_REJECT_000";
	public static final String ERR_USER_APPROVED = "USER_APPROVE_030";
	public static final String ERR_USER_REJECTED = "USER_REJECT_031";
	public static final String FAIL_USER_DELETE = "USER012";

	/*Group  start */
	public static final String ERR_GROUP_NAME = "GRP006";
	public static final String ERR_GROUP_RETRIEVE = "GRP013";
	public static final String ERR_GROUP_NULL = "GRP001";
	public static final String ERR_GROUP_ROLE_NAME="GRP007";
	public static final String ERR_GROUP_DOES_NOT_EXIST="GRP_002";
	public static final String ERR_GROUP_NAME_EXIST="GRP_001";
	public static final String GROUP_ALREADY_APPROVED="GRP_APPROVE_003";
	public static final String GROUP_ALREADY_REJECTED="GRP_REJECT_003";
	public static final String ERROR_GROUP_APPROVE="GRP_ERROR_030";
	public static final String ERROR_GROUP_REJECT="GRP_ERROR_031";
	public static final String ERROR_GROUP_COUNT="GRP_ERROR_032";

	public static final String GROUP_APPROVED_000="GROUP_APPROVED_000";

	public static final String GROUP_REJECTED_000="GROUP_REJECTED_000";

	public static final String GROUP_CHANGESTATUS_000="GROUP_CHANGESTATUS_000";

	/*Group  End */

	/* ROLE */
	public static final String ERR_ROLE_NAME = "ROLE006";
	public static final String ERR_ROLE_ID = "ROLE007";
	public static final String ERR_ROLE_EXISTS = "ROLE008";	
	public static final String ALL_SUCCESS_ROLE_RETRIEVE = "ROLE009";
	public static final String FAIL_ROLE_DELETE = "ROLE010";
	public static final String FAILED_ROLE_RETRIEVE = "ROLE011";
	public static final String FAIL_ENTITY_PERMISSION_FETCH="ROLE012";


	/* MERCHANT */

	public static final String ERR_MERCHANT_NULL = "MER005";
	public static final String ERR_MERCHANT_NAME = "MER006";
	public static final String ERR_MERCHANT_ID = "MER007";
	public static final String ERR_MERCHANT_INS_USER = "MER008";
	public static final String FAIL_MERCHANT_DELETE = "MER010";
		public static final String ERR_MERCHANT_UPD_USER = "MER009";		
		public static final String ERR_MERCHANT_PRODUCT_NULL = "MERPRO005";
		public static final String ERR_MERCHANT_PRODUCT_INS_USER = "MERPRO006";
		public static final String ERR_MERCHANT_PRODUCT_UPD_USER = "MERPRO007";
		public static final String MERCHANT_PRODUCT_NOTEXIST = "MERPRO002";
		public static final String FAIL_MERCHANT_PRODUCT_DELETE = "MERPRO012";
		public static final String MERCHANT_ALREADY_EXISTT="MERPRO22";

	/* FULFILLMENT */
	
	public static final String ERR_FULFILLMENT_NULL = "FUL005";
	public static final String ERR_FULFILLMENT_NAME = "FUL006";
	public static final String ERR_FULFILLMENT_ID = "FUL007";
	public static final String ERR_FULFILLMENT_INS_USER = "FUL008";
	public static final String ERR_FULFILLMENT_UPD_USER = "FUL009";
	public static final String ERR_FULFILLMENT_INS_DATE = "FUL010";
	public static final String ERR_FULFILLMENT_LPD_DATE = "FUL011";
	public static final String ERR_FULFILLMENT_CCF = "FUL012";
	public static final String ERR_FULFILLMENT_REPL_CCF = "FUL013";
	public static final String ERR_FULFILLMENT_DOESNOT_EXISTS = "FUL_ERR_002";

	/* CCF */
	
	public static final String SUCCESS_CCF_PARAM_RETRIEVE= "CCF_PARAM_000";

	/* RULE */
	
	public static final String ALL_SUCCESS_RULE_RETRIEVE = "RULE011";	
	public static final String ERR_RULE_NULL = "RULE005";
	public static final String ERR_RULE_NAME = "RULE006";
	public static final String ERR_RULE_ID = "RULE007";
	public static final String ERR_TRANSACTION_TYPE_ID = "RULE008";
	public static final String FAILED_TRANSACTION_TYPE_RETRIEVE = "RULE009";
	public static final String FAILED_RULE_RETRIEVE = "RULE013";
	public static final String SUCCESS_RULE_CONFIG_RET= "RUL_CONFG_000";

	/* RULESET*/
	
	public static final String ERR_RULESET_NAME = "RULESET006";
	public static final String FAILED_RULESET_RETRIEVE = "RULESET013";
	public static final String ALL_SUCCESS_RULESET_RETRIEVE= "RULESET000";	
	
	
	/*Transaction Flex*/
	
	/* PRM Starts */
	public static final String SUCCESS_TRANFLEXDESC_UPDATE = "TXNFLEX004";
	public static final String FAILED_TRANFLEXDESC_UPDATE = "TXNFLEX005";
	/*Location*/
	public static final String ALL_LOCATION_RETRIEVE_000 = "ALL_LOCATION_RETRIEVE_000";
	public static final String LOCATION_RETRIEVE_000 = "LOCATION_RETRIEVE_000";
	public static final String LOCATION_ADD_000 = "LOCATION_ADD_000";
	public static final String ERR_LOCATION_DOES_NOT_EXIST="LOCATION_002";
	public static final String  ERR_LOCATION_ALREADY_EXIST="LOCATION_001";
	public static final String ERROR_LOCATOIN_COUNT="LOCATION_004";
	
	public static final String ERR_LOCATION_NULL="LOCATION_003";
	public static final String ERR_LOCATION_ID="LOCATION_005";
	public static final String ERR_ADDRESSONE_NULL="LOCATION_006";
	public static final String ERR_ADDRESSTWO_NULL="LOCATION_007";
	public static final String ERR_CITY_NULL="LOCATION_008";
	public static final String ERR_ZIP_NULL="LOCATION_009";
	public static final String ERR_STATE_NULL="LOCATION_010";
	public static final String ERR_COUNTRY_NULL="LOCATION_011";
	public static final String ERR_LOCATION_UPD_USER="LOCATION_012";
	public static final String ERR_LOCATION_INS_USER="LOCATION_013";
	public static final String ERR_LOCATION_ALREADY_UNIQUE_EXIST="LOCATION_014";

	/* PRM Ends */
	
	/*
	 * PAN EXPIRY
	 */
	public static final String EMPTY_PRODUCT_PANEXPIRY_RETRIEVE = "PRODUCTPANEXPIRY005";
	public static final String SUCCESS_PRODUCT_PANEXPIRY_RETRIEVE = "PRODUCTPANEXPIRY000";
	public static final String SUCCESS_PRODUCT_PANEXPIRY_UPDATE = "PRODUCTPANEXPIRYUPDATE000";
	public static final String ERR_PRODUCT_PANEXPIRY_UPDATE = "PRODUCTPANEXPIRYUPDATE006";
	public static final String FAILED_PRODUCT_PANEXPIRY_UPDATE = "PRODUCTPANEXPIRYUPDATE007";


	public static final String TIME_NOT_CONFIGURED = "REDEMPTION_DELAY_001";


	public static final String FAIL_REDEMPTION_DELAY_DELETE = "REDEMPTION_DELAY_002";


	public static final String MERCHANT_PRODUCT_REDEMPTION_ALREADY_EXISTT = "REDEMPTION_DELAY_003";
	
	
	/*Customer Profile*/
	public static final String SUCCESS_CUSTOMER_PROFILE_CREATE = "CUSTOMERPROFILE003";
	public static final String SUCCESS_CUSTOMER_PROFILE_RETRIEVE = "CUSTOMERPROFILE004";
	public static final String ERR_CUSTOMER_PROFILE_EXISTS = "CUSTOMERPROFILE005";
	public static final String CUSTOMER_PROFILE_RETRIEVE_SUCCESS = "CUSTOMERPROFILE006";
	public static final String ERR_PROFILE_ID_NEGATIVE = "CUSTOMERPROFILE007";
	public static final String SUCCESS_CARD_LIMITS_UPDATE = "CUSTOMERPROFILE008";
	public static final String FAILED_CARD_LIMITS_UPDATE = "CUSTOMERPROFILE009";
	public static final String EMPTY_CARD_LIMITS_RETRIEVE = "CUSTOMERPROFILE010";
	public static final String SUCCESS_CARD_LIMITS_RETRIEVE = "CUSTOMERPROFILE011";
	public static final String SUCCESS_CARD_TXN_FEES_UPDATE = "CUSTOMERPROFILE012";
	public static final String FAILED_CARD_TXN_FEES_UPDATE = "CUSTOMERPROFILE013";
	public static final String EMPTY_CARD_TXN_FEES_RETRIEVE = "CUSTOMERPROFILE014";
	
	public static final String SUCCESS_CARD_TXN_FEES_RETRIEVE = "CUSTOMERPROFILE015";
	public static final String CUSTOMER_PROFILE_DELETE_FAIL = "CUSTOMERPROFILE016";
	
	public static final String SUCCESS_CARD_MAINTENANCE_FEE_UPDATE = "CUSTOMERPROFILE017";
	public static final String FAILED_CARD_MAINTENANCE_FEE_UPDATE = "CUSTOMERPROFILE018";
	public static final String EMPTY_CARD_MAINTENANCE_FEE_RETRIEVE = "CUSTOMERPROFILE019";
	public static final String SUCCESS_CARD_MAINTENANCE_FEE_RETRIEVE = "CUSTOMERPROFILE020";
	
	public static final String SUCCESS_CARD_MONTHLYFEECAP_UPDATE = "CUSTOMERPROFILE021";
	public static final String FAILED_CARD_MONTHLYFEECAP_UPDATE = "CUSTOMERPROFILE022";
	public static final String EMPTY_CARD_MONTHLYFEECAP_RETRIEVE = "CUSTOMERPROFILE023";
	public static final String SUCCESS_CARD_MONTHLYFEECAP_RETRIEVE = "CUSTOMERPROFILE024";
	public static final String SUCCESS_CARDS_RETRIEVE = "CUSTOMERPROFILE025";
	public static final String FAILED_CARDS_RETRIEVE = "CUSTOMERPROFILE026";
	public static final String SUCCESS_CARD_PROFILE_UPDATE = "CUSTOMERPROFILE027";
	public static final String FAILED_CARD_PROFILE_UPDATE = "CUSTOMERPROFILE028";
	public static final String ERR_NO_ATTRIBUTES_FOUND = "CUSTOMERPROFILE029";
	public static final String ERR_CUSTOMER_PROFILE_DOES_NOT_EXISTS = "CUSTOMERPROFILE030";
	public static final String SUCCESS_PROFILE_DELETE = "CUSTOMERPROFILE031";

	public static final String ERR_CUSTOMER_PROFILE_CREATE = "CUSTOMERPROFILE032";
	public static final String SUCCESS_PDF_METADATA = "CUSTOMERPROFILE016";



	public static final String ERR_PRODUCT_CACHE_UPDTAE = "PRODUCTCACHE19";


	public static final String SUCCESS_PRODUCT_CACHE_UPDTAE = "PRODUCTCACHE20";
	
	
	
		/*PROGRAM ID */
	
	public static final String SUCCESS_PROGRAM_ID_CREATE = "PRGMIDS001";
	public static final String ERR_PROGRAM_ID_EXISTS = "PRGMIDS010";
	public static final String ERR_PROGRAM_ID = "PRGMIDS007";
	public static final String PROGRAM_ID_UPDATE_FAIL = "PROGRAM_ID_UPDATE_FAIL";
	public static final String PROGRAM_ID_INSERT_FAIL = "PROGRAM_ID_INSERT_FAIL";
	public static final String ERR_PROGRAM_ID_NULL = "PROGRAM005";
	public static final String ERR_PROGRAM_NAME = "PROGRAM006";


	/**public static final String ERR_PROGRAM_INS_USER = "PROGRAM008";


	public static final String ERR_PROGRAM_UPD_USER = "PROGRAM009";*/


	public static final String ERR_PROGRAMID_INVALID = "PROGRAM013";


	public static final String FAIL_PROGRAM_DELETE = "PROGRAM014";


	public static final String ERR_PROGRAM_INS_USER = "PROGRAM015";


	public static final String ERR_PROGRAM_UPD_USER = "PROGRAM015";
	
	
	public static final String ERR_PROGRAM_ID_NEGATIVE = "PROGRAMIDERR002";
	public static final String SUCCESS_PROGRAM_PARAMETERS_UPDATE = "PROGRAMPARAMETER001";
	public static final String FAILED_PROGRAM_PARAMETERS_UPDATE = "PROGRAMPARAMETER002";
	
	public static final String ERR_AUTHENTICATION_TYPE = "AUTHTYPE001";
	
	public static final String ERR_ENV_TYPE = "ENV001";
	
	public static final String ERR_UPC_EMPTY = "UPCEMPTY002";
	
	public static final String ERR_UPC_EXC = "UPCERR003";
	
	public static final String ERR_UPC_NOT_EXIST = "UPCNOTEXIST002";
	public static final String ERR_PRODUCT_PACKAGE = "PRODUCTPACKAGE002";
	
	public static final String PRODUCT_PURSE_UPDATE = "PRODUCTPURSEUPDATE";
	
	public static final String SUCCESS_USER_ACTIVATED = "USER_ACTIVE_000";
	public static final String SUCCESS_USER_DEACTIVATED = "USER_DEACTIVE_000";
	
}
