package com.incomm.cclp.dao;

import java.sql.SQLException;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilDeactivationDAO {

	public String[] invokeDeactivation(ValueDTO valueDto) throws ServiceException, SQLException;

}
