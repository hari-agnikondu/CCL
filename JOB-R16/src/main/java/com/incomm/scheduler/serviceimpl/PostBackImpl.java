package com.incomm.scheduler.serviceimpl;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.scheduler.dao.PostBackDAO;
import com.incomm.scheduler.utils.JobConstants;
/**
 * 
 * @author sampathkumarl
 *
 */
@Service
public class PostBackImpl {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	PostBackDAO postBackDAO;
	
	private static final Logger logger = LogManager.getLogger(PostBackImpl.class);
	
	/**
	 * 
	 * @param headerFromReq
	 * @param orderMap
	 * @param valuHashMap
	 * @return
	 */
	@Async
	public String sendResponse(Map<String,String> headerFromReq,Map<String,Object> reqMap, Map<String, Object> valuHashMap ) {
		
		String response = "success";
		try {
			
			logger.info("Post Back invoked with above headerFromReq ,order");
			
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			
			for(Entry<String, String> map:headerFromReq.entrySet()) {
				headers.add(map.getKey(), map.getValue());
			}
			
			
			
			headers.add(JobConstants.API_NAME, String.valueOf(valuHashMap.get(JobConstants.API_NAME)));
			
			String url = valuHashMap.get(JobConstants.POST_BACK_URL).toString();
			logger.info("Post Back Sent url"+url);
			
			restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
			
			ResponseEntity<String> responseEntity = restTemplate.exchange(url,HttpMethod.POST,
					new HttpEntity<Map<String,Object>>(reqMap, headers), String.class); 
			
			logger.info("Post Back Sent,Return response as responseEntity "+responseEntity);
			
			JSONObject jsonRes = new JSONObject(responseEntity.getBody());
			
				valuHashMap.put(JobConstants.POST_BACK_RESP_MSG, jsonRes.length()>500?jsonRes.toString().substring(0, 500):jsonRes.toString());

			
			
			logger.info("Post Back Sent,Return response as jsonRes "+jsonRes);
			
			if("success".equals(jsonRes.get("responseCode"))){
			
				valuHashMap.put(JobConstants.POSTBACK_STATUS, "Y");
			}else{
				valuHashMap.put(JobConstants.POSTBACK_STATUS, "N");
			}
			postBackDAO.updatePostBackStatus(valuHashMap);
			
			
		} catch (RestClientException e) {
			logger.error("RestClientException Occured at Exception :", e);
			valuHashMap.put(JobConstants.POSTBACK_STATUS, "N");
			postBackDAO.updatePostBackStatus(valuHashMap);
			response="Fail";
		}catch (Exception ex) {
			valuHashMap.put(JobConstants.POSTBACK_STATUS, "N");
			postBackDAO.updatePostBackStatus(valuHashMap);
			logger.error("Exception Occured at Exception :", ex);
			response="Fail";
			}
		
		return response;
		
		}



}
