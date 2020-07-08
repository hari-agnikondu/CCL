/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.repos;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.domain.AccountPurse;
import com.incomm.cclp.domain.AccountPurseInfo;
import com.incomm.cclp.domain.AccountPursePK;

/**
 *
 * @author skocherla
 */

@Repository
public interface AccountPurseRepo extends CrudRepository<AccountPurse, AccountPursePK> {

	@Query(nativeQuery = true)
	AccountPurseInfo getAccountPurse(@Param("productId") BigInteger productId, @Param("accountId") BigInteger accountId,
			@Param("currencyCode") String currencyCode, @Param("cardNumber") String cardNum);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_BALANCES_QUERY, nativeQuery = true)
	int updateBalances(@Param("accountId") BigInteger accountId, @Param("purseId") BigInteger purseId,
			@Param("closingLedgerBalance") double closingLegderBalance, @Param("closingAvailableBalance") double closingAvailBalance);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_LOAD_DATE, nativeQuery = true)
	int updateFirstLoadDate(@Param("accountPurseId") long accountId, @Param("date") java.sql.Date date);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_LOAD_DATE_TO_NULL, nativeQuery = true)
	int updateFirstLoadDatetoNull(@Param("accountId") BigInteger accountId);

	@Query(nativeQuery = true)
	List<AccountPurse> getAccountPurseByPurseId(@Param("accountId") BigInteger accountId, @Param("productId") BigInteger productId,
			@Param("purseId") BigInteger purseId);

	@Query(nativeQuery = true)
	AccountPurse getAccountPurseByAccountPurseId(@Param("accountPurseId") BigInteger accountPurseId);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_ACCOUNT_PURSE_BALANCE, nativeQuery = true)
	int updateBalancesByAccountPurseId(@Param("accountPurseId") long accountPurseId,
			@Param("closingLedgerBalance") double closingLegderBalance, @Param("closingAvailableBalance") double closingAvailBalance);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.EXPIRE_ACCOUNT_PURSE, nativeQuery = true)
	int updateBalanceExpDate(@Param("accountPurseId") long accountPurseId, @Param("closingLedgerBalance") double closingLegderBalance,
			@Param("closingAvailableBalance") double closingAvailBalance);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_FIRST_LOAD_DATE_TO_NULL, nativeQuery = true)
	int updateFirstLoadDatetoNull(@Param("accountId") BigInteger accountId, @Param("productId") BigInteger productId,
			@Param("purseId") BigInteger purseId);

	@Query(value = QueryConstants.GET_PURSE_DETAILS, nativeQuery = true)
	public Map<String, Object> getDefaultPurseDetails(@Param("accountId") long accountId, @Param("purseId") long purseId);
}
