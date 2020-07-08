package com.incomm.scheduler.job.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class BulkTransactionJobListener extends JobExecutionListenerSupport {

	private static final Logger logger = LogManager.getLogger(BulkTransactionJobListener.class);

	@Override
	public void afterJob(JobExecution jobExecution) {

		logger.info("Finish Job! Status:" + jobExecution.getStatus() + " Exist Status:" + jobExecution.getExitStatus());

		if (jobExecution.getExitStatus() == ExitStatus.UNKNOWN) {

			jobExecution.setExitStatus(ExitStatus.FAILED);
		}

		if (jobExecution.getStatus() == BatchStatus.UNKNOWN) {

			jobExecution.setStatus(BatchStatus.FAILED);

			logger.info("Job status: " + BatchStatus.FAILED);
		}

		if (jobExecution.getStatus() == BatchStatus.FAILED) {
			logger.info("Job Failed");
		}
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {

		logger.info("Before Finish Job! Status:" + jobExecution.getStatus() + " Exist Status:"
				+ jobExecution.getExitStatus());

		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

			logger.debug("Finish Job! Check the results");
		}

		if (jobExecution.getStatus() == BatchStatus.FAILED) {

			logger.info("Job Failed");
		}
	}
}
