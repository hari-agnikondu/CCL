package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductAlertId implements Serializable{

	private static final long serialVersionUID = 1L;
   
	@Column(name = "PRODUCT_ID")
	private Long productId;
	
	@Column(name = "ALERT_ID")
	private Long alertId;

	public ProductAlertId() {
	}

	public ProductAlertId(Long productId, Long alertId) {
		this.productId = productId;
		this.alertId = alertId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
 
        if (obj == null || getClass() != obj.getClass()) 
            return false;
 
        ProductAlertId that = (ProductAlertId) obj;
        return Objects.equals(productId, that.productId) && 
               Objects.equals(alertId, that.alertId);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(productId, alertId);
    }

}