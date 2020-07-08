package com.incomm.cclp.service;

import com.incomm.cclp.domain.BulkTransactionResponseFile;
import com.incomm.cclp.exception.ServiceException;

public interface SPILBatchService {

	BulkTransactionResponseFile callSPILTransaction(String xmlMsg) throws ServiceException;

}
