package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PurseUpdateTransactionInfo {

	private SpNumberType spNumberType;
	private String spNumber;
	private String purseName;
	private PurseUpdateActionType actionType;
	private BigDecimal transactionAmount;

	private ZonedDateTime effectiveDate;
	private ZonedDateTime expiryDate;
	private String skuCode;
	private String currency;

	private LocalDateTime transactionDateTime;

	private Long inputAccountPurseId;
	private Long accountPurseId;
	private BigDecimal authorizedAmount;
	private BigDecimal availablePurseBalance;
	private CardStatusType cardStatus;

}
