package com.incomm.cclp.dao.service;

import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.exception.ServiceException;

public interface AuthChecksService {
	public void authorizeTransaction(RequestInfo req) throws ServiceException;
}
