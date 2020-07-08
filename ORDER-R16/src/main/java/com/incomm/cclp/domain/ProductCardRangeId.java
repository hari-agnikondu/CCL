package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class ProductCardRangeId implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Product product;	
	
	private CardRange cardRange;

	@ManyToOne
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@ManyToOne
	public CardRange getCardRange() {
		return cardRange;
	}

	public void setCardRange(CardRange cardRange) {
		this.cardRange = cardRange;
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        ProductCardRangeId that = (ProductCardRangeId) obj;
        
        return Objects.equals(product, that.product) && 
                Objects.equals(cardRange, that.cardRange);
    }
	
	@Override
	public int hashCode() {
		return Objects.hash(product, cardRange);
    }

}
