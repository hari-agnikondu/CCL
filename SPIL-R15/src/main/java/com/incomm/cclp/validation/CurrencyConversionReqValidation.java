package com.incomm.cclp.validation;

import static com.incomm.cclp.constants.CurrConvConstants.CURR_CODE_EXPR;
import static com.incomm.cclp.constants.CurrConvConstants.CURR_EXPR;
import static com.incomm.cclp.constants.CurrConvConstants.DATETIME_FORMAT;
import static com.incomm.cclp.constants.CurrConvConstants.FAILURE_RESPONSE_CODE;
import static com.incomm.cclp.constants.CurrConvConstants.INVALID_EFF_DATE;
import static com.incomm.cclp.constants.CurrConvConstants.INVALID_MDM_ID;
import static com.incomm.cclp.constants.CurrConvConstants.MISSING_CHANNEL_RESPONSE_MSG;
import static com.incomm.cclp.constants.CurrConvConstants.MISSING_INVALID_EXCHG_RATE;
import static com.incomm.cclp.constants.CurrConvConstants.MISSING_INVALID_ISSUE_CURR;
import static com.incomm.cclp.constants.CurrConvConstants.MISSING_INVALID_TRAN_CURR;
import static com.incomm.cclp.constants.CurrConvConstants.MISSING_USER_RESPONSE_MSG;
import static com.incomm.cclp.constants.CurrConvConstants.SUCCESS_RESPONSE_CODE;
import static com.incomm.cclp.constants.CurrConvConstants.SUCCESS_RESPONSE_MSG;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.dao.CurrencyConversionDAO;
import com.incomm.cclp.dto.CurrencyConversionReq;
import com.incomm.cclp.dto.CurrencyConversionResp;
import com.incomm.cclp.dto.CurrencyConversionRespDetails;
import com.incomm.cclp.util.Util;

import io.micrometer.core.instrument.util.StringUtils;

@Component
public class CurrencyConversionReqValidation {

	@Autowired
	CurrencyConversionDAO currencyConversionDAO;

	private static final Logger logger = LogManager.getLogger(CurrencyConversionReqValidation.class);

	public CurrencyConversionResp validateHeadersMdmId(List<CurrencyConversionReq> currConvReqList, String user, String channel,
			String mdmId) {
		logger.debug("validateHeadersMdmId START>>");
		CurrencyConversionResp currConvResp = new CurrencyConversionResp();
		if (StringUtils.isEmpty(user)) {
			currConvResp.setResponseCode(FAILURE_RESPONSE_CODE);
			currConvResp.setResponseMessage(MISSING_USER_RESPONSE_MSG);
		} else if (StringUtils.isEmpty(channel)) {
			currConvResp.setResponseCode(FAILURE_RESPONSE_CODE);
			currConvResp.setResponseMessage(MISSING_CHANNEL_RESPONSE_MSG);
		} else if (!currencyConversionDAO.isValidMdmId(mdmId)) {
			currConvResp.setResponseCode(FAILURE_RESPONSE_CODE);
			currConvResp.setResponseMessage(INVALID_MDM_ID);
		}
		logger.debug("validateHeadersMdmId END<<<");
		return currConvResp;
	}

	private CurrencyConversionResp buildResponse(String respMessage, List<CurrencyConversionRespDetails> currConvRespDtlsList) {
		CurrencyConversionResp currConvResp = new CurrencyConversionResp();
		currConvResp.setResponseCode(SUCCESS_RESPONSE_CODE);
		currConvResp.setResponseMessage(respMessage);
		currConvResp.setCurrencyConversionRespDetails(currConvRespDtlsList);
		return currConvResp;
	}

	public List<CurrencyConversionRespDetails> copyRequestToResponseObj(List<CurrencyConversionReq> currConvReqList) {
		List<CurrencyConversionRespDetails> currConvRespDetailList = new ArrayList<CurrencyConversionRespDetails>();

		for (CurrencyConversionReq currConvReq : currConvReqList) {
			CurrencyConversionRespDetails currConvRespdtls = new CurrencyConversionRespDetails();
			try {
				BeanUtils.copyProperties(currConvRespdtls, currConvReq);
			} catch (IllegalAccessException | InvocationTargetException e) {
				logger.warn("Exception occured in copyRequestToResponse method::{}", e.getMessage(), e);
			}
			currConvRespDetailList.add(currConvRespdtls);
		}
		return currConvRespDetailList;
	}

