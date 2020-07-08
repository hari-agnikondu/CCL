package com.incomm.scheduler.controller;
/**
 * RestartJobController provides the rest end point to restart the job based on jobExecutionId.
 * author venkateshgaddam
 */
import javax.batch.operations.JobRestartException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.utils.ResponseBuilder;
import com.incomm.scheduler.utils.ResponseMessages;

@RestController
@RequestMapping("/restartJob")
public class RestartJobController {

	private static final Logger logger = LogManager.getLogger(CnFileController.class);

	@Autowired
	private ResponseBuilder responseBuilder;

	@Autowired
	JobOperator jobOperator;
	@Autowired
	JobExplorer jobExplorer;

	@RequestMapping(value="{jobId}",method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> processCNFileRestart(@PathVariable("jobId") int jobExecutionId) {
		
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		try {
			long timeTaken = 0;
			long timeBeforeTransaction = System.currentTimeMillis();
			final Long restartId = jobOperator.restart(jobExecutionId);
			JobExecution jobExecution=jobExplorer.getJobExecution(restartId);
			String batchStatus=jobExecution.getStatus().toString();
			logger.info("batchStatus:"+batchStatus);
			if("COMPLETED".equals(batchStatus)){
				responseDto = responseBuilder.buildSuccessResponse("Exit Status", ResponseMessages.CN_RESULT,
						ResponseMessages.SUCCESS);
			}
			else{
				responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILURE,ResponseMessages.FAILURE );
			}
			long timeAfterTransaction = System.currentTimeMillis();
			timeTaken = timeAfterTransaction - timeBeforeTransaction;
			logger.debug("Time taken Time: " + timeTaken);
		} 

		catch (JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException | UnexpectedJobExecutionException e) {
			logger.debug("Exception found while restarting job" +e.getMessage());
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.ALREADY_EXISTS,ResponseMessages.ALREADY_EXISTS );
		} catch (Exception ex) {
			logger.debug("Exception found while initiating job" + ex.getMessage());
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.FAILURE,ResponseMessages.FAILURE );
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}

}
