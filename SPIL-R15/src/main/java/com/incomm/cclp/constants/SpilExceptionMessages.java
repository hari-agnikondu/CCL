package com.incomm.cclp.constants;

/*
 * This class has the Description or Message to be used in Exception
 */
public class SpilExceptionMessages {

	private SpilExceptionMessages() {
		throw new IllegalStateException("Spil Exception Messages class");
	}

	// XML Validation Messages
	public static final String REVERSAL_EXEC = "Exception from Reversal Transaction Class";
	public static final String PROP_FILE_SQLEXEC = "SQLException in getting the Property file path for the transaction";
	public static final String PROP_FILE_NOTFOUND = "Property file path not found for the transaction";
	public static final String PROP_FILE_EXEC = "Exception in getting the Property file path for the transaction";

	public static final String XML_VALIDATION_FAILED = "XML Validation Failed";
	public static final String PINCHANGE_EXEC = "Exception in PIN Change";
	public static final String PINCHANGE_HSM_EXEC = "HSM Exception in PIN Change";

	public static final String DAO_LOAD_EXEC = "Exception from DAO Class";

	public static final String PREAUTH_FAILED = "PreAuth Verifications Failed";
	public static final String PREAUTH_EXEC = "Exception from PreAuth Verification";

	public static final String CLASSNAME_VERIFICATION_NOTFOUND = "No ClassName found for the Msgtype and TxnCode";
	public static final String EXEC_VERIFICATION_LOAD = "Exception in loading the Verification Class";
	public static final String VERIFICATION_INTERFACEIMPL = "Verification Classes must implement the interface TransactionVerification";

	public static final String FIIDNOTFOUND = "FIID is not found";
	public static final String INVALIDFIID = "Invalid FIID";
	public static final String CARDNOTFOUND = "Invalid Card";
	public static final String ACCOUNTNUMNOTFOUND = "Invalid Account Number";
	public static final String INVALIDCOMBINATATION = "Invalid Combination of Account and Card  Number";
	public static final String ORIGCARD_NOTFOUND = "Original Card Not Found";
	public static final String INVALIDCARDSTATUS = "Invalid Card Status";
	public static final String CARDSDET_SQLEXECEPTION = "SQLException in getting Card Details";
	public static final String CARDSDET_EXECEPTION = "Exception in getting Card Details";
	public static final String CARDSTAT_EXECEPTION = "Exception in Card Status Verification";
	public static final String FIID_SQLEXECEPTION = "SQLException in FIID Verification";
	public static final String FIID_EXECEPTION = "Exception in FIID Verification";

	public static final String INVALID_MEMBERNO = "InValid Member Number";
	public static final String MEMNO_SQLEXECEPTION = "SQLException in FIID Verification";
	public static final String MEMNO_EXECEPTION = "Exception in FIID Verification";
	public static final String INVALID_EXPDATE = "Invalid Expiry Date";
	public static final String CARD_EXPIRED = "Expired Card";
	public static final String EXPDATE_SQLEXECEPTION = "SQLException in Expiry Date Verification";
	public static final String EXPDATE_EXECEPTION = "Exception in Expiry Date Verification";

	public static final String HSM_SQLEXCEPTION = "SQLException in getting the Details of HSM for the Institution";
	public static final String LOAD_PROFILE_SQLEXCEPTION = "SQLException in Loading Profile Details";
	public static final String LOAD_PROFILE_EXCEPTION = "Exception in Loading Profile Details";

	public static final String HSMCMD_EXCEPTION = "Exception in HSM command generation";
	public static final String HSM_EXCEPTION = "Exception from HSM";
	public static final String HSM_EXCEPTION_CONN = "Exception from HSM in getting the Connection";
	public static final String CVV_VERIFY_FAIL = "CVV Verification Failed";
	public static final String ERROR_CVV = "Error in CVV Verification";
	public static final String CVV_KEY_ERROR = "CVV Key Error";
	public static final String ERROR_CVK_STORAGE = "Error in CVV input or storage";
	public static final String VERIFICTION_SQLEXCEPTION = "SQLException in loading the Verification classes";

	public static final String ATMPIN_VERIFY = "Exception in ATMPINVerification";
	public static final String PINFAIL = "PIN VERIFICATION FAILURE";
	public static final String UPD_PINCHECK = "ERROR IN UPDATION OF PIN VERIFICATION RESULT";
	public static final String UPD_PINCHECK_SQLEXEC = "SQLException IN UPDATION OF PIN VERIFICATION RESULT";
	public static final String CHECKPIN_DELIVERYCHNL = "Exception in Checking the Details of PINVerification for Delivery Channel";
	public static final String PIN_INTERFACEIMPL = "PINVerifcation classes must implement the interface com.fss.cmsauth.preauthverification.PINVerificationforDeliveryChannel";
	public static final String PRESCREEN_INTERFACEIMPL = "PreScreening classes must implement the interface";
	public static final String PIN_VERIFY = "Exception in PINVerification";
	public static final String PIN_ILLEGALACCESS = "IllegalAccessException in PINVerification";
	public static final String PIN_CLASSNOTFOUND = "ClassNotFoundException in PINVerification";

