package com.incomm.cclp.service;

import java.sql.SQLException;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilBalanceInquiryService {

	public String[] invoke(ValueDTO valueDto) throws SQLException, ServiceException;

}
