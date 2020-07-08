package com.incomm.cclp.account.domain.validator;

import static com.incomm.cclp.account.util.CodeUtil.isNotNullAndEmpty;
import static com.incomm.cclp.account.util.CodeUtil.isNullOrEmpty;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.account.domain.exception.DomainExceptionFactory;
import com.incomm.cclp.account.domain.exception.DomainExceptionType;
import com.incomm.cclp.account.util.DateTimeFormatType;
import com.incomm.cclp.account.util.DateTimeUtil;
import com.incomm.cclp.cache.AppCacheService;
import com.incomm.cclp.constants.ApiExceptionMessage;
import com.incomm.cclp.constants.ProductAttributesConstants;
import com.incomm.cclp.service.ProductService;

@Service
public class ProductValidator {

	private static final Logger logger = LogManager.getLogger(ProductValidator.class);

	@Autowired
	ProductService productService;

	public void validateProductExpiryDate(long productId, AppCacheService cacheService) {

		Map<String, Object> product = cacheService.getProductAttributesForGroup(ProductAttributesConstants.ATTRIBUTE_GROUP_PRODUCT);

		Object validityDate = product.get("productValidityDate");

		if (isNullOrEmpty(validityDate)) {
			return;
		}
		Date productValidityDate = DateTimeUtil.parseDate(validityDate.toString(), DateTimeFormatType.MM_DD_YYYY_WITH_SLASH);
		if (DateTimeUtil.isPastDate(productValidityDate)) {
			logger.info("Expired Product:" + productId);
			throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, " Expired Product");
		}

		logger.info("Product validation check successfull");
	}

	// this check is disabled.
	public void validateUPC(String inputUPC, Object productUPC) {
		logger.debug("ENTER");

		if (isNotNullAndEmpty(inputUPC)) {
			if (isNullOrEmpty(productUPC)) {
				logger.error("Product does not contain UPC code value");
				throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, ApiExceptionMessage.ERR_PRODUCT_UPC);

			} else {
				if (productUPC.equals(inputUPC)) {
					logger.info("UPC validation check successfull");
				} else {
					logger.error("Invalid UPC");
					throw DomainExceptionFactory.from(DomainExceptionType.INPUT_VALIDATION_FAILED, ApiExceptionMessage.ERR_INVALID_UPC);

				}
			}
		}
	}
}