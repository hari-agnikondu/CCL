package com.incomm.cclp.fsapi.dao;

import java.util.Map;

	
public interface SerialNumberRangeActivationDAO {
	Map<String, Object> serialNumberRangeActivation(Map<String, Object> valuMap) ;
	
	void updateCardInfo(Map<String, Object> valuHashMap);
	
	void logAPIRequestDtls(Map<String, Object> valuHashMap,String respMsg);

}