	public static final String DELIVERYCHNL_NOTFOUND = "Delivery Channel Details Not Found for PINVerification";
	public static final String PINVERIFY_DELIVERYCHNL_SQL = "SQLException in Checking the Details of PINVerification for Delivery Channel";
	public static final String PINVERIFY_DELIVERYCHNL_EXEC = "Exception in Checking the Details of PINVerification for Delivery Channel";
	public static final String PINVERIFY_DELIVERYCHNL_SQLEXEC = "SQLException in Checking the Details of PINVerification for Delivery Channel";
	public static final String PROFILENOTFOUND = "Card Profile Not Found";

	public static final String HSM_COMM_EXCEPTION = "Exception in Sending and Receiving Message";
	public static final String HSM_PINPARAM_EXCEPTION = "Exception in Setting the PIN Parameters";
	public static final String HSM_PINPARAM_SQLEXCEPTION = "SQLException in Setting the PIN Parameters";
	public static final String PIN_NOSUCHMETHOD = "No such Method Exception in PIN Verification";

	public static final String RESTRICTED_CARD = "Restricted Card";
	public static final String ACTIVE_CARD = "Active Card";

	public static final String INACTIVE_CARD = "Inactive Card";
	public static final String HSM_CONN_EXCEPTION = "Problem in establishing connection with HSM";

	public static final String LOST_CARD = "Lost Card";
	public static final String CLOSED_CARD = "Closed Card";
	public static final String HSM_COMMAND_EXEC = "Exception in getting HSM Command";
	public static final String HSM_COMMANDGEN_EXEC = "Exception in command generation";
	public static final String DATASOUCE_FILENOTFOUND = "DataSource Property File Not Found";
	public static final String IO_EXEC = "IOException in reading the DataSource Property file";

	public static final String AUTHLEVEL_FILENOTFOUND = "AuthLevel Property File Not Found";
	public static final String AUTHLEVEL_IO_EXEC = "IOException in reading the AuthLevel Property file";
	public static final String AUTHLEVEL_EXEC = "Exception in Loading AuthLevel Classes";

	public static final String CONNECTION_EXEC = "Exception in getting Connection";
	public static final String INST_DETNOTFOUND = "Institution details are not found";
	public static final String DATASOURCE_EXEC = "Exception in Getting DataSource";
	public static final String JNDI_CONN_EXEC = "Exception in getting JNDIConnection";
	public static final String JNDI_CONN_NULLEXEC = "Null Pointer Exception in getting JNDIConnection";
	public static final String JNDI_INST_NOTFOUND = "DSN details of the Institution not Found";
	public static final String JNDI_SQLEXEC = "SQLException in getting Institution details";

	public static final String INSTCODE_NOTFOUND = "Institution code not found for the Bin";
	public static final String INST_SQLEXEC = "SQLException in finding the institution";
	public static final String INST_EXEC = "Exception in finding the institution";

	public static final String INST_DSN_SQLEXEC = "SQLException in getting the DSN for the institution";
	public static final String INST_DSN_EXEC = "Exception in loading the DSN for the institution";

	public static final String BINAUTHDET_EXEC = "Exception in getting Authorization details for the bin";
	public static final String PINPARAMDET_NOTFOUND = "PIN parameter details not found for the institution";
	public static final String PINPROCESS_ERROR = "Error in PIN Verification";
	public static final String INVALID_DELIVERYCHNL = "Invalid DeliveryChannel";

	// Added for PreScreening
	public static final String INCORRECT_PAN_LENGTH = " Incorrect PAN Length ";
	public static final String PANLENGTHCHECK_SQLEXCEPTION = " SQLException in PAN Length Check ";
	public static final String PANLENGTHCHECK_NULLPOINTEREXCEPTION = " NULL Pointer Exception in PAN Length Check ";
	public static final String PANLENGTHCHECK_EXCEPTION = " Exception in PAN Length Check ";

	public static final String MOD10CHECK_FAILED = " MOD10 Check Failed ";
	public static final String ISO93_CARDBLANK_FAILED = "PAN Check Failed";
	public static final String MOD10CHECK_SQLEXCEPTION = " SQLException in MOD10 Length Check ";
	public static final String MOD10CHECK_NULLPOINTEREXCEPTION = " NULL Pointer Exception in MOD10 Length Check ";
	public static final String MOD10CHECK_INDEXOUTOFBOUNDEXCEPTION = " Index out of Bound Exception in MOD10 Length Check ";
	public static final String MOD10CHECK_EXCEPTION = " Exception in MOD10 Length Check ";

