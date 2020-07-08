package com.incomm.cclp.fsapi.bean;

/**
 * 
 *
 */
public class ErrorMsgBean {
	private String key;
	private String errorMsg;
	private String respCode;

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
