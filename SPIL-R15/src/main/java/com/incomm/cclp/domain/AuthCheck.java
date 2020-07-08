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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author skocherla
 */
@Entity
@Table(name = "AUTH_CHECK")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "AuthCheck.findAll", query = "SELECT a FROM AuthCheck a"),
		@NamedQuery(name = "AuthCheck.findByTransactionCode", query = "SELECT a FROM AuthCheck a WHERE a.authCheckPK.transactionCode = :transactionCode"),
		@NamedQuery(name = "AuthCheck.findAuthChecks", query = "SELECT a FROM AuthCheck a WHERE a.authCheckPK.transactionCode = :transactionCode and a.authCheckPK.channelCode = :channelCode and a.authCheckPK.messageType = :messageType"),
		@NamedQuery(name = "AuthCheck.findByChannelCode", query = "SELECT a FROM AuthCheck a WHERE a.authCheckPK.channelCode = :channelCode"),
		@NamedQuery(name = "AuthCheck.findByMessageType", query = "SELECT a FROM AuthCheck a WHERE a.authCheckPK.messageType = :messageType"),
		@NamedQuery(name = "AuthCheck.findByCheckName", query = "SELECT a FROM AuthCheck a WHERE a.authCheckPK.checkName = :checkName"),
		@NamedQuery(name = "AuthCheck.findByAuthOrder", query = "SELECT a FROM AuthCheck a WHERE a.authOrder = :authOrder") })
public class AuthCheck implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected AuthCheckPK authCheckPK;
	@Column(name = "AUTH_ORDER")
	private Integer authOrder;

	public AuthCheck() {
	}

	public AuthCheck(AuthCheckPK authCheckPK) {
		this.authCheckPK = authCheckPK;
	}

	public AuthCheck(String transactionCode, String channelCode, String messageType, String checkName) {
		this.authCheckPK = new AuthCheckPK(transactionCode, channelCode, messageType, checkName);
	}

	public AuthCheckPK getAuthCheckPK() {
		return authCheckPK;
	}

	public void setAuthCheckPK(AuthCheckPK authCheckPK) {
		this.authCheckPK = authCheckPK;
	}

	public Integer getAuthOrder() {
		return authOrder;
	}

	public void setAuthOrder(Integer authOrder) {
		this.authOrder = authOrder;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (authCheckPK != null ? authCheckPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof AuthCheck)) {
			return false;
		}
		AuthCheck other = (AuthCheck) object;
		if ((this.authCheckPK == null && other.authCheckPK != null)
				|| (this.authCheckPK != null && !this.authCheckPK.equals(other.authCheckPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.AuthCheck[ authCheckPK=" + authCheckPK + " ]";
	}

}
