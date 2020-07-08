
package com.incomm.cclp.dao;

import java.sql.SQLException;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface SpilActivationDAO {

	public String[] invokeActivation(ValueDTO valueDto) throws ServiceException, SQLException;

}
