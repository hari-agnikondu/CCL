package com.incomm.cclpvms.config.service.impl;


import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.incomm.cclpvms.config.model.Merchant;
import com.incomm.cclpvms.config.model.MerchantDTO;
import com.incomm.cclpvms.config.model.MerchantProduct;
import com.incomm.cclpvms.config.model.MerchantProductDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.service.MerchantService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.exception.MerchantException;
import com.incomm.cclpvms.exception.ServiceException;
@Service
public class MerchantServiceImpl implements MerchantService{

	
	private static final Logger logger = LogManager.getLogger(MerchantServiceImpl.class);
	
	private static RestTemplate restTemplate = new RestTemplate();
	
	@Value("${CONFIG_BASE_URL}") String configBaseUrl;
	

	private static final String MERCHANT_NAME = "merchantName";
	private static final String MERCHANTS = "merchants";
	private static final String MERCHANT_BASED_URL = "merchants/";
	
	
	@Override
	public ResponseDTO createMerchant(MerchantDTO merchantDTO) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl + MERCHANTS, merchantDTO,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of createMerchant " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createMerchant()"+e);
			throw new ServiceException("Failed to process.. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;

	}

	@Override
	public List<MerchantDTO> getAllMerchants() throws ServiceException, MerchantException {
		
		logger.debug(CCLPConstants.ENTER);
		String url = "";
		ResponseDTO responseBody = null;
		List<MerchantDTO> merchantList = new ArrayList<>();
		
		try {
			url = MERCHANT_BASED_URL;
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + url,
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all Merchants");
			}
			@SuppressWarnings("unchecked")
			List<MerchantDTO> issuerDtos = (List<MerchantDTO>) responseBody.getData();

			merchantList.addAll(issuerDtos);
			
		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllMerchants()"+e);
			throw new ServiceException("Failed to process... Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return merchantList;
	
		
		
		
	}

	@Override
	public ResponseDTO updateMerchant(MerchantDTO merchantDTO) throws ServiceException {


		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<MerchantDTO> requestEntity = new HttpEntity<>(merchantDTO, headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + MERCHANTS, HttpMethod.PUT,
					requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call for  updateMerchant : " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in updateMerchant()" + e);
			throw new ServiceException("Failed to process.... Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	
	}

	@Override
	public ResponseDTO deleteMerchant(Long merchantId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		HttpHeaders headers = new HttpHeaders();
		String url = "";
		try {
			
			url = MERCHANT_BASED_URL + merchantId;
		
			HttpEntity<MerchantDTO> requestEntity = new HttpEntity(null, headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + url, HttpMethod.DELETE,
					requestEntity, ResponseDTO.class, (Object) merchantId);
			
			
			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call for deleteMerchant: " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in deleteMerchant()" + e);
			throw new ServiceException("Failed to process..... Please try again later.");
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public Merchant getMerchantById(Long merchantId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String tempurl = "";
		tempurl = MERCHANT_BASED_URL + merchantId;
		String merchantDescription = "";
		String mdmId = "";
		String idSearchUrl = tempurl;
		ResponseDTO responseBody = null;
		Merchant merchant = null;
		String merchantName = null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + idSearchUrl,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			Map<String, Object> merchantobj = null;
			merchantobj = (Map<String, Object>) responseBody.getData();
			if(merchantobj!=null) {
				if (merchantobj.get("description") != null) {
					merchantDescription = String.valueOf(merchantobj.get("description"));
				} else {
					merchantDescription = "";
				}
				if (merchantobj.get("mdmId") != null) {
					mdmId = String.valueOf(merchantobj.get("mdmId"));
				} else {
					mdmId = "";
				}
				
				if (merchantobj.get(MERCHANT_NAME) != null) {
					merchantName = String.valueOf(merchantobj.get(MERCHANT_NAME));
				} else {
					merchantName = "";
				}
				
			}
			
			
			merchant = new Merchant(merchantName, merchantDescription, mdmId, merchantId);

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getMerchantById()" + e);
			throw new ServiceException("Failed to process, Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return merchant;
	}

	@Override
	public List<MerchantDTO> getMerchantsByName(String merchantName) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		List<MerchantDTO> merchantList = new ArrayList<>();
		String tempurl = "";
		tempurl = "merchants/search";
		try {
		
			
			  UriComponentsBuilder builder = UriComponentsBuilder
			    .fromUriString(configBaseUrl+tempurl)
			        .queryParam(MERCHANT_NAME, merchantName);

ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);

			responseBody=responseEntity.getBody();
			@SuppressWarnings("unchecked")
			List<MerchantDTO> merchantDtos = (List<MerchantDTO>) responseBody.getData();
			
			merchantList.addAll(merchantDtos);
			
		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getMerchantsByName()" + e);
			throw new ServiceException(" Failed to process. Please try again later.");
		}
		logger.debug(CCLPConstants.EXIT);
		return merchantList;
	
	}
	
	@Override
	public List<MerchantProductDTO> getAllMerchantsLinkedWithProducts(MerchantProduct merchantProduct)
			throws ServiceException {
		logger.debug("ENTER");
		String url = "";
		ResponseDTO responseBody = null;
		List<MerchantProductDTO> merchantProductList = new ArrayList<>();
		
		try {
			url = MERCHANT_BASED_URL;
			
			
			String productName; 
			if(merchantProduct.getProductName()==null || merchantProduct.getProductName().trim().isEmpty()){
				productName="*";
			}
			else{
				productName=merchantProduct.getProductName();
			}
			String merchantName; 
			if(merchantProduct.getMerchantName()==null || merchantProduct.getMerchantName().trim().isEmpty()){
				merchantName="*";
			}
			else{
				merchantName=merchantProduct.getMerchantName();
			}
			
		UriComponentsBuilder builder = UriComponentsBuilder
					.fromUriString(configBaseUrl+url+"searchMerchantProduct")
					.queryParam(MERCHANT_NAME,merchantName)
					.queryParam("productName", productName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.info("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase("FAILURE")) {
				throw new ServiceException(
						responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all Merchants Linked with Products");
			}
			List<MerchantProductDTO> merchantProductDtos = (List<MerchantProductDTO>) responseBody.getData();

			merchantProductList.addAll(merchantProductDtos);
			
	
		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllMerchantsLinkedWithProducts()"+e);
			throw new ServiceException("  Failed to process. Please try again later.");
		}

		logger.debug("EXIT");
		return merchantProductList;
	}

	@Override
	public ResponseDTO updateAssignProductToMerchant(MerchantProductDTO merchantProductDTO) 
			throws ServiceException {
			
		String url="/assignProductToMerchant";
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			
ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl + MERCHANTS+url, merchantProductDTO,
					ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.info("status of updateAssignProductToMerchant " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in updateAssignProductToMerchant()"+e);
			throw new ServiceException("Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;

	
	}
	
	@Override
	public ResponseDTO deleteProductMerchant(MerchantProductDTO merchantProductDTO) throws ServiceException
	{
		logger.debug(CCLPConstants.ENTER);
		String delUrl="/assignProductToMerchant";
		ResponseDTO responseBody = null;
		String url = "";
		try {
			
			url = MERCHANTS;
			
			ResponseEntity<ResponseDTO> responseDTO=null;
			responseDTO = restTemplate.exchange(configBaseUrl+url+delUrl,HttpMethod.DELETE,
					new HttpEntity<MerchantProductDTO>(merchantProductDTO), ResponseDTO.class);

			responseBody = responseDTO.getBody();
			logger.info("reponse from Rest Call for deleteProductMerchant: " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in deleteProductMerchant()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
			
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantProductDTO> getMerchantProductId(Long merchantId, Long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<MerchantProductDTO> merchantProductList = new ArrayList<>();
		String tempurl = "";
		tempurl = MERCHANT_BASED_URL + merchantId+"/"+productId;
		ResponseDTO responseBody = null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + tempurl,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			List<MerchantProductDTO> merchantProductDtos = (List<MerchantProductDTO>) responseBody.getData();

			merchantProductList.addAll(merchantProductDtos);
			
	
		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllMerchantsLinkedWithProducts()"+e);
			throw new ServiceException("  Failed to process. Please try again later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return  merchantProductList;
	}
	}
	
	

