package com.incomm.cclp.fsapi.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.constants.APIConstants;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.CardStatusInquiryDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.OrderValidator;
import com.incomm.cclp.fsapi.helper.ReqResLogger;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.CardStatusInquiryService;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.PostBackService;
import com.incomm.cclp.fsapi.validation.RegexValidation;

@Service
public class CardStatusInquiryServiceImpl implements CardStatusInquiryService {

	@Autowired
	APIHelper apiHelper;

	@Autowired
	CommonService commonService;

	@Autowired
	private RegexValidation regexValid;

	@Autowired
	private OrderValidator orderValidator;

	@Autowired
	ResponseBuilder responseBuilder;

	@Autowired
	CardStatusInquiryDAO cardStatusInquiryDao;

	@Autowired
	ReqResLogger reqResLogger;
	
	@Autowired
	FSAPIUtils utils;
	
	@Autowired
	PostBackService postBackService;

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public ResponseEntity<Object> cardStatusInquiry(Map<String, Object> valueObj) {
		Map<String, Object> valueHashMap = new HashMap<>();
		String apiName = null;
		String reqMethod = null;
		ResponseEntity<Object> resp = null;
		final long startTime = System.currentTimeMillis();

		try {

			apiName = String.valueOf(valueObj.get(ValueObjectKeys.API_NAME));
			reqMethod = String.valueOf(valueObj
					.get(ValueObjectKeys.REQUEST_METHOD_TYPE));

			valueHashMap = apiHelper
					.setReqValues(apiName + reqMethod, valueObj);
			valueHashMap.putAll(valueObj);
			commonService.getBusinessTime(valueHashMap);
			
			utils.getDelchannelTranCode(valueHashMap, apiName);

			valueHashMap.put(ValueObjectKeys.LOG_TXN, APIConstants.N);
			valueHashMap.put(ValueObjectKeys.API_NAME, apiName);
			valueHashMap.put(ValueObjectKeys.REQUEST_METHOD_TYPE, reqMethod);
			valueHashMap.put(ValueObjectKeys.REGEX_API_NAME, apiName
					+ reqMethod);

			// no data found for b2b txn in FSAPI and
			// FSAPI_TRANSACTION..validationBypass
			// flag is present in FSAPI_MAST table
			// so regex validation is mandatory
			regexValid.regexValidation(valueHashMap, apiName + reqMethod, true);

			final List<ErrorMsgBean> errorList = (List<ErrorMsgBean>) valueHashMap
					.get(FSAPIConstants.ORRDER_ERROR_LIST);

			if (valueHashMap.get(FSAPIConstants.POST_BACK_RESP) != null && (FSAPIConstants.ORDER_FSAPI_TRUE
					.equalsIgnoreCase(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())
					|| FSAPIConstants.ORDER_FSAPI_TRUE1
							.equals(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString()))) {

				logger.debug("PostBack enabled");
				if (valueHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL) == null
						|| FSAPIConstants.ORDER_EMPTY_STRING
								.equals(valueHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL))) {

					logger.error("PostBack URL is mandatory if enabled");
					final ErrorMsgBean tempBean = new ErrorMsgBean();
					tempBean.setErrorMsg(FSAPIConstants.MANDATORY_FIELD_FAILURE + FSAPIConstants.COLON_SEPARATOR
							+ FSAPIConstants.RESP_HERDER_POSTBACKURL);
					tempBean.setRespCode(B2BResponseCode.INVALID_FIELD);
					errorList.add(tempBean);
					
				}
			}
			// -----validation check for Partner ID(in Header)-----//
			// partner Id fetched from header
			final String partnerId = String.valueOf(valueHashMap
					.get(FSAPIConstants.ORDER_PARTNERID));
			String partnerIDPattern = "[0-9\\\\s]{1,38}";
			if (!Pattern.matches(partnerIDPattern, partnerId)) {
				logger.error(B2BResponseMessage.INVALID_PARTNER_ID);
				final ErrorMsgBean tempBean = new ErrorMsgBean();
				tempBean.setKey(FSAPIConstants.ORDER_PARTNERID);
				tempBean.setErrorMsg(B2BResponseMessage.INVALID_PARTNER_ID);
				tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
				errorList.add(tempBean);
			}

			String criteria = (String) valueHashMap
					.get(FSAPIConstants.CARDSTAT_CRITERIA);
			String value = (String) valueHashMap.get(FSAPIConstants.CARDSTAT_VALUE);
			
