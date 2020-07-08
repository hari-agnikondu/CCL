/**
 * 
 */
package com.incomm.cclp.service;

import java.sql.SQLException;
import java.text.ParseException;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

/**
 * @author kalaivanip
 *
 */
public interface SpilCommonService {

	public String[] invoke(ValueDTO valueDto) throws ServiceException, SQLException, ParseException;

}
