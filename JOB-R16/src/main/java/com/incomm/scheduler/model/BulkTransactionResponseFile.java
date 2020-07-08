package com.incomm.scheduler.model;

import java.io.Serializable;
import java.util.StringJoiner;

public class BulkTransactionResponseFile implements Serializable {
	 

	 private static final long serialVersionUID = 1L;
	String batchId;
	String sourceReferenceNumber;
	String spNumber;
	String cardStatus;
	String amount;
	String transactionDate;
	String transactionTime;
	String availableBalance;
	String mdmId;
	String storeId;
	String terminalId;
	String responseCode;
	String responseMessage;
	String transactionDesc;
	String recordNum;
	
	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}
	
	public String getTransactionDesc() {
		return transactionDesc;
	}

	public void setTransactionDesc(String transactionDesc) {
		this.transactionDesc = transactionDesc;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	
	public String getSourceReferenceNumber() {
		return sourceReferenceNumber;
	}

	public void setSourceReferenceNumber(String source_Reference_Number) {
		sourceReferenceNumber = source_Reference_Number;
	}

	public String getSpNumber() {
		return spNumber;
	}

	public void setSpNumber(String spNumber) {
		this.spNumber = spNumber;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	public String getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMsg) {
		this.responseMessage = responseMsg;
	}

	
	public BulkTransactionResponseFile() {

	}

	public String getFileRecode() {
		
		StringJoiner js = new StringJoiner(",");
		js.add(getBatchId());
		js.add(getSourceReferenceNumber());
		js.add(getSpNumber());
		js.add(getCardStatus());
		js.add(getAmount());
		js.add(getTransactionDate());
		js.add(getTransactionTime());
		js.add(getAvailableBalance());
		js.add(getMdmId());
		js.add(getStoreId());
		js.add(getTerminalId());
		js.add(getResponseCode());
		js.add(getResponseMessage());
		js.add(getTransactionDesc());
		js.add(getRecordNum());
		return js.toString();
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

}
