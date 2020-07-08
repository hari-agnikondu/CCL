package com.incomm.cclp.fsapi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.fsapi.bean.FsApiMaster;


public class FsApiMasterlMapper  implements RowMapper<FsApiMaster> {

	@Override
	public FsApiMaster mapRow(ResultSet rs, int rowNum) throws SQLException {
	
		FsApiMaster fsapidet= new  FsApiMaster();
		fsapidet.setApiName(rs.getString(1));
		fsapidet.setReqEncrypt(rs.getString(2));
		fsapidet.setResEncrypt(rs.getString(3));
		fsapidet.setUserIdentifyType(rs.getString(4));
		fsapidet.setPartnerValid(rs.getString(5));
		fsapidet.setCustTypeValid(rs.getString(6));
		fsapidet.setReqMethod(rs.getString(7));
		fsapidet.setReqParse(rs.getString(8));
		fsapidet.setValidationBypass(rs.getString(9));
		fsapidet.setApiDesc(rs.getString(10));
		
		return fsapidet;
		
		
		
	}

}
