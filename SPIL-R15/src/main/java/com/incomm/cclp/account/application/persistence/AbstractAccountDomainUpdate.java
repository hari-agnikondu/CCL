package com.incomm.cclp.account.application.persistence;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.incomm.cclp.account.domain.model.TransactionLogUpdate;
import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.TransactionLog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractAccountDomainUpdate {

	private String rrn;

	private Long authId;

	private BigDecimal transactionAmount;
	private BigDecimal authorizedAmount;

	private LocalDateTime transactionDate;

	@Singular // Singular annotations initializes the collections to empty collection rather than nulls.
	private List<StatementsLog> statementLogs;

	@Singular
	private List<TransactionLog> transactionLogs;

	@Builder.Default
	public final Optional<TransactionLogUpdate> transactionLogUpdate = Optional.empty();

}
