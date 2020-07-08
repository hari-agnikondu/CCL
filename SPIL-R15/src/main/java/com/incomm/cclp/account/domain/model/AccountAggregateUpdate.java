package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountAggregateUpdate {

	@Builder.Default
	private BigDecimal authorizedAmount = BigDecimal.ZERO;
	@Builder.Default
	private BigDecimal transactionFee = BigDecimal.ZERO;

	private List<AccountPurseGroupUpdate> accountPurseGroupUpdate;

	@Builder.Default
	private Optional<CardUpdate> cardUpdate = Optional.empty();

	@Builder.Default
	private Optional<AccountUpdate> accountUpdate = Optional.empty();

}
