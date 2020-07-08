package com.incomm.cclp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="clp_configuration.product_alert")
public class ProductAlert implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ProductAlertId primaryKey = new ProductAlertId();
	
	@ManyToOne
	@JoinColumn(name="PRODUCT_ID", referencedColumnName="PRODUCT_ID")
	@MapsId("productId")
	private Product product;
	
	@ManyToOne
	@JoinColumn(name="ALERT_ID", referencedColumnName="ALERT_ID")
	@MapsId("alertId")
	private Alert alert;
	
	@Column(name="LANGUAGE_CODE")
	private String languageCode;
	
	@Column(name="SMS_TEMPLATE")
	private String smsTemplate;
	
	@Column(name="EMAIL_SUBJECT")
	private String emailSubject;
	
	@Column(name="EMAIL_BODY")
	private String emailBody;
	
	@Column(name="INS_USER",updatable = false )
	private long insUser;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INS_DATE",updatable = false)
	private Date insDate;
	
	@Column(name="LAST_UPD_USER")
	private long lastUpdUser;

	@Temporal(TemporalType.DATE)
	@Column(name="LAST_UPD_DATE")
	private Date lastUpdDate;

	public ProductAlertId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ProductAlertId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Transient
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@Transient
	public Alert getAlert() {
		return alert;
	}

	public void setAlert(Alert alert) {
		this.alert = alert;
	}

	public String getLanguageCode() {
		return languageCode;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}

	public String getSmsTemplate() {
		return smsTemplate;
	}

	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public long getInsUser() {
		return insUser;
	}

	public void setInsUser(long insUser) {
		this.insUser = insUser;
	}

	public Date getInsDate() {
		return insDate;
	}

	public void setInsDate(Date insDate) {
		this.insDate = new java.sql.Date(new java.util.Date().getTime());
	}

	public long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(long lastUpdUser) {
		this.lastUpdUser = lastUpdUser;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	@Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
 
        if (obj == null || getClass() != obj.getClass())
            return false;
 
        ProductAlert that = (ProductAlert) obj;
        return Objects.equals(product, that.product) &&
               Objects.equals(alert, that.alert);
    }
 
    @Override
    public int hashCode() {
        return Objects.hash(product, alert);
    }
}