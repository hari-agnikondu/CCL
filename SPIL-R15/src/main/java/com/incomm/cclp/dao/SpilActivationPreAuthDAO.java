package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilActivationPreAuthDAO {

	public String[] invoke(ValueDTO valueDto) throws ServiceException;

}
