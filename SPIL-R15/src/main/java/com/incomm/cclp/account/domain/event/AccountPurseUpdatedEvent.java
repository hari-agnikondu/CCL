package com.incomm.cclp.account.domain.event;

import java.util.List;

import com.incomm.cclp.account.domain.model.AccountPurseUpdate;
import com.incomm.cclp.account.domain.model.PurseUpdateTransactionInfo;

import lombok.EqualsAndHashCode;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AccountPurseUpdatedEvent extends AbstractAccountDomainEvent {

	@Singular
	private List<AccountPurseUpdate> accountPurseUpdates;

	private PurseUpdateTransactionInfo transactionInfo;

}
