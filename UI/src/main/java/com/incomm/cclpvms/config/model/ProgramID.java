package com.incomm.cclpvms.config.model;

import java.util.Date;
import java.util.Map;

import com.incomm.cclpvms.config.validator.LimitMap;
import com.incomm.cclpvms.config.validator.MaintenanceFee;
import com.incomm.cclpvms.config.validator.MonthlyFeeCapFee;
import com.incomm.cclpvms.config.validator.TransactionFee;

public class ProgramID {

	private Long programID;
	
	
	
	private String programIDName;

	
	private Long partnerId;
	
	
	private String partnerName;
	
	private String description;
	
	private String limitsFees;
	
	private String deliveryChannelList;
	
	private String transaction;
	
	private String purseList;
	
	
	@LimitMap(groups=Limitmap.class)
	@MaintenanceFee(groups=maintenanceFee.class)
	@MonthlyFeeCapFee(groups=monthlyFeeCapFee.class)
	@TransactionFee(groups=transactionFee.class)
	private Map<String, Object> productAttributes ;

	
	
	private long insUser;
	
	private Date insDate;
	
	private long lastUpdUser;

	private Date lastUpdDate;
	
	

	public ProgramID() {
		
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
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

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}


	public Long getProgramID() {
		return programID;
	}


	public void setProgramID(Long programID) {
		this.programID = programID;
	}


	public String getProgramIDName() {
		return programIDName;
	}


	public void setProgramIDName(String programIDName) {
		this.programIDName = programIDName;
	}
	
	


	public String getLimitsFees() {
		return limitsFees;
	}


	public void setLimitsFees(String limitsFees) {
		this.limitsFees = limitsFees;
	}
	
	


	public String getDeliveryChannelList() {
		return deliveryChannelList;
	}


	public void setDeliveryChannelList(String deliveryChannelList) {
		this.deliveryChannelList = deliveryChannelList;
	}
	
	
	

	public String getTransaction() {
		return transaction;
	}


	public void setTransaction(String transaction) {
		this.transaction = transaction;
	}
	
	


	public Map<String, Object> getProductAttributes() {
		return productAttributes;
	}


	public void setProductAttributes(Map<String, Object> productAttributes) {
		this.productAttributes = productAttributes;
	}


	public ProgramID(Long programID, String programIDName, Long partnerId, String partnerName, String description) {
		super();
		this.programID = programID;
		this.programIDName = programIDName;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.description = description;
			}

	public String getPurseList() {
		return purseList;
	}


	public void setPurseList(String purseList) {
		this.purseList = purseList;
	}


	@Override
	public String toString() {
		return "ProgramID [programID=" + programID + ", programIDName=" + programIDName + ", partnerId=" + partnerId
				+ ", partnerName=" + partnerName + ", description=" + description + ", limitsFees=" + limitsFees
				+ ", deliveryChannelList=" + deliveryChannelList + ", transaction=" + transaction + ", purseList="
				+ purseList + ", productAttributes=" + productAttributes + ", insUser=" + insUser + ", insDate="
				+ insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}
	
	
	
	
	

}
