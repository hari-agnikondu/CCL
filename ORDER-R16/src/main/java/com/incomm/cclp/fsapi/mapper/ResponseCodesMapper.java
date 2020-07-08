package com.incomm.cclp.fsapi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.fsapi.bean.CSSResponseCode;


public class ResponseCodesMapper implements RowMapper<CSSResponseCode> {

	@Override
	public CSSResponseCode mapRow(ResultSet rs, int rowNum) throws SQLException {
		CSSResponseCode cssResponseCode = new CSSResponseCode();
		cssResponseCode.setResponseCode(rs.getString(1));
		cssResponseCode.setChannelResponseCode(rs.getString(2));
		cssResponseCode.setResponseDescription(rs.getString(3));
		cssResponseCode.setChannelCode(rs.getString(4));
		return cssResponseCode;
	}

}
