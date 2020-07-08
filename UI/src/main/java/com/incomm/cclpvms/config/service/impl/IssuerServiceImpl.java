package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.Issuer;
import com.incomm.cclpvms.config.model.IssuerDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.IssuerService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.exception.ServiceException;

@Service
public class IssuerServiceImpl implements IssuerService {

	private static final Logger logger = LogManager.getLogger(IssuerServiceImpl.class);

	private static RestTemplate restTemplate = new RestTemplate();
	
	@Value("${CONFIG_BASE_URL}") String configBaseUrl;
	
	private static final String ISSUER_BASED_URL = "issuers/";

	public ResponseDTO createIssuer(IssuerDTO issuerdto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl + "issuers", issuerdto,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of CreateIssuer " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createIssuer()"+e);
			throw new ServiceException("Failed to process,Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;

	}

	public List<IssuerDTO> getAllIssuers() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String url = "";
		ResponseDTO responseBody = null;
		List<IssuerDTO> issList = new ArrayList<>();
		
		try {
			url = ISSUER_BASED_URL;
			ObjectMapper objectMapper = new ObjectMapper();
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + url,
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call is: " , responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all Issuers");
			}
			List<Object> issuerDtos = (List<Object>) responseBody.getData();
			Iterator<Object> itr = issuerDtos.iterator();

			while (itr.hasNext()) {
				issList.add(objectMapper.convertValue(itr.next(), IssuerDTO.class));
			}
		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllIssuers()"+e);
			throw new ServiceException("Failed to process Please try again later");
		}

		logger.debug(CCLPConstants.EXIT);
		return issList;
	}

	public ResponseDTO updateIssuer(IssuerDTO issuerdto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<IssuerDTO> requestEntity = new HttpEntity<>(issuerdto, headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + "issuers", HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in updateIssuer()" + e);
			throw new ServiceException("Failed to process, Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	public ResponseDTO deleteIssuer(long issuerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		HttpHeaders headers = new HttpHeaders();
		String url = "";
		try {
			
			url = ISSUER_BASED_URL + issuerId;
			HttpEntity<IssuerDTO> requestEntity = new HttpEntity(null, headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + url, HttpMethod.DELETE,
					requestEntity, ResponseDTO.class, (Object) issuerId);
			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in deleteIssuer()" + e);
			throw new ServiceException("Failed to process, Please try again later.");
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	public Issuer getIssuerById(long issuerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String tempurl = "";
		tempurl = ISSUER_BASED_URL + issuerId;
		String iDescription = "";
		String iMdmId = "";
		String idSearchUrl = tempurl;
		ResponseDTO responseBody = null;
		Issuer issBean = null;
		String iName = null;
		String iCode = null;
		String iActive =null;
		
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + idSearchUrl,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			Map<String, Object> issobj = null;
			issobj = (Map<String, Object>) responseBody.getData();
			if(issobj!=null) {
				if (issobj.get("description") != null) {
					iDescription = String.valueOf(issobj.get("description"));
				} else {
					iDescription = "";
				}
				if (issobj.get("mdmId") != null) {
					iMdmId = String.valueOf(issobj.get("mdmId"));
				} else {
					iMdmId = "";
				}
				
				iName =  (String) issobj.get("issuerName");
				iCode =  String.valueOf(issobj.get("issuerId"));
				iActive = String.valueOf(issobj.get("isActive"));
			}
			
			issBean = new Issuer(iName, iCode, iDescription, iMdmId, iActive);

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getIssuerById()" + e);
			throw new ServiceException("Failed to process.. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return issBean;
	}

	public List<IssuerDTO> getIssuersByName(String issuerName) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<IssuerDTO> issList = new ArrayList<>();
		ObjectMapper objectMapper = new ObjectMapper();
		String tempurl = "";
		tempurl = "issuers/search";
		try {

			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(configBaseUrl + tempurl)
					.queryParam("issuerName", issuerName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			List<IssuerDTO> issuerDtos = (List<IssuerDTO>) responseBody.getData();
			Iterator<IssuerDTO> itr = issuerDtos.iterator();
			while (itr.hasNext()) {
				issList.add(objectMapper.convertValue(itr.next(), IssuerDTO.class));
			}
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getIssuersByName()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}
		logger.debug(CCLPConstants.EXIT);
		return issList;
	}

	/**private ClientHttpRequestFactory getClientHttpRequestFactory() {
		int timeout = 10000;
		HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		clientHttpRequestFactory.setConnectTimeout(timeout);
		clientHttpRequestFactory.setReadTimeout(timeout);
		return clientHttpRequestFactory;
	}*/

	public ResponseDTO getCardRangeCount(long issuerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + ISSUER_BASED_URL+issuerId+"/cardRangeCount", ResponseDTO.class);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getIssuerCount()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

}
