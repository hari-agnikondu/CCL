package com.incomm.cclp.fsapi.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.fsapi.bean.FsApiTransaction;


public class FsApiTransactionDetailMapper implements RowMapper<FsApiTransaction> {

	@Override
	public FsApiTransaction mapRow(ResultSet rs, int rowNum) throws SQLException {
		FsApiTransaction fsapiTransactiondetail = new FsApiTransaction();

		fsapiTransactiondetail.setChannelCode(rs.getString(1));
		fsapiTransactiondetail.setMsgType(rs.getString(2));
		fsapiTransactiondetail.setTranCode(rs.getString(3));
		fsapiTransactiondetail.setDaoCName(rs.getString(4));
		fsapiTransactiondetail.setRequestType(rs.getString(5));
		fsapiTransactiondetail.setRequestMethod(rs.getString(6));
		fsapiTransactiondetail.setValidationName(rs.getString(7));
		fsapiTransactiondetail.setVerifyCName(rs.getString(8));
		fsapiTransactiondetail.setReversalTransaction(rs.getString(9));
		fsapiTransactiondetail.setActionName(rs.getString(10));
		fsapiTransactiondetail.setLogExemption(rs.getString(11));
		fsapiTransactiondetail.setChannelDescritption(rs.getString(12));
		fsapiTransactiondetail.setIsFinancial(rs.getString(13));
		fsapiTransactiondetail.setCreditDebitIndicator(rs.getString(14));
		fsapiTransactiondetail.setChannelShortName(rs.getString(15));
		fsapiTransactiondetail.setPassiveSupported(rs.getString(16));
		fsapiTransactiondetail.setTransactionDesc(rs.getString(17));
		fsapiTransactiondetail.setTransactionShortName(rs.getString(18));

		return fsapiTransactiondetail;

	}
}