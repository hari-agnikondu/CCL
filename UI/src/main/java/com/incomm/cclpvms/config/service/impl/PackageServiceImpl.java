package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.Date;
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

import com.incomm.cclpvms.config.model.PackageDTO;
import com.incomm.cclpvms.config.model.PackageID;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.PackageService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.util.ResourceBundleUtil;


@Service
public class PackageServiceImpl implements PackageService {
	private static final Logger logger = LogManager.getLogger(PackageServiceImpl.class.getName());

	@Autowired
	RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}") String configBaseUrl;

	private static final String PACKAGE_BASE_URL = "/packages";
	
	private static final String MASTER_BASE_URL = "/master";
		
	private static final String REPLACEMENT_STRING = "[\\]\\[]";


	public List<String> getPackageByIds(List<String> packageIds) throws ServiceException {



		List<String> packageList = new ArrayList<>();

		ResponseDTO responseDTO=null;
		String	packageUrl = "packages/";
		String url="/getPackageDataById/"+packageIds;
		logger.debug(CCLPConstants.ENTER);

		try {	
			logger.debug("Calling '{}' service for getting package by Ids ",configBaseUrl);

			responseDTO=restTemplate.getForObject(configBaseUrl +packageUrl+url , ResponseDTO.class);
			if(responseDTO!=null && responseDTO.getData()!=null){ 

				packageList=(List<String>) responseDTO.getData();

			}

		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCardRange:"+e);
			throw new ServiceException(ResponseMessages.SERVER_DOWN);

		}
		logger.debug("EXIT");
		return packageList;

	}

	public List<PackageDTO> getAllPackages() throws ServiceException {
		logger.info("Inside PackageServiceImpl getAllPackages Method Starts Here");
		ResponseDTO responseBody = null;
		List<PackageDTO> packageDtos = null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + PACKAGE_BASE_URL,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(responseBody.getResult());
			}
			packageDtos = (List<PackageDTO>) responseBody.getData();

		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllPackages()");
			throw new ServiceException("Failed to process. Please try again later.");
		}

		return packageDtos;
	}



	public List<PackageDTO> getAllPackagesByName(String packageName) throws ServiceException {
		logger.info("Inside PackageServiceImpl getAllPackagesByName Method Starts Here");
		ResponseDTO responseBody = null;
		List<PackageDTO> packageDtos = null;
		try {

			UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(configBaseUrl + PACKAGE_BASE_URL + "/search")
					.queryParam("packageName", packageName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(responseBody.getResult());
			}
			packageDtos = (List<PackageDTO>) responseBody.getData();

		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllPackagesByName()");
			throw new ServiceException("Failed to process. Please try again later.");
		}

		return packageDtos;
	}

	@Override
	public Map getPackageIdList() throws ServiceException {
		ResponseDTO responseBody = null;
		Map<String, String> shipmentMapList = null;
		try {
			logger.debug(CCLPConstants.ENTER);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + PACKAGE_BASE_URL + "/packageIdList", ResponseDTO.class);
			responseBody = responseEntity.getBody();
			@SuppressWarnings("unchecked")
			List<Object> shipmentList = (List<Object>) responseBody.getData();
			shipmentMapList = shipmentList.stream().map(s -> s.toString().replaceAll(REPLACEMENT_STRING, "").trim().split(","))
					.collect(Collectors.toMap(a -> a[0], a -> a[1]));
		} catch (RestClientException ex) {
			logger.error("Error while getting shipment List, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return shipmentMapList;
	}

	@Override
	public Map getShipmentAttList() throws ServiceException {
		ResponseDTO responseBody = null;
		Map<String, String> shipmentMapList = null;
		try {
			logger.debug(CCLPConstants.ENTER);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + MASTER_BASE_URL + "/packageShipmentList", ResponseDTO.class);
			responseBody = responseEntity.getBody();
			@SuppressWarnings("unchecked")
			List<Object> shipmentList = (List<Object>) responseBody.getData();
			shipmentMapList = shipmentList.stream().map(s -> s.toString().replaceAll(REPLACEMENT_STRING, "").trim().split(","))
					.collect(Collectors.toMap(a -> a[0], a -> a[1]));
		} catch (RestClientException ex) {
			logger.error("Error while getting shipment List, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return shipmentMapList;
	}

	public Map getfulFillmentList() throws ServiceException {
		ResponseDTO responseBody = null;
		Map<String, String> fulFillmentList = null;
		try {
			logger.debug(CCLPConstants.ENTER);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + PACKAGE_BASE_URL + "/fulFillmentList", ResponseDTO.class);
			responseBody = responseEntity.getBody();
			@SuppressWarnings("unchecked")
			List<Object> shipmentList = (List<Object>) responseBody.getData();
			fulFillmentList = shipmentList.stream().map(s -> s.toString().replaceAll(REPLACEMENT_STRING, "").trim().split(","))
					.collect(Collectors.toMap(a -> a[0], a -> a[1]));
		} catch (RestClientException ex) {
			logger.error("Error while getting FulFillment List, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return fulFillmentList;
	}

	@Override
	public ResponseDTO createPackage(PackageID packageDtls) throws ServiceException {
		ResponseDTO responseBody = null;
		logger.debug(CCLPConstants.ENTER);
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl + PACKAGE_BASE_URL,
					constructObj(packageDtls), ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException ex) {
			logger.error("Error while creating new Package, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO udpatePackage(PackageID packageDtls) throws ServiceException {
		ResponseDTO responseBody = null;
		logger.debug(CCLPConstants.ENTER);
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + PACKAGE_BASE_URL,
					HttpMethod.PUT, new HttpEntity<PackageDTO>(constructObj(packageDtls)), ResponseDTO.class);
			responseBody = responseEntity.getBody();
		} catch (RestClientException ex) {
			logger.error("Error while creating update Package, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	private PackageDTO constructObj(PackageID packageDtls){
		PackageDTO packageDTO = new PackageDTO();
		packageDTO.setPackageId(packageDtls.getPackageId());
		packageDTO.setDescription(packageDtls.getDescription());
		packageDTO.setReplacementPackageId(packageDtls.getReplacementPackageId());
		packageDTO.setFulfillmentId(packageDtls.getFulfillmentId());
		packageDTO.setPackageId(packageDtls.getPackageId());
		packageDTO.setPackageAttributes(packageDtls.getPackageAttributes());
		packageDTO.setIsActive("Y");
		packageDTO.setInsDate(new Date());
		packageDTO.setLastUpdDate(new Date());
		logger.debug("packageDTO:" + packageDTO.toString());
		return packageDTO;
	}

	@Override
	public ResponseDTO deletePackage(PackageID packageDtls) throws ServiceException {
		return null;
	}

	@Override
	public PackageID getPackageId(String packageId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		PackageID packageObj = null;
		ModelMapper mm = new ModelMapper();
		logger.debug("Calling '{}' service for get getPackageId By ID '{}'", configBaseUrl, packageId);
		ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl + PACKAGE_BASE_URL + "/" + packageId,
				ResponseDTO.class);
		if (responseDTO != null && responseDTO.getData() != null) {
			PackageDTO packageDTO = mm.map(responseDTO.getData(), PackageDTO.class);
			packageObj = mm.map(packageDTO, PackageID.class);
		}
		return packageObj;
	}


}
