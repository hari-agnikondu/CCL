package com.incomm.cclp.service;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilActivationRevService {

	public String[] invoke(ValueDTO valueDto) throws ServiceException;
}