	public CurrencyConversionResp processAddCurrConvs(List<CurrencyConversionRespDetails> currConvRespDtlsList, String mdmId, String user,
			String channel) {
		logger.debug("validateRequestInputs START>>>");
		CurrencyConversionResp currConvResp = new CurrencyConversionResp();
		for (CurrencyConversionRespDetails currConvRespDtl : currConvRespDtlsList) {
			// curr code validation
			if (!(currConvRespDtl.getIssuingCurrency()
				.matches(CURR_CODE_EXPR))) {
				currConvRespDtl.setResponseMessage(MISSING_INVALID_ISSUE_CURR);
				currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
				continue;
				// curr code validation
			} else if (!(currConvRespDtl.getTransactionCurrency()
				.matches(CURR_CODE_EXPR))) {
				currConvRespDtl.setResponseMessage(MISSING_INVALID_TRAN_CURR);
				currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
				continue;
				// exchange rate validation
			} else if (!(String.valueOf(currConvRespDtl.getExchangeRate())
				.matches(CURR_EXPR))) {
				currConvRespDtl.setResponseMessage(MISSING_INVALID_EXCHG_RATE);
				currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
				continue;
				// effectiveDateTime rate validation
			} else if (StringUtils.isNotEmpty(currConvRespDtl.getEffectiveDateTime())
					&& !Util.isValid(currConvRespDtl.getEffectiveDateTime(), DATETIME_FORMAT)) {
				currConvRespDtl.setResponseMessage(INVALID_EFF_DATE);
				currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
				continue;

			} // all validations passed
			else {
				// Call SP to add currency data
				try {
					Map<String, Object> resultMap = currencyConversionDAO.addCurrencyRates(mdmId, channel, user, currConvRespDtl);
					currConvRespDtl.setResponseMessage(resultMap.get("8")
						.toString()
						.equals(FAILURE_RESPONSE_CODE) ? "Error occured while adding/updating records"
								: resultMap.get("9")
									.toString());
					currConvRespDtl.setResponseCode(resultMap.get("8")
						.toString());
				} catch (Exception e) {
					currConvRespDtl.setResponseMessage("Error occured adding/updating records.");
					currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
					logger.warn("Exception occured in calling dao method:: {}", e.getMessage(), e);

				}

			}

		}
		currConvResp = buildResponse(SUCCESS_RESPONSE_MSG, currConvRespDtlsList);
		logger.debug("validateRequestInputs END<<<");
		return currConvResp;
	}

	public CurrencyConversionResp processDelCurrConvs(List<CurrencyConversionRespDetails> currConvRespDtlsList, String mdmId, String user,
			String channel) {
		logger.debug("validateDelRequestInputs START>>>");
		CurrencyConversionResp currConvResp = new CurrencyConversionResp();
		for (CurrencyConversionRespDetails currConvRespDtl : currConvRespDtlsList) {
			// curr code validation
			if (!(currConvRespDtl.getIssuingCurrency()
				.matches(CURR_CODE_EXPR))) {
				currConvRespDtl.setResponseMessage(MISSING_INVALID_ISSUE_CURR);
				currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
				continue;
				// curr code validation
			} else if (!(currConvRespDtl.getTransactionCurrency()
				.matches(CURR_CODE_EXPR))) {
				currConvRespDtl.setResponseMessage(MISSING_INVALID_TRAN_CURR);
				currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
				continue;
				// effectiveDateTime validation
			} else if (!StringUtils.isEmpty(currConvRespDtl.getEffectiveDateTime())
					&& !Util.isValid(currConvRespDtl.getEffectiveDateTime(), DATETIME_FORMAT)) {
				currConvRespDtl.setResponseMessage(INVALID_EFF_DATE);
				currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
				continue;

			} // all validations passed
			else {
				// Call SP to update currency data
				try {
					Map<String, Object> resultMap = currencyConversionDAO.delCurrencyRates(mdmId, channel, user, currConvRespDtl);
					currConvRespDtl.setResponseMessage(resultMap.get("7")
						.toString()
						.equals("1") ? "Error occured while deleting records"
								: resultMap.get("8")
									.toString());
					currConvRespDtl.setResponseCode(resultMap.get("7")
						.toString());
				} catch (Exception e) {
					currConvRespDtl.setResponseMessage("Error occured while deleting records.");
					currConvRespDtl.setResponseCode(FAILURE_RESPONSE_CODE);
					logger.warn("Exception occured in calling delCurrencyRates dao method : " + e.getMessage(), e);

				}

			}

		}
		currConvResp = buildResponse(SUCCESS_RESPONSE_MSG, currConvRespDtlsList);
		logger.debug("validateDelRequestInputs END<<<");
		return currConvResp;
	}

}
