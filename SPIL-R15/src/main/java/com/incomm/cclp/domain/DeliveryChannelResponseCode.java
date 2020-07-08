/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "DELIVERY_CHANNEL_RESPONSE_CODE")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "DeliveryChannelResponseCode.findAll", query = "SELECT d FROM DeliveryChannelResponseCode d"),
		@NamedQuery(name = "DeliveryChannelResponseCode.findByChannelCode", query = "SELECT d FROM DeliveryChannelResponseCode d WHERE d.deliveryChannelResponseCodePK.channelCode = :channelCode"),
		@NamedQuery(name = "DeliveryChannelResponseCode.findByResponseCode", query = "SELECT d FROM DeliveryChannelResponseCode d WHERE d.deliveryChannelResponseCodePK.responseCode = :responseCode"),
		@NamedQuery(name = "DeliveryChannelResponseCode.findByChannelResponseCode", query = "SELECT d FROM DeliveryChannelResponseCode d WHERE d.channelResponseCode = :channelResponseCode"),
		@NamedQuery(name = "DeliveryChannelResponseCode.findByDeliveryResponseDescription", query = "SELECT d FROM DeliveryChannelResponseCode d WHERE d.deliveryResponseDescription = :deliveryResponseDescription") })
public class DeliveryChannelResponseCode implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected DeliveryChannelResponseCodePK deliveryChannelResponseCodePK;
	@Size(max = 50)
	@Column(name = "CHANNEL_RESPONSE_CODE")
	private String channelResponseCode;
	@Size(max = 500)
	@Column(name = "DELIVERY_RESPONSE_DESCRIPTION")
	private String deliveryResponseDescription;
	@JoinColumn(name = "RESPONSE_CODE", referencedColumnName = "RESPONSE_CODE", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private ResponseCode responseCode1;

	public DeliveryChannelResponseCode() {
	}

	public DeliveryChannelResponseCode(DeliveryChannelResponseCodePK deliveryChannelResponseCodePK) {
		this.deliveryChannelResponseCodePK = deliveryChannelResponseCodePK;
	}

	public DeliveryChannelResponseCode(String channelCode, String responseCode) {
		this.deliveryChannelResponseCodePK = new DeliveryChannelResponseCodePK(channelCode, responseCode);
	}

	public DeliveryChannelResponseCodePK getDeliveryChannelResponseCodePK() {
		return deliveryChannelResponseCodePK;
	}

	public void setDeliveryChannelResponseCodePK(DeliveryChannelResponseCodePK deliveryChannelResponseCodePK) {
		this.deliveryChannelResponseCodePK = deliveryChannelResponseCodePK;
	}

	public String getChannelResponseCode() {
		return channelResponseCode;
	}

	public void setChannelResponseCode(String channelResponseCode) {
		this.channelResponseCode = channelResponseCode;
	}

	public String getDeliveryResponseDescription() {
		return deliveryResponseDescription;
	}

	public void setDeliveryResponseDescription(String deliveryResponseDescription) {
		this.deliveryResponseDescription = deliveryResponseDescription;
	}

	public ResponseCode getResponseCode1() {
		return responseCode1;
	}

	public void setResponseCode1(ResponseCode responseCode1) {
		this.responseCode1 = responseCode1;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (deliveryChannelResponseCodePK != null ? deliveryChannelResponseCodePK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DeliveryChannelResponseCode)) {
			return false;
		}
		DeliveryChannelResponseCode other = (DeliveryChannelResponseCode) object;
		if ((this.deliveryChannelResponseCodePK == null && other.deliveryChannelResponseCodePK != null)
				|| (this.deliveryChannelResponseCodePK != null
						&& !this.deliveryChannelResponseCodePK.equals(other.deliveryChannelResponseCodePK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.DeliveryChannelResponseCode[ deliveryChannelResponseCodePK=" + deliveryChannelResponseCodePK + " ]";
	}

}
