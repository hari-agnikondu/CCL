package com.incomm.cclp.service;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface TransactionValidationsService {

	public void validateProductDenomCheck(ValueDTO valueDto) throws ServiceException;

	public void cardAlreadyRedeemedCheck(ValueDTO valueDto) throws ServiceException;

	public void cardAlreadyDeactivatedCheck(ValueDTO valueDto) throws ServiceException;

	public void reloadApplicableCheck(ValueDTO valueDto) throws ServiceException;

	public void partialAuthCheck(ValueDTO valueDto) throws ServiceException;
}
