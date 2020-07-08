package com.incomm.cclp.transaction.bean;

public class Transactions {

	private String transactionCode;
	private String transactionDescription;
	private String transactionShortName;
	private String isFinancial;
	private String crDrIndicator;

	public Transactions() {

	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getTransactionShortName() {
		return transactionShortName;
	}

	public void setTransactionShortName(String transactionShortName) {
		this.transactionShortName = transactionShortName;
	}

	public String getIsFinancial() {
		return isFinancial;
	}

	public void setIsFinancial(String isFinancial) {
		this.isFinancial = isFinancial;
	}

	public String getCrDrIndicator() {
		return crDrIndicator;
	}

	public void setCrDrIndicator(String crDrIndicator) {
		this.crDrIndicator = crDrIndicator;
	}

	public Transactions(String transactionCode, String transactionDescription, String transactionShortName, String isFinancial,
			String crDrIndicator) {
		super();
		this.transactionCode = transactionCode;
		this.transactionDescription = transactionDescription;
		this.transactionShortName = transactionShortName;
		this.isFinancial = isFinancial;
		this.crDrIndicator = crDrIndicator;
	}

	public String getResponseCode() {
		return null;
	}

}
