package com.incomm.cclp.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.incomm.cclp.constants.QueryConstants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;

@Entity
@Table(name = "REDEMPTION_LOCK")
@Builder(builderClassName = "Builder")
@AllArgsConstructor

@NamedQueries({ @NamedQuery(name = "RedemptionLock.getRedemptionLocks", query = QueryConstants.GET_REDEMPTION_LOCKS) })

public class RedemptionLock {

	@EmbeddedId
	private RedemptionLockPK redemptionLockPK;

	@Column(name = "CARD_NUM_HASH")
	@NotNull
	private String cardNumberHash;

	@Column(name = "ACCOUNT_ID")
	@NotNull
	private long accountId;

	@Column(name = "PURSE_ID")
	@NotNull
	private long purseId;

	@Column(name = "DELIVERY_CHANNEL")
	private String deliveryChannel;

	@Column(name = "RRN")
	@NotNull
	private String rrn;

	@Column(name = "TRANSACTION_DATE")
	private String transactionDate;

	@Column(name = "TRANSACTION_TIME")
	private String transactionTime;

	@Column(name = "TRANSACTION_AMOUNT")
	private BigDecimal transactionAmount;

	@Column(name = "AUTH_AMOUNT")
	private BigDecimal authorizedAmount;

	@Column(name = "TRANFEE_AMOUNT")
	private BigDecimal transactionFee;

	@Column(name = "LOCK_FLAG")
	@Setter
	private String lockFlag;

	@Column(name = "STORE_ID")
	private String storeId;

	@Column(name = "TERMINAL_ID")
	private String terminalId;

	@Column(name = "OPENING_BALANCE")
	private BigDecimal ledgerBalance;

	@Column(name = "CLOSING_BALANCE")
	private BigDecimal closingLedgerBalance;

	@Column(name = "AVAIL_OPENING_BALANCE")
	private BigDecimal availableBalance;

	@Column(name = "AVAIL_CLOSING_BALANCE")
	private BigDecimal closingAvailableBalance;

	@Column(name = "INS_DATE")
	private LocalDateTime insDate;

	@Column(name = "UNLOCK_DATE")
	private LocalDateTime unlockDate;

	@Column(name = "LOCK_EXPIRY")
	private LocalDateTime lockExpiry;

	@Column(name = "MAX_FEE_FLAG")
	private String maxFeeFlag;

	@Column(name = "FREE_FEE_FLAG")
	private String freeFeeFlag;

	@Column(name = "UNLOCK_RRN")
	private String unlockRrn;

	@Column(name = "LOCK_FOUND")
	private String lockFound;

	@Column(name = "CARD_NUM_ENCR")
	private String cardNumberEncrypted;

	@Column(name = "SOURCE_DESCRIPTION", columnDefinition = "default='CCLP'")
	private String sourceDescription;

	public RedemptionLock() {
	}
}
