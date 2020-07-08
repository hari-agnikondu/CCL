package com.incomm.scheduler.model;

import java.io.Serializable;
import java.util.Date;
import java.util.StringJoiner;

public class CurrencyRateResponseFile implements Serializable {

	private static final long serialVersionUID = 1L;
	private String mdmId;
	private String transactionCurrency;
	private String purseCurrency;
	private String exchangeRate;
	private String effectiveDateTime;
	private String action;
	private String ActionDateTime;
	private String status;
	private String errorMessage;
	private long insUser;
	private Date insDate;
	private String recordNum;
	private String batchId;

	public CurrencyRateResponseFile() {

	}

	public String getRecordNum() {
		return recordNum;
	}

	public void setRecordNum(String currencyConvId) {
		this.recordNum = currencyConvId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String currencyRateBatchId) {
		this.batchId = currencyRateBatchId;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getPurseCurrency() {
		return purseCurrency;
	}

	public void setPurseCurrency(String purseCurrency) {
		this.purseCurrency = purseCurrency;
	}

	public String getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(String exchangeRate) {
		this.exchangeRate = exchangeRate;
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

	public String getActionDateTime() {
		return ActionDateTime;
	}

	public void setActionDateTime(String actionDateTime) {
		ActionDateTime = actionDateTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = 1;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}
	
	public String getFileRecode() {

		StringJoiner js = new StringJoiner(",");
		js.add(getMdmId());
		js.add(getPurseCurrency());
		js.add(getExchangeRate());
		js.add(getEffectiveDateTime());
		js.add(getAction());
		js.add(getActionDateTime());
		js.add(getStatus());
		js.add(getErrorMessage());
		
		return js.toString();
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

}
