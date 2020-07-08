package com.incomm.cclp.fsapi.helper;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.FsApiDetail;
import com.incomm.cclp.fsapi.constants.APIConstants;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.service.TransactionService;


@Component
public class APIHelper {

	@Autowired
	TransactionService transactionService;
	
	private final Logger logger = LogManager.getLogger(this.getClass());
	/**
	 * 
	 * @param apiName
	 * @param valuMap
	 * @return
	 * @throws ServiceException 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> setReqValues(final String apiName, final Map<String, Object> valuMap) throws ServiceException {
		final FsApiDetail fsapiDetailsBean =  transactionService.getFsapiDetailByApiKey(apiName);
		logger.debug("tempFSAPIBean   " + fsapiDetailsBean);
		final Map<String, Object> tempValueMap = valuMap;
 		final Map<String, Object> valueHashMap = new HashMap<>();
		try {
			if (fsapiDetailsBean != null) {
				String reqParse=String.valueOf(tempValueMap.get(ValueObjectKeys.REQ_PARSE));
				for (Entry<String, Object> temp : tempValueMap.entrySet()) {
					final String key = temp.getKey();
					final Object value = temp.getValue();
 					final String key1 = fsapiDetailsBean.getReqFields().get(key);
					if (key1 != null) {
						if (value instanceof List) {
							List<Map<String, Object>> newTempList = listReqParse(value, fsapiDetailsBean, key1,reqParse,valueHashMap);
							valueHashMap.put(key1, newTempList);
						} else if (value instanceof Map) {
							Map<String, Object> tempMapObj = (Map<String, Object>) value;
							Map<String, Object> tmps = reqMapParse(fsapiDetailsBean, key1, tempMapObj,reqParse,valueHashMap);
							valueHashMap.put(key1, tmps);

						} else {
							valueHashMap.put(key1, value);
						}
					}
				}
			}
		} catch (Exception exp) {
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}
		return valueHashMap;
	}
	
	
	/**
	 * 
	 * @param value
	 * @param fsapiDetailsBean
	 * @param key1
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> listReqParse(Object value, FsApiDetail fsapiDetailsBean, String key1, String reqParse, Map<String, Object> valueHashMap) {
		List<Map<String, Object>> tempList = (List<Map<String, Object>>) value;
		List<Map<String, Object>> newTempList = new LinkedList<>();
		for (Map<String, Object> tempMap : tempList) {
			Map<String, Object> tmps = reqMapParse(fsapiDetailsBean, key1, tempMap,reqParse,valueHashMap);
			newTempList.add(tmps);
		}
		return newTempList;
	}

	/**
	 * 
	 * @param fsapiDetailsBean
	 * @param key1
	 * @param tempMapObj
	 * @param tempNewMapObj
	 * @return
	 */
	private Map<String, Object> reqMapParse(final FsApiDetail fsapiDetailsBean, final String key1,
			Map<String, Object> tempMapObj, String reqParse, Map<String, Object> valueHashMap) {
		Map<String, Object> tempNewMapObj = new HashMap<>();
		Map<String, String> fieldDtls = fsapiDetailsBean.getReqSubTagFields().get(key1);
		logger.debug("fieldDtls  ::: " + fieldDtls);
		if (fieldDtls != null) {
			for (Entry<String, Object> tempEntry : tempMapObj.entrySet()) {
				tempNewMapObj.put(fieldDtls.get(tempEntry.getKey()), tempEntry.getValue());
				//added by nawaz on 24082018 for update alerts
				valueHashMap.put(fieldDtls.get(tempEntry.getKey()), tempEntry.getValue());
				if(APIConstants.Y.equals(reqParse)){
					if(tempEntry.getValue() instanceof Map){
							valueHashMap.putAll((Map<? extends String, ? extends Object>) tempEntry.getValue());
					}else{
							valueHashMap.putAll(tempNewMapObj);
					}	
				} 
			}
		}
		return tempNewMapObj;
	}

/*	*//**
	 * 
	 * @param apiName
	 * @param valuHashMap
	 * @return
 * @throws ServiceException 
	 */
	public Map<String, Object> setResValue(final String apiName, final Map<String, Object> valuHashMap) throws ServiceException {
		final FsApiDetail tempFSAPIBean =  transactionService.getFsapiDetailByApiKey(apiName);
		final Map<String, Object> tempValueMap = valuHashMap;
		final Map<String, Object> valueResMap = new HashMap<>();
		Object tempObj = null;
		if (tempFSAPIBean != null) {
			for (final Entry<String, String> temp : tempFSAPIBean.getResFields().entrySet()) {
				final String key = temp.getKey();
				final String value = temp.getValue();
				if (key.equalsIgnoreCase(ValueObjectKeys.CARDSTATUS)) {	
					String tmp=String.valueOf(tempValueMap.get(key));
					if (tmp !=null && isInteger(tmp)) {
						tempObj =transactionService.getAllCardStatus().get(tmp);
					} else {
						tempObj = tempValueMap.get(key);
					}					
				} else {
					tempObj = tempValueMap.get(key);
		
				if (tempObj instanceof List) {
					List<Map<String, Object>> newTempObj1 = listResParse(tempObj, tempFSAPIBean, valueResMap, value);
					valueResMap.put(key, newTempObj1);
				} else if (tempObj instanceof Map) {
					Map<String, Object> temps = mapResParsing(tempFSAPIBean, valueResMap, value, tempObj);
					valueResMap.put(value, temps);
				} else {
					if (tempObj instanceof Boolean) {
						valueResMap.put(value, tempObj);
					}else if (tempObj instanceof BigDecimal) {
						valueResMap.put(value, tempObj);
					}  
					else {
						valueResMap.put(value, returnBlank(tempObj));
					}
				}

			}
		}
	}
		return valueResMap;
	}

