package com.incomm.scheduler.dao;

import java.util.List;

import com.incomm.scheduler.model.BatchLoadAccountPurseLog;
import com.incomm.scheduler.model.BatchLoadAccountPurse;

public interface BatchLoadAccountPurseDAO {

	List<BatchLoadAccountPurse> getPendingAccountPurseUpdateRequests();

	int updateAccountPurseUpdateRequest(long requestId, String status);

	int getRecordCountByBatchId(long batchId);

	int[] addAccountPurseBatchLog(List<? extends BatchLoadAccountPurseLog> items);

	int[] updateAccountPurseBatchLog(List<? extends BatchLoadAccountPurseLog> items);

	long getNextPurseLoadBatchId();

	int addBatchLoadAccountPurseRequest(BatchLoadAccountPurse request);


}
