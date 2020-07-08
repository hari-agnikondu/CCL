package com.incomm.cclp.transaction.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.transaction.bean.SpilStartupMsgTypeBean;

/**
 * SpilStartupMsgTypeBeanMapper used to map all table columns values to SpilStartupMsgTypeBean
 * 
 * @author venkateshgaddam
 *
 */
public class SpilStartupMsgTypeBeanMapper implements RowMapper<SpilStartupMsgTypeBean> {

	@Override
	public SpilStartupMsgTypeBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		SpilStartupMsgTypeBean msgTypeBean = new SpilStartupMsgTypeBean();
		msgTypeBean.setDeliveryChannel(rs.getString(1));
		msgTypeBean.setMsgType(rs.getString(2));
		msgTypeBean.setSpilMsgType(rs.getString(3));
		msgTypeBean.setTxnCode(rs.getString(4));
		msgTypeBean.setPartySupported(rs.getString(5));
		msgTypeBean.setAuthJavaClass(rs.getString(6));
		msgTypeBean.setIsFinacial(rs.getString(7));
		msgTypeBean.setCreditDebitIndicator(rs.getString(8));
		msgTypeBean.setChannelShortName(rs.getString(9));
		msgTypeBean.setPassiveSupported(rs.getString(10));
		msgTypeBean.setTransactionDesc(rs.getString(11));
		msgTypeBean.setTransactionShortName(rs.getString(12));
		return msgTypeBean;
	}

}
