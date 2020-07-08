package com.incomm.cclp.account.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TransactionLogUpdate {

	private final String correlationId;
	private final String cardNumberHash;
	private final String isReversedFlag;
	private final TransactionType transactionType;

}
