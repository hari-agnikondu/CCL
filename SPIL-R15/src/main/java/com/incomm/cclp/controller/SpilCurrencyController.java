package com.incomm.cclp.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.validation.constraints.Max;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.incomm.cclp.dto.CurrencyConversionReq;
import com.incomm.cclp.dto.CurrencyConversionResp;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCurrencyConversionService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Spil Currency")
@RequestMapping("/currencyConversion")
//@Validated
public class SpilCurrencyController {

	@Autowired
	SpilCurrencyConversionService spilCurrencyConversionService;

	@Autowired
	private ObjectMapper mapper;

	// @Autowired
	// private ObjectReader reader;

	private static final Logger logger = LogManager.getLogger(SpilCurrencyController.class);

	// Add currency conversion records into DB
	@RequestMapping(value = "/{mdmId}", method = RequestMethod.POST, produces = "application/json")
	public CurrencyConversionResp addCurrConversions(@RequestHeader(value = "x-incfs-channel", required = false) String channel,
			@RequestHeader(value = "x-incfs-username", required = false) String user, @PathVariable("mdmId") @Max(20) String mdmId,
			@RequestBody String requestMessage) throws ServiceException, IOException {
		logger.debug("ENTER");

		CurrencyConversionResp responseMsg = null;

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ObjectReader reader = mapper.readerFor(new TypeReference<List<CurrencyConversionReq>>() {
		})
			.withRootName("currencyConversion");
		List<CurrencyConversionReq> currConvReqList = reader.readValue(requestMessage);
		responseMsg = spilCurrencyConversionService.addCurrencyRates(currConvReqList, user, channel, mdmId);

		logger.debug("CurrencyConversionReq-->" + currConvReqList);

		logger.debug("CurrencyConversionResp-->" + responseMsg);

		logger.debug("Exit");
		return responseMsg;
	}

	@RequestMapping(value = "/{mdmId}", method = RequestMethod.DELETE, produces = "application/json")
	public CurrencyConversionResp delCurrConversions(@RequestHeader(value = "x-incfs-channel", required = false) String channel,
			@RequestHeader(value = "x-incfs-username", required = false) String user, @PathVariable("mdmId") @Max(20) String mdmId,
			@RequestBody String requestMessage) throws ServiceException, IOException {
		logger.debug("ENTER delCurrConversions>>");

		CurrencyConversionResp responseMsg = null;

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		ObjectReader reader = mapper.readerFor(new TypeReference<List<CurrencyConversionReq>>() {
		})
			.withRootName("currencyConversion");
		List<CurrencyConversionReq> currConvReqList = reader.readValue(requestMessage);
		responseMsg = spilCurrencyConversionService.delCurrencyRates(currConvReqList, user, channel, mdmId);

		logger.debug("CurrencyConversionReq-->" + currConvReqList);

		logger.debug("CurrencyConversionResp-->" + responseMsg);

		logger.debug("Exit delCurrConversions<<");
		return responseMsg;
	}

	// Gets list of currency conversion details based on mdmId
	@RequestMapping(value = "/{mdmId}", method = RequestMethod.GET, produces = "application/json")
	public CurrencyConversionResp getCurrConversions(@PathVariable("mdmId") @Max(20) String mdmId) throws ServiceException {
		logger.debug("ENTER");
		CurrencyConversionResp responseMsg = spilCurrencyConversionService.getCurrConversions(mdmId);
		logger.debug("Exit");
		return responseMsg;
	}

	/**
	 * Rest end point to getRate
	 * 
	 * @param mdmId,transactionCurrency,issuerCurrency.
	 * @return JSON responseMsg.
	 * @throws SQLException
	 */
	@RequestMapping(value = "/{mdmId}/transactionCurrency/{transactionCurrency}/issuingCurrency/{issuingCurrency}", method = RequestMethod.GET)
	public String getCurrencyRate(@PathVariable("mdmId") @Max(20) String mdmId,
			@PathVariable("transactionCurrency") String transactionCurrency, @PathVariable("issuingCurrency") String issuingCurrency)
			throws ServiceException {
		logger.debug("ENTER");
		String rate = spilCurrencyConversionService.getCurrencyRate(mdmId, transactionCurrency, issuingCurrency);
//		String responseMsg= Util.convertMapToJson(rate);
		String responseMsg = "{\n" + "\"exchangeRate\":" + rate + "\n}";
		logger.debug("exit");
		return responseMsg;
	}

}
