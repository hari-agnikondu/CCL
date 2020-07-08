
package com.incomm.cclp.controller;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilService;

import io.swagger.annotations.Api;

/**
 * Spil Controller provides all the REST operations for Spil Transactions.
 */
@RestController
@Api(value = "Spil Transactions")
public class SpilController {

	@Autowired
	SpilService spilService;

	// the logger
	private static final Logger logger = LogManager.getLogger(SpilController.class);

	/**
	 * Rest end point of spil transactions
	 * 
	 * @param XML requestMessage.
	 * @return XML response message.
	 * @throws SQLException
	 */
	@RequestMapping(value = "/process", method = RequestMethod.POST)
	public String transact(@RequestBody String requestMessage) throws ServiceException {
		logger.debug("ENTER");
		String responseMsg = null;
		responseMsg = spilService.callSPILTransaction(requestMessage);
		logger.debug("Exit");
		return responseMsg;
	}

}
