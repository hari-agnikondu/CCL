package com.incomm.cclp.transaction.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.transaction.bean.TransactionAuthDefinition;

public class TransactionAuthDefinitionMapper implements RowMapper<TransactionAuthDefinition> {

	@Override
	public TransactionAuthDefinition mapRow(ResultSet rs, int rowNum) throws SQLException {
		TransactionAuthDefinition transactionAuthDefinition = new TransactionAuthDefinition();
		transactionAuthDefinition.setTransactionCode(rs.getString(1));
		transactionAuthDefinition.setMsgType(rs.getString(2));
		transactionAuthDefinition.setProcessKey(rs.getString(3));
		transactionAuthDefinition.setProcessType(rs.getString(4));
		transactionAuthDefinition.setExecutionOrder(Long.parseLong(rs.getString(5)));
		transactionAuthDefinition.setValidationType(rs.getString(6));
		transactionAuthDefinition.setChannelCode(rs.getString(7));
		return transactionAuthDefinition;
	}

}
