package com.incomm.cclp.dao.impl;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilRedemptionLockDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.Util;

@Repository
public class SpilRedemptionLockDAOImpl implements SpilRedemptionLockDAO {
	@Autowired
	JdbcTemplate jdbcTemplate;

	private static final Logger logger = LogManager.getLogger(SpilCardRedemptionDAOImpl.class);

	@Override
	public String[] invoke(ValueDTO valueDto) throws ServiceException {
		String[] result = new String[7];

		Map<String, String> valueObj = valueDto.getValueObj();

		try {

			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.DATE));
			params.add(new SqlParameter(Types.DATE));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.NUMERIC));
			params.add(new SqlParameter(Types.NUMERIC));

			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.FLOAT));

			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));

			params.add(new SqlOutParameter("responseCode", Types.VARCHAR));
			params.add(new SqlOutParameter("responsemsg", Types.VARCHAR));
			params.add(new SqlOutParameter("AuthorizedId", Types.VARCHAR));
			params.add(new SqlOutParameter("Balance", Types.NUMERIC));
			params.add(new SqlOutParameter("PurseAuthorizedAmt", Types.NUMERIC));
			params.add(new SqlOutParameter("AuthorizedAmt", Types.NUMERIC));
			params.add(new SqlOutParameter("purseAuthResponse", Types.VARCHAR));

			Map<String, Object> resultMap = jdbcTemplate.call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {

					CallableStatement cs = con.prepareCall(QueryConstants.SP_SPIL_REDEMPTION_LOCK); // need to call
																									// proper proce

					java.sql.Date activationdate = null;
					java.sql.Date lasttxndate = null;

					SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
					if (valueObj.get(ValueObjectKeys.CARD_ACTIVATION_DATE) != null) {
						java.util.Date date = null;
						try {
							date = sdf1.parse(valueObj.get(ValueObjectKeys.CARD_ACTIVATION_DATE));
						} catch (ParseException e) {
							logger.warn("Error Occured while parsing Card Activation Date {} : error message {}",
									valueObj.get(ValueObjectKeys.CARD_ACTIVATION_DATE), e.getMessage());

						}
						if (date != null)
							activationdate = new java.sql.Date(date.getTime());
					}
					if (valueObj.get(ValueObjectKeys.LAST_TXN_DATE) != null) {
						java.util.Date date1 = null;
						try {
							date1 = sdf1.parse(valueObj.get(ValueObjectKeys.LAST_TXN_DATE));
						} catch (ParseException e) {
							logger.warn("Error Occured while parsing Last Transaction Date {} : error message {}",
									valueObj.get(ValueObjectKeys.LAST_TXN_DATE), e.getMessage());
						}
						if (date1 != null)
							lasttxndate = new java.sql.Date(date1.getTime());
					}
					logger.info(QueryConstants.SP_SPIL_REDEMPTION_LOCK);
					cs.setString(1, valueObj.get(ValueObjectKeys.CARD_NUMBER));
					cs.setInt(2, Integer.parseInt(valueObj.get(ValueObjectKeys.SPIL_PROD_ID)));
					logger.info(ValueObjectKeys.SPIL_PROD_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_PROD_ID));
					cs.setBigDecimal(3, new BigDecimal(valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID)));
					logger.info(ValueObjectKeys.CARD_ACCOUNT_ID + ":" + valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID));
					cs.setString(4, valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					logger.info(ValueObjectKeys.CARD_CARDSTAT + ":" + valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					cs.setString(5, valueObj.get(ValueObjectKeys.OLD_CARD_STATUS));
					logger.info(ValueObjectKeys.OLD_CARD_STATUS + ":" + valueObj.get(ValueObjectKeys.OLD_CARD_STATUS));
					cs.setString(6, valueObj.get(ValueObjectKeys.PROXY_NUMBER));
					logger.info(ValueObjectKeys.PROXY_NUMBER + ":" + valueObj.get(ValueObjectKeys.PROXY_NUMBER));
					cs.setDate(7, activationdate);
					logger.info("ActivationCode:" + activationdate);
					cs.setDate(8, lasttxndate);
					logger.info("Last txndate:" + lasttxndate);
					cs.setString(9, valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));
					logger.info(ValueObjectKeys.FIRST_TIMETOPUP_FLAG + ":" + valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));
					cs.setString(10, valueObj.get(ValueObjectKeys.IS_REDEEMED));
					logger.info(ValueObjectKeys.IS_REDEEMED + ":" + valueObj.get(ValueObjectKeys.IS_REDEEMED));
					// Usage Limit
					cs.setString(11, null);
					logger.info("Limit: null");
					cs.setString(12, valueObj.get(ValueObjectKeys.SPIL_UPC));
					logger.info(ValueObjectKeys.SPIL_UPC + ":" + valueObj.get(ValueObjectKeys.SPIL_UPC));
					cs.setString(13, valueObj.get(ValueObjectKeys.SP_NUM_TYPE));
					logger.info(ValueObjectKeys.SP_NUM_TYPE + ":" + valueObj.get(ValueObjectKeys.SP_NUM_TYPE));
					cs.setString(14, valueObj.get(ValueObjectKeys.MSGTYPE));
					logger.info(ValueObjectKeys.MSGTYPE + ":" + valueObj.get(ValueObjectKeys.MSGTYPE));
					cs.setString(15, valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
					logger.info(ValueObjectKeys.DELIVERY_CHANNEL_CODE + ":" + valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
					cs.setString(16, valueObj.get(ValueObjectKeys.TRANS_CODE));
					logger.info(ValueObjectKeys.TRANS_CODE + ":" + valueObj.get(ValueObjectKeys.TRANS_CODE));
					cs.setString(17, valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					logger.info(ValueObjectKeys.SPIL_TRAN_DATE + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					cs.setString(18, valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					logger.info(ValueObjectKeys.SPIL_TRAN_TIME + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					cs.setString(19, valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
					logger.info(ValueObjectKeys.SPIL_REQTIME_ZONE + ":" + valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
					cs.setString(20, valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					logger.info(ValueObjectKeys.INCOM_REF_NUMBER + ":" + valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					cs.setString(21, valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
					logger.info(ValueObjectKeys.SPIL_MERCHANT_NAME + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
					cs.setString(22, valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
					logger.info(ValueObjectKeys.SPIL_STORE_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
					cs.setString(23, valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					logger.info(ValueObjectKeys.SPIL_TERM_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					cs.setString(24, valueObj.get(ValueObjectKeys.SPIL_ADDRESS_1));
					cs.setString(25, valueObj.get(ValueObjectKeys.SPIL_ADDRESS_2));
					cs.setString(26, valueObj.get(ValueObjectKeys.SPIL_CITY));
					logger.info(ValueObjectKeys.SPIL_CITY + ":" + valueObj.get(ValueObjectKeys.SPIL_CITY));
					cs.setString(27, valueObj.get(ValueObjectKeys.SPIL_STATE));
					logger.info(ValueObjectKeys.SPIL_STATE + ":" + valueObj.get(ValueObjectKeys.SPIL_STATE));
					cs.setString(28, valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
					logger.info(ValueObjectKeys.SPIL_MERCREF_NUMBER + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
					cs.setString(29, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
					logger.info(ValueObjectKeys.SPIL_LOCALE_CNTRY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
					cs.setString(30, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
					logger.info(ValueObjectKeys.SPIL_LOCALE_CURRENCY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
					cs.setString(31, valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
					logger.info(ValueObjectKeys.SPIL_LOCALE_LANGUAGE + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
					cs.setString(32, valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
					logger.info(ValueObjectKeys.SPIL_POS_ENTRYMODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
					cs.setString(33, valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));
					logger.info(ValueObjectKeys.SPIL_POS_CONDCODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));
					cs.setString(34, valueObj.get(ValueObjectKeys.SOURCE_INFO));
					logger.info(ValueObjectKeys.SOURCE_INFO + ":" + valueObj.get(ValueObjectKeys.SOURCE_INFO));
					cs.setBigDecimal(35, new BigDecimal(valueObj.get(ValueObjectKeys.SPIL_FEE_AMT)));
					logger.info(ValueObjectKeys.SPIL_FEE_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_FEE_AMT));
					cs.setBigDecimal(36, new BigDecimal(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT)));
					logger.info(ValueObjectKeys.SPIL_TRAN_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
					cs.setString(37, valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));
					logger.info(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR + ":" + valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));
					cs.setString(38, valueObj.get(ValueObjectKeys.PARTY_SUPPORTED));
					logger.info(ValueObjectKeys.PARTY_SUPPORTED + ":" + valueObj.get(ValueObjectKeys.PARTY_SUPPORTED));
					cs.setString(39, Util.getProductPartialIndicator(valueDto));
					logger.info(ValueObjectKeys.PARTIAL_PREAUTH_INDICATOR + ":" + Util.getProductPartialIndicator(valueDto));

					cs.setString(40, valueObj.get(ValueObjectKeys.MDM_ID));
					logger.info(ValueObjectKeys.MDM_ID + ":" + valueObj.get(ValueObjectKeys.MDM_ID));
					cs.setString(41, valueObj.get(ValueObjectKeys.CASHIER_ID));
					logger.info(ValueObjectKeys.CASHIER_ID + ":" + valueObj.get(ValueObjectKeys.CASHIER_ID));
					cs.setString(42, valueObj.get(ValueObjectKeys.EXPDATE));
					logger.info(ValueObjectKeys.EXPDATE + ":" + valueObj.get(ValueObjectKeys.EXPDATE));
					cs.setInt(43, Integer.parseInt(String.valueOf(valueObj.get(ValueObjectKeys.PARTNER_ID))));
					logger.info(ValueObjectKeys.PARTNER_ID + ":" + valueObj.get(ValueObjectKeys.PARTNER_ID));
					cs.setInt(44, Integer.parseInt(String.valueOf(valueObj.get(ValueObjectKeys.ISSUER_ID))));
					logger.info(ValueObjectKeys.ISSUER_ID + ":" + valueObj.get(ValueObjectKeys.ISSUER_ID));

					if (valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE) != null) {
						cs.setBigDecimal(45, new BigDecimal(valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE)));
						logger.info(ValueObjectKeys.CURRENCY_CONV_RATE + ":" + valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE));
					} else {
						cs.setBigDecimal(45, null);
						logger.info(ValueObjectKeys.CURRENCY_CONV_RATE + ":" + "null");
					}
					cs.setString(46, valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));
					logger.info(ValueObjectKeys.TXN_CURRENCY_CODE + ":" + valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));
					cs.setBigDecimal(47, new BigDecimal(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)));
					logger.info(ValueObjectKeys.ORGNL_TRAN_AMOUNT + ":" + valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT));

					if (!Util.isEmpty(valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID))) {
						cs.setInt(48, Integer.parseInt(valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID)));
						logger.info(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID + ":"
								+ valueObj.get(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID));
					} else {
						cs.setInt(48, 0);
						logger.info(ValueObjectKeys.PURAUTHREQ_ACCOUNT_PURSE_ID + ":" + "null");
					}
					cs.setString(49, valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME));
					logger.info(ValueObjectKeys.PURAUTHREQ_PURSE_NAME + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_NAME));

					if (valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT) != null) {
						cs.setBigDecimal(50, new BigDecimal(valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT)));
						logger.info(ValueObjectKeys.PURAUTHREQ_TRAN_AMT + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_TRAN_AMT));
					} else {
						cs.setBigDecimal(50, null);
						logger.info(ValueObjectKeys.PURAUTHREQ_TRAN_AMT + ":" + "null");
					}

					cs.setString(51, valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
					logger.info(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_PURSE_CURRENCY));
					cs.setString(52, valueObj.get(ValueObjectKeys.PURAUTHREQ_SKUCODE));
					logger.info(ValueObjectKeys.PURAUTHREQ_SKUCODE + ":" + valueObj.get(ValueObjectKeys.PURAUTHREQ_SKUCODE));
					cs.setInt(53, Integer.parseInt(valueObj.get(ValueObjectKeys.PURSE_ID)));
					logger.info(ValueObjectKeys.PURSE_ID + ":" + valueObj.get(ValueObjectKeys.PURSE_ID));
					cs.setString(54, valueObj.get(ValueObjectKeys.SPIL_ONE_USD_TRAN));
					logger.info(ValueObjectKeys.SPIL_ONE_USD_TRAN + ":" + valueObj.get(ValueObjectKeys.SPIL_ONE_USD_TRAN));

					cs.setString(55, valueObj.get(ValueObjectKeys.STORE_ID));
					logger.info(ValueObjectKeys.STORE_ID + ":" + valueObj.get(ValueObjectKeys.STORE_ID));

					cs.registerOutParameter(56, Types.VARCHAR);
					cs.registerOutParameter(57, Types.VARCHAR);
					cs.registerOutParameter(58, Types.VARCHAR);
					cs.registerOutParameter(59, Types.NUMERIC);
					cs.registerOutParameter(60, Types.NUMERIC);
					cs.registerOutParameter(61, Types.NUMERIC);
					cs.registerOutParameter(62, Types.VARCHAR);

					return cs;
				}
			}, params);

			result[0] = (String) resultMap.get("responseCode");
			result[1] = (String) resultMap.get("responsemsg");
			result[2] = (String) resultMap.get("AuthorizedId");
			result[3] = String.valueOf(resultMap.get("Balance"));
			result[4] = String.valueOf(resultMap.get("AuthorizedAmt"));
			result[5] = valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE);
			result[6] = String.valueOf(resultMap.get("purseAuthResponse"));
			logger.info(
					"Redemption unlock respose: responseCode: {}, responseMsg:{}, AuthorizedId:{}, Balance:{}, AuthorizedAmt:{}, CurrencyCode:{},purseAuthResponse{}",
					result[0], result[1], result[2], result[3], result[4], result[5], result[6]);

		} catch (Exception e) {
			logger.error("Error Occured in {} procedure. error message {}", QueryConstants.SP_SPIL_REDEMPTION_LOCK, e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}
		return result;
	}
}