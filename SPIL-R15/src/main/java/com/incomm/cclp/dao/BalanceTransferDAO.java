
package com.incomm.cclp.dao;

import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;

public interface BalanceTransferDAO {

	public String[] balanceTransfer(ValueDTO valueDto) throws ServiceException;

	public String checkDuplicateRRN(String rrn, String targetCardNumber, String targetCustomerId) throws ServiceException;

}
