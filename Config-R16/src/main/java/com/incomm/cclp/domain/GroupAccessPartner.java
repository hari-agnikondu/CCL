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




/**
 * The persistent class for the GROUP_ACCESS_PARTNER database table.
 * 
 */
@Entity
@Audited
@Table(name = "GROUP_ACCESS_PARTNER")
@AssociationOverride(name = "groupAccessPartnerID.groupAccess", joinColumns = @JoinColumn(name = "GROUP_ACCESS_ID"))
@AssociationOverride(name = "groupAccessPartnerID.partner", joinColumns = @JoinColumn(name = "PARTNER_ID"))
public class GroupAccessPartner implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	private GroupAccessPartnerID groupAccessPartnerID;

	@Column(name="INS_USER")
	private Long insUser;

	@CreationTimestamp
	@Column(name="INS_DATE",updatable=false)
	private Date insDate;
 
	@Column(name="LAST_UPD_USER")
	private Long lastUpdUser;

	@UpdateTimestamp
	@Column(name="LAST_UPD_DATE")
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
		this.lastUpdDate = lastUpdDate;
	}

	public GroupAccessPartnerID getGroupAccessPartnerID() {
		return groupAccessPartnerID;
	}

	public void setGroupAccessPartnerID(GroupAccessPartnerID groupAccessPartnerID) {
		this.groupAccessPartnerID = groupAccessPartnerID;
	}
	

	@Transient
	public Partner getPartner() {
		return groupAccessPartnerID.getPartner();
	}

	public void setPartner(Partner partner) {
		groupAccessPartnerID.setPartner(partner);
	}

	@Transient
	public GroupAccess getGroupAccess() {
		return groupAccessPartnerID.getGroupAccess();
	}

	public void setGroupAccess(GroupAccess groupAccess) {
		groupAccessPartnerID.setGroupAccess(groupAccess);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		GroupAccessPartner that = (GroupAccessPartner) obj;
		
		return Objects.equals(getGroupAccessPartnerID(), that.getGroupAccessPartnerID());
	}

	@Override
	public int hashCode() {
		return (getGroupAccessPartnerID() != null ? getGroupAccessPartnerID().hashCode() : 0);
	}

	@Override
	public String toString() {
		return "GroupAccessPartner [groupAccessPartnerID="
				+ groupAccessPartnerID + ", insUser=" + insUser + ", insDate="
				+ insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate="
				+ lastUpdDate + "]";
	}


}