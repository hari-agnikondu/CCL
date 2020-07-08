package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RedemptionLockUpdate {
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
	private LocalDateTime unlockDate;
	private LocalDateTime lockExpiryDate;

	private String cardNumberHash;
	private String cardNumberEncrypted;
	private long accountId;

	private DeliveryChannelType deliveryChannelType;
	private String storeId;
	private String terminalId;

	private LocalDateTime insDate;

	private boolean isNew;

}
