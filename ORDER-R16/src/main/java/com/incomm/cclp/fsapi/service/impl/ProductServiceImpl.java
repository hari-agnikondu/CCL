package com.incomm.cclp.fsapi.service.impl;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.B2BResponseMessage;
import com.incomm.cclp.fsapi.dao.ProductDAO;
import com.incomm.cclp.fsapi.service.ProductService;
import com.incomm.cclp.service.impl.DistributedCacheServiceImpl;
import com.incomm.cclp.util.Util;

@Service
public class ProductServiceImpl implements ProductService {

	private static final Logger logger = LogManager.getLogger(ProductServiceImpl.class);

	@Autowired
	ProductDAO productDao;

	@Autowired
	DistributedCacheServiceImpl distributedCacheServiceImpl;

	@Override
	public Map<String, Map<String, Object>> getProductAttributes(String productId) throws ServiceException {
		logger.debug("ENTER");

		String strProductAttributes = null;
		Map<String, Map<String, Object>> productAttributes = null;

		try {

			if (CollectionUtils.isEmpty(productAttributes)) {

				if (!Util.isEmpty(productId) && productDao.checkProductByProductId(productId) == 1) {
					/**
					 * Get product attributes json string by product id
					 */
					strProductAttributes = productDao.getProductAttributesByProductId(productId);

					/**
					 * Convert product attributes json to map
					 */
					if(!Util.isEmpty(strProductAttributes)) {
					productAttributes = Util.jsonToMap(strProductAttributes);
					}
					/**
					 * Add product attributes map to Cache
					 */
					//distributedCacheServiceImpl.getOrAddProductAttributes(Long.parseLong(productId), productAttributes);
					
					//checking whether product is of B2B type
					/*productAttributes_product = productAttributes.get("Product");

					
					 if(productAttributes_product != null && productAttributes_product.containsKey("b2bUpc") && !Objects.isNull(productAttributes.get("b2bUpc"))) 
					 {
						
					}*/
					
				} else {
					logger.error("Product does not exist");
					
				return null;
				}

			}

		} catch (NumberFormatException ne) {
			logger.error("Error Occured while try to convert String to Long, {}", ne);
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}catch (IOException e) {
			logger.error("Error Occured while try to convert product attribut json to map, {}", e);
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		} catch (Exception e) {
			logger.error("Error Occured while try to get product attributes, {}", e);
			throw new ServiceException(B2BResponseMessage.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR, B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		}

		logger.debug("EXIT");

		return productAttributes;
	}

}
