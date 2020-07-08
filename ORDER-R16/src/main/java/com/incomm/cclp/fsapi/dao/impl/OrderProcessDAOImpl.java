package com.incomm.cclp.fsapi.dao.impl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.bean.CSSResponseCode;
import com.incomm.cclp.fsapi.bean.ErrorMsgBean;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;
import com.incomm.cclp.fsapi.dao.OrderProcessDAO;
import com.incomm.cclp.fsapi.helper.APIHelper;
import com.incomm.cclp.fsapi.response.ResponseBuilder;
import com.incomm.cclp.fsapi.service.impl.PostBackProcessImpl;
import com.incomm.cclp.util.Util;

@Repository
public class OrderProcessDAOImpl extends JdbcDaoSupport implements OrderProcessDAO{

	@Value("${PSTBK_ORDER_ACTIVATION_STATUS_UPDATE}") 
	String PSTBK_ORDER_ACTIVATION_STATUS_UPDATE;
	
	@Value("${PSTBK_RELOAD}") 
	String PSTBK_RELOAD;
	
	@Value("${PSTBK_RENEWAL}") 
	String PSTBK_RENEWAL;
	
	@Value("${PSTBK_REPLACEMENT}") 
	String PSTBK_REPLACEMENT;
	
	@Autowired
	PostBackProcessImpl postBackProcessImpl;
	
	@Autowired
	ResponseBuilder responseBuilder;
	
	
	@Autowired
    public void setDs(@Qualifier("orderDs") DataSource dataSource) {
         setDataSource(dataSource);
    }
	
	private final Logger log = LogManager.getLogger(this.getClass());
	
