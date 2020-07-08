package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.account.domain.model.RedemptionLockKey;
import com.incomm.cclp.account.domain.model.RedemptionLockUpdate;
import com.incomm.cclp.dao.RedemptionLockDao;
import com.incomm.cclp.exception.ServiceException;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Repository
public class RedemptionLockDaoImpl extends JdbcDaoSupport implements RedemptionLockDao {

	@Autowired
	private EntityManager em;

	@Autowired
	DataSource dataSource;

	RowMapper<RedemptionLockEntity> redemptionLockEntityRowMapper;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
		this.redemptionLockEntityRowMapper = this.getRedemptionLockEntityRowMapper();
	}

	private static final String UPDATE_REDEMPTION_LOCK_FLAG = "UPDATE redemption_lock SET lock_flag = :lockFlag WHERE lock_flag = 'Y' AND card_num_hash =:cardNumberHash AND rrn =:rrn "
			+ "AND account_purse_id = :accountPurseId AND CLOSING_BALANCE=:closingLedgerBalance AND AVAIL_CLOSING_BALANCE =:closingAvailableBalance";

	private static final String ADD_NEW_REDEMPTION_LOCK = "INSERT INTO redemption_lock (card_num_hash,account_id,purse_id,delivery_channel,rrn,transaction_date,transaction_time,transaction_amount,"
			+ "auth_amount,tranfee_amount,lock_flag,ins_date,store_id,terminal_id,TRANSACTION_SQID,account_purse_id,OPENING_BALANCE,CLOSING_BALANCE,AVAIL_OPENING_BALANCE,AVAIL_CLOSING_BALANCE) "
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?)";

	private static final String GET_REDEMPTION_LOCKS_FOR_CARD_BY_LOCK_FLAG = "select purse_id,account_purse_id,transaction_sqid,transaction_date,transaction_time,rrn,unlock_rrn,transaction_amount,"
			+ "auth_amount,tranfee_amount,opening_balance as opening_ledger_balance,closing_balance as closing_ledger_balance,avail_opening_balance as opening_available_balance,"
			+ "avail_closing_balance as closing_available_balance,lock_flag,lock_found,card_num_hash as card_number_hash from redemption_lock "
			+ "where lock_flag = :lockFlag AND card_num_hash = :cardNumberHash";

	@Override
	public List<RedemptionLockEntity> getRedemptionLocks(String cardNumberHash, String lockFlag) {
		return getJdbcTemplate().query(GET_REDEMPTION_LOCKS_FOR_CARD_BY_LOCK_FLAG, this.redemptionLockEntityRowMapper, cardNumberHash,
				lockFlag);
	}

	@Override
	public int addRedemptionLock(RedemptionLockUpdate redemptionLockUpdate) {
		try {
			int count = getJdbcTemplate().update(ADD_NEW_REDEMPTION_LOCK, //
					redemptionLockUpdate.getCardNumberHash(), //
					redemptionLockUpdate.getAccountId(), //
					redemptionLockUpdate.getRedemptionLockKey()
						.getPurseId(), //
					redemptionLockUpdate.getDeliveryChannelType()
						.getChannelCode(), //
					redemptionLockUpdate.getCorrelationId(), //
					redemptionLockUpdate.getTransactionDate(), //
					redemptionLockUpdate.getTransactionTime(), //
					redemptionLockUpdate.getTransactionAmount(), //
					redemptionLockUpdate.getAuthorizedAmount(), //
					redemptionLockUpdate.getTransactionFee(), //
					redemptionLockUpdate.getLockFlag(), //
					redemptionLockUpdate.getStoreId(), //
					redemptionLockUpdate.getTerminalId(), //
					redemptionLockUpdate.getRedemptionLockKey()
						.getTransactionSeqId(), //
					redemptionLockUpdate.getRedemptionLockKey()
						.getAccountPurseId(), //
					redemptionLockUpdate.getPreviousLedgerBalance(), //
					redemptionLockUpdate.getClosingLedgerBalance(), //
					redemptionLockUpdate.getPreviousAvailableBalance(), //
					redemptionLockUpdate.getClosingAvailableBalance());
			if (count == 0) {
				throw new SQLIntegrityConstraintViolationException();
			}
			return count;
		} catch (SQLIntegrityConstraintViolationException e) {
			log.warn("Unique constraint voilation while inserting into redemptionLock table: " + e.getMessage(), e);
			throw new ServiceException();
		}
	}

	@Override
	public int updateRedemptionLock(RedemptionLockUpdate redemptionLockUpdate) {
		return getJdbcTemplate().update(UPDATE_REDEMPTION_LOCK_FLAG, //
				redemptionLockUpdate.getLockFlag(), //
				redemptionLockUpdate.getCardNumberHash(), //
				redemptionLockUpdate.getCorrelationId(), //
				redemptionLockUpdate.getRedemptionLockKey()
					.getAccountPurseId(), //
				redemptionLockUpdate.getClosingLedgerBalance(), //
				redemptionLockUpdate.getClosingAvailableBalance());
	}

	private RowMapper<RedemptionLockEntity> getRedemptionLockEntityRowMapper() {
		return (rs, rowNum) -> RedemptionLockEntity.builder()
			.redemptionLockKey(
					RedemptionLockKey.from(rs.getLong("PURSE_ID"), rs.getLong("ACCOUNT_PURSE_ID"), rs.getLong("TRANSACTION_SQID")))
			.transactionDate(rs.getString("TRANSACTION_DATE"))
			.transactionTime(rs.getString("TRANSACTION_TIME"))
			.correlationId(rs.getString("RRN"))
			.unlockCorrelationId(rs.getString("UNLOCK_RRN"))
			.transactionAmount(new BigDecimal(rs.getString("TRANSACTION_AMOUNT")))
			.authorizedAmount(new BigDecimal(rs.getString("AUTH_AMOUNT")))
			.transactionFee(new BigDecimal(rs.getString("TRANFEE_AMOUNT")))
			.previousLedgerBalance(new BigDecimal(rs.getString("OPENING_LEDGER_BALANCE")))
			.closingLedgerBalance(new BigDecimal(rs.getString("CLOSING_LEDGER_BALANCE")))
			.previousAvailableBalance(new BigDecimal(rs.getString("OPENING_AVAILABLE_BALANCE")))
			.closingAvailableBalance(new BigDecimal(rs.getString("CLOSING_AVAILABLE_BALANCE")))
			.lockFlag(rs.getString("LOCK_FLAG"))
			.lockFound(rs.getString("LOCK_FOUND"))
			.cardNumberHash(rs.getString("CARD_NUMBER_HASH"))
			.build();
	}
}
