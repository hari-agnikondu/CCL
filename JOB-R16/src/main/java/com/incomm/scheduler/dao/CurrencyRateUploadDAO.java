package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Map;

import org.springframework.batch.item.ExecutionContext;

import com.incomm.scheduler.model.CurrencyRateRequestFile;

public interface CurrencyRateUploadDAO {

	int chkDuplicateFiles(String fileName);

	void InsertCurrencyRateUploadFiles(Map<String, ExecutionContext> fileList);
	
	String getBatchId();

	void insertRequest(List<? extends CurrencyRateRequestFile> currencyRateRequestFiles);

	void updateFileStatus(String value);
	int getRecordCountBatchId(Long batchId);

	void updateResponseByAction(String batchId, String action, String errorMsg,String Status);

	List<Map<String, Object>> getRecordsToProcess(String batchId);

	void updateDuplicateFileStatus(String fileName, String errorMsg, String status);

	int checkCurrencyCode(String txnCurrency);

	void updateResponse(String batchId, String recordNumber, String responesMsg,String status);

}
