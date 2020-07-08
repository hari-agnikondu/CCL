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
import com.incomm.cclp.dao.SpilCashOutDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

@Service("com.incomm.cclp.service.impl.SpilCashOutServiceImpl")
public class SpilCashOutServiceImpl implements SpilCommonService {

	private static final Logger logger = LogManager.getLogger(SpilCashOutServiceImpl.class);

	@Autowired
	private SpilDAO spilDao;

	@Autowired
	SpilCashOutDAO spilCashOutDAO;

	@Override
	@Transactional

	public String[] invoke(ValueDTO valueDTO) throws ServiceException {

		String[] response = spilCashOutDAO.spilCashOut(valueDTO);

		logger.info(
				"Response for spil cashout  procedure: responseCode: {},"
						+ "response msg: {},Authorized Id: {}, Authorized Amt: {},currency Code: {} ",
				response[0], response[2], response[1], response[3], response[4]);

		if (response[0].equalsIgnoreCase(ResponseCodes.SUCCESS) && response[1].equalsIgnoreCase("OK")) {
			spilDao.updateUsageLimits(valueDTO);
		}

		return response;
	}

}
