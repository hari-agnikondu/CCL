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

@Entity
@Audited
@Table(name = "PARTNER_CURRENCY")
@AssociationOverride(name = "partnerCurrencyID.partner", joinColumns = @JoinColumn(name = "PARTNER_ID"))
@AssociationOverride(name = "partnerCurrencyID.currencyCode", joinColumns = @JoinColumn(name = "CURRENCY_ID"))
public class PartnerCurrency implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PartnerCurrencyID partnerCurrencyID;

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

	public PartnerCurrencyID getPartnerCurrencyID() {
		return partnerCurrencyID;
	}

	public void setPartnerCurrencyID(PartnerCurrencyID partnerCurrencyID) {
		this.partnerCurrencyID = partnerCurrencyID;
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
		return partnerCurrencyID.getPartner();
	}

	public void setPartner(Partner partner) {
		partnerCurrencyID.setPartner(partner);
	}

	@Transient
	public CurrencyCode getCurrencyCode() {
		return partnerCurrencyID.getCurrencyCode();
	}

	public void setCurrencyCode(CurrencyCode currencyCode) {
		partnerCurrencyID.setCurrencyCode(currencyCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		PartnerCurrency that = (PartnerCurrency) obj;

		return Objects.equals(getPartnerCurrencyID(), that.getPartnerCurrencyID());
	}

	@Override
	public int hashCode() {
		return (getPartnerCurrencyID() != null ? getPartnerCurrencyID().hashCode() : 0);
	}

	@Override
	public String toString() {
		return "PartnerCurrency [partnerCurrencyID=" + partnerCurrencyID + ", insUser=" + insUser + ", insDate="
				+ insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate + "]";
	}

}
