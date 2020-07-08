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
@Table(name = "MESSAGE_TYPE")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "MessageType.findAll", query = "SELECT m FROM MessageType m"),
		@NamedQuery(name = "MessageType.findByMessageType", query = "SELECT m FROM MessageType m WHERE m.messageType = :messageType"),
		@NamedQuery(name = "MessageType.findByDescription", query = "SELECT m FROM MessageType m WHERE m.description = :description") })
public class MessageType implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 5)
	@Column(name = "MESSAGE_TYPE")
	private String messageType;
	@Size(max = 20)
	@Column(name = "DESCRIPTION")
	private String description;

	public MessageType() {
	}

	public MessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (messageType != null ? messageType.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof MessageType)) {
			return false;
		}
		MessageType other = (MessageType) object;
		if ((this.messageType == null && other.messageType != null)
				|| (this.messageType != null && !this.messageType.equals(other.messageType))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.MessageType[ messageType=" + messageType + " ]";
	}

}
