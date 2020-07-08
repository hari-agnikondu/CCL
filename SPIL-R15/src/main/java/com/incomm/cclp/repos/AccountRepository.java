package com.incomm.cclp.repos;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.domain.Account;

public interface AccountRepository extends CrudRepository<Account, BigDecimal> {
	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_INITIAL_LOAD_BALANCE, nativeQuery = true)
	public int updateInitialLoadBalance(@Param("accountId") BigInteger accountId, @Param("initialLoadAmt") double initialLoadAmt,
			@Param("newLoadAmt") double newLoadAmt);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_LOAD_BALANCES, nativeQuery = true)
	public int updateInitialLoadBalance(@Param("accountId") BigInteger accountId);
}