			if (FSAPIConstants.CARDSTAT_EXPIRES_IN.equalsIgnoreCase(criteria)
					|| FSAPIConstants.CARDSTAT_EXPIRES_IN_X_DAYS
							.equalsIgnoreCase(criteria)) {
				orderValidator
						.conditionalMandatoryValidation(valueHashMap,
								Arrays.asList(FSAPIConstants.CARDSTAT_VALUE),
								errorList);
				if (!errorList.isEmpty()) {
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
							errorList.get(0).getRespCode());
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
							errorList.get(0).getErrorMsg());
				} else if (FSAPIConstants.CARDSTAT_EXPIRES_IN
							.equalsIgnoreCase(criteria) && !(value)
							.matches(FSAPIConstants.CARDSTAT_DATEFORMAT_MMYY)) {
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
								FSAPIConstants.INVALID_FIELD + ":"
										+ FSAPIConstants.CARDSTAT_VALUE);
						valueHashMap
								.put(FSAPIConstants.ORDER_RESPONSE_CODE,
										B2BResponseCode.INVALID_FIELD);				
				}
				cardStatusInquiryDao.processCardStatus(valueHashMap, errorList);
				
			} else if (FSAPIConstants.CARDSTAT_LIST_OF_CARDS
					.equalsIgnoreCase(criteria)) {
				orderValidator.conditionalMandatoryValidation(valueHashMap,
						Arrays.asList(FSAPIConstants.TYPE,
								FSAPIConstants.CARDSTAT_VALUE), errorList);
				if (!errorList.isEmpty()) {
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
							errorList.get(0).getRespCode());
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
							errorList.get(0).getErrorMsg());
				} else {
					cardStatusInquiryDao.processCardStatusList(valueHashMap,
							errorList);
				}
			} else if (FSAPIConstants.CARDSTAT_RANGE.equalsIgnoreCase(criteria)) {
				orderValidator.conditionalMandatoryValidation(valueHashMap,
						Arrays.asList(FSAPIConstants.TYPE,
								FSAPIConstants.CARDSTAT_START,
								FSAPIConstants.CARDSTAT_END), errorList);
				if (!errorList.isEmpty()) {
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
							errorList.get(0).getRespCode());
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
							errorList.get(0).getErrorMsg());
				} else {
					orderValidator.proxyorserialRangeValiation(valueHashMap,
							errorList);
					if (errorList.isEmpty()) {
						orderValidator.checkPartnerId(valueHashMap, errorList);
						if (errorList.isEmpty()) {
							orderValidator.validateOrderDetails(valueHashMap,
									errorList);
							if (errorList.isEmpty()) {
								cardStatusInquiryDao.processCardStatusInquiry(
										valueHashMap, errorList);
							} else {
								valueHashMap.put(
										FSAPIConstants.ORDER_RESPONSE_CODE,
										errorList.get(0).getRespCode());
								valueHashMap.put(
										FSAPIConstants.ORDER_RESPONSE_MSG,
										errorList.get(0).getErrorMsg());
							}
						} else {
							valueHashMap.put(
									FSAPIConstants.ORDER_RESPONSE_CODE,
									errorList.get(0).getRespCode());
							valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
									errorList.get(0).getErrorMsg());
						}
					} else {
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
								errorList.get(0).getRespCode());
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
								errorList.get(0).getErrorMsg());
					}

				}
			}
			else
			{
				logger.error(B2BResponseMessage.INVALID_FIELD +": criteria");
				ErrorMsgBean errorbean=new ErrorMsgBean();
    			errorbean.setErrorMsg(B2BResponseMessage.INVALID_FIELD +": criteria");
    			errorbean.setRespCode(B2BResponseCode.INVALID_FIELD);
    			errorList.add(errorbean);
    			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
						errorList.get(0).getRespCode());
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
						errorList.get(0).getErrorMsg());
				
			}
			
			if (errorList.isEmpty()){
				
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.SUCCESS);
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseMessage.SUCCESS);
				resp = responseBuilder.buildCardStatusSuccessResponse(valueHashMap);
			}
			else{
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
						errorList.get(0).getRespCode());
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
						errorList.get(0).getErrorMsg());
				resp = responseBuilder.buildCardStatusFailureResponse(errorList.get(0)
						.getRespCode(), errorList.get(0).getErrorMsg(),
						valueHashMap);

		}	
				} 
		catch (ServiceException ex) {
			logger.error("Error while logging response message " + ex);
			resp = responseBuilder.buildCardStatusFailureResponse(
					ex.getCode(), 
					ex.getMessage(),
					valueHashMap);
		} 
		catch (Exception e) {
			logger.error("Error while logging response message " + e);
			resp = responseBuilder.buildCardStatusFailureResponse(
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR, 
					B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,
					valueHashMap);
		} 
		finally {
			final long endTime = System.currentTimeMillis();
			final long timeTaken = endTime - startTime;

			logger.debug("Total Time Taken......" + timeTaken);
			
		try{
			
			if(B2BResponseCode.SUCCESS.equals(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)))
			{
				postBackService.loggingCardStatusInquiry(valueHashMap);
			}
			else
			{
				utils.getRRN(valueHashMap, FSAPIConstants.RELOADRRNSEQ);
				utils.logTxnDtls(valueHashMap, valueHashMap.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");	
				
			}
			reqResLogger
			.responseLogger(GeneralConstants.ORDER,valueHashMap, null, timeTaken, null);
			}
		
			catch(Exception e)
			{
				logger.info("Exception while updating Transaction log",e);
			}
		
		}
		return resp;
	}
}
