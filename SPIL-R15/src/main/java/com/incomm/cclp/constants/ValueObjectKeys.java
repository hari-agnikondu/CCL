package com.incomm.cclp.constants;

public class ValueObjectKeys {

	private ValueObjectKeys() {
		throw new IllegalStateException("Value Object Keys class");
	}

	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String CHANNEL_CODE = "channelCode";

	public static final String CARDNO = "PrimaryAccountNumber";
	public static final String HASH_CARDNO = "HashPrimaryAccountNumber";
	public static final String INSTCODE = "InstitutionCode";
	public static final String INST_DSN = "Institution_DSN";
	public static final String INST_SHORTNAME = "InstShortName";
	public static final String REVERSAL_CODE = "RvslCde";
	public static final String PROXY_NUMBER = "ProxyNumber";
	public static final String PAN = "PAN";

	public static final String PRESCREENING_RES = "PreScreeningRes";

	public static final String ORIGINAL_BUSINESS_DATE = "OrigLocalTranDate";
	public static final String ORIGINAL_BUSINESS_TIME = "OrigLocalTranTime";
	public static final String ORIGINAL_RRN = "OrigRRN";
	public static final String ORIGINAL_TERMINAL_ID = "OrigCardAcceptTermId";
	public static final String ORIGINAL_CARD_NO = "OriginalPrimaryAccountNumber";
	public static final String ORIGINAL_PROXY_NUM = "OrigProxyNum";

	public static final String DAO_CLASSNAME = "DAOClassName";
	public static final String TXN_MODE = "Transaction Mode";

	public static final String EXPDATE = "expDate";
	public static final String FIID = "ATMTermData";
	public static final String MEMBERNO = "CardSequenceNumber";
	public static final String DELIVERYCHNL = "DeliveryChnl";
	public static final String TERMINALID = "CardAcceptTermID";
	public static final String MERCHANTID = "MerchantType";
	public static final String BUSINESSDATE = "LocalTranDate";
	public static final String BUSINESSTIME = "LocalTranTime";
	public static final String MSGTYPE = "MsgType";
	public static final String PINBLOCK = "PINData";
	public static final String CVV1 = "CVV1";
	public static final String SERVICECODE = "ServiceCode";
	public static final String TRANAMOUNT = "TranAmt";
	public static final String CURR_COVRT_TRANAMOUNT = "Curr_Convert_TransactionAmount";
	public static final String CARD_CARDSTAT = "DBCardStatus";
	public static final String CARD_MBRNO = "DBMemberNumber";
	public static final String CARD_FIID = "CARDFIID";
	public static final String RRN = "RRN";
	public static final String SYSTRACE_AUDITNO = "SystemTraceAuditNumber";
	public static final String NEW_PIN_BLOCK = "NewPinBlock";
	public static final String TXN_CURRENCY_CODE = "TranCurrCode";

	public static final String CARD_EXPDTAE_YYMM = "CArdExpiryDate_YYMM";
	public static final String CARD_EXPDATE = "CardExpiryDate";
	public static final String TODATE = "ToDate";
	public static final String PROD_CATG = "ProductCategory";
	public static final String PIN_OFFSET = "CardPINOffset";
	public static final String PROD_CODE = "ProductCode";
	public static final String HSM_PAN_VERIFY = "HSM_PAN_VERIFY";
	public static final String HSM_PAN_OFFSET = "HSM_PAN_OFFSET";
	public static final String HSM_PAN_LENGTH = "HSM_PAN_LENGTH";
	public static final String HSM_DECI_VALUE = "HSM_DECI_VALUE";
	public static final String HSM_PIN_LENGTH = "HSM_PIN_LENGTH";
	public static final String INCOM_REF_NUMBER = "IncommRefNum";
	public static final String SPIL_MERCREF_NUMBER = "MerchRefNum";
	public static final String CLOSING_BALANCE = "ClosingBalance";
	public static final String SPIL_UPC_CODE = "UPC";

	// Fields for Profile Details
	public static final String PVK_KEY_SPECIFIER = "ERACOM_IBMDES_KEY_SPECIFIER";
	public static final String PINBLOCK_FORMAT = "ERACOM_IBMDES_PINBLOCK_FORMAT";
	public static final String PPK = "ERACOM_IBMDES_PPK";
	public static final String PVK = "IBMDES_PVK";
	public static final String PVK_INDEX = "IBMDES_PVK";
	public static final String VISAPVVPAIR = "VISAPVVPAIR";
	public static final String VISAPVV_PVKI = "VISAPVV_PVKI";
	public static final String TPK_KEY = "TPK_KEY";
	public static final String PIN_BLOCK_FORMAT = "PIN_BLOCK_FORMAT";
	public static final String CVK_A = "CVK_A";
	public static final String CVK_B = "CVK_B";
	public static final String CVK_INDEX = "CVK_INDEX";

	public static final String CVK_KEY_SPECIFIER = "CVK_KEY_SPECIFIER";

	public static final String BIN = "BIN";
	public static final String CVK_HSM_STORED = "CVK_HSM_STORED";

	public static final String IBMDES_PAN_OFFSET = "IBMDES_PAN_OFFSET";
	public static final String IBMDES_PAN_VERIFY_LENGTH = "IBMDES_PAN_VERIFY_LENGTH";

	public static final String PVK_HSM_STORED = "PVK_HSM_STORED";
	public static final String PPK_HSM_STORED = "PPK_HSM_STORED";

	public static final String AUTH_ERACOM_IBMDES_PINBLOCK_FORMAT = "AUTH_ERACOM_IBMDES_PINBLOCK_FORMAT";
	public static final String AUTH_ERACOM_IBMDES_PPK_KEY_SPECIFIER = "AUTH_ERACOM_IBMDES_PPK_KEY_SPECIFIER";

