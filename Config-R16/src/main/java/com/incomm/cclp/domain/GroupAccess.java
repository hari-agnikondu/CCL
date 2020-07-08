package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.envers.Audited;




/**
 * The persistent class for the GROUP_ACCESS database table.
 * 
 */
@Entity
@Audited
@Table(name = "GROUP_ACCESS")
public class GroupAccess implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name="GROUP_ACCESS_ID_SEQ", sequenceName="GROUP_ACCESS_ID_SEQ", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="GROUP_ACCESS_ID_SEQ")
	@Column(name="GROUP_ACCESS_ID")
	private Long groupAccessId;		
	@Column(name="GROUP_ACCESS_NAME")
	private String groupAccessName;

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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "groupAccessPartnerID.groupAccess", cascade = { CascadeType.PERSIST,
			CascadeType.MERGE }, orphanRemoval = true)
	private List<GroupAccessPartner> groupAccessPartnerList; 

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


	public List<GroupAccessPartner> getGroupAccessPartnerList() {
		return groupAccessPartnerList;
	}

	public void setGroupAccessPartnerList(List<GroupAccessPartner> groupAccessPartnerList) {
		this.groupAccessPartnerList = groupAccessPartnerList;
	}

	
	@Override
	public String toString() {
		return "GroupAccess [groupAccessId=" + groupAccessId
				+ ", groupAccessName=" + groupAccessName + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser="
				+ lastUpdUser + ", lastUpdDate=" + lastUpdDate
				;
	}

}