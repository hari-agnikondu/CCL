package com.incomm.cclp.dto;

import java.util.List;


public class AlertDTO {private Long alertId;
private String alertName;
private  List<String> productAlerts;

public Long getAlertId() {
	return alertId;
}
public void setAlertId(Long alertId) {
	this.alertId = alertId;
}
public String getAlertName() {
	return alertName;
}
public void setAlertName(String alertName) {
	this.alertName = alertName;
}
public List<String> getProductAlerts() {
	return productAlerts;
}
public void setProductAlerts(List<String> productAlerts) {
	this.productAlerts = productAlerts;
}
public AlertDTO() {

}
public AlertDTO(Long alertId, String alertName, List<String> productAlerts) {
	super();
	this.alertId = alertId;
	this.alertName = alertName;
	this.productAlerts = productAlerts;
}
	
	}



