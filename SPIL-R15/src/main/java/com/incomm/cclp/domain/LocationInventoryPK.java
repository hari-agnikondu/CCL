package com.incomm.cclp.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class LocationInventoryPK implements Serializable {

	private static final long serialVersionUID = 8422412365945440922L;

	@Basic(optional = false)
	@NotNull
	@Column(name = "MERCHANT_ID")
	String merchantId;

	@Basic(optional = false)
	@NotNull
	@Column(name = "LOCATION_ID")
	String locationId;

	@Basic(optional = false)
	@NotNull
	@Column(name = "PRODUCT_ID")
	BigDecimal productId;

	public LocationInventoryPK(String merchantId, String locationId, BigDecimal productId) {
		this.merchantId = merchantId;
		this.locationId = locationId;
		this.productId = productId;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public BigDecimal getProductId() {
		return productId;
	}

	public void setProductId(BigDecimal productId) {
		this.productId = productId;
	}
}
