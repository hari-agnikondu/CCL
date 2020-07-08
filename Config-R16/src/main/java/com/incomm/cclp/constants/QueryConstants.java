/**
 * 
 */
package com.incomm.cclp.constants;

/**
 * QueryConstants class defines all the sql queries used by the Configuration
 * Service.
 * 
 * @author abutani
 *
 */
public class QueryConstants {

	/**
	 * Constants class should not be instantiated.
	 */
	private QueryConstants() {
		throw new IllegalStateException("Constants class");
	}

	/**
	 * common query constants for issuer
	 */

	public static final String SELECT_MERCHANTS = "select merchant from Merchant merchant ";

	public static final String DELETE_MERCHANTS = "delete from Merchant merchant ";

	public static final String SELECT_ISSUERS = "select issuer from Issuer issuer ";

	public static final String DELETE_ISSUERS = "delete from Issuer issuer ";

	/**
	 * redemption delay merchants
	 */
	public static final String SELECT_REDEMPTION_MERCHANTS = "select merchantRedemption from MerchantRedemption merchantRedemption ";
	/**
	 * common query constants for product
	 */
	public static final String SELECT_PRODUCTS = "select product from Product product ";
	public static final String DELETE_PRODUCTS = "delete from Product product ";

	/**
	 * ISSUER Queries
	 */

	public static final String GET_ALL_ISSUERS = SELECT_ISSUERS + "order by issuer.issuerName";
	public static final String DELETE_ISSUER = DELETE_ISSUERS + "where issuer.issuer_Id = :issuerId";
	public static final String GET_ISSUER_BY_NAME = SELECT_ISSUERS + "where UPPER(issuer.issuerName) like :issuerName";
	public static final String GET_ISSUER_BY_NAME_UNDERSCORE = SELECT_ISSUERS
			+ "where UPPER(issuer.issuerName) like :issuerName  ESCAPE '\\'";

	public static final String DELETE_ISSUER_BY_ID = DELETE_ISSUERS + " where issuer.issuer_Id = :issuerId";

	public static final String MAKE_ISSUER_INACTIVE_BY_ID = "update Issuer issuer set is_active=1 "
			+ "where issuer.issuer_Id = :issuerId";

	public static final String CARDRANGE_COUNT_BY_ISSUER = "select cardrange from CardRange cardrange where cardrange.issuer.issuerId= :issuerId";

	public static final String COUNT_BY_ISSUER = "select count(issuer.issuer_Id) from  Issuer issuer "
			+ "where issuer.issuer_Id = :issuerId";

	/* PARTNER Queries */
	public static final String GET_ALL_PARTNERS = "select partner from Partner partner where IS_ACTIVE='Y' order by partner.partnerName";
	public static final String DELETE_PARTNER = "delete from Partner partner " + "where partner.partnerId = :partnerId";
	public static final String GET_PARTNER_BY_NAME = "select partner from Partner partner "
			+ "where IS_ACTIVE='Y' AND UPPER(partner.partnerName) like :partnerName ESCAPE '\\'";
	public static final String REMOVE_PARTNER_BY_ID = "delete  from Partner partner " + "where partnerId =:partnerId";
	public static final String REMOVE_FROM_PARTNER_PURSE = "delete  from PartnerPurse partnerPurse " + "where partnerPurseID.partner.partnerId =:partnerId";
	public static final String REMOVE_FROM_PARTNER_CURRENCY = "delete  from PartnerCurrency partnerCurrency " + "where partnerCurrencyID.partner.partnerId =:partnerId";
	public static final String REMOVE_FROM_PARTNER_PROGRAM = "delete  from ProgramID programID " + "where partner.partnerId =:partnerId";
	public static final String GET_PRODUCT_COUNT_BY_PARTNER_ID = "select count(*) from Product product where product.partner_id =:partnerId";

	/* Card Range Queries Starts */
	public static final String GET_CARDRANGE = "select cardrange from CardRange cardrange";
	public static final String GET_CARDRANGE_BY_CARDRANGE_ID = "select cardrange from CardRange cardrange "
			+ "where cardrange.cardRangeId=:cardRangeId";
	public static final String UPDATE_CARDRANGE_STATUS = "update CARD_RANGE set STATUS=:status, CHECKER_REMARKS=:checkerDesc, LAST_UPD_USER=:lastUpdUser "
			+ "where CARD_RANGE_ID=:cardRangeId and STATUS=:newStatus";
	public static final String CARDRANGE_AVAIL_CHECK_CHECKDIGIT = " select case when flag > 0 then 'OVERLAP' else 'NO-OVERLAP' end status from(SELECT nvl(SUM ("
			+ " CASE WHEN " + "(to_number(:startRange || case when :checkDigit = 'Y' then '0' else '' end) "
			+ "BETWEEN "

			+ " to_number(1||PREFIX||start_card_nbr|| case when IS_CHECK_DIGIT_REQUIRED = 'Y' then '0' else '' end)"
			+ " AND to_number(1||PREFIX||end_card_nbr|| case when IS_CHECK_DIGIT_REQUIRED = 'Y' then '9' else '' end)) "

			+ " OR (" + " to_number(:endRange || case when :checkDigit = 'Y' then '9' else '' end)"

			+ " BETWEEN "
			+ " to_number(1||PREFIX||start_card_nbr || case when IS_CHECK_DIGIT_REQUIRED = 'Y' then '0' else '' end)"
			+ " AND to_number(1||PREFIX||end_Card_nbr|| case when IS_CHECK_DIGIT_REQUIRED = 'Y' then '9' else '' end)"
			+ ")"

			+ " OR ("
			+ "to_number(1||PREFIX||start_card_nbr|| case when IS_CHECK_DIGIT_REQUIRED = 'Y' then '0' else '' end)"

