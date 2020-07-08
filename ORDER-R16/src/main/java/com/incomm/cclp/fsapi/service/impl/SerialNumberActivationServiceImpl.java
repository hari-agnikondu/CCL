package com.incomm.cclp.fsapi.service.impl;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.constants.APIConstants;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.SerialNumberActivationDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.JsonHelper;
import com.incomm.cclp.fsapi.helper.ReqResLogger;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.CommonService;
import com.incomm.cclp.fsapi.service.SerialNumberActivationService;
import com.incomm.cclp.fsapi.validation.RegexValidation;
/**
 * 
 * @author sampathkumarl
 *
 */
@Service
public class SerialNumberActivationServiceImpl implements SerialNumberActivationService{
	@Autowired
	APIHelper apiHelper;

	@Autowired
	CommonService commonService;

	@Autowired
	private RegexValidation regexValid;


	@Autowired
	ResponseBuilder responseBuilder;

	@Autowired
	ReqResLogger reqResLogger;
	
	@Autowired
	FSAPIUtils utils;
	
	@Autowired
	SerialNumberActivationDAO serialNumberActivationDAO;
	private final Logger logger = LogManager.getLogger(this.getClass());
	
	/**
	 * serialNumberActivationProcess
	 */
	@Override
	public ResponseEntity<Object> serialNumberActivationProcess(Map<String, String> reqHeaders,Map<String, Object> valueObj) throws ServiceException {
		
			Map<String, Object> valueHashMap = new HashMap<>();
			long timeTaken = 0;
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
				    
					apiName = String.valueOf(valueObj.get(ValueObjectKeys.API_NAME));
					reqMethod = String.valueOf(valueObj.get(ValueObjectKeys.REQUEST_METHOD_TYPE));

					final Map<String, Object> valuMap = JsonHelper.jsonToMap(jsonObj);
					valuMap.put(ValueObjectKeys.ACTIVATION_CODE, (String) valuMap.get(ValueObjectKeys.ACTIVATION_CODE));

					if (valuMap != null) {
						valueObj.putAll(valuMap);

						valueHashMap = apiHelper.setReqValues(apiName+reqMethod, valueObj);
						valueHashMap.putAll(valueObj);
						commonService.getBusinessTime(valueHashMap);
						utils.getDelchannelTranCode(valueHashMap, apiName);
						utils.getRRN(valueHashMap,FSAPIConstants.FSAPI_SERIALACTIVATION_SEQ_NAME);
						logger.debug("Input valueHashMap: {} ", valueHashMap.toString());
						valueHashMap.put(ValueObjectKeys.LOG_TXN, APIConstants.N);
						valueHashMap.put(ValueObjectKeys.API_NAME, apiName);
						valueHashMap.put(ValueObjectKeys.REQUEST_METHOD_TYPE, reqMethod);
						valueHashMap.put(ValueObjectKeys.REGEX_API_NAME, apiName+reqMethod);
					
						regexValid.regexValidation(valueHashMap, apiName+reqMethod, true);
						
						@SuppressWarnings("unchecked")
						final List<ErrorMsgBean> errorList = (List<ErrorMsgBean>) valueHashMap
								.get(FSAPIConstants.ORRDER_ERROR_LIST);

						logger.error("Error list after regex validation: {} ", String.valueOf(errorList));

						final String partnerId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));
						String partnerIDPattern = "[0-9\\\\s]{1,38}";
						if (!Pattern.matches(partnerIDPattern, partnerId)) {
							logger.debug(FSAPIConstants.INVALID_PARTNERID_MSG);
							final ErrorMsgBean tempBean = new ErrorMsgBean();
							tempBean.setKey(FSAPIConstants.ORDER_PARTNERID);
							tempBean.setErrorMsg(FSAPIConstants.INVALID_PARTNERID_MSG);
							tempBean.setRespCode(B2BResponseCode.INVALID_PARTNER_ID);
							errorList.add(tempBean);
						}
						if (errorList.isEmpty()) {
							
								serialNumberActivationDAO.serialNumberActivation(valueHashMap,errorList);
								
						}	else {
							
							valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, errorList.get(0).getRespCode());
							valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, errorList.get(0).getErrorMsg());
							
						logger.debug("Request Failed with validation::: ");
						}
					}
				
				
					}
			
			catch (ServiceException cae) {
				
				 valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, cae.getCode());
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, cae.getMessage());
				 
				logger.error("CMSAuthException occured during serial number activation " + cae);
			}catch (Exception ex) {
				logger.error("Exception occured during proxy number activation " + ex);
			} finally {
				
				resp = responseBuilder.buildResponsewithCard(valueHashMap);
				orderReq = String.valueOf(valueObj.get(ValueObjectKeys.REQUEST));
				try {
					 reqResLogger.reqRespLogger(valueHashMap, orderReq,timeTaken, String.valueOf(reqHeaders),String.valueOf(valueHashMap.get(ValueObjectKeys.API_NAME)));
					 if(!valueHashMap.containsKey(ValueObjectKeys.TRAN_LOG_UPDATE)){
							utils.logTxnDtls(valueHashMap, valueHashMap.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");
						}
				} catch (UnknownHostException e) {
					logger.error("Error while logging response message: " + e);
				}catch (Exception e) {
					
					logger.info("Error while logging response message:: " + e);
					logger.error("Error while logging response message:::" + e);
				}
				
			}
			logger.debug("EXIT");
			return resp;
			
			
		}
	

	
	
	

}
