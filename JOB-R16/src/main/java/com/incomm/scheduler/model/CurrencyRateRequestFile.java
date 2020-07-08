package com.incomm.scheduler.model;

public class CurrencyRateRequestFile {

	String mdmId;
	String transactionCurrency;
	String issuingCurrency;
	double conversionRate;
	String effectiveDateTime;
	String action;
	String fileName;
	String batchId;
	String source;
	String recordNum;
	
	public CurrencyRateRequestFile() {

	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String currencyRateBatchId) {
		this.batchId = currencyRateBatchId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String currencyConvId) {
		this.recordNum = currencyConvId;
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getIssuingCurrency() {
		return issuingCurrency;
	}

	public void setIssuingCurrency(String issuingCurrency) {
		this.issuingCurrency = issuingCurrency;
	}

	public double getConversionRate() {
		return conversionRate;
	}

	public void setConversionRate(double conversionRate) {
		this.conversionRate = conversionRate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getEffectiveDateTime() {
		return effectiveDateTime;
	}

	public void setEffectiveDateTime(String effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String toString() {

		StringBuilder builder = new StringBuilder();
		builder.append("Currency Rate [mdmId=");
		builder.append(mdmId);
		builder.append(", Transaction Currency=");
		builder.append(transactionCurrency);
		builder.append(", Issuing Currency=");
		builder.append(issuingCurrency);
		builder.append(", Conversion Rate=");
		builder.append(conversionRate);
		builder.append(", Effective Date and Time=");
		builder.append(effectiveDateTime);
		builder.append(", Actione=");
		builder.append(action);
		builder.append(", CurrencyRateBatchId=");
		builder.append(batchId);
		builder.append(", fileName=");
		builder.append(fileName);
		builder.append("]");
		return builder.toString();
	}

}
