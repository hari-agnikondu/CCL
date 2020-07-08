package com.incomm.cclp.fsapi.service.impl;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
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
import com.incomm.cclp.fsapi.dao.OrderProcessDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.JsonHelper;
import com.incomm.cclp.fsapi.helper.OrderValidator;
import com.incomm.cclp.fsapi.helper.ReqResLogger;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.B2BService;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.OrderProcessService;
import com.incomm.cclp.fsapi.service.PostBackService;
import com.incomm.cclp.fsapi.service.SerialNumberRangeActivationService;
import com.incomm.cclp.fsapi.service.TransactionService;
import com.incomm.cclp.fsapi.validation.RegexValidation;
import com.incomm.cclp.service.impl.OrderServiceImpl;
import com.incomm.cclp.util.Util;

@Service
public class B2BServiceImpl implements B2BService {

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
	OrderProcessDAO orderProcessDAO;

	@Autowired
	ReqResLogger reqResLogger;

	@Autowired
	OrderProcessService orderProcessService;

	@Autowired
	TransactionService transactionService;
	@Autowired
	SerialNumberRangeActivationService serialNumberRangeActivationService;
	@Autowired
	PostBackService postBackService;
	
	@Autowired
	ReloadServiceImpl reload;
	
	@Autowired
	OrderServiceImpl orderService;
	
