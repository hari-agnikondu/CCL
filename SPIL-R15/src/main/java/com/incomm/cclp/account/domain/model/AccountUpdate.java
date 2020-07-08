package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountUpdate {
	private final long accountId;

	private final BigDecimal previousInitialLoadAmount;
	private final BigDecimal previousNewInitialLoadAmount;

	private final BigDecimal initialLoadAmount;
	private final BigDecimal newInitialLoadAmount;

	private final LocalDateTime lastUpdateDate;
}
