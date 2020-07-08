package com.incomm.cclp.account.domain.event;

import com.incomm.cclp.domain.TransactionLog;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class TransactionFailedEvent extends AbstractAccountDomainEvent {

	public static TransactionFailedEvent from(TransactionLog transactionLog) {
		return TransactionFailedEvent.builder()
			.transactionLog(transactionLog)
			.build();
	}

}