	public static final String AUTH_ERACOM_IBMDES_PPK = "AUTH_ERACOM_IBMDES_PPK";
	public static final String AUTH_ERACOM_IBMDES_PPK_INDEX = "AUTH_ERACOM_IBMDES_PPK";

	public static final String PIN_LENGTH = "PIN_LENGTH";

	// Added for PreScreening
	public static final String ATM_ONLINE_LIMIT = "ATM Online Limit";
	public static final String POS_ONLINE_LIMIT = "POS Online Limit";
	public static final String ONLINE_AGGR_LIMIT = "Online Aggr Limit";
	public static final String ATM_USAGE_AMT = "ATM Usage Amt";
	public static final String POS_USAGE_AMT = "POS Usage Amt";
	public static final String ATM_USAGE_LIMIT = "ATM Usage Limit";
	public static final String POS_USAGE_LIMIT = "POS Usage Limit";
	public static final String LUPD_DATE = "Lupd Date";
	public static final String TRANS_DATE = "Trans Date";
	public static final String TRANS_CODE = "TranCde";
	public static final String PROCESS_CODE = "ProcessingCode";
	public static final String BUSINESS_DATE = "LocalTranDate";
	public static final String BUSINESS_TIME = "LocalTranTime";
	public static final String BUSINESS_END_TIME = "BusinessEndTime";
	public static final String SYS_DATE = "System Date";
	public static final String ATMUSAGE_MAXLIMIT = "MaximumATMUsageLimit";
	public static final String POSUSAGE_MAXLIMIT = "MaximumPOSUsageLimit";

	// Added for MMPOS
	public static final String MMPOS_ONLINE_LIMIT = "MMPOS Online Limit";
	public static final String MMPOSUSAGE_MAXLIMIT = "MaximumMMPOSUsageLimit";
	public static final String MMPOS_USAGE_AMT = "MMPOS Usage Amt";
	public static final String MMPOS_USAGE_LIMIT = "MMPOS Usage Limit";

	public static final String PREAUTHUSAGE_LIMIT = "PreAuthUsageLimit";
	public static final String PREAUTHUSAGE_MAXLIMIT = "PreAuthMaxUsageLimit";

	public static final String PREAUTH_SEQ_NO = "PreAuthSequenceNumber";
	public static final String PREAUTH_EXP_PERIOD = "PreAuthExpryPeriod";

	public static final String PREAUTH_COMPLETION_COUNT = "CompletionCount";
	public static final String PREAUTH_LAST_COMP_INDICATOR = "LastCompletionIndicator";

	public static final String LOAD_CARD_DETAILS = "LOAD_CARD_DETAILS";
	public static final String LOAD_TRAN_DETAILS = "LOAD_TRAN_DETAILS";
	public static final String DOBYEAR = "DOBYear";
	// For Card TopUp and Activation transaction of MM-POS DeliveryChannel

	public static final String LOCATION = "Location";// Terminal ID of MM-POS
	public static final String SSN = "SSN";
	public static final String DOB = "DOB";
	public static final String FIRST_NAME = "FirstName";
	public static final String MIDDLE_NAME = "MiddleName";
	public static final String LAST_NAME = "LastName";
	public static final String ADDLINE_ONE = "AddLineOne";
	public static final String ADDLINE_TWO = "AddLineTwo";
	public static final String PHYADDLINE_ONE = "PhyAddLineOne";
	public static final String PHYADDLINE_TWO = "PhyAddLineTwo";
	public static final String PHYCITY = "PhyCity";
	public static final String PHYZIP = "PhyZIP";
	public static final String PHYPHONE_NUMBER = "PhyPhoneNumber";
	public static final String PHYOTHERPHONE = "PhyOtherPhone";
	public static final String PHYSTATE = "PhyState";
	public static final String PHYCOUNTRYCODE = "PhyCountryCode";

	public static final String OTHERPHONE = "OtherPhone";
	public static final String CITY = "City";
	public static final String ZIP = "ZIP";
	public static final String PHONE_NUMBER = "PhoneNumber";
	public static final String EMAIL = "Email";
	public static final String STATE = "State";
	public static final String MOBILE = "Mobile";
	public static final String COUNTRYCODE = "CountryCode";
	public static final String PRODID = "ProdId";

	public static final String ORGNL_TRAN_AMNT = "OrigTranAmt";

	public static final String MERCHANT_FLOORLIMIT_INDICATOR = "MerchantFloorLimitIndicator";
	public static final String INCREMENTAL_INDICATOR = "IncrementalIndicator";
	public static final String ADDR_VERFICATION_INDICATOR = "AddressVerificationIndicator";
	public static final String PARTIAL_PREAUTH_INDICATOR = "PartialAuthIndicator";
	public static final String INTERNATIONAL_INDICATOR = "InternationalIndicator";

