package com.incomm.cclp.service;

import java.util.List;

import javax.validation.constraints.Max;

import com.incomm.cclp.dto.CurrencyConversionReq;
import com.incomm.cclp.dto.CurrencyConversionResp;

public interface SpilCurrencyConversionService {

	public String getCurrencyRate(String mdmId, String txnCurrency, String issuingCurrency);

	public CurrencyConversionResp getCurrConversions(String mdmId);

	public CurrencyConversionResp addCurrencyRates(List<CurrencyConversionReq> currConvReq, String user, String channel, String mdmId);

	public CurrencyConversionResp delCurrencyRates(List<CurrencyConversionReq> currConvReqList, String user, String channel,
			@Max(20) String mdmId);
}
