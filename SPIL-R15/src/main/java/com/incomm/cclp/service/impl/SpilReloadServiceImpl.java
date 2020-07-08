/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.sql.SQLException;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dao.SpilReloadDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

/**
 * Spil Activation Service provides all the Service operations for Spil Activation Transactions.
 * 
 * @author Lavanya
 */

@Service("com.incomm.cclp.service.impl.SpilReloadServiceImpl")
public class SpilReloadServiceImpl implements SpilCommonService {

	private static final Logger logger = LogManager.getLogger(SpilReloadServiceImpl.class);

	@Autowired
	SpilReloadDAO spilReloadDao;

	@Autowired
	private SpilDAO spilDAO;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException, SQLException {
		String[] response = spilReloadDao.invokeReload(valueDto);
		if (ResponseCodes.SUCCESS.equalsIgnoreCase(response[0]) && "OK".equalsIgnoreCase(response[1])) {
			spilDAO.updateUsageLimits(valueDto);
			Map<String, String> valueObj = valueDto.getValueObj();
			spilDAO.updateUsageLimits(valueDto);
			String accountId = valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID);
			String purBal = spilDAO.getPurseBalance(accountId);
			valueDto.getValueObj()
				.put(ValueObjectKeys.PUR_BAL, purBal);
		}
		logger.info("Response for SPIL Reload : ", response[0], response[1]);
		return response;
	}

}
