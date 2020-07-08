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
public class DeliveryChannelTransactionPK implements Serializable {

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "CHANNEL_CODE")
	private String channelCode;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 3)
	@Column(name = "TRANSACTION_CODE")
	private String transactionCode;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 5)
	@Column(name = "MESSAGE_TYPE")
	private String messageType;

	public DeliveryChannelTransactionPK() {
	}

	public DeliveryChannelTransactionPK(String channelCode, String transactionCode, String messageType) {
		this.channelCode = channelCode;
		this.transactionCode = transactionCode;
		this.messageType = messageType;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (channelCode != null ? channelCode.hashCode() : 0);
		hash += (transactionCode != null ? transactionCode.hashCode() : 0);
		hash += (messageType != null ? messageType.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DeliveryChannelTransactionPK)) {
			return false;
		}
		DeliveryChannelTransactionPK other = (DeliveryChannelTransactionPK) object;
		if ((this.channelCode == null && other.channelCode != null)
				|| (this.channelCode != null && !this.channelCode.equals(other.channelCode))) {
			return false;
		}
		if ((this.transactionCode == null && other.transactionCode != null)
				|| (this.transactionCode != null && !this.transactionCode.equals(other.transactionCode))) {
			return false;
		}
		if ((this.messageType == null && other.messageType != null)
				|| (this.messageType != null && !this.messageType.equals(other.messageType))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.DeliveryChannelTransactionPK[ channelCode=" + channelCode + ", transactionCode=" + transactionCode
				+ ", messageType=" + messageType + " ]";
	}

}
