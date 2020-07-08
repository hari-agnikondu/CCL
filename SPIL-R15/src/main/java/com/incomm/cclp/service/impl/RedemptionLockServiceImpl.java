package com.incomm.cclp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.account.domain.model.RedemptionLockEntity;
import com.incomm.cclp.account.domain.model.RedemptionLockUpdate;
import com.incomm.cclp.dao.RedemptionLockDao;
import com.incomm.cclp.service.RedemptionLockService;

@Service
public class RedemptionLockServiceImpl implements RedemptionLockService {

	@Autowired
	RedemptionLockDao redemptionLockDao;

	@Override
	public List<RedemptionLockEntity> getRedemptionLocks(String cardNumberHash, String lockFlag) {
		return redemptionLockDao.getRedemptionLocks(cardNumberHash, lockFlag);
	}

	@Override
	public int addRedemptionLock(RedemptionLockUpdate redemptionLockUpdate) {
		return redemptionLockDao.addRedemptionLock(redemptionLockUpdate);
	}

	@Override
	public int updateRedemptionLock(RedemptionLockUpdate redemptionLockUpdate) {
		return redemptionLockDao.updateRedemptionLock(redemptionLockUpdate);
	}

}
