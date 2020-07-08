package com.incomm.cclp.domain;

import java.math.BigInteger;

public class TransactionLogInfo {
	double authAmount;
	double tranfeeAmount;
	String tranReverseFlag;
	String processFlag;
	double transactionAmount;
	String transactionDate;
	String transactionTime;
	String terminalId;
	String originalRrn;
	BigInteger accountPurseId;
	double openingLedgerBalance;
	double openingAvailableBalance;
	double closingLedgerBalance;
	double closingAvailableBalance;

	public TransactionLogInfo() {

	}

	public TransactionLogInfo(double authAmount, double tranfeeAmount, String tranReverseFlag, String processFlag, double transactionAmount,
			String transactionDate, String transactionTime, String terminalId, String originalRrn, BigInteger accountPurseId,
			double openingLedgerBalance, double openingAvailableBalance, double closingLedgerBalance, double closingAvailableBalance) {
		super();
		this.authAmount = authAmount;
		this.tranfeeAmount = tranfeeAmount;
		this.tranReverseFlag = tranReverseFlag;
		this.processFlag = processFlag;
		this.transactionAmount = transactionAmount;
		this.transactionDate = transactionDate;
		this.transactionTime = transactionTime;
		this.terminalId = terminalId;
		this.originalRrn = originalRrn;
		this.accountPurseId = accountPurseId;
		this.openingLedgerBalance = openingLedgerBalance;
		this.openingAvailableBalance = openingAvailableBalance;
		this.closingLedgerBalance = closingLedgerBalance;
		this.closingAvailableBalance = closingAvailableBalance;

	}

	public String getProcessFlag() {
		return processFlag;
	}

	public void setProcessFlag(String processFlag) {
		this.processFlag = processFlag;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public double getAuthAmount() {
		return authAmount;
	}

	public void setAuthAmount(double authAmount) {
		this.authAmount = authAmount;
	}

	public double getTranfeeAmount() {
		return tranfeeAmount;
	}

	public void setTranfeeAmount(double tranfeeAmount) {
		this.tranfeeAmount = tranfeeAmount;
	}

	public String getTranReverseFlag() {
		return tranReverseFlag;
	}

	public void setTranReverseFlag(String tranReverseFlag) {
		this.tranReverseFlag = tranReverseFlag;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getOriginalRrn() {
		return originalRrn;
	}

	public void setOriginalRrn(String originalRrn) {
		this.originalRrn = originalRrn;
	}

	public BigInteger getAccountPurseId() {
		return accountPurseId;
	}

	public void setAccountPurseId(BigInteger accountPurseId) {
		this.accountPurseId = accountPurseId;
	}

	public double getClosingLedgerBalance() {
		return closingLedgerBalance;
	}

	public void setClosingLedgerBalance(double closingLedgerBalance) {
		this.closingLedgerBalance = closingLedgerBalance;
	}

	public double getClosingAvailableBalance() {
		return closingAvailableBalance;
	}

	public void setClosingAvailableBalance(double closingAvailableBalance) {
		this.closingAvailableBalance = closingAvailableBalance;
	}

	public double getOpeningLedgerBalance() {
		return openingLedgerBalance;
	}

	public void setOpeningLedgerBalance(double openingLedgerBalance) {
		this.openingLedgerBalance = openingLedgerBalance;
	}

	public double getOpeningAvailableBalance() {
		return openingAvailableBalance;
	}

	public void setOpeningAvailableBalance(double openingAvailableBalance) {
		this.openingAvailableBalance = openingAvailableBalance;
	}
}
