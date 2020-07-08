package com.incomm.cclp.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.constants.ProductAttributesConstants;
import com.incomm.cclp.constants.PurseAPIConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CardDetailsService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.transaction.bean.SupportedPurse;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.service.impl.DistributedCacheServiceImpl;
import com.incomm.cclp.util.Util;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

	@Autowired
	ProductDAO productDao;

	@Autowired
	DistributedCacheServiceImpl distributedCacheServiceImpl;

	@Value("${CONFIG_BASE_URL}")
	private String configUrl;

	@Autowired
	CardDetailsService cardDetailsService;

	@Autowired
	RestTemplate restTemplate;

	public Map<String, Map<String, Object>> getProductAttributes(String productId, String purseId) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		String strProductAttributes = null;
		Map<String, Map<String, Object>> productAttributes = null;
		try {
			productAttributes = getProductOrPurseAttributesFromCache(productId, purseId);

			if (CollectionUtils.isEmpty(productAttributes)) {
				logger.info("Get product attributes from config service");
				/** added by nawaz for fetching attributes from config */
				Long productPurseId = Util.isEmpty(purseId) ? 0l : Long.parseLong(purseId);
				strProductAttributes = getProdPurseAttributes(Long.parseLong(productId), productPurseId);
				/**
				 * Convert product attributes json to map
				 */
				productAttributes = Util.jsonToMap(strProductAttributes);
			} else {
				logger.info("Get product attributes from distributed cache success");
			}

		} catch (NumberFormatException ne) {
			logger.error("Error Occured while trying to convert String to Long " + ne.getMessage(), ne);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		} catch (IOException e) {
			logger.error("Error Occured while trying to convert product attributes json to map " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		} catch (Exception e) {
			logger.error("Error Occured while trying to get product attributes " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug(GeneralConstants.EXIT);

		return productAttributes;
	}

	@Override
	public String getProductUPC(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		String actualUpcCode = "";
		Map<String, Object> productAttributes = null;
		productAttributes = valueDto.getProductAttributes()
			.get(ValueObjectKeys.PRODUCT);
		if (productAttributes.containsKey(ValueObjectKeys.RETAIL_UPC)
				&& !Util.isEmpty(String.valueOf(productAttributes.get(ValueObjectKeys.RETAIL_UPC)))) {
			actualUpcCode = productAttributes.get(ValueObjectKeys.RETAIL_UPC)
				.toString();
		} else if (productAttributes.containsKey(ValueObjectKeys.B2B_UPC)
				&& !Util.isEmpty(String.valueOf(productAttributes.get(ValueObjectKeys.B2B_UPC)))) {
			actualUpcCode = productAttributes.get(ValueObjectKeys.B2B_UPC)
				.toString();
		}

		logger.debug(GeneralConstants.EXIT);
		return actualUpcCode;

	}

	@Override
	public boolean checkProductValidity(ValueDTO valueDto) {

		logger.debug(GeneralConstants.ENTER);
		Map<String, String> valueObjMap = valueDto.getValueObj();
		String productId = valueObjMap.get(ValueObjectKeys.PRODUCT_ID);

		int count = productDao.checkProductValidity(productId);
		logger.info("Product Validity check {}", count);
		logger.debug(GeneralConstants.EXIT);
		return (count == 1) ? true : false;

	}

	@Override
	@Transactional
	public void updateCardStatus(String cardNumHash, String cardStatus) throws ServiceException {
		productDao.updateCardStatus(cardNumHash, cardStatus);
	}

	@Override
	public String getProductIdUsingUPC(String upc) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		String productId = null;
		try {
			logger.debug("Rest URL: " + configUrl + "products/" + upc + "/getProductIDByUPC");

			ResponseDTO responseDto = restTemplate.getForObject(configUrl + "products/" + upc + "/getProductIDByUPC", ResponseDTO.class);

			logger.debug("Response: {}", responseDto);
			if (ResponseCodes.API_RESPONSE_INVALID_UPC.equals(responseDto.getCode())) {
				logger.info(SpilExceptionMessages.INVALID_UPC);
				throw new ServiceException(SpilExceptionMessages.INVALID_UPC, ResponseCodes.INVALID_UPC);
			} else if (ResponseCodes.API_RESPONSE_SUCCESS.equals(responseDto.getCode())) {
				productId = String.valueOf(responseDto.getData());
			} else {
				logger.info(responseDto.getMessage());
				throw new ServiceException(responseDto.getMessage(), ResponseCodes.SYSTEM_ERROR);
			}
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {
			logger.error("Error in getProductIdUsingUPC: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug(GeneralConstants.EXIT);
		return productId;
	}

	@Override
	public String getProductType(ValueDTO valueDto) {
		logger.debug(GeneralConstants.ENTER);

		String productType = "";
		Map<String, Object> productAttributes = null;
		productAttributes = valueDto.getProductAttributes()
			.get(ProductAttributesConstants.ATTRIBUTE_GROUP_PRODUCT);
		if (productAttributes.containsKey(ProductAttributesConstants.ATTRIBUTE_FORM_FACTOR)
				&& !Util.isEmpty(String.valueOf(productAttributes.get(ProductAttributesConstants.ATTRIBUTE_FORM_FACTOR)))) {
			productType = productAttributes.get(ProductAttributesConstants.ATTRIBUTE_FORM_FACTOR)
				.toString();
		}
		logger.debug(GeneralConstants.EXIT);
		return productType;
	}

	@Override
	public List<String> getPackageIdsByProductId(String productId) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		List<String> packageIds = null;

		try {
			logger.debug("Rest URL: " + configUrl + "products/" + productId + "/packageIds");

			ResponseDTO responseDto = restTemplate.getForObject(configUrl + "products/" + productId + "/packageIds", ResponseDTO.class);

			logger.debug("Response: {}", responseDto);

			if (ResponseCodes.API_RESPONSE_SUCCESS.equals(responseDto.getCode())) {
				ObjectMapper objMapper = new ObjectMapper();
				packageIds = objMapper.convertValue(responseDto.getData(), new TypeReference<List<String>>() {
				});
			} else {
				logger.error("Error occured in getPackageIdsByProductId: " + responseDto.getMessage());
				throw new ServiceException(responseDto.getMessage(), ResponseCodes.SYSTEM_ERROR);
			}
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {
			logger.error("Error occured in getPackageIdsByProductId: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		logger.debug(GeneralConstants.EXIT);
		return packageIds;
	}

	/**
	 * Getting List of Supported Purse Details from productAttributes in Cache.
	 * 
	 * @author venkateshgaddam
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<SupportedPurse> getSupportedPurseDtls(ValueDTO valueDto) {

		Map<String, Map<String, Object>> productAtrributes = valueDto.getProductAttributes();
		List<SupportedPurse> purseMapping = new ArrayList<>();

		if (!CollectionUtils.isEmpty(productAtrributes)) {

			List<Object> supportedPurse = (List<Object>) productAtrributes.get("Product")
				.get("support_purses");
			if (supportedPurse != null && !supportedPurse.isEmpty()) {
				for (Iterator iterator = supportedPurse.iterator(); iterator.hasNext();) {
					Object[] supportedPurseOut = null;
					Object obj = iterator.next();

					if (obj instanceof Collection) {
						supportedPurseOut = ((List) obj).toArray();
					} else {
						supportedPurseOut = (Object[]) obj;
					}
					SupportedPurse suppPurseBean = new SupportedPurse();
					suppPurseBean.setProductId(supportedPurseOut[0] + "");
					suppPurseBean.setPurseId(supportedPurseOut[1] + "");
					suppPurseBean.setIsDefault(supportedPurseOut[2] + "");
					suppPurseBean.setCurrencyId(supportedPurseOut[3] + "");
					suppPurseBean.setCurrencyCode(supportedPurseOut[4] + "");
					suppPurseBean.setUpc(supportedPurseOut[5] + "");
					suppPurseBean.setMinorUnits(supportedPurseOut[6] + ""); // Added for minor units

					purseMapping.add(suppPurseBean);

				}
			}
		}
		return purseMapping;

	}

	@Override
	public String getPartnerDetails(ValueDTO valueDto) {
		logger.debug("Entering getProductDetails");

		String partnerId = String.valueOf(valueDto.getValueObj()
			.get("partnerID"));
		String partnerName = String.valueOf(valueDto.getValueObj()
			.get("partnerName"));
		return productDao.getPartnerDetails(partnerId, partnerName);

	}

	@Override
	public String getProductDefaultCurr(ValueDTO valueDto) {
		logger.debug("Entering getProductDetails");
		String purseId = String.valueOf(valueDto.getProductAttributes()
			.get("Product")
			.get("defaultPurse"));
		return productDao.getProductDefaultCurr(purseId);

	}

	@Override
	public String getMdmId(String partnerId) {
		logger.debug("Entering MdmId");
		return productDao.getMdmId(partnerId);
	}

	/*
	 * This method is used to get the product purse attributes from the Cache
	 */
	private Map<String, Map<String, Object>> getProductOrPurseAttributesFromCache(String productId, String purseId) {
		Map<String, Map<String, Object>> productOrPurseAttributes = null;

		try {

			logger.info("get Product Attributes for productId: {}, purseId:{}", productId, purseId);
			productOrPurseAttributes = distributedCacheServiceImpl.getProductAttributesCache(Long.parseLong(productId),
					productOrPurseAttributes);
			if (productOrPurseAttributes == null) {
				return null;
			}

			if (Util.isEmpty(purseId)) {
				purseId = (String) productOrPurseAttributes.get("Product")
					.get("defaultPurse");
			}

			logger.info("get Product Purse Attributes for productId: {}, purseId:{}", productId, purseId);
			String productpurse = productId + "_" + purseId;
			Map<String, Map<String, Object>> purseAttributes = distributedCacheServiceImpl.getProductPurseAttributesCache(productpurse,
					productOrPurseAttributes);
			productOrPurseAttributes.putAll(purseAttributes);

		} catch (Exception e) {
			logger.error("Error in getProductPurseAttributesFromCache: " + e.getMessage(), e);
			return null;
		}

		return productOrPurseAttributes;
	}

	/*
	 * This method is used to get the product purse attributes from the Configuration service
	 */
	public String getProdPurseAttributes(long productId, long purseId) {
		String existingProdPurseAttributes = "";
		try {
			Map<?, ?> responseDTO = restTemplate.getForObject(
					configUrl + "products/" + productId + "/purse/" + purseId + "/cache/getOrUpdateProdAttributesCache", Map.class);

			if (responseDTO != null && !responseDTO.isEmpty()
					&& GeneralConstants.SUCCESS_RESPONSE_CODE.equals(responseDTO.get("code") + "")) {
				existingProdPurseAttributes = (String) responseDTO.get("data");
			} else {
				logger.info("Error Occured while try to get product purse attributes, {}");
				throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
			}

		} catch (RestClientException e) {
			logger.error("RestClientException in getProdPurseAttributes:" + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

		return existingProdPurseAttributes;
	}

	@Override
	public void getPurseDetails(ValueDTO valueDto) {
		Map<String, String> valObj = valueDto.getValueObj();
		logger.debug("Entering getPurseDetails");
		if (valObj.containsKey(ValueObjectKeys.PURAUTHREQ_PURSE_NAME) && !Util.isEmpty(valObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME))) {
			String purseName = valObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME);
			PurseDTO purseDto = productDao.getpurseDto(purseName);
			if (purseDto != null) {
				valObj.put(ValueObjectKeys.PURSE_ID, String.valueOf(purseDto.getPurseId()));
				valObj.put(ValueObjectKeys.TXN_CURRENCY_CODE, purseDto.getCurrencyCode());
				valObj.put(ValueObjectKeys.PURSE_TYPE_ID, String.valueOf(purseDto.getPurseTypeId()));
				valObj.put(ValueObjectKeys.PURSE_TYPE, purseDto.getPurseType());
			} else {
				throw new ServiceException(SpilExceptionMessages.INVALID_PURSE, ResponseCodes.INVALID_PURSE);
			}
		} else {
			valueDto.getValueObj()
				.put(ValueObjectKeys.PURSE_TYPE_ID, PurseAPIConstants.CONSUMER_FUNDED_CURRENCY); // Must be Consumer
																									// funded currency
			valObj.put(ValueObjectKeys.PURSE_ID, null);
		}
		logger.debug("Exit getPurseDetails");
	}

}
