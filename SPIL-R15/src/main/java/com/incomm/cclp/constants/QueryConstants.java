/**
 * 
 */
package com.incomm.cclp.constants;

/**
 * QueryConstants class defines all the sql queries used by the Spil Service.
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

	public static final String GET_CVV_SUPPORTED_FLAG = "SELECT  po.attributes.Product.cvvSupported cvvSupported FROM product po  WHERE PRODUCT_ID= ?";

	public static final String CHECK_ENTRY_FOR_SP_NUMBER_CARD = "Select count(*) from CARD where clp_transactional.fn_dmaps_main(CARD_NUM_ENCR) = ?";
	public static final String GET_TRANSACTION_SHORT_NAME = "select TRANSACTION_SHORT_NAME from TRANSACTION where TRANSACTION_CODE = ?";
	public static final String GET_PRODUCT_ATTRIBUTES = "select ATTRIBUTES from PRODUCT where PRODUCT_ID = ?";

	public static final String GET_CVK = "SELECT po.attributes.CVV.cvkA||po.attributes.CVV.cvkB cvk FROM product po  WHERE PRODUCT_ID= ?";

	public static final String GET_EXPIRY_DATE = "select to_char(expiry_date,'MMYY') from card where account_id= ? and CARD_NUM_HASH= ?";

	public static final String CHECK_SP_NUMBER_TYPE = "select CARD_NUM_HASH,PRODUCT_ID,CARD_RANGE_ID,CARD_STATUS,clp_transactional.fn_dmaps_main(CARD_NUM_ENCR) AS CARDNUMBER,PROXY_NUMBER,SERIAL_NUMBER,ACCOUNT_ID,FIRSTTIME_TOPUP  from CARD where ";

	public static final String GET_PASSIVE_CHECK_FLAG = "select PASSIVE_SUPPORTED from DELIVERY_CHANNEL where CHANNEL_CODE = ?";

	public static final String UPDATE_PASSIVE_STATUS = "update CARD set CARD_STATUS = nvl( OLD_CARDSTAT , ? ) where CARD_NUM_HASH = ? and  CARD_STATUS = ? ";

	public static final String CHECK_CURRECNY_CODE = "select c.CURRENCY_CODE FROM CURRENCY_CODE c JOIN PURSE p ON p.CURRENCY_CODE = c.CURRENCY_ID "
			+ "JOIN PRODUCT_PURSE pr ON pr.PURSE_ID = p.PURSE_ID WHERE pr.PRODUCT_ID = ?";

	public static final String GET_DEL_CHANNEL_SHORTNAME = "select CHANNEL_SHORT_NAME from DELIVERY_CHANNEL where CHANNEL_CODE = ?";
	public static final String GET_CARD_LIMIT_ATTRIBUTES = "select USAGE_LIMIT from CARD where PRODUCT_ID = ? and CARD_NUM_HASH=?";
	public static final String GET_LAST_TRANSACTION_DATE = "select LAST_TXNDATE from CARD where CARD_NUM_HASH = ? ";
	public static final String UPDATE_CARD_LIMIT_ATTRIBUTES = "UPDATE card  SET USAGE_LIMIT = ? WHERE card_num_hash = ? AND product_id = ? ";

	public static final String UPDATE_CARD_USAGE_LIMITS = "UPDATE ACCOUNT_PURSE_USAGE  SET USAGE_LIMIT = ? WHERE   account_id = ? AND purse_id=?";

	public static final String GET_SPNUMBER_DETAILS = "SELECT CARD_NUM_HASH,fn_dmaps_main(CARD_NUM_ENCR) as CARD_NUM_ENCR, SERIAL_NUMBER, ACCOUNT_ID, CUSTOMER_CODE, PRODUCT_ID, PROXY_NUMBER, CARD_STATUS, to_char(EXPIRY_DATE,'YYMM'), DATE_OF_ACTIVATION,  FIRSTTIME_TOPUP, STARTERCARD_FLAG, OLD_CARDSTAT, LAST_TXNDATE,"
			+ " IS_REDEEMED, USAGE_LIMIT, USAGE_FEE from CARD ";

	public static final String GET_CARDNUMBERFORPAN = "Select fn_dmaps_main(card_num_encr) as Pan ,proxy_number,serial_number,account_number From card c, account a  Where c.account_id=a.account_id and  card_num_hash=fn_hash(?)";

	public static final String GET_CARDBYACTIVE = " and (serial_number = ? or proxy_number = ? or a.account_number = ? or customer_code = REPLACE(?,'A','')) and card_status not in('0','9')";

	public static final String GET_CARDBYINACTIVE = "and (serial_number = ? or proxy_number = ? or a.account_number = ? or customer_code = REPLACE(?,'A','')) and card_status ='0'";

	public static final String GET_CARDBYCLOSE = "and (serial_number = ? or proxy_number = ? or a.account_number = ? or customer_code = REPLACE(?,'A','')) and card_status = '9' order by c.ins_date desc ) where rownum <= 1";

	public static final String CARDREDEMPTION = "{call PKG_CARD_REDEEM.SPIL_CARD_REDEMPTION(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	public static final String TRANSACTION_LOG_ENTRY = "{call sp_transaction_log(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	public static final String ADD_CONVERSION_RATE = " {call PKG_CURRENCY_CONVERSION.add_update_currency_conv(?,?,?,?,?,?,?,?,?)}";

	private static final String GET_CARD_DETAILS_SELECT = "SELECT c.CARD_NUM_HASH, fn_dmaps_main(c.CARD_NUM_ENCR) as CARD_NUMBER, "
			+ "c.SERIAL_NUMBER, c.ACCOUNT_ID, c.CUSTOMER_CODE, c.PRODUCT_ID, c.PROXY_NUMBER, c.CARD_STATUS, "
			+ "to_char(c.EXPIRY_DATE,'YYMM') as EXPIRY_DATE, C.CARD_NUM_ENCR, SUBSTR(C.CARD_NUM_MASK, LENGTH(C.CARD_NUM_MASK)-3) AS LAST4DIGIT, "
			+ "c.DATE_OF_ACTIVATION, c.FIRSTTIME_TOPUP, c.STARTERCARD_FLAG, c.OLD_CARDSTAT, c.LAST_TXNDATE, "
			+ "c.IS_REDEEMED, c.PRFL_CODE, c.digital_pin,p.issuer_id,p.partner_id , nvl(c.REPL_FLAG,0) replFlag, a.account_number as ACCOUNT_NUMBER,SYSDATE AS db_sysdate ";

	public static final String GET_CARD_DETAILS = GET_CARD_DETAILS_SELECT
			+ "FROM CARD c, product p, account a where c.product_id=p.product_id and c.account_id=a.account_id ";

	public static final String GET_CARD_DETAILS_BY_CUSTOMER_ID = GET_CARD_DETAILS_SELECT
			+ " FROM CARD c, product p, account a, customer_profile cp where c.product_id=p.product_id and c.account_id=a.account_id "
			+ "and c.customer_code = cp.customer_code  AND cp.CUSTOMER_ID = to_number(?) ";

	public static final String SP_SPIL_STORE_CREDIT = "{call PKG_CARD_LOAD_FUND.SPIL_STORE_CREDIT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	public static final String SP_SPIL_CREDIT = "{call PKG_CARD_LOAD_FUND.SPIL_CREDIT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	public static final String SP_SPIL_REDEMPTION_LOCK = "{call PKG_CARD_REDEEM.SPIL_REDEMPTION_LOCK(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	public static final String SP_SPIL_REDEMPTION_UNLOCK = "{call PKG_CARD_REDEEM.SPIL_REDEMPTION_UNLOCK(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_SPIL_CARD_REDEMPTION_REVERSAL = "{call PKG_CARD_REDEEM.SPIL_CARD_REDEMPTION_REVERSAL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_SPIL_REDEMPTION_LOCK_REVERSAL = "{call PKG_CARD_REDEEM.SPIL_REDEMPTION_LOCK_REVERSAL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_SPIL_REDEMPTION_UNLOCK_REVERSAL = "{call PKG_CARD_REDEEM.SPIL_REDEMPTION_UNLOCK_REVERSAL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_SPIL_STORE_CREDIT_REVERSAL = "{call PKG_CARD_LOAD_FUND.SPIL_STORE_CREDIT_REVERSAL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_SPIL_CREDIT_REVERSAL = "{call PKG_CARD_LOAD_FUND.SPIL_CREDIT_REVERSAL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	public static final String SP_BALANCE_TRANSFER = "{call PKG_CARD_TRANSFER.SPIL_BALANCE_TRANSFER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_BALANCE_TRANSFER_RVSL = "{call PKG_CARD_TRANSFER.spil_balance_transfer_rvsl(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_CARD_TO_CARD_TRANSFER = "{call PKG_CARD_TRANSFER.SPIL_CARD_TO_CARD_TRANSFER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_CARD_TO_CARD_TRANSFER_RVSL = "{call PKG_CARD_TRANSFER.SPIL_CARD_TO_CARD_TRANSFER_RVSL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_SPIL_TOPUP = "{call PKG_CARD_LOAD_FUND.SPIL_CARD_TOPUP(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_SPIL_TOPUP_RVSL = "{call PKG_CARD_LOAD_FUND.SPIL_CARD_TOPUP_RVSL(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String SP_SPIL_PRE_VALUE_INS = "{call PKG_CARD_LOAD_FUND.SPIL_PRE_VALUE_INS (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	public static final String CHECK_PRODUCT = "SELECT COUNT(*) from PRODUCT WHERE PRODUCT_ID = ?";
	public static final String UPDATE_CARDSTATUS = "UPDATE CARD SET CARD_STATUS = ?, OLD_CARDSTAT = ? WHERE CARD_NUM_HASH = ? ";

	/* customer profile */

	public static final String GET_CARD_ATTRIBUTES = "select attributes from CARD where prfl_code = ?";

	/* RuleSet */
	public static final String CHECK_RULESET = "SELECT COUNT(*) from RULESET WHERE RULESET_ID = ?";
	public static final String GET_RULES_FOR_RULESETID = "select rule_id,rule_name,rule_exp,TRANSACTION_TYPE,ACTION_TYPE,JSON_REQ from rule where rule_id in (select rule_id from rule_ruleset where ruleset_id=?)";
	public static final String GET_RULES_DETAILS_FOR_RULEID = "select rule_details_id,rule_filter from rule_details where rule_id =?";
	public static final String GET_TRANSACTION_IDENTIFIER = "Select TRANSACTION_IDENTIFIER from delivery_channel_transaction where channel_code=? and transaction_code=? and MESSAGE_TYPE=?";

	public static final String GET_RULE_SET_ID = "select ruleset_id from product_ruleset where product_id=? ";

	public static final String GET_CARD_NUMBER = "	select fn_dmaps_main(customer_card_nbr_encr) from transaction_log where rrn = ? and transaction_code = ? and delivery_channel='01' and response_id='R0001'";

	// Added by Hari for Product validity Check
	public static final String CHECK_PRODUCT_VALIDITY = "SELECT COUNT(1) from clp_configuration.Product p where p.product_id=:productId and SYSDATE between to_date(p.attributes.Product.activeFrom,'mm/dd/yyyy') and nvl((to_date(p.attributes.Product.productValidityDate,'mm/dd/yyyy')+1),sysdate)";

	// Added for product funding query
	public static final String GET_PRODUCT_FUNDING = "SELECT  b.product_funding,  b.fund_amount, b.denomination,c.merchant_id, c.location_id FROM order_line_item_dtl a JOIN order_line_item b ON a.order_id = b.order_id JOIN order_details c ON a.order_id = c.order_id AND a.order_line_item_id = b.line_item_id AND a.partner_id = c.partner_id AND a.card_num_hash = ?";

	public static final String UPDATE_CARD_STATUS = "UPDATE CARD SET CARD_STATUS = ? WHERE CARD_NUM_HASH = ? ";

	public static final String CARD_NUM_BY_PRODUCTID = "select * FROM ( SELECT CLP_TRANSACTIONAL.FN_DMAPS_MAIN(CARD_NUM_ENCR) FROM CLP_TRANSACTIONAL.CARD WHERE CARD_STATUS = '99' AND PRODUCT_ID = ?) WHERE ROWNUM = 1 FOR UPDATE";

	public static final String CARDNO_BY_RRN = "SELECT CLP_TRANSACTIONAL.FN_DMAPS_MAIN(CUSTOMER_CARD_NBR_ENCR) FROM CLP_TRANSACTIONAL.TRANSACTION_LOG WHERE RRN = ? AND RESPONSE_ID = 'R0001' AND MSG_TYPE = '0200' AND TRANSACTION_CODE = ? AND DELIVERY_CHANNEL = '01' and TRANSACTION_DATE=?";

	public static final String GET_CONVERSION_RATE = "select * FROM (select conversion_rate as exchangeRate from CLP_TRANSACTIONAL.currency_conversion PARTITION (STATUS_PART1) where mdm_id=? and transaction_currency=upper(?) and issuing_currency=upper(?) and effective_date<=current_timestamp and current_timestamp<=expiry_date order by effective_date desc) WHERE ROWNUM = 1";

	public static final String GET_ALL_CONVERSION_RATE = "select transaction_currency,issuing_currency,conversion_rate,to_char(to_date(to_char(effective_date,'DD-MON-YYYY HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS') as effective_date from CLP_TRANSACTIONAL.currency_conversion PARTITION (STATUS_PART1) where mdm_id=? and effective_date<=current_timestamp order by effective_date desc";
	// Added for NoSP
	public static final String UPDATE_CURRENT_CARD_STATUS = "update card set card_status = :cardStatus where card_num_hash=:cardNumHash";
	public static final String UPDATE_CARD_STATUS_NOT_ACTIVE_CLOSED = " update card set card_status = :cardStatus where card_num_hash=:cardNumHash and card_status not in (0,9)";
	public static final String UPDATE_CARD_STATUS_OLD_CARD_STATUS = "update card set card_status = :cardStatus, old_cardstat=:oldCardStatus where card_num_hash=:cardNumHash";
	public static final String UPDATE_INITIAL_LOAD_BALANCE = "update account set initialload_amt = :initialLoadAmt, new_initialload_amt= :newLoadAmt, last_upd_date = SYSDATE where account_id= :accountId";
	public static final String UPDATE_LOAD_BALANCES = "update account set initialload_amt = null, new_initialload_amt= null, last_upd_date = SYSDATE where account_id= :accountId";
	public static final String GET_DAMAGED_CARD_COUNT = "select count(*) from card where account_id = :accountId and card_status = :cardStatus";
	public static final String GET_ACCOUNT_PURSE_INFO = "SELECT\n" + "nvl(available_balance,0) as avlbl,\n"
			+ "nvl(ledger_balance,0) as ledbl,\n" + "b.purse_id as pid, \n" + "b.currency_code as currencyCode\n" + "FROM\n"
			+ "CLP_CONFIGURATION.product_purse a \n" + "JOIN\n" + "CLP_TRANSACTIONAL.account_purse b \n" + "ON \n"
			+ "a.purse_id = b.purse_id \n" + "JOIN \n" + "CLP_TRANSACTIONAL.card c \n" + "ON \n" + "b.account_id = c.account_id \n"
			+ "AND \n" + "a.is_default = 'Y' \n" + "AND \n" + "a.product_id = :productId \n" + "AND \n" + "b.account_id = :accountId \n"
			+ "AND\n" + "b.currency_code = ( \n" + "SELECT \n" + "currency_id \n" + "FROM \n" + "CLP_TRANSACTIONAL.currency_code \n"
			+ "WHERE \n" + "currency_code = :currencyCode) \n" + "AND \n" + "c.card_num_hash = :cardNumber";

	public static final String UPDATE_BALANCES_QUERY = "UPDATE account_purse SET ledger_balance = :closingLedgerBalance, available_balance =:closingAvailableBalance "
			+ "WHERE account_id = :accountId AND purse_id = :purseId";

	public static final String UPDATE_LOCATION_INVENTORY = "UPDATE CLP_ORDER.LOCATION_INVENTORY "
			+ "SET CLP_ORDER.LOCATION_INVENTORY.curr_inventory = CLP_ORDER.LOCATION_INVENTORY.curr_inventory + 1 WHERE product_id = :productId AND "
			+ "merchant_id = :merchantId AND location_id = :locationId";

	public static final String SUBTRACT_LOCATION_INVENTORY = "UPDATE CLP_ORDER.LOCATION_INVENTORY "
			+ "SET CLP_ORDER.LOCATION_INVENTORY.curr_inventory = CLP_ORDER.LOCATION_INVENTORY.curr_inventory - 1 WHERE product_id = :productId AND "
			+ "merchant_id = :merchantId AND location_id = :locationId";

	public static final String GET_LOCATION_MERCHANT_ID = "SELECT c.merchant_id,c.location_id FROM order_line_item_dtl a JOIN order_line_item b ON a.order_id = b.order_id "
			+ "JOIN order_details c ON a.order_id = c.order_id AND a.order_line_item_id = b.line_item_id "
			+ "AND a.partner_id = c.partner_id AND a.card_num_hash = :cardNumHash";

	public static final String GET_ORDER_DETAILS = "select c.merchant_id, c.location_id FROM "
			+ "order_line_item_dtl a JOIN order_line_item b ON a.order_id = b.order_id JOIN order_details c ON a.order_id = c.order_id "
			+ "AND a.order_line_item_id = b.line_item_id AND a.partner_id = c.partner_id AND a.card_num_hash = :cardNumHash";

	public static final String UPDATE_LOAD_DATE = "update account_purse set first_load_date = :date where account_purse_id= :accountPurseId";
	public static final String GET_REPLACED_CARD_STATUS = "SELECT old_cardstat as oldCardStatus, card_num_hash as cardNumHash FROM card WHERE date_of_activation = ( SELECT MAX(date_of_activation) FROM card WHERE "
			+ "account_id = :accountId AND card_status = :cardStatus) AND account_id = :accountId AND card_status = :cardStatus";