	public static final String TRANSAMTEXCEEDSONLINEAGGRLIMIT = " Transaction Amount exceeds Online Aggregate Limit ";
	public static final String TRANSAMTEXCEEDSATMONLINELIMIT = " Transaction Amount exceeds ATM Online Limit ";
	public static final String TRANSAMTEXCEEDSPOSONLINELIMIT = " Transaction Amount exceeds POS Online Limit ";

	public static final String CASHWITHDRAWALCHECK_SQLEXCEPTION = " SQLException in Cash Withdrawal Check ";
	public static final String CASHWITHDRAWALCHECK_EXCEPTION = " Exception in Cash Withdrawal Check ";

	public static final String PURCHASETRANSACTION_SQLEXCEPTION = " SQLException in Purchase Transaction Check ";
	public static final String PURCHASETRANSACTION_EXCEPTION = " Exception in Purchase Transaction Check ";

	public static final String BINAUTHORIZATION_SQLEXCEPTION = " SQLException in Purchase Transaction Check ";
	public static final String BINAUTHORIZATION_EXCEPTION = " Exception in Loading BIN Authorization ";

	public static final String ATM_USAGE_LIMIT_CHECK = " ATM Usage Limit has exceeded the maximum limit for the day ";
	public static final String POS_USAGE_LIMIT_CHECK = " POS Usage Limit has exceeded the maximum limit for the day ";
	public static final String PREAUTH_USAGE_LIMIT_CHECK = " PreAuth Usage Limit has exceeded the maximum limit for the day ";
	public static final String USAGE_LIMIT_EXCEPTION = " Exception in Purchase Transaction Check ";

	public static final String FIRST_TRANS_POS = " First Transaction for the card cannot happen in POS ";
	public static final String FIRST_TRANS_SQLEXCEPTION = " SQLException in First Transaction Check ";
	public static final String FIRST_TRANS_EXCEPTION = " Exception in First Transaction Check ";

	public static final String PRESCREENING_FAILED = "PreScreening Checks Failed";

	public static final String TRANSACTION_LIMIT_CHECK_SQLEXCEPTION = " SQLException in Transaction Limit Check ";
	public static final String TRANSACTION_LIMIT_CHECK_EXCEPTION = " Exception in Transaction Limit Check ";

	// Exceptions After the verification process

	public static final String PROCEDURE_EXCE = "Exception from Procedure for transaction";

	public static final String AUTHLEVELCLASS_NOTFOUND = "AuthLevel classe load Failed";
	public static final String AUTHLEVELCLASS_ILLEGALACCESS = "IllegalAccessException in Getting the AuthLevel Object";
	public static final String AUTHLEVEL_INTERFACEIMPL = "AuthorizationLevel classes must implement the interface com.fss.cmsauth.authlevels.Authorize";
	// Exceptions from DAO classes
	public static final String DAO_SQL_EXEC = "SQL Exception in DAO Class";
	public static final String DAO_EXEC = "Exception in DAO Class";

	public static final String PIN_CHNG_EXEC = "Exception in PIN change process";

	public static final String CARDSTATUS_FAILURE_RES_DESC = "CARD STATUS VERIFICATION FAILED";
	public static final String FIRSTTRANS_FAILURE_RES_DESC = "FIRST ATM TRANSACTION CHECK FAILED";
	public static final String MEMBERNO_FAILURE_RES_DESC = "INCORRECT MEMBER NUMBER";
	public static final String CARDEXPIRY_FAILURE_RES_DESC = "EXPIRATION DATE VERIFICATION FAILED";
	public static final String FIID_FAILURE_RES_DESC = "FIID VERIFICATION FAILED";
	public static final String PIN_FAILURE_RES_DESC = "PIN VERIFICATION FAILED";
	public static final String CVV_FAILURE_RES_DESC = "CVV VERIFICATION FAILED";
	public static final String PRESREEN_FAILURE_DESC = "PreScreening Checks Failed";
	public static final String TRANS_LIMIT_NOTDEFINED = "Transaction Limit details not defined for the Card";

	public static final String REQPARSING_EXEC = "Exception in Parsing the Request details ";
	public static final String REQPARSING_MASTERDATA_EXEC = "Exception in Parsing the Master Data of Request";

	public static final String REQPARSING_HEADER_EXEC = "Exception in Request Parsing the Header";
	public static final String PREAUTH_DET_EXEC = "Exception in getting the PreAuth details";
	public static final String PREAUTH_UPD_EXEC = "Exception while updating the PreAuth details in acct mast";

	public static final String HASH_CARD_EXEC = "Exception in getting Hashed Card Number";

