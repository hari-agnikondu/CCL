package com.incomm.cclp.account.domain.model;

import lombok.Getter;

@Getter
public enum LedgerEntryType {

	FLAT_FEE("Fee"),
	PERCENT_FEE("Percent Fee"),
	TRANSACTION_AMOUNT("Transaction Amount");

	private String description;

	private LedgerEntryType(String description) {
		this.description = description;
	}

	public static LedgerEntryType byLogDescripton(String logDescription) {
		if (logDescription.contains(PERCENT_FEE.getDescription())) {
			return PERCENT_FEE;
		}
		if (logDescription.contains(FLAT_FEE.getDescription())) {
			return FLAT_FEE;
		}
		return TRANSACTION_AMOUNT;
	}
}