	public static final String IDTYPE = "IDType";
	public static final String IDISSUER = "IDIssuer";
	public static final String IDNUMBER = "IDNumber";
	public static final String PRODUCT = "Product";
	public static final String PRODUC_CATEGORY = "ProductCategory";
	public static final String CARD_NUMBER = "CardNumber";
	public static final String ACCOUNT_NUMBER = "AccountNumber";
	public static final String PIN_DATA = "PINData";
	public static final String DOB_YEAR = "DOB";
	public static final String SOCIALSECURITYNUMBER = "SocialSecurityNumber";
	public static final String EMAIL_ID = "EmailID";
	public static final String EMAIL_ID1 = "EmailID1";
	public static final String EMAIL_ID2 = "EmailID2";
	public static final String TELEPHONENUMBER = "TelephoneNumber";
	public static final String NEW_PIN_DATA = "NewPINData";
	public static final String CALL_LOGID = "CallLogID";
	public static final String TRAN_CNT = "TranCnt";
	public static final String TOCARDNUMBER = "ToCardNumber";
	public static final String SPIL_TRAN_DATE = "Date";
	public static final String SPIL_TRAN_TIME = "Time";
	public static final String SPIL_TRAN_AMT = "Amount";
	public static final String SPIL_ORGNL_TRAN_CURR = "CurrencyCode";
	public static final String SPIL_TERM_ID = "TermID";
	public static final String SESSION_KEY = "SessionKey";
	public static final String CELLPHONENO = "CellPhoneNo";
	public static final String CELLPHONECARRIER = "CellPhoneCarrier";
	public static final String LOADORCREDITALERT = "LoadOrCreditAlert";
	public static final String LOWBALALERT = "LowBalAlert";
	public static final String NEGATIVEBALALERT = "NegativeBalAlert";
	public static final String INCORRECTPINALERT = "InCorrectPINAlert";
	public static final String HIGHAUTHAMTALERT = "HighAuthAmtAlert";
	public static final String MONTH_YEAR = "MonthYear";
	public static final String CVV2 = "CVV2";
	public static final String CVV2_LENGTH = "CVV2Length";
	public static final String SPNUMBER = "SPNumber";
	public static final String CARDSTATUSCHANGE = "CardStatusChange";

	// Open Loop CVV tag
	public static final String CLCVV = "CLCVV";
	// Added for CVV verification with track2 details
	public static final String TRACK2_FORMAT_PROFILE = "Track2Format";
	public static final String TRACK2_DATA_REGEX = "TrackDataPattern";
	public static final String STARTSENTINEL_TRACK2 = "StartSentinel_Track2";
	public static final String CARDNO_TRACK2 = "CardNumber_Track2";
	public static final String SEPARATOR = "Separator";
	public static final String EXPIRY_YEAR_TRACK2 = "ExpiryYear_Track2";
	public static final String EXPIRY_MONTH_TRACK2 = "ExpiryMonth_Track2";
	public static final String SERVICE_CODE_TRACK2 = "ServiceCode_Track2";
	public static final String PINOFFSET_TRACK2 = "PINOffset_Track2";
	public static final String CVV_TRACK2 = "CVV_Track2";
	public static final String ENDSENTINEL_TRACK2 = "EndSentinel_Track2";

	public static final String TRACK2_EQUAL_TO = "SP";

	// Added for SPIL User ID and Password
	public static final String SPIL_USER_ID = "UserID";
	public static final String SPIL_PASSWORD = "Password";
	// Header Details
	public static final String HEADER_SRCAPP = "SrcApp";
	public static final String KEYCHECK_VALUE = "KeyCheckValue";
	public static final String INTERFACE_CODE = "InterfaceCode";
	public static final String TRACE_NUMBER = "TraceNumber";
	public static final String ODFI = "ODFI";
	public static final String RDFI = "RDFI";
	public static final String ACHFILENAME = "ACHFileName";
	public static final String SECCODE = "SECCode";
	public static final String IMPDATE = "ImpDate";
	public static final String PROCESSDATE = "ProcessDate";
	public static final String EFFECTIVEDATE = "EffectiveDate";
	public static final String RETURN_ACHFILENAME = "ReturnACHFileName";
	public static final String RETUN_FILE_TIMESTAMP = "RtnFileTimeStamp";
	public static final String INCOMING_CREDIT_FILEID = "IncomingCreditFileID";

	public static final String ACH_TRANSACTION_TYPEID = "ACHtransactiontypeID";
	public static final String FILE_PATH = "FilePath";

	public static final String ACH_INDIDNUM = "IndIdNum";
	public static final String ACH_INDNAME = "IndName";
	public static final String ACH_COMPANYNAME = "CompName";
	public static final String ACH_COMPANYID = "CompanyId";
	public static final String ACH_ID = "Id";
	public static final String ACH_COMPENTRYDESC = "CompEntryDesc";

	public static final String ENCR_CARDNO = "EncryptedCardNumber";

	public static final String ANI = "ANI";
	public static final String DNI = "DNI";
	public static final String IPADDRESS = "IPAddress";

	public static final String LOWBALAMOUNT = "LowBalAmount";
	public static final String HIGHAUTHAMT = "HighAuthAmt";
	public static final String DAILYBALALERT = "DailyBalAlert";
	public static final String BEGINTIME = "BeginTime";
	public static final String ENDTIME = "EndTime";
	public static final String INSUFFICIENTALERT = "InsufficientAlert";

	public static final String MAILINGADDRESSPOBOX = "MailingAddressPOBox";
	public static final String MAILINGADDRESSCITY = "MailingAddressCity";
	public static final String MAILINGADDRESSSTATE = "MailingAddressState";
	public static final String MAILINGADDRESSZIP = "MailingAddressZIP";
	public static final String MAILINGADDRESSCOUNTRYCODE = "MailingAddressCountryCode";
	public static final String PHYSICALADDRESSCOUNTRYCODE = "PhysicalAddressCountryCode";
	public static final String IDISSUANCEDATE = "IDIssuanceDate";
	public static final String IDEXPIRYDATE = "IDExpiryDate";
	public static final String MOTHERSMAIDENNAME = "MothersMaidenName";
	public static final String ANSWERONE = "AnswerOne";
	public static final String ANSWERTWO = "AnswerTwo";
	public static final String ANSWERTHREE = "AnswerThree";
	public static final String QUESTIONONE = "QuestionOne";
	public static final String QUESTIONTWO = "QuestionTwo";
	public static final String QUESTIONTHREE = "QuestionThree";
	public static final String IDOLOGYID = "IdologyID";
	public static final String MERCHANT_NAME = "MerchantName";
	public static final String MERCHANT_CITY = "MerchantCity";
	public static final String MERCHANT_REF_NUM = "MerchantRefNum";

