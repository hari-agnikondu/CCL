package com.incomm.cclp.account.domain.model;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountPurseOperationPartial {

	private Optional<Long> accountPurseId;
	private Optional<AccountPurseAltKeyAttributes> attributes;
	private BigDecimal amount;
	private LedgerEntryType ledgerEntryType;

	public static AccountPurseOperationPartial from(Long accountPurseId, AccountPurseAltKeyAttributes attributes, BigDecimal amount,
			LedgerEntryType ledgerEntryType) {
		return new AccountPurseOperationPartial(Optional.ofNullable(accountPurseId), Optional.ofNullable(attributes), amount,
				ledgerEntryType);
	}

	public boolean hasAccountPurseId() {
		return this.accountPurseId.isPresent();
	}

}
