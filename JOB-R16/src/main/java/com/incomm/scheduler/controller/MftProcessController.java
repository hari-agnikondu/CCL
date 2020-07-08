package com.incomm.scheduler.controller;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.utils.JobConstants;

@RestController
@RequestMapping("/mftschedulers")
public class MftProcessController {

	@Autowired
	JobLauncher jobLauncher;

	// The Logger
	private static final Logger logger = LogManager.getLogger(OrderProcessController.class);

	@Autowired
	@Qualifier("returnFileJob")
	Job returnFileJob;

	@Autowired
	@Qualifier("responseFileJob")
	Job responseFileJob;

	@Autowired
	@Qualifier("ShipmentFileJob")
	Job shipmentFileJob;
	
	@Value("${RETURN_FILE_PATH}") String returnFilePath;
	@Value("${RESPONSE_FILE_PATH}") String responseFilePath;
	@Value("${SHIPMENT_FILE_PATH}") String shipmentFilePath;

	@RequestMapping(value = "/mft/{jobName}")
	public String orderProcessing(@PathVariable("jobName") String jobName, @RequestBody String fileNames) {

		logger.info(CCLPConstants.ENTER);
		try {
			long timeTaken = 0;
			long timeBeforeTransaction = System.currentTimeMillis();

			JobParameters jobParameters;

			if ("returnFileJob".equalsIgnoreCase(jobName)) {
				jobParameters = new JobParametersBuilder().addLong(JobConstants.DATETIME, System.currentTimeMillis())
						.addString(JobConstants.JOBNAME, jobName).addString(JobConstants.DIRECTORY_PATH, returnFilePath+"mft/")
						.addString("fileNames", fileNames).toJobParameters();
				jobLauncher.run(returnFileJob, jobParameters);
			} else if ("responseFileJob".equalsIgnoreCase(jobName)) {
				jobParameters = new JobParametersBuilder().addLong(JobConstants.DATETIME, System.currentTimeMillis())
						.addString(JobConstants.JOBNAME, jobName).addString(JobConstants.DIRECTORY_PATH, responseFilePath+"mft/")
						.addString("fileNames", fileNames).toJobParameters();
						
				jobLauncher.run(responseFileJob, jobParameters);
			} else if ("shipmentFileJob".equalsIgnoreCase(jobName)) {
				jobParameters = new JobParametersBuilder().addLong(JobConstants.DATETIME, System.currentTimeMillis())
						.addString(JobConstants.JOBNAME, jobName).addString(JobConstants.DIRECTORY_PATH, shipmentFilePath+"mft/")
						.addString("fileNames", fileNames).toJobParameters();
				jobLauncher.run(shipmentFileJob, jobParameters);
			}
			
			long timeAfterTransaction = System.currentTimeMillis();
			timeTaken = timeAfterTransaction - timeBeforeTransaction;
			logger.info("TIme taken to complete the MFT processing job : {}", timeTaken);
		
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			logger.error(e.getMessage());
			return "failed";
		
		} catch (Exception ex) {
			logger.error(ex.getMessage());
			return "failed";
		}
		
		logger.info(CCLPConstants.EXIT);
		return "success";
	}

}