	public static final String ORIG_CARD_EXEC = "Exception in getting Original Card Number";
	public static final String CONVERT_CURR_EXCE = "INVALID TRANSACTION CURRENCY";

	public static final String DATE_PARSE_EXCE = "Date Parse Exception";

	// For Card Activation profile
	public static final String PROD_ID_EXEC = "Product Details Not Present";

	// For validation after parsing xml
	public static final String INVALID_DATA_ELEMENT = "Invalid Data Element";
	public static final String VALIDATION_FILE_MISSING = "Validation File Missing";
	public static final String VALIDATION_FILE_IO_ERROR = "Validation File IO Error";

	public static final String INVALID_PREAUTH_COMPCOUNT = "Invalid PreAuth CompletionCount";
	public static final String INVALID_TXN_DATE = "Invalid Transaction Date";
	public static final String INVALID_TXN_TIME = "Invalid Transaction Time";
	// For validation RVSLCDE with respective of MSG_TYPE
	// Added For Card issuence Process
	public static final String BRANCH_CODE_NOT_DEFINED = "BRANCH CODE NOT DEFINED FOR INTUSTION";
	public static final String KYC_FAIL = "KYC VERIFICATION FAILED";
	public static final String KYC_ACCESS_FAIL = "IDOLOGY COMMUNICATION FAILED";
	public static final String DUPLICATE_RRN = "DUPLICATE RRN";
	public static final String KYC_NOT_DONE_UNREGISTERD = "Unregistered / KYC Not done";
	public static final String DOB_YEAR = "Invalid Data for Year of Date of Birth";
	public static final String PING_GEN_FAILED = "Pin Generation Failed";
	public static final String PINGEN_HIST_FAILED = "Exception while inserting the PIN Generation history";
	public static final String PINGEN_NOT_DONE = "Pin Generation Process Not done";
	public static final String PIN_UPDATE_NOT_DONE = "Pin Updation Process Not done";
	// Added for MMPOS UsageLimit check
	public static final String MMPOS_USAGE_LIMIT_CHECK = " MMPOS Usage Limit has exceeded the maximum limit for the day ";
	public static final String TRANSAMT_EXCEEDS_MMPOS_ONLINE_LIMIT = " Transaction Amount exceeds MMPOS Online Limit ";
	public static final String PIN_GEN_EXCEPTION = "PIN_GEN_EXCEPTION";
	public static final String SPIL_INVALID_TRACK2 = "Invalid Track2";
	public static final String SPIL_INVALID_PAN = "Card/PIN is Invalid";
	public static final String INVALID_PIN = " Incorrect personal identification number ";
	public static final String SPIL_INVALID_CURRENCY = "Invalid Currency Code";
	public static final String SPIL_STOLEN_CARD = "Stolen Card";
	public static final String PIN_TRIES_EXCEDED = " PIN tries exceeded ";
	public static final String INVALID_DATA_FOR_STATE = "Invalid Data for Physical Address State";
	public static final String INVALID_DATA_FOR_COUNTRY = "Invalid Data for Country Code";

	public static final String INVALID_DATA_FOR_PRODUCT = "Invalid Data for Product";
	public static final String INVALID_AMOUNT = "Invalid Amount";
	public static final String PIN_ALR_GEN_EXCE = "Pin Already Generated";

	public static final String CARD_ACTIVATION_ALREADY_DONE = "CARD ACTIVATION ALREADY DONE";
	public static final String INVALID_DATA_FIRSTNAME = "Invalid Data for First Name";
	public static final String INVALID_DATA_LASTNAME = "Invalid Data for Last Name";
	public static final String INVALID_DATA_PHYADDR_LINE_ONE = "Invalid Data for Physical Address Line One";
	public static final String INVALID_DATA_PHYADDR_LINE_TWO = "Invalid Data for Physical Address Line Two";
	public static final String INVALID_DATA_PHYADDR_CITY = "Invalid Data for Physical Address City";
	public static final String INVALID_DATA_PHYADDR_STATE = "Invalid Data for Physical Address State";
	public static final String INVALID_DATA_PHYADDR_ZIP = "Invalid Data for Physical Address ZIP";
	public static final String INVALID_DATA_IDTYPE = "Invalid Data for ID Type";
	public static final String INVALID_DATA_IDISS = "Invalid Data for ID Issuer";
	public static final String INVALID_DATA_IDNUM = "Invalid Data for ID Number";
	public static final String INVALID_DATA_SSN = "Invalid Data for Social Security Number";
	public static final String INVALID_DATA_PRDCTG = "Invalid Data for Product Category";
	public static final String INVALID_DATA_TELNUM = "Invalid Data for Telephone Number";
	public static final String INVALID_DATA_MOBNUM = "Invalid Data for Mobile Number";
	public static final String INVALID_DATA_EMAILID = "Invalid Data for Email ID";
	public static final String AGE_LIMIT = "Age Limit Verification Failed";

