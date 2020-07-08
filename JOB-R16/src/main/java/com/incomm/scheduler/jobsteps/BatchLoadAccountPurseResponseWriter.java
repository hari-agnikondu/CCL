package com.incomm.scheduler.jobsteps;

import java.util.List;

import org.springframework.batch.item.ItemWriter;

import com.incomm.scheduler.dao.BatchLoadAccountPurseDAO;
import com.incomm.scheduler.model.BatchLoadAccountPurseLog;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BatchLoadAccountPurseResponseWriter implements ItemWriter<BatchLoadAccountPurseLog> {

	private BatchLoadAccountPurseDAO batchUpdateRequestDAO;

	public BatchLoadAccountPurseResponseWriter(BatchLoadAccountPurseDAO batchUpdateRequestDAO) {
		super();
		this.batchUpdateRequestDAO = batchUpdateRequestDAO;
	}

	@Override
	public void write(List<? extends BatchLoadAccountPurseLog> items) throws Exception {
		log.info("Updating Account Purse Request log");
		this.batchUpdateRequestDAO.updateAccountPurseBatchLog(items);
	}

}
