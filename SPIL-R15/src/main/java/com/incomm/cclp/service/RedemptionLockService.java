package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.account.domain.model.RedemptionLockUpdate;

public interface RedemptionLockService {
	public List<RedemptionLockEntity> getRedemptionLocks(String cardNumberHash, String lockFlag);

	public int addRedemptionLock(RedemptionLockUpdate redemptionLockUpdate);

	public int updateRedemptionLock(RedemptionLockUpdate redemptionLockUpdate);
}
