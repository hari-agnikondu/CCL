
package com.incomm.cclp.transaction.controller;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.bean.SpilResponseCode;
import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;
import com.incomm.cclp.transaction.bean.TransactionInputValidation;
import com.incomm.cclp.transaction.bean.Transactions;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.transaction.service.TransactionService;
import com.incomm.cclp.transaction.service.impl.LocalCacheServiceImpl;

import io.swagger.annotations.Api;

/**
 * Transaction Controller provides all the REST operations for Transactions.
 * 
 * @author venkateshgaddam
 */
@RestController
@RequestMapping("/transaction")
@Api(value = "Transaction")
public class TransactionController {

	@Autowired
	TransactionService transactionService;

	@Autowired
	LocalCacheServiceImpl localCacheService;

	// the logger
	private static final Logger logger = LogManager.getLogger(TransactionController.class);

	/**
	 * Rest end point to refresh the transaction Input validations into local cache
	 */
	@RequestMapping(value = "/txnInputValidation", method = RequestMethod.POST)
	public void transactionInputValidations() throws ServiceException {
		logger.debug("ENTER");
		Map<String, List<TransactionInputValidation>> inputValidationsMap = transactionService.loadTxnInputValidations("01");
		localCacheService.setInputValidations(inputValidationsMap);
		logger.debug("Exit");
	}

	@RequestMapping(value = "/updateTxnAuthDefinition", method = RequestMethod.POST)
	public void updateTransactionAuthDefinitions() {
		logger.debug(GeneralConstants.ENTER);

		Map<String, List<TransactionAuthDefinition>> mapTxnAuthDefinitions = transactionService.getAllTransactionAuthDefinitions();

		Map<String, List<TransactionAuthDefinition>> updatedTxnAuthDefinitions = localCacheService
			.updateOrGetAllTransactionAuthDefinitions(mapTxnAuthDefinitions);

		logger.info("Transaction Auth validations to LOCAL Cache updated successfully with values {}: ", updatedTxnAuthDefinitions);

		logger.debug(GeneralConstants.EXIT);
	}

	@RequestMapping(value = "/updateSpilResponseCode", method = RequestMethod.POST)
	public void updateSpilResponseCode() throws ServiceException {
		logger.debug("ENTER");
		Map<String, SpilResponseCode> mapSpilResponseCode = transactionService.getSpilResponseCodes("01");
		localCacheService.setSpilResponseCode(mapSpilResponseCode);
		logger.debug("Exit");
	}

	@RequestMapping(value = "/updateTransactionCode", method = RequestMethod.POST)
	public void updateSpilTxnCode() throws ServiceException {
		logger.debug("ENTER");
		Map<String, Transactions> mapSpilTxnCode = transactionService.getSpilTransactionCodes("01");
		localCacheService.setTransactionMapping(mapSpilTxnCode);
		logger.debug("Exit");
	}

}
