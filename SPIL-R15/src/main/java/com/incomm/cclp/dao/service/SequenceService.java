package com.incomm.cclp.dao.service;

import java.math.BigDecimal;

import com.incomm.cclp.exception.ServiceException;

public interface SequenceService {
	public BigDecimal getNextTxnSeqId() throws ServiceException;

	public BigDecimal getNextRecordSeqId() throws ServiceException;

	public String getNextAuthSeqId() throws ServiceException;

	public Long getNextAccountPurseId() throws ServiceException;
}
