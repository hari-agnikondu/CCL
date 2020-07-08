package com.incomm.cclp.account.domain.model;

import lombok.Getter;

@Getter
public enum PurseBalanceType {
	LEDGER_BALANCE("Ledger Balance only"),
	AVAILABLE_BALANCE("Available Balance only"),
	BOTH("BOTH Available Balance and Ledger Balance");

	private final String value;

	private PurseBalanceType(String value) {
		this.value = value;
	}
}
