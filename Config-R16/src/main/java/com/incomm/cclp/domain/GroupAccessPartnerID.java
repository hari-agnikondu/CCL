package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 * The persistent class for the GroupAccessPartnerID database table.
 * 
 */
@Embeddable
public class GroupAccessPartnerID implements Serializable {

	private static final long serialVersionUID = 1L;
	@ManyToOne
	private GroupAccess groupAccess;		
	@ManyToOne
	private Partner partner;
	
	public GroupAccessPartnerID(){
		super();
	}

	public GroupAccess getGroupAccess() {
		return groupAccess;
	}

	public void setGroupAccess(GroupAccess groupAccess) {
		this.groupAccess = groupAccess;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	@Override
	public String toString() {
		return "GroupAccessPartnerID [groupAccess=" + groupAccess
				+ ", partner=" + partner + "]";
	}

}