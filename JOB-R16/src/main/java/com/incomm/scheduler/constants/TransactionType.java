package com.incomm.scheduler.constants;

import lombok.Getter;

@Getter
public enum TransactionType {
	
	AUTO_TOPUP_PURSE("109", "Auto Top-up Purse", "autoTopUpAccountPurse"),
	AUTO_ROLLOVER_PURSE("110", "Auto Rollover Purse", "autoRolloverAccountPurse"),
	AUTORELOAD_ACCOUNT_PURSE("108", "AutoReload Account Purse", "autoReloadAccountPurse");
	
	private String transactionCode;
	private String transactionDesc;
	private String transactionShortName;
	
	private TransactionType(String transactionCode, String transactionDesc, String transactionShortName) {
		this.transactionCode = transactionCode;
		this.transactionDesc = transactionDesc;
		this.transactionShortName = transactionShortName;
	}

}
