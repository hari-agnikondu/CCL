package com.incomm.cclp.util;

import java.net.Socket;

import org.springframework.stereotype.Component;

import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.fsapi.constants.B2BResponseCode;
import com.incomm.cclp.fsapi.constants.FSAPIConstants;

@Component
public class HSMCommandBuilder {
	
	
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
	        if(socket != null) {
	        socket.close();
	        }
		   }catch(Exception e) {
			   
			   throw new ServiceException(FSAPIConstants.ERROR_CVV_GENERATION,B2BResponseCode.TRANSACTION_DECLINED_DUE_TO_SYSTEM_ERROR);
		   }
		   
		  
		   return cvv;
		   
	   }

	private String parseHSMResponse(String hsmResponse, String header) {
		String hsmHeader = hsmResponse.substring(2, header.length()+2);
		
		 String hsmResponseCommand = hsmResponse.substring(header.length()+2, header.length()+4);
		 if(!hsmResponseCommand.equalsIgnoreCase("cx")) {
			 return null;
		 }
		 String hsmResponseCode = hsmResponse.substring(hsmResponseCommand.length()+hsmHeader.length()+2, hsmResponseCommand.length()+hsmHeader.length()+4);
		 if(!hsmResponseCode.equalsIgnoreCase("00")){
			 return null;
		 }
		 return hsmResponse.substring(hsmResponseCommand.length()+hsmHeader.length()+hsmResponseCode.length()+2);
		
	}

}
