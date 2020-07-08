package com.incomm.cclp.account.domain.model;

import java.util.Optional;

import lombok.Getter;

public enum PurseStatusType {

	ACTIVE("1"),
	INACTIVE("0");

	@Getter
	private String statusCode;

	private PurseStatusType(String statusCode) {
		this.statusCode = statusCode;
	}

	public static Optional<PurseStatusType> byStatusCode(String statusCode) {

		for (PurseStatusType type : PurseStatusType.values()) {
			if (type.statusCode.equals(statusCode)) {
				return Optional.of(type);
			}
		}

		return Optional.empty();
	}

	public static Optional<PurseStatusType> byName(String status) {

		for (PurseStatusType type : PurseStatusType.values()) {
			if (type.name()
				.equals(status)) {
				return Optional.of(type);
			}
		}

		return Optional.empty();
	}

}
