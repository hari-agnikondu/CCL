package com.incomm.scheduler.controller;


import java.util.Map;

import javax.batch.operations.JobRestartException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.service.ManualOrderService;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.ResponseBuilder;
import com.incomm.scheduler.utils.ResponseMessages;



@RestController
@RequestMapping("/schedulers")
public class OrderProcessController {
	
	@Autowired
	JobLauncher jobLauncher;
	
	// The Logger
	private static final Logger logger = LogManager.getLogger(OrderProcessController.class);
	
	@Autowired
	@Qualifier("OrderJob")	
	Job orderJob;

	@Autowired
	ManualOrderService orderService;
	
	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	
	@RequestMapping("/OrderJob")
	public String orderProcessing(){
		
		logger.info(CCLPConstants.ENTER);
		try {
			long timeTaken = 0;
			long timeBeforeTransaction = System.currentTimeMillis();

			JobParameters jobParameters = new JobParametersBuilder()
					.addLong(JobConstants.DATETIME, System.currentTimeMillis())
					.addString(JobConstants.JOBNAME, "OrderJob").toJobParameters();
		           jobLauncher.run(orderJob, jobParameters);
			
			long timeAfterTransaction = System.currentTimeMillis();
			timeTaken = timeAfterTransaction - timeBeforeTransaction;
			logger.info("TIme taken to complete teh Order processing job : {}",timeTaken);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			logger.error(e.getMessage());
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return "OrderProcessing";
	}
	
	@RequestMapping(value = "/{orderId}/{lineItemId}/{partnerId}", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> manualOrder(@PathVariable("orderId") String orderId,
			@PathVariable("lineItemId") String lineItemId,@PathVariable("partnerId") Long partnerId) {

		logger.info("Manual Order : ENTER");
		ResponseDTO responseDto = null;

		String result = null;
		long timeBeforeTransaction = System.currentTimeMillis();
		try {
			result = orderService.getManualOrder(orderId, lineItemId,partnerId);
			responseDto = responseBuilder.buildSuccessResponse(result, ResponseMessages.RESULT, ResponseMessages.SUCCESS);
		} catch (Exception e) {
			logger.error("Order had been failed : {}",e.getMessage());
			responseDto = responseBuilder.buildFailureResponse("", ResponseMessages.FAILURE);
			
		}
		long timeAfterTransaction = System.currentTimeMillis();
		Long timeTaken = timeAfterTransaction - timeBeforeTransaction;
		logger.info("TIme taken to complete the manual Order : {}" ,timeTaken);
		logger.info("Manual Order : EXIT");
		return  new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value="/processMail",method = RequestMethod.POST)
	public void orderStatus(@RequestBody String payLoad,
			@RequestHeader Map<String, String> headers) {
		logger.info("order mail  Started");
		
		orderService.processMailForOrder(payLoad, headers);
		
	}
	

}
