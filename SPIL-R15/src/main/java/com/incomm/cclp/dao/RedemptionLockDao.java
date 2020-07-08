package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.account.domain.model.RedemptionLockUpdate;

public interface RedemptionLockDao {
	public List<RedemptionLockEntity> getRedemptionLocks(String cardNumberHash, String lockFlag);

	public int addRedemptionLock(RedemptionLockUpdate redemptionLockUpdate);

	public int updateRedemptionLock(RedemptionLockUpdate redemptionLockUpdate);
}
