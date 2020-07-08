package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;

import com.incomm.scheduler.dao.BulkTransactionDAO;
import com.incomm.scheduler.model.BulkTransactionRequestFile;

public class BulkTransactionRequestWriter  implements ItemWriter<BulkTransactionRequestFile>{


	private final BulkTransactionDAO bulkTransactionDAO;
	
	private static final Logger logger = LogManager.getLogger(BulkTransactionRequestWriter.class);

	public BulkTransactionRequestWriter(BulkTransactionDAO bulkTransactionDAO) {
		this.bulkTransactionDAO = bulkTransactionDAO;
	}

	@Override
	public void write(List<? extends BulkTransactionRequestFile> BulkTransactionRequestFiles) throws Exception {
		logger.debug("writing request..");
		bulkTransactionDAO.insertRequest(BulkTransactionRequestFiles);
		
		
	}

}
