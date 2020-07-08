package com.incomm.cclp.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.dao.AccountDAO;
import com.incomm.cclp.service.AccountService;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountDAO accountDoa;

	@Override
	public int updateInitialLoadBalance(BigInteger accountId, BigDecimal initialLoadAmt, BigDecimal newInitialLoadAmt,
			BigDecimal previousInitialLoadAmount) {
		return accountDoa.updateInitialLoadBalance(accountId, initialLoadAmt, newInitialLoadAmt, previousInitialLoadAmount);
	}

}
