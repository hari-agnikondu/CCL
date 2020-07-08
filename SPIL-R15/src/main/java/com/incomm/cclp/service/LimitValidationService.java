package com.incomm.cclp.service;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface LimitValidationService {

	void validateLimits(ValueDTO valueDto) throws ServiceException;

	void resetLimits(ValueDTO valueDto) throws ServiceException;

	void updateCardLimitAttributes(ValueDTO valueDto) throws ServiceException;

	void revertCardLimitAttributes(ValueDTO valueDto) throws ServiceException;

}
