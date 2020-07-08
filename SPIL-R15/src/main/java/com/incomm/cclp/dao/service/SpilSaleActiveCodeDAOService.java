package com.incomm.cclp.dao.service;

import java.text.ParseException;

import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.exception.ServiceException;

public interface SpilSaleActiveCodeDAOService {
	public ResponseInfo doSaleActiveCode(RequestInfo req) throws ServiceException, ParseException;
}
