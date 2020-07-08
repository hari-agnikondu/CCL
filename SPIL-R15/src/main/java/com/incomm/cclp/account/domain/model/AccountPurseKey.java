package com.incomm.cclp.account.domain.model;

import lombok.Value;

@Value
public class AccountPurseKey {

	private long accountId;
	private long purseId;
	private long accountPurseId;

	private AccountPurseKey(long accountId, long purseId, long accountPurseId) {
		super();
		this.accountId = accountId;
		this.purseId = purseId;
		this.accountPurseId = accountPurseId;
	}

	public static AccountPurseKey from(long accountId, long purseId, long accountPurseId) {
		return new AccountPurseKey(accountId, purseId, accountPurseId);
	}

}
