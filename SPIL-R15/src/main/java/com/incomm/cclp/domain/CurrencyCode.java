/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "CURRENCY_CODE")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "CurrencyCode.findAll", query = "SELECT c FROM CurrencyCode c"),
		@NamedQuery(name = "CurrencyCode.findByCurrencyId", query = "SELECT c FROM CurrencyCode c WHERE c.currencyId = :currencyId"),
		@NamedQuery(name = "CurrencyCode.findByCurrencyCode", query = "SELECT c FROM CurrencyCode c WHERE c.currencyCode = :currencyCode"),
		@NamedQuery(name = "CurrencyCode.findByCurrencyDescription", query = "SELECT c FROM CurrencyCode c WHERE c.currencyDescription = :currencyDescription"),
		@NamedQuery(name = "CurrencyCode.findByMinorUnits", query = "SELECT c FROM CurrencyCode c WHERE c.minorUnits = :minorUnits") })
public class CurrencyCode implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 20)
	@Column(name = "CURRENCY_ID")
	private String currencyId;
	@Size(max = 20)
	@Column(name = "CURRENCY_CODE")
	private String currencyCode;
	@Size(max = 100)
	@Column(name = "CURRENCY_DESCRIPTION")
	private String currencyDescription;
	@Size(max = 20)
	@Column(name = "MINOR_UNITS")
	private String minorUnits;

	public CurrencyCode() {
	}

	public CurrencyCode(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(String currencyId) {
		this.currencyId = currencyId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getCurrencyDescription() {
		return currencyDescription;
	}

	public void setCurrencyDescription(String currencyDescription) {
		this.currencyDescription = currencyDescription;
	}

	public String getMinorUnits() {
		return minorUnits;
	}

	public void setMinorUnits(String minorUnits) {
		this.minorUnits = minorUnits;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (currencyId != null ? currencyId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof CurrencyCode)) {
			return false;
		}
		CurrencyCode other = (CurrencyCode) object;
		if ((this.currencyId == null && other.currencyId != null)
				|| (this.currencyId != null && !this.currencyId.equals(other.currencyId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.CurrencyCode[ currencyId=" + currencyId + " ]";
	}

}
