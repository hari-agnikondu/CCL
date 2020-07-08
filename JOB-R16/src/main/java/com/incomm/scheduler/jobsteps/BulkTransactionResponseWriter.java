package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.scheduler.dao.BulkTransactionDAO;
import com.incomm.scheduler.model.BulkTransactionResponseFile;

public class BulkTransactionResponseWriter implements ItemWriter<BulkTransactionResponseFile> {

	private final BulkTransactionDAO bulkTransactionDAO;
	
	private static final Logger logger = LogManager.getLogger(BulkTransactionResponseWriter.class);

	public BulkTransactionResponseWriter(BulkTransactionDAO bulkTransactionDAO) {
		this.bulkTransactionDAO = bulkTransactionDAO;
	}

	@Override
	@Transactional
	public void write(List<? extends BulkTransactionResponseFile> BulkTransactionResponseFiles) throws Exception {
		logger.debug("Updating Response...");
		bulkTransactionDAO.updateResponse(BulkTransactionResponseFiles);
		
	}
	
}