	public static final String SPIL_MERCHANT_NAME = "MerchName";
	public static final String SPIL_STORE_ID = "Storedbid";

	// Added for ACH status inquiry transaction
	public static final String ACH_ORIGINAL_ACCOUNTNUMBER = "OrigAccountNumber";

	// For savings account creation & funds transfer
	public static final String SAVING_ACCOUNT_CREATION_FLAG = "AcctCreationFlag";
	public static final String ACCTID1 = "AcctId1";
	public static final String ACCTID2 = "AcctId2";
	// For Card Number Changed to Customer Id
	public static final String CUSTOMER_ID = "CustomerID";
	public static final String TOCUSTOMER_ID = "ToCustomerID";

	// For username,password,Security Question & Answer
	public static final String USERNAME = "Username";
	public static final String PASSWORD = "Password";
	public static final String SECURITYQUESTIONONE = "SecurityQuestionOne";
	public static final String SECURITYQUESTIONTWO = "SecurityQuestionTwo";
	public static final String SECURITYQUESTIONTHREE = "SecurityQuestionThree";
	public static final String SECURITYQUESTIONONEANSWER = "SecurityQuestionOneAnswer";
	public static final String SECURITYQUESTIONTWOANSWER = "SecurityQuestionTwoAnswer";
	public static final String SECURITYQUESTIONTHREEANSWER = "SecurityQuestionThreeAnswer";

	public static final String USERID = "UserId";

	// user authentication
	public static final String OLDPASSWORD = "OldPassword";
	public static final String STATUS = "Status";
	public static final String CARDACCEP_NAMELOC = "CardAcceptNameLoc";
	public static final String ORIGINALDATAELEMENTS = "OriginalDataElements";

	// Batch upload.
	public static final String ALTERNATEID = "AlternateID";
	public static final String MASKEDCARDNUMBER = "MaskedCardNumber";
	public static final String CARDREGDATE = "CardRegDate";
	public static final String CARDEXPRDATE = "CardExprDate";
	public static final String VMSINSTITUTION = "VmsInst";
	public static final String MASKEDSSN = "MaskedSSN";
	public static final String RESULT = "result";
	public static final String CIPCHECK = "cip";
	public static final String BATCHID = "batchId";
	public static final String PROD_CODEDESC = "Prod_codeDesc";
	public static final String PROD_CATG_DESC = "Prod_catgDesc";
	public static final String PROD_CATGCODE = "Prod_catgCode";

	public static final String MERC_ID = "MerchantId";
	public static final String TRANFEEAMT = "TranFeeAmt";
	public static final String MERCHANTZIPCODE = "MerchantZipCode";
	public static final String TRANSAMOUNT = "TransactionAmount";
	public static final String FORCEPOST = "ForcePost";
	public static final String ADMINREASONCODE = "AdminReasonCode";
	public static final String TRANSACTIONDESC = "Transdesc";
	public static final String TRANSACTIONSHORTNAME = "TranShortName";
	public static final String LEDGERBALANCE1 = "LedgerBalance1"; // Before Transaction Ledger Balance
	public static final String LEDGERBALANCE2 = "LedgerBalance2"; // After Transaction Ledger Balance.
	public static final String LEDGERBALANCE = "LedgerBalance"; // Before Transaction Ledger Balance

	public static final String POS_PINTXN = "POS_PINTXN";

	public static final String FEEPLANID = "FeePlanID";

	public static final String USER_NAME = "UserName";
	public static final String CUST_CODE = "CustCode";
	public static final String ALERT_NAME = "AlertName";
	public static final String ALERT_STAT = "AlertStat";
	public static final String NOTIFY_TYPE = "NotificationType";

	public static final String SECURITYQUESTION = "SecurityQuestion";
	public static final String SECURITYQUESTIONANSWER = "SecurityQuestionAnswer";

	public static final String LOADTIMETRANSFER = "LoadTimeTransfer";
	public static final String LOADTIMETRANSFERAMOUNT = "LoadTimeTransferAmount";
	public static final String FIRSTMONTHTRANSFER = "FirstMonthTransfer";
	public static final String FIRSTMONTHTRANSFERAMOUNT = "FirstMonthTransferAmount";
	public static final String FIFTEENMONTHTRANSFER = "FifteenMonthTransfer";
	public static final String FIFTEENMONTHTRANSFERAMOUNT = "FifteenMontTransferAmount";

	public static final String SUBJECT = "Subject";
	public static final String QUESTION = "Question";

	public static final String DECLINEFEE_RESPONSE = "DeclineFeeResponse";
	public static final String DECLINEFEE_ATTACH_TYPE = "DeclineFeeAttachType";
	public static final String DECLINEFEE_AMOUNT = "DeclineFeeAmount";
	public static final String DECLINEFEE_CODE = "DeclineFeeCode";
	public static final String DECLINEFEE_CRACCT_NO = "DeclineFeeCrAcctNo";
	public static final String DECLINEFEE_DRACCT_NO = "DeclineFeeDrAcctNo";
	public static final String DECLINEFEE_ACTUAL_AMNT = "DeclineFeeActualAmount";
	public static final String DECLINEFEE_FEEPLAN = "DeclineFeeFeePlan";
	public static final String DECLINEFEE_WAIVAMOUNT = "DeclineFeeWaivAmount";
	public static final String DECLINEFEE_WAIVPERC = "DeclineFeeWaivPercent";