			+ " BETWEEN" + " to_number(:startRange || case when :checkDigit = 'Y' then '0' else '' end) and"
			+ " to_number(:endRange || case when :checkDigit = 'Y' then '9' else '' end)" + ") "

			+ "OR ("
			+ " to_number(1||PREFIX||end_card_nbr || case when IS_CHECK_DIGIT_REQUIRED='Y' then '9' else '' end) "
			+ "BETWEEN " + " to_number(:startRange  || case when :checkDigit = 'Y' then '0' else '' end) "
			+ " and to_number(:endRange || case when :checkDigit = 'Y' then '9' else '' end )) "
			+ " THEN 1 ELSE 0 END),0) flag" + "   FROM card_range where STATUS <>'REJECTED')";

	public static final String CARDRANGE_AVAIL_CHECK = " select case when flag > 0 then 'OVERLAP' else 'NO-OVERLAP' end status from(SELECT nvl(SUM ( CASE WHEN ((to_number(:startRange) BETWEEN to_number(1||PREFIX||start_card_nbr) AND to_number(1||PREFIX||end_card_nbr))"
			+ "OR (to_number(:endRange) BETWEEN to_number(1||PREFIX||start_card_nbr) AND to_number(1||PREFIX||end_Card_nbr))) OR ((to_number(1||PREFIX||start_card_nbr) BETWEEN to_number(:startRange) and to_number(:endRange)) "
			+ "OR (to_number(1||PREFIX||end_card_nbr) BETWEEN to_number(:startRange) and to_number(:endRange))) THEN 1 ELSE 0 END),0) flag   FROM card_range where STATUS <>'REJECTED')";
	public static final String GET_ACTIVE_ISSUERS = "select issuer from Issuer issuer where issuer.isActive=:activeStatus";
	public static final String GET_CARDRANGES_BY_ISSUERID = "select cardrange from CardRange cardrange where cardrange.issuer.issuerId=:issuerId and status='APPROVED'";

	/* Card Range Queries Ends */

	/* PRODUCT QUERIES */

	public static final String GET_ALL_PRODUCTS = SELECT_PRODUCTS + "order by product.productName";
	public static final String DELETE_PRODUCT = DELETE_PRODUCTS + "where product.product_Id = :productId";
	public static final String GET_PRODUCT_BY_NAME = SELECT_PRODUCTS
			+ "where UPPER(product.productName) like :productName";

	public static final String GET_PROGRAM_ID_BY_NAME = "select programID from ProgramID programID "
			+ "where UPPER(programID.programIDName) like : programIDName";

	public static final String GET_PRODUCT_BY_NAME_COPY = SELECT_PRODUCTS
			+ "where UPPER(product.productName) = :productName";
	public static final String DELETE_PRODUCT_BY_ID = DELETE_PRODUCTS + "where product.product_Id = :productId";
	public static final String MAKE_PRODUCT_INACTIVE_BY_ID = "update Product product set is_active=1 "
			+ "where product.product_Id = :productId";
	public static final String PRODUCT_COUNT = "select count(cardrange.card_range_Id) from CARD_RANGE cardrange "
			+ "where cardrange.issuer_Id = :issuerId";

	public static final String GET_ISSUER_NAMES = SELECT_ISSUERS;

	public static final String GET_PARTNER_NAMES = "select partner from Partner partner ";

	public static final String GET_PARENT_PRODUCT_NAMES = "select product.productName from Product product ";

	public static final String GET_PARENT_PRODUCTS_LIST = "select new Product(product.productId,product.productName) from Product product ";

	public static final String UPDATE_PRODUCT_ATTRIBUTES = "update PRODUCT set ATTRIBUTES=:attributes "
			+ "where PRODUCT_ID=:productId";
	public static final String UPDATE_ATTRIBUTES = "update Product product set product.attributes =:prodAttributes "
			+ "where product.productId =:productId";

	public static final String GET_RULE_SET_LIST = "select ruleset from RuleSet ruleset";

	public static final String UPDATE_LIMIT_ATTRIBUTES = "update product set attributes = :attributes where product_id=:product_id";

	public static final String GET_ALL_PARENT_PRODUCTS = "select * from  product";
	public static final String GET_ALL_RETAIL_PRODUCTS = "select * from  product p where p.Attributes.Product.productType='Retail'";
																																		

	public static final String GET_ALL_TRANSACTIONS = "select dc.channel_code,dc.channel_short_name,dc.channel_name,t.transaction_short_name ,t.transaction_description,t.is_financial,dct.ERIF,dct.MRIF ,dct.channel_code||dct.transaction_code||dct.message_type,dct.FLEX_DESCRIPTION \r\n"
			+ "from delivery_channel dc,transaction t, delivery_channel_transaction dct \r\n"
			+ "where dct.channel_code=dc.channel_code and dct.transaction_code=t.transaction_code AND dct.message_type='0200'";

	public static final String GET_CHILD_PRODUCTS_LIST_LIMITS = "select * from  product start with product_id = :product_Id CONNECT BY PRIOR product_id = parent_product_id";
	public static final String GET_PARENT_PRODUCTS_LIST_LIMITS = "select * from  product start with product_id = :product_Id CONNECT BY PRIOR parent_product_id = product_id";

	public static final String GET_PACKAGE_IDS = "select PACKAGE_ID from PACKAGE_DEFINITION ";

	public static final String GET_CCF_VERSION = " SELECT VERSION_NAME FROM CCF_CONF_VERSION ";

	public static final String GET_CARD_RANGE = "select cardrange from CardRange cardrange";

	public static final String GET_PURSE_TYPE = " SELECT PURSE_TYPE FROM PURSE ";

	public static final String GET_PROGRAM_IDS_BY_PARTNER_ID = "select programId from ProgramID programId where programId.partner.partnerId=:partnerId ";

	/**
	 * Redemption Delay Queries
	 */

