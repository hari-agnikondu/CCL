package com.incomm.cclpvms.config.model;

import java.util.Map;

public class DeliveryChannel implements Comparable<Object> {

	private String deliveryChnlId;
	private String deliveryChnlShortName;
	private String deliveryChnlName;
	private Map<String, String> transactionMap;
	
	
	public DeliveryChannel() {

	}

	public DeliveryChannel(String deliveryChnlId, String deliveryChnlShortName, String deliveryChnlName,
			Map<String, String> transactionMap) {
		
		this.deliveryChnlId = deliveryChnlId;
		this.deliveryChnlShortName = deliveryChnlShortName;
		this.deliveryChnlName = deliveryChnlName;
		this.transactionMap = transactionMap;
	}

	public String getDeliveryChnlId() {
		return deliveryChnlId;
	}

	public void setDeliveryChnlId(String deliveryChnlId) {
		this.deliveryChnlId = deliveryChnlId;
	}

	public String getDeliveryChnlName() {
		return deliveryChnlName;
	}

	public void setDeliveryChnlName(String deliveryChnlName) {
		this.deliveryChnlName = deliveryChnlName;
	}

	public Map<String, String> getTransactionMap() {
		return transactionMap;
	}

	public void setTransactionMap(Map<String, String> transactionMap) {
		this.transactionMap = transactionMap;
	}

	public String getDeliveryChnlShortName() {
		return deliveryChnlShortName;
	}

	public void setDeliveryChnlShortName(String deliveryChnlShortName) {
		this.deliveryChnlShortName = deliveryChnlShortName;
	}

	@Override
	public String toString() {
		return "DeliveryChannel [deliveryChnlId=" + deliveryChnlId + ", deliveryChnlShortName=" + deliveryChnlShortName
				+ ", deliveryChnlName=" + deliveryChnlName + ", transactionMap=" + transactionMap + "]";
	}

	@Override
	public int compareTo(Object o) {
		DeliveryChannel deliveryChnl = (DeliveryChannel) o;
		return this.deliveryChnlName.compareTo(deliveryChnl.deliveryChnlName);
	}
	@Override
	  public boolean equals(Object obj) {
		return false; /* ... */ 
		}
	
	@Override
	public int hashCode() {
		return 0;
	}

}