	public static final String ZIPCODE = "ZipCode";
	public static final String FEEAMOUNT = "FeeAmount";
	public static final String PAYMENT_TOKEN = "Paymenttoken";
	public static final String AUTO_RECHARGE_SETTINGS = "Autorechargesettings";
	public static final String AUTO_ENABLE_DISABLE_FLAG = "EnableDisableFlag";

	public static final String NEW_STATUS = "NewStatus";
	public static final String OLD_STATUS = "OldStatus";

	public static final String DATE_RANGE_FROM = "DateRangeFrom";
	public static final String DATE_RANGE_TO = "DateRangeTo";
	public static final String TXN_HISTORY_LIST = "TxnHistoryList";

	public static final String POS_CONDITION_CODE = "POSConditionCode";
	public static final String BUSINESS_DATE_TIME = "LocalTranDateTime";

	public static final String SPIL_ADDRESS_1 = "Address1";
	public static final String SPIL_ADDRESS_2 = "Address2";
	public static final String SPIL_CITY = "City";
	public static final String SPIL_STATE = "State";
	public static final String SPIL_ZIP = "Zip";

	public static final String PARTIAL_PREAUTH_APPR_IND = "PartialApprovalIndicator";

	public static final String ORIGINAL_MSGTYPE = "OrigMSGTYPE";
	public static final String ORIGINAL_STAN = "OrigSTAN";
	public static final String ISO_FUNC_CDE = "FunctionCode";

	public static final String ORIG_ACCQ_INST_ID_CDE = "OriginalAccqInstIDCODE";
	public static final String ROWID = "RowId";
	public static final String SP_NUM_TYPE = "SPNumberType";
	public static final String WRAP_STATUS = "WrapStatus";
	public static final String ADDITIONAL_AMNT = "AdditionalAmount";

	public static final String POSTALCODE = "PostalCode";
	public static final String CARDHOLDERBILLINGADDRESS = "CardholderBillingAddress";
	public static final String OPTIONCODE = "OptionCode";
	public static final String RESULTCODE = "ResultCode";
	public static final String POSTERMINALPANENTRYMODE = "POSTerminalPANEntryMode";
	public static final String TERMFIID = "TermFIID";
	public static final String CARDVERIFICATIONINFORMATION = "CardVerificationInformation";
	public static final String CARDVERIFICATIONRESULT = "cardVerificationResult";
	public static final String TRACK1_FORMAT_PROFILE = "Track1Format";
	public static final String TRACK1_DATA_REGEX = "Track1DataPattern";
	public static final String TRACK1_EQUAL_TO = "SP";
	public static final String STARTSENTINEL_TRACK1 = "StartSentinel_Track1";
	public static final String CARDNO_TRACK1 = "CardNumber_Track1";
	public static final String EXPIRY_YEAR_TRACK1 = "ExpiryYear_Track1";
	public static final String EXPIRY_MONTH_TRACK1 = "ExpiryMonth_Track1";
	public static final String SERVICE_CODE_TRACK1 = "ServiceCode_Track1";
	public static final String PVV_TRACK1 = "PVV_Track1";
	public static final String CVV_TRACK1 = "CVV_Track1";
	public static final String ENDSENTINEL_TRACK1 = "EndSentinel_Track1";
	public static final String OLS_FROM_NETWORK = "FromNetwork";
	public static final String OLS_POSTAL_CODE = "PostalCode";
	public static final String OLS_POS_ENTRY_MODE = "POSTerminalPANEntryMode";
	public static final String OLS_CVC2 = "CVC2";

	public static final String MESSAGE_SECURITY_CODE = "MessageSecurityCode";
	public static final String KEYCHECK_VAL = "KeyCheckVal";
	public static final String KEY_TYPE = "KeyType";
	public static final String KEY_INDEX = "KeyIndex";
	public static final String KEY_LENGTH = "KeyLength";

	public static final String CVVRESULT = "CVVResult";
	public static final String CVVRESPONSE = "CVVResponse";

	public static final String CARDHOLDERBILLINGAMOUNT = "CardholderbillingAmount";
	public static final String CURRENCYCODECARDHOLDERBILLING = "CurrencyCodeCardholderBilling";
	public static final String ACQUIRINGINSTITUTIONCOUNTRYCODE = "AcquiringInstitutionCountryCode";
	public static final String MERCHANTTYPE = "MerchantType";
	public static final String EXPIRATIONDATE = "ExpirationDate";

	public static final String STAN = "STAN";
	public static final String ORIGINAL_MERCHANT_TYPE = "OrigMerchantType";
	public static final String NETWORK_SETTLEMENT_DATE = "DateSettlement";
	public static final String CARDACCEPTOR_NAMELOC = "CardAcceptorNameLocation";
	public static final String PINDATA = "PersonalIdentificationNumberData";
	public static final String TRACK3DATA = "Track3Data";
	public static final String DE61DATA = "ReservedPrivate61";
	public static final String AUTH_ID = "AUTH_ID";
	public static final String BEFORE_BALANCE = "beforeBalance";

	public static final String MERCHANTSTREET = "MerchantStreet";
	public static final String MERCHANTSTATE = "MerchantState";
	public static final String MERCHANTCOUNTRYCODE = "MerchantCountryCode";

	public static final String STORE_ID = "StoreID";

	public static final String CVV_VERIFICATION_TYPE = "CVVVerificationType";

	public static final String ISO_RESP_CODE = "ISORespCode";
	public static final String PHY_ADDRESS_REQUIRED = "PhysicalAddressRequired";
	public static final String VERIFICATION_CLASSNAME = "ClassName";
	public static final String FIRST_TIMETOPUP_FLAG = "TopupFlag";

