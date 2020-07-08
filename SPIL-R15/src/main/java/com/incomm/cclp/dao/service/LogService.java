package com.incomm.cclp.dao.service;

import java.util.List;

import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.dto.RequestInfo;

public interface LogService {
	void doStatementsLog(RequestInfo req);

	void doTransactionLog(RequestInfo req);

	void addStatementLog(RequestInfo req);

	void addReversalStatementlog(RequestInfo req, List<StatementsLogInfo> stmtLog);
}
