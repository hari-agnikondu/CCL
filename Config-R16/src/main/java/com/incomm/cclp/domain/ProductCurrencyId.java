package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

	@Embeddable
	public class ProductCurrencyId implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private Product product;	
		
		private CurrencyCode currencyCode;

		@ManyToOne
		public Product getProduct() {
			return product;
		}

		public void setProduct(Product product) {
			this.product = product;
		}
		@ManyToOne
		public CurrencyCode getCurrencyCode() {
			return currencyCode;
		}

		public void setCurrencyCode(CurrencyCode currencyCode) {
			this.currencyCode = currencyCode;
		}

		@Override
		public boolean equals(Object obj) {
	        if (this == obj) return true;
	        if (obj == null || getClass() != obj.getClass()) return false;

	        ProductCurrencyId that = (ProductCurrencyId) obj;

	        return Objects.equals(product, that.product) &&
	        		Objects.equals(currencyCode, that.currencyCode);
	    }
		
		@Override
		public int hashCode() {
			return Objects.hash(product, currencyCode);
	    }

		
	
}