	public static final String ADD_REDEMPTION_DELAY = "INSERT INTO REDEMPTION_DELAY(PRODUCT_ID,MERCHANT_ID,START_TIME_DISPLAY,END_TIME_DISPLAY,REDEMPTION_DELAY_TIME,INS_USER,INS_DATE,LAST_UPD_USER,LAST_UPD_DATE) VALUES(?,?,?,?,?,?,?,?,?)";

	public static final String DELETE_REDEMPTION_DELAY = "DELETE FROM RedemptionDelay redemptionDelay where redemptionDelay.primaryKey.product.productId =";

	/* package */

	public static final String GET_ALL_PACKAGES = "select package from PackageDefinition package order by package.packageId  ";

	public static final String GET_PACKAGES_BY_ID = "select package from PackageDefinition package where packageId in (:packageId) order by package.packageId  ";

	/* Program ID */

	public static final String GET_ALL_PROGRAM_IDS = "select programID from ProgramID programID order by programID.prgmId ";

	public static final String GET_PROGRAMIDS_BY_NAME = "select programID from ProgramID programID "
			+ "where UPPER(programID.programIDName) like UPPER(:programIDName)";

	/* purse starts */
	public static final String GET_ALL_PURSES = "select purse from Purse purse";
	public static final String GET_PARTNER_BY_PURSE ="select count(partnerpurse.partner_id) from partner_purse partnerpurse where purse_id =:purseId";
	public static final String GET_PRODUCT_BY_PURSE = "select productpurse from ProductPurse productpurse where purse_id=:purseId";
	public static final String GET_PURSES_BY_IDS = "select purse from Purse purse where purse.purseId in (:purseId) ";
	public static final String GET_CURRENCY_CODE_BY_IDS = "select currencyCode from CurrencyCode currencyCode where currencyCode.currencyCode=:purseId ";


	/* purse ends */

	/* RuleSet master table starts */
	public static final String GET_PRODUCT_RULESET = "select productRuleSet from ProductRuleSet"
			+ " productRuleSet where id.productId=:productId";

	public static final String DELETE_PRODUCT_RULESET = "DELETE FROM PRODUCT_RULESET" + " where PRODUCT_ID=:productId";

	/* RuleSet master table ends */

	/* Card status starts */
	public static final String GET_ACTIVE_CARD_STATUS = "select cardStatus from CardStatus cardStatus where cardStatus.screenConfig = 'Y'";
	/* Card status ends */

	public static final String GET_ALERT_MESSAGES = "select alert from Alert alert ";

	public static final String DELETE_PRODUCT_ALERT_BY_PRODUCT_ID = "delete from product_alert where product_id=:productId";

	/* Master table queries starts */
	public static final String GET_ALL_PURSE_TYPE = "select pursetype from PurseType pursetype";
	public static final String GET_ALL_CURRENCY_CODE = "select currencycode from CurrencyCode currencycode";
	public static final String GET_ALL_PURSE = "select purse from Purse purse";
	/* Master table queries ends */

	/* User Management */

	public static final String GET_ALL_USERS = "select user from ClpUser user";

	public static final String GET_USER_BY_USER_NAME = "select user from ClpUser user "
			+ "where UPPER(user.userLoginId) = :userName and access_status='ACTIVE'";

	public static final String GET_USER_BY_USER_ID = "select user from ClpUser user " + "where user.userId = :userId";

	public static final String UPDATE_LAST_LOGIN_TIME_FOR_USER = "update Clp_User "
			+ "set last_Login_Time = CURRENT_TIMESTAMP " + "where user_id = :userId";
	/* by nawaz */

	public static final String GET_CARD_RANGE_BY_CARD_ID = "select cardrange from CardRange cardrange where cardrange.cardRangeId in (:cardRangeId) ";

	/* Group Access Starts */
	public static final String GROUP_ACCESS_PRODUCTS_LIST = "select groupAccessProduct from GroupAccessProduct groupAccessProduct";

	public static final String GROUP_ACCESS_LIST = "select groupAccess from GroupAccess groupAccess ";

	public static final String GROUP_ACCESS_PARTNERS_LIST = "select groupAccessPartner from GroupAccessPartner groupAccessPartner order by groupAccessPartnerID.partner.partnerName";

	public static final String GROUP_ACCESS_PARTNERS_LIST_BY_ACCESSID = "select groupAccessPartner from GroupAccessPartner groupAccessPartner  where groupAccessPartner.groupAccessPartnerID.groupAccess.groupAccessId=:groupAccessId order by groupAccessPartner.groupAccessPartnerID.partner.partnerName";

	public static final String DELETE_GROUP_ACCESS_PRODUCTS = "DELETE FROM GROUP_ACCESS_PRODUCT ";

	public static final String GET_PRODUCTS_BY_ACCESSID = "select PRODUCT_ID,PRODUCT_NAME from product where PARTNER_ID in (select partner_id from group_access_partner where group_access_id=:groupAccessId)";
	public static final String GET_PARTNERS_LIST_BY_ACCESSID = "select par.PARTNER_ID,par.partner_name from product prd,group_access_product grp,PARTNER par where prd.partner_id=grp.partner_id and prd.product_id=grp.product_id and par.partner_id=prd.partner_id and group_access_id=:groupAccessId";
	/* Group Access Ends */
	public static final String GET_GLOBAL_PARAMETERS = "select attributedefinition from AttributeDefinition attributedefinition "
			+ "where attributedefinition.attributeGroup=:attributeGroup";

