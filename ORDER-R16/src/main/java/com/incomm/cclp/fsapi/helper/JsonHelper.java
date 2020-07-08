package com.incomm.cclp.fsapi.helper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;

/**
 * 
 *
 */
public class JsonHelper {
	/**
	 * 
	 * @param jsonStr
	 * @return
	 * @throws ServiceException 
	 */
	private JsonHelper() {}
	
	public static JSONObject isJSONValid(String jsonStr) throws ServiceException {
		JSONObject temp = null;
		if (jsonStr != null && jsonStr != "") {
			try {
				temp = new JSONObject(jsonStr);
			} catch (Exception ex) {
				throw new ServiceException(B2BResponseMessage.ERR_JSON_VALUE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			}
		}
		return temp;
	}

	/**
	 * 
	 * @param object
	 * @return
	 * @throws ServiceException 
	 */
	public static Map<String, Object> jsonToMap(JSONObject object) throws ServiceException {
		Map<String, Object> map = new HashMap<>();

		@SuppressWarnings("unchecked")
		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value;
			try {
				value = object.get(key);
			} catch (JSONException e) {
				throw new ServiceException(B2BResponseMessage.ERR_JSON_VALUE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			}

			if (value instanceof JSONArray) {
				value = jsonToList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				value = jsonToMap((JSONObject) value);
			}
			map.put(key, value);
		}
		return map;
	}

	/**
	 * 
	 * @param array
	 * @return
	 * @throws ServiceException 
	 */
	public static List<Object> jsonToList(JSONArray array) throws ServiceException {
		List<Object> list = new LinkedList<>();
		for (int i = 0; i < array.length(); i++) {
			Object value;
			try {
				value = array.get(i);
			} catch (JSONException e) {
				throw new ServiceException(B2BResponseMessage.ERR_JSON_VALUE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			}
			if (value instanceof JSONArray) {
				value = jsonToList((JSONArray) value);
			} else if (value instanceof JSONObject) {
				try {
					value = jsonToMap((JSONObject) value);
				} catch (ServiceException e) {
					throw new ServiceException(B2BResponseMessage.ERR_JSON_VALUE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				}
			}
			list.add(value);
		}
		return list;
	}

	/**
	 * 
	 * @param map
	 * @return
	 * @throws ServiceException 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject getJsonFromMap(final Map<String, Object> map) throws ServiceException {
		JSONObject jsonData = new JSONObject();
		for (Map.Entry<String,Object> entry : map.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();
			if (value instanceof Map<?, ?>) {
				value = getJsonFromMap((Map<String, Object>) value);
			} else if (value instanceof List<?>) {
				value = getJsonFromList((List<Object>) value);
			}
			if (key != null) {
				if ("challengeInfo".equalsIgnoreCase(key)) {
					try {
						JSONObject mainObject = new JSONObject(String.valueOf(value));
						jsonData.put(key, mainObject);
					} catch (Exception e) {
						try {
							jsonData.put(key, JSONObject.NULL);
						} catch (JSONException e1) {
							throw new ServiceException(B2BResponseMessage.ERR_JSON_VALUE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
						}
					}
				} else {
					try {
						jsonData.put(key, "null".equals(value) ? JSONObject.NULL : null != value ? value : JSONObject.NULL);
					} catch (JSONException e) {
						throw new ServiceException(B2BResponseMessage.ERR_JSON_VALUE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
					}
				}
			}
		}
		return jsonData;
	}

	@SuppressWarnings("unchecked")
	private static JSONArray getJsonFromList(List<Object> value) throws ServiceException {
		JSONArray tempJsonArr = new JSONArray();
		for (Object tempObj : value) {
			if (tempObj instanceof Map<?, ?>) {
				try {
					tempJsonArr.put(getJsonFromMap((Map<String, Object>) tempObj));
				} catch (ServiceException e) {
					throw new ServiceException(B2BResponseMessage.ERR_JSON_VALUE,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				}
			}
		}
		return tempJsonArr;
	}
}
