package com.incomm.cclp.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)

public class CurrencyConversionRespDetails {

	private String transactionCurrency;
	private String issuingCurrency;
	private double exchangeRate;
	private String effectiveDateTime;
	private String responseCode;
	private String responseMessage;
	private long recordNumber;

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

	public double getExchangeRate() {
		return exchangeRate;
	}

	@JsonInclude(Include.NON_DEFAULT)
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getEffectiveDateTime() {
		return effectiveDateTime;
	}

	public void setEffectiveDateTime(String effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
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

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public long getRecordNumber() {
		return recordNumber;
	}

	@JsonInclude(Include.NON_DEFAULT)
	public void setRecordNumber(long recordNumber) {
		this.recordNumber = recordNumber;
	}

	@Override
	public String toString() {
		return "CurrencyConversionRespDetails [transactionCurrency=" + transactionCurrency + ", issuingCurrency=" + issuingCurrency
				+ ", exchangeRate=" + exchangeRate + ", effectiveDateTime=" + effectiveDateTime + ", responseCode=" + responseCode
				+ ", responseMessage=" + responseMessage + ", recordNumber=" + recordNumber + "]";
	}

}
