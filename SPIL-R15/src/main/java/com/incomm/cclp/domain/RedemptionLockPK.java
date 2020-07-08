package com.incomm.cclp.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Embeddable
@Data
@AllArgsConstructor
public class RedemptionLockPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "ACCOUNT_PURSE_ID")
	private long accountPurseId;

	@Basic(optional = false)
	@NotNull
	@Column(name = "TRANSACTION_SQID")
	private long transactionSqid;

	public RedemptionLockPK() {
	}
}
