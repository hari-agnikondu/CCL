package com.incomm.cclp.dao.impl;

import static com.incomm.cclp.account.util.CodeUtil.not;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.factory.AccountPurseStateType;
import com.incomm.cclp.account.domain.model.AccountPurseGroupStatus;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.account.util.JsonUtil;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.AccountPurseDAO;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.AccountPurseInfo;
import com.incomm.cclp.dto.AccountBalance;
import com.incomm.cclp.dto.AccountPurseUsageDto;
import com.incomm.cclp.exception.ServiceException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class AccountPurseDAOImpl extends JdbcDaoSupport implements AccountPurseDAO {

	@Autowired
	private EntityManager em;

	@Autowired
	DataSource dataSource;

	RowMapper<AccountPurseBalance> acountPurseBalanceRowMapper;
	RowMapper<AccountPurseUsageDto> accountPurseUsageRowMapper;
	RowMapper<AccountBalance> accountBalanceRowMapper;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
		this.acountPurseBalanceRowMapper = this.getAccountPurseBalanceRowMapper();
		this.accountPurseUsageRowMapper = this.getAccountPurseUsageRowMapper();
		this.accountBalanceRowMapper = this.getAccountBalanceRowMapper();
	}

	private static final String GET_ACCOUNT_PURSE_USAGE_BY_ACCOUNT_ID = "select account_id, purse_id, usage_fee, usage_limit, last_txndate, PURSE_STATUS, "
			+ " start_date, end_date from account_purse_usage where account_id = ? ";

	private static final String GET_ACCOUNT_PURSE_USAGE_BY_PURSE_ID = GET_ACCOUNT_PURSE_USAGE_BY_ACCOUNT_ID + " and purse_id = ?";

	private static final String UPDATE_ACCOUNT_PURSE_USAGE_QUERY = "update account_purse_usage set usage_fee= :usageFees, "
			+ " usage_limit= :usageLimits, last_txndate= :lastTransactionDate, purse_status= :purseStatus, start_date = :startDate, end_date = :endDate "
			+ " where account_id =:accountId and purse_id =:purseId";

	private static final String UPDATE_PURSE_STATUS_QUERY = "update account_purse_usage set purse_status= :purseStatus, "
			+ " last_txndate= :lastTransactionDate, start_date= :startDate, end_date= :endDate "
			+ " where account_id =:accountId and purse_id =:purseId";

	private static final String GET_ACCOUNT_BALANCE = "SELECT account_id, product_id, purse_id, account_purse_id, ledger_balance, "
			+ " available_balance, purse_type, currency_code, purse_type_id, effective_date, expiry_date "
			+ " FROM CLP_TRANSACTIONAL.account_purse where account_purse_id= :accountPurseId";

	private static final String QUERY_STR = "SELECT nvl(available_balance,0) as avlbl, "
			+ "nvl(ledger_balance,0) as ledbl, purse_id as pid,currency_code as currencyCode "
			+ "FROM CLP_TRANSACTIONAL.account_purse where account_purse_id= :accountPurseId";

	private static final String GET_ACCOUNT_PURSE_BALANCE_QUERY_BASE = "SELECT ap.account_id, ap.product_id, ap.purse_id, ap.account_purse_id, "
			+ " ap.ledger_balance, ap.available_balance, ap.purse_type, ap.currency_code, ap.purse_type_id, ap.effective_date, ap.expiry_date, ap.sku_code, ap.TOPUP_STATUS "
			+ " FROM account_purse ap WHERE ap.account_id = :accountid ";

	private static final String PURSE_ID_CLAUSE = " AND ap.purse_id = :purseid ";
	private static final String UNEXPIRED_CLAUSE = " and nvl(ap.expiry_date, sysdate) >= sysdate ";

	private static final String GET_ACCOUNT_PURSE_BALANCE_QUERY = "select ap.account_id, ap.product_id, ap.purse_id, ap.account_purse_id, "
			+ "ap.ledger_balance, ap.available_balance, ap.purse_type, ap.currency_code, ap.purse_type_id, ap.effective_date, "
			+ "ap.expiry_date, ap.sku_code, ap.topup_status from account_purse ap  "
			+ "where ap.account_id=:accountId and ap.product_id=:productId and ap.purse_id=:purseId and "
			+ "(SYSDATE <= ap.expiry_date OR ap.expiry_date is null) order by ap.expiry_date desc";

	private static final String GET_ACTIVE_ACCOUNT_PURSE = "select ap.account_id, ap.product_id, ap.purse_id, ap.account_purse_id, "
			+ "ap.ledger_balance, ap.available_balance, ap.purse_type, ap.currency_code, ap.purse_type_id, ap.effective_date, "
			+ "ap.expiry_date, ap.sku_code, ap.topup_status from account_purse ap join account_purse_usage apu on ap.account_id = apu.account_id and ap.purse_id=apu.purse_id "
			+ "where ap.account_id=:accountId and ap.product_id=:productId and ap.purse_id=:purseId and "
			+ "(ap.effective_date <= SYSDATE OR ap.effective_date is null)  and "
			+ "(SYSDATE <= ap.expiry_date OR ap.expiry_date is null) and apu.purse_status=1";

	private static final String GET_EXPIRED_ACCOUNT_PURSE_BALANCE_QUERY = "select ap.account_id, ap.product_id, ap.purse_id, ap.account_purse_id, "
			+ "ap.ledger_balance, ap.available_balance, ap.purse_type, ap.currency_code, ap.purse_type_id, ap.effective_date, "
			+ "ap.expiry_date, ap.sku_code, ap.topup_status from account_purse ap join account_purse_usage apu on ap.account_id = apu.account_id and ap.purse_id=apu.purse_id "
			+ "where ap.account_id=:accountId and ap.product_id=:productId and ap.purse_id=:purseId and "
			+ "topup_status='AUTOTOPUP'  and  ap.available_balance > 0 and " + "(SYSDATE > ap.expiry_date) and apu.purse_status=1";

	private static final String GET_ALL_ACCOUNT_PURSE_BALANCE_QUERY = "select account_id, product_id, purse_id, account_purse_id, ledger_balance, "
			+ "available_balance, purse_type, currency_code, purse_type_id, effective_date, expiry_date, SKU_CODE, TOPUP_STATUS from CLP_TRANSACTIONAL.account_purse"
			+ " where account_id=:accountId and product_id=:productId";

	private static final String ADD_ACCOUNT_PURSE = "insert into account_purse(account_id,product_id,purse_id,ledger_balance, "
			+ "available_balance,purse_type,currency_code,purse_type_id,first_load_date,account_purse_id,effective_date, "
			+ "expiry_date,sku_code,TOPUP_STATUS) VALUES(?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?)";

	private static final String ADD_ACCOUNT_PURSE_USAGE = "Insert into account_purse_usage (ACCOUNT_ID, PURSE_ID, USAGE_FEE, USAGE_LIMIT "
			+ ",LAST_TXNDATE, INS_USER, INS_DATE,LAST_UPD_USER,LAST_UPD_DATE, PURSE_STATUS, START_DATE, END_DATE) "
			+ "values (?, ?, ?, ?, ? , 1, sysdate, 1, sysdate, ?, ?, ?)";

	private static final String GET_ACTIVE_ACCOUNT_PURSE_BALANCE_ = "select ap.account_id, ap.product_id, ap.purse_id, ap.account_purse_id, "
			+ "ap.ledger_balance, ap.available_balance, ap.purse_type, ap.currency_code, ap.purse_type_id, ap.effective_date, "
			+ "ap.expiry_date, ap.sku_code from account_purse ap  "
			+ "where ap.account_id=:accountId and ap.account_purse_id=:accountPurseId and (ap.effective_date <= SYSDATE OR ap.effective_date is null)  and "
			+ "(SYSDATE <= ap.expiry_date OR ap.expiry_date is null) ";

	private static final String GET_ACTIVE_ACCOUNT_PURSE_BALANCES = "select ap.account_id, ap.product_id, ap.purse_id, ap.account_purse_id, "
			+ "ap.ledger_balance, ap.available_balance, ap.purse_type, ap.currency_code, ap.purse_type_id, ap.effective_date, "
			+ "ap.expiry_date, ap.sku_code from account_purse ap join account_purse_usage apu on ap.account_id = apu.account_id and ap.purse_id=apu.purse_id "
			+ "where ap.account_id=:accountId and ap.purse_id=:purseId and ap.sku_code=:skuCode and "
			+ "(ap.effective_date <= SYSDATE OR ap.effective_date is null)  and "
			+ "(SYSDATE <= ap.expiry_date OR ap.expiry_date is null) and apu.purse_status=1";

	private static final String GET_ACCOUNT_PURSE_BALANCES = "select ap.account_id, ap.product_id, ap.purse_id, ap.account_purse_id, "
			+ "ap.ledger_balance, ap.available_balance, ap.purse_type, ap.currency_code, ap.purse_type_id, ap.effective_date, "
			+ "ap.expiry_date, ap.sku_code from account_purse ap join account_purse_usage apu on ap.account_id = apu.account_id and ap.purse_id=apu.purse_id "
			+ "where ap.account_id=:accountId and ap.purse_id=:purseId and "
			+ "(ap.effective_date <= SYSDATE OR ap.effective_date is null)  and "
			+ "(SYSDATE <= ap.expiry_date OR ap.expiry_date is null) and apu.purse_status=:purseStatus";

	private static final String GET_ALL_ACTIVE_PURSE_ACCOUNT_BALANCES = "select account_purse_id,(SELECT ext_purse_id FROM purse WHERE purse_id = a.purse_id ) as purse_name, "
			+ "a.available_balance,CASE WHEN purse_type IN ( 'SKU','POINTS') THEN purse_type WHEN purse_type NOT IN ( 'SKU','POINTS') THEN ( SELECT currency_code "
			+ "FROM currency_code WHERE currency_id = a.currency_code) ELSE purse_type END as currency_code,expiry_date,sku_code from account_purse a,product_purse p, "
			+ "account_purse_usage au WHERE a.account_id = au.account_id and a.purse_id = au.purse_id and au.purse_status= 1 and a.account_id = :accountId "
			+ "AND a.purse_id = p.purse_id  AND a.available_balance >= 0 AND nvl(expiry_date,sysdate) >= SYSDATE AND a.product_id = p.product_id AND p.is_default <> 'Y' ORDER BY account_purse_id";

	@Override
	public AccountPurseInfo getAccountPurse(long accountPurseId) {
		try {
			Query query = em.createNativeQuery(QUERY_STR, "AccountPurseInfoResult");
			query.setParameter("accountPurseId", accountPurseId);

			@SuppressWarnings("unchecked")
			List<AccountPurseInfo> ac = query.getResultList();

			if (ac.get(0) == null) {
				logger.warn("NO DATA FOUND");

				throw new ServiceException(SpilExceptionMessages.ERROR_NO_DATA_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
			}

			return ac.get(0);

		} catch (NoResultException nre) {
			logger.warn("No Result getAccountPurse: " + nre.getMessage());

			throw new ServiceException(SpilExceptionMessages.ERROR_NO_DATA_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		} catch (Exception e) {
			logger.warn(SpilExceptionMessages.ERROR_AVAILABLE_BALANCE + " " + e.getMessage(), e);

			throw new ServiceException(SpilExceptionMessages.ERROR_AVAILABLE_BALANCE, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public int updateAccountPurse(long accountId, long purseId, long accountPurseId, BigDecimal closingLedgBalance,
			BigDecimal closingAvailBalance, BigDecimal previousLedgBalance, BigDecimal previousAvailBalance, String topupStatus) {
		String query = "UPDATE account_purse " + "SET ledger_balance = :closingLedgBal, "
				+ "available_balance =:closingAvailBal, TOPUP_STATUS=:topupStatus " + "WHERE account_id = :accountId "
				+ "AND purse_id = :purseId "
				+ "AND account_purse_id= :accountPurseId and ledger_balance=:previousLedgBalance and available_balance=:previousAvailBalance";

		return getJdbcTemplate().update(query, closingLedgBalance, closingAvailBalance, topupStatus, accountId, purseId, accountPurseId,
				previousLedgBalance, previousAvailBalance);

	}

	@Override
	public int addAccountPurseUsage(long accountId, long purseId, String usageFees, String usageLimits,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime lastTransactionDate) {

		return getJdbcTemplate().update(ADD_ACCOUNT_PURSE_USAGE, accountId, purseId, usageFees, usageLimits,
				lastTransactionDate == null ? null : Timestamp.valueOf(lastTransactionDate), accountPurseGroupStatus.getPurseStatusType()
					.getStatusCode(),
				map(accountPurseGroupStatus.getStartDate()), map(accountPurseGroupStatus.getEndDate()));

	}

	@Override
	public int updateAccountPurseUsage(long accountId, long purseId, String usageFees, String usageLimits,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime lastTransactionDate) {

		return getJdbcTemplate().update(UPDATE_ACCOUNT_PURSE_USAGE_QUERY, usageFees, usageLimits, map(lastTransactionDate),
				accountPurseGroupStatus.getPurseStatusType()
					.getStatusCode(),
				map(accountPurseGroupStatus.getStartDate()), map(accountPurseGroupStatus.getEndDate()), accountId, purseId);
	}

	@Override
	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, long productId, long purseId) {
		return getJdbcTemplate().query(GET_ACCOUNT_PURSE_BALANCE_QUERY, this.acountPurseBalanceRowMapper, accountId, productId, purseId);
	}

	@Override
	public AccountPurseUsageDto getAccountPurseUsage(long accountId, long purseId) {
		List<AccountPurseUsageDto> accountPurseUsageDtos = getJdbcTemplate().query(GET_ACCOUNT_PURSE_USAGE_BY_PURSE_ID,
				this.accountPurseUsageRowMapper, accountId, purseId);
		return CollectionUtils.isEmpty(accountPurseUsageDtos) ? null : accountPurseUsageDtos.get(0);
	}

	@Override
	public List<AccountPurseUsageDto> getAccountPurseUsage(long accountId) {
		return getJdbcTemplate().query(GET_ACCOUNT_PURSE_USAGE_BY_ACCOUNT_ID, this.accountPurseUsageRowMapper, accountId);
	}

	public AccountPurseBalance getAccountBalance(long accountPurseId) {
		return getJdbcTemplate().query(GET_ACCOUNT_BALANCE, this.acountPurseBalanceRowMapper, accountPurseId)
			.get(0);
	}

	public int addAccountPurse(AccountPurseBalance accountPurse) {
		try {
			int count = getJdbcTemplate().update(ADD_ACCOUNT_PURSE, //
					accountPurse.getAccountId(), //
					accountPurse.getProductId(), //
					accountPurse.getPurseId(), //
					accountPurse.getLedgerBalance(), //
					accountPurse.getAvailableBalance(), //
					accountPurse.getPurseType(), //
					accountPurse.getCurrencyCode(), //
					accountPurse.getPurseTypeId(), //
					accountPurse.getAccountPurseId(), //
					DateTimeUtil.map(accountPurse.getEffectiveDate()), //
					DateTimeUtil.map(accountPurse.getExpiryDate()), //
					accountPurse.getSkuCode(), accountPurse.getTopupStatus());
			if (count == 0) {
				throw new SQLIntegrityConstraintViolationException();
			}
			return count;
		} catch (SQLIntegrityConstraintViolationException e) {
			log.warn("Unique constraint voilation while inserting into account_purse: " + e.getMessage(), e);
			throw new ServiceException();
		}
	}

	private RowMapper<AccountPurseBalance> getAccountPurseBalanceRowMapper() {
		return (rs, rowNum) -> AccountPurseBalance.builder()
			.accountId(rs.getLong("ACCOUNT_ID"))
			.purseId(rs.getLong("PURSE_ID"))
			.productId(rs.getLong("PRODUCT_ID"))
			.accountPurseId(rs.getLong("ACCOUNT_PURSE_ID"))
			.ledgerBalance(rs.getBigDecimal("LEDGER_BALANCE"))
			.availableBalance(rs.getBigDecimal("AVAILABLE_BALANCE"))
			.effectiveDate(DateTimeUtil.map(rs.getTimestamp("EFFECTIVE_DATE")))
			.expiryDate(DateTimeUtil.map(rs.getTimestamp("EXPIRY_DATE")))
			.purseType(rs.getString("PURSE_TYPE"))
			.currencyCode(rs.getString("CURRENCY_CODE"))
			.purseTypeId(rs.getInt("purse_type_id"))
			.skuCode(rs.getString("SKU_CODE"))
			.topupStatus(rs.getString("TOPUP_STATUS"))
			.build();

	}

	private RowMapper<AccountPurseUsageDto> getAccountPurseUsageRowMapper() {

		return (rs, rowNum) -> AccountPurseUsageDto.builder()
			.accountId(rs.getLong("account_id"))
			.purseId(rs.getLong("purse_id"))
			.lastTransactionDate(DateTimeUtil.map(rs.getTimestamp("last_txndate")))
			.usageFees(JsonUtil.jsontoMap(rs.getString("usage_fee")))
			.usageLimits(JsonUtil.jsontoMap(rs.getString("usage_limit")))
			.purseStatus(rs.getString("PURSE_STATUS"))
			.startDate(DateTimeUtil.map(rs.getTimestamp("START_DATE")))
			.endDate(DateTimeUtil.map(rs.getTimestamp("END_DATE")))
			.build();

	}

	private RowMapper<AccountBalance> getAccountBalanceRowMapper() {
		return (rs, nowNum) -> AccountBalance.builder()
			.accountPurseId(rs.getLong("ACCOUNT_PURSE_ID"))
			.purseName(rs.getString("PURSE_NAME"))
			.availableBalance(rs.getBigDecimal("AVAILABLE_BALANCE"))
			.currencyCode(rs.getString("CURRENCY_CODE"))
			.expiryDate(DateTimeUtil.map(rs.getTimestamp("EXPIRY_DATE")))
			.skuCode(rs.getString("SKU_CODE") == null ? "" : rs.getString("SKU_CODE"))
			.build();
	}

	public List<AccountPurseBalance> getAllAccountPurseBalances(BigInteger accountId, BigInteger productId) {
		return getJdbcTemplate().query(GET_ALL_ACCOUNT_PURSE_BALANCE_QUERY, this.acountPurseBalanceRowMapper, accountId, productId);
	}

	@Override
	public int updatePurseStatus(long accountId, long purseId, String statusCode, LocalDateTime lastTransactionDate,
			LocalDateTime startDate, LocalDateTime endDate) {
		return getJdbcTemplate().update(UPDATE_PURSE_STATUS_QUERY, statusCode, map(lastTransactionDate), map(startDate), map(endDate),
				accountId, purseId);
	}

	@Override
	public List<AccountPurseBalance> getActiveAccountPurseBalances(long accountId, long productId, long purseId) {
		return getJdbcTemplate().query(GET_ACTIVE_ACCOUNT_PURSE, this.acountPurseBalanceRowMapper, accountId, productId, purseId);
	}

	@Override
	public List<AccountPurseBalance> getExpiredAccountPurseBalances(long accountId, long productId, long purseId) {
		return getJdbcTemplate().query(GET_EXPIRED_ACCOUNT_PURSE_BALANCE_QUERY, this.acountPurseBalanceRowMapper, accountId, productId,
				purseId);
	}

	@Override
	public List<AccountPurseBalance> getActiveAccountPurseBalance(long accountId, long accountPurseId) {
		return getJdbcTemplate().query(GET_ACTIVE_ACCOUNT_PURSE_BALANCE_, this.acountPurseBalanceRowMapper, accountId, accountPurseId);
	}

	@Override
	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, long purseId, String skuCode) {
		return getJdbcTemplate().query(GET_ACTIVE_ACCOUNT_PURSE_BALANCES, this.acountPurseBalanceRowMapper, accountId, purseId, skuCode);
	}

	@Override
	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, long purseId, boolean purseStatus) {
		return getJdbcTemplate().query(GET_ACCOUNT_PURSE_BALANCES, this.acountPurseBalanceRowMapper, accountId, purseId, purseStatus);
	}

	@Override
	public List<AccountBalance> getActiveAccountPurses(long accountId) {
		return getJdbcTemplate().query(GET_ALL_ACTIVE_PURSE_ACCOUNT_BALANCES, this.accountBalanceRowMapper, accountId);
	}

	@Override
	public List<AccountPurseBalance> getAccountPurseBalances(long accountId, Optional<Long> purseId, AccountPurseStateType purseStateType) {

		if (purseStateType.isSelectedPurseIdOnly() && not(purseId.isPresent())) {
			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR,
					"state to load has selectedPurseId set but purseId parameter is empty.");
		}

		if (purseStateType.isSkipAccountPurseLoad()) {
			return Collections.emptyList();
		}

		String query = this.getAccountBalanceQueryString(purseStateType);

		return purseStateType.isSelectedPurseIdOnly()
				? getJdbcTemplate().query(query, this.acountPurseBalanceRowMapper, accountId, purseId.get())
				: getJdbcTemplate().query(query, this.acountPurseBalanceRowMapper, accountId);
	}

	private String getAccountBalanceQueryString(AccountPurseStateType purseStateType) {
		StringBuilder query = new StringBuilder(GET_ACCOUNT_PURSE_BALANCE_QUERY_BASE);

		if (purseStateType.isUnexpiredOnly()) {
			query.append(UNEXPIRED_CLAUSE);
		}
		if (purseStateType.isSelectedPurseIdOnly()) {
			query.append(PURSE_ID_CLAUSE);
		}

		return query.toString();
	}

	private Timestamp map(LocalDateTime dateTime) {
		return dateTime == null ? null : Timestamp.valueOf(dateTime);
	}

}