//	    public static final String GET_FIRSTTIME_TOPUP_FLAG = "select process_flag from transaction_log where delivery_channel = :deliveryChannel " + 
//	    		"and transaction_code = :txnCode and card_number = :cardNumber and response_id = :responseId and msg_type = :msgType and rrn = :rrn";
	public static final String UPDATE_TXN_REVERSAL_FLAG = "UPDATE transaction_log SET tran_reverse_flag = :reversalFlag WHERE rrn = :rrn AND card_number = :cardNumHash AND "
			+ "response_id = 'R0001' AND msg_type = '0200' AND transaction_code = :txnCode AND delivery_channel = '01'";
	public static final String GET_LAST_SUCCESS_ACT_TXN = "select transaction_amount as transactionAmount ,auth_amount as authAmount ,tranfee_amount as tranfeeAmount,tran_reverse_flag as tranReverseFlag, process_flag as processFlag, "
			+ " TRANSACTION_DATE as transactionDate,TRANSACTION_TIME as transactionTime,TERMINAL_ID as terminalId,orgnl_rrn as originalRrn, account_purse_id as accountPurseId, "
			+ " OPENING_LEDGER_BALANCE as openingLedgerBalance,OPENING_AVAILABLE_BALANCE as openingAvailableBalance, "
			+ " LEDGER_BALANCE as closingLedgerBalance,ACCOUNT_BALANCE as closingAvailableBalance "
			+ " from transaction_log where delivery_channel = :deliveryChannel "
			+ "and transaction_code = :txnCode and card_number = :cardNumber and response_id = :responseId and msg_type = :msgType and rrn = :rrn and transaction_date=:txnDate";
	public static final String UPDATE_LOAD_DATE_TO_NULL = "update account_purse set first_load_date = null where account_id= :accountId";

	public static final String UPDATE_REDEMPTION_DELAY_FLAG = " UPDATE ACCOUNT SET redemption_delay_flag='Y' WHERE "
			+ " account_id = ? AND NVL(redemption_delay_flag,'N') = 'N' ";

	public static final String INSERT_DELAY_LOAD = " INSERT INTO delayed_load (account_id,delivery_channel,txn_code,rrn,tran_amt,expiry_date,ins_date) "
			+ " VALUES (?,?,?,?,?,SYSDATE + ? / 1440,SYSDATE)";

	public static final String UPDATE_DELAY_LOAD_REVERSAL = " UPDATE delayed_load SET expiry_date = SYSDATE WHERE account_id = ? "
			+ "AND rrn = ? AND delivery_channel = ? " + "AND txn_code = ? AND tran_amt = ? AND expiry_date > SYSDATE ";

	/** R08 changes added by venkateshgaddam starts **/
	public static final String GET_MONTHLY_FEE_CAP_DETAILS_QRY = "SELECT FEE_CAP,FEE_ACCRUED FROM MONTHLY_FEECAP_DTL WHERE ACCOUNT_NUMBER=? AND FEE_PERIOD=?";
	public static final String MONTHLY_FEE_CAP_DETAILS_INSERT_QRY = "INSERT INTO MONTHLY_FEECAP_DTL(ACCOUNT_NUMBER,FEE_PERIOD,FEE_CAP,FEE_ACCRUED,FEE_WAIVED,INS_USER,INS_DATE) VALUES(?,?,?,?,?,?,SYSDATE)";
	public static final String MONTHLY_FEE_CAP_DETAILS_UPDATE_QRY = "UPDATE MONTHLY_FEECAP_DTL SET FEE_ACCRUED=?,FEE_CAP=?, LAST_UPD_DATE=SYSDATE WHERE ACCOUNT_NUMBER=? and FEE_PERIOD=?";
	public static final String UPDATE_CARD_USAGE_FEE = "UPDATE account_purse_usage  SET USAGE_FEE = ? WHERE  account_id = ? AND purse_id=?";
	/** R08 changes added by venkateshgaddam ends **/

	public static final String UPDATE_CARD_STATUS_DIG_IN_PROCESS = "UPDATE CARD SET CARD_STATUS = ? WHERE CARD_NUM_HASH = FN_HASH(?) ";
	public static final String UPDATE_OLDCARD_STATUS_DIG_NULL = "UPDATE CARD SET old_cardStat=null WHERE CARD_NUM_HASH = FN_HASH(?) ";
	public static final String UPDATE_EXPIRY_DATE = "UPDATE CARD SET EXPIRY_DATE = ? WHERE CARD_NUM_HASH = FN_HASH(?) AND CARD_STATUS = '1' ";
	public static final String UPDATE_OLD_CARD_STATUS = "update card set old_cardstat=:oldCardStatus where card_num_hash=:cardNumHash";
	public static final String GET_LAST_FEE_TXN = "select transaction_amount as txnAmount ,transaction_narration as txnDesc,account_purse_id as accountPurseId, purse_id as purseId"
			+ " from statements_log where delivery_channel='01' and transaction_code=:txnCode and rrn = :rrn and credit_debit_flag = :credit_debit_flag and transaction_date=:txnDate";

	public static final String GET_ALL_STATEMENTS_LOG = "select transaction_amount as transactionAmount ,transaction_narration as transactionDescription,account_purse_id as accountPurseId, purse_id as purseId,"
			+ " credit_debit_flag as operationType, to_purse_id as toPurseId "
			+ " from statements_log where delivery_channel='01' and transaction_code=:txnCode and rrn = :rrn and transaction_date=:txnDate";

	public static final String GET_PARTNER_DETAILS = "SELECT MDM_ID FROM  partner WHERE partner_id =? or PARTNER_NAME=?";
	public static final String GET_CURRENCY_CODE = "select CURRENCY_CODE from CURRENCY_CODE where currency_id = (select CURRENCY_CODE from purse where purse_id = ?)";
	public static final String DEL_CONVERSION_RATE = " {call PKG_CURRENCY_CONVERSION.delete_currency_conv(?,?,?,?,?,?,?,?)}";
	public static final String CHECK_MDMID = "select count(1) mdmid_exists from partner where mdm_id = ?";

	public static final String GET_MDMID = "SELECT MDM_ID FROM  partner WHERE partner_id =?";

	public static final String UPDATE_LAST_TXNDATE = "update account_purse_usage set last_txndate = SYSDATE  where account_id= :accountId and purse_id= :purseId";

	public static final String GET_ACCOUNTPURSE_LIST = "SELECT a FROM AccountPurse a WHERE a.accountPursePK.accountId = :accountId and a.accountPursePK.productId = :productId and a.accountPursePK.purseId=:purseId and (a.effectiveDate<=SYSDATE OR a.effectiveDate is null) and (a.expiryDate is null OR SYSDATE < a.expiryDate) order by a.effectiveDate asc";
	public static final String GET_ACCOUNTPURSE_DETAILS = "SELECT a FROM AccountPurse a WHERE a.accountPurseId=:accountPurseId";
	public static final String UPDATE_ACCOUNT_PURSE_BALANCE = "UPDATE account_purse SET ledger_balance = :closingLedgerBalance, available_balance =:closingAvailableBalance "
			+ "WHERE account_purse_id = :accountPurseId";

	public static final String GET_ACCOUNT_PURSE_USAGE_DETAILS = "SELECT  au.LAST_TXNDATE, "
			+ " au.USAGE_LIMIT, au.USAGE_FEE,au.PURSE_STATUS "
			+ "FROM ACCOUNT_PURSE_USAGE au JOIN ACCOUNT_PURSE ap on au.account_id = ap.account_id and au.purse_id=ap.purse_id  where au.account_id =? and au.purse_id=? and NVL(ap.EXPIRY_DATE,trunc(SYSDATE)) >= TRUNC(SYSDATE) ";

	public static final String EXPIRE_ACCOUNT_PURSE = "UPDATE account_purse SET ledger_balance = :closingLedgerBalance, available_balance =:closingAvailableBalance, expiry_date=SYSDATE "
			+ "WHERE account_purse_id = :accountPurseId";

	public static final String UPDATE_CARD_DETAILS = "UPDATE card SET card_status = nvl(:oldCardStatus,1), firsttime_topup = :firstTimeTopUp,old_cardstat = 0, "
			+ "last_txndate = SYSDATE, date_of_activation = SYSDATE WHERE card_num_hash = :cardNumHash";

	public static final String GET_PURSE_BAL = "SELECT listagg(account_purse_id||'|'|| (SELECT ext_purse_id FROM purse WHERE purse_id = a.purse_id ) ||'|'||a.available_balance||'|'||"
			+ "  CASE WHEN purse_type IN ( 'SKU','POINTS') THEN purse_type WHEN purse_type NOT IN ( 'SKU','POINTS') THEN ( SELECT currency_code"
			+ " FROM currency_code WHERE currency_id = a.currency_code) ELSE purse_type END ||'|'||expiry_date||'|'||sku_code,',')"
			+ " within group (order by account_purse_id) as list " + " FROM account_purse a,product_purse p,account_purse_usage au"
			+ " WHERE a.account_id = au.account_id and a.purse_id = au.purse_id and au.purse_status= 1 and a.account_id = ?"
			+ " AND a.purse_id = p.purse_id and  (a.EFFECTIVE_DATE is null or a.EFFECTIVE_DATE <= sysdate) AND (a.expiry_date is null or a.expiry_date >= sysdate) AND a.product_id = p.product_id AND p.is_default <> 'Y'";

	public static final String CHECK_STMTLOG_FOR_DEACTIVATION = "select count(1) from statements_log where CARD_NUM_HASH = fn_hash(?) and transaction_code not in ('002','003','081','100') ";
	public static final String CHECK_TXNLOG_FOR_DEACTIVATION = "select count(1) from transaction_log where card_number = fn_hash(?) and transaction_code = '007' ";

	public static final String GET_DELAYED_AMOUNT = "select NVL (SUM (tran_amt), 0) FROM delayed_load WHERE account_id = ? AND expiry_date > SYSDATE";

	public static final String UPDATE_FIRST_LOAD_DATE_TO_NULL = "UPDATE account_purse set FIRST_LOAD_DATE = null where account_id =? and product_id=? and purse_id=?";
	public static final String UPDATE_CARDSTATUS_TO_INACTIVE = "UPDATE CARD SET CARD_STATUS = ?, OLD_CARDSTAT = ?, firsttime_topup = 'N', last_txndate = sysdate, date_of_activation = '' WHERE CARD_NUM_HASH = ? ";

	public static final String UPDATE_DELAYED_AMOUNT = "Update delayed_load set tran_amt=0 WHERE account_id = ? AND expiry_date > SYSDATE";

	public static final String GET_ACCOUNT_DEFAULTPURSE_AVAILABLE_BALANCE = "SELECT ACCT.AVAILABLE_BALANCE, (SELECT CURRENCY_CODE FROM CURRENCY_CODE WHERE CURRENCY_ID = ACCT.CURRENCY_CODE) FROM ACCOUNT_PURSE ACCT,PRODUCT_PURSE PURSE "
			+ " WHERE ACCT.PRODUCT_ID = PURSE.PRODUCT_ID AND PURSE.PURSE_ID = ACCT.PURSE_ID AND PURSE.IS_DEFAULT ='Y' AND ACCT.ACCOUNT_ID = :accountId";

