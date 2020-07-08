package com.incomm.cclpvms.config.model;

import java.io.Serializable;
import java.util.Date;

public class ProgramIDDTO implements Serializable{


private static final long serialVersionUID = 1L;
	
	private Long programID;
	
	private String programIDName;
	
	private Long partnerId;
	
	private String partnerName;
	
	private String description;
	
	private String limitsFees;
	
	private Long insUser;
	
	private Date insDate;
	
	private Long lastUpdUser;
	
	private Date lastUpdDate;
	
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

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	
	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	

	public String getLimitsFees() {
		return limitsFees;
	}

	public void setLimitsFees(String limitsFees) {
		this.limitsFees = limitsFees;
	}

	public ProgramIDDTO() {
		super();
	}
	
	public ProgramIDDTO(Long programID,String programIDName,  Long partnerId, String partnerName,String description, 
			Long insUser, Date insDate, Long lastUpdUser, Date lastUpdDate) {
		
		super();
		this.programID = programID;
		this.description = description;
		this.programIDName = programIDName;
		this.partnerId = partnerId;
		this.partnerName = partnerName;
		this.insUser = insUser;
		this.insDate= insDate;
		this.lastUpdUser = lastUpdUser;
		this.lastUpdDate = lastUpdDate;
		
	}

	@Override
	public String toString() {
		return "ProgramIDDTO [programID=" + programID + ", description=" + description + ", programIDName="
				+ programIDName + ", partnerId=" + partnerId  + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ "]";
	}

	

}



