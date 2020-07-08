package com.incomm.cclp.service.impl;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilActivationRevDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RedemptionDelayService;
import com.incomm.cclp.service.SpilActivationRevService;
import com.incomm.cclp.service.SpilCommonService;
import com.incomm.cclp.util.Util;

@Service("com.incomm.cclp.service.impl.SpilActivationRevServiceImpl")

public class SpilActivationRevServiceImpl implements SpilCommonService {

	private static final Logger logger = LogManager.getLogger(SpilActivationRevService.class);

	@Autowired
	private SpilDAO spilDao;

	@Autowired
	SpilActivationRevDAO spilActivationRevDao;

	@Autowired
	RedemptionDelayService redemptionDelayService;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException {

		String[] response;

		response = spilActivationRevDao.invokeActivationRev(valueDto);
		logger.info("Response for Activation Reversal: responseCode: {}," + "response msg: {},Authorized Id: {}, Authorized Amt: {} ",
				response[0], response[1], response[2], response[3]);

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
