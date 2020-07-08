package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.FulFillment;
import com.incomm.cclpvms.config.model.FulfillmentDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.FulFillmentService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class FulFillmentServiceImpl implements FulFillmentService
{

	private static final String FUL_URL = "/fulfillments";
	private static final String MASTER_BASE_URL = "/master";

	@Autowired
	private RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}")
	String CONFIG_BASE_URL;
	
	@Value("${INS_USER}")
	long userId;
	
	private static final Logger logger = LogManager.getLogger(FulFillmentServiceImpl.class);
	
	
	public ResponseDTO createFulfillment(FulFillment fulFillment) throws ServiceException {
		ResponseDTO responseBody = null;
		ModelMapper mm = new ModelMapper();
		logger.debug(CCLPConstants.ENTER);
		try {
			FulfillmentDTO fulfillmentDTO = mm.map(fulFillment, FulfillmentDTO.class);
			if(fulfillmentDTO.getIsAutomaticShipped().equals("0"))
			{
				fulfillmentDTO.setShippedTimeDealy(0);
			}
			fulfillmentDTO.setInsUser(userId);
			fulfillmentDTO.setLastUpdUser(userId);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(CONFIG_BASE_URL+FUL_URL,fulfillmentDTO, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException ex) {
			logger.error("Error while creating new FulFillment, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO updateFulfillment(FulFillment fulFillment) throws ServiceException {
		ResponseDTO responseBody = null;
		ModelMapper mm = new ModelMapper();
		try {
			logger.debug(CCLPConstants.ENTER);
			FulfillmentDTO fulfillmentDTO = mm.map(fulFillment, FulfillmentDTO.class);
			if (fulfillmentDTO.getIsAutomaticShipped().equals("0")) {
				fulfillmentDTO.setShippedTimeDealy(0);
			}
			fulfillmentDTO.setInsUser(userId);
			fulfillmentDTO.setLastUpdUser(userId);

			logger.debug("Calling '{}' service to update FulFillment", CONFIG_BASE_URL);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + FUL_URL,
					HttpMethod.PUT, new HttpEntity<FulfillmentDTO>(fulfillmentDTO), ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException ex) {
			logger.error("Exception while updating FulFillment {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO deleteFulfillment(long fulFillmentSEQID,String fulfillmentID) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			FulfillmentDTO fulfillmentDTO = new FulfillmentDTO();
			fulfillmentDTO.setFulFillmentSEQID(fulFillmentSEQID);
			fulfillmentDTO.setFulfillmentID(fulfillmentID);
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					CONFIG_BASE_URL +FUL_URL, HttpMethod.DELETE, new HttpEntity<FulfillmentDTO>(fulfillmentDTO), ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException ex) {
			logger.error("Exception while updating FulFillment {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	public List<FulfillmentDTO> getAllFulFillment() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<FulfillmentDTO> fulFillmentList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + FUL_URL,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				logger.debug("Failed to Fetch FulFillment list from config service");
				throw new ServiceException(responseBody.getMessage() != null ? responseBody.getMessage()
						: ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
			}

			@SuppressWarnings("unchecked")
			List<Object> fullFillmentDtos = (List<Object>) responseBody.getData();
			convertDTOList(fulFillmentList, objectMapper, fullFillmentDtos);
		} catch (RestClientException e) {
			logger.debug("Exception while fetching records {}", e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return fulFillmentList;
	}

	public List<FulfillmentDTO> getAllFulFillmentByName(String fulFillmentName) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<FulfillmentDTO> fulFillmentList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			
			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(CONFIG_BASE_URL + FUL_URL + "/search")
					.queryParam("fulFillmentName", fulFillmentName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				logger.debug("Failed to Fetch FulFillment list from config srvice");
				throw new ServiceException(responseBody.getMessage() != null ? responseBody.getMessage()
						: ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
			}

			@SuppressWarnings("unchecked")
			List<Object> fullFillmentDtos = (List<Object>) responseBody.getData();

			convertDTOList(fulFillmentList, objectMapper, fullFillmentDtos);

		} catch (RestClientException e) {
			logger.debug("Exception while fetching records {}", e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return fulFillmentList;
	}

	private void convertDTOList(List<FulfillmentDTO> fulFillmentList, ObjectMapper objectMapper,
			List<Object> fullFillmentDtos) {
		fullFillmentDtos.forEach(fullFillmentDto -> fulFillmentList
				.add(objectMapper.convertValue(fullFillmentDto, FulfillmentDTO.class)));
	}

	@Override
	public FulFillment getFulfillmentById(long fulFillmentSEQID) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		FulFillment fulFillment = null;
		ModelMapper mm = new ModelMapper();
		logger.debug("Calling '{}' service for get FulFillment By ID And Name '{}'", CONFIG_BASE_URL, fulFillmentSEQID);
		ResponseDTO responseDTO = restTemplate.getForObject(CONFIG_BASE_URL + FUL_URL + "/" + fulFillmentSEQID,
				ResponseDTO.class);
		if (responseDTO != null && responseDTO.getData() != null) {
			FulfillmentDTO fulfillmentDTO = mm.map(responseDTO.getData(), FulfillmentDTO.class);
			logger.debug("DTO Object:", CONFIG_BASE_URL, fulfillmentDTO.getB2bCnFileIdentifier());
			fulFillment = mm.map(fulfillmentDTO, FulFillment.class);
			logger.debug("BEAN Object 22222222:", CONFIG_BASE_URL, fulFillment.getB2bCnFileIdentifier());
	}
		logger.debug(CCLPConstants.EXIT);
		return fulFillment;
	}

	@Override
	public Map<String, String> getShipmentAttList() throws ServiceException {
		ResponseDTO responseBody = null;
		Map<String,String> shipmentMapList = null;
		try {
			logger.debug(CCLPConstants.ENTER);
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + 
					MASTER_BASE_URL+"/fulFillmentList",ResponseDTO.class);
			
			responseBody = responseEntity.getBody();
			@SuppressWarnings("unchecked")
			List<Object> shipmentList =  (List<Object>) responseBody.getData();
			shipmentMapList = shipmentList 
					.stream()
					.map(s->s.toString().replaceAll("[\\]\\[]", "").trim().split(","))
					.collect(Collectors.toMap(a -> a[0],a->a[1]));
			} catch (RestClientException ex) {
			logger.error("Error while creating new FulFillment, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return shipmentMapList;
	}

}
