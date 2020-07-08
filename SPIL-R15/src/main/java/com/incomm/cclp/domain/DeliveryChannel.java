/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "DELIVERY_CHANNEL")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "DeliveryChannel.findAll", query = "SELECT d FROM DeliveryChannel d"),
		@NamedQuery(name = "DeliveryChannel.findByChannelCode", query = "SELECT d FROM DeliveryChannel d WHERE d.channelCode = :channelCode"),
		@NamedQuery(name = "DeliveryChannel.findByChannelName", query = "SELECT d FROM DeliveryChannel d WHERE d.channelName = :channelName"),
		@NamedQuery(name = "DeliveryChannel.findByChannelShortName", query = "SELECT d FROM DeliveryChannel d WHERE d.channelShortName = :channelShortName"),
		@NamedQuery(name = "DeliveryChannel.findByInstrumentType", query = "SELECT d FROM DeliveryChannel d WHERE d.instrumentType = :instrumentType"),
		@NamedQuery(name = "DeliveryChannel.findByPassiveSupported", query = "SELECT d FROM DeliveryChannel d WHERE d.passiveSupported = :passiveSupported") })
public class DeliveryChannel implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "CHANNEL_CODE")
	private String channelCode;
	@Size(max = 100)
	@Column(name = "CHANNEL_NAME")
	private String channelName;
	@Size(max = 10)
	@Column(name = "CHANNEL_SHORT_NAME")
	private String channelShortName;
	@Size(max = 50)
	@Column(name = "INSTRUMENT_TYPE")
	private String instrumentType;
	@Size(max = 1)
	@Column(name = "PASSIVE_SUPPORTED")
	private String passiveSupported;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "deliveryChannel")
	private Collection<DeliveryChannelTransaction> deliveryChannelTransactionCollection;

	public DeliveryChannel() {
	}

	public DeliveryChannel(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getChannelShortName() {
		return channelShortName;
	}

	public void setChannelShortName(String channelShortName) {
		this.channelShortName = channelShortName;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public String getPassiveSupported() {
		return passiveSupported;
	}

	public void setPassiveSupported(String passiveSupported) {
		this.passiveSupported = passiveSupported;
	}

	@XmlTransient
	public Collection<DeliveryChannelTransaction> getDeliveryChannelTransactionCollection() {
		return deliveryChannelTransactionCollection;
	}

	public void setDeliveryChannelTransactionCollection(Collection<DeliveryChannelTransaction> deliveryChannelTransactionCollection) {
		this.deliveryChannelTransactionCollection = deliveryChannelTransactionCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (channelCode != null ? channelCode.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DeliveryChannel)) {
			return false;
		}
		DeliveryChannel other = (DeliveryChannel) object;
		if ((this.channelCode == null && other.channelCode != null)
				|| (this.channelCode != null && !this.channelCode.equals(other.channelCode))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.DeliveryChannel[ channelCode=" + channelCode + " ]";
	}

}
