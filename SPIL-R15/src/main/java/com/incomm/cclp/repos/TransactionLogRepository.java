/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.repos;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.domain.TransactionLog;
import com.incomm.cclp.domain.TransactionLogInfo;

/**
 *
 * @author skocherla
 */
public interface TransactionLogRepository extends CrudRepository<TransactionLog, BigDecimal> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_TXN_REVERSAL_FLAG, nativeQuery = true)
	public int updateTransactionReversalFlag(@Param("cardNumHash") String cardHashNum, @Param("txnCode") String txnCode,
			@Param("rrn") String rrn, @Param("reversalFlag") String reversalFlag);

	@Query(nativeQuery = true)
	TransactionLogInfo getLastSuccessTxn(@Param("deliveryChannel") String deliveryChannel, @Param("cardNumber") String cardNumber,
			@Param("txnCode") String txnCode, @Param("responseId") String responseId, @Param("msgType") String msgType,
			@Param("rrn") String rrn, @Param("txnDate") String txnDate);

	@Query(nativeQuery = true)
	List<TransactionLogInfo> getLastSuccessfulTransactions(@Param("deliveryChannel") String deliveryChannel,
			@Param("cardNumber") String cardNumber, @Param("txnCode") String txnCode, @Param("responseId") String responseId,
			@Param("msgType") String msgType, @Param("rrn") String rrn, @Param("txnDate") String txnDate);

	@Query(nativeQuery = true)
	TransactionLogInfo getLastSuccessPurseTxn(@Param("deliveryChannel") String deliveryChannel, @Param("cardNumber") String cardNumber,
			@Param("txnCode") String txnCode, @Param("responseId") String responseId, @Param("msgType") String msgType,
			@Param("rrn") String rrn, @Param("txnDate") String txnDate, @Param("purseId") long purseId);

	@Query(nativeQuery = true)
	TransactionLogInfo getLastSuccessfulActTxn(@Param("deliveryChannel") String deliveryChannel, @Param("cardNumber") String cardNumber,
			@Param("txnCode") String txnCode, @Param("responseId") String responseId, @Param("msgType") String msgType,
			@Param("purseId") BigInteger purseId);

	@Query(value = QueryConstants.GET_ALL_LAST_SUCCESS_PURSE_ACT_TXN, nativeQuery = true)
	TransactionLog getAllLastSuccessPurseTxn(@Param("deliveryChannel") String deliveryChannel, @Param("cardNumber") String cardNumber,
			@Param("txnCode") String txnCode, @Param("responseId") String responseId, @Param("msgType") String msgType,
			@Param("rrn") String rrn, @Param("txnDate") String txnDate, @Param("purseId") long purseId);
}
