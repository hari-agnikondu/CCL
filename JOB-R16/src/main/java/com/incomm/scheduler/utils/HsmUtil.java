package com.incomm.scheduler.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
public class HsmUtil {
    
	private static final Logger logger = LogManager.getLogger(HsmUtil.class);
	
		public Socket getSocketConnection(String hsmIpAddress,int hsmPort ) {
		Socket ptSocket=null;
		try {
		logger.info("hsmIpAddress"+hsmIpAddress);
		logger.info("hsmPort"+hsmPort);
			InetAddress piad = InetAddress.getByName(hsmIpAddress);
			ptSocket = new Socket(piad,hsmPort);
		}catch(Exception e) {
			logger.error(e);
		}
		
		return ptSocket;
	}
	
	public void sendRequest(Socket psSocket, String psRequest) {
		try {
		BufferedOutputStream poBWMsg = new BufferedOutputStream(psSocket.getOutputStream());
		int piLen = psRequest.length();
		
		poBWMsg.write(piLen >> 8); // qoutient (1 byte out of 2 byte length
									// indicator)
		poBWMsg.write(piLen); // output stream will only write one byte at a
								// time.it will ignore remaining bytes.
		poBWMsg.write(psRequest.getBytes("ISO-8859-1"));// command message
		poBWMsg.flush();
		}catch(Exception e) {
			logger.error(e);
		}
	}

	public String getResponse(Socket psSocket) {
		String psData = "";
		byte[] b = new byte[100];
		try {
		InputStream poInpStr = psSocket.getInputStream();
		BufferedInputStream pBISMessage = new BufferedInputStream(poInpStr, 1024);
		pBISMessage.read(b);    
		/* while ((pBISMessage.read(b)) > 0) {  // need to get clarification as it not working

		psData = new String(b);
		 }*/
		psData = new String(b);
		}catch(Exception e) {
			logger.error(e);
		}
		return psData;
	}


}
