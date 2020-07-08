package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class GroupAccessDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long groupAccessId;		
	
	private String productName;

	private String groupAccessName;
	
	private Long productId;	
	
	private Long partnerId;
	
	private String partnerName;

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;
	
	private List<PartnerDTO> groupAccessPartnerList; 
	
	private List<String> partnerList;
	
	private String partnerPartyType;
	
	private GroupAccessPartnerDTO [] partnerArray; 


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

	public List<PartnerDTO> getGroupAccessPartnerList() {
		return groupAccessPartnerList;
	}

	public void setGroupAccessPartnerList(List<PartnerDTO> groupAccessPartnerList) {
		this.groupAccessPartnerList = groupAccessPartnerList;
	}

	public List<String> getPartnerList() {
		return partnerList;
	}

	public void setPartnerList(List<String> partnerList) {
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

	@Override
	public String toString() {
		return "GroupAccessDTO [groupAccessId=" + groupAccessId
				+ ", productName=" + productName + ", groupAccessName="
				+ groupAccessName + ", productId=" + productId + ", partnerId="
				+ partnerId + ", partnerName=" + partnerName + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser="
				+ lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ ", groupAccessPartnerList=" + groupAccessPartnerList
				+ ", partnerList=" + partnerList + ", partnerPartyType="
				+ partnerPartyType + ", partnerArray="
				+ Arrays.toString(partnerArray) + "]";
	}


}