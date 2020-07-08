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

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Entity
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Table(name="PRODUCT_ALERT")
public class ProductAlert implements Serializable {

	private static final long serialVersionUID = 1L;

	private ProductAlertId primaryKey = new ProductAlertId();

	private String smsTemplate;

	private String emailSubject;

	private String emailBody;

	private Long insUser;

	private Date insDate;

	private Long lastUpdUser;

	private Date lastUpdDate;

	@EmbeddedId
	public ProductAlertId getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(ProductAlertId primaryKey) {
		this.primaryKey = primaryKey;
	}

	@Column(name = "SMS_TEMPLATE")
	public String getSmsTemplate() {
		return smsTemplate;
	}

	public void setSmsTemplate(String smsTemplate) {
		this.smsTemplate = smsTemplate;
	}

	@Column(name = "EMAIL_SUBJECT")
	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	@Column(name = "EMAIL_BODY")
	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	@Column(name = "INS_USER", updatable = false)
	public Long getInsUser() {
		return insUser;
	}

	public void setInsUser(Long insUser) {
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
	public Long getLastUpdUser() {
		return lastUpdUser;
	}

	public void setLastUpdUser(Long lastUpdUser) {
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
	public Alert getAlert() {
		return getPrimaryKey().getAlert();
	}

	public void setAlert(Alert alert) {
		getPrimaryKey().setAlert(alert);
	}

	@Transient
	public String getLanguageCode() {
		return getPrimaryKey().getLanguageCode();
	}

	public void setLanguageCode(String languageCode) {
		getPrimaryKey().setLanguageCode(languageCode);
	}
}