package com.incomm.cclp.dao;

import java.math.BigDecimal;
import java.math.BigInteger;

public interface AccountDAO {

	public int updateInitialLoadBalance(BigInteger accountId, BigDecimal initialLoadAmt, BigDecimal newInitialLoadAmt,
			BigDecimal previousInitialLoadAmount);

}
