package com.incomm.cclp.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDeactivationRevDAO;
import com.incomm.cclp.dao.service.SpilDeactivationRevDAOService;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.dto.ResponseInfo;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.TransactionConstant;
import com.incomm.cclp.util.RequestInfoService;

@Repository
public class SpilDeactivationRevDAOImpl implements SpilDeactivationRevDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	@Autowired
	RequestInfoService requestInfoService;
	@Autowired
	SpilDeactivationRevDAOService spilDeactivationRevDAOService;

	private static final Logger logger = LogManager.getLogger(SpilDeactivationRevDAOImpl.class);

	@Override
	public String[] invokeSpilDeactivationRev(ValueDTO valueDto) throws ServiceException {

		String[] result = new String[5];

		try {
			valueDto.getValueObj()
				.replace(ValueObjectKeys.CREDIT_DEBIT_INDICATOR, decodeCreditDebitFlag(valueDto.getValueObj()
					.get(ValueObjectKeys.CREDIT_DEBIT_INDICATOR)));
			valueDto.getValueObj()
				.replace(ValueObjectKeys.TRANSACTIONDESC, TransactionConstant.DEACTIVATION_REVERSAL_TXN_DESC);

			RequestInfo req = requestInfoService.getRequestInfo(valueDto);
			ResponseInfo resp = spilDeactivationRevDAOService.doDeactivationRev(req);

			result[0] = resp.getRespCode();
			result[1] = resp.getErrMsg();
			result[2] = resp.getAuthId();
			result[3] = String.valueOf(resp.getAuthorizedAmt());
			result[4] = resp.getCurrCode();
			logger.info("responseCode: {}, responseMsg:{}, AuthorizedId: {}, AuthorizedAmt:{}, CurrencyCode: {}", result[0], result[1],
					result[2], result[3], result[4]);

			return result;
		}

		catch (Exception e) {
			logger.error("Error in Spil Deactivation Revesal Dao: " + e.getMessage(), e);
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
