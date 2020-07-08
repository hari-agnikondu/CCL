/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author skocherla
 */
@Embeddable
public class StatementsLogPK implements Serializable {

	@Basic(optional = false)
	@NotNull
	@Column(name = "RECORD_SEQ")
	private BigInteger recordSeq;
	@Basic(optional = false)
	@NotNull
	@Column(name = "TRANSACTION_SQID")
	private BigDecimal transactionSqid;

	public StatementsLogPK() {
	}

	public StatementsLogPK(BigInteger recordSeq, BigDecimal transactionSqid) {
		this.recordSeq = recordSeq;
		this.transactionSqid = transactionSqid;
	}

	public BigInteger getRecordSeq() {
		return recordSeq;
	}

	public void setRecordSeq(BigInteger recordSeq) {
		this.recordSeq = recordSeq;
	}

	public BigDecimal getTransactionSqid() {
		return transactionSqid;
	}

	public void setTransactionSqid(BigDecimal transactionSqid) {
		this.transactionSqid = transactionSqid;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (recordSeq != null ? recordSeq.hashCode() : 0);
		hash += (transactionSqid != null ? transactionSqid.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof StatementsLogPK)) {
			return false;
		}
		StatementsLogPK other = (StatementsLogPK) object;
		if ((this.recordSeq == null && other.recordSeq != null) || (this.recordSeq != null && !this.recordSeq.equals(other.recordSeq))) {
			return false;
		}
		if ((this.transactionSqid == null && other.transactionSqid != null)
				|| (this.transactionSqid != null && !this.transactionSqid.equals(other.transactionSqid))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.incomm.cclp.StatementsLogPK[ recordSeq=" + recordSeq + ", transactionSqid=" + transactionSqid + " ]";
	}

}
