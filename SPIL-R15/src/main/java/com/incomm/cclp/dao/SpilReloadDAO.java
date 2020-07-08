package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilReloadDAO {

	public String[] invokeReload(ValueDTO valueDto) throws ServiceException;

}
