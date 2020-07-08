package com.incomm.cclpvms.fraud.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BlockList implements Serializable {
	private static final long serialVersionUID = 1L;
	private String channelCode;
	private String instrumentType;
	private String instrumentId;
	private long insUser;
	
	public BlockList() {
		
	}
	public BlockList(String channelCode) {
		
		this.channelCode = channelCode;
	}
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
	@Override
	public String toString() {
		return "BlockList [channelCode=" + channelCode + ", instrumentType=" + instrumentType + ", instrumentId="
				+ instrumentId + ", insUser=" + insUser + "]";
	}
	
}
