package com.incomm.cclp.service;

import java.util.Map;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface CommonValidationsService {

	void cardStatusCheckValidation(ValueDTO valueDto) throws ServiceException;

	void expiryDateCheck(String cardExpiryDate, String tranExpiryDate, String validationType) throws ServiceException;

	void passiveStatusValidation(Map<String, String> valueObj) throws ServiceException;

	void instrumentTypeCheck(ValueDTO valueDto) throws ServiceException;

	void upcValidation(ValueDTO valueDto) throws ServiceException;

	void spillCvv2Validation(ValueDTO valueDto) throws ServiceException;

	void currencyCodeValidation(ValueDTO valueDto) throws ServiceException;

	void firstPartyThirdPartyCheck(ValueDTO valueDto) throws ServiceException;

	void purseValidityCheck(ValueDTO valueDto) throws ServiceException;

}
