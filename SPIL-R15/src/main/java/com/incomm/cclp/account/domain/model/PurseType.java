package com.incomm.cclp.account.domain.model;

import java.util.Optional;

import lombok.Getter;

@Getter
public enum PurseType {

	CONSUMER_FUNDED_CURRENCY(1, "CONSUMER FUNDED CURRENCY"),
	POINTS(2, "POINTS"),
	SKU(3, "SKU"),
	PARTNER_FUNDED_CURRENCY(4, "PARTNER FUNDED CURRENCY");

	private final int purseTypeId;
	private final String purseTypeString;

	private PurseType(int purseTypeId, String purseType) {
		this.purseTypeId = purseTypeId;
		this.purseTypeString = purseType;
	}

	public static Optional<PurseType> byPurseTypeId(Integer purseTypeId) {

		if (purseTypeId == null) {
			return Optional.empty();
		}

		for (PurseType type : PurseType.values()) {
			if (type.purseTypeId == purseTypeId) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}

	public static Optional<PurseType> byPurseTypeString(String purseType) {

		if (purseType == null) {
			return Optional.empty();
		}

		for (PurseType type : PurseType.values()) {
			if (type.purseTypeString.equals(purseType)) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}

}
