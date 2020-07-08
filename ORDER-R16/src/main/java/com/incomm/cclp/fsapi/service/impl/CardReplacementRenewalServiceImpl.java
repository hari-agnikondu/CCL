package com.incomm.cclp.fsapi.service.impl;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.constants.APIConstants;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.CardReplacementRenewalDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.helper.FSAPIUtils;
import com.incomm.cclp.fsapi.helper.JsonHelper;
import com.incomm.cclp.fsapi.helper.ReqResLogger;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.CardReplacementRenewalService;
import com.incomm.cclp.fsapi.service.PostBackService;
import com.incomm.cclp.fsapi.validation.RegexValidation;

@Repository
public class CardReplacementRenewalServiceImpl extends JdbcDaoSupport implements CardReplacementRenewalService {


	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}


	@Autowired
	public JdbcTemplate jdbctemplate;

	private final Logger log = LogManager.getLogger(this.getClass());

	@Autowired
	ResponseBuilder responseBuilder;

	@Autowired
	ReqResLogger reqResLogger;
	@Autowired
	FSAPIUtils utils;

	@Autowired
	APIHelper apiHelper;

	@Autowired
	private RegexValidation regexValid;

	@Autowired
	PostBackService postBackService;

	@Autowired
	CardReplacementRenewalDAO cardReplacementRenewalDAO;

	@Autowired
	FSAPIUtils fsapiUtils;
	
	/**
	 * card Replacement Renewal Process
	 */
	public ResponseEntity<Object> cardReplacementRenewalProcess(Map<String, String> reqHeaders,
			String replacementReq,Map<String, Object> valueObj) throws ServiceException {

		long timeTaken = 0;
		String apiName = null;
		String reqMethod = null;
		ResponseEntity<Object> resp = null;
		Map<String, Object> valueHashMap = new HashMap<>();

		long timeAfterTxn = 0;
		long timeBeforeTxn = System.currentTimeMillis();
		JSONObject jsonObj = JsonHelper.isJSONValid(replacementReq);

		if (jsonObj != null) {

			try {
				Map<String, Object> valuMap = JsonHelper.jsonToMap(jsonObj);
				if (valuMap != null) {
					Set<String> headerKeys = reqHeaders.keySet();
					for ( String header : headerKeys) {
						valuMap.put(header, reqHeaders.get(header));
					}
					apiName = String.valueOf(valueObj.get(ValueObjectKeys.API_NAME));
					reqMethod = String.valueOf(valueObj.get(ValueObjectKeys.REQUEST_METHOD_TYPE));

					valueHashMap = apiHelper.setReqValues(apiName+reqMethod, valuMap);
					valueHashMap.putAll(valuMap);

					log.debug("replacement  req *****" + valueHashMap);

					log.debug("Input valueHashMap: {} ", valueHashMap.toString());
					valueHashMap.put(ValueObjectKeys.LOG_TXN, APIConstants.N);
					valueHashMap.put(ValueObjectKeys.API_NAME, apiName);
					valueHashMap.put(ValueObjectKeys.REQUEST_METHOD_TYPE, reqMethod);
					valueHashMap.put(ValueObjectKeys.REGEX_API_NAME, apiName+reqMethod);
					fsapiUtils.getDelchannelTranCode(valueHashMap,FSAPIConstants.CARD_REPLACE_RENEWAL_API);

					regexValid.regexValidation(valueHashMap, apiName+reqMethod, true);

					@SuppressWarnings("unchecked")
					List<ErrorMsgBean> errorList = (List<ErrorMsgBean>) valueHashMap
					.get(FSAPIConstants.ERROR_LIST);
					
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
					
					log.error("Error list after regex validation: {} ", String.valueOf(errorList));
					log.debug(" replacement errorList " + errorList);

					if (!errorList.isEmpty()) {
						valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, errorList.get(0).getErrorMsg());
						valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, errorList.get(0).getRespCode());
						log.debug(" replacement Request Failed with validation::: ");

						resp = responseBuilder.buildResponse(valueHashMap);
						utils.getRRN(valueHashMap,FSAPIConstants.FSAPI_RPLRENEWAL_SEQ);
						utils.logTxnDtls(valueHashMap, valueHashMap.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");

					}

					else{
						cardReplacementRenewalDAO.saveCardReplacementOrder(valueHashMap);
						cardReplacementRenewalDAO.validateProxySerial(valueHashMap,errorList);
						if(!errorList.isEmpty()){
							valueHashMap.put(ValueObjectKeys.RESPONSEMESSAGE, errorList.get(0).getErrorMsg());
							valueHashMap.put(ValueObjectKeys.FSAPIRESPONSECODE, errorList.get(0).getRespCode());
							log.debug(" replacement Request Failed with validation::: ");
							resp = responseBuilder.buildResponse(valueHashMap);
							utils.getRRN(valueHashMap,FSAPIConstants.FSAPI_RPLRENEWAL_SEQ);
							utils.logTxnDtls(valueHashMap, valueHashMap.get(ValueObjectKeys.RESPONSEMESSAGE)+"", valueHashMap.get(ValueObjectKeys.FSAPIRESPONSECODE)+"");
						}else{

							valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,B2BResponseCode.SUCCESS);
							valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, B2BResponseMessage.SUCCESS);

							resp = responseBuilder.buildResponse(valueHashMap); 

							postBackService.cardReplaceRenewal(valueHashMap,reqHeaders);  

						}
					}

				}
			}catch(Exception exp){
				log.error("Exception while "+exp);

			}finally{
				timeAfterTxn = System.currentTimeMillis();
				timeTaken = timeAfterTxn - timeBeforeTxn;


				try {
					reqResLogger.reqRespLogger(valueHashMap, String.valueOf(valueHashMap.get(ValueObjectKeys.REQUEST)),timeTaken, 
							String.valueOf(reqHeaders),String.valueOf(valueHashMap.get(ValueObjectKeys.API_NAME)));

				} catch (UnknownHostException e) {
					log.error("Error while logging response message " + e);
				}

			}
		}
		return resp;
	}


}

