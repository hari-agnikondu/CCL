package com.incomm.cclp.util;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.transaction.constants.GeneralConstants;

@Component
public class HSMCommandBuilder {

	HsmUtil hsmUtil = new HsmUtil();

	Logger logger = LogManager.getLogger(HSMCommandBuilder.class);

	public String verifyCVV(String pan, String expryDate, String srvCode, String cvk, String cvv2, String hsmIpAddress, int hsmPort)
			throws ServiceException {
		logger.debug(GeneralConstants.ENTER);

		String requestData = null;
		String hsmResponse = null;
		Socket socket = null;
		String hsmResponseCode = null;
		try {
			String[][] maCommandList = new String[1][2];
			maCommandList[0][0] = "CY";

			requestData = maCommandList[0][0] + cvk + cvv2 + pan + ";" + expryDate + srvCode;

			socket = hsmUtil.getSocketConnection(hsmIpAddress, hsmPort);

			if (socket == null) {
				return null;
			}
			String header = String.valueOf(Math.random())
				.substring(2, 2 + 4);
			hsmUtil.sendRequest(socket, header + requestData);
			hsmResponse = hsmUtil.getResponse(socket);
			hsmResponseCode = parseHSMResponse(hsmResponse, header);
			if (hsmResponseCode == null) {
				logger.error(SpilExceptionMessages.ERROR_CVV);
				throw new ServiceException(SpilExceptionMessages.ERROR_CVV, ResponseCodes.CVV_VERIFICATION_FAILED);
			}
		} catch (Exception e) {
			logger.error("CVV Verification failed: " + e.getMessage(), e);
			throw new ServiceException(SpilExceptionMessages.ERROR_CVV, ResponseCodes.CVV_VERIFICATION_FAILED);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException ioe) {
					logger.error("IOException Occured while closing socket in verifyCVV()");
				}
			}
		}

		logger.debug(GeneralConstants.EXIT);

		return hsmResponseCode;
	}

	private String parseHSMResponse(String hsmResponse, String header) {
		String hsmHeader = hsmResponse.substring(2, header.length() + 2);

		String hsmResponseCommand = hsmResponse.substring(header.length() + 2, header.length() + 4);
		if (hsmResponseCommand.equalsIgnoreCase("cz")) {
			return hsmResponse.substring(hsmResponseCommand.length() + hsmHeader.length() + 2,
					hsmResponseCommand.length() + hsmHeader.length() + 4);
		} else if (hsmResponseCommand.equalsIgnoreCase("cx")) {
			String hsmResponseCode = hsmResponse.substring(hsmResponseCommand.length() + hsmHeader.length() + 2,
					hsmResponseCommand.length() + hsmHeader.length() + 4);
			if (!hsmResponseCode.equalsIgnoreCase("00")) {
				return null;
			}
			String cvvparse = hsmResponse.substring(hsmResponseCommand.length() + hsmHeader.length() + hsmResponseCode.length() + 2);
			return cvvparse.substring(0, 3);
		} else {
			return null;
		}

	}

	public String genCVV(String pan, String expryDate, String srvCode, String cvk, String hsmipaddress, int hsmport)
			throws ServiceException {

		String requestData = null;
		String hsmResponse = null;
		Socket socket = null;
		String cvv = null;
		try {
			String[][] maCommandList; // ** Command List

			maCommandList = new String[1][2];
			maCommandList[0][0] = "CW";

			requestData = maCommandList[0][0] + cvk + pan + ";" + expryDate + srvCode;

			socket = hsmUtil.getSocketConnection(hsmipaddress, hsmport);

			if (socket == null) {
				return null;
			}
			String header = String.valueOf(Math.random())
				.substring(2, 2 + 4);
			hsmUtil.sendRequest(socket, header + requestData);
			hsmResponse = hsmUtil.getResponse(socket);
			cvv = parseHSMResponse(hsmResponse, header);
			logger.debug("cvv " + cvv);

			socket.close();

		} catch (Exception e) {
			logger.error(SpilExceptionMessages.CCF_ERROR_CVV_GENERATION, e);
			throw new ServiceException(SpilExceptionMessages.CCF_ERROR_CVV_GENERATION);
		}

		return cvv;

	}

}