//	public static final String GET_LAST_SUCCESSFUL_ACT_TXN_FOR_DEACT = "select transaction_amount as transactionAmount ,auth_amount as authAmount ,tranfee_amount as tranfeeAmount,tran_reverse_flag as tranReverseFlag, process_flag as processFlag, "
//			+ " TRANSACTION_DATE as transactionDate,TRANSACTION_TIME as transactionTime,TERMINAL_ID as terminalId,orgnl_rrn as originalRrn, account_purse_id as accountPurseId, "
//			+ " OPENING_LEDGER_BALANCE as openingLedgerBalance,OPENING_AVAILABLE_BALANCE as openingAvailableBalance, "
//			+ " LEDGER_BALANCE as closingLedgerBalance,ACCOUNT_BALANCE as closingAvailableBalance " + " FROM transaction_log txnLog WHERE "
//			+ " txnLog.TRANSACTION_SQID = ( SELECT MAX(TRANSACTION_SQID) FROM  transaction_log txnLog WHERE "
//			+ "delivery_channel = :deliveryChannel " + "and transaction_code = :txnCode and card_number = :cardNumber "
//			+ "and response_id = :responseId and msg_type = :msgType and purse_Id= :purseId)";

	public static final String GET_LAST_SUCCESSFUL_ACT_TXN_FOR_DEACT = "SELECT transaction_amount AS transactionamount,auth_amount AS authamount,tranfee_amount AS tranfeeamount,"
			+ "tran_reverse_flag AS tranreverseflag,process_flag AS processflag,transaction_date AS transactiondate,transaction_time AS transactiontime,terminal_id AS terminalid,"
			+ "orgnl_rrn AS originalrrn,account_purse_id AS accountpurseid,opening_ledger_balance AS openingledgerbalance,opening_available_balance AS openingavailablebalance,"
			+ "ledger_balance AS closingledgerbalance,account_balance AS closingavailablebalance FROM (SELECT transaction_amount,auth_amount,tranfee_amount,tran_reverse_flag,process_flag,"
			+ "transaction_date,transaction_time,terminal_id,orgnl_rrn,account_purse_id,opening_ledger_balance,opening_available_balance,ledger_balance,account_balance FROM transaction_log txnlog "
			+ "WHERE delivery_channel = :deliveryChannel AND transaction_code = :txnCode AND msg_type = :msgType AND card_number = :cardNumber AND response_id = :responseId AND purse_id = :purseId "
			+ "ORDER BY ins_time_stamp DESC) WHERE ROWNUM = 1";

	public static final String GET_PURSE_DETAILS = "select ap.account_purse_id,p.purse_type_id,p.purse_id,ap.purse_type from account_purse ap join purse p on ap.purse_id=p.purse_id where ap.account_id=:accountId and ap.purse_id=:purseId";

	public static final String CARDNO_BY_CUSTOMERID = "select clp_transactional.fn_dmaps_main(card_num_encr) spNumber from card c, customer_profile cp where c.CUSTOMER_CODE = cp.CUSTOMER_CODE and cp.CUSTOMER_ID = ? and card_status<>9 ";

	public static final String CARDNO_FROM_BAL_TRANSFER_DETAILS = "select clp_transactional.fn_dmaps_main(source_card_num_encr) source_card_num, clp_transactional.fn_dmaps_main(target_card_num_encr) target_card_num from clp_transactional.balance_transfer_details where rrn = ? and customer_code = ?";

	public static final String GET_RRN_FROM_BALANCE_TRANSFER_DETAILS = "select count (1) rrnCount from clp_transactional.balance_transfer_details where rrn = ? and (customer_code = ? OR fn_dmaps_main(target_card_num_encr) = ?)";

	public static final String GET_ALL_LAST_SUCCESS_PURSE_ACT_TXN = "select * from transaction_log where delivery_channel = :deliveryChannel "
			+ "and transaction_code = :txnCode and card_number = :cardNumber and response_id = :responseId and msg_type = :msgType and rrn = :rrn and transaction_date=:txnDate and purse_id <> :purseId";

	public static final String GET_SERVER_DATE = "select to_char(Systimestamp,'YYYY-MM-DD HH24:MI:SS.FF2TZH:TZM') from dual";

	public static final String UPDATE_CARD = "update card set card_status = nvl(:newCardStatus,1), firsttime_topup = :firstTimeTopUp, "
			+ " date_of_activation = :activationDate WHERE card_num_hash = :cardNumberHash and card_status =:oldCardStatus";

	public static final String UPDATE_CARD_FIRSTTIME_TOPUP = "update card set firsttime_topup =:newFirstTimeTopUpFlag WHERE card_num_hash = :cardNumberHash and firsttime_topup =:oldFirstTimeTopUpFlag";

	public static final String GET_REDEMPTION_LOCKS = "select r FROM RedemptionLock r WHERE r.lockFlag = :lockFlag AND r.cardNumberHash = :cardNumberHash";
}
