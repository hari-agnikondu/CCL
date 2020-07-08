package com.incomm.scheduler.constants;

import java.util.Optional;

import lombok.Getter;

@Getter
public enum PurseUpdateActionType {
	LOAD("load"),
	UNLOAD("unload"),
	TOP_UP("topUp"),
	AUTOTOPUP("autoTopup"),
	AUTORELOAD("autoReload"),
	AUTOROLLOVER("autoRollover");

	private final String action;

	private PurseUpdateActionType(String action) {
		this.action = action;
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
