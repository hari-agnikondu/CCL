package com.incomm.cclp.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.PartnerPurse;
import com.incomm.cclp.domain.Purse;



public class PartnerDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private long partnerId;
	
	private String partnerName;

	private String partnerDesc;
	
	private String mdmId;
	
	private String isActive;
	
	private long insUser;

	private Date insDate;
	
	private long lastUpdUser;

	private Date lastUpdDate;

	
	private List<CurrencyCode> partnerCurrencyList; 
	private List<String> currencyList; 
	
	private List<Purse> partnerPurseList; 
	private List<String> purseList; 
	
	
	public PartnerDTO() {
		super();
	}

	public PartnerDTO(long partnerId, String partnerName, String partnerDesc, String mdmId, String isActive,
			long insUser, long lastUpdUser, Date insDate, Date lastUpdDate, List<CurrencyCode> listOfCurrencyCodes,
			List<Purse> listOfpurse) {
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.partnerDesc = partnerDesc;
		this.mdmId = mdmId;
		this.isActive = isActive;
		this.insUser = insUser;
		this.lastUpdUser = lastUpdUser;
		this.insDate = insDate;
		this.lastUpdDate = lastUpdDate;
		this.partnerCurrencyList= listOfCurrencyCodes;
		this.partnerPurseList= listOfpurse;
		
	}

	public List<CurrencyCode> getPartnerCurrencyList() {
		return partnerCurrencyList;
	}

	public void setPartnerCurrencyList(List<CurrencyCode> partnerCurrencyList) {
		this.partnerCurrencyList = partnerCurrencyList;
	}

	public List<String> getCurrencyList() {
		return currencyList;
	}

	public void setCurrencyList(List<String> currencyList) {
		this.currencyList = currencyList;
	}

	public long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(long partnerId) {
		this.partnerId = partnerId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	
	public String getPartnerDesc() {
		return partnerDesc;
	}

	public void setPartnerDesc(String partnerDesc) {
		this.partnerDesc = partnerDesc;
	}

	public String getMdmId() {
		return mdmId;
	}

	public void setMdmId(String mdmId) {
		this.mdmId = mdmId;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
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
	
	public List<Purse> getPartnerPurseList() {
		return partnerPurseList;
	}

	public void setPartnerPurseList(List<Purse> partnerPurseList) {
		this.partnerPurseList = partnerPurseList;
	}

	public List<String> getPurseList() {
		return purseList;
	}

	public void setPurseList(List<String> purseList) {
		this.purseList = purseList;
	}

	@Override
	public String toString() {
		return "PartnerDTO [partnerId=" + partnerId + ", partnerName=" + partnerName + ", partnerDesc=" + partnerDesc
				+ ", mdmId=" + mdmId + ", isActive=" + isActive + ", insUser=" + insUser + ", insDate=" + insDate
				+ ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + ", partnerCurrencyList="
				+ partnerCurrencyList + ", currencyList=" + currencyList + ", partnerPurseList=" + partnerPurseList
				+ ", purseList=" + purseList + "]";
	}

}
