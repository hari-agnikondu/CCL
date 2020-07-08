package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilTransactionHistoryDAO {

	public String[] spilTransactionHistory(ValueDTO valueDto) throws ServiceException;

}
