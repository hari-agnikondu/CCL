package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.AssociationOverride;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Audited(targetAuditMode=RelationTargetAuditMode.NOT_AUDITED)
@Table(name = "PARTNER_PURSE")
@AssociationOverride(name = "partnerPurseID.partner", joinColumns = @JoinColumn(name = "PARTNER_ID"))
@AssociationOverride(name = "partnerPurseID.purse", joinColumns = @JoinColumn(name = "PURSE_ID"))
public class PartnerPurse implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PartnerPurseID partnerPurseID;

	@Column(name = "INS_USER")
	private Long insUser;

	@CreationTimestamp
	@Column(name = "INS_DATE", updatable = false)
	private Date insDate;

	@Column(name = "LAST_UPD_USER")
	private Long lastUpdUser;

	@UpdateTimestamp
	@Column(name = "LAST_UPD_DATE")
	private Date lastUpdDate;

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
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
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
		this.lastUpdDate = new java.sql.Date(new java.util.Date().getTime());
	}

	@Transient
	public Partner getPartner() {
		return partnerPurseID.getPartner();
	}

	public void setPartner(Partner partner) {
		partnerPurseID.setPartner(partner);
	}

	@Transient
	public Purse getPurse() {
		return partnerPurseID.getPurse();
	}

	public void setPurse(Purse purse) {
		partnerPurseID.setPurse(purse);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		PartnerPurse that = (PartnerPurse) obj;

		return Objects.equals(getPartnerPurseID(), that.getPartnerPurseID());
	}

	@Override
	public int hashCode() {
		return (getPartnerPurseID() != null ? getPartnerPurseID().hashCode() : 0);
	}

	public PartnerPurseID getPartnerPurseID() {
		return partnerPurseID;
	}

	public void setPartnerPurseID(PartnerPurseID partnerPurseID) {
		this.partnerPurseID = partnerPurseID;
	}

	@Override
	public String toString() {
		return "PartnerPurse [partnerPurseID=" + partnerPurseID + ", insUser=" + insUser + ", insDate=" + insDate
				+ ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}

}
