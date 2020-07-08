package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Map;

public class DeliveryChannelDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	private String deliveryChnlId;
	private String deliveryChnlShortName;
	private String deliveryChnlName;
	
	
	
	public DeliveryChannelDTO() {

	}

	public DeliveryChannelDTO(String deliveryChnlId, String deliveryChnlShortName, String deliveryChnlName,
			Map<String, String> transactionMap) {
		
		this.deliveryChnlId = deliveryChnlId;
		this.deliveryChnlShortName = deliveryChnlShortName;
		this.deliveryChnlName = deliveryChnlName;
		
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

	

	

	public String getDeliveryChnlShortName() {
		return deliveryChnlShortName;
	}

	public void setDeliveryChnlShortName(String deliveryChnlShortName) {
		this.deliveryChnlShortName = deliveryChnlShortName;
	}

	@Override
	public String toString() {
		return "DeliveryChannel [deliveryChnlId=" + deliveryChnlId + ", deliveryChnlShortName=" + deliveryChnlShortName
				+ ", deliveryChnlName=" + deliveryChnlName + "]";
	}
}