	public static final String SPIL_PROD_ID = "productID";
	public static final String SPIL_FEE_AMT = "Fee";
	public static final String SPIL_TARGET_CARDNUM = "TargetCardNumber";
	public static final String SPIL_PIN_DATA = "PIN";

	public static final String AUTO_RECHARGE_ENABLE_DISABLE_FLAG = "AutoRechargeEnableDisableflag";
	public static final String RTVS_ENABLE_DISABLE_FLAG = "RtvsEnableDisableFlag";
	public static final String EBRIO_ENABLE_DISABLE_FLAG = "EbrioEnableDisableFlag";
	public static final String SPIL_TRAN_CNT = "TxnCount";

	public static final String EBRIO_INDICATOR_PASSIVE_CARD = "EbrioIndicatorForPassiveCard";

	public static final String SPIL_REQTIME_ZONE = "TimeZone";
	public static final String SPIL_ORIGINTRAN_DATE = "OrginDate";
	public static final String SPIL_ORIGINTRAN_TIME = "OrginTime";
	public static final String SPIL_ORIGINTIME_ZONE = "OrginTimeZone";
	public static final String SPIL_LOCALE_CNTRY = "CountryCode";
	public static final String SPIL_LOCALE_CURRENCY = "LocaleCurrencyCode";
	public static final String SPIL_LOCALE_LANGUAGE = "Language";
	public static final String SPIL_POS_ENTRYMODE = "EntryMode";
	public static final String SPIL_POS_CONDCODE = "ConditionCode";
	public static final String SPIL_START_DATE = "StartDate";
	public static final String SPIL_END_DATE = "EndDate";
	public static final long SOCKET_TIME_OUT = 2500000;
	public static final String SOURCE_INFO = "SourceInfo";

	/* Spill Validations */

	public static final String SPIL_CARD_STATUS_CHECK = "cardStatusCheck";
	public static final String CARD_RANGE_ID = "CardRangeID";
	public static final String PRODUCT_ID = "productID";
	public static final String MDM_ID = "merchantdbid";

	public static final String CARD_NUM_HASH = "cardNumHash";
	public static final String CARD_ACCOUNT_ID = "accountId";
	public static final String CARD_SERIAL_NUMBER = "serialNumber";
	public static final String DELIVERY_CHANNEL_CODE = "DeliveryChnl";
	public static final String DELIVERY_CHANNEL_SHORT_NAME = "deliveryChannelShortName";
	public static final String ORIGINAL_TRANSACTION_SHORT_NAME = "originaltransactionShortName";
	public static final String AUTH_JAVA_CLASS_NAME = "transactionClassName";
	public static final String PARTY_SUPPORTED = "partySupported";
	public static final String SPIL_UPC = "UPC";
	public static final String ACTUAL_AMNT = "actualAmount";
	public static final String IS_FINANCIAL = "isFinacial";
	public static final String CREDIT_DEBIT_INDICATOR = "CreditDebitIndicator";
	public static final String SPIL_MSG_TYPE = "SpilMsgType";

	public static final String MSG_TYPE = "MsgType"; // added by Hari for checking instrument Id

	public static final String SP_NUM_TYPE_PAN = "PAN";
	public static final String SP_NUM_TYPE_ACCOUNT_NUMBER = "Account Number";
	public static final String SP_NUM_TYPE_SERIAL_NUMBER = "Serial Number";
	public static final String SP_NUM_TYPE_PROXY_NUMBER = "Proxy Number";
	public static final String SP_NUM_TYPE_CUSTOMER_ID = "Customer Id";

	public static final String CARD_ACTIVATION_DATE = "CardActivationDate";
	public static final String OLD_CARD_STATUS = "OldCardStatus";
	public static final String LAST_TXN_DATE = "LastTransactionDate";
	public static final String CARD_NUM = "ClearCardNumber";
	public static final String PURSE_ID = "PurseId";
	public static final String CUSTOMER_CODE = "CustomerCode";
	public static final String IS_REDEEMED = "IsRedeemed";

	public static final String EXT_DESC = "ExtDesc";
	public static final String EXT_ADDR = "ExtAddr";
	public static final String CARD_USAGE_LIMIT = "cardUsageLimit";

	public static final String RESP_CODE = "responseCode";
	public static final String RESP_MSG = "responseMsg";
	public static final Object TRANSACTION_STATUS = "transactionStatus";
	public static final Object AVAILABLE_BALACE = "availableBalance";
	public static final Object STATUS_FAILED = "F";
	public static final Object STATUS_YES = "Y";
	public static final Object STATUS_NO = "N";
	public static final String PASSIVE_SUPPORT_FLAG = "passiveSupportFlag";
	public static final String CASHIER_ID = "cashierID";
	public static final String EXP_DATE = "expDate";

	public static final String ISSUER_ID = "issuerID";
	public static final String PARTNER_ID = "partnerID";

	public static final String CARD_NUM_ENCR = "CARD_NUM_ENCR";
	public static final String PRFL_ID = "profileId";

	public static final String CONSUMED_FLAG = "Consumedstatus";
	public static final String TRANSACTION_IDENTIFIER = "transactionIdentifier";
	public static final String GLOBAL_RULE_ID = "globalRuleID";
	public static final String GLOBAL_RULE_NAME = "globalRuleName";
	public static final String GLOBAL_RULE_FLAG = "globalRuleFlag";
	public static final String AMEX_WALLETCOUNT_RESULT = "AMEXWALLETCOUNTRESULT";
	public static final String AMEX_DE62WALLETCOUNT_RESULT = "AMEXDE62WALLETCOUNTRESULT";
	public static final String TOKEN_RULE_RESPONSE = "TOKEN_RULE_RESPONSE";
	public static final String CARD_ACCEPT_ID_CODE_DE42 = "CardAcceptorIdentificationCode";
	public static final String ACTIVATION_AS_RELOAD = "activationAsReload";

