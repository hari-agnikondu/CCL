package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ExecutionContext;

import com.incomm.scheduler.model.BulkTransactionRequestFile;
import com.incomm.scheduler.model.BulkTransactionResponseFile;

public interface BulkTransactionDAO {

	int chkDuplicateFiles(String fileName);

	void InsertBlkTxnFiles(Map<String, ExecutionContext> fileList);
	
	void updateResponse(List<? extends BulkTransactionResponseFile> bulkTransactionResponseFiles);
	String getBatchId();

	void insertRequest(List<? extends BulkTransactionRequestFile> bulkTransactionRequestFiles);

	void updateFileStatus(String value);
	int getRecordCountBatchId(Long BatchId);

	

}
