package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilPreValueInsertionDAO {

	public String[] spilPreValueInsertion(ValueDTO valueDto) throws ServiceException;

}
