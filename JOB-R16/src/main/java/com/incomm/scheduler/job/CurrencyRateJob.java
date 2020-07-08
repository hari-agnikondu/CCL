package com.incomm.scheduler.job;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.Transformer;
import org.springframework.messaging.Message;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.utils.JobConstants;

public class CurrencyRateJob {

	private static final Logger logger = LogManager.getLogger(CurrencyRateJob.class);
	
	@Value("${CURRENCY_RATE_FILE_PATH}") String currencyRatePath;
	
	@Autowired
	@Qualifier("currencyRateUploadJob")
	Job currencyRateUploadJob;
	
	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	JobRepository jobRepository;
	
	@Transformer
	public void currencyRateUpload(Message<File> message) {
		
		String fileName = message.getPayload().getAbsoluteFile().getName();
		logger.info(CCLPConstants.ENTER);
		try {
			long timeTaken = 0;
			long timeBeforeTransaction = System.currentTimeMillis();

			JobParameters jobParameters = new JobParametersBuilder()
					.addLong(JobConstants.DATETIME, System.currentTimeMillis())
					.addString(JobConstants.DIRECTORY_PATH,currencyRatePath)
					.addString(JobConstants.JOBNAME, JobConstants.CURRENCY_RATE_JOB_ID)
					.addString("fileNames", fileName).toJobParameters();
		           jobLauncher.run(currencyRateUploadJob, jobParameters);
			
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
	}
}
