package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AccountPurseAggregateInterface {

	/////////////////////
	// Credit methods
	/////////////////////

	/**
	 * Credits the transactionAmount to default selected account purse.
	 * 
	 * @param purseId
	 * @param transactionAmount
	 * @param transactionFees
	 * @param maxPurseBalance
	 * @return
	 */
	public AccountPurseGroupUpdate creditPurseBalance(long purseId, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			boolean createPurseIfNoneExist);

	/**
	 * Credits the transactionAmount to matching accountPurse with given attributes.
	 * 
	 * @param purseId
	 * @param attributes
	 * @param transactionAmount
	 * @param transactionFees
	 * @return
	 */
	public AccountPurseGroupUpdate creditPurseBalance(long purseId, //
			AccountPurseAltKeyAttributes attributes, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			boolean createPurseIfNoneExist);

	/**
	 * Credits the transactionAmount to given accontPurseId.
	 * 
	 * @param purseId
	 * @param accountPurseId
	 * @param transactionAmount
	 * @param transactionFees
	 * @param maxPurseBalance
	 * @return
	 */
	public AccountPurseGroupUpdate creditPurseBalance(long purseId, //
			long accountPurseId, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees);

	/////////////////////
	// Debit methods
	/////////////////////

	public AccountPurseGroupUpdate debitPurseBalance(long purseId, //
			AccountPurseAltKeyAttributes attributes, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth);

	public AccountPurseGroupUpdate debitPurseBalance(long purseId, //
			long accountPurseId, //
			BigDecimal transactionAmount, Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth);

	public AccountPurseGroupUpdate debitPurseBalance(long purseId, //
			BigDecimal transactionAmount, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType, //
			boolean allowPartialAuth);

	/////////////////////
	// Reversal methods
	/////////////////////

	public AccountPurseGroupUpdate revertPurseBalance(long purseId, //
			List<LedgerEntry> ledgerEntries, //
			Map<LedgerEntryType, BigDecimal> transactionFees, //
			PurseBalanceType purseBalanceType);

	public AccountPurseGroupUpdate debitLockedPurseBalance(List<RedemptionLockEntity> redemptionLocks,
			Map<LedgerEntryType, BigDecimal> transactionFees, long purseId, BigDecimal unlockTransactionAmount,
			BigDecimal lockTransactionAmount);

	public void updateLimitsAndFee(long purseId, final Map<String, Object> fees, final Map<String, Object> limits);

	public void setMaxPurseBalance(long purseId, BigDecimal balance);

	public AccountPurseGroupUpdate updatePurseStatus(long purseId, AccountPurseGroupStatus groupStatus);

	public BigDecimal getDefaultPurseAvailableBalance();

}
