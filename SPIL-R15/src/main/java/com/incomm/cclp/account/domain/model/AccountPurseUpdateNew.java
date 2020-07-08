package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountPurseUpdateNew {

	private AccountPurseKey accountPurseKey;

	private final AccountPurseAltKeyAttributes attributes;

	private long productId;

	private BigDecimal previousLedgerBalance;
	private BigDecimal previousAvailableBalance;

	private BigDecimal newLedgerBalance;
	private BigDecimal newAvailableBalance;

	// private BigDecimal transactionAmount;
	// private BigDecimal authorizedAmount;

	private final boolean isNew;
	private String topUpStatus;

	private final LocalDateTime previousLastTransactionDate;

}