	// Added for Track2Parsing and CVV Verification
	public static final String LOAD_TRACK2_PROFILE_EXEC = "Exception while getting Track2 information for the profile";
	public static final String TRACK2_PROFILE_NOTFOUND = "Track2 information is not defined for the profile";
	public static final String PARSE_TRACK2_PROFILE_EXEC = "Exception while Parsing the track2 details";
	public static final String FORMAT_NOTMATCHED = "Emboss Format of profile and Data are not Matched";

	public static final String INVALID_INTERFACE = "Invalid Source Interface ";
	public static final String NOMATCH_KEYCHKVAL = "Key Check Value is not mathced";
	public static final String ZMK_NOTFOUND = "Encrypted ZMK not found for the Interface";
	public static final String INTERFACE_EXEC = "Exception in getting Interface details";
	public static final String INVALID_SESSION_KEY = "Session key is not found for the Interface.Key Exchange needs to be done";
	public static final String NO_KEYSTORE_TYPE = "KeyStore type not found for the profile";
	public static final String INVALID_TRANCDE = "Invalid Transaction code";
	public static final String EXCE_KEY_UPD = "Exception in key updation";

	// Added For ACH
	public static final String ACCTNOTFOUND = "Account Not Found";
	public static final String ACH_INVALID_ACCTNO = "Invalid Account Number";

	public static final String INVALID_DATA_MAILADDR_LINE_ONE = "Invalid Data for Mailing Address Line One";
	public static final String INVALID_DATA_MAILADDR_LINE_TWO = "Invalid Data for Mailing Address Line Two";
	public static final String INVALID_DATA_ANSWERTWO = "Invalid Data for Answer Two";
	public static final String INVALID_DATA_ANSWERTHREE = "Invalid Data for Answer Three";
	public static final String INVALID_DATA_MAILADDRCITY = "Invalid Data for Mailing Address City";
	public static final String INVALID_DATA_MAILADDRSTATE = "Invalid Data for Mailing Address State";
	public static final String INVALID_DATA_MAILADDRZIP = "Invalid Data for Mailing Address Zip";
	public static final String INVALID_DATA_MAILADDRCNTRYCDE = "Invalid Data for Mailing Address Country Code";
	public static final String INVALID_DATA_IDISSUDATE = "Invalid Data for ID Issuer Date";
	public static final String INVALID_DATA_IDEXPDATE = "Invalid Data for ID Expirty Date";
	public static final String INVALID_DATA_MOTHERMAIDNAME = "Invalid Data for Mother's Maiden Name";
	public static final String INVALID_DATA_ANSONE = "Invalid Data for Answer One";
	public static final String INVALID_DATA_IDOLOGYID = "Invalid Data for Idology ID";
	public static final String KYC_FAILED_WITH_QUESTIONS = "KYC Verification Failed with Questions";

	public static final String EXEC_PASSIVE_PERIOD_CALC = "Exception in Passive Period Calculation";
	// Added for Starter card to GPR Card Issuance
	public static final String STARTER_CARD_ALREADY_GENERATED = "GPR Card Already Issued for this stater card";
	public static final String STARTERCARD_EXPIRED = "Starter Card Expired";
	public static final String STARTERCARD_CANNOT_BE_NULL = "Strater Card not found";
	public static final String INVALID_TRANSACTION = "Transaction Not Defined";
	public static final String DUPLICATE_REGISTRATION_ATTEMPT = "Duplicate Registration Attempt";

	public static final String SAVINGS_ACCT_CLOSE = "Exception in Savings Account close";

	public static final String SAVINGS_ACCT_REOPEN = "Exception in Savings Account ReOpen";

	public static final String CUSTOMERID_NOT_FOUND = "Invalid Data for Customer ID(No such Customer)";
	public static final String CARD_NO_SQLEXEC = "Exception finding the card number";
	public static final String USER_NOT_FOUND = "Invalid Data for User Id";
	public static final String CURRENCY_CODE_EXEC = "Exception in getting currency code";
	public static final String CURRENCY_CODE_NODATAFOUND = "Base currency is not defined for the institution";
	public static final String CURRENCY_CODE_NOTNULL = "Base currency cannot be null";
	public static final String PROBM_FOR_ACNUM = "Exception while finding a/c number & card number";
	public static final String INVALID_DATA_FOR_PROC = "Exception while executing procedure";
	public static final String SPIL_INVALID_USERNAEM_PASSWORD = "Invalid UserID or Password";
	public static final String SPIL_ACTIVATION_NOT_DONE = "Spil Activation  not done for this card";

