package com.incomm.cclp.transaction.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.transaction.bean.SpilResponseCode;

public class SpilResponseCodesMapper implements RowMapper<SpilResponseCode> {

	@Override
	public SpilResponseCode mapRow(ResultSet rs, int rowNum) throws SQLException {
		SpilResponseCode spilResponseCode = new SpilResponseCode();
		spilResponseCode.setResponseCode(rs.getString(1));
		spilResponseCode.setChannelResponseCode(rs.getString(2));
		spilResponseCode.setResponseDescription(rs.getString(3));
		return spilResponseCode;
	}

}
