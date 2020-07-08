package com.incomm.cclp.dao;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.CurrencyConversionRespDetails;

public interface CurrencyConversionDAO {
	public String getConversionRate(String mdmId, String txnCurrency, String issuingCurrency);

	public List<CurrencyConversionRespDetails> getAllCurrencyConversions(String mdmId);

	public Map<String, Object> addCurrencyRates(String mdmId, String channel, String user, CurrencyConversionRespDetails currConvRespDtl)
			throws Exception;

	public Map<String, Object> delCurrencyRates(String mdmId, String channel, String user, CurrencyConversionRespDetails currConvRespDtl)
			throws Exception;

	public boolean isValidMdmId(String mdmId);

}
