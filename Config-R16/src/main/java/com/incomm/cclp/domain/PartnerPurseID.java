package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;


@Embeddable
public class PartnerPurseID implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Partner partner;		
	@ManyToOne
	private Purse purse;
	
	public PartnerPurseID() {
		super();
	}
	
	public Partner getPartner() {
		return partner;
	}
	
	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public Purse getPurse() {
		return purse;
	}

	public void setPurse(Purse purse) {
		this.purse = purse;
	}

	@Override
	public String toString() {
		return "PartnerPurseID [partner=" + partner + ", purse=" + purse + "]";
	}
	
	
	
}
