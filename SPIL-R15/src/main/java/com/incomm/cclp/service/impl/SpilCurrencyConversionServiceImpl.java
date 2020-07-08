package com.incomm.cclp.service.impl;

import static com.incomm.cclp.constants.CurrConvConstants.FAILURE_RESPONSE_CODE;
import static com.incomm.cclp.constants.CurrConvConstants.INVALID_MDM_ID;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.incomm.cclp.dao.CurrencyConversionDAO;
import com.incomm.cclp.dto.CurrencyConversionReq;
import com.incomm.cclp.dto.CurrencyConversionResp;
import com.incomm.cclp.dto.CurrencyConversionRespDetails;
import com.incomm.cclp.service.SpilCurrencyConversionService;
import com.incomm.cclp.validation.CurrencyConversionReqValidation;

@Service
public class SpilCurrencyConversionServiceImpl implements SpilCurrencyConversionService {

	@Autowired
	CurrencyConversionDAO currencyConversionDAO;

	@Autowired
	CurrencyConversionReqValidation currencyConvReqValidation;

	private static final Logger logger = LogManager.getLogger(SpilCurrencyConversionServiceImpl.class);

	@Override
	public String getCurrencyRate(String mdmId, String txnCurrency, String issuingCurrency) {
		String rateData = currencyConversionDAO.getConversionRate(mdmId, txnCurrency, issuingCurrency);
		return rateData;
	}

	@Override
	public CurrencyConversionResp getCurrConversions(String mdmId) {
		CurrencyConversionResp rateData = new CurrencyConversionResp();
		if (!currencyConversionDAO.isValidMdmId(mdmId)) {
			rateData.setResponseCode(FAILURE_RESPONSE_CODE);
			rateData.setResponseMessage(INVALID_MDM_ID);
		} else {
			rateData.setCurrencyConversionRespDetails(currencyConversionDAO.getAllCurrencyConversions(mdmId));
		}
		return rateData;
	}

	@Override
	public CurrencyConversionResp addCurrencyRates(List<CurrencyConversionReq> currConvReqList, String user, String channel, String mdmId) {
		logger.info("addCurrencyRates() ENTER>>>");
		CurrencyConversionResp currencyConvResp = null;
		List<CurrencyConversionRespDetails> currConvRespDtlsList = null;
		// Validate Headers and mdmId
		currencyConvResp = currencyConvReqValidation.validateHeadersMdmId(currConvReqList, user, channel, mdmId);
		currConvRespDtlsList = currencyConvReqValidation.copyRequestToResponseObj(currConvReqList);
		// If Headers/MdmId validation passed, validate request fields
		if (currencyConvResp != null && StringUtils.isEmpty(currencyConvResp.getResponseCode())) {
			logger.debug("Headers and MdmId validations passed");
			currencyConvResp = currencyConvReqValidation.processAddCurrConvs(currConvRespDtlsList, mdmId, user, channel);
		}
		logger.info("addCurrencyRates() EXIT");
		return currencyConvResp;
	}

	@Override
	public CurrencyConversionResp delCurrencyRates(List<CurrencyConversionReq> currConvReqList, String user, String channel, String mdmId) {
		logger.info("delCurrencyRates() ENTER>>>");
		// Validate Headers and mdmId
		CurrencyConversionResp currencyConvResp = null;
		List<CurrencyConversionRespDetails> currConvRespDtlsList = null;
		currencyConvResp = currencyConvReqValidation.validateHeadersMdmId(currConvReqList, user, channel, mdmId);
		currConvRespDtlsList = currencyConvReqValidation.copyRequestToResponseObj(currConvReqList);
		// If Headers/MdmId validation passed, validate request fields
		if (currencyConvResp != null && StringUtils.isEmpty(currencyConvResp.getResponseCode())) {
			logger.debug("Header and MdmId validations passed");
			currencyConvResp = currencyConvReqValidation.processDelCurrConvs(currConvRespDtlsList, mdmId, user, channel);
		}
		logger.info("delCurrencyRates() EXIT<<<");
		return currencyConvResp;

	}

}
