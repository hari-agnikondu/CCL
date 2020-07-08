package com.incomm.cclp.fsapi.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.bean.CardStatusBean;
import com.incomm.cclp.fsapi.bean.FsApiMaster;
import com.incomm.cclp.fsapi.bean.FsApiTransaction;
import com.incomm.cclp.fsapi.constants.TransactionQueryConstants;
import com.incomm.cclp.fsapi.dao.TransactionDao;
import com.incomm.cclp.fsapi.mapper.FsApiMasterlMapper;
import com.incomm.cclp.fsapi.mapper.FsApiTransactionDetailMapper;
import com.incomm.cclp.fsapi.mapper.ResponseCodesMapper;



@Repository
public class TransactionDaoImpl extends JdbcDaoSupport implements TransactionDao {



	@Autowired
    public void setDs(@Qualifier("transactionalDs") DataSource dataSource) {
         setDataSource(dataSource);
    }

	@Override
	public List<FsApiMaster> getAllFsapiMastDetails() {
		return getJdbcTemplate().query(TransactionQueryConstants.FSAPI_API, new FsApiMasterlMapper());
	}

	@Override
	public List<Map<String, Object>> getAllFsapiDetails() {
		return getJdbcTemplate().queryForList(TransactionQueryConstants.FSAPI_DETAILS);
	}

	@Override
	public List<Map<String, Object>> getAllFsapiValidationDetails() {
		return getJdbcTemplate().queryForList(TransactionQueryConstants.FSAPI_VALIDATION_DTLS);
	}

	@Override
	public List<FsApiTransaction> getAllFsapiTransactionDetails() {
		return getJdbcTemplate().query(TransactionQueryConstants.FSAPI_TRANSACTION, new FsApiTransactionDetailMapper());
	}

	@Override
	public List<CSSResponseCode> getCssResponseCodes() {
		return getJdbcTemplate().query(TransactionQueryConstants.B2B_RESPONSE_CODE, new ResponseCodesMapper());
	}
	

	@Override
	public List<CardStatusBean> getAllCardStatus() {
		
		return getJdbcTemplate().query(TransactionQueryConstants.CARD_STATUS,
				new ResultSetExtractor<List<CardStatusBean>>() {
			public List<CardStatusBean> extractData(ResultSet rs)throws SQLException {
				List<CardStatusBean> cardStatusList=new ArrayList<>();
				while (rs.next()) {
					CardStatusBean cardStatusBean = new CardStatusBean();
					cardStatusBean.setStatusCode(rs.getString(1));
					cardStatusBean.setStatusDesc(rs.getString(2));
					cardStatusList.add(cardStatusBean);
				}
				return cardStatusList;
			}
		});
	}

}
