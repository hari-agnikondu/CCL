package com.incomm.scheduler.utils;

import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.exception.ServiceException;

@Component
public class HSMCommandBuilder {
	
	private static final Logger logger = LogManager.getLogger(HSMCommandBuilder.class);
	HsmUtil hsmUtil = new HsmUtil();
	
	public String genCVV(String pan,String expryDate,String srvCode,String cvk,String hsmipaddress,int hsmport) throws ServiceException{
		  
		   String requestData=null;
		   String hsmResponse = null;
		   Socket socket = null;
		   String cvv = null;
		   try{
	       String[][] maCommandList; //** Command List 
	       
	       maCommandList =new String[1][2];
	       maCommandList[0][0] = "CW";
	       
	      
	        requestData= maCommandList[0][0]+cvk+pan+";"+expryDate+srvCode;
	        
	         socket = hsmUtil.getSocketConnection(hsmipaddress,hsmport);
	        
	         if(socket == null) {
	        	 return null;
	         }
	        String header=String.valueOf(Math.random()).substring(2, 2 + 4);
	        hsmUtil.sendRequest(socket, header+requestData);
	        hsmResponse = hsmUtil.getResponse(socket);
	         cvv = parseHSMResponse(hsmResponse,header);
	         logger.debug("cvv"+cvv);
	        
	        socket.close();
	        
		   }catch(Exception e) {
			   
			   throw new ServiceException(ResponseMessages.CCF_ERROR_CVV_GENERATION);
		   }
		   
		  
		   return cvv;
		   
	   }

	private String parseHSMResponse(String hsmResponse, String header) {
		String hsmHeader = hsmResponse.substring(2, header.length()+2);
		
		 String hsmResponseCommand = hsmResponse.substring(header.length()+2, header.length()+4);
		 if(!hsmResponseCommand.equalsIgnoreCase("cx")) {
			 logger.debug(" HSM response :"+hsmResponseCommand);
			 return null;
		 }
		 String hsmResponseCode = hsmResponse.substring(hsmResponseCommand.length()+hsmHeader.length()+2, hsmResponseCommand.length()+hsmHeader.length()+4);
		 if(!hsmResponseCode.equalsIgnoreCase("00")){
			 logger.debug(" HSM response :"+hsmResponseCode);
			 return null;
		 }
		 String cvvparse = hsmResponse.substring(hsmResponseCommand.length()+hsmHeader.length()+hsmResponseCode.length()+2);
		 return cvvparse.substring(0, 3);
	}

}
