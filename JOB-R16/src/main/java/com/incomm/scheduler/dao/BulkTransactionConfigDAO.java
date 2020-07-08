package com.incomm.scheduler.dao;

import java.util.List;
import java.util.Map;

public interface BulkTransactionConfigDAO {

	List<Map<String, Object>> getThreadProperties();

	int verifyMdmId(String mdmId);

}
