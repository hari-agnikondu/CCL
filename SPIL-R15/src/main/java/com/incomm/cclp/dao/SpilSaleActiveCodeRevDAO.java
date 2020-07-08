package com.incomm.cclp.dao;

import java.text.ParseException;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilSaleActiveCodeRevDAO {

	public String[] invokeSaleActiveCodeRev(ValueDTO valueDto) throws ParseException, ServiceException;

}
