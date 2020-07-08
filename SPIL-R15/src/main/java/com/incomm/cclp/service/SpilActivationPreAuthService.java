package com.incomm.cclp.service;

import java.sql.Connection;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilActivationPreAuthService {

	public String[] invoke(ValueDTO valueDto, Connection con) throws ServiceException;
}
