package com.incomm.cclp.dao.service.impl;

import java.math.BigInteger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.dao.service.AccountPurseDAOService;
import com.incomm.cclp.dao.service.SequenceService;
import com.incomm.cclp.domain.AccountPurse;
import com.incomm.cclp.dto.RequestInfo;
import com.incomm.cclp.repos.GenericRepo;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AccountPurseDAOServiceImpl implements AccountPurseDAOService {

	@Autowired
	private GenericRepo genericRepo;

	@Autowired
	private SequenceService sequenceService;

	@Override
	public void getAccountPurseDetails(RequestInfo req) {
		log.info(LoggerConstants.ENTER);
		AccountPurse purseInfo = null;
		BigInteger purseId = null;
		if (req.isExtPurse()) {
			purseId = BigInteger.valueOf(req.getPurAuthReq()
				.getPurseId());
			List<AccountPurse> accountPurse = genericRepo.getAccountPurseByPurseId(req.getAccountId(), req.getProductId(), purseId);
			if (accountPurse.isEmpty()) {
				log.info("New Account Purse");
				req.setNewPurse(true);
				purseInfo = new AccountPurse();
				purseInfo.setAccountPurseId(BigInteger.valueOf(sequenceService.getNextAccountPurseId()));
				req.getPurAuthReq()
					.setAccountPurseId(purseInfo.getAccountPurseId()
						.longValue());
			} else {
				log.info("Account Purse already exists Getting account purse details");
				purseInfo = accountPurse.get(0);
				req.getPurAuthReq()
					.setAccountPurseId(purseInfo.getAccountPurseId()
						.longValue());
			}
		} else {
			log.info("Getting Account Purse details");
			purseId = req.getPurseId();
			List<AccountPurse> accountPurse = genericRepo.getAccountPurseByPurseId(req.getAccountId(), req.getProductId(),
					req.getPurseId());
			purseInfo = accountPurse.get(0);
		}
		req.setOpeningBalance(purseInfo.getAvailableBalance());
		req.setOpeningAvailBalance(purseInfo.getAvailableBalance());
		req.setOpeningLedgerBalance(purseInfo.getLedgerBalance());
		req.setAccountPurseId(purseInfo.getAccountPurseId()
			.longValue());
		req.setPurseId(purseId);
		log.info(LoggerConstants.EXIT);
	}

}
