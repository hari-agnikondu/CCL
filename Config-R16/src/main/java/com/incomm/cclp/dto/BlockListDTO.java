package com.incomm.cclp.dto;

import java.util.Date;

public class BlockListDTO {
	private static final long serialVersionUID = 1L;
	private String channelCode;
	private String instrumentType;
	private String instrumentId;
	private long insUser;
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	public String getInstrumentType() {
		return instrumentType;
	}
	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}
	public String getInstrumentId() {
		return instrumentId;
	}
	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}
	public long getInsUser() {
		return insUser;
	}
	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}
	public Date getInsDate() {
		return insDate;
	}
	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	private Date insDate;
	@Override
	public String toString() {
		return "BlockListDTO [channelCode=" + channelCode + ", instrumentType=" + instrumentType + ", instrumentId="
				+ instrumentId + ", insUser=" + insUser + ", insDate=" + insDate + "]";
	}

}
