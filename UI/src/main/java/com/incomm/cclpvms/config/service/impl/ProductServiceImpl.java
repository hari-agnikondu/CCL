package com.incomm.cclpvms.config.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.incomm.cclpvms.config.model.Alert;
import com.incomm.cclpvms.config.model.CardStatusDTO;
import com.incomm.cclpvms.config.model.CurrencyCodeDTO;
import com.incomm.cclpvms.config.model.DeliveryChannel;
import com.incomm.cclpvms.config.model.Product;
import com.incomm.cclpvms.config.model.ProductDTO;
import com.incomm.cclpvms.config.model.PurseDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.config.model.RuleSetDTO;
import com.incomm.cclpvms.config.service.ProductService;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ProductException;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.session.SessionService;
import com.incomm.cclpvms.util.ResourceBundleUtil;
import com.incomm.cclpvms.util.Util;

@Service
public class ProductServiceImpl implements ProductService {
	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class.getName());
	private static final String MASTER_BASE_URL = "/master";

	@Autowired
	RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}")
	String configBaseUrl;

	@Value("${JOB_BASE_URL}")
	String jobBaseUrl;

	@Autowired
	SessionService sessionService;

	String productBaseURL = "/products";
	String alertsBaseURL = "/alerts";

	public ResponseDTO createProduct(ProductDTO productdto) throws ServiceException {
		logger.info("Inside ProductServiceImpl createProduct Method Starts Here");

		ResponseDTO responseBody = null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.postForEntity(configBaseUrl + "products",
					productdto, ResponseDTO.class);
			HttpStatus httpStatus = responseEntity.getStatusCode();
			logger.debug("status of CreateIssuer " + httpStatus);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			logger.error("Error Occured During Rest Call in createProduct()");
			throw new ServiceException("Failed to process, Please try again");
		}

		logger.info("Inside ProductServiceImpl createProduct Method Ends Here");
		return responseBody;

	}

	@SuppressWarnings("unchecked")
	public List<ProductDTO> getAllProducts() throws ServiceException, ProductException {
		logger.info("Inside ProductServiceImpl getAllProducts Method Starts Here");
		String tempurl = "";
		tempurl = "products/";
		String nameSearchUrl = tempurl;
		ResponseDTO responseBody = null;

		List<ProductDTO> productDtos = null;
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + nameSearchUrl,
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call is: ", responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.FAILURE)) {
				throw new ProductException(responseBody.getMessage() != null ? responseBody.getMessage()
						: "Failed to fetch all the products");
			}
			productDtos = (List<ProductDTO>) responseBody.getData();

		} catch (RestClientException e) {

			logger.error("Error Occured while making a Rest Call in getAllProducts()");
			throw new ServiceException("Failed to process Please try again later.");
		}

		return productDtos;
	}

	public ResponseDTO updateProduct(ProductDTO productdto) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;
		try {
			HttpHeaders headers = new HttpHeaders();
			HttpEntity<ProductDTO> requestEntity = new HttpEntity<>(productdto, headers);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(configBaseUrl + "products",
					HttpMethod.PUT, requestEntity, ResponseDTO.class);
			responseBody = responseEntity.getBody();
			logger.info("reponse of Rest Call : " + responseBody.getResult());

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in updateProduct()" + e);
			throw new ServiceException("Failed to process, Please try later.");
		}

		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	public ResponseDTO deleteProduct(Long productId) throws ServiceException {
		return null;
	}

	@SuppressWarnings("unchecked")
	public Product getProductById(Long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		String tempurl = "";
		tempurl = "products/" + productId;

		String idSearchUrl = tempurl;
		ResponseDTO responseBody = null;
		Product prodBean = null;
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + idSearchUrl,
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			Map<String, Object> prodObj = null;
			prodObj = (Map<String, Object>) responseBody.getData();

			String productName = (String) prodObj.get(CCLPConstants.PRODUCT_NAME);
			String productShortName = (String) prodObj.get("productShortName");
			String description = (String) prodObj.get("description");
			String partnerName = (String) prodObj.get("partnerName");
			String issuerName = (String) prodObj.get("issuerName");
			List<String> cardRanges = (List<String>) prodObj.get("listOfCardRange");

			String parentProductName = (String) prodObj.get("parentProductName");
			String action = (String) prodObj.get("action");
			String isActive = (String) prodObj.get("isActive");
			String issuerId = String.valueOf(prodObj.get("issuerId"));
			String partnerId = String.valueOf(prodObj.get("partnerId"));
			Long programId = Long.parseLong(Util.convertAsLong(prodObj.get("programId")));

			List<String> packageId = (List<String>) prodObj.get("listOfPackageDefinition");

			List<String> supportedPurse = (List<String>) prodObj.get("listOfPurse");
			
			List<String> partnerCurrency = (List<String>) prodObj.get("listOfCurrencyCode");
		
			
			String upc = (String) prodObj.get("upc");

			Map<String, String> productAttribute = new HashMap<>();
			Map<String, Map<String, String>> attributes = new HashMap<>();
			if (prodObj.get("attributesMap") != null) {
				attributes = (Map<String, Map<String, String>>) prodObj.get("attributesMap");
				productAttribute = attributes.get("Product");
			}
			List<Object> packageIdObj = (List<Object>) prodObj.get("listOfPackageDefinition");
			List<Object> cardRangeObj = (List<Object>) prodObj.get("listOfCardRange");
			List<Object> supportedPurseObj = (List<Object>) prodObj.get("listOfPurse");
			List<Object> productPurseObj = (List<Object>) prodObj.get("listProductPurse");
			prodBean = new Product(productName, partnerName, parentProductName, issuerName, action, description,
					isActive, productShortName, cardRanges, packageId, supportedPurse, upc, attributes, issuerId,
					partnerId, productId, prodObj, productId, productAttribute, cardRangeObj, packageIdObj,
					supportedPurseObj, programId,partnerCurrency,productPurseObj);

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getProductById()" + e);
			throw new ServiceException("Failed to process, Please try again later");
		}

		logger.debug(CCLPConstants.EXIT);
		return prodBean;
	}

	@SuppressWarnings("unchecked")
	public List<ProductDTO> getProductsByName(String productName) throws ServiceException, ProductException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		String tempurl = "";
		tempurl = "products/search";
		List<ProductDTO> productDtos = null;

		try {

			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(configBaseUrl + tempurl)
					.queryParam(CCLPConstants.PRODUCT_NAME, productName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.FAILURE)) {
				throw new ProductException(
						responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all products");
			}
			productDtos = (List<ProductDTO>) responseBody.getData();

		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllProducts()");
			throw new ServiceException("Failed to process Please try again later");
		}

		return productDtos;
	}

	public ResponseDTO getProductCount(Long productId) throws ServiceException {
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, List> getCCFList() throws ServiceException, ProductException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		String url = "products/ccfs";
		Map<String, List> dropdownlist = new HashMap<>();

		try {
			responseBody = restTemplate.getForObject(configBaseUrl + url, ResponseDTO.class);
			dropdownlist = (Map<String, List>) responseBody.getData();

		} catch (RestClientException e) {
			logger.error("Error Occured during making a Rest Call in getIssuerNames()" + e);
			throw new ServiceException("Failed to process. Please try again later.");
		}
		logger.debug(CCLPConstants.EXIT);
		return dropdownlist;
	}

	/* General tab starts */
	@SuppressWarnings("unchecked")
	public Product getGeneralTabByProductId(long productId) throws ServiceException {
		Product product = null;
		logger.debug(CCLPConstants.ENTER);

		try {
			product = new Product();
			ResponseDTO responseDTO = restTemplate.getForObject(
					configBaseUrl + productBaseURL + "/" + productId + "/generalConfigs", ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				Map<String, Object> map = (Map<String, Object>) responseDTO.getData();
				Map<String, Object> generalMap = (Map<String, Object>) map.get("generalAttributes");
				product.setProductAttributes(generalMap);
				product.setProductId(productId);
				Integer partnerId = (Integer) map.get("partnerId");
				product.setPartnerId(Long.valueOf(partnerId));
				Integer ruleSetId = (Integer) map.get("ruleSetId");
				if (ruleSetId != null)
					product.setRuleSetId(Long.valueOf(ruleSetId));
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getGeneralTabByProductId:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return product;

	}

	public ResponseEntity<ResponseDTO> updateGeneralTab(Product product) throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO = null;
		try {
			Map<String, Object> productMap = new HashMap<>();
			productMap.put(CCLPConstants.USER_ID, sessionService.getUserId());
			productMap.put("generalMap", product.getProductAttributes());
			if (product.getRuleSetId() > 0) {
				productMap.put("ruleSetId", product.getRuleSetId());
			}

			responseDTO = restTemplate.exchange(configBaseUrl + productBaseURL + "/" + product.getProductId(),
					HttpMethod.PUT, new HttpEntity<Map<String, Object>>(productMap), ResponseDTO.class);

		} catch (RestClientException e) {
			logger.error("RestClientException in updateGeneralTab:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;

	}

	@SuppressWarnings("unchecked")
	public Map<Long, String> getParentProducts() throws ServiceException {
		Map<Long, String> parentProductMap = null;
		Map<Long, String> sortedProductMap = null;

		logger.debug(CCLPConstants.ENTER);

		try {
			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl + productBaseURL + "/getParentProducts",
					ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				parentProductMap = new ModelMapper().map(responseDTO.getData(), Map.class);

				try {
				sortedProductMap = parentProductMap.entrySet().stream()
						.sorted(Map.Entry.comparingByValue())
						.collect(Collectors.toMap(Map.Entry::getKey, e ->e.getKey()+":"+e.getValue(),(e1, e2) -> e1,
                                LinkedHashMap::new));
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch (RestClientException e) {
			
			logger.error("RestClientException in getParentProducts:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return sortedProductMap;

	}

	public Map<Long, String> getRuleSetMetaData() throws ServiceException {

		Map<Long, String> ruleSetMap = new HashMap<>();
		logger.debug(CCLPConstants.ENTER);

		try {
			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl + "/ruleSet", ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				List<RuleSetDTO> ruleSetList = new ModelMapper().map(responseDTO.getData(),
						new TypeToken<List<RuleSetDTO>>() {
						}.getType());
				if (ruleSetList != null) {
					for (Iterator<RuleSetDTO> iterator = ruleSetList.iterator(); iterator.hasNext();) {
						RuleSetDTO ruleSetDTO = iterator.next();
						ruleSetMap.put(ruleSetDTO.getRuleSetId(), ruleSetDTO.getRuleSetName());

					}
				}
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getRuleSetMetaData:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return ruleSetMap;

	}

	/* General tab ends */
	/* Limit tab starts */

	public ResponseDTO addLimits(Map<String, Object> limitAttributes, long productId,long purseId) throws ServiceException {

		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(limitAttributes, headers);
			logger.debug("Calling '{}' service to update partner", configBaseUrl);
			logger.debug("addLimits" + limitAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					configBaseUrl + "/products/{productId}/purse/{purseId}/limits", HttpMethod.PUT, requestEntity, ResponseDTO.class,
					productId,purseId);

			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating partner {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		return responseBody;
	}

	@Override
	public ResponseDTO getLimits(long productId,long parentProductId,long purseId) throws ServiceException {

		ResponseDTO responseBody = null;
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + "/products/{productId}/parentProduct/{parentProductId}/purse/{purseId}/limits", ResponseDTO.class, productId,parentProductId,purseId);
			responseBody = responseEntity.getBody();

		} catch (RestClientException e) {

			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseBody;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getDeliveryChnlTxns() throws ServiceException {

		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;

		List<List<Object>> deliveryChnlTxns = null;
		List<DeliveryChannel> delChnlList = new ArrayList<>();
		List<Object> txnDtlsList = new ArrayList<>();
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + "/products/transactionsList", ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				deliveryChnlTxns = mm.map(responseBody.getData(), List.class);

				List<Object> list = new ArrayList<>();
				Map<String, String> txnFinancialFlag = new HashMap<>();
				Map<String, String> txnERIFFlag = new HashMap<>();
				Map<String, String> txnMRIFFlag = new HashMap<>();
				Map<String, String> txndesc = new HashMap<>();
				Map<String, String> dlctxn = new HashMap<>();

				Iterator<List<Object>> chnlIterator = deliveryChnlTxns.iterator();
				while (chnlIterator.hasNext()) {
					List<Object> objAry = chnlIterator.next();

					if (list.contains(objAry.get(0)))
						continue;

					Map<String, String> txnMap = new HashMap<>();
					Iterator<List<Object>> chnlIterator2 = deliveryChnlTxns.iterator();
					while (chnlIterator2.hasNext()) {
						List<Object> objAry1 = chnlIterator2.next();
						if (objAry1.get(0).equals(objAry.get(0)) && !objAry1.get(3).equals(objAry.get(3))) {
							txnMap.put((String) objAry1.get(3), (String) objAry1.get(4));
							txnERIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(6));
							txnMRIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(7));
							txndesc.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(9));
							dlctxn.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(8));
							list.add(objAry1.get(0));

						}
						txnFinancialFlag.put((String) objAry1.get(3), (String) objAry1.get(5));
					}
					txnMap.put((String) objAry.get(3), (String) objAry.get(4));
					txnERIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(6));
					txnMRIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(7));
					txndesc.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(9));
					dlctxn.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(8));

					DeliveryChannel delChnl = new DeliveryChannel((String) objAry.get(0), (String) objAry.get(1),
							(String) objAry.get(2), txnMap);
					delChnlList.add(delChnl);
					logger.debug(delChnl);
				}

				Collections.sort(delChnlList);
				logger.debug(delChnlList);

				txnDtlsList.add(delChnlList);
				txnDtlsList.add(txnFinancialFlag);
				txnDtlsList.add(txnERIFFlag);
				txnDtlsList.add(txnMRIFFlag);
				txnDtlsList.add(dlctxn);
				txnDtlsList.add(txndesc);
			} else
				logger.error("Failed to Fetch Delivery Channels from config srvice");
		} catch (RestClientException e) {
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return txnDtlsList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<Object, String> getAllParentProducts() throws ServiceException {

		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;
		Map<Object, String> productMap = null;

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + "/products/parentProductList", ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				Map<Object, String> parentProductMap = mm.map(responseBody.getData(), Map.class);
				productMap = parentProductMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
						.collect(Collectors.toMap(e -> e.getKey(), e -> e.getKey()+":"+e.getValue(),(e1, e2) -> e1,
                                LinkedHashMap::new));
				logger.debug(parentProductMap);
			} else
				logger.error("Failed to Fetch LimitAttributes from config srvice");
		} catch (RestClientException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return productMap;
	}
	/* Limit tab ends */

	@SuppressWarnings({ "unchecked" })
	public Product getCVVTabByProductId(Long productId) throws ServiceException {
		Product product = new Product();
		logger.debug(CCLPConstants.ENTER);

		try {

			ResponseDTO responseDTO = restTemplate
					.getForObject(configBaseUrl + productBaseURL + "/" + productId + "/cvvs", ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				Map<String, Object> map = (Map<String, Object>) responseDTO.getData();
				Map<String, Object> cvvMap = (Map<String, Object>) map.get("cvvAttributes");

				if (cvvMap != null) {
					product.setProductAttributes(cvvMap);
				}
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getCVVTabByProductId:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return product;
	}

	public ResponseEntity<ResponseDTO> updateCVVTab(Map<String, Object> productAttributes, Long productId)
			throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO = null;
		Map<String, Object> prodAttributes = new HashMap<>();
		prodAttributes.put("cvvAttributes", productAttributes);
		prodAttributes.put(CCLPConstants.USER_ID, sessionService.getUserId());

		try {

			responseDTO = restTemplate.exchange(configBaseUrl + productBaseURL + "/" + productId + "/cvvs",
					HttpMethod.PUT, new HttpEntity<Map<String, Object>>(prodAttributes), ResponseDTO.class);

		} catch (RestClientException e) {
			logger.error("RestClientException in updateCvvTab:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;

	}

	@Override
	public List<String> getCardStatus() throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		List<String> cardStatusList = new ArrayList<>();

		try {

			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl + "/cardStatus", ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				List<CardStatusDTO> cardStatusDtoList = new ModelMapper().map(responseDTO.getData(),
						new TypeToken<List<CardStatusDTO>>() {
						}.getType());

				if (cardStatusDtoList != null)
					cardStatusList = cardStatusDtoList.stream().map(CardStatusDTO::getStatusDesc)
							.collect(Collectors.toList());

			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getCardStatus:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return cardStatusList;
	}

	@SuppressWarnings("unchecked")
	public Product getTxnBasedOnCardStatus(Long productId) throws ServiceException {
		Product product = new Product();
		logger.debug(CCLPConstants.ENTER);

		try {

			ResponseDTO responseDTO = restTemplate.getForObject(
					configBaseUrl + productBaseURL + "/" + productId + "/txnCardStatusConfigs", ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				Map<String, Object> map = (Map<String, Object>) responseDTO.getData();
				Map<String, Object> cardStatusMap = (Map<String, Object>) map.get("cardStatusAttributes");

				if (cardStatusMap != null) {
					product.setProductAttributes(cardStatusMap);
				}
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getTxnBasedOnCardStatus:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return product;
	}

	@Override
	public ResponseEntity<ResponseDTO> saveTxnOnCardStatById(Map<String, Object> productAttributes, Long productId)
			throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO = null;
		Map<String, Object> prodAttributes = new HashMap<>();
		prodAttributes.put("cardStatusAttributes", productAttributes);
		prodAttributes.put(CCLPConstants.USER_ID, sessionService.getUserId());

		try {

			responseDTO = restTemplate.exchange(
					configBaseUrl + productBaseURL + "/" + productId + "/txnCardStatusConfigs", HttpMethod.PUT,
					new HttpEntity<Map<String, Object>>(prodAttributes), ResponseDTO.class);

		} catch (RestClientException e) {
			logger.error("RestClientException in saveTxnOnCardStatById:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;
	}

	/**
	 * Alerts tab starts from here
	 */

	@SuppressWarnings("unchecked")
	public List<Alert> getMessages() throws ServiceException {
		List<Alert> alertMessagesMap = null;

		logger.debug(CCLPConstants.ENTER);

		try {
			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl + productBaseURL + alertsBaseURL,
					ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				alertMessagesMap = (List<Alert>) responseDTO.getData();
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in alertMessagesMap:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return alertMessagesMap;

	}

	/* TxnFee Tab Starts */
	@Override
	public ResponseDTO addTxnFees(Map<String, Object> txnFeeAttributes, long productId,long purseId ) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(txnFeeAttributes, headers);
			logger.debug("Calling '{}' service to update TxnFees", configBaseUrl);

			logger.debug("addTxnFees" + txnFeeAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					configBaseUrl + "/products/{productId}/purse/{purseId}/txnFees", HttpMethod.PUT, requestEntity, ResponseDTO.class,
					productId,purseId);

			responseBody = responseEntity.getBody();
			logger.info("responseBody " + responseBody);
		} catch (RestClientException e) {
			logger.error("Exception while updating TxnFees {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO getTxnFees(long productId,long parentProductId,long purseId) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + "/products/{productId}/parentProduct/{parentProductId}/purse/{purseId}/txnFees", ResponseDTO.class, productId,parentProductId,purseId);
			responseBody = responseEntity.getBody();
			logger.debug("responseBody " + responseBody);

		} catch (RestClientException e) {
			logger.error("Failed to Fetch transaction fee Attributes from config srvice " + e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}
	/* Txn Fees Tab Ends */

	/* Monthly Fee Cap Tab Starts */
	@Override
	public ResponseDTO addMonthlyFeeCap(Map<String, Object> monthlyFeeCapAttributes, long productId,long purseId)
			throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(monthlyFeeCapAttributes, headers);
			logger.debug("Calling '{}' service to update Monthly Fee Cap", configBaseUrl);

			logger.debug("addmonthlyFeeCapAttributes " + monthlyFeeCapAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					configBaseUrl + "/products/{productId}/purse/{purseId}/monthlyFeeCap", HttpMethod.PUT, requestEntity,
					ResponseDTO.class, productId,purseId);

			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating Monthly Fee Cap {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO getMonthlyFeeCap(long productId,long parentProductId,long purseId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ResponseEntity<ResponseDTO> responseEntity = null;
		try {

			responseEntity = restTemplate.getForEntity(configBaseUrl + "/products/{productId}/parentProduct/{parentProductId}/purse/{purseId}/monthlyFeeCap",
					ResponseDTO.class, productId,parentProductId,purseId);
			logger.debug(responseEntity.getBody());

		} catch (RestClientException e) {
			logger.error("Failed to Fetch Monthly Fee Cap Attributes from config srvice " + e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseEntity.getBody();
	}
	/* Monthly Fee Cap Tab Ends */

	/* Maintenance Fee Tab Starts */
	@Override
	public ResponseDTO addMaintenanceFee(Map<String, Object> monthlyFeeCapAttributes, long productId)
			throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(monthlyFeeCapAttributes, headers);
			logger.debug("Calling '{}' service to update Monthly Fee Cap", configBaseUrl);

			logger.debug("addmonthlyFeeCapAttributes " + monthlyFeeCapAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					configBaseUrl + "/products/{productId}/maintenanceFee", HttpMethod.PUT, requestEntity,
					ResponseDTO.class, productId);

			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating MaintenanceFee {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseBody;
	}

	@Override
	public ResponseDTO getMaintenanceFee(long productId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		ResponseEntity<ResponseDTO> responseEntity = null;
		try {

			responseEntity = restTemplate.getForEntity(configBaseUrl + "/products/{productId}/maintenanceFee",
					ResponseDTO.class, productId);
			logger.debug(responseEntity.getBody());

		} catch (RestClientException e) {
			logger.error("Failed to Fetch MaintenanceFee Attributes from config srvice " + e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return responseEntity.getBody();
	}

	/* Maintenance Fee Tab Ends */

	@SuppressWarnings("unchecked")
	public Product getPinTabByProductId(Long productId) throws ServiceException {
		Product product = null;
		logger.debug(CCLPConstants.ENTER);

		try {
			product = new Product();
			ResponseDTO responseDTO = restTemplate
					.getForObject(configBaseUrl + productBaseURL + "/" + productId + "/pins", ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				Map<String, Object> map = (Map<String, Object>) responseDTO.getData();
				Map<String, Object> pinMap = (Map<String, Object>) map.get("pinAttributes");

				if (pinMap != null) {

					product.setProductAttributes(pinMap);
				}
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getPINTabByProductId:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return product;

	}

	/* update Pin Details */
	public ResponseEntity<ResponseDTO> updatePinTab(Map<String, Object> productAttributes, Long productId)
			throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO = null;
		try {

			responseDTO = restTemplate.exchange(configBaseUrl + productBaseURL + "/" + productId + "/pins",
					HttpMethod.PUT, new HttpEntity<Map<String, Object>>(productAttributes), ResponseDTO.class);

		} catch (RestClientException e) {
			logger.error("RestClientException in updatePinTab:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;

	}

	/**
	 * update alerts
	 */
	public ResponseEntity<ResponseDTO> updateAlertsTab(Map<String, String> alertAttributes, Long productId)
			throws ServiceException {
		ResponseEntity<ResponseDTO> responseDTO = null;
		try {

			responseDTO = restTemplate.exchange(configBaseUrl + productBaseURL + "/" + productId + alertsBaseURL,
					HttpMethod.PUT, new HttpEntity<Map<String, String>>(alertAttributes), ResponseDTO.class);

		} catch (RestClientException e) {
			logger.error("RestClient Exception in updateAlertsTab:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return responseDTO;

	}

	/**
	 * get alerts
	 */
	public ResponseDTO getAlertsTab(Long productId) throws ServiceException {

		ResponseEntity<ResponseDTO> responseDTO = null;

		ResponseDTO respDTO = new ResponseDTO();
		try {
			responseDTO = restTemplate.getForEntity(configBaseUrl + productBaseURL + "/" + productId + alertsBaseURL,
					ResponseDTO.class);

			respDTO = responseDTO.getBody();
		} catch (RestClientException e) {
			logger.error("RestClientException in updateAlertsTab:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return respDTO;
	}

	public ResponseDTO getPanExpiryDetails(Long productId) throws ServiceException {

		ResponseEntity<ResponseDTO> responseDTO = null;

		ResponseDTO respDTO = new ResponseDTO();
		try {

			responseDTO = restTemplate.getForEntity(configBaseUrl + productBaseURL + "/" + productId + "/panExpiry",
					ResponseDTO.class);

			respDTO = responseDTO.getBody();

		} catch (RestClientException e) {
			logger.error("RestClientException in updateAlertsTab:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return respDTO;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductDTO> getProductsByNameForCopy(String productName) throws ServiceException, ProductException {
		logger.debug(CCLPConstants.ENTER);
		ResponseDTO responseBody = null;

		String tempurl = "";
		tempurl = "products/searchProductCopy";
		List<ProductDTO> productDtos = null;

		try {

			UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(configBaseUrl + tempurl)
					.queryParam(CCLPConstants.PRODUCT_NAME, productName);

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(builder.build().encode().toUri(),
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			logger.debug("reponse from Rest Call : " + responseBody.getResult());
			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.FAILURE)) {
				throw new ProductException(
						responseBody.getMessage() != null ? responseBody.getMessage() : "Failed to fetch all products");
			}
			productDtos = (List<ProductDTO>) responseBody.getData();

		} catch (RestClientException e) {

			logger.error("Error Occured during making a Rest Call in getAllProducts()");
			throw new ServiceException("Failed to process. Please try again later.");
		}

		return productDtos;
	}

	@Override
	public Map<Object, String> getAllRetailProducts() throws ServiceException {

		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;
		Map<Object, String> productMap = null;

		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + "/products/retailProductList", ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				@SuppressWarnings("unchecked")
				Map<Object, String> parentProductMap = mm.map(responseBody.getData(), Map.class);
				productMap = parentProductMap.entrySet().stream().sorted(Map.Entry.comparingByValue())
						.collect(Collectors.toMap(e -> e.getKey(), e -> e.getKey()+":"+e.getValue(),(e1, e2) -> e1,
                                LinkedHashMap::new));
				logger.debug(parentProductMap);
			} else
				logger.error("Failed to Fetch LimitAttributes from config srvice");
		} catch (RestClientException e) {
			logger.error(Arrays.toString(e.getStackTrace()));
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return productMap;

	}

	@Override
	public Map getCardStatusList() throws ServiceException {
		ResponseDTO responseBody = null;
		Map<String, String> cardStatusMapList = null;
		try {
			logger.debug(CCLPConstants.ENTER);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + MASTER_BASE_URL + "/cardstatus", ResponseDTO.class);
			responseBody = responseEntity.getBody();
			@SuppressWarnings("unchecked")
			List<Object> cardStatusList = (List<Object>) responseBody.getData();
			cardStatusMapList = cardStatusList.stream()
					.map(s -> s.toString().replaceAll("[\\]\\[]", "").trim().split(","))
					.collect(Collectors.toMap(a -> a[0], a -> a[1]));
		} catch (RestClientException ex) {
			logger.error("Error while getting cardStatus List, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return cardStatusMapList;
	}

	/**
	 * This Method retrieves ProductList Based on partner
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Map<Long, String> getProductListwithSamePartner(Long partnerId, Long productId) throws ServiceException {
		Map<Long, String> parentProductMap = null;
		Map<Long, String> sortedProductMap = null;

		logger.debug(CCLPConstants.ENTER);

		ResponseDTO responseDTO = restTemplate.getForObject(
				configBaseUrl + productBaseURL + "/" + partnerId + "/getProductsWithSamePartner/"+ productId, ResponseDTO.class);
		if (responseDTO != null && responseDTO.getData() != null) {
			parentProductMap = new ModelMapper().map(responseDTO.getData(), Map.class);

			sortedProductMap = parentProductMap.entrySet().stream()
					.sorted(Map.Entry.comparingByValue(Comparator.naturalOrder()))
					.filter(map -> Long.parseLong(map.getKey() + "") != productId)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
							LinkedHashMap::new));
		}

		logger.debug(CCLPConstants.EXIT);
		return sortedProductMap;
	}

	@Override
	public String getSerialNumberResponse(long productId, String upc, long serialNumberQuantity)
			throws ServiceException {
		ResponseDTO responseBody = null;
		String response = "success";
		try {
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
					jobBaseUrl + "serialNumberRequest/" + productId + "/" + upc + "/" + serialNumberQuantity,
					ResponseDTO.class);

			responseBody = responseEntity.getBody();
			response = (String) responseBody.getData();
		} catch (RestClientException ex) {
			logger.error("Exception while sending serial Number request, {}", ex.getMessage());
			// throw new
			// ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		} catch (Exception ex) {
			logger.error("Main Exception  while sending serial Number request, {}", ex.getMessage());
		}

		return response;
	}

	@Override
	public Map<Double, String> generatePdfDocument() throws ServiceException {
		ResponseDTO responseBody = null;
		Map<Double, String> cardStatusList = null;
		try {
			logger.debug(CCLPConstants.ENTER);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(configBaseUrl + "/products/download",
					ResponseDTO.class);
			responseBody = responseEntity.getBody();
			cardStatusList = (Map<Double, String>) responseBody.getData();

		} catch (RestClientException ex) {
			logger.error("Error while getting cardStatus List, {}", ex.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return cardStatusList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<DeliveryChannel> getDelChnlTxns() throws ServiceException {

		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;

		List<List<Object>> deliveryChnlTxns = null;
		List<DeliveryChannel> delChnlList = new ArrayList<>();
		List<Object> txnDtlsList = new ArrayList<>();
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + "/products/transactionsList", ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				deliveryChnlTxns = mm.map(responseBody.getData(), List.class);

				List<Object> list = new ArrayList<>();
				Map<String, String> txnFinancialFlag = new HashMap<>();
				Map<String, String> txnERIFFlag = new HashMap<>();
				Map<String, String> txnMRIFFlag = new HashMap<>();
				Map<String, String> txndesc = new HashMap<>();
				Map<String, String> dlctxn = new HashMap<>();

				Iterator<List<Object>> chnlIterator = deliveryChnlTxns.iterator();
				while (chnlIterator.hasNext()) {
					List<Object> objAry = chnlIterator.next();

					if (list.contains(objAry.get(0)))
						continue;

					Map<String, String> txnMap = new HashMap<>();
					Iterator<List<Object>> chnlIterator2 = deliveryChnlTxns.iterator();
					while (chnlIterator2.hasNext()) {
						List<Object> objAry1 = chnlIterator2.next();
						if (objAry1.get(0).equals(objAry.get(0)) && !objAry1.get(3).equals(objAry.get(3))) {
							txnMap.put((String) objAry1.get(3), (String) objAry1.get(4));
							txnERIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(6));
							txnMRIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(7));
							txndesc.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(9));
							dlctxn.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(8));
							list.add(objAry1.get(0));

						}
						txnFinancialFlag.put((String) objAry1.get(3), (String) objAry1.get(5));
					}
					txnMap.put((String) objAry.get(3), (String) objAry.get(4));
					txnERIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(6));
					txnMRIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(7));
					txndesc.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(9));
					dlctxn.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(8));

					DeliveryChannel delChnl = new DeliveryChannel((String) objAry.get(0), (String) objAry.get(1),
							(String) objAry.get(2), txnMap);
					delChnlList.add(delChnl);
					logger.debug(delChnl);
				}

			} else
				logger.error("Failed to Fetch Delivery Channels from config srvice");
		} catch (RestClientException e) {
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return delChnlList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> getDeliveryChnlTxnsByChnlCode(String channelName) throws ServiceException {

		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;

		List<List<Object>> deliveryChnlTxns = null;
		List<DeliveryChannel> delChnlList = new ArrayList<>();
		List<Object> txnDtlsList = new ArrayList<>();
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate
					.getForEntity(configBaseUrl + "/products/transactionsList/" + channelName, ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				deliveryChnlTxns = mm.map(responseBody.getData(), List.class);

				List<Object> list = new ArrayList<>();
				Map<String, String> txnFinancialFlag = new HashMap<>();
				Map<String, String> txnERIFFlag = new HashMap<>();
				Map<String, String> txnMRIFFlag = new HashMap<>();
				Map<String, String> txndesc = new HashMap<>();
				Map<String, String> dlctxn = new HashMap<>();

				Iterator<List<Object>> chnlIterator = deliveryChnlTxns.iterator();
				while (chnlIterator.hasNext()) {
					List<Object> objAry = chnlIterator.next();

					if (list.contains(objAry.get(0)))
						continue;

					Map<String, String> txnMap = new HashMap<>();
					Iterator<List<Object>> chnlIterator2 = deliveryChnlTxns.iterator();
					while (chnlIterator2.hasNext()) {
						List<Object> objAry1 = chnlIterator2.next();
						if (objAry1.get(0).equals(objAry.get(0)) && !objAry1.get(3).equals(objAry.get(3))) {
							txnMap.put((String) objAry1.get(3), (String) objAry1.get(4));
							txnERIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(6));
							txnMRIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(7));
							txndesc.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(9));
							dlctxn.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(8));
							list.add(objAry1.get(0));

						}
						txnFinancialFlag.put((String) objAry1.get(3), (String) objAry1.get(5));
					}
					txnMap.put((String) objAry.get(3), (String) objAry.get(4));
					txnERIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(6));
					txnMRIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(7));
					txndesc.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(9));
					dlctxn.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(8));

					DeliveryChannel delChnl = new DeliveryChannel((String) objAry.get(0), (String) objAry.get(1),
							(String) objAry.get(2), txnMap);
					delChnlList.add(delChnl);
					logger.debug(delChnl);
				}

				Collections.sort(delChnlList);
				logger.debug(delChnlList);

				txnDtlsList.add(delChnlList);
				txnDtlsList.add(txnFinancialFlag);
				txnDtlsList.add(txnERIFFlag);
				txnDtlsList.add(txnMRIFFlag);
				txnDtlsList.add(dlctxn);
				txnDtlsList.add(txndesc);
			} else
				logger.error("Failed to Fetch Delivery Channels from config srvice based on channel name");
		} catch (RestClientException e) {
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return txnDtlsList;
	}

	@Override
	public List<Object> getDeliveryChnlTxnsByChnlCodeTxnName(String delchanl, String transactn)
			throws ServiceException {
		ModelMapper mm = new ModelMapper();
		ResponseDTO responseBody = null;

		List<List<Object>> deliveryChnlTxns = null;
		List<DeliveryChannel> delChnlList = new ArrayList<>();
		List<Object> txnDtlsList = new ArrayList<>();
		try {

			ResponseEntity<ResponseDTO> responseEntity = restTemplate.getForEntity(
					configBaseUrl + "/products/transactionsList/" + delchanl + "/" + transactn, ResponseDTO.class);
			responseBody = responseEntity.getBody();

			if (responseBody.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				deliveryChnlTxns = mm.map(responseBody.getData(), List.class);

				List<Object> list = new ArrayList<>();
				Map<String, String> txnFinancialFlag = new HashMap<>();
				Map<String, String> txnERIFFlag = new HashMap<>();
				Map<String, String> txnMRIFFlag = new HashMap<>();
				Map<String, String> txndesc = new HashMap<>();
				Map<String, String> dlctxn = new HashMap<>();

				Iterator<List<Object>> chnlIterator = deliveryChnlTxns.iterator();
				while (chnlIterator.hasNext()) {
					List<Object> objAry = chnlIterator.next();

					if (list.contains(objAry.get(0)))
						continue;

					Map<String, String> txnMap = new HashMap<>();
					Iterator<List<Object>> chnlIterator2 = deliveryChnlTxns.iterator();
					while (chnlIterator2.hasNext()) {
						List<Object> objAry1 = chnlIterator2.next();
						if (objAry1.get(0).equals(objAry.get(0)) && !objAry1.get(3).equals(objAry.get(3))) {
							txnMap.put((String) objAry1.get(3), (String) objAry1.get(4));
							txnERIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(6));
							txnMRIFFlag.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(7));
							txndesc.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(9));
							dlctxn.put((String) objAry1.get(1) + "_" + (String) objAry1.get(3),
									(String) objAry1.get(8));
							list.add(objAry1.get(0));

						}
						txnFinancialFlag.put((String) objAry1.get(3), (String) objAry1.get(5));
					}
					txnMap.put((String) objAry.get(3), (String) objAry.get(4));
					txnERIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(6));
					txnMRIFFlag.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(7));
					txndesc.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(9));
					dlctxn.put((String) objAry.get(1) + "_" + (String) objAry.get(3), (String) objAry.get(8));

					DeliveryChannel delChnl = new DeliveryChannel((String) objAry.get(0), (String) objAry.get(1),
							(String) objAry.get(2), txnMap);
					delChnlList.add(delChnl);
					logger.debug(delChnl);
				}

				Collections.sort(delChnlList);
				logger.debug(delChnlList);

				txnDtlsList.add(delChnlList);
				txnDtlsList.add(txnFinancialFlag);
				txnDtlsList.add(txnERIFFlag);
				txnDtlsList.add(txnMRIFFlag);
				txnDtlsList.add(dlctxn);
				txnDtlsList.add(txndesc);
			} else
				logger.error(
						"Failed to Fetch Delivery Channels from config srvice based on channel name and transaction name");
		} catch (RestClientException e) {
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return txnDtlsList;
	}

	@Override
	public ResponseDTO addAttributesByProgramID(Map<String, Object> productAttributes, Long programID)
			throws ServiceException {

		ResponseDTO responseBody = null;

		try {

			HttpHeaders headers = new HttpHeaders();
			HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(productAttributes, headers);
			logger.debug("Calling '{}' service to update attributes", configBaseUrl);
			logger.debug("addattributes" + productAttributes);
			ResponseEntity<ResponseDTO> responseEntity = restTemplate.exchange(
					configBaseUrl + "/products/{programID}/program", HttpMethod.PUT, requestEntity, ResponseDTO.class,
					programID);

			responseBody = responseEntity.getBody();
		} catch (RestClientException e) {
			logger.error("Exception while updating partner {}", e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		return responseBody;
	}

	@Override
	public Map<String, Map<String, String>> getAuthTypeMetaData() throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		Map<String, Map<String, String>> authenticationTypes = new HashMap<>();

		try {
			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl + MASTER_BASE_URL + "/authTypes",
					ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				authenticationTypes = new ModelMapper().map(responseDTO.getData(),
						new TypeToken<Map<String, Map<String, String>>>() {
						}.getType());
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getAuthTypeMetaData:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return authenticationTypes;

	}
	@Override
	public Map<String, Object> getPartnerCurrencies(long partnerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);

		Map<String, Object> partnerCurrencies =null;
		try {
			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl  +productBaseURL+ "/"+partnerId+"/getPartnerCurrency",
					ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				partnerCurrencies = (Map<String, Object>) responseDTO.getData();
			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getAuthTypeMetaData:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);

		return partnerCurrencies;
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CurrencyCodeDTO> getCurrencyCodeList(Long partnerId) throws ServiceException {
		logger.debug(CCLPConstants.ENTER);
		List<CurrencyCodeDTO>	currencyCodeDTO= new ArrayList<>();
		
		try {
			ResponseDTO responseDTO = restTemplate.getForObject(configBaseUrl  +productBaseURL+ "/"+partnerId+"/getPartnerCurrencyList",
					ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				currencyCodeDTO =  (List<CurrencyCodeDTO>) responseDTO.getData();
			}
		} 
		catch (RestClientException e) {
			logger.error("RestClientException in getCurrencyCodeby PartnerId:"+e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.debug(CCLPConstants.EXIT);
		return currencyCodeDTO;
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<PurseDTO> getPurseByProductId(Long productId, Long parentProductId, String attributeGroup) throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		List<PurseDTO> purseList = null;
		
		try {
			ResponseDTO responseDTO = restTemplate
					.getForObject(configBaseUrl + productBaseURL + "/" + productId +"/" + parentProductId +"/"+ attributeGroup + "/purse", ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				purseList = (List<PurseDTO>) responseDTO.getData();

			}
		} catch (RestClientException e) {
			logger.error("RestClientException in getPurseByProductId:" + e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.info(CCLPConstants.EXIT);
		return purseList;
	}

	@Override
	public ResponseEntity<ResponseDTO> updatePurse(Map<String, Object> productAttributes, Long productId,Long purseId)
			throws ServiceException {

		logger.info(CCLPConstants.ENTER);
		ResponseEntity<ResponseDTO> responseDTO = null;
		try {
			responseDTO = restTemplate.exchange(configBaseUrl + productBaseURL + CCLPConstants.SLASH + productId + "/purse/" + purseId,
					HttpMethod.PUT, new HttpEntity<Map<String, Object>>(productAttributes), ResponseDTO.class);
		} catch (RestClientException e) {
			logger.error("RestClientException in updatePinTab:" + e);
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.info(CCLPConstants.EXIT);
		return responseDTO;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getProductPurseAttributes(Long productId, Long purseId) throws ServiceException {
		logger.info(CCLPConstants.ENTER);

		Map<String, Object> purseMap = null;

		try {
			ResponseDTO responseDTO = restTemplate.getForObject(
					configBaseUrl + productBaseURL + CCLPConstants.SLASH + productId + "/purse/" + purseId, ResponseDTO.class);
			if (responseDTO != null && responseDTO.getData() != null) {
				purseMap = new ModelMapper().map(responseDTO.getData(), Map.class);

			}
		} catch (RestClientException e) {
			logger.error("RestClientException in retieving Product Purse Attributes:" + e.getMessage());
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		logger.info(CCLPConstants.EXIT);
		return purseMap;
	}


	
	
	
	
}
