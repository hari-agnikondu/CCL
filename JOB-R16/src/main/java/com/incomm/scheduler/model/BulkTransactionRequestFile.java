package com.incomm.scheduler.model;

public class BulkTransactionRequestFile{
	String sourceReferenceNumber;
	String spNumber;
	String action;
	String amount;
	String mdmId;
	String storeId;
	String terminalId;
	String batchId;
	String fileName;
	String recordNum;

	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String recordNum) {
		this.recordNum = recordNum;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
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

	
	public BulkTransactionRequestFile() {

	}

	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		builder.append("Bulk Transaction Process [RRN=");
		builder.append(sourceReferenceNumber);
		builder.append(", SpNumber=");
		builder.append(spNumber);
		builder.append(", Action=");
		builder.append(action);
		builder.append(", amount=");
		builder.append(amount);
		builder.append(", PartnerName=");
		builder.append(mdmId);
		builder.append(", StoreId=");
		builder.append(storeId);
		builder.append(", TerminalId=");
		builder.append(terminalId);
		builder.append(", BatchID=");
		builder.append(batchId);
		builder.append(", FileName=");
		builder.append(fileName);
		builder.append(", RecordNumber=");
		builder.append(recordNum);
			builder.append("]");
		return builder.toString();
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

}
