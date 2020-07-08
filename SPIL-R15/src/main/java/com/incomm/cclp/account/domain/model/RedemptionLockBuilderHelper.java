package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.incomm.cclp.account.application.command.TransactionInfo;
import com.incomm.cclp.account.domain.model.RedemptionLockUpdate.RedemptionLockUpdateBuilder;
import com.incomm.cclp.account.util.DateTimeFormatType;
import com.incomm.cclp.account.util.DateTimeUtil;

public class RedemptionLockBuilderHelper {
	private final RedemptionLockUpdateBuilder builder;

	private RedemptionLockBuilderHelper(RedemptionLockUpdateBuilder builder) {
		super();
		this.builder = builder;
	}

	public RedemptionLockUpdate build() {
		LocalDateTime currentDateTime = LocalDateTime.now();
		return builder.insDate(currentDateTime)
			.build();
	}

	public static RedemptionLockBuilderHelper from(RedemptionLockKey redemptionLockKey) {
		return new RedemptionLockBuilderHelper(RedemptionLockUpdate.builder()
			.redemptionLockKey(redemptionLockKey));
	}

	public static RedemptionLockBuilderHelper from(long transactionSeqId, long accountPurseId, long purseId) {
		return new RedemptionLockBuilderHelper(RedemptionLockUpdate.builder()
			.redemptionLockKey(RedemptionLockKey.from(purseId, accountPurseId, transactionSeqId)));
	}

	public RedemptionLockBuilderHelper card(CardEntity cardEntity) {
		builder.accountId(cardEntity.getAccountId())
			.cardNumberEncrypted(cardEntity.getCardNumberEncrypted())
			.cardNumberHash(cardEntity.getCardNumberHash());
		return this;
	}

	public RedemptionLockBuilderHelper command(TransactionInfo transactionInfo) {
		LocalDateTime currentDateTime = LocalDateTime.now();
		builder.deliveryChannelType(transactionInfo.getDeliveryChannelType())
			.correlationId(transactionInfo.getCorrelationId())
			.storeId(transactionInfo.getStoreId())
			.terminalId(transactionInfo.getTerminalId())
			.transactionDate(DateTimeUtil.convert(currentDateTime, DateTimeFormatType.YYYYMMDD))
			.transactionTime(DateTimeUtil.convert(currentDateTime, DateTimeFormatType.HHMMSS));
		return this;
	}

	public RedemptionLockBuilderHelper account(BigDecimal transactionAmount, BigDecimal transactionFee, BigDecimal authorizedAmount,
			BigDecimal openingLedgerBalance, BigDecimal openingAvailableBalance, BigDecimal closingAvailableBalance) {
		builder.transactionAmount(transactionAmount.equals(BigDecimal.ZERO) ? transactionFee : transactionAmount)
			.authorizedAmount(authorizedAmount)
			.transactionFee(transactionFee)
			.previousLedgerBalance(openingLedgerBalance)
			.closingLedgerBalance(openingLedgerBalance)
			.previousAvailableBalance(openingAvailableBalance)
			.closingAvailableBalance(closingAvailableBalance);
		return this;
	}

	public RedemptionLockBuilderHelper lock(String lockFound, String lockFlag, LocalDateTime lockExpiryDate, boolean isNew) {
		builder.lockFlag(lockFlag)
			.lockExpiryDate(lockExpiryDate)
			.lockFound(lockFound)
			.isNew(isNew);
		return this;
	}

	public RedemptionLockBuilderHelper unLock(String unlockCorrelationId, LocalDateTime unlockDate) {
		builder.unlockCorrelationId(unlockCorrelationId)
			.unlockDate(unlockDate);
		return this;
	}
}
