/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.incomm.cclp.domain.StatementsLog;
import com.incomm.cclp.domain.StatementsLogInfo;
import com.incomm.cclp.domain.StatementsLogPK;
import com.incomm.cclp.dto.StatementLog;

/**
 *
 * @author skocherla
 */
public interface StatementsLogRepository extends CrudRepository<StatementsLog, StatementsLogPK> {
	@Query(nativeQuery = true)
	List<StatementsLogInfo> getLastFeeTransactions(@Param("rrn") String rrn, @Param("credit_debit_flag") String cr_db_flg,
			@Param("txnDate") String txnDate, @Param("txnCode") String txnCode);

	@Query(nativeQuery = true)
	List<StatementLog> getAllStatementLogs(@Param("rrn") String rrn, @Param("txnDate") String txnDate, @Param("txnCode") String txnCode);
}
