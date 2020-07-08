package com.incomm.cclp.domain;

import java.math.BigInteger;

public class StatementsLogInfo {
	private String txnDesc;
	private double txnAmount;
	private BigInteger accountPurseId;
	private long purseId;

	public StatementsLogInfo(String txnDesc, double txnAmount, BigInteger accountPurseId, long purseId) {
		super();
		this.txnDesc = txnDesc;
		this.txnAmount = txnAmount;
		this.accountPurseId = accountPurseId;
		this.purseId = purseId;
	}

	public String getTxnDesc() {
		return txnDesc;
	}

	public void setTxnDesc(String txnDesc) {
		this.txnDesc = txnDesc;
	}

	public double getTxnAmount() {
		return txnAmount;
	}

	public void setTxnAmount(double txnAmount) {
		this.txnAmount = txnAmount;
	}

	public BigInteger getAccountPurseId() {
		return accountPurseId;
	}

	public void setAccountPurseId(BigInteger accountPurseId) {
		this.accountPurseId = accountPurseId;
	}

	public long getPurseId() {
		return purseId;
	}

	public void setPurseId(long purseId) {
		this.purseId = purseId;
	}

}
