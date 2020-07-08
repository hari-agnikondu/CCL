package com.incomm.cclp.account.domain.model;

import java.util.Optional;

import lombok.Getter;

@Getter
public enum PurseUpdateActionType {
	// (action, transactionCode, transactionShortName)
	LOAD("load", "100", OperationType.CREDIT, "loadAccountPurse"),
	UNLOAD("unload", "101", OperationType.DEBIT, "unloadAccountPurse"),
	TOP_UP("topUp", "102", OperationType.CREDIT, "topUpAccountPurse"),
	AUTORELOAD("autoReload", "108", OperationType.CREDIT, "autoReloadAccountPurse"),
	AUTO_TOP_UP("autoTopup", "109", OperationType.CREDIT, "autoTopUpAccountPurse"),
	AUTO_ROLLOVER("autoRollover", "110", OperationType.DEBIT, "autoRolloverAccountPurse"),
	AUTO_ROLLOVER_DEBIT("autoRolloverDebit", "110", OperationType.DEBIT, "autoRolloverAccountPurse"),
	AUTO_ROLLOVER_CREDIT("autoRolloverCredit", "110", OperationType.CREDIT, "autoRolloverAccountPurse");

	private final String action;
	private final String transctionCode;
	private final String transactionShortName;
	private final OperationType operationType;

	private PurseUpdateActionType(String action, String transctionCode, OperationType operationType, String transactionShortName) {
		this.action = action;
		this.transctionCode = transctionCode;
		this.operationType = operationType;
		this.transactionShortName = transactionShortName;
	}

	public static Optional<PurseUpdateActionType> byAction(String value) {
		for (PurseUpdateActionType type : PurseUpdateActionType.values()) {
			if (type.action.equals(value)) {
				return Optional.of(type);
			}
		}
		return Optional.empty();
	}
}
