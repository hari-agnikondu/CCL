package com.incomm.cclp.transaction.validation;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CommonValidationsService;
import com.incomm.cclp.service.FeeCalculationService;
import com.incomm.cclp.service.LimitValidationService;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionValidationContants;

@Service
public class CommonAuthValidation {

	private static final Logger logger = LogManager.getLogger(AuthorizationValidation.class);

	@Autowired
	CommonValidationsService commonValidationsService;

	@Autowired
	LimitValidationService limitValidationService;

	@Autowired
	FeeCalculationService feeCalculationService;

	public void validateCommonauth(String processKey, String validationType, ValueDTO valueDto) throws ServiceException {

		logger.debug(GeneralConstants.ENTER);

		logger.debug("Process Key : {}", processKey);

		switch (processKey) {

		case TransactionValidationContants.CARD_STATUS_CHECK:
			commonValidationsService.cardStatusCheckValidation(valueDto);
			break;

		case TransactionValidationContants.SP_NUMBER_TYPE_CHECK:
			commonValidationsService.instrumentTypeCheck(valueDto);
			break;

		case TransactionValidationContants.EXPIRY_DATE_CHECK:
			String cardExpiryDate = valueDto.getValueObj()
				.get(ValueObjectKeys.CARD_EXPDATE);
			String tranExpiryDate = valueDto.getValueObj()
				.get(ValueObjectKeys.EXP_DATE);
			commonValidationsService.expiryDateCheck(cardExpiryDate, tranExpiryDate, validationType);
			break;

		case TransactionValidationContants.PIN_VERIFICATION:
			// TBD
			break;
		case TransactionValidationContants.CVV2_VERIFICATION:
			commonValidationsService.spillCvv2Validation(valueDto);
			break;

		case TransactionValidationContants.TRACK1_VERIFICATION:
			// TBD
			break;

		case TransactionValidationContants.TRACK2_VERIFICATION:
			// TBD
			break;

		case TransactionValidationContants.PASSIVE_CARD_UPDATE:
			commonValidationsService.passiveStatusValidation(valueDto.getValueObj());
			break;

		case TransactionValidationContants.CARD_NUMBER_LENGTH:
			// TBD
			break;

		case TransactionValidationContants.LIMIT_CHECK:

			Map<String, Map<String, Object>> productAttributesMap = valueDto.getProductAttributes();
			Map<String, Object> productAttributes = productAttributesMap.get("Product");
			logger.info("Product Product Attributes {} ", productAttributes);
			if (productAttributes.containsKey("limitSupported")
					&& ("Enable".equalsIgnoreCase((String) productAttributes.get("limitSupported"))))
				limitValidationService.validateLimits(valueDto);
			break;

		case TransactionValidationContants.TRANSACTION_DATE_CHECK:
			// TBD
			break;

		case TransactionValidationContants.CURRENCY_CHECK:
			commonValidationsService.currencyCodeValidation(valueDto);
			break;

		case TransactionValidationContants.UPC_CHECK:
			commonValidationsService.upcValidation(valueDto);
			break;

		case TransactionValidationContants.FIRST_PARTY_THIRD_PARTY_CHECK:
			commonValidationsService.firstPartyThirdPartyCheck(valueDto);
			break;

		case TransactionValidationContants.FEE_CHECK:
			Map<String, Object> productAttr = valueDto.getProductAttributes()
				.get("Product");
			if (productAttr.containsKey("feesSupported") && ("Enable".equalsIgnoreCase((String) productAttr.get("feesSupported"))))
				feeCalculationService.feeCalculation(valueDto);
			break;

		case TransactionValidationContants.PURSE_VALIDITY_CHECK:
			commonValidationsService.purseValidityCheck(valueDto);
			break;

		default:
			// TBD
			break;
		}

		logger.debug(GeneralConstants.EXIT);

	}

}
