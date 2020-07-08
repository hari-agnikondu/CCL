package com.incomm.cclp.account.domain.model;

import java.util.Optional;

public enum SpNumberType {

	CARD_NUMBER,
	CUSTOMER_ID,
	ACCOUNT_NUMBER,
	SERIAL_NUMBER,
	PROXY_NUMBER;

	public static Optional<SpNumberType> byName(String name) {
		for (SpNumberType type : SpNumberType.values()) {
			if (type.name()
				.equals(name)) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}

}
