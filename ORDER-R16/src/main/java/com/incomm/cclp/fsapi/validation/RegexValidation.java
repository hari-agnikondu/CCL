package com.incomm.cclp.fsapi.validation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.bean.FsApiValidationDetail;
import com.incomm.cclp.fsapi.constants.APIConstants;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.service.TransactionService;
import com.incomm.cclp.util.Util;

/**
 * 
 *
 */
@Component
public class RegexValidation {
	
	private final Logger logger  = LogManager.getLogger(this.getClass());
	
	@Autowired
	TransactionService transactionService;
	

	/**
	 * 
	 * @param valuMap
	 * @param apiName
	 * @param errListForValFlag
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public void regexValidation(final Map<String, Object> valuMap, final String apiName, boolean errListForValFlag) throws ServiceException {
		final Map<String, FsApiValidationDetail> validationDtlMap = transactionService.getFsapiValidationDetailByApiKey(apiName);
		
		final List<ErrorMsgBean> errList = new LinkedList<>();
		if (validationDtlMap != null) {
			for (final Entry<String, FsApiValidationDetail> temp : validationDtlMap.entrySet()) {
				final String key = temp.getKey();
				final FsApiValidationDetail tempBean = temp.getValue();
				final Object values = valuMap.get(key);
				if (!APIConstants.ORDER_FSAPI_ONE.equals(tempBean.getSubTagField())) {
					boolean valFlag = true;
					try {
						if (values instanceof List) {

							final List<Map<String, String>> tempList = (List<Map<String, String>>) values;
							for (final Map<String, String> tempMap : tempList) {
								jsonObjValidation(validationDtlMap, valFlag, tempMap, tempBean, errList,
										errListForValFlag);
							}
						} else if (values instanceof Map) {
							final Map<String, String> tempMapObj = (Map<String, String>) values;
							jsonObjValidation(validationDtlMap, valFlag, tempMapObj, tempBean, errList, false);
						} else {
							regexvalidation(values, tempBean, valFlag, errList);
						}
					
				}catch (Exception ex) {
					logger.error("Exception Occured while Validate Field", ex);
					logger.debug("OrderValidator   exception key" + key);
					final ErrorMsgBean tempErrBean = new ErrorMsgBean();
					tempErrBean.setErrorMsg(tempBean.getValidationErrMsg() + ":" + tempBean.getFieldName());
					tempErrBean.setKey(tempBean.getFieldName());
					tempErrBean.setRespCode(B2BResponseCode.INVALID_FIELD);
					errList.add(tempErrBean);
				}
			}
		}
		}
		valuMap.put(APIConstants.ERROR_LIST, errList);
	}

	/**
	 * 
	 * @param validationDtlMap
	 * @param valFlag
	 * @param tempMapObj
	 * @param tempBean
	 * @param errList
	 * @param errListForValFlag
	 * @throws ServiceException 
	 */
	private void jsonObjValidation(final Map<String, FsApiValidationDetail> validationDtlMap, boolean valFlag,
			final Map<String, String> tempMapObj, FsApiValidationDetail tempBean, List<ErrorMsgBean> errList,
			boolean errListForValFlag) throws ServiceException {
		List<ErrorMsgBean> errorList = null;
		if (errListForValFlag) {
			errorList = new LinkedList<>();
		} else {
			errorList = errList;
		}
		for (String tempVal : tempBean.getSubTagList()) {
			Object tempVals = tempMapObj.get(tempVal);
			if (tempVals instanceof Map) {
				@SuppressWarnings("unchecked")

				Map<String, String> tempValMap = (Map<String, String>) tempVals;
				jsonObjValidation(validationDtlMap, valFlag, tempValMap, validationDtlMap.get(tempVal), errList, false);
			} else if (tempVals instanceof List) {
				final List<Map<String, String>> tempList = (List<Map<String, String>>) tempVals;
				for (final Map<String, String> tempMap : tempList) {
					jsonObjValidation(validationDtlMap, valFlag, tempMap, validationDtlMap.get(tempVal), errList,
							false);
				}
			} else {
				regexvalidation(tempMapObj.get(tempVal), validationDtlMap.get(tempVal), valFlag, errorList);
			}

		}

			if (errListForValFlag && !errorList.isEmpty()) {
				tempMapObj.put(FSAPIConstants.ERROR_MSG, errorList.get(0).getErrorMsg());
				tempMapObj.put(FSAPIConstants.ORDER_RESPONSE_CODE, errorList.get(0).getRespCode());
		}
	}

	/**
	 * 
	 * @param value
	 * @param tempBean
	 * @param valFlag
	 * @param errorList
	 * @throws ServiceException 
	 */
	private void regexvalidation(final Object value, final FsApiValidationDetail tempBean, boolean valFlag,
			final List<ErrorMsgBean> errorList) throws ServiceException {
		
		boolean mandatoryFlag = false;
		
		if (tempBean != null && APIConstants.MANDATORY_FIELD.equals(tempBean.getValidationType())) {
			if (tempBean.getRegexExpression() != null && value instanceof String && value != null
					&& !APIConstants.EMPTY_STRING.equals(value)) {
				final String tempVal = String.valueOf(value);

				valFlag = Pattern.matches(tempBean.getRegexExpression(), tempVal);
			} else if (tempBean.getRegexExpression() != null && value != null && !APIConstants.EMPTY_STRING.equals(value)) {
				final String tempVal = String.valueOf(value);

				valFlag = Pattern.matches(tempBean.getRegexExpression(), tempVal);
			} else {
				valFlag = false;
				mandatoryFlag = true;
			}
		} else if (tempBean != null && APIConstants.NONMANDATORY_FIELD.equals(tempBean.getValidationType())) {
			if (tempBean.getRegexExpression() != null && value instanceof String && value != null
					&& !APIConstants.EMPTY_STRING.equals(value)) {
				final String tempVal = String.valueOf(value);
				final String regex = String.valueOf(tempBean.getRegexExpression());
				valFlag = Pattern.matches(regex, tempVal);
			}
		} else if (!Util.isEmpty(tempBean.getRegexExpression()) && value != null && !APIConstants.EMPTY_STRING.equals(value)) {
				final String tempVal = String.valueOf(value);
				valFlag = Pattern.matches(tempBean.getRegexExpression(), tempVal);
		} else {
			logger.debug("OrderValidator  validate  tempBean.getValidationType()");
		}
		if (!valFlag) {
			if (mandatoryFlag) {
				logger.error(FSAPIConstants.MANDATORY_FIELD_FAILURE + ":" + tempBean.getFieldName());
				final ErrorMsgBean tempErrBean = new ErrorMsgBean();
				tempErrBean.setErrorMsg(FSAPIConstants.MANDATORY_FIELD_FAILURE + ":" + tempBean.getFieldName());
				tempErrBean.setKey(tempBean.getFieldName());
				
				tempErrBean.setRespCode(B2BResponseCode.REQUIRED_REQUIRED_PROPERTY);
				errorList.add(tempErrBean);
			
			} else {
				logger.error(tempBean.getValidationErrMsg() + ":" + tempBean.getFieldName());
				final ErrorMsgBean tempErrBean = new ErrorMsgBean();
				tempErrBean.setErrorMsg(tempBean.getValidationErrMsg() + ":" + tempBean.getFieldName());
				tempErrBean.setKey(tempBean.getFieldName());
				tempErrBean.setRespCode(B2BResponseCode.INVALID_FIELD);
				errorList.add(tempErrBean);
			}
			


		}
	}

}
