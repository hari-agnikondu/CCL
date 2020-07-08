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
public class AuthCheckPK implements Serializable {

	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 3)
	@Column(name = "TRANSACTION_CODE")
	private String transactionCode;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 2)
	@Column(name = "CHANNEL_CODE")
	private String channelCode;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 5)
	@Column(name = "MESSAGE_TYPE")
	private String messageType;
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 100)
	@Column(name = "CHECK_NAME")
	private String checkName;

	public AuthCheckPK() {
	}

	public AuthCheckPK(String transactionCode, String channelCode, String messageType, String checkName) {
		this.transactionCode = transactionCode;
		this.channelCode = channelCode;
		this.messageType = messageType;
		this.checkName = checkName;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (transactionCode != null ? transactionCode.hashCode() : 0);
		hash += (channelCode != null ? channelCode.hashCode() : 0);
		hash += (messageType != null ? messageType.hashCode() : 0);
		hash += (checkName != null ? checkName.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof AuthCheckPK)) {
			return false;
		}
		AuthCheckPK other = (AuthCheckPK) object;
		if ((this.transactionCode == null && other.transactionCode != null)
				|| (this.transactionCode != null && !this.transactionCode.equals(other.transactionCode))) {
			return false;
		}
		if ((this.channelCode == null && other.channelCode != null)
				|| (this.channelCode != null && !this.channelCode.equals(other.channelCode))) {
			return false;
		}
		if ((this.messageType == null && other.messageType != null)
				|| (this.messageType != null && !this.messageType.equals(other.messageType))) {
			return false;
		}
		if ((this.checkName == null && other.checkName != null) || (this.checkName != null && !this.checkName.equals(other.checkName))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.AuthCheckPK[ transactionCode=" + transactionCode + ", channelCode=" + channelCode + ", messageType="
				+ messageType + ", checkName=" + checkName + " ]";
	}

}
