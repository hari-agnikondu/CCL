package com.incomm.scheduler.serviceimpl;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.scheduler.cache.service.DistributedCacheService;
import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.ResponseMessages;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.service.ProductService;
import com.incomm.scheduler.utils.Util;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	DistributedCacheService distributedCacheService;

	@Autowired
	RestTemplate restTemplate;

	@Value("${CONFIG_BASE_URL}")
	private String configUrl;

	@Override
	public Map<String, Map<String, Object>> getProductAttributes(String productId, String purseId) {
		log.debug(CCLPConstants.ENTER);

		Map<String, Map<String, Object>> productAttributes = null;
		String strProductAttributes = null;

		try {
			productAttributes = getProductOrPurseAttributesFromCache(productId, purseId);

			if (CollectionUtils.isEmpty(productAttributes)) {
				log.info("Get product attributes from config service");
				Long productPurseId = Util.isEmpty(purseId) ? 0l : Long.parseLong(purseId);
				strProductAttributes = getProdPurseAttributes(Long.parseLong(productId), productPurseId);
				/**
				 * Convert product attributes json to map
				 */
				productAttributes = Util.jsonToMap(strProductAttributes);

			} else {
				log.info("Get product attributes from distributed cache success");
			}

		} catch (NumberFormatException ne) {
			log.error("Error Occured while trying to convert String to Long " + ne.getMessage(), ne);
			throw new ServiceException("Unable to convert String to Long", ResponseMessages.GENERIC_ERR_MESSAGE);
		} catch (IOException e) {
			log.error("Error Occured while trying to convert product attributes json to map " + e.getMessage(), e);
			throw new ServiceException("Unable to convert product attributes string to map", ResponseMessages.GENERIC_ERR_MESSAGE);
		} catch (Exception e) {
			log.error("Error Occured while trying to get product attributes " + e.getMessage(), e);
			throw new ServiceException("Unable to get product attributes", ResponseMessages.GENERIC_ERR_MESSAGE);
		}
		log.debug(CCLPConstants.EXIT);

		return productAttributes;
	}

	/*
	 * This method is used to get the product purse attributes from the Cache
	 */
	private Map<String, Map<String, Object>> getProductOrPurseAttributesFromCache(String productId, String purseId) {
		Map<String, Map<String, Object>> productOrPurseAttributes = null;

		try {

			log.info("get Product Attributes for productId: {}, purseId:{}", productId, purseId);
			productOrPurseAttributes = distributedCacheService.getProductAttributesCache(Long.parseLong(productId),
					productOrPurseAttributes);

		} catch (Exception e) {
			log.error("Error in getProductPurseAttributesFromCache: " + e.getMessage(), e);
			return Collections.emptyMap();
		}

		return productOrPurseAttributes;
	}

	/*
	 * This method is used to get the product purse attributes from the
	 * Configuration service
	 */
	public String getProdPurseAttributes(long productId, long purseId) {
		String existingProdPurseAttributes = "";
		try {
			Map<?, ?> responseDTO = restTemplate.getForObject(
					configUrl + "products/" + productId + "/purse/" + purseId + "/cache/getOrUpdateProdAttributesCache",
					Map.class);

			if (responseDTO != null && !responseDTO.isEmpty()
					&& CCLPConstants.SUCCESS_RESPONSE_CODE.equals(responseDTO.get("code") + "")) {
				existingProdPurseAttributes = (String) responseDTO.get("data");
			} else {
				log.info("Error Occured while try to get product purse attributes, {}");
				throw new ServiceException("Unable to get product attributes", ResponseMessages.GENERIC_ERR_MESSAGE);
			}

		} catch (RestClientException e) {
			log.error("RestClientException in getProdPurseAttributes:" + e.getMessage(), e);
			throw new ServiceException("Unable to get product attributes", ResponseMessages.GENERIC_ERR_MESSAGE);
		}

		return existingProdPurseAttributes;
	}

}
