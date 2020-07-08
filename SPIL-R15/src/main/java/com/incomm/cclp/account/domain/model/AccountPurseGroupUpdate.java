package com.incomm.cclp.account.domain.model;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AccountPurseGroupUpdate {

	private final AccountPurseGroupKey accountPurseGroupKey;
	private final long productId;
	private final PurseType purseType;
	private final String currencyCode;

	List<AccountPurseUpdateNew> accountPurseUpdates;

	List<LedgerEntry> ledgerEntries;

	private final boolean isNew;
	private final boolean isDefault;

	private Map<String, Object> previousUsageLimit;
	private Map<String, Object> previousUsageFee;

	private Map<String, Object> newUsageLimit;
	private Map<String, Object> newUsageFee;

	private AccountPurseGroupStatus previousStatus;
	private AccountPurseGroupStatus newStatus;

}
