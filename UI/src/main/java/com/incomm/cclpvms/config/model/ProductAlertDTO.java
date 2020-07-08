package com.incomm.cclpvms.config.model;

import java.util.List;

public class ProductAlertDTO {


	private Long alertId;
	private Long productId;
	private String languageCode;
	private String smsTemplate;
	private String emailSubject;
	private String emailBody;
	private  List<String> productAlerts;
	
	public ProductAlertDTO() {
		
	}

	public Long getAlertId() {
		return alertId;
	}

	public void setAlertId(Long alertId) {
		this.alertId = alertId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public List<String> getProductAlerts() {
		return productAlerts;
	}

	public void setProductAlerts(List<String> productAlerts) {
		this.productAlerts = productAlerts;
	}

	public ProductAlertDTO(Long alertId, Long productId, String languageCode, String smsTemplate, String emailSubject,
			String emailBody, List<String> productAlerts) {
		super();
		this.alertId = alertId;
		this.productId = productId;
		this.languageCode = languageCode;
		this.smsTemplate = smsTemplate;
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;
		this.productAlerts = productAlerts;
	}


	
	
	
	
	


}
