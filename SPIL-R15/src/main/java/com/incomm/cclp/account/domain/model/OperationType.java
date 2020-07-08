package com.incomm.cclp.account.domain.model;

import lombok.Getter;

public enum OperationType {
	CREDIT("C"),
	DEBIT("D"),
	NONFINANCIAL("NA");

	@Getter
	private String flag;

	private OperationType(String flag) {
		this.flag = flag;
	}

	public static OperationType byFlag(String flag) {
		if (CREDIT.getFlag()
			.equals(flag)) {
			return CREDIT;
		}
		if (DEBIT.getFlag()
			.equals(flag)) {
			return DEBIT;
		}
		return OperationType.NONFINANCIAL;
	}

	public OperationType getReversalOperationType() {
		if (this == CREDIT) {
			return DEBIT;
		}
		if (this == DEBIT) {
			return CREDIT;
		}
		return NONFINANCIAL;
	}

}
