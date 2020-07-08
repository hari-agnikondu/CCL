package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.incomm.ccl.common.util.CodeUtil;
import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;

import lombok.Builder;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@Builder
public class RedemptionLockAggregate implements RedemptionLockAggregateInterface {
	private CardEntity cardEntity;
	private TransactionInfo transactionInfo;
	private List<RedemptionLockEntity> redemptionLocks;

	@Override
	public RedemptionLockUpdate getRedemptionLockUpdate(List<LedgerEntry> ledgerEntryList, long accountPurseId, long transactionSeqId,
			long purseId) {

		String lockFound = null;
		String lockFlag = "Y";
		LocalDateTime lockExpiryDate = null;
		boolean isNew = true;

		BigDecimal transactionFee = BigDecimal.ZERO;
		BigDecimal transactionAmount = BigDecimal.ZERO;
		BigDecimal authorizedAmount = BigDecimal.ZERO;
		BigDecimal openingAvailableBalance = BigDecimal.ZERO;
		BigDecimal openingLedgerBalance = BigDecimal.ZERO;

		log.info("Build redemptionLocks for accountPurseId: {} and transactionSqId: {}", accountPurseId, transactionSeqId);

		for (LedgerEntry ledgerEntry : ledgerEntryList) {
			if (ledgerEntry.getLedgerEntryType() == LedgerEntryType.FLAT_FEE
					|| ledgerEntry.getLedgerEntryType() == LedgerEntryType.PERCENT_FEE) {
				transactionFee = transactionFee.add(ledgerEntry.getTransactionAmount());
			} else {
				transactionAmount = transactionAmount.add(ledgerEntry.getTransactionAmount());
				authorizedAmount = authorizedAmount.add(ledgerEntry.getAuthorizedAmount());
			}

			if (openingLedgerBalance.compareTo(ledgerEntry.getPreviousLedgerBalance()) < 0) {
				openingLedgerBalance = ledgerEntry.getPreviousLedgerBalance();
			}

			if (openingAvailableBalance.compareTo(ledgerEntry.getPreviousAvailableBalance()) < 0) {
				openingAvailableBalance = ledgerEntry.getPreviousAvailableBalance();
			}
		}

		log.info("Redemption lock of transactionFee: {}, transactionAmount: {}, authorizedAmount: {}", transactionFee, transactionAmount,
				authorizedAmount);
		log.info("openingAvailableBalance: {}, openingLedgerBalance: {}", openingAvailableBalance, openingLedgerBalance);

		return RedemptionLockBuilderHelper.from(transactionSeqId, accountPurseId, purseId)
			.card(cardEntity)
			.command(transactionInfo)
			.account(transactionAmount, transactionFee, authorizedAmount, openingLedgerBalance, openingAvailableBalance,
					openingAvailableBalance.subtract(authorizedAmount)
						.subtract(transactionFee))
			.lock(lockFound, lockFlag, lockExpiryDate, isNew)
			.build();
	}

	@Override
	public List<RedemptionLockUpdate> revertRedemptionLockFlag(String rrn, long purseId) {
		log.info("Reverting redemption locks for rrn: {}, purseId: {}", rrn, purseId);
		List<RedemptionLockEntity> matchingRedemptionLocks = getMatchingRedemptionLock(rrn, purseId);

		if (CodeUtil.isNull(matchingRedemptionLocks)) {
			log.error("No matching redemption Locks found for rrn {} and purseId {}", rrn, purseId);
			throw new ServiceException(SpilExceptionMessages.CARD_NOT_LOCKED, ResponseCodes.CARD_NOT_LOCKED);
		}

		return matchingRedemptionLocks.stream()
			.map(this::map)
			.collect(Collectors.toList());

	}

	private RedemptionLockUpdate map(RedemptionLockEntity lockEntity) {
		String lockFound = null;
		String lockFlag = "N";
		LocalDateTime lockExpiryDate = null;
		boolean isNew = false;
		return RedemptionLockBuilderHelper.from(lockEntity.getRedemptionLockKey())
			.card(cardEntity)
			.command(transactionInfo)
			.account(lockEntity.getTransactionAmount(), lockEntity.getTransactionFee(), lockEntity.getAuthorizedAmount(),
					lockEntity.getPreviousLedgerBalance(), lockEntity.getPreviousAvailableBalance(),
					lockEntity.getClosingAvailableBalance())
			.lock(lockFound, lockFlag, lockExpiryDate, isNew)
			.build();
	}

	private List<RedemptionLockEntity> getMatchingRedemptionLock(String rrn, long purseId) {
		if (CodeUtil.isNull(redemptionLocks)) {
			log.error("No matching redemption Locks found for accountId ", cardEntity.getAccountId());
			throw new ServiceException(SpilExceptionMessages.CARD_NOT_LOCKED, ResponseCodes.CARD_NOT_LOCKED);
		}
		return this.redemptionLocks.stream()
			.filter(p -> p.getCorrelationId()
				.equals(rrn)
					&& p.getRedemptionLockKey()
						.getPurseId() == purseId)
			.collect(Collectors.toList());
	}

}
