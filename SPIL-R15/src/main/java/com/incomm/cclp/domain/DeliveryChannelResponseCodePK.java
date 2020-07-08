/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author skocherla
 */
@Embeddable
public class DeliveryChannelResponseCodePK implements Serializable {

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "CHANNEL_CODE")
	private String channelCode;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 5)
	@Column(name = "RESPONSE_CODE")
	private String responseCode;

	public DeliveryChannelResponseCodePK() {
	}

	public DeliveryChannelResponseCodePK(String channelCode, String responseCode) {
		this.channelCode = channelCode;
		this.responseCode = responseCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (channelCode != null ? channelCode.hashCode() : 0);
		hash += (responseCode != null ? responseCode.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DeliveryChannelResponseCodePK)) {
			return false;
		}
		DeliveryChannelResponseCodePK other = (DeliveryChannelResponseCodePK) object;
		if ((this.channelCode == null && other.channelCode != null)
				|| (this.channelCode != null && !this.channelCode.equals(other.channelCode))) {
			return false;
		}
		if ((this.responseCode == null && other.responseCode != null)
				|| (this.responseCode != null && !this.responseCode.equals(other.responseCode))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.DeliveryChannelResponseCodePK[ channelCode=" + channelCode + ", responseCode=" + responseCode
				+ " ]";
	}

}
