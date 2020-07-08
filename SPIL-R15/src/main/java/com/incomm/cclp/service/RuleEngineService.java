package com.incomm.cclp.service;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface RuleEngineService {

	void callTransactionFilter(ValueDTO valueDto) throws ServiceException;

}
