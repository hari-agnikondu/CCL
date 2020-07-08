package com.incomm.cclp.dto;

public class SwitchOverSchedulerDTO {

	private String physicalServer;
	private String managedServer;
	private Long port;
	private String status;
	
	public String getPhysicalServer() {
		return physicalServer;
	}
	public void setPhysicalServer(String physicalServer) {
		this.physicalServer = physicalServer;
	}
	public String getManagedServer() {
		return managedServer;
	}
	public void setManagedServer(String managedServer) {
		this.managedServer = managedServer;
	}
	
	public Long getPort() {
		return port;
	}
	public void setPort(Long port) {
		this.port = port;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "SwitchOverScheduler [physicalServer=" + physicalServer + ", managedServer=" + managedServer + ", port="
				+ port + ", status=" + status + "]";
	}
	
}
