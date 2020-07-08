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
@Table(name = "RESPONSE_CODE")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "ResponseCode.findAll", query = "SELECT r FROM ResponseCode r"),
		@NamedQuery(name = "ResponseCode.findByResponseCode", query = "SELECT r FROM ResponseCode r WHERE r.responseCode = :responseCode"),
		@NamedQuery(name = "ResponseCode.findByResponseDescription", query = "SELECT r FROM ResponseCode r WHERE r.responseDescription = :responseDescription"),
		@NamedQuery(name = "ResponseCode.findByIsSuccess", query = "SELECT r FROM ResponseCode r WHERE r.isSuccess = :isSuccess") })
public class ResponseCode implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 5)
	@Column(name = "RESPONSE_CODE")
	private String responseCode;
	@Size(max = 255)
	@Column(name = "RESPONSE_DESCRIPTION")
	private String responseDescription;
	@Size(max = 1)
	@Column(name = "IS_SUCCESS")
	private String isSuccess;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "responseCode1")
	private Collection<DeliveryChannelResponseCode> deliveryChannelResponseCodeCollection;

	public ResponseCode() {
	}

	public ResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseDescription() {
		return responseDescription;
	}

	public void setResponseDescription(String responseDescription) {
		this.responseDescription = responseDescription;
	}

	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	@XmlTransient
	public Collection<DeliveryChannelResponseCode> getDeliveryChannelResponseCodeCollection() {
		return deliveryChannelResponseCodeCollection;
	}

	public void setDeliveryChannelResponseCodeCollection(Collection<DeliveryChannelResponseCode> deliveryChannelResponseCodeCollection) {
		this.deliveryChannelResponseCodeCollection = deliveryChannelResponseCodeCollection;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (responseCode != null ? responseCode.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof ResponseCode)) {
			return false;
		}
		ResponseCode other = (ResponseCode) object;
		if ((this.responseCode == null && other.responseCode != null)
				|| (this.responseCode != null && !this.responseCode.equals(other.responseCode))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.ResponseCode[ responseCode=" + responseCode + " ]";
	}

}
