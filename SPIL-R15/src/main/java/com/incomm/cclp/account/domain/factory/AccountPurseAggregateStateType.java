package com.incomm.cclp.account.domain.factory;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class AccountPurseAggregateStateType {

	private final AccountPurseUsageStateType purseUsageStateType;
	private final AccountPurseStateType purseStateType;

	public static AccountPurseAggregateStateType from(AccountPurseUsageStateType purseUsageStateType,
			AccountPurseStateType purseStateType) {
		return new AccountPurseAggregateStateType(purseUsageStateType, purseStateType);
	}

	public static AccountPurseAggregateStateType from(AccountPurseStateType purseStateType) {
		return new AccountPurseAggregateStateType(AccountPurseUsageStateType.SELECTED, purseStateType);
	}

}