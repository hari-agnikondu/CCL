package com.incomm.cclp.account.domain.factory;

import lombok.Getter;

@Getter
public enum AccountPurseStateType {

	NONE(true, false, false, false), // for purseStatusUpdate API
	SELECTED_ALL(false, true, false, false), // for purseUpdate API, expired needed for rollover.
	ALL_UNEXPIRED(false, false, true, true); // for all SPIL APIs

	private final boolean skipAccountPurseLoad;
	private final boolean selectedPurseIdOnly;

	private final boolean unexpiredOnly;
	private final boolean activePurseGroupOnly;

	private AccountPurseStateType(boolean skipAccountPurseLoad, boolean selectedPurseIdOnly, boolean unexpiredOnly,
			boolean activePurseGroupOnly) {
		this.skipAccountPurseLoad = skipAccountPurseLoad;
		this.selectedPurseIdOnly = selectedPurseIdOnly;

		this.unexpiredOnly = unexpiredOnly;
		this.activePurseGroupOnly = activePurseGroupOnly;
	}

}