package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "PROGRAM",uniqueConstraints = { @UniqueConstraint(columnNames = "PROGRAM_ID")})
public class ProgramID implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id 
	@SequenceGenerator(name="PROGRAM_SEQ_GEN",sequenceName="PROGRAM_ID_SEQ",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="PROGRAM_SEQ_GEN")
	@Column(name="PROGRAM_ID")
	private Long prgmId;
	
	@Column(name="PROGRAM_NAME")
	private String programIDName;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="PARTNER_ID",referencedColumnName="PARTNER_ID")
	private Partner partner;
	
	@Column(name="DESCRIPTION")
	private String description;
	
	@Column(name="INS_USER",updatable = false)
	private long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;
	
	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	public Long getPrgmID() {
		return prgmId;
	}

	public void setPrgmID(Long prgmID) {
		this.prgmId = prgmID;
	}

	public String getProgramIDName() {
		return programIDName;
	}

	public void setProgramIDName(String programIDName) {
		this.programIDName = programIDName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	
	@Override
	public String toString() {
		return "ProgramIDDTO [programID=" + prgmId + ", description=" + description + ", programIDName="
				+ programIDName + ", partnerId=" + partner.getPartnerId()  + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ "]";
	}

}
