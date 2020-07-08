package com.incomm.cclp.repos;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.domain.RedemptionLock;
import com.incomm.cclp.domain.RedemptionLockPK;

public interface RedemptionLockRepository extends CrudRepository<RedemptionLock, RedemptionLockPK> {
	@Query(nativeQuery = true)
	List<RedemptionLockEntity> getRedemptionLocks(@Param("cardNumberHash") String rrn, @Param("lockFlag") String txnDate);
}
