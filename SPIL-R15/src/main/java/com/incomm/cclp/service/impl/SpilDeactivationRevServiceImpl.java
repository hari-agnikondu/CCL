package com.incomm.cclp.service.impl;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dao.SpilDeactivationRevDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RedemptionDelayService;
import com.incomm.cclp.service.SpilCommonService;
import com.incomm.cclp.util.Util;

@Service("com.incomm.cclp.service.impl.SpilDeactivationRevServiceImpl")

public class SpilDeactivationRevServiceImpl implements SpilCommonService {

	@Autowired
	SpilDeactivationRevDAO spilDeactivationRevDAO;
	@Autowired
	private SpilDAO spilDao;
	@Autowired
	RedemptionDelayService redemptionDelayService;

	private static final Logger logger = LogManager.getLogger(SpilDeactivationRevServiceImpl.class);

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException {

		String[] response = spilDeactivationRevDAO.invokeSpilDeactivationRev(valueDto);

		logger.info(
				"Response for card Redemption procedure: responseCode: {},"
						+ "response msg: {},Authorized Id: {}, Authorized Amt: {},currency Code: {} ",
				response[0], response[2], response[1], response[3], response[4]);

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