	public static final String GET_ALL_JOBS = "SELECT sch.jobId,sch.jobName FROM ScheduleJob sch";
	public static final String GET_USER_MAIL_LIST = "select USER_ID,USER_NAME,USER_EMAIL from CLP_USER where STATUS ='APPROVED'  order by USER_NAME";
	public static final String UPDATE_SCHEDULER_CONFIG = "UPDATE PROCESS_SCHEDULE SET SCHEDULE_DAYS=:days,START_HOUR=:startHour,START_MIN=:startMin,RETRY_CNT=:retryCnt,PROCESS_INTERVAL=:processInt,SCHEDULER_STAT=:schedulerStat,MAIL_SUCCESS=:mailSuccess,MAIL_FAIL=:mailFailure,PROCRETRY_DATE=SYSDATE,DAYOF_MONTH=:monthDays,MULTIRUN_INTERVAL=:multiInt,MULTIRUN_INTERVAL_TYPE=:multiType,MULTIRUN_FLAG=:multiFlag,PROCINTERVAL_TYPE=:procIntType WHERE PROCESS_ID=:procID";

	public static final String GET_ALL_SERVERS = "select distinct(sc.PHYSICAL_SERVER),sc.MANAGED_SERVER,mfcs.LISTEN_PORT,sc.SCHEDULER_RUNNING from SCHEDULER_RUNNING sc,MENU_FILE_CREATION_STATUS mfcs where sc.PHYSICAL_SERVER=mfcs.PHYSICAL_SERVER";
	public static final String SERVER_RUNNING_CHECK = "select count(*) from SCHEDULER_RUNNING where SCHEDULER_RUNNING=:SCHEDULER_RUNNING";
	public static final String UPDATE_SERVER = "update SCHEDULER_RUNNING set SCHEDULER_RUNNING=:SCHEDULER_RUNNING where PHYSICAL_SERVER=:PHYSICAL_SERVER";

	/* PRM Starts */

	public static final String UPDATE_PRM_ATTRIBUTES_MRIF_IN = "update DELIVERY_CHANNEL_TRANSACTION dct set dct.MRIF='E' "
			+ "where dct.CHANNEL_CODE||dct.TRANSACTION_CODE||dct.MESSAGE_TYPE IN :MRIFLIST";

	public static final String UPDATE_PRM_ATTRIBUTES_MRIF_NOTIN = "update DELIVERY_CHANNEL_TRANSACTION dct set dct.MRIF='D' "
			+ "where dct.CHANNEL_CODE||dct.TRANSACTION_CODE||dct.MESSAGE_TYPE NOT IN :MRIFLIST";

	public static final String UPDATE_PRM_ATTRIBUTES_ERIF_IN = "update DELIVERY_CHANNEL_TRANSACTION dct set dct.ERIF='E' "
			+ "where dct.CHANNEL_CODE||dct.TRANSACTION_CODE||dct.MESSAGE_TYPE IN :ERIFLIST";

	public static final String UPDATE_PRM_ATTRIBUTES_ERIF_NOTIN = "update DELIVERY_CHANNEL_TRANSACTION dct set dct.ERIF='D' "
			+ "where dct.CHANNEL_CODE||dct.TRANSACTION_CODE||dct.MESSAGE_TYPE NOT IN :ERIFLIST";

	public static final String UPDATE_ALL_PRM_ATTRIBUTES = "update DELIVERY_CHANNEL_TRANSACTION dct set dct.ERIF='D' , dct.MRIF='D' ";

	public static final String DISABLE_ALL_ERIF_PRM_ATTRIBUTES = "update DELIVERY_CHANNEL_TRANSACTION dct set dct.ERIF='D'";

	public static final String DISABLE_ALL_MRIF_PRM_ATTRIBUTES = "update DELIVERY_CHANNEL_TRANSACTION dct set dct.MRIF='D'";
	/* PRM ENDS */
	/* Transaction Flex */
	public static final String UPDATE_TRANSACTION_FLEX_DESCRIPTION = "Update DELIVERY_CHANNEL_TRANSACTION Set FLEX_DESCRIPTION=:value where CHANNEL_CODE||TRANSACTION_CODE||MESSAGE_TYPE=:key ";

	/* Attribute Definition */
	public static final String GET_ALL_ATTRIBUTES = "select attributeDefinition from AttributeDefinition attributeDefinition where attributeDefinition.attributeGroup not in "
			+ "('Global Parameters','FULFILMENT_SHIPMENT','Card','Environment')";
	public static final String GET_ALL_RUNNING_JOBS = "SELECT a.process_name job_name,  DECODE (PROC_RUNNING, 'Y', 'Running', 'N', 'Not Running'),  \r\n"
			+ "decode(b.NEXT_RUN,null,'-',to_char(b.NEXT_RUN, 'dd/mm/yyyy HH24:MI:SS')) \r\n"
			+ "FROM PROCESS_SCHEDULE a, PROCESS_STATUS b  \r\n"
			+ "WHERE a.process_id = b.process_id  order by b.process_id";

	public static final String GET_ALL_DELIVERY_CHANNELS = "select channel_code, channel_name,instrument_type from delivery_channel where instrument_type is not null";

	public static final String GET_BLOCKLIST_COUNT_BY_INS_ID = "select count(*) from BlockList block_list where block_list.instrumentId =:instrumentId";

	public static final String GET_BLOCKLIST_BY_ID = "select block_list from BlockList block_list where block_list.channelCode=:channelCode";

	public static final String GET_ALL_BLOCKLIST = "select blockList from BlockList blockList";
	/* Group */
	public static final String REMOVE_BLOCKLIST_BY_INSID = "delete from BlockList blockList "
			+ "where blockList.instrumentId in :instrumentIds";

	public static final String GET_USER_BY_NAME_UNDERSCORE = "select user from ClpUser user "
			+ "where UPPER(user.userName) like :userName  ESCAPE '\\'";

	public static final String GET_USER_BY_NAME = "select user from ClpUser user "
			+ "where UPPER(user.userName) like :userName";

	public static final String GET_GROUP_BY_NAME = "select grp from Group grp where UPPER(grp.groupName) like :groupName ESCAPE '\\'";
	public static final String GET_ALL_GROUPS = "select group from Group group ";
	public static final String UPDATE_GROUP_STATUS = "update CLP_GROUP set STATUS=:status,CHECKER_REMARKS=:checkerDesc,LAST_UPD_USER=:lastUpdUser "
			+ "where GROUP_ID=:groupId and STATUS=:newStatus";

