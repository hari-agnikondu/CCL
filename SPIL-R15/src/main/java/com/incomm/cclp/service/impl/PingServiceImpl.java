package com.incomm.cclp.service.impl;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import com.incomm.cclp.constants.LoggerConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.response.SPILResponseBuilder;
import com.incomm.cclp.service.PingService;
import com.incomm.cclp.transaction.constants.GeneralConstants;
import com.incomm.cclp.util.LoggerUtil;
import com.incomm.cclp.util.Util;

@Service
public class PingServiceImpl implements PingService {

	private static final Logger logger = LogManager.getLogger(PingServiceImpl.class);

	@Autowired
	SpilDAO spilDao;

	@Autowired
	SPILResponseBuilder responseBuilder;

	@Autowired
	LoggerUtil loggerUtil;

	@Value("${functionCode}")
	private int functionCodeConfig;

	@Value("${app.heartbeat.hide-host-identifier}")
	private boolean hostId;

	@Value("${app.heartbeat.disable}")
	private boolean heartBeat;

	static String localHost = "";

	@PostConstruct
	public static void getServerName() {

		try {
			localHost = InetAddress.getLocalHost()
				.getHostName();
		} catch (Exception ex) {
			logger.error("Exception occured while getting local host: " + ex.getMessage(), ex);
		}
	}

	@Override
	public ResponseEntity<String> getServerDetails(MultiValueMap<String, String> reqHeaders, int functionCode) {

		logger.debug(GeneralConstants.ENTER);

		Map<String, Object> respObj = new HashMap<>();
		Map<String, String> valueObj = new HashMap<>();
		long timeBeforeTransaction = System.currentTimeMillis();
		try {
			valueObj.put(ValueObjectKeys.TRANSACTIONDESC, ValueObjectKeys.PING);
			valueObj.put(ValueObjectKeys.FUNCTION_CODE, String.valueOf(functionCode));

			getHeaders(reqHeaders, valueObj);
			validateFunctionCode(functionCodeConfig, functionCode);

			if (heartBeat) {
				logger.debug("HeartBeat is Disabled");
				return new ResponseEntity<>(reqHeaders, HttpStatus.FORBIDDEN);
			}
			String strDate = spilDao.getServerDate();
			logger.debug(" Server date is : {}", strDate);

			if (Util.isEmpty(strDate)) {
				logger.debug("Server is not running");
				respObj = responseBuilder.pingSuccessResponse("01", "Server is not Running", "", hostId, localHost);
				return new ResponseEntity<>(new JSONObject(respObj).toString(), reqHeaders, HttpStatus.OK);
			}
			valueObj.put(ValueObjectKeys.RESP_MSG, ValueObjectKeys.SUCCESS_MSG);
			respObj = responseBuilder.pingSuccessResponse("00", ValueObjectKeys.SUCCESS_MSG, strDate, hostId, localHost);
		} catch (Exception e) {
			respObj = responseBuilder.pingSuccessResponse("01", "Invalid Headers/function Code", "", hostId, localHost);
			return new ResponseEntity<>(new JSONObject(respObj).toString(), reqHeaders, HttpStatus.OK);
		} finally {
			long timeAfterTransaction = System.currentTimeMillis();
			long timeTaken = timeAfterTransaction - timeBeforeTransaction;
			loggerUtil.logSupport(LoggerConstants.PAYLOAD_MSGTYPE_REQUEST, LoggerConstants.PING_CLASS_METHOD, valueObj.toString(), "", "",
					valueObj);
			loggerUtil.logSupport(LoggerConstants.PAYLOAD_MSGTYPE_RESPONSE, LoggerConstants.PING_CLASS_METHOD, respObj.toString(),
					String.valueOf(timeTaken), "", valueObj);
		}

		logger.debug(GeneralConstants.EXIT);
		return new ResponseEntity<>(new JSONObject(respObj).toString(), reqHeaders, HttpStatus.OK);

	}

	private void getHeaders(MultiValueMap<String, String> reqHeaders, Map<String, String> valueObj) throws ServiceException {

		final Set<String> headerKeys = reqHeaders.keySet();
		for (final String header : headerKeys)
			valueObj.put(header, reqHeaders.get(header)
				.get(0));

		if (!Util.isEmpty(valueObj.get(ValueObjectKeys.CORRELATION_ID)) && !Util.isEmpty(valueObj.get(ValueObjectKeys.CHANNEL))
				&& !Util.isEmpty(valueObj.get(ValueObjectKeys.DATE))) {
			valueObj.put(ValueObjectKeys.DELIVERY_CHANNEL_SHORT_NAME, valueObj.get(ValueObjectKeys.CHANNEL));
			valueObj.put(ValueObjectKeys.INCOM_REF_NUMBER, valueObj.get(ValueObjectKeys.CORRELATION_ID));
			valueObj.put("date", valueObj.get(ValueObjectKeys.DATE));
		} else {
			logger.debug("Invalid Header fields received in the Request");
			throw new ServiceException("Invalid Header");
		}

	}

	private void validateFunctionCode(int code, int functionCode) {
		if (code != functionCode) {
			logger.debug("Invalid function Code received from the Request ");
			throw new ServiceException("invalid function code");
		}
	}

}
