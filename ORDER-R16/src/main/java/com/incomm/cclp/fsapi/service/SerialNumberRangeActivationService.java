package com.incomm.cclp.fsapi.service;

import java.util.Map;

public interface SerialNumberRangeActivationService {
	
	Map<String, Object> serialNumberRangeActivation(Map<String, Object> valuMap);
	
	void updateCardInfo(Map<String,Object> tempValuHashMap);
	
	void logAPIRequestDtls(Map<String,Object> tempValuHashMap,String respMsg);

}