	public static final String INVALID_CARD_CUSTID = "Invalid combination of Card Number and Customer ID";
	public static final String INVALID_USER_NAME = "Invalid User Name";

	public static final String SERIAL_NUMBER_NOT_AVAILABLE = "Serial Number Not Available";
	public static final String SERIAL_NUMBER_EXCEPTION = "Exception While Serial Number Flag And Count Check";

	public static final String IVR_CALLLOG_DETAILS = "Exception in Getting Call Log Details";

	public static final String CARD_STAUSUPDATION_NOT_DONE = "Card Status Updation Not Done";
	public static final String DFG_CARD_NOTFOUND = "Only DFG Cards can be upgraded from Starter to GPR";
	public static final String NULLPOINTEREXCEPTION = "NULL Pointer Exception in getting getHSMPIN ";
	public static final String GPRCARDNOTFOUND = "GPR Card not generated for StarterCard";
	public static final String NOVALIDITY = "No validity data found either product/product type profile";
	public static final String NOVALIDITYPERIOD = "Validity period is not defined for product profile";
	public static final String INVALID_DATA_GPRPRDCTG = "GPR Product Category Not Found for this Card";
	public static final String PROVIDE_CARD_CUSTID = "Provide Card Number or Customer ID Value";
	public static final String SSN_FAILED = "Maximum Threshold Reached For The ID Type";
	public static final String SPIL_INVALID_ACTIVATION = "Invalid Serial Number";
	public static final String ISO93_EXPIRY_DATE_MISSMATCH = "INVALID EXPIRY DATE";
	public static final String SPIL_REQ_MSG_MISSING = "REQUIRED MESSAGE ELEMENTS NOT PRESENT";
	public static final String CARD_EXEC = "Exception in getting  Card Number";
	public static final String INVALID_PROXY_NUMBER = "Invalid Proxy Number";
	public static final String ISO93_ADDITIONALAMNT_FAILED = "Number Format Exception in getting additional amount";
	public static final String LOAD_TRACK1_PROFILE_EXEC = "Exception while getting Track2 information for the profile";
	public static final String TRACK1_PROFILE_NOTFOUND = "Track1 information is not defined for the profile";
	public static final String PARSE_TRACK1_PROFILE_EXEC = "Exception while Parsing the track1 details";
	// Added for CVV failed in OLS
	public static final String ISOCVVRESULT1 = "CVV1 Not Verified, since CVV1 is neither present in Track-2 nor Track-1";
	public static final String ISOCVVRESULT2 = "CVV2 Not Verified, since Tag-008 value is empty";
	public static final String SPIL_INVALID_REQUEST = "Invalid Request";
	public static final String CHW_INVALID_REQUEST = "INELIGIBLE TRANSACTION";
	public static final String REQ_DATA_FOR_PHYADDONE = "Required Property Not Present for PhyAddLineOne";
	public static final String REQ_DATA_FOR_PHYSTATE = "Required Property Not Present for PhyState";
	public static final String REQ_DATA_FOR_PHYCITY = "Required Property Not Present for PhyCity";
	public static final String REQ_DATA_FOR_PHYCOUNTRY = "Required Property Not Present for PhyCountryCode";
	public static final String REQ_DATA_FOR_PHYZIP = "Required Property Not Present for PhyZIP";
	public static final String SPIL_CLOSED_CARD = "Card/PIN is Suspended";
	public static final String SPIL_MSG_TYPE_INVALID = "INVALID MESSAGE TYPE";
	public static final String INVALID_TXN_STARTDATE = "Invalid Start Date";
	public static final String INVALID_TXN_ENDDATE = "Invalid End Date";

	/* Common validation */
	public static final String INVALID_INPUT_DATA = "Invalid input data";
	public static final String PASSIVE_STATUS_UPDATION_FAILED = "Exception in updating passive status";
	public static final String DAILY_MAX_AMT_REACHED = "Daily maximum transaction amount reached";
	public static final String WEEKLY_MAX_AMT_REACHED = "Weekly maximum transaction amount reached";
	public static final String MONTHLY_MAX_AMT_REACHED = "Monthly maximum transaction amount reached";
	public static final String YEARLY_MAX_AMT_REACHED = "Yearly maximum transaction amount reached";

	public static final String DAILY_MAX_CNT_REACHED = "Daily maximum transaction count reached";
	public static final String WEEKLY_MAX_CNT_REACHED = "Weekly maximum transaction count reached";
	public static final String MONTHLY_MAX_CNT_REACHED = "Monthly maximum transaction count reached";
	public static final String YEARLY_MAX_CNT_REACHED = "Yearly maximum transaction count reached";
	public static final String LESS_THAN_MIN_AMT_PER_TXN = "Transaction Amount Is Less Than Minimum Per Txn Amount";
	public static final String GREATER_THAN_MAX_AMT_PER_TXN = "Transaction Amount Is Greater Than Maximum Per Txn Amount";
	public static final String EXCP_IN_LIMIT_VALIDATION = "Exception occurred in limit validation";
	public static final String LIMIT_UPDATION_FAILED = "Limit updation failed";
	public static final String LIMIT_REVERT_FAILED = "Failed to revert Limit";

