/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dao.SpilTransactionHistoryDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

@Service("com.incomm.cclp.service.impl.SpilTransactionHistoryServiceImpl")

public class SpilTransactionHistoryServiceImpl implements SpilCommonService {

	private static final Logger logger = LogManager.getLogger(SpilTransactionHistoryServiceImpl.class);

	@Autowired
	SpilTransactionHistoryDAO spilTransactionHistoryDAO;

	@Autowired
	private SpilDAO spilDao;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException, ParseException {

		Map<String, String> valueObj = valueDto.getValueObj();

		String startDate = valueObj.get("StartDate");
		String endDate = valueObj.get("EndDate");

		if (!(Objects.isNull(startDate) || Objects.isNull(endDate) || endDate.equals("") || startDate.equals(""))) {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			Date sDate = simpleDateFormat.parse(startDate);
			Date eDate = simpleDateFormat.parse(endDate);

			if (sDate.compareTo(eDate) > 0) {
				throw new ServiceException(SpilExceptionMessages.START_DATE_IS_LESS_THAN_END_DATE,
						ResponseCodes.START_DATE_IS_LESS_THAN_END_DATE);
			}

		}

		String[] response = spilTransactionHistoryDAO.spilTransactionHistory(valueDto);

		logger.info(
				"Response for spil Transaction history procedure: responseCode: {},"
						+ "response msg: {},Authorized Id: {}, Authorized Amt: {},currency Code: {} ",
				response[0], response[1], response[2], response[3], response[4]);

		if (response[0].equalsIgnoreCase(ResponseCodes.SUCCESS) && response[1].equalsIgnoreCase("OK")) {
			spilDao.updateUsageLimits(valueDto);
		}

		return response;
	}

}
