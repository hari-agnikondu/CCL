package com.incomm.scheduler.tasklet;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HSMConnection {
	
	private static final Logger logger = LogManager.getLogger(HSMConnection.class);
	
	
	/*@Value("${HSM_IP_ADDRESS}") */
	private static  String hsmIPAddress = "10.4.1.60";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			

			Socket socket = getSocketConnection();
			String header=String.valueOf(Math.random()).substring(2, 2 + 4);
			sendRequest(socket, header+"CW918B41A598AD226197846485D7E196721010101002507;0823000");
			getResponse(socket);

			socket.close();
		} catch (Exception e) {
			logger.error("Exception occured while getting Socket Connection : {}", e);
		}

	}

	private static void sendRequest(Socket psSocket, String psRequest) {
		try {
		BufferedOutputStream poBWMsg = new BufferedOutputStream(psSocket.getOutputStream());
		int piLen = psRequest.length();
		logger.debug("HSM REQMSG:---->" + psRequest);
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

	private static void getResponse(Socket psSocket) throws IOException {
		String psData = "";
		byte[] b = new byte[100];
		InputStream poInpStr = psSocket.getInputStream();
		BufferedInputStream pBISMessage = new BufferedInputStream(poInpStr, 1024);
		int count = 0;
		/*try{
		while ((count = pBISMessage.read(b)) < 0) {
		psData = new String(b);
		}
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		pBISMessage.read(b);
		psData = new String(b);
		
		logger.info("Response from HSM:------->" + psData);
	}

	private static Socket getSocketConnection() {
		Socket ptSocket=null;
		try {
			int portNo=1500;
			InetAddress piad = InetAddress.getByName(hsmIPAddress);
			ptSocket = new Socket(piad,portNo);
		} catch (Exception e) {
			logger.error(e);
		}
		return ptSocket;
	}

}
