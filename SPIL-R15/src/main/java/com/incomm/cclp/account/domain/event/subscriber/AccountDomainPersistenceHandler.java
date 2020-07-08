package com.incomm.cclp.account.domain.event.subscriber;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.account.domain.event.AccountPurseUpdatedEvent;
import com.incomm.cclp.account.domain.event.DomainEvent;
import com.incomm.cclp.account.domain.event.PurseStatusUpdatedEvent;
import com.incomm.cclp.account.domain.event.TransactionFailedEvent;
import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.domain.model.AccountPurseUpdate;
import com.incomm.cclp.account.domain.model.PurseUpdate;
import com.incomm.cclp.account.util.CodeUtil;
import com.incomm.cclp.dao.service.LogService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.AccountPurseBalance;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.repos.GenericRepo;
import com.incomm.cclp.repos.StatementsLogRepository;
import com.incomm.cclp.repos.TransactionLogRepository;
import com.incomm.cclp.service.AccountPurseService;
import com.incomm.cclp.service.CardDetailsService;

import lombok.EqualsAndHashCode;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
@EqualsAndHashCode
public class AccountDomainPersistenceHandler implements DomainEventSubscriber {

	@Autowired
	private LogService logService;

	@Autowired
	private StatementsLogRepository stmtLogRepository;

	@Autowired
	private TransactionLogRepository transactionLogRepository;

	@Autowired
	private AccountPurseService accountPurseService;

	@Autowired
	private CardDetailsService cardDetailsService;

	@Autowired
	private SequenceService sequenceService;

	@Autowired
	private GenericRepo genericRepository;

	@Override
	@Transactional
	public void handle(List<DomainEvent> events) {
		log.info("Received list of domain update events for saving to database.");
		events.stream()
			.forEach(this::handle);
	}

	@Override
	@Transactional
	public void handle(DomainEvent event) {
		log.info("Received the domain update event for saving to database.");

		if (event instanceof AccountPurseUpdatedEvent) {
			this.handleEvent((AccountPurseUpdatedEvent) event);
		} else if (event instanceof PurseStatusUpdatedEvent) {
			this.handleEvent((PurseStatusUpdatedEvent) event);
		} else if (event instanceof TransactionFailedEvent) {
			this.handleEvent((TransactionFailedEvent) event);
		} else {
			log.warn("No handlers for event in AccountDomainPersistenceHandler:" + event);
		}

	}

	private void handleEvent(PurseStatusUpdatedEvent event) {

		log.info("Persisting the PurseStatusUpdatedEvent.");

		this.updatePurseStatus(event.getPurseUpdate(), event.getTransactionDate());

		this.addStatementLog(event.getStatementLogs());
		this.addTransactionLog(event.getTransactionLogs());
	}

	private void handleEvent(TransactionFailedEvent event) {
		this.addTransactionLog(event.getTransactionLogs());
	}

	@Override
	public boolean isSubscribed(DomainEvent domainEventClass) {
		log.warn("isSubscribed is not yet implemented.");

		throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR, "isSubscribed is not yet implemented.");
	}

	private void handleEvent(AccountPurseUpdatedEvent event) {
		log.info("Persisting AccountPurseUpdatedEvent.");

		event.getAccountPurseUpdates()
			.forEach(accountPurseUpdate -> {

				int updateCount = accountPurseUpdate.isNewAccountPurse() ? this.addAccountPurse(accountPurseUpdate)
						: this.updateAccountPurse(accountPurseUpdate);

				if (updateCount == 0) {
					throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR,
							"Unable to update account purse table:" + accountPurseUpdate.toString());
				}

				updateCount = accountPurseUpdate.isNewPurseUsage()
						? this.addAccountPurseUsage(accountPurseUpdate, event.getTransactionDate())
						: this.updateAccountPurseUsage(accountPurseUpdate, event.getTransactionDate());

				if (updateCount == 0) {
					throw DomainExceptionFactory.from(DomainExceptionType.SYSTEM_ERROR,
							"Unable to update account purse table:" + accountPurseUpdate.toString());
				}

			});
		// call logs

		this.addStatementLog(event.getStatementLogs());
		this.addTransactionLog(event.getTransactionLogs());
	}

	private int updateAccountPurse(AccountPurseUpdate accountPurseUpdate) {

		log.info("updating account purse:" + accountPurseUpdate.toString());

		return accountPurseService.updateAccountPurse(accountPurseUpdate.getAccountPurseKey()
			.getAccountId(),
				accountPurseUpdate.getAccountPurseKey()
					.getPurseId(),
				accountPurseUpdate.getAccountPurseKey()
					.getAccountPurseId(),
				accountPurseUpdate.getNewLedgerBalance(), accountPurseUpdate.getNewAvailableBalance(),
				accountPurseUpdate.getPreviousLedgerBalance(), accountPurseUpdate.getPreviousAvailableBalance(),
				accountPurseUpdate.getTopupStatus());
	}

	private int addAccountPurse(AccountPurseUpdate accountPurseUpdate) {

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
			.skuCode(accountPurseUpdate.getSkuCode())
			.purseTypeId(accountPurseUpdate.getPursetype()
				.getPurseTypeId())
			.purseType(accountPurseUpdate.getPursetype()
				.name())
			.currencyCode(accountPurseUpdate.getCurrencyCode())
			.effectiveDate(accountPurseUpdate.getEffectiveDate())
			.expiryDate(accountPurseUpdate.getExpiryDate())
			.firstLoadDate(LocalDateTime.now())
			.topupStatus(accountPurseUpdate.getTopupStatus())
			.build();

		log.info("adding account purse:" + accountPurseBalance.toString());

		return accountPurseService.addAccountPurse(accountPurseBalance);
	}

	private int addAccountPurseUsage(AccountPurseUpdate accountPurseUpdate, LocalDateTime transactionDate) {

		log.info("adding account purse usage:" + accountPurseUpdate.getAccountPurseKey()
			.toString());

		return accountPurseService.addAccountPurseUsage(accountPurseUpdate.getAccountPurseKey()
			.getAccountId(),
				accountPurseUpdate.getAccountPurseKey()
					.getPurseId(),
				accountPurseUpdate.getNewUsageFee(), accountPurseUpdate.getNewUsageLimit(), accountPurseUpdate.getNewGroupStatus(),
				transactionDate);
	}

	private int updateAccountPurseUsage(AccountPurseUpdate accountPurseUpdate, LocalDateTime transactionDate) {

		log.info("Updating account purse usage:" + accountPurseUpdate.getAccountPurseKey()
			.toString());

		return accountPurseService.updateAccountPurseUsage(accountPurseUpdate.getAccountPurseKey()
			.getAccountId(),
				accountPurseUpdate.getAccountPurseKey()
					.getPurseId(),
				accountPurseUpdate.getNewUsageFee(), accountPurseUpdate.getNewUsageLimit(), accountPurseUpdate.getNewGroupStatus(),
				transactionDate);
	}

	private int updatePurseStatus(PurseUpdate purseUpdate, LocalDateTime transactionDate) {

		log.info("Updating the purse status to:{}", purseUpdate.getNewStatus());

		return accountPurseService.updatePurseStatus(purseUpdate.getPurseKey()
			.getAccountId(),
				purseUpdate.getPurseKey()
					.getPurseId(),
				purseUpdate.getNewStatus(), transactionDate, purseUpdate.getNewStartDate(), purseUpdate.getNewEndDate());
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
