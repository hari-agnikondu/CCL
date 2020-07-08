package com.incomm.cclp.account.domain.model;

import lombok.Value;

@Value
public class RedemptionLockKey {
	private long purseId;
	private long accountPurseId;
	private long transactionSeqId;

	private RedemptionLockKey(long purseId, long accountPurseId, long transactionSeqId) {
		super();
		this.purseId = purseId;
		this.accountPurseId = accountPurseId;
		this.transactionSeqId = transactionSeqId;
	}

	public static RedemptionLockKey from(long purseId, long accountPurseId, long transactionSeqId) {
		return new RedemptionLockKey(purseId, accountPurseId, transactionSeqId);
	}
}
