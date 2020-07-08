package com.incomm.cclp.dto;

public class ResponseDTO {

	private Object data;

	private String result;

	private String message;

	private String responseCode;

	private String code;

	public Object getData() {
		return data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public String toString() {
		return "ResponseDTO [data=" + data + ", result=" + result + ", message=" + message + ", responseCode=" + responseCode + ", code="
				+ code + "]";
	}

}
