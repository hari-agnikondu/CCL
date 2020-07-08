/**
 * 
 */
package com.incomm.cclp.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.ResponseDTO;

/**
 * ResponseBuilder class builds the response returned by the Configuration Service.
 * 
 * @author abutani
 *
 */
@Component
public class ResponseBuilder {
	
	// the locale handler
	@Autowired
	private LocaleHandler localeHandler;

	
	/**
	 * Returns a ResponseDTO for a successful response.
	 * 
	 * @param data The data to be sent in the response.
	 * @param message The message to be sent in the response.

	 * @return the ResponseDTO for the success response.
	 */
	public ResponseDTO buildSuccessResponse(Object data, String message,String code)
	{
		ResponseDTO responseDto = new ResponseDTO();
		responseDto.setData(data);
		responseDto.setMessage(localeHandler.getLocalizedMessage(message));		
		responseDto.setCode(ResponseMessages.SUCCESS);		
		responseDto.setResult(localeHandler.getLocalizedMessage(
				ResponseMessages.SUCCESS));		
		
		return responseDto;
	}
	
	
	/**
	 * Returns a ResponseDTO for a failure response.
	 * 
	 * @param message The message to be sent in the response.

	 * @return the ResponseDTO for the failure response.
	 */
	public ResponseDTO buildFailureResponse(String message,String code)
	{
		ResponseDTO responseDto = new ResponseDTO();
	
		responseDto.setResult(code);
		responseDto.setMessage(localeHandler.getLocalizedMessage(message));
		responseDto.setCode(code);
		
		return responseDto;
	}
	
	public ResponseDTO buildFailureResponse(Object data, String message,String code)
	{
		ResponseDTO responseDto = new ResponseDTO();
		responseDto.setData(data);
		responseDto.setMessage(localeHandler.getLocalizedMessage(message));		
		responseDto.setCode(code);		
		responseDto.setResult(localeHandler.getLocalizedMessage(
				code));		
		
		return responseDto;
	}
	
	/**
	 * Fills in the placeholder in the message with the actual value.
	 * 
	 * @param objName The name of the placeholder.
	 * @param objValue The value for the placeholder.
	 * @param responseMsg - The message to be updated. 

	 * @return the updated response message.
	 */
	public static String fillPlaceHolder(String objName,String objValue, String msg)
	{
		
		if (Util.isEmpty(objName) || objValue == null)
			return msg;
		
		String tempMsg = msg;
		Map<String, String> valuesMap = new HashMap<>();
		valuesMap.put(objName, objValue);
	
		StrSubstitutor sub = new StrSubstitutor(valuesMap);
		return sub.replace(tempMsg);

	}
	
}
