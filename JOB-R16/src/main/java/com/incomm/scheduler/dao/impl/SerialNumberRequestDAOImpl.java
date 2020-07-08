package com.incomm.scheduler.dao.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.constants.FSAPIConstants;
import com.incomm.scheduler.constants.QueryConstants;
import com.incomm.scheduler.dao.SerialNumberRequestDAO;
import com.incomm.scheduler.utils.Util;

@Repository
public class SerialNumberRequestDAOImpl extends JdbcDaoSupport implements SerialNumberRequestDAO {


	
	@Value("${SERIAL_NUMBER_REQUEST_URL}") 
	String serialNumberURL;
	
	@Value("${B2B_MERCHANT_ID}") String merchantID;
	@Value("${B2B_MERCHANT_NAME}") String merchantName;
	@Value("${ENVIRONMENT}") String environment;
	@Value("${B2B_PROGRAM_TYPE}") String programType;
	@Value("${SERIALNUMBER_REQUESTHEADER}") String headerElement;
	@Value("${B2BFSAPIKEY}") String apiKey;
	@Value("${IS_CONN_SERIAL_REQ}") String status;
	 
	private static final Logger logger = LogManager.getLogger(SerialNumberRequestDAOImpl.class);

	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}

	@Override
	public void getSerialNumber(Long productId, String upc, Long serialNumberQuantity) {

		logger.info(CCLPConstants.ENTER);
		String ipAddrss = "";
		String reqMessage="";
		String respmessage="success";
		try {
			ipAddrss = String.valueOf(InetAddress.getLocalHost().getHostAddress());
			Map<String, String> headerElementMap = new HashMap<>();
			headerElement = FSAPIConstants.SERIALNUMBER_REQUESTHEADER;
			if (headerElement != null) {
				final String[] headArr = headerElement.split(FSAPIConstants.TILDE_OPER);
				for (int i = 0; i < headArr.length; i++) {
					String[] headValueArry = headArr[i].split(":");
					headerElementMap.put(headValueArry[0], headValueArry[1]);
				}
			}

			String prodAttributes = getJdbcTemplate().queryForObject(QueryConstants.GET_PRODUCT_ATTRIBUTES,
					new Object[] { productId }, String.class);
			
			Map<String, Map<String, Object>> productAttributesMap = Util.convertJsonToHashMap(prodAttributes);

			Map<String, Object> prodAttributesMap = productAttributesMap.get("Product");
			
			HashMap<String, String> productCardtype = new HashMap<>();
			productCardtype.put("productDescription", String.valueOf(productId));
			productCardtype.put("merchantID", merchantID);
			productCardtype.put("merchantName", merchantName);
			productCardtype.put("environment", environment);
			productCardtype.put("programType", programType);

			productCardtype.put("dcmsid", (String) prodAttributesMap.get("dcmsId"));
			productCardtype.put("upc", upc);
			productCardtype.put("denomination", (String) prodAttributesMap.get("denominationType"));
			productCardtype.put("fileLocation", FSAPIConstants.FILE_LOCATION);

			productCardtype.put("quantity", String.valueOf(serialNumberQuantity));
			
			
			reqMessage = getJSONMessg(productCardtype);
			if("Y".equalsIgnoreCase(status)) {
			 respmessage = sendRequestTOSerialNumberAPI(reqMessage, serialNumberURL, headerElementMap, apiKey,
					ipAddrss);
			}
			logger.info("logging into SERIAL_NUM_AUDIT table");
			logSerialRequst(productId, reqMessage, respmessage,"Y");

		} catch (UnknownHostException e) {
			logger.error("UnknownHostException  for Serial NumberRequest",e.getMessage());
			logSerialRequst(productId, reqMessage, respmessage,"N");
		}catch(Exception e) {
			logger.error("Main Exception for Serial NumberRequest",e.getMessage());
			logSerialRequst(productId, reqMessage, respmessage,"N");
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public void getSerialNumber(Long productId) {

		logger.info(CCLPConstants.ENTER);
		String ipAddrss = "";
		String reqMessage="";
		String respmessage="success";

		try {
			ipAddrss = String.valueOf(InetAddress.getLocalHost().getHostAddress());
			Map<String, String> headerElementMap = new HashMap<>();
			headerElement = FSAPIConstants.SERIALNUMBER_REQUESTHEADER;
			if (headerElement != null) {
				final String[] headArr = headerElement.split(FSAPIConstants.TILDE_OPER);
				for (int i = 0; i < headArr.length; i++) {
					String[] headValueArry = headArr[i].split(":");
					headerElementMap.put(headValueArry[0], headValueArry[1]);
				}
			}
			boolean flag = false;

			String prodAttributes = getJdbcTemplate().queryForObject(QueryConstants.GET_PRODUCT_ATTRIBUTES,
					new Object[] { productId }, String.class);

			Map<String, Map<String, Object>> productAttributesMap = Util.convertJsonToHashMap(prodAttributes);

			Map<String, Object> prodAttributesMap = productAttributesMap.get("Product");
			String initialQuantity = (prodAttributesMap.containsKey("b2bInitSerialNumQty") && prodAttributesMap.get("b2bInitSerialNumQty")!=null &&  prodAttributesMap.get("b2bInitSerialNumQty")!="" ) ? (String) prodAttributesMap.get("b2bInitSerialNumQty") : "0" ;
			String replenishmentValue = (prodAttributesMap.containsKey("b2bSerialNumAutoReplenishVal") && prodAttributesMap.get("b2bSerialNumAutoReplenishVal")!=null &&  prodAttributesMap.get("b2bSerialNumAutoReplenishVal")!="" ) ? (String) prodAttributesMap.get("b2bSerialNumAutoReplenishVal") : "0" ;
			String replenishmentLevel = (prodAttributesMap.containsKey("b2bSerialNumAutoReplenishLevel") && prodAttributesMap.get("b2bSerialNumAutoReplenishLevel")!=null &&  prodAttributesMap.get("b2bSerialNumAutoReplenishLevel")!="" ) ? (String) prodAttributesMap.get("b2bSerialNumAutoReplenishLevel") : "0" ;
					

			HashMap<String, String> productCardtype = new HashMap<>();
			productCardtype.put("productDescription", String.valueOf(productId));
			productCardtype.put("merchantID", merchantID);
			productCardtype.put("merchantName", merchantName);
			productCardtype.put("environment", environment);
			productCardtype.put("programType", programType);

			productCardtype.put("dcmsid", (String) prodAttributesMap.get("dcmsId"));
			productCardtype.put("upc", (String) prodAttributesMap.get("b2bUpc"));
			productCardtype.put("denomination", (String) prodAttributesMap.get("denominationType"));
			productCardtype.put("fileLocation", FSAPIConstants.FILE_LOCATION);

			String quantity = "";
			long currentSerialVal = getCurrnetSerialValue(productId);
			if (currentSerialVal == 0) {
				quantity = initialQuantity;
				flag = true;
			} else if (currentSerialVal > 0 && currentSerialVal <= Long.parseLong(replenishmentLevel)) {
				quantity = replenishmentValue;
				flag = true;
			}
			productCardtype.put("quantity", quantity);

			if (flag) {
				reqMessage = getJSONMessg(productCardtype);
				if("Y".equalsIgnoreCase(status)) {
					respmessage = sendRequestTOSerialNumberAPI(reqMessage, serialNumberURL, headerElementMap, apiKey,
						ipAddrss);
				}
				logger.info("logging into SERIAL_NUM_AUDIT table");
				logSerialRequst(productId, reqMessage, respmessage, "Y");
			}

		} catch (UnknownHostException e) {
			logger.error("UnknownHostException  for Serial NumberRequest",e.getMessage());
			logSerialRequst(productId, reqMessage, respmessage,"N");
		}catch(Exception e) {
			logger.error("Main Exception for Serial NumberRequest",e.getMessage());
			logSerialRequst(productId, reqMessage, respmessage,"N");
		}
		logger.info(CCLPConstants.EXIT);
	}

	
	private String sendRequestTOSerialNumberAPI(String reqMessage, String serialNumberURL,
			Map<String, String> headerElementMap, String apiKey, String ipAddrss) {
		
		logger.info(CCLPConstants.ENTER);
		HttpURLConnection urlConn = null;
		final String[] resp = new String[2];
		try {
			final URL url = new URL(serialNumberURL);
			final StringBuilder respData = new StringBuilder();
			InputStreamReader in = null;
			urlConn = (HttpURLConnection) (url.openConnection());
			if (urlConn != null) {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

				String date = dateFormat.format(new java.util.Date());

				urlConn.setDoInput(true);
				urlConn.setDoOutput(true);
				urlConn.setUseCaches(false);
				urlConn.setRequestMethod("POST");
				urlConn.setRequestProperty("Accept-Language", "en");
				urlConn.setAllowUserInteraction(false);
				urlConn.setRequestProperty("Content-Type", "application/json");
				Set<String> keys = headerElementMap.keySet();
				Iterator<String> itr = keys.iterator();
				while (itr.hasNext()) {
					String key = itr.next();
					String val = headerElementMap.get(key);

					if (val.equals("date")) {
						urlConn.setRequestProperty(key, date);
					} else if (val.equals("apikey")) {
						urlConn.setRequestProperty(key, apiKey);
					} else if (val.equals("ip")) {
						urlConn.setRequestProperty(key, ipAddrss);
					} else {
						urlConn.setRequestProperty(key, val);
					}
				}
				OutputStreamWriter wr = new OutputStreamWriter(urlConn.getOutputStream());
				wr.write(reqMessage);
				wr.flush();
				final int responseCode = urlConn.getResponseCode();

				InputStream istream = null;
				if (responseCode == 200) {
					istream = urlConn.getInputStream();
				} else {
					istream = urlConn.getErrorStream();
				}
				if (istream != null) {
					in = new InputStreamReader(istream, Charset.defaultCharset());
					final BufferedReader bufferedReader = new BufferedReader(in);

					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						respData.append((char) cp);
					}
					bufferedReader.close();
					resp[0] = respData.toString();

				}
			}
		} catch (Exception exp) {
			logger.error("Error occured"+exp.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return resp[0];
	}

	private void logSerialRequst(Long productId, String reqMessage, String respmessage,String status) {
		
		getJdbcTemplate().update(QueryConstants.LOG_RESPONSE,productId,reqMessage,respmessage,status);
	}

	private String getJSONMessg(HashMap<String, String> productCardtype) {

		JSONObject serialNoReq = new JSONObject(productCardtype);
		return Util.convertAsString(serialNoReq);
	}

	private long getCurrnetSerialValue(Long productId) {

		return getJdbcTemplate().queryForObject(QueryConstants.CURR_SERIAL_NUMBER,
				new Object[] { productId }, Long.class);
	}

	@Override
	public List<Long> getAllProductIds() {

		return getJdbcTemplate().queryForList(QueryConstants.GET_PRODUCT_IDS, Long.class);
	}

}
