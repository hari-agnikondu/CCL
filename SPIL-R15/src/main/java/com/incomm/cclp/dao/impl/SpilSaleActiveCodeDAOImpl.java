package com.incomm.cclp.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.CardStatusConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.SpilSaleActiveCodeDAO;
import com.incomm.cclp.dao.service.SpilSaleActiveCodeDAOService;
import com.incomm.cclp.dao.service.SpilSaleActiveCodeRevDaoService;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.RequestInfoService;

@Repository
public class SpilSaleActiveCodeDAOImpl implements SpilSaleActiveCodeDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SpilSaleActiveCodeDAOService spilSaleActiveCodeService;
	@Autowired
	RequestInfoService requestInfoService;

	@Autowired
	SpilSaleActiveCodeRevDaoService spilSaleActiveCodeRevService;

	private static final Logger logger = LogManager.getLogger(SpilSaleActiveCodeDAO.class);

	@Override
	public String[] invokeSaleActiveCode(ValueDTO valueDto) {

		String[] result = new String[9];

		logger.info("invokeSaleActiveCode START >>>");

		try {

			RequestInfo req = requestInfoService.getRequestInfo(valueDto);
			if (CardStatusConstants.DIGITAL_IN_PROCESS.equalsIgnoreCase(req.getCardStatus())) {
				req.setCardStatus(CardStatusConstants.PRINTER_SENT);
			}
			ResponseInfo resp = spilSaleActiveCodeService.doSaleActiveCode(req);

			logger.info("RESP --->RESP CODE---->" + resp.getRespCode());
			logger.info("RESP --->RESP ERROR MSG---->" + resp.getErrMsg());

			result[0] = resp.getRespCode();
			result[1] = resp.getErrMsg();
			result[2] = resp.getAuthId();
			result[3] = String.valueOf(resp.getAuthorizedAmt());
			result[4] = resp.getCurrCode();
			result[5] = resp.getCardStatus();
			result[6] = resp.getCardNumber();
			result[7] = String.valueOf(resp.getSerialNumber());
			result[8] = resp.getDigitalPin();
			logger.info("invokeSaleActiveCode END <<<");
			return result;
		} catch (ServiceException se) {
			logger.error("Error in Spil SaleActiveCode Dao: " + se.getMessage(), se);
			throw se;

		} catch (Exception e) {
			logger.error("Error in Spil SaleActiveCode Dao: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

	}
}
