package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ProductPurseId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Product product;	
	
	private Purse purse;

	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	public Purse getPurse() {
		return purse;
	}

	public void setPurse(Purse purse) {
		this.purse = purse;
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ProductPurseId that = (ProductPurseId) obj;

        return Objects.equals(product, that.product) &&
        		Objects.equals(purse, that.purse);
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(product, purse);
    }
}
