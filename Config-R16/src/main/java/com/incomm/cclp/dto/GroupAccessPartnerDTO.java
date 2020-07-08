package com.incomm.cclp.dto;

import java.io.Serializable;

public class GroupAccessPartnerDTO  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long groupAccessId;		
	
	private String groupAccessName;
	
	private Long partnerId;
	
	private String partnerName;

	private String partnerPartyType;
	
	public Long getGroupAccessId() {
		return groupAccessId;
	}

	public void setGroupAccessId(Long groupAccessId) {
		this.groupAccessId = groupAccessId;
	}


	public String getGroupAccessName() {
		return groupAccessName;
	}

	public void setGroupAccessName(String groupAccessName) {
		this.groupAccessName = groupAccessName;
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

	public String getPartnerPartyType() {
		return partnerPartyType;
	}

	public void setPartnerPartyType(String partnerPartyType) {
		this.partnerPartyType = partnerPartyType;
	}

	@Override
	public String toString() {
		return "GroupAccessPartnerDTO [groupAccessId=" + groupAccessId
				+ ", groupAccessName=" + groupAccessName + ", partnerId="
				+ partnerId + ", partnerName=" + partnerName
				+ ", partnerPartyType=" + partnerPartyType + "]";
	}


}
