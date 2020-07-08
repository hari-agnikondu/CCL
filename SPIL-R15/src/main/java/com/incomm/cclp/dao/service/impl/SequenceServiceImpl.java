package com.incomm.cclp.dao.service.impl;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.exception.ServiceException;

@Service
public class SequenceServiceImpl implements SequenceService {
	@Autowired
	private EntityManager em;

	private static final Logger logger = LogManager.getLogger(SequenceServiceImpl.class);

	@Override
	public BigDecimal getNextTxnSeqId() {
		BigDecimal result = null;
		try {
			Query q = em.createNativeQuery("SELECT TRANSACTION_ID_SEQ.nextval from DUAL");
			result = (BigDecimal) q.getSingleResult();
			logger.info("getNextTxnSeqId: <<<" + result);
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_TRANSACTION_ID_SEQ, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_TRANSACTION_ID_SEQ, ResponseCodes.SYSTEM_ERROR);
		}
		return result;
	}

	@Override
	public BigDecimal getNextRecordSeqId() {
		try {
			Query q = em.createNativeQuery("SELECT RECORD_ID_SEQ.nextval from DUAL");
			return (BigDecimal) q.getSingleResult();
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_RECORD_ID_SEQ, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_RECORD_ID_SEQ, ResponseCodes.SYSTEM_ERROR);
		}
	}

	@Override
	public String getNextAuthSeqId() {
		try {
			Query q = em.createNativeQuery("SELECT lpad(AUTH_ID_SEQ.nextval,6,0) from DUAL");
			return (String) q.getSingleResult();
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_AUTH_ID_SEQ, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_AUTH_ID_SEQ, ResponseCodes.SYSTEM_ERROR);
		}
	}

	@Override
	public Long getNextAccountPurseId() {
		try {
			Query query = em.createNativeQuery("SELECT SEQ_ACCOUNT_PURSE_ID.nextval from DUAL");
			BigDecimal nextValue = (BigDecimal) query.getSingleResult();
			return nextValue.longValue();
		} catch (Exception e) {
			logger.error(SpilExceptionMessages.ERROR_ACCOUNT_PURSE_ID_SEQ + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_ACCOUNT_PURSE_ID_SEQ, ResponseCodes.SYSTEM_ERROR);
		}
	}

}
