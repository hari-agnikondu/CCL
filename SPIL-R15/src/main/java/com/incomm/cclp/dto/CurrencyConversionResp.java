package com.incomm.cclp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_NULL)
public class CurrencyConversionResp {

	private String responseCode;
	private String responseMessage;

	private List<CurrencyConversionRespDetails> currencyConversion;

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

	@JsonProperty(value = "currencyConversion")
	public List<CurrencyConversionRespDetails> getCurrencyConversionRespDetails() {
		return currencyConversion;
	}

	public void setCurrencyConversionRespDetails(List<CurrencyConversionRespDetails> currencyConversionRespDetails) {
		this.currencyConversion = currencyConversionRespDetails;
	}

	@Override
	public String toString() {
		return "CurrencyConversionResp [responseCode=" + responseCode + ", responseMessage=" + responseMessage
				+ ", currencyConversionRespDetails=" + currencyConversion + "]";
	}
}
