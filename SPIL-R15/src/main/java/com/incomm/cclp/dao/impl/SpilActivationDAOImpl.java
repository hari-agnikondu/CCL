package com.incomm.cclp.dao.impl;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.SpilActivationDAO;
import com.incomm.cclp.dao.service.SpilActivationDAOService;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.RequestInfoService;
import com.incomm.cclp.util.Util;

@Repository /* (("com.incomm.cclp.service.impl.SpilActivationServiceImpl")) */
public class SpilActivationDAOImpl implements SpilActivationDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SpilActivationDAOService spilActivationDAOService;
	@Autowired
	RequestInfoService requestInfoService;

	private static final Logger logger = LogManager.getLogger(SpilActivationDAOImpl.class);

	@Override
	public String[] invokeActivation(ValueDTO valueDto) throws ServiceException, SQLException {
		logger.info("invokeActivation START >>>");

		String[] result = new String[9];

		try {

			RequestInfo req = requestInfoService.getRequestInfo(valueDto);

			ResponseInfo resp = spilActivationDAOService.doActivation(req);

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
			result[8] = Util.getPurseAuthResp(resp.getAccountPurse());

			logger.info("invokeActivation END <<<");

			return result;

		} catch (ServiceException se) {
			logger.warn("Error in SpilActivationDAOImpl: " + se.getMessage(), se);
			throw se;

		} catch (Exception e) {
			logger.error("Error in SpilActivationDAOImpl: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

	}

}
