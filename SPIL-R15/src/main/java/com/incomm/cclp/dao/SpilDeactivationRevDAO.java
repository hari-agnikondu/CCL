package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilDeactivationRevDAO {

	public String[] invokeSpilDeactivationRev(ValueDTO valueDto) throws ServiceException;

}
