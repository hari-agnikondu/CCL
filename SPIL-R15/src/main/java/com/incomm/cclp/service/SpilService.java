/**
 * 
 */
package com.incomm.cclp.service;

import com.incomm.cclp.exception.ServiceException;

public interface SpilService {

	public String callSPILTransaction(String xmlMsg) throws ServiceException;

}
