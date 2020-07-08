package com.incomm.cclp.fsapi.service.impl;

import java.util.Date;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.util.Util;
/**
 * 
 * @author sampathkumarl
 *
 */

@Service
public class PostBackProcessImpl extends JdbcDaoSupport{
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }
	

	@Autowired
	public JdbcTemplate jdbctemplate;
	

	@Autowired
	RestTemplate restTemplate;
	
	@Value("${APIKEY}") 
	String APIKEY;
	
	

	private static final Logger logger = LogManager.getLogger(PostBackProcessImpl.class);

	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Async
	public void sendResponse(String apiName,String orderId, String reqHeaders, String reqStr, String respCode, String respMsg, String respStr, String serverID, long timeTaken, String postBackURL) {
		
		String response[] =  new String[2];
		response[0]="success";
		
		String headerForTable =null;
		Map<String,String> finalReqHeader =null;
		int insertCount = 0;
		try {
			
			
			finalReqHeader = Util.jsonToMapCon(reqHeaders);
			
			logger.info("Post Back invoked with above headerFromReq ,order");
			logger.info("URL"+postBackURL);			
			HttpHeaders headers = new HttpHeaders();
	        headers.setContentType( MediaType.APPLICATION_JSON );
	        headers.set("x-incfs-date", new Date()+"");
	        headers.set("x-incfs-ip", serverID);
	        headers.set("x-incfs-channel", finalReqHeader.get("x-incfs-channel"));
	        headers.set("x-incfs-channel-identifier", finalReqHeader.get("x-incfs-channel-identifier"));
	        
	        if(!"ORDER".equalsIgnoreCase(apiName)) {
	        	headers.set("x-incfs-username", Util.isEmpty(finalReqHeader.get("x-incfs-username"))? "Test":finalReqHeader.get("x-incfs-username"));
		        headers.set("apikey", Util.isEmpty(finalReqHeader.get("apikey"))? APIKEY:finalReqHeader.get("apikey"));
	        }
	        headers.set("x-incfs-correlationid", finalReqHeader.get("x-incfs-correlationid"));
	     
	        headerForTable = headers.toString();
	        
	        
	        
	        logger.info("Loggerr for hearder is --"+headers);	
	        HttpEntity request= new HttpEntity( reqStr, headers );
	  
	        String responseFromURL = restTemplate.postForObject( postBackURL, request, String.class );
	        
	        response[0]= responseFromURL;
			Map<String,String> rsMap=Util.jsonToMapCon(responseFromURL);
			
			
			
	        logger.info("Post Back Sent,Return responseFromURL "+responseFromURL);
	        
	        
	        if("success".equalsIgnoreCase(rsMap.get("responseCode"))){
				
	        	response[1]="Y";
	        	
	        	insertCount = getJdbcTemplate().update(QueryConstants.FSAPI_POSTBACK_LOG,  apiName, orderId, headerForTable,
						 reqStr, respCode,  response[0],  respStr,  serverID,  timeTaken,response[1], postBackURL);
	        	
	        	logger.info("------responseLogger---" + insertCount);
	        	
			}else{
				response[1]="N";
				insertCount = getJdbcTemplate().update(QueryConstants.FSAPI_POSTBACK_LOG,  apiName, orderId, headerForTable,
						 reqStr, respCode,  response[0],  respStr,  serverID,  timeTaken,response[1], postBackURL);
				
				logger.info("------responseLogger----" + insertCount);
			}
			
	        
	    
		} catch (RestClientException e) {
			logger.error("RestClientException Occured at Exception :", e);
			response[1]="N";
			response[0]=e.getMessage();
			insertCount = getJdbcTemplate().update(QueryConstants.FSAPI_POSTBACK_LOG,  apiName, orderId, headerForTable,
					 reqStr, respCode,  response[0],  respStr,  serverID,  timeTaken,response[1], postBackURL);
			logger.info("-----responseLogger-----" + insertCount);
			
		}catch (Exception ex) {
			
			logger.error("Exception Occured at Exception :", ex);
			response[1]="N";
			response[0]=ex.getMessage();
			insertCount = getJdbcTemplate().update(QueryConstants.FSAPI_POSTBACK_LOG,  apiName, orderId, headerForTable,
					 reqStr, respCode,  response[0],  respStr,  serverID,  timeTaken,response[1], postBackURL);
			logger.info("-----responseLogger----" + insertCount);
			}
	
		}

	

}
