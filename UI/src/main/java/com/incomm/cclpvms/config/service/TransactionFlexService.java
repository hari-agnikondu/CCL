package com.incomm.cclpvms.config.service;

import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface TransactionFlexService {

	public ResponseDTO updateTransFlexConfig(Map<String, String> txnDesc) throws ServiceException ;

	

}
