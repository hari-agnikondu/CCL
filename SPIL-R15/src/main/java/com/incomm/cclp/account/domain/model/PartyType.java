package com.incomm.cclp.account.domain.model;

import lombok.Getter;

public enum PartyType {

	FIRST("FIRST PARTY"),
	BOTH("BOTH");

	@Getter
	private final String value;

	private PartyType(String value) {
		this.value = value;
	}

}
