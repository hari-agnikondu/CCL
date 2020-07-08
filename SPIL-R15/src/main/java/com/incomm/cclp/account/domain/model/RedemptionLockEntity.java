package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RedemptionLockEntity {
	private RedemptionLockKey redemptionLockKey;
	private String transactionDate;
	private String transactionTime;

	private String correlationId;
	private String unlockCorrelationId;

	private BigDecimal transactionAmount;
	private BigDecimal authorizedAmount;
	private BigDecimal transactionFee;
	private BigDecimal previousLedgerBalance;
	private BigDecimal closingLedgerBalance;
	private BigDecimal previousAvailableBalance;
	private BigDecimal closingAvailableBalance;

	private String lockFlag;
	private String lockFound;
	private String cardNumberHash;

}
