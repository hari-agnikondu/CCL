package com.incomm.cclp.fsapi.service;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import com.incomm.cclp.exception.ServiceException;

public interface B2BService {

	ResponseEntity<Object> orderStatusProcess(Map<String, Object> valueObj);

	ResponseEntity<Object> orderProcess(Map<String, String> reqHeaders, Map<String, Object> reqValueObj)
			throws ServiceException;
	
	public ResponseEntity<Object> orderActivation(Map<String, Object> valueObj,Map<String, String> reqHeaders) throws ServiceException  ;
	
	public ResponseEntity<Object> serialNumberRangeActivation(Map<String,Object> valueObj,Map<String, String> reqHeaders) throws ServiceException ;
	public ResponseEntity<Object> reloadProcess(Map<String,Object> valuHashMap,Map<String, String> reqHeaders) throws ServiceException ;
}
