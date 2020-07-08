package com.incomm.cclp.account.domain.event;

import java.time.LocalDateTime;
import java.util.List;

import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.TransactionLog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@SuperBuilder
public abstract class AbstractAccountDomainEvent implements DomainEvent {

	private String rrn;

	private long accountId;

	private LocalDateTime transactionDate;

	@Singular // Singular annotations initializes the collections to empty collection rather than nulls.
	private List<StatementsLog> statementLogs;

	@Singular
	private List<TransactionLog> transactionLogs;

}
