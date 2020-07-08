package com.incomm.scheduler.dao.impl;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.dao.BulkTransactionConfigDAO;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class BulkTransactionConfigDAOImpl extends JdbcDaoSupport implements BulkTransactionConfigDAO{
	
	@Autowired
    public void setDs(@Qualifier("configurationDs") DataSource dataSource) {
         setDataSource(dataSource);
    }

	@Override
	public List<Map<String, Object>> getThreadProperties() {
	
		return getJdbcTemplate().queryForList(ScriptUtils.GET_THREAD_POOL_CONFIG);
	}

	@Override
	public int verifyMdmId(String mdmId) {
		int result = 0;
		String sql = ScriptUtils.VERIFY_MDMID;

		result = getJdbcTemplate().queryForObject(sql, new Object[] { mdmId }, Integer.class);

		return result;
	}
}
