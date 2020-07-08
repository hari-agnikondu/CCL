package com.incomm.cclp.service.impl;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.dao.TransactionFlexDAO;
import com.incomm.cclp.service.TransactionFlexService;

@Service
public class TransactionFlexServiceImpl implements TransactionFlexService {
	
	@Autowired
	TransactionFlexDAO transactionflexDao;
	
	private static final Logger logger = LogManager.getLogger(TransactionFlexServiceImpl.class);
	
	@Override
	public int updateTransactionFlexDesc(Map<String, String> txnFlexDesc) {
		logger.info(CCLPConstants.ENTER);
		txnFlexDesc.entrySet().stream().forEach(e -> 
		  transactionflexDao.updateTransactionFlexDesc(e.getKey(),e.getValue())
		);  
		logger.info(CCLPConstants.EXIT);
		return 1;
	}

}