	@Autowired
	FSAPIUtils utils;
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public ResponseEntity<Object> orderProcess(final Map<String, String> reqHeaders, Map<String, Object> valueObj)
			throws ServiceException {

		final long startTime = System.currentTimeMillis();
		Map<String, Object> valueHashMap = new HashMap<>();

		String orderReq = APIConstants.EMPTY_STRING;
		logger.debug("ENTER");

		String apiName = null;
		String reqMethod = null;
		ResponseEntity<Object> resp = null;

		try {

			final Set<String> headerKeys = reqHeaders.keySet();
			for (final String header : headerKeys) {
				valueObj.put(header, reqHeaders.get(header));
			}

			JSONObject jsonObj = JsonHelper.isJSONValid(String.valueOf(valueObj.get(ValueObjectKeys.REQUEST)));
			jsonObj = (JSONObject) jsonObj.get(FSAPIConstants.HEADER_FIELD);

			if (jsonObj != null) {
				apiName = String.valueOf(valueObj.get(ValueObjectKeys.API_NAME));
				reqMethod = String.valueOf(valueObj.get(ValueObjectKeys.REQUEST_METHOD_TYPE));

				final Map<String, Object> valuMap = JsonHelper.jsonToMap(jsonObj);

				if (valuMap != null) {
					valueObj.putAll(valuMap);

					valueHashMap = apiHelper.setReqValues(apiName + APIConstants.COLON + reqMethod, valueObj);
					valueHashMap.putAll(valueObj);
					commonService.getBusinessTime(valueHashMap);

					logger.debug("Input valueHashMap: {} ", valueHashMap.toString());
					valueHashMap.put(ValueObjectKeys.LOG_TXN, APIConstants.N);
					valueHashMap.put(ValueObjectKeys.API_NAME, apiName);
					valueHashMap.put(ValueObjectKeys.REQUEST_METHOD_TYPE, reqMethod);
					valueHashMap.put(ValueObjectKeys.REGEX_API_NAME, apiName + APIConstants.COLON + reqMethod);

					
					regexValid.regexValidation(valueHashMap, apiName + APIConstants.COLON + reqMethod, true);
					
					@SuppressWarnings("unchecked")
					final List<ErrorMsgBean> errorList = (List<ErrorMsgBean>) valueHashMap
							.get(FSAPIConstants.ORRDER_ERROR_LIST);

					logger.error("Error list after regex validation: {} ", String.valueOf(errorList));
					logger.error("Error list with Line item after regex validation: {} ", valueHashMap
							.get(FSAPIConstants.ORDER_LINE_ITEMDTLS));
					
					final String partnerId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));
					String partnerIDPattern = "[0-9\\\\s]{1,38}";
					if (!Pattern.matches(partnerIDPattern, partnerId)) {
						logger.error(FSAPIConstants.INVALID_PARTNERID_MSG);
						final ErrorMsgBean tempBean = new ErrorMsgBean();
						tempBean.setKey(FSAPIConstants.ORDER_PARTNERID);
						tempBean.setErrorMsg(FSAPIConstants.INVALID_PARTNERID_MSG);
						tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
						errorList.add(tempBean);
					}

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
					
					

					orderValidator.validate(valueHashMap);

					if (FSAPIConstants.Y
							.equalsIgnoreCase(String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_DUPLICATE_CHECK)))) {
						logger.debug("Duplicate order found");
						valueHashMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, B2BResponseCode.DUPLICATE_ORDER);
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseMessage.DUPLICATE_ORDER);
					} else if (FSAPIConstants.Y.equalsIgnoreCase(
							String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_DUPLICATE_LINEITEM)))) {
						logger.debug("Duplicate order line item found");
						valueHashMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, B2BResponseCode.DUPLICATE_ORDER_LINE_ITEM);
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseCode.INVALID_LINE_ITEM_ID);

					} else {
						
						orderProcessDAO.saveOrders(valueHashMap);
						
					}
					orderProcessDAO.processOrder(valueHashMap);
					
					resp = responseBuilder.buildSuccessResponse(valueHashMap);
				}
			}

		} catch (ServiceException se) {
			logger.debug("ServiceException" + se);
			valueHashMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, se.getCode());
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, se.getMessage());
		} catch (JSONException je) {
			logger.debug("JSONException" + je);
			valueHashMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
					B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		} catch (Exception e) {
			logger.debug("Exception" + e);
			valueHashMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
					B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		} finally {
			final long endTime = System.currentTimeMillis();
			final long timeTaken = endTime - startTime;

			logger.debug("Total Time Taken......" + timeTaken);

			if (resp == null) {
				resp = responseBuilder.buildSuccessResponse(valueHashMap);
			}

			orderReq = String.valueOf(valueObj.get(ValueObjectKeys.REQUEST));
			try {
				reqResLogger.responseLogger(GeneralConstants.ORDER,valueHashMap, orderReq, timeTaken,
						String.valueOf(new JSONObject(reqHeaders)));
				
				
			} catch (UnknownHostException e) {
				logger.error("Error while logging respse message " + e);
			}
		}

		logger.debug("EXIT");
		return resp;

	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<Object> orderStatusProcess(Map<String, Object> valueObj) {
		Map<String, Object> valueHashMap = new HashMap<>();
		String apiName = null;
		String reqMethod = null;
		ResponseEntity<Object> resp = null;
		final long startTime = System.currentTimeMillis();

		try {

			apiName = String.valueOf(valueObj.get(ValueObjectKeys.API_NAME));
			reqMethod = String.valueOf(valueObj.get(ValueObjectKeys.REQUEST_METHOD_TYPE));

			valueHashMap = apiHelper.setReqValues(apiName + reqMethod, valueObj);
			valueHashMap.putAll(valueObj);
			commonService.getBusinessTime(valueHashMap);

		
			valueHashMap.put(ValueObjectKeys.LOG_TXN, APIConstants.N);
			valueHashMap.put(ValueObjectKeys.API_NAME, apiName);
			valueHashMap.put(ValueObjectKeys.REQUEST_METHOD_TYPE, reqMethod);
			valueHashMap.put(ValueObjectKeys.REGEX_API_NAME, apiName + reqMethod);

			regexValid.regexValidation(valueHashMap, apiName + reqMethod, true);

			final List<ErrorMsgBean> errorList = (List<ErrorMsgBean>) valueHashMap
					.get(FSAPIConstants.ORRDER_ERROR_LIST);

			
			final String partnerId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));
			String partnerIDPattern = "[0-9\\\\s]{1,38}";
			if (!Pattern.matches(partnerIDPattern, partnerId)) {
				logger.error(FSAPIConstants.INVALID_PARTNERID_MSG);
				final ErrorMsgBean tempBean = new ErrorMsgBean();
				tempBean.setKey(FSAPIConstants.ORDER_PARTNERID);
				tempBean.setErrorMsg(FSAPIConstants.INVALID_PARTNERID_MSG);
				tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
				errorList.add(tempBean);
			}

			String orderId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDERID));

			if (errorList.isEmpty()) {
				orderValidator.checkOrderIdForActivation(orderId, partnerId, errorList);
				orderValidator.checkLineItemIds(orderId, partnerId,
						valueHashMap, errorList);
				if (errorList.isEmpty()) {
					orderValidator.getOrderStatus(valueHashMap);
					orderValidator.getlineItemDtls(valueHashMap, apiName);
					

				} else {
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, errorList.get(0).getRespCode());
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, errorList.get(0).getErrorMsg());
				}
			} else {
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, errorList.get(0).getRespCode());
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, errorList.get(0).getErrorMsg());
				
			}
			if (errorList.isEmpty())
				resp = responseBuilder.buildOrderStatusSuccessResponse(valueHashMap);
			else
				resp = responseBuilder.buildFailureResponse(errorList.get(0).getRespCode(),
						errorList.get(0).getErrorMsg(), valueObj);

		} catch (ServiceException se) {
			logger.info("ServiceException",se);
			se.printStackTrace();
			valueHashMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, se.getCode());
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, se.getMessage());
		} 
		catch (Exception e) {
			logger.info("Main Exception",e);
			e.printStackTrace();
			/*valueHashMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, se.getCode());
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, se.getMessage());*/
		} finally {
			
			final long endTime = System.currentTimeMillis();
			final long timeTaken = endTime - startTime;

			logger.debug("Total Time Taken......" + timeTaken);

			if (resp == null) {
				resp = responseBuilder.buildOrderStatusSuccessResponse(valueHashMap);
			}
			try {
				reqResLogger.responseLogger(String.valueOf(valueHashMap.get(ValueObjectKeys.API_NAME)),valueHashMap, null, timeTaken,
						null);
				
				
			} catch (UnknownHostException e) {
				logger.error("Error while logging respse message " + e);
			}

		}
		
		logger.info("respnse {}***********"+resp);
		return resp;

	}
	public ResponseEntity<Object> serialNumberRangeActivation(Map<String,Object> valuHashMap,Map<String, String> reqHeaders) throws ServiceException {
		logger.debug("serialNumberRangeActivation");
		long timeTaken = 0;
		long timeAfterTxn = 0;
		long timeBeforeTxn = System.currentTimeMillis();
		ResponseEntity<Object> resp = null;
		JSONObject reqHeadersJsonObj=null;
		try {
			JSONObject jsonObj = JsonHelper.isJSONValid(String.valueOf(valuHashMap.get(ValueObjectKeys.REQUEST)));
			if (jsonObj != null) {
				final Map<String, Object> valuMap = JsonHelper.jsonToMap(jsonObj);

				if (valuMap != null) {
					valuHashMap.putAll(valuMap);
					Map<String, Object> tempValuMap =  apiHelper.setReqValues(FSAPIConstants.SERIALNUMBER_RANGE_VALIDATION_API+FSAPIConstants.PUT, valuMap);
					valuHashMap.putAll(tempValuMap);
					valuHashMap.put(ValueObjectKeys.X_INCFS_CHANNEL, (String) reqHeaders.get(ValueObjectKeys.X_INCFS_CHANNEL));
					utils.getDelchannelTranCode(valuHashMap, FSAPIConstants.ACTIVATION_API);
					regexValid.regexValidation(valuHashMap,FSAPIConstants.SERIALNUMBER_RANGE_VALIDATION_API+FSAPIConstants.PUT,false);
					@SuppressWarnings("unchecked")
					final List<ErrorMsgBean> errorList = (List<ErrorMsgBean>) valuHashMap.get(FSAPIConstants.ORRDER_ERROR_LIST);
					String partnerId = (String) valuHashMap.get(FSAPIConstants.ORDER_PARTNERID);
					if (!Pattern.matches("[0-9\\\\s]{1,38}", partnerId)) {
						logger.error(B2BResponseMessage.INVALID_PARTNER_ID);
						final ErrorMsgBean tempBean = new ErrorMsgBean();
						tempBean.setKey(FSAPIConstants.ORDER_PARTNERID);
						tempBean.setErrorMsg(B2BResponseMessage.INVALID_PARTNER_ID);
						tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
						errorList.add(tempBean);
					}
					if(valuHashMap.get(FSAPIConstants.POST_BACK_RESP) != null &&(
							"true".equalsIgnoreCase(valuHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())
							|| "1".equals(valuHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())) && (valuHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL) == null || "".equals(valuHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL))))
					{				
							logger.error(FSAPIConstants.MANDATORY_FIELD_FAILURE + ":" + FSAPIConstants.RESP_HERDER_POSTBACKURL);
							final ErrorMsgBean tempBean = new ErrorMsgBean();
							tempBean.setErrorMsg(FSAPIConstants.MANDATORY_FIELD_FAILURE + ":" + FSAPIConstants.RESP_HERDER_POSTBACKURL);
							tempBean.setRespCode(B2BResponseCode.INVALID_FIELD);
							errorList.add(tempBean);

					}

					if (errorList.isEmpty()) {
						serialNumberRangeActivationService.serialNumberRangeActivation(valuHashMap);
					} else {
						valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, errorList.get(0).getRespCode());
						valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, errorList.get(0).getErrorMsg());
						logger.debug("Request Failed with validation:: ");
					}
				} else {
					logger.debug("JSON object was not valid: ");
					valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, FSAPIConstants.ORDER_RESP_DECRESCODE);
					valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, "JSON object was not valid::: ");
				}
			} else {
				logger.debug("JSON object was not valid::: ");
				valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, FSAPIConstants.ORDER_RESP_DECRESCODE);
				valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, "JSON object was not valid::: ");
			}

		} 
		catch (ServiceException se) {
			logger.error("ServiceException in orderActivation:"+se);
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, se.getCode());
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, se.getMessage());
		} 
		catch (Exception ex) {
			logger.error("Exception while getting connection " , ex);
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, ex.getMessage());
		} finally {
			
			resp=responseBuilder.buildResponse(valuHashMap);

			timeAfterTxn = System.currentTimeMillis();
			timeTaken = timeAfterTxn - timeBeforeTxn;

			reqHeadersJsonObj=new JSONObject(reqHeaders);
			
			try {
				 reqResLogger.reqRespLogger(valuHashMap, String.valueOf(valuHashMap.get(ValueObjectKeys.REQUEST)),
						 timeTaken, String.valueOf(reqHeadersJsonObj),String.valueOf(valuHashMap.get(ValueObjectKeys.API_NAME)));
			 } catch (UnknownHostException e) {
				 logger.error("Error while logging response message:: " + e);
			 }
			serialNumberRangeActivationService.logAPIRequestDtls(valuHashMap,(String)valuHashMap.get(FSAPIConstants.OUT_RESP_STR));

			if(B2BResponseCode.SUCCESS.equals(valuHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE))) 
			{
				postBackService.callPostBackProcess(valuHashMap,FSAPIConstants.SERIALNUMBER_RANGE_VALIDATION_API,timeTaken,String.valueOf(reqHeadersJsonObj));
			}
			else{
				utils.getRRN(valuHashMap,  FSAPIConstants.FSAPI_SERIALNUMBERRANGE_ACTIVATION_SEQ_NAME);
				utils.logTxnDtls(valuHashMap, valuHashMap.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valuHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");
			}
			
			
		}
		return resp; 

	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ResponseEntity<Object> orderActivation(Map<String, Object> valueObj,Map<String, String> reqHeaders) throws ServiceException {
		long startTime = System.currentTimeMillis();
		Map<String, Object> valueHashMap = new HashMap<>();
		logger.debug("ENTER");
		String apiName = null;
		String reqMethod = null;
		ResponseEntity<Object> resp = null;
		boolean orderLineItemStatus = false;
		boolean checkLineItemStatus = false;
		List<Map<String, Object>> lineItemStatus = null;
		try {
			JSONObject jsonObj = JsonHelper.isJSONValid(String.valueOf(valueObj.get(ValueObjectKeys.REQUEST)));

			apiName = String.valueOf(valueObj.get(ValueObjectKeys.API_NAME));
			reqMethod = String.valueOf(valueObj.get(ValueObjectKeys.REQUEST_METHOD_TYPE));
			Map<String, Object> valuMap = JsonHelper.jsonToMap(jsonObj);
			
			if (valuMap != null) {
				String orderId = String.valueOf(valueObj.get(FSAPIConstants.ORDERID));
				Set<String> lineItemSet = new HashSet<>();
				lineItemSet = Util.csvToSet((String) valuMap.get("lineItemID"));
				valuMap.put(FSAPIConstants.LINEITEMID, lineItemSet);
				valueObj.putAll(valuMap);

				valueHashMap = apiHelper.setReqValues(apiName+ APIConstants.COLON + reqMethod, valueObj);
				valueHashMap.putAll(valueObj);
				commonService.getBusinessTime(valueHashMap);
				valueHashMap.put(ValueObjectKeys.X_INCFS_CHANNEL, (String) reqHeaders.get(ValueObjectKeys.X_INCFS_CHANNEL));
				utils.getDelchannelTranCode(valueHashMap, FSAPIConstants.ACTIVATION_API);
				logger.debug("Input valueHashMap: {} ", valueHashMap.toString());
				valueHashMap.put(ValueObjectKeys.LOG_TXN, APIConstants.N);
				valueHashMap.put(ValueObjectKeys.API_NAME, apiName);
				valueHashMap.put(ValueObjectKeys.REQUEST_METHOD_TYPE, reqMethod);
				valueHashMap.put(ValueObjectKeys.REGEX_API_NAME, apiName
						+ APIConstants.COLON + reqMethod);

				regexValid.regexValidation(valueHashMap, apiName+ APIConstants.COLON + reqMethod, false);

				String partnerId = (String) valueHashMap.get(FSAPIConstants.ORDER_PARTNERID);
			
				List<ErrorMsgBean> errorList = (List<ErrorMsgBean>) valueHashMap.get(FSAPIConstants.ORRDER_ERROR_LIST);
				if (!Pattern.matches("[0-9\\\\s]{1,38}", partnerId)) {
					logger.error(B2BResponseMessage.INVALID_PARTNER_ID);
					final ErrorMsgBean tempBean = new ErrorMsgBean();
					tempBean.setKey(FSAPIConstants.ORDER_PARTNERID);
					tempBean.setErrorMsg(B2BResponseMessage.INVALID_PARTNER_ID);
					tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
					errorList.add(tempBean);
				}
				if (valueHashMap.get(FSAPIConstants.POST_BACK_RESP) != null
						&& ("true".equalsIgnoreCase(valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString()) || "1"
								.equals((String) valueHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())) && (valueHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL) == null
										|| "".equals(valueHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL)))) {
					    logger.error(B2BResponseMessage.INVALID_FIELD+ ":" + FSAPIConstants.RESP_HERDER_POSTBACKURL);
						final ErrorMsgBean tempBean = new ErrorMsgBean();
						tempBean.setErrorMsg(B2BResponseMessage.INVALID_FIELD+ ":" + FSAPIConstants.RESP_HERDER_POSTBACKURL);
						tempBean.setRespCode(B2BResponseCode.INVALID_FIELD);
						errorList.add(tempBean);
					
				}

				if (errorList.isEmpty()) {
					orderValidator.checkOrderIdForActivation(orderId, partnerId, errorList);
					if(errorList.isEmpty()){
					orderValidator.checkLineItemIdsForActivation(orderId, partnerId,(Set<String>) valueHashMap.get(FSAPIConstants.LINEITEMID), errorList);
					}
					if (errorList.isEmpty()) {
						orderValidator.getOrderStatus(valueHashMap);
						orderValidator.getlineItemDtls(valueHashMap, apiName);
						if (lineItemSet.isEmpty()) {
							if (!((valueHashMap.get(FSAPIConstants.ORDER_STATUS).toString()).equalsIgnoreCase(FSAPIConstants.ORDER_LINEITEM_STATUS_COMPLETED)) ||  (FSAPIConstants.ORDER_LINEITEM_STATUS_SHIPPED.equalsIgnoreCase(valueHashMap.get(FSAPIConstants.ORDER_STATUS)+""))) {
								valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.INVALID_ORDER_STATUS);
								valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.INVALID_ORDER_STATUS);
								orderLineItemStatus = true;
							}
						} else {
							lineItemStatus = (LinkedList<Map<String, Object>>) valueHashMap.get(FSAPIConstants.ORDER_LINE_ITEMDTLS);
							if (lineItemStatus != null) {
								ListIterator list = lineItemStatus.listIterator();
								while (list.hasNext()) {
									Map<String, String> lineItemStat = (Map<String, String>) list.next();
									String value = lineItemStat.get(FSAPIConstants.LINEITEM_STATUS);
									if (value != null && (value.equalsIgnoreCase(FSAPIConstants.ORDER_LINEITEM_STATUS_COMPLETED) || value.equalsIgnoreCase(FSAPIConstants.ORDER_LINEITEM_STATUS_SHIPPED))) {
										checkLineItemStatus = true;
									}
								}
								if (!checkLineItemStatus) {
									valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.INVALID_LINE_ITEM_STATUS);
									valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.INVALID_LINE_ITEM_STATUS);
									orderLineItemStatus = true;
								}
							}
						}
						if (!orderLineItemStatus) {
							valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.SUCCESS);
							valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseMessage.SUCCESS);
						}

					} else {
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,errorList.get(0).getRespCode());
						valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,errorList.get(0).getErrorMsg());
						orderLineItemStatus = true;
					}
				} else {
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,errorList.get(0).getRespCode());
					valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,errorList.get(0).getErrorMsg());
					orderLineItemStatus = true;
				}
			} else {
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,"JSON object was not valid::: ");
				orderLineItemStatus = true;
			}

		} catch (ServiceException se) {
			logger.error("ServiceException in orderActivation:"+se);
			valueHashMap.put(FSAPIConstants.ORDER_STATUS,FSAPIConstants.ORDER_REJECT_STATUS);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, se.getCode());
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, se.getMessage());
		} 
		 catch (Exception e) {
				logger.error("Exception in orderActivation:"+e);
			 valueHashMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
			 valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			 valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		 } finally {

			 final long endTime = System.currentTimeMillis();
			 final long timeTaken = endTime - startTime;

			 logger.debug("Total Time Taken......" + timeTaken);

			 if (!orderLineItemStatus) {
				 postBackService.callPostBackProcess(valueHashMap,FSAPIConstants.ORDER_ACTIVATION_API, timeTaken,String.valueOf(new JSONObject(reqHeaders)));
			 }
			 else{
					utils.getRRN(valueHashMap,  FSAPIConstants.FSAPI_BULKORDER_ACTIVATION_SEQ_NAME);
					utils.logTxnDtls(valueHashMap, valueHashMap.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");
			 }
			  
			  if (resp == null) {
					 resp = responseBuilder.buildResponse(valueHashMap);
				 }
			  utils.logAPIRequestDtls(valueHashMap);
			  
			 try {
				 reqResLogger.reqRespLogger(valueHashMap, String.valueOf(valueObj.get(ValueObjectKeys.REQUEST)),
						 timeTaken, String.valueOf(new JSONObject(reqHeaders)),String.valueOf(valueHashMap.get(ValueObjectKeys.API_NAME)));
			 } catch (UnknownHostException e) {
				 logger.error("Error while logging response message " + e);
			 }
		 }

		logger.debug("EXIT");
		return resp;

	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public ResponseEntity<Object> reloadProcess(Map<String,Object> valuHashMap,Map<String, String> reqHeaders) throws ServiceException{
		logger.debug("reloadProcess");
		long timeTaken = 0;
		long timeAfterTxn = 0;
		long timeBeforeTxn = System.currentTimeMillis();
		JSONObject reqHeadersJsonObj = null;
		ResponseEntity<Object> resp = null;
		try {
			JSONObject jsonObj = JsonHelper.isJSONValid(String.valueOf(valuHashMap.get(ValueObjectKeys.REQUEST)));
			if (jsonObj != null) {
				final Map<String, Object> valuMap = JsonHelper.jsonToMap(jsonObj);

				if (valuMap != null) {
					valuHashMap.putAll(valuMap);
					reqHeadersJsonObj=new JSONObject(valuMap);
					Map<String, Object> tempValuMap =  apiHelper.setReqValues(FSAPIConstants.RELOAD_API+FSAPIConstants.PUT, valuMap);
					valuHashMap.putAll(tempValuMap);
					valuHashMap.put(ValueObjectKeys.X_INCFS_CHANNEL, (String) reqHeaders.get(ValueObjectKeys.X_INCFS_CHANNEL));
					commonService.getBusinessTime(valuHashMap);
					utils.getDelchannelTranCode(valuHashMap, FSAPIConstants.RELOAD_API);
					regexValid.regexValidation(valuHashMap,FSAPIConstants.RELOAD_API+FSAPIConstants.PUT,false);
					final List<ErrorMsgBean> errorList = (List<ErrorMsgBean>) valuHashMap.get(FSAPIConstants.ORRDER_ERROR_LIST);
					String partnerId = (String) valuHashMap.get(FSAPIConstants.ORDER_PARTNERID);
					if (!Pattern.matches("[0-9\\\\s]{1,38}", partnerId)) {
						logger.error(B2BResponseMessage.INVALID_PARTNER_ID);
						final ErrorMsgBean tempBean = new ErrorMsgBean();
						tempBean.setKey(FSAPIConstants.ORDER_PARTNERID);
						tempBean.setErrorMsg(B2BResponseMessage.INVALID_PARTNER_ID);
						tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
						errorList.add(tempBean);
					}
					if(valuHashMap.get(FSAPIConstants.POST_BACK_RESP) != null &&(
							"true".equalsIgnoreCase(valuHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())
							|| "1".equals(valuHashMap.get(FSAPIConstants.POST_BACK_RESP).toString())) && (valuHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL) == null || "".equals(valuHashMap.get(FSAPIConstants.RESP_HERDER_POSTBACKURL))))
					{
							logger.error(FSAPIConstants.MANDATORY_FIELD_FAILURE + ":" + FSAPIConstants.RESP_HERDER_POSTBACKURL);
							final ErrorMsgBean tempBean = new ErrorMsgBean();
							tempBean.setErrorMsg(FSAPIConstants.MANDATORY_FIELD_FAILURE + ":" + FSAPIConstants.RESP_HERDER_POSTBACKURL);
							tempBean.setRespCode(B2BResponseCode.INVALID_FIELD);
							errorList.add(tempBean);

					}
					logger.info(" errorList " + errorList.size());
					if (errorList.isEmpty()) {
						reload.getOrderID(valuHashMap);
						reload.insertReloadDtls(valuHashMap);
						LinkedList cardlist = (LinkedList) valuHashMap.get(FSAPIConstants.CARDS);
						logger.info("cardlist " + cardlist);
						ListIterator list = cardlist.listIterator();
						while (list.hasNext()) {
							Map<String, String> cardlst = (Map<String, String>) list.next();
							for (Entry<String, String> cardTemp : cardlst.entrySet()) {
								logger.info("cardTemp key" + cardTemp.getKey());
								logger.info("cardTemp value" + cardTemp.getValue());
								valuHashMap.put(cardTemp.getKey(), cardTemp.getValue());
							}
							reload.insertReloadmast(valuHashMap);
						}
						reload.validateProxySerial(valuHashMap,errorList);
						if(!errorList.isEmpty()){
							valuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, errorList.get(0).getErrorMsg());
							valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, errorList.get(0).getRespCode());
							logger.info("proxy nubmer  validation::: ");
						}else{
							reload.partnerIdValidation(valuHashMap,errorList);
							logger.info(" errorList " + errorList.size());
							if(!errorList.isEmpty()){
								valuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, errorList.get(0).getErrorMsg());
								valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, errorList.get(0).getRespCode());
								logger.info("Request Failed with validation::: ");
							} else {
								valuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE,B2BResponseMessage.SUCCESS);
								valuHashMap	.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.SUCCESS);
							}
						}
					} else {
						valuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, errorList.get(0).getErrorMsg());
						valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, errorList.get(0).getRespCode());
						logger.info("Request Failed with validation::: ");
					}

				} else {
					logger.info("JSON object was not valid::: ");
					valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, FSAPIConstants.ORDER_RESP_DECRESCODE);
					valuHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, "JSON object was not valid::: ");
				}
			} else {
				logger.info("JSON object was not valid::: ");
				valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, FSAPIConstants.ORDER_RESP_DECRESCODE);
				valuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, "JSON object was not valid::: ");
			}

		} 
		catch (ServiceException se) {
			logger.error("ServiceException in reloadProcess:"+se);
			valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, se.getCode());
			valuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, se.getMessage());
		} 
		catch (Exception ex) {
			logger.error("Exception in reloadProcess " , ex);
			valuHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			valuHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, ex.getMessage());
		} finally {
			if (resp == null) {
				resp = responseBuilder.buildResponse(valuHashMap);
			}
			if(B2BResponseCode.SUCCESS.equals(valuHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE))){
				//Calling Reload Post Back 
				postBackService.reloadPostBack(valuHashMap); 
			}
			else{
				utils.getRRN(valuHashMap,  FSAPIConstants.RELOADRRNSEQ);
				utils.logTxnDtls(valuHashMap, valuHashMap.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valuHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");
			}
		
			timeAfterTxn = System.currentTimeMillis();
			timeTaken = timeAfterTxn - timeBeforeTxn;

			if(reqHeadersJsonObj == null){
				reqHeadersJsonObj = new JSONObject(reqHeaders);
			}
			try {
				reqResLogger.reqRespLogger(valuHashMap, String.valueOf(valuHashMap.get(ValueObjectKeys.REQUEST)),
						timeTaken, String.valueOf(reqHeadersJsonObj),String.valueOf(valuHashMap.get(ValueObjectKeys.API_NAME)));
			} catch (UnknownHostException e) {
				logger.error("Error while logging response message " + e);
			}
		}
		return resp;
	}
}
