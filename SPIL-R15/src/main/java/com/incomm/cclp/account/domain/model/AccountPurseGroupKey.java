package com.incomm.cclp.account.domain.model;

import java.io.Serializable;

import lombok.Value;

@Value
public class AccountPurseGroupKey implements Serializable {

	private static final long serialVersionUID = 1552740421817248645L;

	private long accountId;
	private long purseId;

	private AccountPurseGroupKey(long accountId, long purseId) {
		super();
		this.accountId = accountId;
		this.purseId = purseId;
	}

	public static AccountPurseGroupKey from(long accountId, long purseId) {
		return new AccountPurseGroupKey(accountId, purseId);
	}

}
