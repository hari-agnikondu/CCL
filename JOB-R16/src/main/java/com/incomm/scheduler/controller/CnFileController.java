package com.incomm.scheduler.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.batch.operations.JobRestartException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dto.ResponseDTO;
import com.incomm.scheduler.service.CnFileService;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.ResponseBuilder;
import com.incomm.scheduler.utils.ResponseMessages;

@RestController
@RequestMapping("/cnFileUpload")
public class CnFileController {

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	Job retailShipmentJob;
	
	String[] fileNames;
	
	@Autowired
	CnFileService cnFileService;
	
	// The Logger
	private static final Logger logger = LogManager.getLogger(CnFileController.class);
	
	// the response builder
	@Autowired
	private ResponseBuilder responseBuilder;
	
	@Value("${CN_FILE_PATH}") String cnFilePath;
	
	@RequestMapping(value="/runJob",method = RequestMethod.PUT)
	public ResponseEntity<ResponseDTO> processCNFile(@RequestBody String fileNames) {
		
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDto = null;
		try {
			long timeTaken = 0;
			long timeBeforeTransaction = System.currentTimeMillis();
			String exitStatus;
			logger.debug("Entered into Process CN File Method");
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong(JobConstants.DATETIME, System.currentTimeMillis())
					.addString(JobConstants.DIRECTORY_PATH,cnFilePath)
					.addString(JobConstants.JOBNAME, "retailShipmentJob")
					.addString("fileNames", fileNames).toJobParameters();
			JobExecution execution = jobLauncher.run(retailShipmentJob, jobParameters);
			exitStatus = execution.getStatus().toString();
			logger.debug("Batch Execution Status:" + exitStatus);
			 responseDto = responseBuilder.buildSuccessResponse(exitStatus, ResponseMessages.CN_RESULT,
						ResponseMessages.SUCCESS);
			long timeAfterTransaction = System.currentTimeMillis();
			timeTaken = timeAfterTransaction - timeBeforeTransaction;
			logger.debug("Time taken Time: " + timeTaken);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			logger.error(e.getMessage());
		} catch (Exception ex) {
			logger.debug("Exception found while initiating job" + ex.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/listCnFiles", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getListOfCnFiles() {
		
		logger.debug("get List Of CnFiles : ENTER");
		ResponseDTO responseDto = null;
		List<String> filesList = new ArrayList<>();
		
		try {
			filesList = cnFileService.listFiles(cnFilePath);
			responseDto = responseBuilder.buildSuccessResponse(filesList, ResponseMessages.CN_RETRIEVED_SUCCESS, ResponseMessages.SUCCESS);
		} catch (Exception e) {
			logger.error("Order had been failed : {}",e.getMessage());
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.CN_RETRIEVED_FAILED, ResponseMessages.FAILURE);
			
		}
		logger.debug("get List Of CnFiles : EXIT");
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/status", method = RequestMethod.GET)
	public ResponseEntity<ResponseDTO> getCnFileStatus() {
		
		logger.debug("get List Of CnFiles status: ENTER");
		ResponseDTO responseDto = null;
		List<Map<String, Object>> filesList = new ArrayList<>();
		
		try {
			filesList = cnFileService.cnFileList();
			responseDto = responseBuilder.buildSuccessResponse(filesList, ResponseMessages.CN_RETRIEVED_SUCCESS, ResponseMessages.SUCCESS);
		} catch (Exception e) {
			logger.error("error while fetching cn file list : {}",e.getMessage());
			responseDto = responseBuilder.buildFailureResponse(ResponseMessages.CN_RETRIEVED_FAILED, ResponseMessages.FAILURE);
			
		}
		logger.debug("get List Of CnFiles status: EXIT");
		return new ResponseEntity<>(responseDto, HttpStatus.OK);
		
	}


}
