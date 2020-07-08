package com.incomm.cclp.service;

import java.sql.Connection;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilCashOutReversalService {

	public String[] invoke(ValueDTO valueDTO, Connection con) throws ServiceException;

}
