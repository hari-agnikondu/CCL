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
@Table(name = "DELIVERY_CHANNEL_TRANSACTION")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "DeliveryChannelTransaction.findAll", query = "SELECT d FROM DeliveryChannelTransaction d"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByChannelCode", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.deliveryChannelTransactionPK.channelCode = :channelCode"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByTransactionCode", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.deliveryChannelTransactionPK.transactionCode = :transactionCode"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByChannelTransactionCode", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.channelTransactionCode = :channelTransactionCode"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByMrif", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.mrif = :mrif"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByErif", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.erif = :erif"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByFlexDescription", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.flexDescription = :flexDescription"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByMessageType", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.deliveryChannelTransactionPK.messageType = :messageType"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByPartySupported", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.partySupported = :partySupported"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByAuthJavaClass", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.authJavaClass = :authJavaClass"),
		@NamedQuery(name = "DeliveryChannelTransaction.findBySuccessAlerts", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.successAlerts = :successAlerts"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByFailureAlerts", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.failureAlerts = :failureAlerts"),
		@NamedQuery(name = "DeliveryChannelTransaction.findByTransactionIdentifier", query = "SELECT d FROM DeliveryChannelTransaction d WHERE d.transactionIdentifier = :transactionIdentifier") })
public class DeliveryChannelTransaction implements Serializable {

	private static final long serialVersionUID = 1L;
	@EmbeddedId
	protected DeliveryChannelTransactionPK deliveryChannelTransactionPK;
	@Size(max = 50)
	@Column(name = "CHANNEL_TRANSACTION_CODE")
	private String channelTransactionCode;
	@Size(max = 1)
	@Column(name = "MRIF")
	private String mrif;
	@Size(max = 1)
	@Column(name = "ERIF")
	private String erif;
	@Size(max = 100)
	@Column(name = "FLEX_DESCRIPTION")
	private String flexDescription;
	@Size(max = 20)
	@Column(name = "PARTY_SUPPORTED")
	private String partySupported;
	@Size(max = 100)
	@Column(name = "AUTH_JAVA_CLASS")
	private String authJavaClass;
	@Size(max = 100)
	@Column(name = "SUCCESS_ALERTS")
	private String successAlerts;
	@Size(max = 200)
	@Column(name = "FAILURE_ALERTS")
	private String failureAlerts;
	@Size(max = 1)
	@Column(name = "TRANSACTION_IDENTIFIER")
	private String transactionIdentifier;
	@JoinColumn(name = "CHANNEL_CODE", referencedColumnName = "CHANNEL_CODE", insertable = false, updatable = false)
	@ManyToOne(optional = false)
	private DeliveryChannel deliveryChannel;

	public DeliveryChannelTransaction() {
	}

	public DeliveryChannelTransaction(DeliveryChannelTransactionPK deliveryChannelTransactionPK) {
		this.deliveryChannelTransactionPK = deliveryChannelTransactionPK;
	}

	public DeliveryChannelTransaction(String channelCode, String transactionCode, String messageType) {
		this.deliveryChannelTransactionPK = new DeliveryChannelTransactionPK(channelCode, transactionCode, messageType);
	}

	public DeliveryChannelTransactionPK getDeliveryChannelTransactionPK() {
		return deliveryChannelTransactionPK;
	}

	public void setDeliveryChannelTransactionPK(DeliveryChannelTransactionPK deliveryChannelTransactionPK) {
		this.deliveryChannelTransactionPK = deliveryChannelTransactionPK;
	}

	public String getChannelTransactionCode() {
		return channelTransactionCode;
	}

	public void setChannelTransactionCode(String channelTransactionCode) {
		this.channelTransactionCode = channelTransactionCode;
	}

	public String getMrif() {
		return mrif;
	}

	public void setMrif(String mrif) {
		this.mrif = mrif;
	}

	public String getErif() {
		return erif;
	}

	public void setErif(String erif) {
		this.erif = erif;
	}

	public String getFlexDescription() {
		return flexDescription;
	}

	public void setFlexDescription(String flexDescription) {
		this.flexDescription = flexDescription;
	}

	public String getPartySupported() {
		return partySupported;
	}

	public void setPartySupported(String partySupported) {
		this.partySupported = partySupported;
	}

	public String getAuthJavaClass() {
		return authJavaClass;
	}

	public void setAuthJavaClass(String authJavaClass) {
		this.authJavaClass = authJavaClass;
	}

	public String getSuccessAlerts() {
		return successAlerts;
	}

	public void setSuccessAlerts(String successAlerts) {
		this.successAlerts = successAlerts;
	}

	public String getFailureAlerts() {
		return failureAlerts;
	}

	public void setFailureAlerts(String failureAlerts) {
		this.failureAlerts = failureAlerts;
	}

	public String getTransactionIdentifier() {
		return transactionIdentifier;
	}

	public void setTransactionIdentifier(String transactionIdentifier) {
		this.transactionIdentifier = transactionIdentifier;
	}

	public DeliveryChannel getDeliveryChannel() {
		return deliveryChannel;
	}

	public void setDeliveryChannel(DeliveryChannel deliveryChannel) {
		this.deliveryChannel = deliveryChannel;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (deliveryChannelTransactionPK != null ? deliveryChannelTransactionPK.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof DeliveryChannelTransaction)) {
			return false;
		}
		DeliveryChannelTransaction other = (DeliveryChannelTransaction) object;
		if ((this.deliveryChannelTransactionPK == null && other.deliveryChannelTransactionPK != null)
				|| (this.deliveryChannelTransactionPK != null
						&& !this.deliveryChannelTransactionPK.equals(other.deliveryChannelTransactionPK))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.DeliveryChannelTransaction[ deliveryChannelTransactionPK=" + deliveryChannelTransactionPK + " ]";
	}

}
