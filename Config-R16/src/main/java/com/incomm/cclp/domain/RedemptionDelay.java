package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="REDEMPTION_DELAY")
public class RedemptionDelay implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	MerchantProductRedemptionId primaryKey = new MerchantProductRedemptionId();
	
	@Column(name="START_TIME_DISPLAY")
	private String startTimeDisplay;
	
	@Column(name="END_TIME_DISPLAY")
	private String endTimeDisplay;
	
	@Column(name="REDEMPTION_DELAY_TIME")
	private Long redemptionDelayTime;
	
	@Column(name="INS_USER")
	private Long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE")
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private Long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	

	
	public MerchantProductRedemptionId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(MerchantProductRedemptionId primaryKey) {
		this.primaryKey = primaryKey;
	}
	
	
	
	@Transient
	public MerchantRedemption getMerchant() {
		return getPrimaryKey().getMerchant();
	}



	public void setMerchant(MerchantRedemption merchant) {
		this.getPrimaryKey().setMerchant(merchant);
	}

	@Transient
	public Product getProduct() {
		return getPrimaryKey().getProduct();
	}

	public void setProduct(Product product) {
		this.getPrimaryKey().setProduct(product);
	}
	
	public String getStartTimeDisplay() {
		return startTimeDisplay;
	}

	public void setStartTimeDisplay(String startTimeDisplay) {
		this.startTimeDisplay = startTimeDisplay;
	}

	public String getEndTimeDisplay() {
		return endTimeDisplay;
	}

	public void setEndTimeDisplay(String endTimeDisplay) {
		this.endTimeDisplay = endTimeDisplay;
	}

	public Long getRedemptionDelayTime() {
		return redemptionDelayTime;
	}

	public void setRedemptionDelayTime(Long redemptionDelayTime) {
		this.redemptionDelayTime = redemptionDelayTime;
	}

	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	@Override
	public String toString() {
		return "RedemptionDelay [primaryKey=" + primaryKey + ", startTimeDisplay=" + startTimeDisplay
				+ ", endTimeDisplay=" + endTimeDisplay + ", redemptionDelayTime=" + redemptionDelayTime + ", insUser="
				+ insUser + ", insDate=" + insDate + ", lastUpdUser=" + lastUpdUser + ", lastUpdDate=" + lastUpdDate
				+ "]";
	}
	
	
}
