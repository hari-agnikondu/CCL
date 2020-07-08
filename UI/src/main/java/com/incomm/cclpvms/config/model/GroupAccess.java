package com.incomm.cclpvms.config.model;



import java.util.Date;
import java.util.List;


import com.incomm.cclpvms.config.validator.FieldValidation;


public class GroupAccess {

	
	private Long groupAccessId;		
	
	@FieldValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ]+$", max = 100, messageNotEmpty = "{messageNotEmpty.groupAccess.productName}", messageLength = "{messageLength.groupAccess.productName}", messagePattern = "{messagepattern.groupAccess.productName}", groups = ValidationStepOne.class)
	private String productName;
	
	@FieldValidation(notEmpty = false, pattern = "^[A-Za-z0-9 ]+$", max = 50, messageNotEmpty = "{messageNotEmpty.groupAccess.groupAccessName}", messageLength = "{messageLength.groupAccess.groupAccessName}", messagePattern = "{messagepattern.groupAccess.groupAccessName}", groups = ValidationStepOne.class)
	private String groupAccessName;
	
	private Long productId;	
	
	private Long partnerId;
	
	private String partnerName;

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;
	
	private List<Partner> groupAccessPartnerList; 
	
	private List<Partner> partnerList;
	
	
	
	
	private List<String> selectedPartnerList;
	
	
	private String partnerPartyType;
	
	private GroupAccessPartnerDTO [] partnerArray;

	

	public List<String> getSelectedPartnerList() {
		return selectedPartnerList;
	}

	public void setSelectedPartnerList(List<String> selectedPartnerList) {
		this.selectedPartnerList = selectedPartnerList;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}


	public String getGroupAccessName() {
		return groupAccessName;
	}

	public void setGroupAccessName(String groupAccessName) {
		this.groupAccessName = groupAccessName;
	}

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = insDate;
	}

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public Long getGroupAccessId() {
		return groupAccessId;
	}

	public void setGroupAccessId(Long groupAccessId) {
		this.groupAccessId = groupAccessId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public List<Partner> getGroupAccessPartnerList() {
		return groupAccessPartnerList;
	}

	public void setGroupAccessPartnerList(List<Partner> groupAccessPartnerList) {
		this.groupAccessPartnerList = groupAccessPartnerList;
	}

	
	public interface ValidationStepOne {

		// validation group marker interface

	}
	@Override
	public String toString() {
		return "GroupAccessDTO [groupAccessId=" + groupAccessId
				+ ", productName=" + productName + ", groupAccessName="
				+ groupAccessName + ", productId=" + productId + ", partnerId="
				+ partnerId + ", partnerName=" + partnerName + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser="
				+ lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}

	public List<Partner> getPartnerList() {
		return partnerList;
	}

	public void setPartnerList(List<Partner> partnerList) {
		this.partnerList = partnerList;
	}

	public String getPartnerPartyType() {
		return partnerPartyType;
	}

	public void setPartnerPartyType(String partnerPartyType) {
		this.partnerPartyType = partnerPartyType;
	}

	public GroupAccessPartnerDTO [] getPartnerArray() {
		return partnerArray;
	}

	public void setPartnerArray(GroupAccessPartnerDTO [] partnerArray) {
		this.partnerArray = partnerArray;
	}

	
	

}