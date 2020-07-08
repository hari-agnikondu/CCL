package com.incomm.cclp.fsapi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.fsapi.bean.FsApiValidationDetail;


public class FsApiValidationDetailMapper implements RowMapper<FsApiValidationDetail> {

	@Override
	public FsApiValidationDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
		FsApiValidationDetail fsapiValidationdetail= new  FsApiValidationDetail();
		fsapiValidationdetail.setApiName(rs.getString(1));
		fsapiValidationdetail.setFieldName(rs.getString(2));
		fsapiValidationdetail.setParentTag(rs.getString(3));
		fsapiValidationdetail.setDataType(rs.getString(4));
		fsapiValidationdetail.setFieldValues(rs.getString(5));
		fsapiValidationdetail.setRegexExpression(rs.getString(6));
		fsapiValidationdetail.setValidationType(rs.getString(7));
		fsapiValidationdetail.setValidationErrMsg(rs.getString(8));
		fsapiValidationdetail.setRespCode(rs.getString(9));
		fsapiValidationdetail.setSubTagField(rs.getString(11));
		fsapiValidationdetail.setRequestMethod(rs.getString(12));
		return fsapiValidationdetail;
	}

}
