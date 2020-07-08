/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.sql.SQLException;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dao.SpilDeactivationDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RedemptionDelayService;
import com.incomm.cclp.service.SpilCommonService;
import com.incomm.cclp.util.Util;

/**
 * Spil Activation Service provides all the Service operations for Spil Activation Transactions.
 * 
 * @author Nawaz
 */

@Service("com.incomm.cclp.service.impl.SpilDeactivationServiceImpl")
public class SpilDeactivationServiceImpl implements SpilCommonService {

	private static final Logger logger = LogManager.getLogger(SpilDeactivationServiceImpl.class);

	@Autowired
	SpilDeactivationDAO spilDeactivationDAO;

	@Autowired
	private SpilDAO spilDao;

	@Autowired
	RedemptionDelayService redemptionDelayService;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException, SQLException {

		String[] response = spilDeactivationDAO.invokeDeactivation(valueDto);

		logger.info(
				"Response for spil  Deactivation procedure: responseCode: {},"
						+ "response msg: {},Authorized Id: {}, Authorized Amt: {},currency Code: {}, card Status: {}, "
						+ "Card Number: {}, Serial Number: {}",
				response[0], response[1], response[2], response[3], response[4], response[5], Util.getMaskCardNum(response[6]),
				response[7]);

		if (response[0].equalsIgnoreCase(ResponseCodes.SUCCESS) && response[1].equalsIgnoreCase("OK")) {
			spilDao.updateUsageLimits(valueDto);
			redemptionDelayService.redemptionDelayCheck(valueDto);
			spilDao.updateUsageFee(valueDto);
			if (!Util.isEmpty(valueDto.getValueObj()
				.get(ValueObjectKeys.CURRENT_FEE_ACCRUED)) && !Util.isEmpty(
						valueDto.getValueObj()
							.get(ValueObjectKeys.USAGE_FEE_PERIOD))) {
				spilDao.updateMonthlyFeeCap(valueDto.getValueObj());
			}
		}

		return response;

	}

}