	/* ROLE Queries */
	public static final String GET_ALL_ROLES = "select role from Role role";

	public static final String GET_ROLE_BY_NAME = "select role from Role role"
			+ " where UPPER(role.roleName) like :roleName ESCAPE '\\' ";
	public static final String GET_ENTITY_PERMISSION = "select ENTITY,OPERATION, PERMISSION_ID from permission where ENTITY is not null and OPERATION is not null";

	/* MERCHANT QUERIES */

	public static final String GET_ALL_MERCHANTS = SELECT_MERCHANTS + "order by merchant.merchantName";

	public static final String GET_MERCHANT_BY_NAME = SELECT_MERCHANTS
			+ "where UPPER(merchant.merchantName) like :merchantName";
	public static final String GET_MERCHANT_BY_NAME_UNDERSCORE = SELECT_MERCHANTS
			+ "where UPPER(merchant.merchantName) like :merchantName  ESCAPE '\\'";

	public static final String DELETE_MERCHANT_BY_ID = DELETE_MERCHANTS + " where merchant.merchant_Id = :merchantId";

	public static final String COUNT_BY_MERCHANT = "select count(merchant.merchant_Id) from  Merchant merchant "
			+ " where merchant.merchant_Id = :merchantId";

	public static final String COUNT_BY_USER = "select count(user_Id) from  Clp_User" + " where user_Id = :userId";
	public static final String COUNT_BY_USER_LOGINID = "select count(user_login_id) from  Clp_User"
			+ " where user_login_id = :userLoginId";
	public static final String COUNT_BY_GROUP = "select count(group_id) from user_group_temp where group_id=:groupId";

	/* Fulfillment Qry */

	public static final String GET_FULFILLMENT_ID_CNT = "select COUNT(FULFILLMENT_VENDOR.FULFILLMENT_VENDOR_ID) FROM FULFILLMENT_VENDOR "
			+ "WHERE FULFILLMENT_VENDOR_ID = :fulfillmentID";

	public static final String GET_ALL_FULFILLMENTS = "select fulFillmentVendor from FulFillmentVendor fulFillmentVendor order by fulFillmentVendor.fulFillmentName";

	public static final String GET_FULFILLMENT_BY_NAME = "select fulFillmentVendor from FulFillmentVendor fulFillmentVendor "
			+ "where UPPER(fulFillmentVendor.fulFillmentName) like :fulFillmentName";

	public static final String REMOVE_FULFILLMENT_BY_ID = "delete from FulFillmentVendor fulFillmentVendor "
			+ "where FULFILLMENT_VENDOR_SQ_ID =:fulFillmentSEQID";

	public static final String GET_FULFILLMENT_SHIPMENT_ATTRIBUTE = "select ATTRIBUTE_VALUE,ATTRIBUTE_NAME from "
			+ "SHIPMENT_ATTRIBUTE where ATTRIBUTE_GROUP ='FULFILMENT_SHIPMENT'";

	public static final String GET_FULFILLMENT_PKG_ID_CNT = "select COUNT(FULFILLMENT_VENDOR_ID) FROM package_definition "
			+ "WHERE FULFILLMENT_VENDOR_ID = :fulfillmentID";

	/* CCF Qry */

	public static final String GET_CCF_ID_CNT = "select COUNT(CCF_KEY.VERSION_ID) FROM CCF_KEY "
			+ "WHERE VERSION_ID = :versionID";
	public static final String DELETE_CCF_DETAIL_BY_ID = "delete from CCFConfDetail ccfConfig "
			+ " where VERSION_NAME = :versionID";

	public static final String GET_ALL_CCFPARAM = "SELECT PARAM_SECTION,PARAM_ID,PARAM_DESCRIPTION FROM ccf_format_param  \r\n"
			+ "order by case WHEN PARAM_SECTION = 'H' THEN 1  when PARAM_SECTION = 'D' then 2  ELSE 3  end";

	public static final String GET_CCF_VERSION_DTLS = "select ccfconfig from CCFConfDetail ccfconfig where ccfconfig.versionName = :version_Name order by case WHEN ccfconfig.recordType = 'H' THEN 1 \r\n"
			+ "when ccfconfig.recordType = 'D' then 2  ELSE 3  end,ccfconfig.dataSeqNo";

	public static final String GET_ALL_CCFKEY = "select key_code key_code,key_code keyValue from ccf_key ";

	// PACKAGE LIST

	public static final String GET_PACKAGE_SHIPMENT_ATTRIBUTE = "select ATTRIBUTE_VALUE,DESCRIPTION from "
			+ "SHIPMENT_ATTRIBUTE where ATTRIBUTE_GROUP ='PACKAGE_SHIPMENT'";

	public static final String GET_PACKAGE_LIST = "select FULFILLMENT_VENDOR_ID,FULFILLMENT_VENDOR_ID ||'-'||FULFILLMENT_VENDOR_NAME from fulfillment_vendor";
	public static final String DELETE_PACKAGE_ATTRIBUTES_BY_ID = "delete from package_attributes packageattributes "
			+ " where PACKAGE_ID = :packageId";

	public static final String GET_PACKAGE_ID_DRPDOWN = "select package_id key_code,package_id keyValue from package_definition ";

	public static final String GET_PACKAGE_ATTR_LIST = "select ATTRIBUTE_NAME,nvl(ATTRIBUTE_VALUE,' ') from package_attributes where PACKAGE_ID =:packageId ";

	public static final String GET_PACKAGEDESC_BY_NAME = "select packageDefinition from PackageDefinition packageDefinition "
			+ "where packageDefinition.isActive ='Y' and UPPER(packageDefinition.description) like UPPER(:description)";

