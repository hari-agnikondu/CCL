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
import com.incomm.cclp.dao.SpilCashOutReversalDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

@Service("com.incomm.cclp.service.impl.SpilCashOutReversalServiceImpl")

public class SpilCashOutReversalServiceImpl implements SpilCommonService {

	private static final Logger logger = LogManager.getLogger(SpilCashOutReversalServiceImpl.class);

	@Autowired
	SpilCashOutReversalDAO spilCashOutReversalDAO;

	@Autowired
	private SpilDAO spilDao;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDTO) throws ServiceException {

		String[] response = spilCashOutReversalDAO.spilCashOutReversal(valueDTO);

		logger.info(
				"Response for card Redemption procedure: responseCode: {},"
						+ "response msg: {},Authorized Id: {}, Authorized Amt: {},currency Code: {} ",
				response[0], response[2], response[1], response[3], response[4]);

		if (response[0].equalsIgnoreCase(ResponseCodes.SUCCESS) && response[1].equalsIgnoreCase("OK")) {
			spilDao.updateUsageLimits(valueDTO);
		}

		return response;
	}

}
