package com.incomm.cclp.fsapi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.fsapi.bean.FsApiDetail;

public class FsApiDetailMapper implements RowMapper<FsApiDetail> {

	@Override
	public FsApiDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
		FsApiDetail fsapidetail= new  FsApiDetail();
		fsapidetail.setApiName(rs.getString(1));
		fsapidetail.setApiUrl(rs.getString(4));
		fsapidetail.setSubTagRespField(rs.getString(5));
		fsapidetail.setSubTagReqField(rs.getString(6));
		fsapidetail.setRequestMetod(rs.getString(7));
		
		
		return fsapidetail;
	}

}