	public static final String GET_PACKAGE_ID_CNT = "select COUNT(PACKAGE_DEFINITION.PACKAGE_ID) FROM PACKAGE_DEFINITION "
			+ "WHERE PACKAGE_ID = :packageId";

	public static final String GET_ALL_RULES = "SELECT rule FROM Rule rule ORDER BY rule.ruleId ASC";

	public static final String GET_RULE_BY_NAME = "select rule from Rule rule "
			+ "where UPPER(rule.ruleName) like :ruleName ESCAPE '\\'";

	public static final String GET_ALL_RULECONFIG = "SELECT config_name,config_value FROM RULE_CONFIG ";

	public static final String GET_TRANSACTION_BY_TXN_IDENTIFIER = "select CHANNEL_NAME,TRANSACTION_DESCRIPTION from DELIVERY_CHANNEL_TRANSACTION,TRANSACTION,delivery_channel  where  DELIVERY_CHANNEL_TRANSACTION.TRANSACTION_CODE= TRANSACTION.TRANSACTION_CODE and DELIVERY_CHANNEL_TRANSACTION.CHANNEL_CODE = delivery_channel.CHANNEL_CODE and DELIVERY_CHANNEL_TRANSACTION.TRANSACTION_IDENTIFIER = :txnTypeId";

	/* Location Starts */
	public static final String GET_ALL_LOCATIONS = "select location from Location location";
	public static final String COUNT_BY_LOCATION = "select count(location_id) from LOCATION_INVENTORY where location_id=:locationId";

	public static final String MERCHANT_PRODUCTS_LIST = "select merchantProduct from MerchantProduct merchantProduct";

	public static final String GET_ALL_COUNTRY_CODE = "select countrycode from CountryCode countrycode";

	public static final String GET_ALL_STATE_CODE = "select statecode from StateCode statecode";

	/* Menu allocation starts */
	public static final String GET_ALL_MENUS = "select clpResource from ClpResource clpResource order by order";

	/* Menu allocation ends */

	/* Ruleset starts */

	public static final String GET_RULESET_BY_NAME = "select ruleSet from RuleSet ruleSet "
			+ "where UPPER(ruleSet.ruleSetName) like :ruleSetName ESCAPE '\\'";

	public static final String GET_RULE_SET_SEQ_NO = "select " + "RULESET_RULESET_ID_SEQ.NEXTVAL from dual ";

	public static final String DELETE_RULESET_DETAIL_BY_ID = "delete from RULE_RULESET  "
			+ " where RULESET_ID = :ruleSetId";

	public static final String GET_RULESET_ID_CNT = "select COUNT(RULESET_ID) FROM RULESET "
			+ "WHERE RULESET_ID = :ruleSetId";

	public static final String GET_ALL_RULEDETAILS = "SELECT DISTINCT R.RULE_NAME,R.RULE_ID FROM RULE R,RULE_RULESET RS "
			+ " WHERE R.RULE_ID = RS.RULE_ID " + " AND RS.RULESET_ID = :ruleSetId";

	/* Ruleset ends */
	/**
	 * MERCHANT REDEMPTION DELAY
	 */
	public static final String GET_MERCHANT_REDEMPTION_BY_NAME_UNDERSCORE = SELECT_REDEMPTION_MERCHANTS
			+ "where UPPER(merchantRedemption.merchantName) like :merchantName  ESCAPE '\\'";

	public static final String GET_MERCHANT_REDEMPTIONBY_NAME = SELECT_REDEMPTION_MERCHANTS
			+ "where UPPER(merchantRedemption.merchantName) like :merchantName";

	public static final String GET_ALL_REDEMPTION_MERCHANTS = SELECT_REDEMPTION_MERCHANTS
			+ "order by merchantRedemption.merchantName";

	public static final String REDEMPTION_DELAY = "select redemptionDelay from RedemptionDelay redemptionDelay";

	/* Customer Profile */
	public static final String GET_CARD_ATTRIBUTES = "select attributeDefinition from AttributeDefinition attributeDefinition where attributeDefinition.attributeGroup in('Transaction Fees','Maintenance Fees','Monthly Fee Cap','Limits','Card')";

	public static final String CREATE_CARD_ATTRIBUTES = "update CARD set ATTRIBUTES=:attributes, prfl_code=profile_code_seq.NEXTVAL "
			+ "where PROXY_NUMBER=:proxyNumber";

	public static final String GET_CUSTOMER_PROFILE_BY_ID = "select clp_transactional.fn_dmaps_main(card_num_encr) as card_number,proxy_number,account_number, to_clob(attributes) from card c JOIN  account act  ON  act.account_id=c.account_id where prfl_code=:profileId and in_use='Y' ";

	public static final String GET_CUSTOMER_PROFILES_LIST = "select prfl_code,account_number,proxy_number,clp_transactional.fn_dmaps_main(card_num_encr) from card c JOIN  account act  ON  act.account_id=c.account_id where c.in_use='Y' and  ";

	public static final String GET_CUSTOMER_PROFILE_CODE_COUNT = "select NVL(count(prfl_code),0) from card where proxy_number=:proxyNumber";

	public static final String UPDATE_CARD_ATTRIBUTES = "update CARD set ATTRIBUTES=:attributes "
			+ "where prfl_code=:profileId";
	public static final String DELETE_CUSTOMER_PROFILE_BY_ID = "update card set prfl_code=null,attributes=null where prfl_code=:profileId";

	public static final String GET_ATTRIBUTES_BY_TYPE = " SELECT c.attributes FROM CARD c JOIN  ACCOUNT act  ON  act.account_id=c.account_id   WHERE   (:accountNumber ='-1'  or act.account_number =:accountNumber )  and (:proxyNumber ='-1' or c.proxy_number =:proxyNumber)  and (:cardNumber ='-1' or c.CARD_NUM_ENCR = clp_transactional.fn_emaps_main(:cardNumber)) AND c.prfl_code IS NOT NULL AND c.in_use='Y'";

