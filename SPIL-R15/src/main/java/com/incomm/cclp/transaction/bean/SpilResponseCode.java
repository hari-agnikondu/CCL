package com.incomm.cclp.transaction.bean;

public class SpilResponseCode {

	private String responseCode;
	private String responseDescription;
	private String channelResponseCode;

	public SpilResponseCode() {

	}

	public SpilResponseCode(String channelResponseCode, String responseDescription) {
		this.channelResponseCode = channelResponseCode;
		this.responseDescription = responseDescription;
	}

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

	@Override
	public String toString() {
		return "SpilResponseCode [responseCode=" + responseCode + ", responseDescription=" + responseDescription + ", channelResponseCode="
				+ channelResponseCode + "]";
	}

}
