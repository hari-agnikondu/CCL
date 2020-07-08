package com.incomm.cclp.transaction.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.transaction.bean.RuleParameterMapping;

public class RuleParameterMappingMapper implements RowMapper<RuleParameterMapping> {

	@Override
	public RuleParameterMapping mapRow(ResultSet rs, int rowNum) throws SQLException {
		RuleParameterMapping ruleParameterMapping = new RuleParameterMapping();
		ruleParameterMapping.setParameterId(rs.getString(1));
		ruleParameterMapping.setParameterMapping(rs.getString(2));
		return ruleParameterMapping;
	}

}