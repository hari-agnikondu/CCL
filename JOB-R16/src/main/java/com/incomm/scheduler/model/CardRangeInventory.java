package com.incomm.scheduler.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "CARD_RANGE_INVENTORY")
public class CardRangeInventory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CARD_RANGE_ID")
	private Long cardRangeId;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
	private CardRange cardRange;

	@Column(name = "TOTAL_INVENTORY")
	private Long totalInventory;

	@Column(name = "AVAILABLE_INVENTORY")
	private Long availableInventory;

	@Column(name = "ISSUED_INVENTORY")
	private Long issuedInventory;

	@Column(name = "LAST_UPD_INVENTORY_DATE")
	private Date lastUpdInventoryDate;

	@Column(name = "IS_INVENTORY_GENERATED")
	private String isInventoryGenerated;

	@Column(name = "LAST_CHUNK")
	private Long lastChunk;

	@Column(name = "INVENTORY_STATUS")
	private String inventoryStatus;

	@Column(name = "IS_USED")
	private String isUsed;

	public Long getCardRangeId() {
		return cardRangeId;
	}

	public void setCardRangeId(Long cardRangeId) {
		this.cardRangeId = cardRangeId;
	}

	public CardRange getCardRange() {
		return cardRange;
	}

	public void setCardRange(CardRange cardRange) {
		this.cardRange = cardRange;
	}

	public Long getTotalInventory() {
		return totalInventory;
	}

	public void setTotalInventory(Long totalInventory) {
		this.totalInventory = totalInventory;
	}

	public Long getAvailableInventory() {
		return availableInventory;
	}

	public void setAvailableInventory(Long availableInventory) {
		this.availableInventory = availableInventory;
	}

	public Long getIssuedInventory() {
		return issuedInventory;
	}

	public void setIssuedInventory(Long issuedInventory) {
		this.issuedInventory = issuedInventory;
	}

	public Date getLastUpdInventoryDate() {
		return lastUpdInventoryDate;
	}

	public void setLastUpdInventoryDate(Date lastUpdInventoryDate) {
		this.lastUpdInventoryDate = lastUpdInventoryDate;
	}

	public String getIsInventoryGenerated() {
		return isInventoryGenerated;
	}

	public void setIsInventoryGenerated(String isInventoryGenerated) {
		this.isInventoryGenerated = isInventoryGenerated;
	}

	public Long getLastChunk() {
		return lastChunk;
	}

	public void setLastChunk(Long lastChunk) {
		this.lastChunk = lastChunk;
	}

	public String getInventoryStatus() {
		return inventoryStatus;
	}

	public void setInventoryStatus(String inventoryStatus) {
		this.inventoryStatus = inventoryStatus;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public CardRangeInventory() {
		//constructor
	}

}
