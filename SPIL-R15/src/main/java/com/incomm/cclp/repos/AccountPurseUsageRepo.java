package com.incomm.cclp.repos;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.domain.AccountPurseUsage;

public interface AccountPurseUsageRepo extends CrudRepository<AccountPurseUsage, BigDecimal> {

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query(value = QueryConstants.UPDATE_LAST_TXNDATE, nativeQuery = true)
	int updateLastTxnDate(@Param("accountId") BigInteger accountId, @Param("purseId") BigInteger purseId);

}
