package com.incomm.scheduler.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.utils.JobConstants;

@RestController
@RequestMapping("/bulkTxn")
public class BulkTransactionController {

	@Autowired
	JobLauncher jobLauncher;
 
	private static final Logger logger = LogManager.getLogger(OrderProcessController.class);
	
	@Autowired
	@Qualifier("bulkTransactionJob")
	Job bulkTransactionJob;

	@Value("${BULK_TXN_FILE_PATH}") String bulkTxnPath;
	
	
	@RequestMapping(method = RequestMethod.PUT)
	public String bulkTxnTest(@RequestBody(required = false) String fileNames){
		
		logger.info(CCLPConstants.ENTER);
		try {
			long timeTaken = 0;
			long timeBeforeTransaction = System.currentTimeMillis();

			JobParameters jobParameters = new JobParametersBuilder()
					.addLong(JobConstants.DATETIME, System.currentTimeMillis())
					.addString(JobConstants.DIRECTORY_PATH,bulkTxnPath)
					.addString(JobConstants.JOBNAME, JobConstants.BULK_TRANSACTION_JOB_ID)
					.addString("fileNames", fileNames).toJobParameters();
		           jobLauncher.run(bulkTransactionJob, jobParameters);
			
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

	@RequestMapping(value="/BlkTxnTest/{chunkSize}/{threadsCount}",method = RequestMethod.GET)
	public String setConfigTest(@PathVariable String chunkSize, @PathVariable String threadsCount) {

		logger.info(CCLPConstants.ENTER);
		try {
			FileInputStream in = new FileInputStream(
					getClass().getClassLoader().getResource("application.properties").getFile());

			Properties props = new Properties();

			props.load(in);
			in.close();

			FileOutputStream out = new FileOutputStream(
					getClass().getClassLoader().getResource("application.properties").getFile());
			String thread = (String) props.get("POOL_SIZE");
			String chunk = (String) props.get("CHUNK_SIZE");
			String grid = (String) props.get("GRID_SIZE");

			props.setProperty("CHUNK_SIZE", chunkSize);
			props.setProperty("POOL_SIZE", threadsCount);

			props.save(out, null);
			out.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return "SUCCESS";

	}
}
