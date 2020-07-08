package com.incomm.cclp.fsapi.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.SpilReloadDAO;

@Repository
public class SpilReloadDAOImpl  extends JdbcDaoSupport implements SpilReloadDAO {

	private static final Logger logUtil = LogManager.getLogger(SpilReloadDAOImpl.class);
	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}
	@Override
	public String[] invokeReload(Map<String,String> valueObj) throws ServiceException {
		String[] respFields = new String[5];

		try {
			
			List<SqlParameter> params = new ArrayList<>();
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.BIGINT));
			params.add( new SqlParameter(Types.BIGINT));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.DATE));
			params.add( new SqlParameter(Types.DATE));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.BIGINT));
			params.add( new SqlParameter(Types.BIGINT));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add( new SqlParameter(Types.VARCHAR));
			params.add(new SqlOutParameter("42", Types.VARCHAR));
			params.add(new SqlOutParameter("43", Types.VARCHAR));
			params.add(new SqlOutParameter("44", Types.VARCHAR));
			params.add(new SqlOutParameter("45", Types.VARCHAR));

			logUtil.info("ValueObj:"+valueObj);//.get(ValueObjectKeys.EXPIRY_DATE)

			Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {			
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs =null;
					cs = con.prepareCall(QueryConstants.SP_CARD_TOPUP);
					cs.setString(1, valueObj.get(ValueObjectKeys.CARDNUMBER));
					cs.setInt(2, Integer.parseInt(valueObj.get(ValueObjectKeys.PROD_CODE)));
					cs.setBigDecimal(3, new BigDecimal(valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID))); 
					cs.setString(4, valueObj.get(ValueObjectKeys.CARDSTATUS));
					cs.setString(5, valueObj.get(ValueObjectKeys.CARDSTATUS));
					cs.setString(6, valueObj.get(FSAPIConstants.RESP_PRXOY_NUMBER));
					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					java.sql.Date activationdate = null;
					java.sql.Date lasttxndate = null;
					java.sql.Date expDate = null;
						
						if (valueObj.get(ValueObjectKeys.CARD_ACTIVATION_DATE) != null) {
							java.util.Date date = null;
							try {
								date = sdf1.parse(valueObj.get(ValueObjectKeys.CARD_ACTIVATION_DATE));
							} catch (ParseException e) {
								logUtil.error("Error Occured while parsing Activation Date ", e.getMessage());
							}
							if (date != null)
								activationdate = new java.sql.Date(date.getTime());
						}
						if (valueObj.get(ValueObjectKeys.LAST_TXN_DATE) != null) {
							java.util.Date date1 = null;
							try {
								date1 = sdf1.parse(valueObj.get(ValueObjectKeys.LAST_TXN_DATE));
							} catch (ParseException e) {
								logUtil.error("Error Occured while parsing Last Transaction Date ", e.getMessage());
							}
							if (date1 != null)
								lasttxndate = new java.sql.Date(date1.getTime());
						}
						cs.setDate(7,activationdate );
						cs.setDate(8, lasttxndate);
					cs.setString(9, valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));
					cs.setString(10, null);
					cs.setString(11, null);
					cs.setString(12, valueObj.get(ValueObjectKeys.MSGTYPE));
					cs.setString(13, valueObj.get(ValueObjectKeys.DELIVERYCHNL));
					cs.setString(14, valueObj.get(ValueObjectKeys.TRANS_CODE));
					cs.setString(15, valueObj.get(ValueObjectKeys.BUSINESS_DATE));
					cs.setString(16, valueObj.get(ValueObjectKeys.BUSINESS_TIME));
					cs.setString(17,null);
					cs.setString(18, valueObj.get(ValueObjectKeys.RRN)); 
					cs.setString(19,valueObj.get(ValueObjectKeys.MERCHANT_NAME));
					cs.setString(20,null);
					cs.setString(21,null);
					cs.setString(22,null);
					cs.setString(23,null);
					cs.setString(24,null);
					cs.setString(25,null);
					cs.setString(26,null);
					cs.setString(27,null);
					cs.setString(28,null);
					cs.setString(29,null);
					cs.setString(30,null);
					cs.setString(31,null);
					cs.setString(32,null);
					cs.setBigDecimal(33, null);
					cs.setBigDecimal(34, new BigDecimal(valueObj.get(ValueObjectKeys.TRANAMOUNT)!=null?valueObj.get(ValueObjectKeys.TRANAMOUNT):"0.0"));
					cs.setString(35,valueObj.get(ValueObjectKeys.CURRENCY_CODE));
					cs.setString(36,null);
					cs.setString(37,null);
					cs.setString(38,null);
					cs.setString(39,null);
					//13.06.2018 added 
					cs.setString(40,valueObj.get(ValueObjectKeys.X_INCFS_CORRELATIONID));
					if (valueObj.get(ValueObjectKeys.EXPIRY_DATE) != null) {
						java.util.Date dateExp = null;
						try {
							dateExp = sdf1.parse(valueObj.get(ValueObjectKeys.EXPIRY_DATE));
						} catch (ParseException e) {
							logUtil.error("Error Occured while parsing Last Transaction Date ", e.getMessage());
						}
						if (dateExp != null)
							expDate = new java.sql.Date(dateExp.getTime());
						cs.setString(41, sdf1.format(expDate));
					}
					else{
						cs.setString(41,null);
					}

					cs.registerOutParameter(42, Types.VARCHAR);
					cs.registerOutParameter(43, Types.VARCHAR);
					cs.registerOutParameter(44, Types.VARCHAR);
					cs.registerOutParameter(45, Types.VARCHAR);

					return cs;
				}
			}, params);	

			respFields[0] = (String)resultMap.get("42");//p_resp_code
			respFields[1] = (String)resultMap.get("43");//p_err_msg
			respFields[2] = (String)resultMap.get("44");//p_auth_id
			respFields[3] = (String)resultMap.get("45");//p_authorizedamt
			logUtil.info("Response message : " + respFields[1] + "Response Code : "
					+ respFields[0]+"  Total amt:"+respFields[3]+" AuthId "+respFields[2]);
		}
		catch (Exception e) {
			logUtil.error("Exception in Spil Reload Dao: "+e.toString(),e);
		}
		return respFields;

	}
}

