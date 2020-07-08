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
@Table(name = "TRANSACTION")
@XmlRootElement
@NamedQueries({ @NamedQuery(name = "Transaction.findAll", query = "SELECT t FROM Transaction t"),
		@NamedQuery(name = "Transaction.findByTransactionCode", query = "SELECT t FROM Transaction t WHERE t.transactionCode = :transactionCode"),
		@NamedQuery(name = "Transaction.findByTransactionDescription", query = "SELECT t FROM Transaction t WHERE t.transactionDescription = :transactionDescription"),
		@NamedQuery(name = "Transaction.findByTransactionShortName", query = "SELECT t FROM Transaction t WHERE t.transactionShortName = :transactionShortName"),
		@NamedQuery(name = "Transaction.findByIsFinancial", query = "SELECT t FROM Transaction t WHERE t.isFinancial = :isFinancial"),
		@NamedQuery(name = "Transaction.findByCreditDebitIndicator", query = "SELECT t FROM Transaction t WHERE t.creditDebitIndicator = :creditDebitIndicator") })
public class Transaction implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@NotNull
	@Size(min = 1, max = 3)
	@Column(name = "TRANSACTION_CODE")
	private String transactionCode;
	@Size(max = 255)
	@Column(name = "TRANSACTION_DESCRIPTION")
	private String transactionDescription;
	@Size(max = 50)
	@Column(name = "TRANSACTION_SHORT_NAME")
	private String transactionShortName;
	@Size(max = 1)
	@Column(name = "IS_FINANCIAL")
	private String isFinancial;
	@Size(max = 6)
	@Column(name = "CREDIT_DEBIT_INDICATOR")
	private String creditDebitIndicator;

	public Transaction() {
	}

	public Transaction(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public void setTransactionCode(String transactionCode) {
		this.transactionCode = transactionCode;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public String getTransactionShortName() {
		return transactionShortName;
	}

	public void setTransactionShortName(String transactionShortName) {
		this.transactionShortName = transactionShortName;
	}

	public String getIsFinancial() {
		return isFinancial;
	}

	public void setIsFinancial(String isFinancial) {
		this.isFinancial = isFinancial;
	}

	public String getCreditDebitIndicator() {
		return creditDebitIndicator;
	}

	public void setCreditDebitIndicator(String creditDebitIndicator) {
		this.creditDebitIndicator = creditDebitIndicator;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (transactionCode != null ? transactionCode.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Transaction)) {
			return false;
		}
		Transaction other = (Transaction) object;
		if ((this.transactionCode == null && other.transactionCode != null)
				|| (this.transactionCode != null && !this.transactionCode.equals(other.transactionCode))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.fs.cclp_spil.Transaction[ transactionCode=" + transactionCode + " ]";
	}

}
