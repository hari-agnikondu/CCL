package com.incomm.scheduler.job.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class BulkTransactionSkipPolicy implements SkipPolicy {

	private static final Logger logger = LogManager.getLogger(BulkTransactionListener.class);
	
	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
		logger.error("Number of recoreds skipped: {}, Exception: {}",skipCount,t);
		return false;
	}

}
