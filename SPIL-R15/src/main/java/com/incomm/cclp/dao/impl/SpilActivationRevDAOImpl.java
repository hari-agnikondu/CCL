package com.incomm.cclp.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilActivationRevDAO;
import com.incomm.cclp.dao.service.SpilActivationRevDAOService;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.util.RequestInfoService;
import com.incomm.cclp.util.Util;

@Repository
public class SpilActivationRevDAOImpl implements SpilActivationRevDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	SpilActivationRevDAOService spilActivationRevDAOService;
	@Autowired
	RequestInfoService requestInfoService;

	private static final Logger logger = LogManager.getLogger(SpilActivationRevDAOImpl.class);

	@Override
	public String[] invokeActivationRev(ValueDTO valueDto) {
		String[] result = new String[9];
		logger.info("invokeActivationRev START >>>");
		try {
			valueDto.getValueObj()
				.replace(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, decodeCreditDebitFlag(valueDto.getValueObj()
					.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR)));
			valueDto.getValueObj()
				.replace(ValueObjectKeys.TRANSACTIONDESC, TransactionConstant.ACTIVATION_REVERSAL_TXN_DESC);
			RequestInfo req = requestInfoService.getRequestInfo(valueDto);
			ResponseInfo resp = spilActivationRevDAOService.doActivationRev(req);

			logger.info("RESP --->RESP CODE---->" + resp.getRespCode());
			logger.info("RESP --->RESP ERROR MSG---->" + resp.getErrMsg());

			result[0] = resp.getRespCode();
			result[1] = resp.getErrMsg();
			result[2] = resp.getAuthId();
			result[3] = String.valueOf(resp.getAuthorizedAmt());
			result[4] = Util.getPurseAuthResp(resp.getAccountPurse());

			logger.info("invokeActivationRev END <<<");
			return result;
		} catch (ServiceException se) {
			logger.error(se.getMessage());
			throw se;
		} catch (Exception e) {
			logger.error("Error in SpilActivationRevDAOImpl: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
	}

	public String decodeCreditDebitFlag(String cr_dr_flag) {
		if (TransactionConstant.CREDIT_CARD.equalsIgnoreCase(cr_dr_flag)) {
			return TransactionConstant.DEBIT_CARD;
		} else if (TransactionConstant.DEBIT_CARD.equalsIgnoreCase(cr_dr_flag)) {
			return TransactionConstant.CREDIT_CARD;
		} else {
			return cr_dr_flag;
		}
	}
}
