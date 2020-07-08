package com.incomm.scheduler.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.incomm.scheduler.model.PostBackLogDtl;
import com.incomm.scheduler.service.PostBackService;
import com.incomm.scheduler.utils.JobConstants;

/**
 * This Class is used to process the postBack Job.
 *
 */

@Service
public class PostBackServiceImpl  implements PostBackService {
	
	private static final Logger logger = LogManager.getLogger(PostBackServiceImpl.class);

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	PostBackDAO postBackDAO;
	
	@Autowired
	PostBackImpl postBackImpl;
	
	/**
	 * This method is used to send the postBack. 
	 */

	public String postBackJob() {

		String response = "Failure";

		
			List<Map<String ,Object>> pdDataT= postbackLogDetails();

			for(Map<String ,Object> pdData: pdDataT) {

				try {
					Map<String, Object> valuHashMap = new HashMap<>();
					valuHashMap.put(JobConstants.ORDER_ID, pdData.get(JobConstants.ORDER_ID));
					valuHashMap.put(JobConstants.POST_BACK_URL, pdData.get(JobConstants.POST_BACK_URL));
					valuHashMap.put(JobConstants.API_NAME, String.valueOf(pdData.get(JobConstants.API_NAME)));
					valuHashMap.put(JobConstants.SEQ_NO, String.valueOf(pdData.get(JobConstants.SEQ_NO)));
					
					Map<String, Object> reqMap = new HashMap<>();
					reqMap.put(JobConstants.REQ_MESSAGE, pdData.get(JobConstants.REQ_MESSAGE));
					
					
					Map<String, String> headerMap = new HashMap<>();
					headerMap.put(JobConstants.REQ_HEADER, String.valueOf(pdData.get(JobConstants.REQ_HEADER)));
					logger.info("PostBack Process for "+valuHashMap.get(JobConstants.API_NAME));
					response = postBackImpl.sendResponse(headerMap,reqMap,valuHashMap);

			}


		 catch (Exception e) {
			response=e.getCause().getMessage();
			logger.error("Exception in fetchParametersAndInvoke process",e);
		}

	}

		return response;
	}

	
	/**
	 * (non-Javadoc)
	 * @see com.incomm.scheduler.service.PostBackService#postbackLogDetails()
	 */

	
	public List<Map<String,Object>> postbackLogDetails()  {
		
		List<Map<String,Object>> valueObjList = new ArrayList<>();
		try{
			List<PostBackLogDtl> postBackLogList=postBackDAO.getPostBackReqTableDetails();
			
			postBackLogList.forEach(pb->{
				 Map<String,Object> valueObj=new HashMap<>();
				valueObj.put(JobConstants.ORDER_ID, pb.getOrderId());
				valueObj.put(JobConstants.API_NAME, pb.getApiName());
				valueObj.put(JobConstants.SEQ_NO, pb.getSeqNo());
				valueObj.put(JobConstants.REQ_MESSAGE, pb.getReqMsg());
				valueObj.put(JobConstants.REQ_HEADER, pb.getReqHeader());
				valueObj.put(JobConstants.RES_HEADER, pb.getResHeader());
				valueObj.put(JobConstants.RES_CODE, pb.getResponseCode());
				valueObj.put(JobConstants.RES_MSG, pb.getResponseMsg());
				valueObj.put(JobConstants.RES_COUNT, pb.getResponseCount());
				valueObj.put(JobConstants.RES_FLAG, pb.getResponseFlag());
				valueObj.put(JobConstants.POST_BACK_STATUS, pb.getPostBackStatus());
				valueObj.put(JobConstants.POST_BACK_URL, pb.getPostBackUrl());
				valueObjList.add(valueObj);
			});
				}

	catch(Exception e){
		
		logger.error("PostBackLog details  fetching Exception",e);
		
			
		}

		return valueObjList;
	}
	
	
}




