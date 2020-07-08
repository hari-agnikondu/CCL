package com.incomm.cclp.account.domain.event;

import com.incomm.cclp.account.domain.model.PurseStatusUpdateTransactionInfo;
import com.incomm.cclp.account.domain.model.PurseUpdate;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class PurseStatusUpdatedEvent extends AbstractAccountDomainEvent {

	private final PurseUpdate purseUpdate;
	private final PurseStatusUpdateTransactionInfo transactionInfo;

}
