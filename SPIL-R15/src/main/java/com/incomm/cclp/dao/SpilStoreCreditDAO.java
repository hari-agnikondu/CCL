package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilStoreCreditDAO {

	String[] invoke(ValueDTO valueDto) throws ServiceException;
}
