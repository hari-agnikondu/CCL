package com.incomm.cclp.dao.impl;

import static com.incomm.cclp.account.util.CodeUtil.isNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.dao.AccountDAO;

@Repository
public class AccountDAOImpl extends JdbcDaoSupport implements AccountDAO {

	private static final String UPDATE_ACCOUNT_BASE = "update account set initialload_amt = :initialLoadAmt, new_initialload_amt= :newInitialLoadAmt, "
			+ "last_upd_date = SYSDATE where account_id= :accountId ";

	private static final String UPDATE_INITIAL_LOAD_AMOUNT_PREVIOUS_VALUE_NOT_NULL = UPDATE_ACCOUNT_BASE
			+ " and INITIALLOAD_AMT = :previousInitialLoadAmount ";

	private static final String UPDATE_INITIAL_LOAD_AMOUNT_PREVIOUS_VALUE_NULL = UPDATE_ACCOUNT_BASE + " and INITIALLOAD_AMT is null ";

	@Autowired
	DataSource dataSource;

	@PostConstruct
	private void initialize() {
		setDataSource(dataSource);
	}

	@Override
	public int updateInitialLoadBalance(BigInteger accountId, BigDecimal initialLoadAmt, BigDecimal newInitialLoadAmt,
			BigDecimal previousInitialLoadAmount) {

		return isNull(previousInitialLoadAmount)
				? getJdbcTemplate().update(UPDATE_INITIAL_LOAD_AMOUNT_PREVIOUS_VALUE_NULL, initialLoadAmt, newInitialLoadAmt, accountId)
				: getJdbcTemplate().update(UPDATE_INITIAL_LOAD_AMOUNT_PREVIOUS_VALUE_NOT_NULL, initialLoadAmt, newInitialLoadAmt, accountId,
						previousInitialLoadAmount);

	}

}
