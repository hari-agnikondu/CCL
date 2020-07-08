package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilCashOutDAO {

	public String[] spilCashOut(ValueDTO valueDTO) throws ServiceException;

}
