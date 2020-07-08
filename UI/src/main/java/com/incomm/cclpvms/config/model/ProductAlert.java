package com.incomm.cclpvms.config.model;


import java.util.Map;

public class ProductAlert {


	private Long alertId;
	private Long productId;
	private String languageCode;
	private String smsTemplate;
	private String emailSubject;
	private String emailBody;
	
	
	private Map<String,String> alertAttributes;
	
	
	private Map<String, Map<String,String>> messageAttributes; 
	
	
	private String parentProductId;


	public String getParentProductId() {
		return parentProductId;
	}

	public void setParentProductId(String parentProductId) {
		this.parentProductId = parentProductId;
	}

	public Map<String, Map<String, String>> getMessageAttributes() {
		return messageAttributes;
	}

	public void setMessageAttributes(Map<String, Map<String, String>> messageAttributes) {
		this.messageAttributes = messageAttributes;
	}

	public ProductAlert() {
		
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

	public Map<String, String> getAlertAttributes() {
		return alertAttributes;
	}

	public void setAlertAttributes(Map<String, String> alertAttributes) {
		this.alertAttributes = alertAttributes;
	}

	public ProductAlert(Long alertId, Long productId, String languageCode, String smsTemplate, String emailSubject,
			String emailBody, Map<String, String> alertAttributes) {
		super();
		this.alertId = alertId;
		this.productId = productId;
		this.languageCode = languageCode;
		this.smsTemplate = smsTemplate;
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;
		this.alertAttributes = alertAttributes;
	}



	public ProductAlert(Map<String, String> alertAttributes) {
		
		this.alertAttributes = alertAttributes;
		
	}


	
	
	


}
