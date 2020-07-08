package com.incomm.cclp.dao.impl;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilCashOutDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.util.Util;

@Repository
public class SpilCashOutDAOImpl implements SpilCashOutDAO {

	@Autowired
	JdbcTemplate jdbcTemplate;
	private static final Logger logger = LogManager.getLogger(SpilCashOutDAOImpl.class);

	@Override
	public String[] spilCashOut(ValueDTO valueDto) throws ServiceException {
		String[] result = new String[5];

		Map<String, String> valueObj = valueDto.getValueObj();

		try {

			String date = new SimpleDateFormat(ValueObjectKeys.DATE_FORMAT).format(new Date());
			Date todayDate = new SimpleDateFormat(ValueObjectKeys.DATE_FORMAT).parse(date);
			java.sql.Date actDate = new java.sql.Date(todayDate.getTime());

			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
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
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));

			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.FLOAT));
			params.add(new SqlParameter(Types.BIGINT));

			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.BIGINT));
			params.add(new SqlParameter(Types.VARCHAR));

			params.add(new SqlOutParameter("52", Types.VARCHAR));
			params.add(new SqlOutParameter("53", Types.VARCHAR));
			params.add(new SqlOutParameter("54", Types.VARCHAR));
			params.add(new SqlOutParameter("55", Types.NUMERIC));
			params.add(new SqlOutParameter("56", Types.NUMERIC));
			params.add(new SqlOutParameter("57", Types.VARCHAR));

			Map<String, Object> resultMap = jdbcTemplate.call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(
							"{call PKG_CARD_REDEEM.SPIL_CASHOUT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

					logger.info(
							"call PKG_CARD_REDEEM.SPIL_CASHOUT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
					cs.setString(1, valueObj.get(ValueObjectKeys.CARD_NUMBER));
					cs.setInt(2, Integer.parseInt(valueObj.get(ValueObjectKeys.SPIL_PROD_ID)));
					logger.info(ValueObjectKeys.SPIL_PROD_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_PROD_ID));
					cs.setDouble(3, Double.parseDouble(valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID)));
					logger.info(ValueObjectKeys.CARD_ACCOUNT_ID + ":" + valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID));
					cs.setString(4, valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					logger.info(ValueObjectKeys.CARD_CARDSTAT + ":" + valueObj.get(ValueObjectKeys.CARD_CARDSTAT));
					cs.setString(5, valueObj.get(ValueObjectKeys.OLD_CARD_STATUS));
					logger.info(ValueObjectKeys.OLD_CARD_STATUS + ":" + valueObj.get(ValueObjectKeys.OLD_CARD_STATUS));
					cs.setString(6, valueObj.get(ValueObjectKeys.PROXY_NUMBER));
					logger.info(ValueObjectKeys.PROXY_NUMBER + ":" + valueObj.get(ValueObjectKeys.PROXY_NUMBER));
					cs.setDate(7, actDate);
					logger.info("Activation Date:" + actDate);
					cs.setDate(8, null);
					logger.info("Limit: Null");
					cs.setString(9, valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));
					logger.info(ValueObjectKeys.FIRST_TIMETOPUP_FLAG + ":" + valueObj.get(ValueObjectKeys.FIRST_TIMETOPUP_FLAG));
					cs.setString(10, valueObj.get(ValueObjectKeys.SPIL_UPC));
					logger.info(ValueObjectKeys.SPIL_UPC + ":" + valueObj.get(ValueObjectKeys.SPIL_UPC));
					cs.setString(11, valueObj.get(ValueObjectKeys.SP_NUM_TYPE));
					logger.info(ValueObjectKeys.SP_NUM_TYPE + ":" + valueObj.get(ValueObjectKeys.SP_NUM_TYPE));
					cs.setString(12, valueObj.get(ValueObjectKeys.MSGTYPE));
					logger.info(ValueObjectKeys.MSGTYPE + ":" + valueObj.get(ValueObjectKeys.MSGTYPE));
					cs.setString(13, valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
					logger.info(ValueObjectKeys.DELIVERY_CHANNEL_CODE + ":" + valueObj.get(ValueObjectKeys.DELIVERY_CHANNEL_CODE));
					cs.setString(14, valueObj.get(ValueObjectKeys.TRANS_CODE));
					logger.info(ValueObjectKeys.TRANS_CODE + ":" + valueObj.get(ValueObjectKeys.TRANS_CODE));
					cs.setString(15, valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					logger.info(ValueObjectKeys.SPIL_TRAN_DATE + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_DATE));
					cs.setString(16, valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					logger.info(ValueObjectKeys.SPIL_TRAN_TIME + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_TIME));
					cs.setString(17, valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
					logger.info(ValueObjectKeys.SPIL_REQTIME_ZONE + ":" + valueObj.get(ValueObjectKeys.SPIL_REQTIME_ZONE));
					cs.setString(18, valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					logger.info(ValueObjectKeys.INCOM_REF_NUMBER + ":" + valueObj.get(ValueObjectKeys.INCOM_REF_NUMBER));
					cs.setString(19, valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
					logger.info(ValueObjectKeys.SPIL_MERCHANT_NAME + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCHANT_NAME));
					cs.setString(20, valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
					logger.info(ValueObjectKeys.SPIL_STORE_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_STORE_ID));
					cs.setString(21, valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					logger.info(ValueObjectKeys.SPIL_TERM_ID + ":" + valueObj.get(ValueObjectKeys.SPIL_TERM_ID));
					cs.setString(22, valueObj.get(ValueObjectKeys.SPIL_ADDRESS_1));
					cs.setString(23, valueObj.get(ValueObjectKeys.SPIL_ADDRESS_2));
					cs.setString(24, valueObj.get(ValueObjectKeys.SPIL_CITY));
					logger.info(ValueObjectKeys.SPIL_CITY + ":" + valueObj.get(ValueObjectKeys.SPIL_CITY));
					cs.setString(25, valueObj.get(ValueObjectKeys.SPIL_STATE));
					logger.info(ValueObjectKeys.SPIL_STATE + ":" + valueObj.get(ValueObjectKeys.SPIL_STATE));
					cs.setString(26, valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
					logger.info(ValueObjectKeys.SPIL_MERCREF_NUMBER + ":" + valueObj.get(ValueObjectKeys.SPIL_MERCREF_NUMBER));
					cs.setString(27, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
					logger.info(ValueObjectKeys.SPIL_LOCALE_CNTRY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CNTRY));
					cs.setString(28, valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
					logger.info(ValueObjectKeys.SPIL_LOCALE_CURRENCY + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY));
					cs.setString(29, valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
					logger.info(ValueObjectKeys.SPIL_LOCALE_LANGUAGE + ":" + valueObj.get(ValueObjectKeys.SPIL_LOCALE_LANGUAGE));
					cs.setString(30, valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
					logger.info(ValueObjectKeys.SPIL_POS_ENTRYMODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_ENTRYMODE));
					cs.setString(31, valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));
					logger.info(ValueObjectKeys.SPIL_POS_CONDCODE + ":" + valueObj.get(ValueObjectKeys.SPIL_POS_CONDCODE));
					cs.setString(32, valueObj.get(ValueObjectKeys.SOURCE_INFO));
					logger.info(ValueObjectKeys.SOURCE_INFO + ":" + valueObj.get(ValueObjectKeys.SOURCE_INFO));
					if (valueObj.get(ValueObjectKeys.SPIL_FEE_AMT) != null) {
						cs.setBigDecimal(33, new BigDecimal(valueObj.get(ValueObjectKeys.SPIL_FEE_AMT)));
						logger.info(ValueObjectKeys.SPIL_FEE_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_FEE_AMT));
					} else {
						cs.setBigDecimal(33, null);
						logger.info(ValueObjectKeys.SPIL_FEE_AMT + ": Null");
					}
					if (valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT) != null) {
						cs.setBigDecimal(34, new BigDecimal(valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT)));
						logger.info(ValueObjectKeys.SPIL_TRAN_AMT + ":" + valueObj.get(ValueObjectKeys.SPIL_TRAN_AMT));
					} else {
						cs.setBigDecimal(34, null);
						logger.info(ValueObjectKeys.SPIL_TRAN_AMT + ": Null");
					}

					// For cashout currency code may come in localeinfo.
					cs.setString(35,
							Util.isEmpty(valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR))
									? valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY)
									: valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR));

					logger.info(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR + ":"
							+ (Util.isEmpty(valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR))
									? valueObj.get(ValueObjectKeys.SPIL_LOCALE_CURRENCY)
									: valueObj.get(ValueObjectKeys.SPIL_ORGNL_TRAN_CURR)));

					cs.setString(36, valueObj.get(ValueObjectKeys.PARTY_SUPPORTED));
					logger.info(ValueObjectKeys.PARTY_SUPPORTED + ":" + valueObj.get(ValueObjectKeys.PARTY_SUPPORTED));
					cs.setString(37, ValueObjectKeys.FLAG_NO);
					logger.info(ValueObjectKeys.PARTIAL_PREAUTH_INDICATOR + ":" + ValueObjectKeys.FLAG_NO);
					cs.setString(38, valueObj.get(ValueObjectKeys.MDM_ID));
					logger.info(ValueObjectKeys.MDM_ID + ":" + valueObj.get(ValueObjectKeys.MDM_ID));

					/*----new tags 12062018 by nawaz*/

					cs.setString(39, valueObj.get(ValueObjectKeys.CASHIER_ID));
					logger.info(ValueObjectKeys.CASHIER_ID + ":" + valueObj.get(ValueObjectKeys.CASHIER_ID));

					cs.setString(40, valueObj.get(ValueObjectKeys.EXPDATE));
					logger.info(ValueObjectKeys.EXPDATE + ":" + valueObj.get(ValueObjectKeys.EXPDATE));
					/*----new tags 12062018*/

					if (valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE) != null) {
						cs.setBigDecimal(41, new BigDecimal(valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE)));
						logger.info(ValueObjectKeys.CURRENCY_CONV_RATE + ":" + valueObj.get(ValueObjectKeys.CURRENCY_CONV_RATE));
					} else {
						cs.setBigDecimal(41, null);
						logger.info(ValueObjectKeys.CURRENCY_CONV_RATE + ":" + "null");
					}

					cs.setString(42, valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));
					logger.info(ValueObjectKeys.TXN_CURRENCY_CODE + ":" + valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE));

					cs.setBigDecimal(43, new BigDecimal(valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT)));
					logger.info(ValueObjectKeys.ORGNL_TRAN_AMOUNT + ":" + valueObj.get(ValueObjectKeys.ORGNL_TRAN_AMOUNT));

					cs.setInt(44, Integer.parseInt(valueObj.get(ValueObjectKeys.PURSE_ID)));
					logger.info(ValueObjectKeys.PURSE_ID + ":" + valueObj.get(ValueObjectKeys.PURSE_ID));

					cs.setString(45, "0");

					cs.setInt(46, 0);
					cs.setString(47, null);
					cs.setString(48, null);
					cs.setInt(49, 0);
					cs.setInt(50, 0);

					cs.setString(51, valueObj.get(ValueObjectKeys.STORE_ID));
					logger.info(ValueObjectKeys.STORE_ID + ":" + valueObj.get(ValueObjectKeys.STORE_ID));

					cs.registerOutParameter(52, Types.VARCHAR);
					cs.registerOutParameter(53, Types.VARCHAR);
					cs.registerOutParameter(54, Types.VARCHAR);
					cs.registerOutParameter(55, Types.NUMERIC);
					cs.registerOutParameter(56, Types.NUMERIC);
					cs.registerOutParameter(57, Types.VARCHAR);
					return cs;
				}
			}, params);

			result[0] = (String) resultMap.get("52");
			result[1] = (String) resultMap.get("53");
			result[2] = (String) resultMap.get("54");
			result[3] = String.valueOf(resultMap.get("55"));
			result[4] = valueObj.get(ValueObjectKeys.TXN_CURRENCY_CODE);
			logger.info("cashOut response: responseCode:{}, responseMsg:{},AuthorizedId:{},AuthorizedAmt:{},currencyCode:{}", result[0],
					result[1], result[2], result[3], result[4]);
			return result;

		}

		catch (Exception e) {
			logger.error("Error in SpilCashoutDAOImpl: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.SYSTEM_ERROR, ResponseCodes.SYSTEM_ERROR);
		}

	}

}
