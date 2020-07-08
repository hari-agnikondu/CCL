package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LedgerEntry {

	private LedgerEntryType ledgerEntryType;
	private OperationType operationType;

	private final AccountPurseKey accountPurseKey;

	private BigDecimal previousLedgerBalance;
	private BigDecimal previousAvailableBalance;

	private BigDecimal newLedgerBalance;
	private BigDecimal newAvailableBalance;

	private BigDecimal authorizedAmount;
	private BigDecimal transactionAmount;

}
