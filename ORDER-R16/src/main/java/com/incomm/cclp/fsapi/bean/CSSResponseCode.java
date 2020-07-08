package com.incomm.cclp.fsapi.bean;

public class CSSResponseCode {

	private String responseCode;

	private String responseDescription;

	private String channelResponseCode;
	
	private String channelCode;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getChannelResponseCode() {
		return channelResponseCode;
	}

	public void setChannelResponseCode(String channelResponseCode) {
		this.channelResponseCode = channelResponseCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	@Override
	public String toString() {
		return "CSSResponseCode [responseCode=" + responseCode + ", responseDescription=" + responseDescription
				+ ", channelResponseCode=" + channelResponseCode + ", channelCode=" + channelCode + "]";
	}

}
