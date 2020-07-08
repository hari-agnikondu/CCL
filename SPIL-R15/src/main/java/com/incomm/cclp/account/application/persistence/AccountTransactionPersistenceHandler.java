package com.incomm.cclp.account.application.persistence;

import static com.incomm.cclp.account.util.CodeUtil.not;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.account.domain.event.TransactionFailedEvent;
import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.model.AccountAggregateUpdate;
import com.incomm.cclp.account.domain.model.AccountPurseGroupKey;
import com.incomm.cclp.account.domain.model.AccountPurseGroupStatus;
import com.incomm.cclp.account.domain.model.AccountPurseGroupUpdate;
import com.incomm.cclp.account.domain.model.AccountPurseUpdateNew;
import com.incomm.cclp.account.domain.model.AccountUpdate;
import com.incomm.cclp.account.domain.model.CardUpdate;
import com.incomm.cclp.account.domain.model.PurseType;
import com.incomm.cclp.account.domain.model.RedemptionLockUpdate;
import com.incomm.cclp.account.domain.model.TransactionLogUpdate;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.repos.StatementsLogRepository;
import com.incomm.cclp.repos.TransactionLogRepository;
import com.incomm.cclp.service.AccountPurseService;
import com.incomm.cclp.service.AccountService;
import com.incomm.cclp.service.CardDetailsService;
import com.incomm.cclp.service.RedemptionLockService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AccountTransactionPersistenceHandler implements AccountTransactionPersistenceHandlerInterface {

	// @formatter:off
	@Autowired private StatementsLogRepository stmtLogRepository;
	@Autowired private TransactionLogRepository transactionLogRepository;
	@Autowired private AccountService accountService;
	@Autowired private AccountPurseService accountPurseService;
	@Autowired private CardDetailsService cardDetailsService;
	@Autowired private SequenceService sequenceService;
	@Autowired private GenericRepo genericRepository;
	@Autowired private RedemptionLockService redemptionLockService;
	// @formatter:on

	@Override
	@Transactional
	public void persist(AccountTransactionUpdate accountTransactionUpdate) {

		try {

			log.info("Persisting accountTransactionUpdate to the database.");

			this.save(accountTransactionUpdate.getAccountAggregateUpdates(), accountTransactionUpdate.getTransactionDate());

			accountTransactionUpdate.getTransactionLogUpdate()
				.ifPresent(this::updateTransactionLog);

			this.addTransactionLog(accountTransactionUpdate.getTransactionLogs());

			this.addStatementLog(accountTransactionUpdate.getStatementLogs());

			this.save(accountTransactionUpdate.getRedemptionLocks());

		} catch (Exception exception) {
			log.warn("Error occured while persisting the accountTransactionUpdate", exception);
			throw exception;
		}

	}

	private void updateTransactionLog(TransactionLogUpdate transactionLogUpdate) {

		log.info("Updating the tranaction log:{}", transactionLogUpdate.toString());

		this.genericRepository.updateTransactionReversalFlag(//
				transactionLogUpdate.getCardNumberHash(), //
				transactionLogUpdate.getTransactionType()
					.getTransactionCode(), //
				transactionLogUpdate.getCorrelationId(), //
				transactionLogUpdate.getIsReversedFlag());
	}

	@Override
	@Transactional
	public void persist(TransactionFailedEvent event) {
		log.info("Persisting the transaction failed object.");
		this.addTransactionLog(event.getTransactionLogs());
	}

	private void save(List<AccountAggregateUpdate> accountAggregateUpdates, LocalDateTime transactionDate) {
		accountAggregateUpdates.forEach(accountAggregateUpdate -> this.save(accountAggregateUpdate, transactionDate));
	}

	private void save(AccountAggregateUpdate accountAggregateUpdate, LocalDateTime transactionDate) {

		log.info("Received the accountAggregateUpdate object for saving to database.");

		accountAggregateUpdate.getCardUpdate()
			.ifPresent(this::save);

		accountAggregateUpdate.getAccountUpdate()
			.ifPresent(this::save);

		if (not(CollectionUtils.isEmpty(accountAggregateUpdate.getAccountPurseGroupUpdate()))) {
			this.saveAccountPurseGroupUpdate(accountAggregateUpdate.getAccountPurseGroupUpdate(), transactionDate);
		}

	}

	private void save(List<RedemptionLockUpdate> redemptionLockUpdates) {
		if (not(CollectionUtils.isEmpty(redemptionLockUpdates))) {
			redemptionLockUpdates.forEach(this::save);
		}

	}

	private void save(RedemptionLockUpdate redemptionLockUpdate) {

		if (redemptionLockUpdate.isNew()) {
			log.info("adding redemption lock to the databae:{}", redemptionLockUpdate.toString());

			this.addRedemptionLock(redemptionLockUpdate);
		} else {
			log.info("Updating redemption lock to the databae:{}", redemptionLockUpdate.toString());
			int updateCount = this.updateRedemptionLock(redemptionLockUpdate);

			if (updateCount == 0) {
				log.info("Unable to update redemptionLock table");
				throw new ServiceException(SpilExceptionMessages.ERROR_UPDATING_REDEMPTION_LOCKS, ResponseCodes.SYSTEM_ERROR);
			}
		}
	}

	private void saveAccountPurseGroupUpdate(List<AccountPurseGroupUpdate> accountPurseGroupUpdates, LocalDateTime transactionDate) {
		accountPurseGroupUpdates.forEach(accountPurseGroupUpdate -> this.save(accountPurseGroupUpdate, transactionDate));
	}

	private void save(AccountPurseGroupUpdate accountPurseGroupUpdate, LocalDateTime transactionDate) {

		if (not(CollectionUtils.isEmpty(accountPurseGroupUpdate.getAccountPurseUpdates()))) {
			accountPurseGroupUpdate.getAccountPurseUpdates()
				.forEach(accountPurseUpdate -> this.save(accountPurseGroupUpdate.getPurseType(), accountPurseGroupUpdate.getCurrencyCode(),
						accountPurseUpdate));
		}

		int updateCount = accountPurseGroupUpdate.isNew()
				? this.addAccountPurseUsage(accountPurseGroupUpdate.getAccountPurseGroupKey(), accountPurseGroupUpdate.getNewUsageFee(),
						accountPurseGroupUpdate.getNewUsageLimit(), accountPurseGroupUpdate.getNewStatus(), transactionDate)

				: this.updateAccountPurseUsage(accountPurseGroupUpdate.getAccountPurseGroupKey(), accountPurseGroupUpdate.getNewUsageFee(),
						accountPurseGroupUpdate.getNewUsageLimit(), accountPurseGroupUpdate.getNewStatus(), transactionDate);

		if (updateCount == 0) {
			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR,
					"Unable to update account purse table:" + accountPurseGroupUpdate.toString());
		}
	}

	private void save(PurseType purseType, String currencyCode, AccountPurseUpdateNew accountPurseUpdate) {
		if (accountPurseUpdate.isNew()) {
			log.info("creating new account purse with purseId: {}", accountPurseUpdate.getAccountPurseKey()
				.getPurseId());
			this.addAccountPurse(purseType, currencyCode, accountPurseUpdate);
		} else {
			log.info("Updating account purse with accountPurseId: {}", accountPurseUpdate.getAccountPurseKey()
				.getAccountPurseId());
			int updateCount = this.updateAccountPurse(accountPurseUpdate);

			if (updateCount == 0) {
				log.error("Unable to update account purse table for accountPurseId: {}", accountPurseUpdate.getAccountPurseKey()
					.getAccountPurseId());
				throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, "Unable to update account purse table");
			}
		}
	}

	private int updateAccountPurse(AccountPurseUpdateNew accountPurseUpdate) {

		log.info("Updating account purse:" + accountPurseUpdate.toString());

		return accountPurseService.updateAccountPurse(accountPurseUpdate.getAccountPurseKey()
			.getAccountId(),
				accountPurseUpdate.getAccountPurseKey()
					.getPurseId(),
				accountPurseUpdate.getAccountPurseKey()
					.getAccountPurseId(),
				accountPurseUpdate.getNewLedgerBalance(), accountPurseUpdate.getNewAvailableBalance(),
				accountPurseUpdate.getPreviousLedgerBalance(), accountPurseUpdate.getPreviousAvailableBalance(),
				accountPurseUpdate.getTopUpStatus());
	}

	private int addRedemptionLock(RedemptionLockUpdate redemptionLockUpdate) {
		log.info("adding redemption lock with transactionId: {}", redemptionLockUpdate.getRedemptionLockKey()
			.getTransactionSeqId());
		return redemptionLockService.addRedemptionLock(redemptionLockUpdate);
	}

	private int updateRedemptionLock(RedemptionLockUpdate redemptionLockUpdate) {
		log.info("updating redemption lock for transactionId: {}", redemptionLockUpdate.getRedemptionLockKey()
			.getTransactionSeqId());
		return redemptionLockService.updateRedemptionLock(redemptionLockUpdate);
	}

	private int addAccountPurse(PurseType purseType, String currencyCode, AccountPurseUpdateNew accountPurseUpdate) {

		AccountPurseBalance accountPurseBalance = AccountPurseBalance.builder()
			.accountId(accountPurseUpdate.getAccountPurseKey()
				.getAccountId())
			.purseId(accountPurseUpdate.getAccountPurseKey()
				.getPurseId())
			.accountPurseId(accountPurseUpdate.getAccountPurseKey()
				.getAccountPurseId())
			.productId(accountPurseUpdate.getProductId())
			.availableBalance(accountPurseUpdate.getNewAvailableBalance())
			.ledgerBalance(accountPurseUpdate.getNewLedgerBalance())
			.skuCode(accountPurseUpdate.getAttributes()
				.getSkuCode())
			.purseTypeId(purseType.getPurseTypeId())
			.purseType(purseType.name())
			.currencyCode(currencyCode)
			.effectiveDate(accountPurseUpdate.getAttributes()
				.getEffectiveDate())
			.expiryDate(accountPurseUpdate.getAttributes()
				.getExpiryDate())
			.firstLoadDate(LocalDateTime.now())
			.build();
		log.info("adding account purse:" + accountPurseBalance.toString());
		return accountPurseService.addAccountPurse(accountPurseBalance);
	}

	private int addAccountPurseUsage(AccountPurseGroupKey groupKey, Map<String, Object> usageFee, Map<String, Object> usageLimit,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime transactionDate) {

		log.info("Adding the account purse usage, key:{}, status:{}", groupKey.toString(), accountPurseGroupStatus.toString());

		return accountPurseService.addAccountPurseUsage(groupKey.getAccountId(), groupKey.getPurseId(), usageFee, usageLimit,
				accountPurseGroupStatus, transactionDate);
	}

	private int updateAccountPurseUsage(AccountPurseGroupKey groupKey, Map<String, Object> usageFee, Map<String, Object> usageLimit,
			AccountPurseGroupStatus accountPurseGroupStatus, LocalDateTime transactionDate) {

		log.info("updating the account purse usage, key:{}, status:{}", groupKey.toString(), accountPurseGroupStatus.toString());

		return accountPurseService.updateAccountPurseUsage(groupKey.getAccountId(), groupKey.getPurseId(), usageFee, usageLimit,
				accountPurseGroupStatus, transactionDate);
	}

	private void save(AccountUpdate accountUpdate) {
		// update account
		log.info("updating the account table:{}", accountUpdate.toString());

		int updateCount = accountService.updateInitialLoadBalance(BigInteger.valueOf(accountUpdate.getAccountId()),
				accountUpdate.getInitialLoadAmount(), //
				accountUpdate.getNewInitialLoadAmount(), //
				accountUpdate.getPreviousInitialLoadAmount());
		if (updateCount == 0) {
			log.error("Unable to update Account new intial load amount");
			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, "Unable to update Account table:");
		}

	}

	private void save(CardUpdate cardUpdate) {
		// update card
		log.info("updating the card table: {}", cardUpdate.toString());

		int updateCount = cardDetailsService.updateCard(cardUpdate.getCardNumberHash(), cardUpdate.getNewCardStatus(),
				cardUpdate.getOldCardStatus(), cardUpdate.getActivationDate(), cardUpdate.getNewFirstTimeTopUpFlag());

		if (updateCount == 0) {
			throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, "Unable to update card table:" + cardUpdate.toString());
		}
	}

	private void addStatementLog(List<StatementsLog> stmtLog) {

		log.info("Saving statement logs, count:{}", stmtLog == null ? null : stmtLog.size());

		stmtLogRepository.saveAll(stmtLog);

	}

	private void addTransactionLog(List<TransactionLog> transactionLogs) {
		transactionLogs.stream()
			.filter(log -> log.getTransactionSqid() == null || CodeUtil.isNullOrEmpty(log.getRrn()))
			.forEach(log -> {
				if (log.getTransactionSqid() == null) {
					log.setTransactionSqid(this.sequenceService.getNextTxnSeqId());
				}

				if (CodeUtil.isNullOrEmpty(log.getRrn())) {
					log.setRrn(log.getTransactionSqid()
						.toString());
				}
			});

		log.info("Saving transaction logs, count:{}", transactionLogs.size());

		transactionLogRepository.saveAll(transactionLogs);
	}

}
