package com.incomm.cclp.fsapi.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
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
import com.incomm.cclp.fsapi.dao.OrderCancelDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.OrderValidator;
import com.incomm.cclp.fsapi.helper.ReqResLogger;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.OrderCancelService;
import com.incomm.cclp.fsapi.service.PostBackService;
import com.incomm.cclp.fsapi.validation.RegexValidation;

@Service
public class OrderCancelServiceImpl implements OrderCancelService {
	
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
	OrderCancelDAO orderCancelDao;

	@Autowired
	ReqResLogger reqResLogger;
	
	@Autowired
	PostBackService postBackService;
	
	@Autowired
	FSAPIUtils utils;

	private final Logger logger = LogManager.getLogger(this.getClass());	

	@Override
	public ResponseEntity<Object> orderCancelService(Map<String, Object> valueObj,Map<String, String> headerObj) {
		
		Map<String, Object> valueHashMap = new HashMap<>();
		String apiName = null;
		String reqMethod = null;
		ResponseEntity<Object> resp = null;
		long startTime = System.currentTimeMillis();
		try{
		apiName = String.valueOf(valueObj.get(ValueObjectKeys.API_NAME));
		reqMethod = String.valueOf(valueObj.get(ValueObjectKeys.REQUEST_METHOD_TYPE));

		valueHashMap = apiHelper.setReqValues(apiName + reqMethod, valueObj);
		valueHashMap.putAll(valueObj);
		valueHashMap.put("headers", headerObj);
		commonService.getBusinessTime(valueHashMap);
		
		utils.getDelchannelTranCode(valueHashMap, apiName);
		
		List<ErrorMsgBean> errorList = new ArrayList<>();
		
		orderValidator.conditionalMandatoryValidation(valueHashMap,Arrays.asList(FSAPIConstants.ORDER_ORDER_ID),errorList);
		if (!errorList.isEmpty()) {
		valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
				errorList.get(0).getRespCode());
		valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
				errorList.get(0).getErrorMsg());
		} 

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
		utils.getRRN(valueHashMap, FSAPIConstants.RELOADRRNSEQ);
		// -----validation check for Partner ID(in Header)-----//
		// partner Id fetched from header
		final String partnerId = String.valueOf(valueHashMap
				.get(FSAPIConstants.ORDER_PARTNERID));
		String partnerIDPattern = "[0-9\\\\s]{1,38}";
		if (!Pattern.matches(partnerIDPattern, partnerId)) {
			logger.error(B2BResponseMessage.INVALID_PARTNER_ID);
			ErrorMsgBean tempBean = new ErrorMsgBean();
			tempBean.setKey(FSAPIConstants.ORDER_PARTNERID);
			tempBean.setErrorMsg(B2BResponseMessage.INVALID_PARTNER_ID);
			tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
			errorList.add(tempBean);
		}
		
		if (!errorList.isEmpty()) {
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,errorList.get(0).getRespCode());
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,errorList.get(0).getErrorMsg());
			} 
		else
		{
			Map<String, Object> respMap = orderCancelDao.cancelOrder(valueHashMap);
			if(!respMap.isEmpty()){
				if(B2BResponseCode.SUCCESS.equals(respMap.get(FSAPIConstants.P_RESP_CODE_OUT)))
				{
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,(String) respMap.get(FSAPIConstants.P_RESP_CODE_OUT));
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, (String) respMap.get(FSAPIConstants.P_RESP_MSG_OUT));	
				}
				else
				{
					
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,(String) respMap.get(FSAPIConstants.P_RESP_CODE_OUT));
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, (String) respMap.get(FSAPIConstants.P_RESP_MSG_OUT));
					logger.error((String) respMap.get(FSAPIConstants.P_RESP_MSG_OUT));
					ErrorMsgBean tempBean = new ErrorMsgBean();
					tempBean.setErrorMsg((String) respMap.get(FSAPIConstants.P_RESP_MSG_OUT));
					tempBean.setRespCode((String) respMap.get(FSAPIConstants.P_RESP_CODE_OUT));
					errorList.add(tempBean);
				}
			}
			else
			{
				logger.error(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				ErrorMsgBean tempBean = new ErrorMsgBean();
				tempBean.setErrorMsg(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				tempBean.setRespCode(B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				errorList.add(tempBean);
			}
		}
		
		if (errorList.isEmpty()){
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.SUCCESS);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseMessage.SUCCESS);
		}
		resp = responseBuilder.buildResponse(valueHashMap);
		}
		catch (ServiceException ex) {
			logger.error("ServiceException while logging response message " + ex);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,ex.getCode());
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, ex.getMessage());
		} 
		catch (Exception e) {
			logger.error("Exception while logging response message " + e);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		} 
		finally {
			try {
			final long endTime = System.currentTimeMillis();
			final long timeTaken = endTime - startTime;
			logger.debug("Total Time Taken......" + timeTaken);
			
			valueHashMap.put("timeTaken", timeTaken);
			
			if(B2BResponseCode.SUCCESS.equals(String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE)))){
				postBackService.cancelOrderProcess(valueHashMap);
				reqResLogger.responseLogger(GeneralConstants.ORDER,valueHashMap, null, timeTaken, String.valueOf(new JSONObject(headerObj)));
			}
			else
			{
					reqResLogger.responseLogger(GeneralConstants.ORDER,valueHashMap, null, timeTaken, String.valueOf(new JSONObject(headerObj)));
					utils.logTxnDtls(valueHashMap, valueHashMap.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");
			}
			
			

			} catch (Exception e) {
				logger.error("Error while logging response message " + e);
			}
		}
		return resp;
	}

}