	// Added By Hari for sonarLint Fixes
	public static final String DAILY_MAX_AMT = "dailyMaxAmt";
	public static final String WEEKLY_MAX_AMT = "weeklyMaxAmt";
	public static final String MONTHLY_MAX_AMT = "monthlyMaxAmt";
	public static final String YEARLY_MAX_AMT = "yearlyMaxAmt";

	public static final String DAILY_MAX_COUNT = "dailyMaxCount";
	public static final String WEEKLY_MAX_COUNT = "weeklyMaxCount";
	public static final String MONTHLY_MAX_COUNT = "monthlyMaxCount";
	public static final String YEARLY_MAX_COUNT = "yearlyMaxCount";

	public static final String MAX_AMT_PET_TX = "maxAmtPerTx";
	public static final String MIN_AMT_PET_TX = "minAmtPerTx";

	public static final String LIMITS = "Limits";
	public static final String LINE_SEPARATOR = "line.separator";
	public static final String EVAL_EXP = "EVAL_EXP";
	public static final String DENOMINATION_TYPE = "denominationType";
	public static final String RETAIL_UPC = "retailUPC";
	public static final String B2B_UPC = "b2bUpc";
	public static final String OTHER_TXN_ID = "otherTxnId";
	public static final String ACTIVATION_ID = "activationId";
	public static final String NULL_PARAM = "NULL_PARAM";
	public static final String RELOADABLE_FLAG = "reloadableFlag";
	public static final String DISABLE = "Disable";

	public static final String CARD_STATUS_CLOSED = "9";
	public static final String CARD_STATUS_DAMAGED = "3";
	public static final String CARD_STATUS_INACTIVE = "0";

	public static final String FLAG_YES = "Y";
	public static final String FLAG_NO = "N";

	// added by anitha for logger purpose
	public static final String AUTHORIZED_ID = "AUTHORIZED ID";
	public static final String AUTHORIZED_AMOUNT = "AUTHORIZED AMOUNT";

	// Added For Product Funding option
	public static final String PRODUCT_TYPE = "productType";
	public static final String B2B_PRODUCT_FUNDING = "product_funding";
	public static final String B2B_SOURCE_FUNDING = "fund_amount";
	public static final String ORDER_DENOM = "denomination";

	public static final String REPL_FLAG = "replFlag";

	public static final String PRODUCT_TYPE_KEY = "productType";

	public static final String PRODUCT_TYPE_RETAIL = "Retail";

	public static final String GENERAL = "General";
	public static final String PARTIALAUTHINDICATOR = "partialAuthIndicator";
	public static final String EXPIRED_PRODUCT_CARD_STATUS = "20";
	public static final String FORM_FACTOR = "formFactor";
	public static final String PACKAGEID = "PackageID";

	public static final String LAST_4DIGIT = "Last4Digit";

	// added by sdhumal --- saleActiveCode
	public static final String PROD_FEE_CONDITION = "ProdFeeCondition";
	public static final String PROD_TXN_FEE_AMT = "ProdTxnFeeAmt";
	public static final String PROD_FLAT_FEE_AMT = "ProdFlatFeeAmt";
	public static final String PROD_PERCENT_FEE_AMT = "ProdPercentFeeAmt";
	public static final String PROD_MIN_FEE_AMT = "ProdMinFeeAmt";
	public static final String FREE_FEE_FLAG = "FreeFeeFlag";
	public static final String MAX_FEE_FLAG = "MaxFeeFlag";
	public static final String MAXCARDBAL = "maxCardBalance";
	public static final String REDEMPTION_DELAY_TRAN_AMOUNT = "REDEMPTION_DELAY_AMOUNT";
	public static final String DB_SYSDATE = "DB_SYSDATE";
	public static final String PRODUCT_FUNDING = "b2bProductFunding";
	public static final String LOCATION_ID = "LOCATION_ID";
	public static final String MERCHANT_ID = "MERCHANT_ID";

	public static final String REVERSAL_FLAG = "reversalFlag";
	public static final String PARTY_TYPE = "PARTY_TYPE";

	public static final String REDEMPTION_DELAY_CHECK_AVAIL = "redemptionDelayCheckAvail";

	public static final String CURRENCY_ID = "currencyId";

	public static final String DIGITAL_PIN = "DIGITAL_PIN";
	public static final String VALIDITY_PERIOD = "validityPeriod";
	public static final String VALIDITY_PERIOD_FORMAT = "validityPeriodFormat";
	public static final String MONTH_END_EXPIRY = "monthEndCardExpiry";
	public static final String MONTH_END_EXPIRY_ENABLE = "Enable";
	public static final String MONTH_END_EXPIRY_DISABLE = "Disable";
	public static final String ACTIVE_FROM = "activeFrom";
	public static final String CVKA = "cvkA";
	public static final String CVKB = "cvkB";
	public static final String CVV = "CVV";
	public static final String ACTION = "action";
	public static final String SOURCE_REF_NUMBER = "sourceReferenceNumber";
	public static final String REFERENCE_DATE_FORMAT = "yyyyMMddHHmmss";
	public static final String TIME_FORMAT = "HHmmss";
	public static final String CARD_STATUS = "cardStatus";
	public static final String TRANLOG_DATE_FORMAT = "yyyyMMdd";
	public static final String PARTNER_NAME = "partnerName";
	public static final String SUCCESS_CODE = "R0001";
	public static final String SUCCESS_MSG = "Success";
	public static final String FAILURE_CODE = "R0012";
	public static final String FAILURE_MSG = "System Error";
	public static final String SPIL_BLK_TXN_RECORD_NUM = "recordNum";

