package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclpvms.config.model.RedemptionDelayDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.RedemptionService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.exception.ServiceException;


@Service
public class RedemptionDelayServiceImpl implements RedemptionService{
	
	private static final Logger logger = LogManager.getLogger(IssuerServiceImpl.class);

	private static RestTemplate restTemplate = new RestTemplate();
	
	@Value("${CONFIG_BASE_URL}") String configBaseUrl;

	@Override
	public ResponseDTO createRedemption(RedemptionDelayDTO redemptionDelaydto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl + "redemptiondelay", redemptionDelaydto,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of Redemeptiondelay " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createRedemeptiondelay()"+e);
			throw new ServiceException(CCLPConstants.ERR_MSG);
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public String getOverlapDelayDetails(String previousValue, String currentValue) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String message="";
		try {
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + "redemptiondelay/"+"overlapDelays/"+"/"+previousValue+ "/"+currentValue,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of Redemeptiondelay " + httpStatus);
			message = (String) responseEntity.getBody().getData();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createRedemeptiondelay()"+e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return message;
	}

	@Override
	public List<RedemptionDelayDTO> getRedemptionDelayByMerchantIdProductId(Long productId, String merchantId) throws ServiceException{
		logger.debug(CCLPConstants.ENTER);
		String url = "";
		ResponseDTO responseBody = null;
		List<RedemptionDelayDTO> redeemList = new ArrayList<>();
		
		try {
			url = "redemptiondelay/getAllRedemptionProductMerchants/";
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + url+productId+"/"+merchantId,
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all Merchants");
			}
			List<RedemptionDelayDTO> redemptiondtos = (List<RedemptionDelayDTO>) responseBody.getData();
			redeemList.addAll(redemptiondtos);
			
		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllMerchants()"+e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return redeemList;
	}

}
