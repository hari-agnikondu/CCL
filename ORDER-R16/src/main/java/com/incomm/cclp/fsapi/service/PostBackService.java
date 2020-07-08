package com.incomm.cclp.fsapi.service;

import java.util.Map;

import org.springframework.http.ResponseEntity;


public interface PostBackService {
	public void callPostBackProcess(Map<String, Object> valuHashMap,final String apiName, long timeTaken, String reqHeaders);
	public void cancelOrderProcess(final Map<String, Object> valuHashMap);
	public  void reloadPostBack(Map<String, Object> valuHashMap);
	
	public void cardReplaceRenewal(Map<String, Object> valProcessMap, Map<String, String> reqHeaders);
	
	public  void loggingCardStatusInquiry(Map<String,Object> valueObject);
	
	public ResponseEntity<Object> postBackStatus(String payLoad,Map<String, String> reqHeaders);
	
	public void loadPostBackUrl(String pstbkOrderActStatus,String postBackPropKey);
	
}
