package com.incomm.scheduler.constants;

import java.util.Optional;

import lombok.Getter;

@Getter
public enum CardStatusType {
	
	INACTIVE("0", "INACTIVE"),
	ACTIVE("1", "ACTIVE"),
	ACTIVE_UNREGISTERED("13", "ACTIVE-UNREGISTERED"),
	BAD_CREDIT("18", "BADCREDIT"),
	CLOSED("9", "CLOSED"),
	CONSUMED("17", "CONSUMED"),
	DAMAGE("3", "DAMAGE"),
	DIGITAL_IN_PROCESS("97", "DIGITAL IN PROCESS"),
	EXPIRED_CARD("7", "EXPIRED CARD"),
	EXPIRED_PRODUCT("20", "Expired Product"),
	FRAUD_HOLD("15", "FRAUD HOLD"),
	HOT_CARDED("11", "HOT CARDED"),
	HOTLIST("19", "HOTLIST"),	
	INTERNATIONAL("21", "INTERNATIONAL"),
	LOST_STOLEN("2", "LOST-STOLEN"),
	MONITORED("5", "MONITORED"),
	ON_HOLD("6", "ON HOLD"),
	PASSIVE("8", "PASSIVE"),
	PRINTER_SENT("99", "PRINTER SENT"),
	RESTRICTED("4", "RESTRICTED"),
	SUSPENDED_CREDIT("12", "SUSPENDED CREDIT"),
	SUSPENDED_DEBIT("14", "SUSPENDED DEBIT");
	
	private String statusCode;
	private String statusDescription;

	private CardStatusType(String statusCode, String statusDescription) {
		this.statusCode = statusCode;
		this.statusDescription = statusDescription;
	}

	public static Optional<CardStatusType> byStatusCode(String statusCode) {
		for (CardStatusType type : CardStatusType.values()) {
			if (type.statusCode.equals(statusCode)) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}
}
