
package com.incomm.cclp.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.domain.BulkTransactionResponseFile;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SPILBatchService;

import io.swagger.annotations.Api;

/**
 * 
 * @author rathanakumark
 *
 */
@RestController
@Api(value = "Spil Transactions")
public class SPILBatchController {

	@Autowired
	SPILBatchService spilBatchService;

	private static final Logger logger = LogManager.getLogger(SpilController.class);

	@PostMapping("/batchProcess")
	public ResponseDTO transact(@RequestBody String requestMessage) throws ServiceException {
		logger.debug("ENTER");
		BulkTransactionResponseFile responseMsg = null;
		responseMsg = spilBatchService.callSPILTransaction(requestMessage);

		ResponseDTO responseDTO = new ResponseDTO();
		if (responseMsg != null && "00".equalsIgnoreCase(responseMsg.getResponseCode())) {
			responseDTO.setData(responseMsg);
			responseDTO.setResponseCode(ValueObjectKeys.SUCCESS_CODE);
			responseDTO.setMessage(ValueObjectKeys.SUCCESS_MSG);
			responseDTO.setResult(null);
			responseDTO.setCode(ValueObjectKeys.SUCCESS_CODE);
		} else {

			responseDTO.setData(responseMsg);
			responseDTO.setResponseCode(ValueObjectKeys.FAILURE_CODE);
			responseDTO.setMessage(ValueObjectKeys.FAILURE_MSG);
			responseDTO.setResult(null);
			responseDTO.setCode(ValueObjectKeys.FAILURE_CODE);
		}
		logger.debug("Exit");
		return responseDTO;
	}

}
