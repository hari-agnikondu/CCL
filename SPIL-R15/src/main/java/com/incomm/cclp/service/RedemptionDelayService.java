package com.incomm.cclp.service;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface RedemptionDelayService {

//	public void redemptionLockCountCheck(ValueDTO valueDto) throws ServiceException;

	public void redemptionDelayCheck(ValueDTO valueDto) throws ServiceException;

//	public void redemptionDelayedLoadCheck(ValueDTO valueDto) throws ServiceException;

}
