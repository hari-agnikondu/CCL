package com.incomm.cclp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CardExpiryDateService;
import com.incomm.cclp.service.ProductService;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.util.Util;

@Service
public class CardExpiryDateServiceImpl implements CardExpiryDateService {

	@Autowired
	ProductService productService;

	private static final Logger logger = LogManager.getLogger(CardExpiryDateServiceImpl.class);

	@Override
	public String calculateExpiryDate(ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);
		int validityPeriod = 0;
		String validityPeriodFormat = "";
		java.util.Date activeFromDate = null;
		String expiryDate = null;
		String activeFrom = "";
		String monthEndExpiryDateFlag = "";
		try {

			Map<String, Object> productAttributes = null;
			productAttributes = valueDto.getProductAttributes()
				.get(ValueObjectKeys.PRODUCT);

			// Get validity period from PRODUCT
			if (productAttributes.containsKey(ValueObjectKeys.VALIDITY_PERIOD)
					&& !Util.isEmpty(String.valueOf(productAttributes.get(ValueObjectKeys.VALIDITY_PERIOD)))) {
				validityPeriod = Integer.valueOf(productAttributes.get(ValueObjectKeys.VALIDITY_PERIOD)
					.toString());
			}

			// Get validityPeriodFormat from PRODUCT
			if (productAttributes.containsKey(ValueObjectKeys.VALIDITY_PERIOD_FORMAT)
					&& !Util.isEmpty(String.valueOf(productAttributes.get(ValueObjectKeys.VALIDITY_PERIOD_FORMAT)))) {
				validityPeriodFormat = productAttributes.get(ValueObjectKeys.VALIDITY_PERIOD_FORMAT)
					.toString();
			}

			// Get month end expiry flag from PRODUCT
			if (productAttributes.containsKey(ValueObjectKeys.MONTH_END_EXPIRY)
					&& !Util.isEmpty(String.valueOf(productAttributes.get(ValueObjectKeys.MONTH_END_EXPIRY)))) {
				monthEndExpiryDateFlag = productAttributes.get(ValueObjectKeys.MONTH_END_EXPIRY)
					.toString();
			}

			// Get activeFrom from PRODUCT
			if (productAttributes.containsKey(ValueObjectKeys.ACTIVE_FROM)
					&& !Util.isEmpty(String.valueOf(productAttributes.get(ValueObjectKeys.ACTIVE_FROM)))) {
				activeFrom = productAttributes.get(ValueObjectKeys.ACTIVE_FROM)
					.toString();
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				activeFromDate = sdf.parse(activeFrom);
			} else {
				logger.error("Configuration Error: active from date not set for the product.");
				throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
			}

			// Calculate expiry date
			Calendar cal = Calendar.getInstance();
			cal.setTime(activeFromDate);
			if (GeneralConstants.HOURS.equals(validityPeriodFormat)) {
				cal.add(Calendar.HOUR, validityPeriod);
			} else if (GeneralConstants.DAYS.equals(validityPeriodFormat)) {
				cal.add(Calendar.DATE, validityPeriod);
			} else if (GeneralConstants.WEEKS.equals(validityPeriodFormat)) {
				cal.add(Calendar.DATE, 7 * validityPeriod);
			} else if (GeneralConstants.MONTHS.equals(validityPeriodFormat)) {
				cal.add(Calendar.MONTH, validityPeriod);
				if (ValueObjectKeys.MONTH_END_EXPIRY_ENABLE.equals(monthEndExpiryDateFlag)) {
					int lastDate = cal.getActualMaximum(Calendar.DATE);
					cal.set(Calendar.DATE, lastDate);
				}
			} else if (GeneralConstants.YEARS.equals(validityPeriodFormat)) {
				cal.add(Calendar.YEAR, validityPeriod);
				if (ValueObjectKeys.MONTH_END_EXPIRY_ENABLE.equals(monthEndExpiryDateFlag)) {
					int lastDate = cal.getActualMaximum(Calendar.DATE);
					cal.set(Calendar.DATE, lastDate);
				}
			}
			expiryDate = new SimpleDateFormat("dd-MMM-yy").format(cal.getTime());

		}

		catch (Exception e) {
			logger.error("Exception in calculateExpiryDate:" + e);
			throw new ServiceException(SpilExceptionMessages.ERROR_CALCULATION_EXPIRY_DATE, ResponseCodes.SYSTEM_ERROR);
		}
		logger.debug(GeneralConstants.EXIT);
		return expiryDate;
	}

}