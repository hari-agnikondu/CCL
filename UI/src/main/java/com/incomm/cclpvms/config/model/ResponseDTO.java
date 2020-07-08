/**
 * 
 */
package com.incomm.cclpvms.config.model;


/**
 * ResponseDTO class represents the data returned by all REST operations.
 * 
 * @author abutani
 *
 */
public class ResponseDTO {
	
	// the data
	private Object data;
	
	// the result - Success/Failure.
	private String result;
	
	// the result message
	private String message;
	
	// the responseCode
	private String responseCode;
	
	private String code;
	
	

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "ResponseDTO [data=" + data + ", result=" + result
				+ ", message=" + message + "]";
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
}