	public static final String GET_CARD_EXECEPTION = "Exception in Get Card Details";

	public static final String INVALID_INSTRUMENT_TYPE = "Exception in get Instrument Type";

	public static final String SYSTEM_ERROR = "System Error";

	public static final String START_DATE_IS_LESS_THAN_END_DATE = "Start Date should be less than End Date";

	public static final String UPC_NOT_MATCHED_WITH_PRODUCT = "Txn UPC and Product UPC not Same";
	public static final String CVK_NOT_EXIST = "CVK does not exist";
	public static final String CVK_NOT_SUPPORTED = "CVK does not Supported";
	public static final String SPIL_MSG_TYPE_NOT_ALLOWED = "Transactions should not be allowed for this Card";

	public static final String DENOMINATION_NOT_MATCHED = "Denomination not Matched";

	public static final String CARD_NOT_FOUND = "Card not found";

	public static final String STOLEN_CARD = "Stolen Card";
	public static final String DAMAGED_CARD = "Damaged Card";
	public static final String EXPIRED_CARD = "Expired Card";
	public static final String INVALID_CARD_STATE = "Card is in an invalid state";
	public static final String INVALID_CARD = "Invalid Card";
	public static final String INVALID_UPC = "Invalid UPC";
	public static final String CARD_IS_REDEEMED = "Card is Redeemed";
	public static final String CARD_NOT_REDEEMED = "Redeemtion transaction not successfull";
	public static final String NO_TRANSACTIONS_FOUND = "No Transaction Found";
	public static final String INVALID_TARGET_CARD = "Invalid Target Card";

	/**
	 * Relodable Flag Check
	 */

	public static final String RELOADABLE_CHECK = "Top up is not applicable on this card number";

	public static final String NOT_DIGITAL_PRODUCT = "Not a Digital Product";
	public static final String INVALID_PACKAGE_ID = "Invalid Package ID";
	public static final String ORIGINAL_TRANSACTION_NOT_FOUND = "ORIGINAL TRANSACTION NOT FOUND";

	// add err_msgs while getting data from DB.
	public static final String ERROR_PARSING_DATE = "ERROR IN PARSING DATE";
	public static final String ERROR_TRANSACTION_ID_SEQ = "ERROR WHILE GETTING TRANSACTION ID SEQUENCE";
	public static final String ERROR_AUTH_ID_SEQ = "ERROR WHILE GETTING AUTH ID SEQUENCE";
	public static final String ERROR_ACCOUNT_PURSE_ID_SEQ = "ERROR WHILE GETTING ACCOUNT PURSE ID SEQUENCE";
	public static final String ERROR_AVAILABLE_BALANCE = "ERROR WHILE GETTING AVAILABLE BALANCE";
	public static final String ERROR_NO_DATA_AVAILABLE_BALANCE = "ERROR NO DATA WHILE GETTING AVAILABLE BALANCE";
	public static final String ERROR_AUTH_TRANSACTION = "ERROR WHILE VALIDATION PROCESS - AUTHORIZE TRANSACTION";
	public static final String ERROR_INSUFFICIENT_BALANCE = "INSUFFICIENT BALANCE";
	public static final String ERROR_INVALID_CARD_STATUS = "CARD IS IN INVALID STATE";
	public static final String ERROR_UPDATE_CARD_STATUS = "ERROR WHILE UPDATING CARD STATUS";
	public static final String ERROR_TRANSACTION_LOG_INSERT = "ERROR WHILE INSERTING INTO TRANSACTION LOG";
	public static final String ERROR_UPDATE_REPLACED_CARD_STATUS = "ERROR WHILE UPDATING REPLACED CARD STATUS";
	public static final String ERROR_STATEMENT_LOG_INSERT = "ERROR WHILE INSERTING STATEMENT LOG TRANSACTION AMOUNT";
	public static final String ERROR_MAX_BALANCE_EXCEED = "MAXIMUM BALANCE LIMITATION EXCEEDS";
	public static final String ERROR_DAMAGE_CARD_COUNT_GET = "ERROR WHILE GETTING DAMAGED CARD COUNT";
	public static final String ERROR_UPDATE_TOPUP_FLAG = "ERROR WHILE UPDATING TOPUP FLAG";
	public static final String ERROR_RECORD_ID_SEQ = "ERROR WHILE GETTING RECORD ID SEQUENCE";
	public static final String ERROR_UPDATE_LOAD_DATE = "ERROR WHILE UPDATING FIRST LOAD DATE";
	public static final String ERROR_UPDATE_BALANCE = "ERROR WHILE UPDATING LEDGER_BALANCE AND AVAILABLE_BALANCE ON CREDIT";
	public static final String ERROR_UPDATE_INITIAL_LOAD = "ERROR WHILE UPDATING INITIAL LOAD AMOUNT";
	public static final String ERROR_GET_AUTHCHECK = "ERROR WHILE GETTING AUTH CHECKS";
	public static final String ERROR_GET_REPLACED_CARDSTATUS = "ERROR WHILE GETTING REPLACED CARD STATUS";
	public static final String ERROR_GET_TOPUP_FLAG = "ERROR WHILE GETTING FIRST TIME TOPUP FLAG";
	public static final String ERROR_UPDATING_INVENTORY = "ERROR WHILE UPDATING THE INVENTORY";
	public static final String ERROR_GET_MERCHANT_LOCATION_ID = "ERROR WHILE GETTING LOCATION AND MERCHANT ID";
	public static final String ERROR_UPDATE_REVERSAL_FLAG = "Error WHILE UPDAING ORIGINAL TRANSACTION REVERSAL FLAG";

