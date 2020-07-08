package com.incomm.cclp.account.domain.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CardUpdate {

	private final String cardNumberHash;

	private final CardStatusType oldCardStatus;
	private final CardStatusType newCardStatus;

	private final LocalDateTime lastTransactionDate;

	// below fields are for activation.
	private final Boolean oldFirstTimeTopUpFlag;
	private final Boolean newFirstTimeTopUpFlag;

	private final LocalDateTime activationDate;

}