	/**
	 * 
	 * @param tempObj
	 * @param tempFSAPIBean
	 * @param valueResMap
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> listResParse(Object tempObj, FsApiDetail tempFSAPIBean,
			Map<String, Object> valueResMap, String value) {
		List<Map<String, Object>> tempObj1 = (List<Map<String, Object>>) tempObj;
		List<Map<String, Object>> newTempObj1 = new LinkedList<>();
		for (final Map<String, Object> tmpMap : tempObj1) {
			if (tmpMap instanceof List) {
				listResParse(tmpMap, tempFSAPIBean, valueResMap, value);
			} else {
				Map<String, Object> temps = mapResParsing(tempFSAPIBean, valueResMap, value, tmpMap);
				newTempObj1.add(temps);
			}
		}
		return newTempObj1;
	}

	/**
	 * 
	 * @param tempFSAPIBean
	 * @param valueResMap
	 * @param value
	 * @param tempObj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> mapResParsing(final FsApiDetail tempFSAPIBean, final Map<String, Object> valueResMap,
			final String value, final Object tempObj) {
		Map<String, String> valuMap = tempFSAPIBean.getResSubTagFields().get(value);
		Map<String, Object> tempObj1 = (Map<String, Object>) tempObj;
		Map<String, Object> newTempObj1 = new HashMap<>();
		for (Entry<String, String> tempMap : valuMap.entrySet()) {
			Object tmpVal = tempObj1.get(tempMap.getKey());
			if (tmpVal instanceof List) {
				List<Map<String, Object>> tempListOfMap = listResParse(tmpVal, tempFSAPIBean, valueResMap,
						tempMap.getKey());
				newTempObj1.put(tempMap.getValue(), tempListOfMap);
			} else if (tmpVal instanceof Map) {
				Map<String, Object> temps = mapResParsing(tempFSAPIBean, valueResMap, tempMap.getValue(), tmpVal);
				newTempObj1.put(tempMap.getValue(), temps);
			} else {
				if (tmpVal instanceof Boolean) {
					newTempObj1.put(tempMap.getValue(), tmpVal);
				}else if (tmpVal instanceof BigDecimal) {
					newTempObj1.put(tempMap.getValue(), tmpVal);
				}   
				else {
					newTempObj1.put(tempMap.getValue(), returnBlank(tmpVal));
				}
			}
		}
		return newTempObj1;
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public static boolean emptyCheck(String val) {
		boolean flag = false;
		if (val != null && !APIConstants.NULL.equalsIgnoreCase(val.trim())
				&& !APIConstants.EMPTY_STRING.equals(val.trim())) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public static boolean isEmpty(String val) {
		boolean flag = false;
		if (val == null || APIConstants.NULL.equalsIgnoreCase(val.trim())
				|| APIConstants.EMPTY_STRING.equals(val.trim())) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 
	 * @param val
	 * @return
	 */
	public static String returnBlank(Object val) {
		String retVal = APIConstants.EMPTY_STRING;
		String tempVal = String.valueOf(val);
		if (val != null && !APIConstants.NULL.equalsIgnoreCase(tempVal.trim())
				&& !APIConstants.EMPTY_STRING.equals(tempVal.trim())) {
			retVal = tempVal;
		}

		return retVal;
	}
	public static boolean isInteger(String s) {
	    return isInteger(s,10);
	}
	public static boolean isInteger(String s, int radix) {
		if(s==null || s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) {
	            	return false;
	            }else {
	            	continue;
	            }
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) {
	        	return false;
	        }
	    }
	    return true;
	}
	
	@SuppressWarnings("unchecked")
	public void getCardArrayDtls(Map<String, Object> valueMap) {
		try {
			List<Map<String, Object>> tempItemList = (LinkedList<Map<String, Object>>) valueMap.get(FSAPIConstants.ORDER_LINE_ITEMDTLS);
			Map<String, Map<String, String>> resFields = transactionService.getFsapiDetailByApiKey(FSAPIConstants.ORDER_STATUS_API+FSAPIConstants.GET).getResSubTagFields();
			for (final Map<String, Object> temp : tempItemList) {
				List<Map<String, String>> tempCardArry = (List<Map<String, String>>) temp.get(FSAPIConstants.CARDS);
				List<Map<String, String>> tempCardArry1 = new ArrayList<>();
				for (Map<String, String> tempCrd : tempCardArry) {
					Map<String, String> tempMap = new HashMap<>();
					if (resFields != null && resFields.get(FSAPIConstants.CARDS) != null) {
						for (Entry<String, String> resp : resFields.get(FSAPIConstants.CARDS).entrySet()) {
							tempMap.put(resp.getValue(), tempCrd.get(resp.getKey()));
						}
					}
					tempCardArry1.add(tempMap);
		}
				valueMap.put(FSAPIConstants.CARDS,tempCardArry1);
	}
		} catch(Exception exc) {
			logger.error( "Exception occured while closing statement"+exc);
			
		}
		
	}

}
