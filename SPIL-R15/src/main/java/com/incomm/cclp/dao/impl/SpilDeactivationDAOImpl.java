package com.incomm.cclp.dao.impl;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.SpilDeactivationDAO;
import com.incomm.cclp.dao.service.SpilDeactivationDAOService;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.RequestInfoService;

@Repository
public class SpilDeactivationDAOImpl implements SpilDeactivationDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	RequestInfoService requestInfoService;

	@Autowired
	SpilDeactivationDAOService spilDeactivationDAOService;

	private static final Logger logger = LogManager.getLogger(SpilDeactivationDAOImpl.class);

	@Override
	public String[] invokeDeactivation(ValueDTO valueDto) throws ServiceException, SQLException {
		logger.info("invokeDeActivation START >>>");

		String[] result = new String[8];

		try {

			RequestInfo req = requestInfoService.getRequestInfo(valueDto);

			ResponseInfo resp = spilDeactivationDAOService.doDeactivation(req);

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

			logger.info("invokeDeactivation END <<<");

			return result;

		} catch (ServiceException se) {
			logger.error("Error in Spil Deactivation Dao: " + se.getMessage(), se);
			throw se;

		} catch (Exception e) {
			logger.error("Error in Spil Deactivation Dao: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

	}

}
