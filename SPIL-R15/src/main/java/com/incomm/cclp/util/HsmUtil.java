package com.incomm.cclp.util;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.incomm.cclp.constants.SpilExceptionMessages;
import com.incomm.cclp.exception.ServiceException;

@Component
@PropertySource("classpath:application.properties")
public class HsmUtil {

	Logger logger = LogManager.getLogger(HSMCommandBuilder.class);

	public Socket getSocketConnection(String hsmIpAddress, int hsmPort) throws ServiceException {
		Socket ptSocket = null;

		try {
			InetAddress piad = InetAddress.getByName(hsmIpAddress);
			ptSocket = new Socket(piad, hsmPort);
		} catch (IOException e) {
			throw new ServiceException(SpilExceptionMessages.ERROR_CVV);
		}
		return ptSocket;
	}

	public void sendRequest(Socket psSocket, String psRequest) throws ServiceException {

		try {
			BufferedOutputStream poBWMsg = new BufferedOutputStream(psSocket.getOutputStream());
			int piLen = psRequest.length();
			poBWMsg.write(piLen >> 8); // qoutient (1 byte out of 2 byte length
			// indicator)
			poBWMsg.write(piLen); // output stream will only write one byte at a
			// time.it will ignore remaining bytes.
			poBWMsg.write(psRequest.getBytes("ISO-8859-1"));// command message
			poBWMsg.flush();
		} catch (IOException e) {
			logger.error(SpilExceptionMessages.ERROR_CVV, e);
			throw new ServiceException(SpilExceptionMessages.ERROR_CVV);
		}
	}

	public String getResponse(Socket psSocket) throws IOException {
		String psData = "";
		byte[] b = new byte[100];
		InputStream poInpStr = psSocket.getInputStream();
		DataInputStream doInpStr = new DataInputStream(poInpStr);
		if (doInpStr.read(b) > 0)
			psData = new String(b);

		return psData;

	}

}
