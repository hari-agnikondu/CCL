package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "clp_configuration.PRODUCT_CARD_RANGE")
@AssociationOverrides({
		@AssociationOverride(name = "primaryKey.product", joinColumns = @JoinColumn(name = "PRODUCT_ID")),
		@AssociationOverride(name = "primaryKey.cardRange", joinColumns = @JoinColumn(name = "CARD_RANGE_ID")) })
public class ProductCardRange implements Serializable {

	private static final long serialVersionUID = 1L;

	// composite-id key
	private ProductCardRangeId primaryKey = new ProductCardRangeId();

	private Long cardRangeOrder;

	private long insUser;

	private Date insDate;

	private long lastUpdUser;

	private Date lastUpdDate;

	public ProductCardRange() {
		//Constructor
	}

	@EmbeddedId
	public ProductCardRangeId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ProductCardRangeId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "CARD_RANGE_ORDER")
	public Long getCardRangeOrder() {
		return cardRangeOrder;
	}

	public void setCardRangeOrder(Long cardRangeOrder) {
		this.cardRangeOrder = cardRangeOrder;
	}

	@Column(name = "INS_USER", updatable = false)
	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "INS_DATE", updatable = false)
	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	@Column(name = "LAST_UPD_USER")
	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_UPD_DATE")
	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	@Transient
	public Product getProduct() {
		return getPrimaryKey().getProduct();
	}

	public void setProduct(Product product) {
		getPrimaryKey().setProduct(product);
	}

	@Transient
	public CardRange getCardRange() {
		return getPrimaryKey().getCardRange();
	}

	public void setCardRange(CardRange cardRange) {
		getPrimaryKey().setCardRange(cardRange);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		ProductCardRange that = (ProductCardRange) obj;
		
		return Objects.equals(getPrimaryKey(), that.getPrimaryKey());
	}

	@Override
	public int hashCode() {
		return (getPrimaryKey() != null ? getPrimaryKey().hashCode() : 0);
	}
}
