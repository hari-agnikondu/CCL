package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;

public interface AccountPurseInterface {

	public LedgerEntry creditPurseBalance(BigDecimal transactionAmount, LedgerEntryType ledgerEntryType, PurseBalanceType purseBalanceType);

	public LedgerEntry debitPurseBalance(BigDecimal transactionAmount, LedgerEntryType ledgerEntryType, PurseBalanceType purseBalanceType);

	public LedgerEntry debitPurseBalanceAllowNegative(BigDecimal transactionAmount, LedgerEntryType ledgerEntryType,
			PurseBalanceType purseBalanceType);

	public LedgerEntry debitPurseBalanceForUnlock(BigDecimal transactionAmount, BigDecimal lockAvailableBalance,
			LedgerEntryType ledgerEntryType, PurseBalanceType purseBalanceType);

	public boolean hasBalanceForDebit(BigDecimal transactionAmount);

	public boolean isUnexpiredEffectivePurse();

	public BigDecimal getAvailableBalance();

	public BigDecimal getLedgerBalance();

}
