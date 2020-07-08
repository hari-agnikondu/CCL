package com.incomm.cclp.service;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface AccountService {
	public int updateInitialLoadBalance(BigInteger accountId, BigDecimal initialLoadAmt, BigDecimal newInitialLoadAmt,
			BigDecimal previousInitialLoadAmount);
}
