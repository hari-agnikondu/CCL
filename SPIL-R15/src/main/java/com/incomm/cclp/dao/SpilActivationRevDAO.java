package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilActivationRevDAO {

	public String[] invokeActivationRev(ValueDTO valueDto) throws ServiceException;

}
