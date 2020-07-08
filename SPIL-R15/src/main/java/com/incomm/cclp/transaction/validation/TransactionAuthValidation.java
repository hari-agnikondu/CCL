package com.incomm.cclp.transaction.validation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CommonValidationsService;
import com.incomm.cclp.service.TransactionValidationsService;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.constants.TransactionValidationContants;
import com.incomm.cclp.transaction.service.TransactionService;

@Service
public class TransactionAuthValidation {

	@Autowired
	TransactionService transactionService;

	@Autowired
	TransactionValidationsService transactionValidationsService;

	@Autowired
	CommonValidationsService commonValidationService;

	private static final Logger logger = LogManager.getLogger(CommonAuthValidation.class);

	public void validateTransactionauth(String processType, ValueDTO valueDto) throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		switch (processType) {

		case TransactionValidationContants.DEACTIVATED:
			transactionValidationsService.cardAlreadyDeactivatedCheck(valueDto);
			break;
		case TransactionValidationContants.CASHED_OUT:
			// TBD
			break;
		case TransactionValidationContants.UNLOCKED:
			// TBD
			break;

		case TransactionValidationContants.REDEEMED:
			transactionValidationsService.cardAlreadyRedeemedCheck(valueDto);
			break;
		case TransactionValidationContants.SUBSEQUENT_ACTIVATION_AS_RELOAD:
			// TBD
			break;
		case TransactionValidationContants.ALREADY_REVERSED:
			// TBD
			break;
		case TransactionValidationContants.DENOM_CHECK:
			transactionValidationsService.validateProductDenomCheck(valueDto);
			break;

		case TransactionValidationContants.RELOADABLE_FLAG_CHECK:
			transactionValidationsService.reloadApplicableCheck(valueDto);
			break;
		case TransactionValidationContants.REDEMPTION_DELAY_CHECK:
			valueDto.getValueObj()
				.put(ValueObjectKeys.REDEMPTION_DELAY_CHECK_AVAIL, TransactionValidationContants.REDEMPTION_DELAY_CHECK);
			break;
		default:
			// TBD
			break;
		}

		logger.debug(GeneralConstants.EXIT);

	}

}
