package com.incomm.cclp.account.domain.validator;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.account.domain.model.CardStatusType;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.impl.CommonValidationsServiceImpl;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CardValidator {

	@Autowired
	private CommonValidationsServiceImpl commonValidations;

	void validateCardStatus(ValueDTO valueDto) {
		commonValidations.cardStatusCheckValidation(valueDto);
	}

	void validateCardExpDate(ValueDTO valueDto, String validationType) {
		String cardExpiryDate = valueDto.getValueObj()
			.get(ValueObjectKeys.CARD_EXPDATE);
		String tranExpiryDate = valueDto.getValueObj()
			.get(ValueObjectKeys.EXP_DATE);
		commonValidations.expiryDateCheck(cardExpiryDate, tranExpiryDate, validationType);
	}

	void validatePassiveUpdate(Map<String, String> valueObj) {
		commonValidations.passiveStatusValidation(valueObj);
	}

	public void validateTransactionAllowed(Map<String, String> valueObj) {
		commonValidations.transactionAllowedCheck(valueObj.get(ValueObjectKeys.TRANS_CODE), valueObj.get(ValueObjectKeys.SPNUMBER));
	}

	public void validateTargetCardStatusForCardSwap(CardStatusType cardStatusType) {
		if (cardStatusType != CardStatusType.INACTIVE) {
			log.info("target card is in invalid state:{}", cardStatusType);
			throw new ServiceException(SpilExceptionMessages.ERROR_INVALID_CARD_STATUS, ResponseCodes.INVALID_TARGET_CARD_STATUS);
		}
	}

	public void validateProductIds(long sourceCardProductId, long targetCardProductId) {
		if (sourceCardProductId != targetCardProductId) {
			log.info("product id does not match between source card:{} and target cards:{}", sourceCardProductId, targetCardProductId);
			throw new ServiceException(SpilExceptionMessages.PRODUCT_MISMATCH, ResponseCodes.PRODUCT_MISMATCH);
		}
	}

}
