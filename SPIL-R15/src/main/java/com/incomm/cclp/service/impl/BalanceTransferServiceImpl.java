/**
 * 
 */
package com.incomm.cclp.service.impl;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.dao.BalanceTransferDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

@Service("com.incomm.cclp.service.impl.BalanceTransferServiceImpl")
public class BalanceTransferServiceImpl implements SpilCommonService {

	private static final Logger logger = LogManager.getLogger(BalanceTransferServiceImpl.class);
	@Autowired
	BalanceTransferDAO balanceTransferDao;

	@Autowired
	private SpilDAO spilDAO;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException {
		String[] response = balanceTransferDao.balanceTransfer(valueDto);
		if (ResponseCodes.SUCCESS.equalsIgnoreCase(response[0]) && "OK".equalsIgnoreCase(response[1])) {
			spilDAO.updateUsageLimits(valueDto);
		}
		logger.info("Response for Balance transfer : ", response[0], response[1]);
		return response;
	}

	public boolean checkDuplicateRRN(String rrn, String targetCardNumber, String targetCustomerId) {

		return balanceTransferDao.checkDuplicateRRN(rrn, targetCardNumber, targetCustomerId)
			.equals("0") ? false : true;
	}
}
