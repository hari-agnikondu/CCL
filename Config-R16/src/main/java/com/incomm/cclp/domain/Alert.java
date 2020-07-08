package com.incomm.cclp.domain;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "ALERT")
public class Alert implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ALERT_ID")
	private Long alertId;
	
	@Column(name = "ALERT_NAME")
	private String alertName;
	
	@Column(name = "ALERT_SHORT_NAME")
	private String alertShortName;
	
	
	
	
	public String getAlertShortName() {
		return alertShortName;
	}
	public void setAlertShortName(String alertShortName) {
		this.alertShortName = alertShortName;
	}
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

	public Alert() {
	
	}
	public Alert(Long alertId, String alertName) {
		super();
		this.alertId = alertId;
		this.alertName = alertName;
	}
	@Override
	public String toString() {
		return "Alert [alertName=" + alertName + ", alertShortName=" + alertShortName + "]";
	}
	

}
