package com.incomm.cclp.account.application.persistence;

import java.util.List;

import com.incomm.cclp.account.domain.model.AccountAggregateUpdate;
import com.incomm.cclp.account.domain.model.RedemptionLockUpdate;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.SuperBuilder;

@Value
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class AccountTransactionUpdate extends AbstractAccountDomainUpdate {

	public final List<AccountAggregateUpdate> accountAggregateUpdates;

	private List<RedemptionLockUpdate> redemptionLocks;

}