	public static final String GET_CARDS_BY_ACCOUNTNUMBER = "select clp_transactional.fn_dmaps_main(CARD_NUM_ENCR),cs.status_desc FROM CARD c JOIN ACCOUNT act ON act.account_id=c.account_id,card_status cs WHERE act.account_number =:accountNumber and c.card_status=cs.status_code order by c.date_of_activation";

	public static final String GET_PACKAGE_BY_NAME_UNDERSCORE =

			"select packageDefinition from PackageDefinition packageDefinition"
					+ "where packageDefinition.isActive ='Y' UPPER(packageDefinition.description) like UPPER(:description)  ESCAPE '\\'";

	public static final String GET_ALL_CARDSTATUS = "select STATUS_CODE, STATUS_DESC from CARD_STATUS";

	public static final String GET_GROUPS = "SELECT group from Group group";
		
	public static final String GET_ALL_TRANSACTIONS_BY_CHANNEL_NAME = "select dc.channel_code,dc.channel_short_name,dc.channel_name,t.transaction_short_name ,t.transaction_description,t.is_financial,dct.ERIF,dct.MRIF ,dct.channel_code||dct.transaction_code||dct.message_type,dct.FLEX_DESCRIPTION  from delivery_channel dc,transaction t, delivery_channel_transaction dct where dct.channel_code=dc.channel_code and dct.transaction_code=t.transaction_code AND dct.message_type='0200' and dc.channel_short_name=:CHANNEL_SHORT_NAME";

	public static final String GET_ALL_TRANSACTIONS_BY_CHANNEL_NAME_TXN_NAME = "select dc.channel_code,dc.channel_short_name,dc.channel_name,t.transaction_short_name ,t.transaction_description,t.is_financial,dct.ERIF,dct.MRIF ,dct.channel_code||dct.transaction_code||dct.message_type,dct.FLEX_DESCRIPTION  from delivery_channel dc,transaction t, delivery_channel_transaction dct where dct.channel_code=dc.channel_code and dct.transaction_code=t.transaction_code AND dct.message_type='0200' and dc.channel_short_name=:CHANNEL_SHORT_NAME and t.transaction_short_name=:TRANSACTION_SHORT_NAME";

	public static final String GET_PRODUCT_BY_PROGRAM_ID = "select * from Product where program_id=:programId";

	public static final String GET_AUTHENTICATION_TYPES = "SELECT authenticationType from AuthenticationType authenticationType";

	public static final String GROUP_ACCESS_PRODUCTS_LIST_COUNT = "select count(*) from GROUP_ACCESS_PRODUCT where product_Id=:productId and partner_id = :partnerId";

	public static final String GET_PRODUCTID_BY_UPC = "select product_id from product po where po.attributes.Product.formFactor = 'Digital' and (po.attributes.Product.retailUPC=:upc or po.attributes.Product.b2bUpc=:upc)";

	public static final String GET_SUPPOTED_PURSE = "SELECT pp.product_id AS PRODUCT_ID, pp.purse_id AS PURSE_ID, pp.is_default AS IS_DEFAULT, p.CURRENCY_CODE AS CURRENCY_ID, CD.CURRENCY_CODE as currency_code,p.UPC AS UPC,CD.MINOR_UNITS as minor_units FROM product_purse pp INNER JOIN purse p ON pp.purse_id = p.purse_id INNER JOIN purse_type pt ON p.purse_type_id = pt.purse_type_id left outer join  CURRENCY_CODE CD  on CD.CURRENCY_ID=p.CURRENCY_CODE ";

	public static final String GET_GROUP_ACCESS_PRODS_BY_PRODUCTID = "select Gap.Product_Id,Gap.Product_Id||'_'||P.Mdm_Id,gap.partner_party_type from group_access_product gap,partner p,Group_Access ga where Ga.Group_Access_Id=Gap.Group_Access_Id and Gap.Partner_Id=P.Partner_Id ";

	public static final String GET_REDEMPTION_DELAY = "SELECT RD.PRODUCT_ID ||'_'||UPPER(RDM.MERCHANT_NAME) as key,RD.PRODUCT_ID as productId,RDM.MERCHANT_NAME as merchantName,RD.MERCHANT_ID as merchantId,RD.START_TIME_DISPLAY as startTime,RD.END_TIME_DISPLAY as endTime,RD.REDEMPTION_DELAY_TIME as delayTime FROM REDEMPTION_DELAY RD,REDEMPTION_DELAY_MERCHANT RDM WHERE RD.MERCHANT_ID=RDM.MERCHANT_ID ";

	public static final String UPDATE_THREAD_POOL_PARAMETERS = "UPDATE attribute_definition SET attribute_value = CASE attribute_name WHEN 'chunkSize' THEN decode(:chunkSize,'0',attribute_value,:chunkSize) WHEN 'threadPoolSize' THEN decode(:threadPoolSize,'0',attribute_value,:threadPoolSize) WHEN 'maxThreadPoolSize' THEN decode(:maxThreadPoolSize,'0',attribute_value,:maxThreadPoolSize) ELSE attribute_value END WHERE attribute_name IN('chunkSize', 'threadPoolSize','maxThreadPoolSize') and attribute_group='Global Parameters'";

	public static final String GET_PROD_ATTRIBUTES_OVERRIDE = "select ATTRIBUTE_GROUP,ATTRIBUTE_KEY,ATTRIBUTE_VALUE from product_attributes_override  where PRODUCT_ID=:productId and STATUS='N'";
	
	public static final String GET_DISTINCT_PRODUCT_ID = "select distinct PRODUCT_ID from product_attributes_override  where STATUS='N'";
	
	public static final String UPDATE_PROD_ATTRIBUTES_FLAG = "update product_attributes_override  set  STATUS='Y' where PRODUCT_ID=:productId";
	
