package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;


@Embeddable
public class PartnerCurrencyID implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	private Partner partner;		
	@ManyToOne
	private CurrencyCode currencyCode;
	
	public PartnerCurrencyID() {
		super();
	}
	
	public Partner getPartner() {
		return partner;
	}
	
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	public CurrencyCode getCurrencyCode() {
		return currencyCode;
	}
	
	public void setCurrencyCode(CurrencyCode currencyCode) {
		this.currencyCode = currencyCode;
	}
	
	@Override
	public String toString() {
		return "PartnerCurrencyID [partner=" + partner + ", currencyCode=" + currencyCode + "]";
	}
	
}
