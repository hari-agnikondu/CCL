package com.incomm.scheduler.job.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.SkipListener;

import com.incomm.scheduler.config.BatchConfiguration;
import com.incomm.scheduler.model.BulkTransactionRequestFile;
import com.incomm.scheduler.model.BulkTransactionResponseFile;

public class BulkTransactionSkipListener implements SkipListener<BulkTransactionRequestFile,BulkTransactionResponseFile> {

	private static final Logger logger = LogManager.getLogger(BatchConfiguration.class);
	
	@Override
	public void onSkipInRead(Throwable t) {
		logger.error("Error in item read"+t.getMessage());
		
	}

	@Override
	public void onSkipInWrite(BulkTransactionResponseFile item, Throwable t) {
		logger.error("Error in write, item:{}, Exception: {}",item,t.getMessage());
		
	}

	@Override
	public void onSkipInProcess(BulkTransactionRequestFile item, Throwable t) {
		logger.error("Error in item process, Item: {}, Exception: {}",item,t.getMessage());
		
	}

	
}