	/** R08 changes added by venkateshgaddam starts **/
	public static final String FEES = "Fees";
	public static final String TRANSACTION_FEES = "Transaction Fees";
	public static final String UPDATE_USAGE_FEE = "UpdateUsageFee";
	public static final String MONTHLY_FEE_CAP = "Monthly Fee Cap";
	public static final String ORGINAL_TXN_SHORT_NAME = "OrigTxnShortName";
	public static final String MONTHLY_FEE_CAP_ASSESSMENT_DATE = "monthlyFeeCap_assessmentDate";
	public static final String FEE_PERIOD = "FeePeriod";
	public static final String FEE_CAP = "FeeCap";
	public static final String FEE_ACCRUED = "FeeAccurued";
	public static final String USAGE_FEE_PERIOD = "UsageFeePeriod";
	public static final String CURRENT_FEE_ACCRUED = "CurrentFeeAccurued";
	public static final String PRODUCT_MONTHLY_FEE_CAP = "ProductMonthlyFeeCap";
	public static final String PARTNER_PARTY_TYPE = "PartnerPartyType";
	public static final String DUPLICATE_CHECK_FLAG = "DuplicateCheckFlag";
	public static final String AVAIL_BALANCE = "AvailBalance";
	public static final String PARTIAL_AUTH_INDICATOR_AVAL_FLAG = "PartialAuthIndicatorAvalFlag";
	public static final String ORIGINAL_PARTIAL_AUTH_IND = "OriginalPartialAuthInd";
	public static final String ACC_NUMBER = "ACCOUNT_NUMBER";
	public static final String PARTIAL_AUTH_IND = "partialAuthIndicator";
	public static final String ORGNL_TRAN_FEE_AMNT = "OrigTranFeeAmt";
	public static final String ORGNL_AUTH_AMNT = "OrigAuthAmt";
	public static final String ORGNL_DR_CR_FLAG = "OrigDrCrFlag";
	public static final String ORGNL_MAX_FEE_FLAG = "OrigMaxFeeFlag";
	public static final String ORGNL_FREE_FEE_FLAG = "OrigFreeFeeFlag";
	public static final String SALE_ACTIVE_CODE_RESP_TYPE = "saleActiveCodeResponseType";
	/** R08 changes added by venkateshgaddam ends **/

	// Currency Conversion
	public static final String PURSE_CURRENCY = "purseCurrency";
	public static final String ORGNL_TRAN_AMOUNT = "OrgnlTranAmount";
	public static final String CURRENCY_CONV_RATE = "ConvRate";
	public static final String MINORUNITS = "minorUnits";
	public static final String ORGNL_CURRENCY_CONV_RATE = "OrgnlConvRate";

	// Passive card status update
	public static final String ORGNL_TRAN_REQ_AMNT = "OrigTranReqAmt";

	public static final String ACCOUNT_PURSE_ID = "accountPurseId";

	// Multipurse
	public static final String PURSE_NAME = "purseName";
	public static final String EFFECTIVE_DATE = "effectiveDate";
	public static final String EXPIRY_DATE = "expiryDate";
	public static final String CURRENCY = "currency";
	public static final String DEFAULT_PURSE = "defaultPurse";
	public static final String PURSE = "Purse";
	public static final String MAX_PURSE_BALANCE = "maxPurseBalance";
	// PurAuthReq
	public static final String PURAUTHREQ = "PurAuthReq";
	public static final String PURAUTHREQ_ACCOUNT_PURSE_ID = "reqAccountPurseId";
	public static final String PURAUTHREQ_PURSE_NAME = "purseName";
	public static final String PURAUTHREQ_PURSE_CURRENCY = "purseCurrency";
	public static final String PURAUTHREQ_TRAN_AMT = "purseTranAmt";
	public static final String PURAUTHREQ_SKUCODE = "skuCode";

	public static final String PURSE_TYPE_ID = "purseTypeID";
	public static final String PUR_BAL = "PurBal";
	public static final String PURSE_TYPE = "purseType";
	public static final Object SPLIT_TENDER = "splitTender";
	public static final String PURAUTHRESP = "PurAuthResp";
	public static final String SKIP_CURRENCY_CHECK_IF_NULL = "skipCurrencyCheckIfNull";

	public static final String DEFAULT_PURSE_CURRENCY = "defaultPurseCurrency";
	public static final String RETAIL_PATRNER_REFERENCE_NUMBER = "RetailPartnerReferenceNumber";

	public static final String SPIL_ONE_USD_TRAN = "spilOneUsdTran";

	public static final String CCA_USER_ID = "UserID";
	public static final String CCA_PASSWORD = "Password";

	public static final String REDEEM_SPLIT_TENDER = "redeemSplitTender";
	public static final String REDEEMLOCK_SPLIT_TENDER = "redeemLockSplitTender";
	public static final String PURSE_REDEEM_SPLIT_TENDER = "purseRedeemSplitTender";
	public static final String PURSE_REDEEMLOCK_SPLIT_TENDER = "purseRedeemLockSplitTender";

	public static final String FUNCTION_CODE = "functionCode";
	public static final String CORRELATION_ID = "x-incfs-correlationid";
	public static final String CHANNEL = "x-incfs-channel";
	public static final String DATE = "x-incfs-date";
	public static final String PING = "Ping";
	public static final String TRANSACTION_AMOUNT_BUMP_UP = "usdOneBumpPumpTxn";

}