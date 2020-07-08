package com.incomm.scheduler.job.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.annotation.OnProcessError;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.service.IThreadPoolMonitorService;


@Component
public class CurrencyRateUploadListener  {

	private static final Logger logger = LogManager.getLogger(CurrencyRateUploadListener.class);

	@Autowired
	IThreadPoolMonitorService ithreadPoolMonitorService;

	@OnReadError
	public void onItemReadError() {
		logger.error("error encountered in file reading...");

	}
	@OnWriteError
	public void onitemWriteError() {
		logger.error("error encountered in file writing...");

	}

	@OnProcessError
	public void onitemProcessError() {
		logger.error("error encountered in file processing...");

	}

	@BeforeStep
	public void beforeStep() {
		logger.info("before starting the step ");

	}
	
	@AfterStep
	public void afterStep() {
		logger.info("After starting the step ");

	}
	
	@AfterRead
	public void afterRead(String item) {
		logger.info("After Read the step "+item);

	}
	
}