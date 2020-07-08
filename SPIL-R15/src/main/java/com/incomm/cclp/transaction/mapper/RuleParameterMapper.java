package com.incomm.cclp.transaction.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.transaction.bean.RuleParameter;

public class RuleParameterMapper implements RowMapper<RuleParameter> {

	@Override
	public RuleParameter mapRow(ResultSet rs, int rowNum) throws SQLException {
		RuleParameter ruleParameter = new RuleParameter();
		ruleParameter.setParameterId(rs.getString(1));
		ruleParameter.setParameterType(rs.getString(2));
		return ruleParameter;
	}

}