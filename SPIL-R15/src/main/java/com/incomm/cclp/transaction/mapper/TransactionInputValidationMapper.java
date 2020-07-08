package com.incomm.cclp.transaction.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.transaction.bean.TransactionInputValidation;

/**
 * TransactionInputValidationMapper used to map all table columns values to TransactionInputValidation
 * 
 * @author venkateshgaddam
 */
public class TransactionInputValidationMapper implements RowMapper<TransactionInputValidation> {

	@Override
	public TransactionInputValidation mapRow(ResultSet rs, int rowNum) throws SQLException {
		TransactionInputValidation inputValidation = new TransactionInputValidation();
		inputValidation.setChannelCode(rs.getString(1));
		inputValidation.setTransactionCode(rs.getString(2));
		inputValidation.setMsgType(rs.getString(3));
		inputValidation.setFieldName(rs.getString(4));
		inputValidation.setType(rs.getString(5));
		inputValidation.setPattern(rs.getString(6));
		inputValidation.setParentTag(rs.getString(7));
		return inputValidation;
	}

}
