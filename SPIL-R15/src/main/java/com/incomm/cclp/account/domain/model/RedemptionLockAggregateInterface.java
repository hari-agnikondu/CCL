package com.incomm.cclp.account.domain.model;

import java.util.List;

public interface RedemptionLockAggregateInterface {
	public RedemptionLockUpdate getRedemptionLockUpdate(List<LedgerEntry> ledgerEntryList, long accountPurseId, long transactionSeqId,
			long purseId);

	public List<RedemptionLockUpdate> revertRedemptionLockFlag(String rrn, long purseId);

}