	@Autowired
	public JdbcTemplate jdbctemplate;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void saveOrders(final Map<String, Object> valuMap) throws SQLException {

		int saveOrderCnt = 0;
		List<ErrorMsgBean> errorList = null;
		int succCnt=0;
		int failCnt=0;
	
		Connection conn = null;
		
	
		final List<Map<String, Object>> lineItemList = (List<Map<String, Object>>) valuMap.get(FSAPIConstants.ORDER_LINE_ITEMS);
		final StringBuilder lineItemFailErrMsg = new StringBuilder();
		List<ErrorMsgBean> lineItemErrorList=new LinkedList<>();
		PreparedStatement orderLineItenStmt = null;

		try {

			List<SqlParameter> params = new ArrayList<>();

			DataSource con = getDataSource();
			conn = con.getConnection();
			orderLineItenStmt = conn.prepareCall(QueryConstants.ORDER_INSERT_ORDER_LINEITEM);

			for (final Map<String, Object> tempMap : lineItemList) {
				String errorMsg = "";
				orderLineItenStmt.clearParameters();
				final List<ErrorMsgBean> lineItemErrList = (List<ErrorMsgBean>) tempMap
						.get(FSAPIConstants.LINEITEM_ERR_LIST);
				errorMsg = (String) tempMap.get(FSAPIConstants.ORDER_ERRMSG);
				lineItemErrorList.addAll(lineItemErrList);
				if (errorMsg != null && !FSAPIConstants.ORDER_EMPTY_STRING.equals(errorMsg)) {
					tempMap.put(FSAPIConstants.ORDER_LINE_ITEMSTATUS, FSAPIConstants.ORDER_REJECT_STATUS);
					tempMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, tempMap.get(FSAPIConstants.ORDER_RESPONSE_CODE));
					tempMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, errorMsg);
					failCnt++;
				} else if (lineItemErrList != null && !lineItemErrList.isEmpty()) {
					final ErrorMsgBean tempBean = lineItemErrList.get(0);
					tempMap.put(FSAPIConstants.ORDER_LINE_ITEMSTATUS, FSAPIConstants.ORDER_REJECT_STATUS);
					tempMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
							tempBean.getRespCode() != null ? tempBean.getRespCode()
									: FSAPIConstants.ORDER_RESP_DECRESCODE);
					tempMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, tempBean.getErrorMsg());
					failCnt++;
					for (final ErrorMsgBean tempBeans : lineItemErrList) {
						lineItemFailErrMsg.setLength(0);
						lineItemFailErrMsg.append(tempBeans.getErrorMsg());
						lineItemFailErrMsg.append(FSAPIConstants.COLON_SEPARATOR);
					}
					tempMap.put(FSAPIConstants.ORDER_LINE_ITEMERRMSG, lineItemFailErrMsg.toString());
				} else {
					tempMap.put(FSAPIConstants.ORDER_LINE_ITEMSTATUS, FSAPIConstants.ORDER_ACCEPT_STATUS);
					tempMap.put(FSAPIConstants.ORDER_LINE_ITEMERRMSG, FSAPIConstants.OK);
					tempMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, B2BResponseCode.SUCCESS);
					tempMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.ORDER_LINEITEM_SUSCMSG);
					succCnt++;
				}
				int qty = 0;
				try {
					qty = Integer.parseInt(String.valueOf(tempMap.get(FSAPIConstants.ORDER_QUANTITY)));
				} catch (Exception ex) {
					log.error(" Exception Occured ...converting quantity:", ex);
				}

				log.debug("productId::::" + tempMap.get(FSAPIConstants.ORDER_PRODUCT_ID));
				
				
				

				orderLineItenStmt.setString(1, String.valueOf(valuMap.get(FSAPIConstants.ORDER_ORDER_ID)));
				orderLineItenStmt.setInt(2,
						Integer.parseInt(String.valueOf(valuMap.get(FSAPIConstants.ORDER_PARTNERID))));
				orderLineItenStmt.setString(3, String.valueOf(tempMap.get(FSAPIConstants.ORDER_LINE_ITEM_ID)));
				orderLineItenStmt.setInt(4,
						Integer.parseInt(String.valueOf(Util.isEmpty(tempMap.get(FSAPIConstants.ORDER_PRODUCT_ID)+"")?"0":tempMap.get(FSAPIConstants.ORDER_PRODUCT_ID))));
				/* orderLineItenStmt.setString(5, null); purse ID */
				orderLineItenStmt.setString(5, String.valueOf(tempMap.get(FSAPIConstants.ORDER_DENOMINATION)));
				String empLine = String.valueOf(valuMap.get(FSAPIConstants.ORDER_EMBOSSED_LINE));

				if (APIHelper.emptyCheck(empLine)) {
					orderLineItenStmt.setString(6, String.valueOf(tempMap.get(FSAPIConstants.ORDER_EMBOSSED_LINE)));
				} else {
					
					orderLineItenStmt.setString(6, null);
				}
				
				/**
				 * if any value is larger than database size doing substring for insert
				 */
				
				String offerCode = String.valueOf(tempMap.get(FSAPIConstants.ORDER_OFFER_CODE));
				if (offerCode != null && offerCode.length() > 20) {
					offerCode = offerCode.substring(0,20);
						logger.debug("Transaction failed rrn: "+offerCode);
						tempMap.put(FSAPIConstants.ORDER_OFFER_CODE, offerCode);
				}
				orderLineItenStmt.setString(7, String.valueOf(tempMap.get(FSAPIConstants.ORDER_OFFER_CODE)));
				orderLineItenStmt.setInt(8, qty);
				orderLineItenStmt.setString(9, String.valueOf(tempMap.get(FSAPIConstants.ORDER_LINE_ITEMSTATUS)));
				orderLineItenStmt.setString(10, String.valueOf(tempMap.get(FSAPIConstants.ORDER_LINE_ITEMERRMSG)));

				String empLineOne = String.valueOf(valuMap.get(FSAPIConstants.ORDER_EMBOSSED_LINE1));

				if (APIHelper.emptyCheck(empLineOne)) {
					orderLineItenStmt.setString(11, String.valueOf(tempMap.get(FSAPIConstants.ORDER_EMBOSSED_LINE1)));
				} else {
					orderLineItenStmt.setString(11, null);
				}

				/*
				 * orderLineItenStmt.setString(13, null); //tracking no
				 * orderLineItenStmt.setDate(14, null); // shipping date
				 * orderLineItenStmt.setString(15, null); //parent OID
				 * orderLineItenStmt.setString(16, null); //CCF Flag
				 * orderLineItenStmt.setString(17, null); //return file Flag
				 */
				orderLineItenStmt.setString(12, String.valueOf(tempMap.get(FSAPIConstants.ORDER_PACKAGE_ID)));
				orderLineItenStmt.setString(13, String.valueOf(tempMap.get(FSAPIConstants.FUNDING_OPTION)));
				orderLineItenStmt.setString(14, String.valueOf(tempMap.get(FSAPIConstants.INITIAL_LOAD_OPTION)));
				orderLineItenStmt.setString(15, String.valueOf(tempMap.get(FSAPIConstants.FUNDING_OVERRIDE)));
				
				
				
				orderLineItenStmt.setString(16, String.valueOf(tempMap.get(ValueObjectKeys.PURSE_ID)));
				

				orderLineItenStmt.addBatch();
			}


			final String acceptPartialOrders = String.valueOf(valuMap.get(FSAPIConstants.ORDER_ACCEPT_PARTIAL_ORDERS));
			String partialFlag = FSAPIConstants.ORDER_FSAPI_F;
			if (acceptPartialOrders != null && (FSAPIConstants.ORDER_FSAPI_TRUE.equalsIgnoreCase(acceptPartialOrders)
					|| FSAPIConstants.ORDER_FSAPI_TRUE1.equals(acceptPartialOrders))) {
				partialFlag = FSAPIConstants.ORDER_FSAPI_T;
			}
			errorList = (List<ErrorMsgBean>) valuMap.get(FSAPIConstants.ORRDER_ERROR_LIST);
			if (CollectionUtils.isEmpty(errorList) && succCnt > 0 && FSAPIConstants.ORDER_FSAPI_T.equals(partialFlag)) {
				valuMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_ACCEPT_STATUS);
				valuMap.put(FSAPIConstants.ORDER_STATUS_ERRMSG, FSAPIConstants.OK);
				valuMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, B2BResponseCode.SUCCESS);
				valuMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.SUCCESS_MSG);
			} else {
				if (CollectionUtils.isEmpty(errorList) && failCnt == 0) {
					valuMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_ACCEPT_STATUS);
					valuMap.put(FSAPIConstants.ORDER_STATUS_ERRMSG, FSAPIConstants.OK);
					valuMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, B2BResponseCode.SUCCESS);
					valuMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.SUCCESS_MSG);
				} else {
					final StringBuilder errMsg = new StringBuilder();
					if (!CollectionUtils.isEmpty(errorList)) {
						for (final ErrorMsgBean tempErrBean : errorList) {
							errMsg.append(tempErrBean.getErrorMsg());
							errMsg.append(FSAPIConstants.SEMICOLON_SEPARATOR);
						}
					}
					errMsg.append(lineItemFailErrMsg.toString());
					valuMap.put(FSAPIConstants.ORDER_STATUS, FSAPIConstants.ORDER_REJECT_STATUS);
					valuMap.put(FSAPIConstants.ORDER_STATUS_ERRMSG, errMsg.toString());
					if (errorList != null && !errorList.isEmpty()) {
						valuMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, errorList.get(0).getRespCode());
						valuMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, errorList.get(0).getErrorMsg());
					} else if (!lineItemErrorList.isEmpty()) {
						valuMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, lineItemErrorList.get(0).getRespCode());
						valuMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, lineItemErrorList.get(0).getErrorMsg());
					} else {
						valuMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
								B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
						valuMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.ORDER_REJECT_STATUS);
						for (final Map<String, Object> tempMap : lineItemList) {
							tempMap.put(FSAPIConstants.ORDER_LINE_ITEMSTATUS, FSAPIConstants.ORDER_REJECT_STATUS);
							tempMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
									B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
							tempMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.ORDER_REJECT_STATUS);
						}
					}
				}

			}

			log.info("--------saveOrders---------");

			List<SqlParameter> orderParams = new ArrayList<>();
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.NUMERIC));
			orderParams.add(new SqlParameter(Types.NUMERIC));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.NUMERIC));
			orderParams.add(new SqlParameter(Types.NUMERIC));
			orderParams.add(new SqlParameter(Types.VARCHAR));
			orderParams.add(new SqlParameter(Types.VARCHAR));

			Map<String, Object> orderResultMap = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {

					CallableStatement orderStmt = con.prepareCall(QueryConstants.ORDER_INSERT_ORDER);
					int issuerId = getIssuerByProductId(String.valueOf(valuMap.get(FSAPIConstants.ORDER_PRODUCT_ID)));
					orderStmt.setString(1, String.valueOf(valuMap.get(FSAPIConstants.ORDER_ORDER_ID)));
					orderStmt.setInt(2, Integer.parseInt(String.valueOf(valuMap.get(FSAPIConstants.ORDER_PARTNERID))));
					orderStmt.setInt(3, issuerId);
					orderStmt.setString(4, String.valueOf(valuMap.get(FSAPIConstants.ORDER_MERCHANT_ID)));
					orderStmt.setString(5, String.valueOf(valuMap.get(FSAPIConstants.ORDER_POST_BACK_RESPONSE)));
					orderStmt.setString(6, String.valueOf(valuMap.get(FSAPIConstants.ORDER_ORDPOST_BACK_URL)));
					orderStmt.setString(7, String.valueOf(valuMap.get(FSAPIConstants.ORDER_ACTIVATION_CODE)));
					orderStmt.setString(8, String.valueOf(valuMap.get(FSAPIConstants.ORDER_SHIPPING_METHOD)));
					orderStmt.setString(9, String.valueOf(valuMap.get(FSAPIConstants.ORDER_STATUS)));

					final Map<String, String> addrMap = (Map<String, String>) valuMap
							.get(FSAPIConstants.ORDER_SHIP_ADDRESS);
					if (addrMap != null) {
						orderStmt.setString(10, String.valueOf(addrMap.get(FSAPIConstants.ORDER_ADDRESS_LINE1)));
						orderStmt.setString(11, String.valueOf(addrMap.get(FSAPIConstants.ORDER_ADDRESS_LINE2)));
						orderStmt.setString(12, String.valueOf(addrMap.get(FSAPIConstants.ORDER_ADDRESS_LINE3)));
						orderStmt.setString(13, String.valueOf(addrMap.get(FSAPIConstants.ORDER_CITY)));
						orderStmt.setString(14, String.valueOf(addrMap.get(FSAPIConstants.ORDER_STATE)));
						orderStmt.setString(15, String.valueOf(addrMap.get(FSAPIConstants.ORDER_POSTAL_CODE)));
						orderStmt.setString(16, String.valueOf(addrMap.get(FSAPIConstants.ORDER_COUNTRY)));
					} else {
						orderStmt.setString(10, null);
						orderStmt.setString(11, null);
						orderStmt.setString(12, null);
						orderStmt.setString(13, null);
						orderStmt.setString(14, null);
						orderStmt.setString(15, null);
						orderStmt.setString(16, null);

					}

					orderStmt.setString(17, String.valueOf(valuMap.get(FSAPIConstants.ORDER_FIRST_NAME)));
					orderStmt.setString(18, String.valueOf(valuMap.get(FSAPIConstants.ORDER_MIDDLE_INITIAL)));
					orderStmt.setString(19, String.valueOf(valuMap.get(FSAPIConstants.ORDER_LAST_NAME)));
					orderStmt.setString(20, String.valueOf(valuMap.get(FSAPIConstants.ORDER_PHONE)));
					orderStmt.setString(21, String.valueOf(valuMap.get(FSAPIConstants.ORDER_EMAIL)));
					orderStmt.setString(22, String.valueOf(valuMap.get(FSAPIConstants.ORDER_SHIP_TO_COMPANY_NAME)));
					orderStmt.setString(23, String.valueOf(valuMap.get(FSAPIConstants.ORDER_SHIPPING_FEE)));
					orderStmt.setString(24, String.valueOf(valuMap.get(FSAPIConstants.ORDER_STATUS_ERRMSG)));
					orderStmt.setString(25, String.valueOf(valuMap.get(FSAPIConstants.ORDER_CHNL_ID)));
					orderStmt.setString(26, (String.valueOf(valuMap.get(FSAPIConstants.ORDER_ACCEPT_PARTIAL_ORDERS))));
					orderStmt.setString(27, (String.valueOf(valuMap.get(FSAPIConstants.ORDER_TYPE))));
					orderStmt.setString(28, (String.valueOf(valuMap.get(FSAPIConstants.FULFILLMENT_TYPE))));
					orderStmt.setString(29, (String.valueOf(valuMap.get(FSAPIConstants.CHANNEL))));
					orderStmt.setString(30, (String.valueOf(valuMap.get(FSAPIConstants.CLIENTID))));
					orderStmt.setString(31, (String.valueOf(valuMap.get(FSAPIConstants.CLIENTNAME))));
					orderStmt.setInt(32, 1);
					orderStmt.setInt(33, 1);
					orderStmt.setString(34, (String.valueOf(valuMap.get(FSAPIConstants.ORDER_ORDER_SHIP_STATE))));
					orderStmt.setString(35, (String.valueOf(valuMap.get(FSAPIConstants.ORDER_PROGRAM_ID))));
					orderStmt.setString(36, Util.objectToString(valuMap.get(FSAPIConstants.IS_SEPARATE_CCF_REQUIRED)));
					return orderStmt;
				}
			}, params);
			orderLineItenStmt.executeBatch();

			saveOrderCnt = (int) orderResultMap.get("#update-count-1");

		} catch(Exception e) {
			logger.error(e);
		}finally {
			if(orderLineItenStmt != null) {
				orderLineItenStmt.close();
			}
			if(conn != null) {
				conn.close();
			}
			
		}
		if (saveOrderCnt != 1) {
			if (errorList != null && !errorList.isEmpty()) {
				valuMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, errorList.get(0).getRespCode());
				valuMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, errorList.get(0).getErrorMsg());
			} else {
				valuMap.put(FSAPIConstants.ORDER_RESPONSE_CODE, FSAPIConstants.ORDER_RESPONSE_REJRESCODE);
				valuMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.ORDERFAIL_ERRMSG);
			}
		}

	}
		
	public int getIssuerByProductId(String productId) {

		try {

			return getJdbcTemplate().queryForObject(QueryConstants.GET_ISSUER_ID, new Object[] { productId },
					Integer.class);
		} catch (Exception e) {
			return 0;
		}
	}

		@Override
		public void processOrder(Map<String, Object> valueHashMap) {
			log.debug("inside processing the order");
			final String orderId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_ORDER_ID));
			final String partnerId = String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID));
			
			List<SqlParameter> params = new ArrayList<>();
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlParameter(Types.VARCHAR));
			params.add(new SqlOutParameter("error", Types.VARCHAR));
			
				Map<String, Object> resultMap = getJdbcTemplate().call(new CallableStatementCreator() {
				public CallableStatement createCallableStatement(Connection con) throws SQLException {
					CallableStatement cs = con.prepareCall(QueryConstants.ORDER_PROC_CALL);
					cs.setString(1, orderId);
					cs.setString(2, partnerId);

					cs.registerOutParameter(3, Types.VARCHAR);
					
					return cs;
				}
			}, params);
	
			log.debug( "order process response: "+ resultMap.get("error").toString());
			log.debug("exit processing the order");
			
		}
	@Override
	public void b2bCheckOrderStatus(Map<String, Object> valueHashMap) {

		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;
		String orderStatus = "";
		String shippingMethod = "";
		String responseMessage = "";
		final Map<String, String> lineItemStatus = new HashMap<>();
		try {

			DataSource con = getDataSource();
			conn = con.getConnection();

			String query = "";
			@SuppressWarnings("unchecked")
			final Set<String> lineItemSet = (Set<String>) valueHashMap.get(FSAPIConstants.ORDER_LINE_ITEM_ID);
			if (lineItemSet.isEmpty()) {
				query = QueryConstants.B2B_ORDER_STATUS;
			} else {
				query = QueryConstants.B2B_ORDER_STATUS;
				query = query + "AND LINE_ITEM_ID in (  ";
				for (int i = 0; i < lineItemSet.size(); i++) {
					if (i == 0)
						query = query + "?";
					else
						query = query + ",?";
				}
				query = query + ")";
			}

			pStmt = conn.prepareStatement(query);
			pStmt.setString(1, String.valueOf(valueHashMap.get(FSAPIConstants.ORDERID)));
			pStmt.setString(2, String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID)));
			if (!lineItemSet.isEmpty()) {
				int j = 3;
				for (String temp : lineItemSet) {
					pStmt.setString(j, temp);
					j++;
				}
			}
			resultSet = pStmt.executeQuery();

			while (resultSet.next()) {
				orderStatus = resultSet.getString("order_status");
				shippingMethod = resultSet.getString("shipping_method") != null ? resultSet.getString("shipping_method")
						: "";
				responseMessage = resultSet.getString("error_msg");
				String responseCode = B2BResponseCode.SUCCESS;
				if (responseCode.equalsIgnoreCase("R0001"))
					responseMessage = FSAPIConstants.SUCCESS_MSG;
				else
					responseMessage = FSAPIConstants.INVALID_ORDER_STATUS;
				if (("").equalsIgnoreCase(resultSet.getString("line_item_status"))) {
					responseCode = B2BResponseCode.INVALID_LINE_ITEM_STATUS;
					responseMessage = FSAPIConstants.INVALID_LINEITEM_STATUS;
				} else {
					responseCode = B2BResponseCode.SUCCESS;
					responseMessage = FSAPIConstants.SUCCESS_MSG;
				}
				lineItemStatus.put(resultSet.getString("line_item_id"),
						resultSet.getString("line_item_status") + ":" + responseMessage + ":" + responseCode);

			}
			valueHashMap.put(FSAPIConstants.SHIPPINGMETHOD, shippingMethod);
			valueHashMap.put(FSAPIConstants.ORDER_STATUS, orderStatus);
			valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
					("").equalsIgnoreCase(orderStatus) ? B2BResponseCode.INVALID_LINE_ITEM_STATUS
							: B2BResponseCode.SUCCESS);
			if (valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_CODE).equals("00"))
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.SUCCESS_MSG);
			else
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG, FSAPIConstants.INVALID_ORDER_STATUS);
			valueHashMap.put(FSAPIConstants.ORDER_LINE_ITEMDTLS, lineItemStatus);

		} catch (SQLException sql) {
			log.error("SQL Exception Occured while Getting the Order Line Item Details {} ", sql.getMessage());
		} catch (Exception e) {
			log.error("Error Occured while Getting the Order Line Item Details - {} ", e.getMessage());
		} finally {
			if (conn != null)
				try {
					if (pStmt != null)
						pStmt.close();
					conn.close();
				} catch (SQLException e) {
					log.error("Error Occured while closing the connection Object {}", e.getMessage());
				}
		}
	}

		@Override
		public List<Map<String, String>> getLineItemDtls(String key, Map<String, Object> valueHashMap, List<Map<String, String>> tempList,
			String apiName) {

		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet resultSet = null;

		try {

			DataSource con = getDataSource();
			conn = con.getConnection();

			pStmt = conn.prepareStatement(QueryConstants.B2B_CARD_DETAILS);
			pStmt.setString(1, String.valueOf(valueHashMap.get(FSAPIConstants.ORDERID)));
			pStmt.setString(2, key);
			pStmt.setString(3, String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID)));

			resultSet = pStmt.executeQuery();

			while (resultSet.next()) {
				Map<String, String> temp = new HashMap<>();
				temp.put(FSAPIConstants.PRINTER_RESPONSE, resultSet.getString("PRINTER_RESPONSE")!=null ? resultSet.getString("PRINTER_RESPONSE") : "");
				temp.put(FSAPIConstants.PROXYNUMBER, resultSet.getString("PROXY_NUMBER")!= null ? resultSet.getString("PROXY_NUMBER") : "");
				temp.put(FSAPIConstants.PIN, resultSet.getString("PIN")!=null ? resultSet.getString("PIN") : "");
				temp.put(FSAPIConstants.PROXY_PIN_ENCR, resultSet.getString("PROXY_PIN_ENCR")!=null ? resultSet.getString("PROXY_PIN_ENCR") : "");
				temp.put(FSAPIConstants.SERIALNUMBER, resultSet.getString("SERIAL_NUMBER")!=null ? resultSet.getString("SERIAL_NUMBER") : "");

				if(resultSet.getString("CARD_STATUS")!=null && ( "PRINTER_PENDING".equalsIgnoreCase(resultSet.getString("CARD_STATUS")) || "PRINTER_SENT".equalsIgnoreCase(resultSet.getString("CARD_STATUS"))))  {
					temp.put(FSAPIConstants.CARD_STATUS,"INACTIVE");
				}
				else {
					temp.put(FSAPIConstants.CARD_STATUS, resultSet.getString("CARD_STATUS")!=null ? resultSet.getString("CARD_STATUS") : "");
				}
				
				temp.put(FSAPIConstants.TRACKING_NUMBER, resultSet.getString("TRACKING_NBR")!=null ? resultSet.getString("TRACKING_NBR") : "");
				temp.put(FSAPIConstants.SHIPPING_DATE, resultSet.getString("SHIPPING_DATE")!=null ? resultSet.getString("SHIPPING_DATE") : "");
				temp.put(FSAPIConstants.HASH_CARDNO, resultSet.getString("CARD_NUM_HASH")!=null ? resultSet.getString("CARD_NUM_HASH") : "");
				
				tempList.add(temp);
			}

			if (tempList.isEmpty()) {
				Map<String, String> temp = new HashMap<>();
				temp.put(FSAPIConstants.PRINTER_RESPONSE, "");
				temp.put(FSAPIConstants.PROXYNUMBER, "");
				temp.put(FSAPIConstants.PIN, "");
				temp.put(FSAPIConstants.PROXY_PIN_ENCR, "");
				temp.put(FSAPIConstants.SERIALNUMBER, "");
				temp.put(FSAPIConstants.CARD_STATUS, "");
				temp.put(FSAPIConstants.TRACKING_NUMBER, "");
				temp.put(FSAPIConstants.SHIPPING_DATE, "");
				temp.put(FSAPIConstants.HASH_CARDNO, "");
				tempList.add(temp);

			}
		} catch (Exception e) {
			log.error("Error Occured while Getting the Card details {} ", e.getMessage());
		}

		finally {
			if (conn != null)
				try {
					if (pStmt != null)
						pStmt.close();
					conn.close();
				} catch (SQLException e) {
					log.error("Error Occured while closing the connection Object {}", e.getMessage());
				}
		}

		return tempList;

	}

		@Override
		public void responsePostBackLogger(String apiName, String orderId, String reqHeaders, String reqStr,
				String respCode, String respMsg, String respStr, String serverID, long timeTaken, String postBackUrl) {
			
			try {
			
			String postBackURL = postBackUrl;

			
				log.info("------apiName--"+apiName);
				

				serverID = InetAddress.getLocalHost().getHostName();
				
				if("ORDER".equals(apiName) ||"ORDERACTIVATION".equals(apiName) || "SERIALNUMBERRANGEACTIVATION".equals(apiName) || "CANCELORDER".equals(apiName) ) {
					postBackURL = PSTBK_ORDER_ACTIVATION_STATUS_UPDATE;
					
				}
				
				log.info("------condition check--"+("RELOAD_POSTBACK".equalsIgnoreCase(apiName)));
				 if("RELOAD_POSTBACK".equalsIgnoreCase(apiName)) {
					postBackURL = PSTBK_RELOAD;
				}
				else if("REPLACERELOAD_PTBACK".equalsIgnoreCase(apiName)) {
					postBackURL = PSTBK_REPLACEMENT;
				}
				
				log.info("------postBackUrl--"+postBackURL);
				postBackProcessImpl.sendResponse(apiName,orderId,reqHeaders,reqStr,respCode,respMsg,respStr,serverID,timeTaken,postBackURL);
				

			}catch (Exception e) {
				log.error("Exception while sendResponse method calling Logger() " + e);
			}

			

			
		}

		@Override
		public void reqRespLogger(String apiName, String reqHeaders, String reqStr, String respCode, String respMsg,
				String respStr, String serverID, long timeTaken) {
			int insertCount = 0;

			try {
				log.info("----responseLogger-------");

				serverID = InetAddress.getLocalHost().getHostName();

				insertCount = getJdbcTemplate().update(QueryConstants.FSAPI_REQ_RES_LOG, apiName, reqHeaders,
						 reqStr, respCode,  respMsg,  respStr,  serverID,  timeTaken);

			} catch (UnknownHostException e) {
				log.error("Exception while loading responseLogger():", e);
			} catch (Exception e) {
				log.error("Main Exception while loading responseLogger()",e);
			}

			log.info("-------responseLogger--------" + insertCount);
		}
		
		@Override
		public Long checkPanCount(String key) {
			return (getJdbcTemplate().queryForObject(QueryConstants.CHECK_PAN_COUNT, new Object[] { key }, Long.class));
		}

		@Override
		public int checkSerialCount(String key, Long value) {
			return (getJdbcTemplate().queryForObject(QueryConstants.CHECK_SERIAL_NUMBER, new Object[] { key,value }, Integer.class));
		}
		
		@Override
		public int updatePostBackUrl(String name,String url){
			return getJdbcTemplate().update(QueryConstants.POST_BACK_URL_UPDATE_QRY, name, url);
		}
		@Override
		public int deletePostBackUrl(){
			return getJdbcTemplate().update(QueryConstants.POST_BACK_URL_DELETE_QRY);
		}
		
		@Override
		public void getLineItemOrderStatus(Map<String, Object> valueHashMap) {
			try {
				Map<String, String> lineItemStatus = new HashMap<>();
				getJdbcTemplate().query(QueryConstants.B2B_ORDER_STATUS,  new ResultSetExtractor<Map<String,Object>>(){
					@Override
					public Map<String,Object> extractData(ResultSet rs) throws SQLException {
						while(rs.next()){
							String shippingMethod = rs.getString("shipping_method");
							valueHashMap.put(FSAPIConstants.SHIPPINGMETHOD, shippingMethod!=null?shippingMethod:"");
							valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_STATUS, rs.getString("order_status"));
							String responseCode = B2BResponseCode.SUCCESS; 
							String lineItemSts=rs.getString("line_item_status");
							valueHashMap.put(FSAPIConstants.POSTBACK_URL, rs.getString("POSTBACK_URL"));
							if (Util.isEmpty(lineItemSts)) {
								responseCode = B2BResponseCode.INVALID_LINE_ITEM_STATUS;
							} 
							CSSResponseCode respCode;
							try {
								respCode = responseBuilder.getCSSResponseCodeByRespId(valueHashMap.get(ValueObjectKeys.DELIVERYCHNL)+"",responseCode);
								lineItemStatus.put(rs.getString("line_item_id"),lineItemSts + ":" + respCode.getResponseDescription() + ":" + respCode.getChannelResponseCode());

							} catch (ServiceException e) {
								log.error("Error Occured while Getting the Order Line Item Details {} "+e, e.getMessage());
								valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
										B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
								valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
										B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
							}
						
						}
						return null;
					}

				}, String.valueOf(valueHashMap.get(FSAPIConstants.ORDERID)),String.valueOf(valueHashMap.get(FSAPIConstants.ORDER_PARTNERID)));

				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
						("").equalsIgnoreCase(valueHashMap.get(FSAPIConstants.ORDER_RESPONSE_STATUS)+"")? B2BResponseCode.INVALID_LINE_ITEM_STATUS
								: B2BResponseCode.SUCCESS);
				valueHashMap.put(FSAPIConstants.ORDER_LINE_ITEMDTLS, lineItemStatus);

			}catch (Exception e) {
				log.error("Error Occured while Getting the Order Line Item Details {} ", e.getMessage());
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_CODE,
						B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
				valueHashMap.put(FSAPIConstants.ORDER_RESPONSE_MSG,
						B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
			}
		}
		
		@Override
		public int updateMailAlertDetails(String smtpHostPropKey,String smtpHostAddress) {

			return getJdbcTemplate().update(QueryConstants.UPDATE_SMTP_HOST_DETAILS,smtpHostPropKey,smtpHostAddress);
		}

		@Override
		public int deleteMailAlertDetails() {
			
			return getJdbcTemplate().update(QueryConstants.DELETE_SMTP_HOST_DETAILS);
		}

		@Override
		public int getReplacedCardOldStatus(String serialNumber) {
			return getJdbcTemplate().queryForObject(QueryConstants.CHECK_DAMAGED_CARD_SERIAL, new Object[] { serialNumber }, Integer.class);
		}

}
