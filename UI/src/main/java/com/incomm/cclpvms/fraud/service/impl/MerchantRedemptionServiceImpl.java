package com.incomm.cclpvms.fraud.service.impl;

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

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.impl.MerchantServiceImpl;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.exception.MerchantException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.fraud.model.MerchantRedemptionDTO;
import com.incomm.cclpvms.fraud.service.MerchantRedemptionService;

@Service
public class MerchantRedemptionServiceImpl implements MerchantRedemptionService{
	private static final Logger logger = LogManager.getLogger(MerchantServiceImpl.class);
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	@Value("${CONFIG_BASE_URL}") String configBaseUrl;

	@Override
	public ResponseDTO createRedemptionMerchant(MerchantRedemptionDTO merchantRedemptionDTO) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl +"redemptiondelay/"+ "merchantRedemptioDelay", merchantRedemptionDTO,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of createMerchant " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createMerchanRedemptiont()"+e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public List<MerchantRedemptionDTO> getAllRedemptionMerchants() throws ServiceException, MerchantException {
		logger.debug(CCLPConstants.ENTER);
		String url = "";
		ResponseDTO responseBody = null;
		List<MerchantRedemptionDTO> merchantRedemptionList = new ArrayList<>();
		
		try {
			url = "redemptiondelay/getAllMerchants/";
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + url,
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all Redemption Merchants");
			}
			List<MerchantRedemptionDTO> redemptionMerchantDtos = (List<MerchantRedemptionDTO>) responseBody.getData();

			merchantRedemptionList.addAll(redemptionMerchantDtos);
			
		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllMerchants()"+e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return merchantRedemptionList;
	}

}
