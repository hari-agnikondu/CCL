package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilCashOutReversalDAO {
	public String[] spilCashOutReversal(ValueDTO valueDTO) throws ServiceException;

}
