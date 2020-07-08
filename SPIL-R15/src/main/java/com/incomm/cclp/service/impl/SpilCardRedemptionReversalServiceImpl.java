package com.incomm.cclp.service.impl;

import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilCardRedemptionReversalDAO;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

@Service("com.incomm.cclp.service.impl.SpilCardRedemptionReversalServiceImpl")
public class SpilCardRedemptionReversalServiceImpl implements SpilCommonService {

	@Autowired
	private SpilDAO spilDAO;

	@Autowired
	SpilCardRedemptionReversalDAO spilCardRedemptionReversaldao;

	private static final Logger logger = LogManager.getLogger(SpilCardRedemptionReversalServiceImpl.class);

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException {

		logger.debug("ENTER");

		String[] response = spilCardRedemptionReversaldao.invoke(valueDto);
		logger.info(
				"Response for card Redemption procedure: responseCode: {},"
						+ "response msg: {},Authorized Id: {},  Balance: {},Authorized Amt: {},currency Code: {} ",
				response[0], response[1], response[2], response[3], response[4], response[5]);

		if (response[1].equalsIgnoreCase("OK")) {

			Map<String, String> valueObj = valueDto.getValueObj();
			spilDAO.updateUsageLimits(valueDto);
			String accountId = valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID);
			String purBal = spilDAO.getPurseBalance(accountId);
			valueDto.getValueObj()
				.put(ValueObjectKeys.PUR_BAL, purBal);

		}
		logger.debug("EXIT");
		return response;
	}

}
