package com.incomm.cclp.transaction.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.incomm.cclp.transaction.bean.CardStatus;

public class CardStatusMapper implements RowMapper<CardStatus> {

	@Override
	public CardStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
		CardStatus cardStatus = new CardStatus();
		cardStatus.setStatusCode(rs.getString(1));
		cardStatus.setStatusDesc(rs.getString(2));
		return cardStatus;
	}

}
