/**
 * 
 */
package com.incomm.cclp.service.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.dao.SpilCardToCardTransferDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

/**
 * Spil Activation Service provides all the Service operations for Spil Activation Transactions.
 * 
 * @author Lavanya
 */

@Service("com.incomm.cclp.service.impl.SpilCardToCardTransferServiceImpl")
public class SpilCardToCardTransferServiceImpl implements SpilCommonService {

	@Autowired
	SpilCardToCardTransferDAO spilCardToCardTransferDao;
	@Autowired
	private SpilDAO spilDAO;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException {
		String[] response = spilCardToCardTransferDao.spilCardToCardTransfer(valueDto);
		if (ResponseCodes.SUCCESS.equalsIgnoreCase(response[0]) && "OK".equalsIgnoreCase(response[1])) {
			spilDAO.updateUsageLimits(valueDto);
		}
		return response;
	}

}