	public static final String REVERSAL_ALREADY_DONE = "The reversal already done/Orignal Transaction not found";
	public static final String REVERSAL_AMT_EXCEEDS = "Reversal amount exceeds the original transaction amount";
	public static final String REDEMPTION_DELAY_ERROR = "REDEMPTION_DELAY_ERROR";

	public static final String TRANSACTION_NOT_SUPPORTED_ON_PARTNER = "Transaction not supported on partner";
	public static final String ERROR_OLD_CARD_STATUS = "ERROR UPDATING OLD CARD STATUS";
	public static final String ERROR_GET_LAST_SUCCESS_ACT_TXN = "ERROR WHILE GETTING ACTIVATION RECORDS FROM TRANSACTION TABLE";
	public static final String ERROR_GET_FEE_TXN = "ERROR WHILE GETTING FEE TRANSACTIONS FROM STATEMENTS LOG TABLE";
	public static final String CARD_REVERSAL_ALREADY_DONE = "CARD REVERSAL ALREADY DONE FOR THE ORIGINAL TRANSACTION";
	public static final String ERROR_CALCULATION_EXPIRY_DATE = "ERROR WHILE CALCULATING EXPIRY DATE";
	public static final String CCF_ERROR_CVV_GENERATION = "ERROR WHILE GENERATING CVV2";
	public static final String CONVERSION_RATE_NOT_FOUND = "CONVERSION RATE NOT FOUND";
	public static final String INVALID_TXN_CURRENCY_CODE = "INVALID TRANSACTION CURRENCY";
	public static final String ACTION_NOT_SUPPORTED = "ACTION NOT SUPPORTED";

	public static final String ERROR_UPDATE_LASTTXN_DATE = "ERROR WHILE UPDATING LAST TRANSACTION DATE";
	public static final String PURSE_NOT_LINKED_WITH_PRODUCT = "PURSE NOT LINKED WITH PRODUCT";
	public static final String DEACTIVATION_NOT_ALLOWED = "Deactivation Not Allowed For This Card";
	public static final String ERROR_FIRST_LOAD_DATE = "ERROR WHILE UPDATING FIRST LOAD DATE";
	public static final String ACCOUNTPURSENOTFOUND = "ACCOUNT_PURSE NOT FOUND";
	public static final String ACCOUNT_PURSE_GROUP_NOT_FOUND = "ACCOUNT PURSE GROUP NOT FOUND";
	public static final String INVALID_PURSE = "PURSE NOT AVAILABLE IN THE SYSTEM";
	public static final String MULTIPURSE_DISABLED = "MULTIPURSE HAS BEEN DISABLED AT PRODUCT LEVEL";
	public static final String PROD_PURSE_VALIDITY = "PRODUCT PURSE VALIDITY DATE IS NOT BETWEEN THE ACTIVE AND EXPIRY DATE";

	public static final String ACCOUNTPURSENOTACTIVE = "ACCOUNT PURSE NOT ACTIVE";
	public static final String ERROR_GETTING_REDEMPTION_LOCKS = "ERROR WHILE GETTING REDEMPTION LOCKS";
	public static final String ERROR_UPDATING_REDEMPTION_LOCKS = "ERROR WHILE UPDATING REDEMPTION LOCKS";

	public static final String PRODUCT_MISMATCH = "Product Mismatch";
	public static final String CARD_BALANCE_LOCKED = "Card balance locked";
	public static final String CARD_NOT_LOCKED = "Card not Locked";
}
