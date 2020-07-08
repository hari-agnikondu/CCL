package com.incomm.cclp.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatementLog {
	private String transactionDescription;
	private BigDecimal transactionAmount;
	private String operationType;
	private long accountPurseId;
	private long purseId;
	private long toPurseId;

	public StatementLog() {
		super();
	}

	public StatementLog(String transactionDescription, BigDecimal transactionAmount, String operationType, long accountPurseId,
			long purseId, long toPurseId) {
		super();
		this.transactionDescription = transactionDescription;
		this.transactionAmount = transactionAmount;
		this.operationType = operationType;
		this.accountPurseId = accountPurseId;
		this.purseId = purseId;
		this.toPurseId = toPurseId;
	}

}