	public static final String GET_PRODUCT_CURRENCY = "SELECT p.product_id||'_'||CD.CURRENCY_CODE as key,CD.CURRENCY_CODE as currency_code FROM PRODUCT_CURRENCY p  join  CURRENCY_CODE CD  on CD.CURRENCY_ID=p.CURRENCY_ID";

	public static final String GET_PARTNER_CURRENCY = "select cc.currency_id,cc.currency_code from CURRENCY_CODE cc ,partner_currency pc   where cc.currency_id =pc.currency_id and pc.PARTNER_ID=:PARTNER_ID";

	public static final String GET_ALL_CURRENCY_CODE_BY_PARTNER = "select c.CURRENCY_ID,c.CURRENCY_CODE,c.CURRENCY_DESCRIPTION,c.MINOR_UNITS from clp_transactional.CURRENCY_CODE c,PARTNER_CURRENCY p where c.CURRENCY_ID =p.CURRENCY_ID and p.PARTNER_ID=:PARTNER_ID";

	public static final String GET_CURRENCY_COUNT_BY_PARTNER_ID = "select distinct currency_id  from clp_configuration.product_currency pc ,clp_configuration.product p where pc.product_id=p.product_id and p.partner_id=:partnerId";
	
	public static final String GET_PRODUCTS_LINKED_WITH_PARTNER_ID_AND_PARTNER_ID = "SELECT p.product_Id,p.product_Name FROM product p WHERE " + 
			"        EXISTS (" + 
			"            SELECT 1 FROM product_purse new " + 
			"            WHERE new.purse_id IN (" + 
			"                        SELECT cur.purse_id FROM product_purse cur " + 
			"                        WHERE cur.product_id =:productId AND cur.is_default = 'Y' " + 
			"                    )" + 
			"            AND new.is_default = 'Y' AND new.product_id = p.product_id " + 
			"        ) " + 
			"    AND NOT " + 
			"        EXISTS  " + 
			"         ( " + 
			"            SELECT purse_id FROM product_purse cur WHERE product_id =:productId " + 
			"            MINUS SELECT purse_id FROM product_purse new " + 
			"            WHERE product_id = p.product_id " + 
			"        ) " + 
			"\r\n" + 
			"    AND ( EXISTS ( SELECT 1 FROM product cur " + 
			"                WHERE cur.attributes.\"Product\".\"internationalSupported\" = 'Disable' " + 
			"                AND cur.product_id =:productId AND p.attributes.\"Product\".\"internationalSupported\" = 'Disable' " + 
			"            ) " + 
			"        OR (  " + 
			"            EXISTS ( SELECT 1 FROM product cur " + 
			"                    WHERE cur.product_id =:productId AND cur.attributes.\"Product\".\"internationalSupported\" = 'Enable' " + 
			"                    AND p.attributes.\"Product\".\"internationalSupported\" = 'Enable' " + 
			"                ) " + 
			"            AND NOT " + 
			"                EXISTS  " + 
			"                 ( " + 
			"                    SELECT currency_id FROM product_currency cur " + 
			"                    WHERE product_id =:productId MINUS SELECT currency_id FROM product_currency new " + 
			"												WHERE product_id = p.product_id " + 
			"                ) " + 
			"        ) " + 
			"    ) AND " + 
			"        EXISTS (  " + 
			"            SELECT 1 FROM product cur " + 
			"            WHERE cur.product_id =:productId AND cur.partner_id = p.partner_id)";
    
	public static final String GET_PURSE_COUNT_BY_PARTNER_ID = "select distinct purse_id  from clp_configuration.product_purse pp ,clp_configuration.product p where pp.product_id=p.product_id and p.partner_id=:partnerId";
	
	public static final String GET_PRODUCT_PURSE_ATTRIBUTES = "select attributes from Product_Purse where product_id=:productId and purse_id=:purseId";
	
	public static final String UPDATE_PRODUCT_PURSE_ATTRIBUTES = "update PRODUCT_PURSE set ATTRIBUTES=:attributes where PRODUCT_ID=:productId and PURSE_ID=:purseId";
	
	
	public static final String GET_PURSE_ATTRIBUTES = "select purse from Purse purse where purse_id in (:purseId)";

	public static final String GET_PRODUCT_PURSE = "select * from Product_Purse where product_id=:productId and purse_id=:purseId";
	
	public static final String GET_PRODUCT_PURSE_BY_ID = "select productPurse from  ProductPurse productPurse where productPurse.primaryKey.product.productId=:productId ";
	
	public static final String GET_PURSE_BY_PRODUCT_ID = "select p.purse_id from product_purse p, product_purse pp , purse pu , purse_type pt where  p.purse_id=pp.purse_id AND p.purse_id= pu.purse_id and pu.purse_type_id = pt.purse_type_id  AND p.PRODUCT_ID=:PRODUCT_ID AND pp.product_id=:PARENT_PRODUCT_ID  ";
	
	public static final String GET_DISTINCT_PRODUCT_PURSE_ID = "select distinct PRODUCT_ID from prod_purse_atribute_ovrride  where STATUS='N'";
	
	public static final String GET_PROD_PURSE_ATTRIBUTES_OVERRIDE = "select PURSE_ID,ATTRIBUTE_GROUP,ATTRIBUTE_KEY,ATTRIBUTE_VALUE from prod_purse_atribute_ovrride  where PRODUCT_ID=:productId and STATUS='N'";

	public static final String UPDATE_PROD_PURSE_ATTRIBUTES_FLAG = "update prod_purse_atribute_ovrride set  STATUS='Y' where PRODUCT_ID=:productId and PURSE_ID=:purseId and ATTRIBUTE_GROUP=:attributeGroup and ATTRIBUTE_KEY=:attributeKey";	
	
	public static final String UPDATE_ACCESS_STATUS = "update Clp_user set access_status=:accessStatus where user_id=:userId";
	
}
