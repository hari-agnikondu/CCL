package com.incomm.cclp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "currencyConversion")
@JsonInclude(Include.NON_NULL)
public class CurrencyConversionReq {

	private String transactionCurrency;

	private String issuingCurrency;

	private double exchangeRate;

	private String effectiveDateTime;

	private long recordNumber;

	public void setRecordNumber(long recordNumber) {
		this.recordNumber = recordNumber;
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

	public double getExchangeRate() {
		return exchangeRate;
	}

	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	public String getEffectiveDateTime() {
		return effectiveDateTime;
	}

	public void setEffectiveDateTime(String effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
	}

	public long getRecordNumber() {
		return recordNumber;
	}

	@Override
	public String toString() {
		return "CurrencyConversionReq [transactionCurrency=" + transactionCurrency + ", issuingCurrency=" + issuingCurrency
				+ ", exchangeRate=" + exchangeRate + ", effectiveDateTime=" + effectiveDateTime + ", recordNumber=" + recordNumber + "]";
	}

}
