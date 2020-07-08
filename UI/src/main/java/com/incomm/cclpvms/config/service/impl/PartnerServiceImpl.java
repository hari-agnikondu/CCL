package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.CurrencyCodeDTO;
import com.incomm.cclpvms.config.model.Partner;
import com.incomm.cclpvms.config.model.PartnerDTO;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.PartnerService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;

@Service
public class PartnerServiceImpl implements PartnerService {

	// the logger
	private static final Logger logger = LogManager.getLogger(PartnerServiceImpl.class);
	@Autowired
	private RestTemplate restTemplate ;
	@Value("${CONFIG_BASE_URL}") String CONFIG_BASE_URL;
	
	
	public ResponseDTO createPartner(Partner partner) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
			logger.debug("Calling '{}' service to create new addGroupAccess",CONFIG_BASE_URL);
			ModelMapper mm = new ModelMapper();
			PartnerDTO partnerDTO = mm.map(partner, PartnerDTO.class);	
			partnerDTO.setCurrencyList(partner.getSupportedCurrencyUpdate());
			partnerDTO.setPurseList(partner.getSupportedPurseUpdate());
			
			logger.debug("Calling '{}' service to create new partner",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(CONFIG_BASE_URL + "/partners", partnerDTO,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Error while creating new partner, {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@SuppressWarnings("unchecked")
	public List<Partner> getAllPartners() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<Partner> partnerList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			logger.debug("Calling '{}' service to search all partners",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(CONFIG_BASE_URL + "/partners",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				logger.error("Failed to Fetch partner list from config srvice");
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : ResourceBundleUtil.getMessage(ResponseMessages.ERR_PARTNER_FETCH));
			}
			List<Object> partnerDtos = (List<Object>) responseBody.getData();
			if (partnerDtos != null) {
				Iterator<Object> itr = partnerDtos.iterator();
				while (itr.hasNext()) {
					partnerList.add(objectMapper.convertValue(itr.next(), Partner.class));
				} 
			}
		} catch (RestClientException e) {
			logger.error("Exception while fetching records {}",e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return partnerList;
	}

	public ResponseDTO updatePartner(Partner partner) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
			ModelMapper mm = new ModelMapper();
			PartnerDTO partnerDTO = mm.map(partner, PartnerDTO.class);	
			partnerDTO.setCurrencyList(partner.getSupportedCurrencyUpdate());
			partnerDTO.setPurseList(partner.getSupportedPurseUpdate());
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<PartnerDTO> requestEntity = new HttpEntity<>(partnerDTO, headers);
			logger.debug("Calling '{}' service to update partner",CONFIG_BASE_URL);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + "/partners", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating partner {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	public ResponseDTO deletePartner(long partnerId) {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		logger.debug("Calling '{}' service to delete partner '{}'",CONFIG_BASE_URL,partnerId);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(CONFIG_BASE_URL + "/partners/{partnerId}",
				HttpMethod.DELETE, null, ResponseDTO.class, partnerId);
		responseBody = responseEntity.getBody();
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	public List<Partner> getPartnersByName(String partnerName) {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		String tempurl="";
		ObjectMapper objectMapper = new ObjectMapper();
		List<Partner> partnerList = new ArrayList<>();
		logger.debug("Calling '{}' service for Search Partner '{}'",CONFIG_BASE_URL,partnerName);
		 tempurl = CONFIG_BASE_URL + "partners/search";
		  
		  UriComponentsBuilder builder = UriComponentsBuilder
		    .fromUriString(tempurl)
		        .queryParam("partnerName", partnerName);

		  ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
			      ResponseDTO.class);
		
		responseBody = responseEntity.getBody();
		@SuppressWarnings("unchecked")
		List<Object> partnerDtos = (List<Object>) responseBody.getData();
		if (partnerDtos != null) {
			Iterator<Object> itr = partnerDtos.iterator();
			while (itr.hasNext()) {
				partnerList.add(objectMapper.convertValue(itr.next(), Partner.class));
			} 
		}
		logger.debug(CCLPConstants.EXIT);
		return partnerList;
	}

	@SuppressWarnings("unchecked")
	public Partner getPartnerByPartnerId(long partnerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		Map<String, Object> partnerMap = null;
		logger.debug("Calling '{}' service for get Partner '{}'", CONFIG_BASE_URL, partnerId);
		ResponseEntity<ResponseDTO> responseEntity = restTemplate
				.getForEntity(CONFIG_BASE_URL + "/partners/{partnerId}", ResponseDTO.class, partnerId);
		ResponseDTO responseDTO = responseEntity.getBody();

		Partner partnerBean = null;
		partnerMap = (Map<String, Object>) responseDTO.getData();

		long id = (int) partnerMap.get("partnerId");
		String partnerName = (String) partnerMap.get("partnerName");
		String partnerDesc = (String) partnerMap.get("partnerDesc");
		String mdmId = (String) partnerMap.get("mdmId");
		String isActive = (String) partnerMap.get("isActive");

		long insUser = (int) partnerMap.get("insUser");
		long lastUpdUser = (int) partnerMap.get("lastUpdUser");

		List<String> currencyList = (List<String>) partnerMap.get("partnerCurrencyList");
		List<Object> currencyListObj = (List<Object>) partnerMap.get("partnerCurrencyList");
		
		List<String> purseList = (List<String>) partnerMap.get("partnerPurseList");
		List<Object> purseListObj = (List<Object>) partnerMap.get("partnerPurseList");
		
		partnerBean = new Partner(id, partnerName, partnerDesc, mdmId, isActive, insUser, lastUpdUser, currencyList,
				currencyListObj,purseList,purseListObj);

		logger.info("Partner information " + responseEntity.getBody());
		logger.debug(CCLPConstants.EXIT);
		return partnerBean;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CurrencyCodeDTO> getCurrencyCodes() throws ServiceException {

		List<CurrencyCodeDTO> currencyCodeDto = new ArrayList<>();
		ResponseDTO responseBody = null;

		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(CONFIG_BASE_URL + "/master" + "/currencyCode", ResponseDTO.class);
			if (responseEntity != null && responseEntity.getBody() != null) {
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				responseBody = responseEntity.getBody();
				logger.debug("reponse from Rest Call : " + responseBody.getResult());
				if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
					throw new ServiceException(responseBody.getResult());
				}
				currencyCodeDto = (List<CurrencyCodeDTO>) responseBody.getData();

			}

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getCurrencyCodeList()" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return currencyCodeDto;

	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PurseDTO> getPurses() throws ServiceException {
		logger.info("ENTER getPurses()>>>");
		List<PurseDTO> purseDTO = new ArrayList<>();
		ResponseDTO responseBody = null;

		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(CONFIG_BASE_URL + "/master" + "/purses", ResponseDTO.class);
			logger.info("ResponseEntity>>>"+responseEntity.getBody().getCode());
			if (responseEntity != null && responseEntity.getBody() != null) {
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				responseBody = responseEntity.getBody();
				logger.debug("reponse from Rest Call for purseList: " + responseBody.getResult());
				if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
					throw new ServiceException(responseBody.getResult());
				}
				purseDTO = (List<PurseDTO>) responseBody.getData();

			}

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getPursesList()" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.info("END getPurses()<<<");
		return purseDTO;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<PurseDTO> getProgramIDPartnerPurses(long programId) throws ServiceException {
		logger.info("ENTER getProgramIDPartnerPurses()>>>"+programId);
		List<PurseDTO> purseDTO = new ArrayList<>();
		ResponseDTO responseBody = null;
		
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(CONFIG_BASE_URL + "/programid" + "/getAllSupportedPursesByProgramId/{programId}", ResponseDTO.class, programId);
			logger.info("ResponseEntity>>>"+responseEntity.getBody().getCode());
			if (responseEntity != null && responseEntity.getBody() != null) {
				if (!ResponseMessages.SUCCESS.equals(responseEntity.getBody().getCode())) {
					throw new ServiceException(responseEntity.getBody().getResult());
				}
				responseBody = responseEntity.getBody();
				logger.debug("reponse from Rest Call for purseList: " + responseBody.getResult());
				if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
					throw new ServiceException(responseBody.getResult());
				}
				purseDTO = (List<PurseDTO>) responseBody.getData();

			}

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getPursesList()" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}catch (Exception e) {
			logger.error("Error Occured during making a Rest Call in getPursesList()" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.info("END getProgramIDPartnerPurses<<<");
		return purseDTO;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PurseDTO> getAllSupportedPurses(Long partnerId) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		List<PurseDTO>	purseDTOs= new ArrayList<>();
		
		try {
			String pursesByPartnerIdurl="/getAllSupportedPursesByPartnerId/";
			logger.debug("Calling '{}' service to search supported purses '{}'",CONFIG_BASE_URL,partnerId);
			
			ResponseDTO responseDTO=restTemplate.getForObject(CONFIG_BASE_URL +"partners"+pursesByPartnerIdurl+partnerId, ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){
				purseDTOs = (List<PurseDTO>) responseDTO.getData();
				
			}

		} 
		catch (RestClientException e) {
			logger.error("RestClientException in get all supported purses:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return purseDTOs;
	}

}
