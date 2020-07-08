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
import com.incomm.cclp.dao.SpilBalanceInquiryDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

@Service("com.incomm.cclp.service.impl.SpilBalanceInquiryServiceImpl")
public class SpilBalanceInquiryServiceImpl implements SpilCommonService {

	@Autowired
	SpilBalanceInquiryDAO spilBalanceInquiryDAO;

	@Autowired
	private SpilDAO spilDao;

	private static final Logger logger = LogManager.getLogger(SpilBalanceInquiryServiceImpl.class);

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException, SQLException {

		String[] response = spilBalanceInquiryDAO.spilBalanceInquiry(valueDto);

		logger.info(
				"Response for card Redemption procedure: responseCode: {},"
						+ "response msg: {},Authorized Id: {}, Authorized Amt: {},currency Code: {}, card status: {} ",
				response[0], response[2], response[1], response[3], response[4], response[5]);

		if (response[0].equalsIgnoreCase(ResponseCodes.SUCCESS) && response[1].equalsIgnoreCase("OK")) {
			Map<String, String> valueObj = valueDto.getValueObj();
			spilDao.updateUsageLimits(valueDto);
			String accountId = valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID) == null ? "" : valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID);
			String purBal = spilDao.getPurseBalance(accountId);

			valueDto.getValueObj()
				.put(ValueObjectKeys.PUR_BAL, purBal);
		}

		return response;

	}

}