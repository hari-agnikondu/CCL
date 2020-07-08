package com.incomm.scheduler.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.scheduler.constants.CCLPConstants;

@Service
public class AsyncServiceCall {

	
private static final Logger logger = LogManager.getLogger(AsyncServiceCall.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Value("${MFT_BASE_URL}") 
	private String mfturl;
	
	public void processOutboundFile(String vendorAndFile){
		logger.info(CCLPConstants.ENTER);
		String response = "";
		HttpHeaders headers = new HttpHeaders();
		try {
			HttpEntity request = new HttpEntity(vendorAndFile, headers);
			logger.debug("calling mft service to process the file, mfturl: {}, vendorandfile: {}", mfturl,
					vendorAndFile);
			response = restTemplate.postForObject(mfturl + "/MFT/transfer/outbound", request, String.class);
		} catch (RestClientException re) {
			logger.error("Exception while calling mft service: {}", re.getMessage());
		}
		if(response.equalsIgnoreCase("success")) {
			logger.info("processing outbound success");
		}else {
			logger.error("Error while processing outbound "+response);
		}
		logger.info(CCLPConstants.EXIT);
	}
}
