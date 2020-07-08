package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AccountPurseGroupInterface {

	public long getPurseId();

	public String getPurseName();

	////////////////////
	// CREDIT
	////////////////////

	public AccountPurseGroupUpdate creditPurseBalance( //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean createPurseIfNoneExist);

	public AccountPurseGroupUpdate creditPurseBalance( //
			AccountPurseAltKeyAttributes attributes, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean createPurseIfNoneExist);

	public AccountPurseGroupUpdate creditPurseBalance( //
			long accountPurseId, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType);

	////////////////////
	// DEBIT
	////////////////////

	public AccountPurseGroupUpdate debitPurseBalance(//
			long accountPurseId, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth);

	public AccountPurseGroupUpdate debitPurseBalance( //
			AccountPurseAltKeyAttributes attributes, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth);

	public AccountPurseGroupUpdate debitPurseBalance( //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth);

	//////////////////////
	// Redemption Unlock
	//////////////////////

	public AccountPurseGroupUpdate debitLockedPurseBalance(//
			List<RedemptionLockEntity> redemptionLocks, //
			long accountPurseId, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			BigDecimal unlockTransactionAmount, //
			BigDecimal lockTransactionAmount);

	public AccountPurseGroupUpdate debitLockedPurseBalance(//
			List<RedemptionLockEntity> redemptionLocks, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			BigDecimal unlockTransactionAmount, //
			BigDecimal lockTransactionAmount);

	////////////////////
	// Reversal
	////////////////////
	public AccountPurseGroupUpdate revertPurseBalance( //
			List<LedgerEntry> ledgerEntries, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType);

	public void updateLimitsAndFee(final Map<String, Object> fees, final Map<String, Object> limits);

	public void setMaxBalance(BigDecimal balance);

	public void validatePurseStatus();

	public BigDecimal getAvailablePurseBalance();

	public boolean isDefaultPurse();

}