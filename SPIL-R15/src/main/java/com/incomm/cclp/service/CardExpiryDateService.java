package com.incomm.cclp.service;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface CardExpiryDateService {

	String calculateExpiryDate(ValueDTO valueDto) throws ServiceException;

}
