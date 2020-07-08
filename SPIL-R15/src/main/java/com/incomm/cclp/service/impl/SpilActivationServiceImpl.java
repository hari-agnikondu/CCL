/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.math.BigDecimal;
import java.sql.SQLException;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilActivationDAO;
import com.incomm.cclp.dao.SpilDAO;
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
@Service("com.incomm.cclp.service.impl.SpilActivationServiceImpl")
public class SpilActivationServiceImpl implements SpilCommonService {

	private static final Logger logger = LogManager.getLogger(SpilActivationServiceImpl.class);

	@Autowired
	private SpilDAO spilDao;

	@Autowired
	SpilActivationDAO spilActivationDao;

	@Autowired
	RedemptionDelayService redemptionDelayService;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException, SQLException {

		String[] response = null;
		String cardStatus = valueDto.getValueObj()
			.get(ValueObjectKeys.CARD_CARDSTAT);
		String consumedStatus = String.valueOf(valueDto.getProductAttributes()
			.get("General")
			.get("consumedFlag"));

		logger.debug("Current card status: {}" + cardStatus);
		logger.debug("Consumed status flag value : {}", consumedStatus);

		if ("17".equalsIgnoreCase(cardStatus) && !"true".equalsIgnoreCase(consumedStatus)) {
			logger.error("Transaction declined due to consumed falg value is {}", consumedStatus);
			throw new ServiceException(SpilExceptionMessages.INACTIVE_CARD, ResponseCodes.INACTIVE_CARD);

		}

		BigDecimal txnAmount = new BigDecimal(valueDto.getValueObj()
			.get(ValueObjectKeys.SPIL_TRAN_AMT));
		if (txnAmount.compareTo(BigDecimal.ZERO) > 0) {
			valueDto.getValueObj()
				.replace(ValueObjectKeys.FIRST_TIMETOPUP_FLAG, "Y");
		}

		response = spilActivationDao.invokeActivation(valueDto);

		logger.info(
				"Response for spil  activation procedure: responseCode: {},"
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