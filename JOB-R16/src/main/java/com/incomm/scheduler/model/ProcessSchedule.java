package com.incomm.scheduler.model;

import java.util.Date;

import com.incomm.scheduler.utils.Util;

public class ProcessSchedule {
	
	private String processId;
	private String processName;
	private String processRetryIntervel;
	private String scheduleDays;
	private String startHH;
	private String startMM;
	private int startSS;
	private String endHH;
	private String endMM;
	private String endSS;
	private String fileId;
	private String processType;
	private String processRetryIntervalType;
	private String retryCnt;
	private String depSubProcessId;
	private String schedularStatus;
	private String mailSuccess;
	private String mailFail;
	private String depProccessId;
	private String procRunning;
	private Date procRetryDate;
	private String processClass;
	private String processJob;
	private String processJobGroup;
	private String processTrigger;
	private String processTriggerGroup;
	private String procCompleteFlag;
	private Date procCompleteDate;
	private String eventType;
	private String frontConfig;
	private String dayOfMonth;
	private String multiRunInterval;
	private String multiRunIntervalType;
	private String multiRunFlag;
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public String getScheduleDays() {
		return scheduleDays;
	}
	public void setScheduleDays(String scheduleDays) {
		this.scheduleDays = scheduleDays;
	}
	public String getStartHH() {
		return !Util.isEmpty(startHH)?startHH:"00";
	}
	public void setStartHH(String startHH) {
		this.startHH = startHH;
	}
	public String getStartMM() {
		return !Util.isEmpty(startMM)?startMM:"00";
	}
	public void setStartMM(String startMM) {
		this.startMM = startMM;
	}
	public int getStartSS() {
		return startSS;
	}
	public void setStartSS(int startSS) {
		this.startSS = startSS;
	}
	public String getEndHH() {
		return endHH;
	}
	public void setEndHH(String endHH) {
		this.endHH = endHH;
	}
	public String getEndMM() {
		return endMM;
	}
	public void setEndMM(String endMM) {
		this.endMM = endMM;
	}
	public String getEndSS() {
		return endSS;
	}
	public void setEndSS(String endSS) {
		this.endSS = endSS;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getProcessType() {
		return processType;
	}
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	public String getRetryCnt() {
		return retryCnt;
	}
	public void setRetryCnt(String retryCnt) {
		this.retryCnt = retryCnt;
	}
	public String getDepSubProcessId() {
		return depSubProcessId;
	}
	public void setDepSubProcessId(String depSubProcessId) {
		this.depSubProcessId = depSubProcessId;
	}
	public String getSchedularStatus() {
		return schedularStatus;
	}
	public void setSchedularStatus(String schedularStatus) {
		this.schedularStatus = schedularStatus;
	}
	public String getMailSuccess() {
		return mailSuccess;
	}
	public void setMailSuccess(String mailSuccess) {
		this.mailSuccess = mailSuccess;
	}
	public String getMailFail() {
		return mailFail;
	}
	public void setMailFail(String mailFail) {
		this.mailFail = mailFail;
	}
	public String getDepProccessId() {
		return depProccessId;
	}
	public void setDepProccessId(String depProccessId) {
		this.depProccessId = depProccessId;
	}
	public String getProcRunning() {
		return procRunning;
	}
	public void setProcRunning(String procRunning) {
		this.procRunning = procRunning;
	}
	public String getProcessClass() {
		return processClass;
	}
	public void setProcessClass(String processClass) {
		this.processClass = processClass;
	}
	public String getProcessJob() {
		return processJob;
	}
	public void setProcessJob(String processJob) {
		this.processJob = processJob;
	}
	public String getProcessJobGroup() {
		return processJobGroup;
	}
	public void setProcessJobGroup(String processJobGroup) {
		this.processJobGroup = processJobGroup;
	}
	public String getProcessTrigger() {
		return processTrigger;
	}
	public void setProcessTrigger(String processTrigger) {
		this.processTrigger = processTrigger;
	}
	public String getProcessTriggerGroup() {
		return processTriggerGroup;
	}
	public void setProcessTriggerGroup(String processTriggerGroup) {
		this.processTriggerGroup = processTriggerGroup;
	}
	public String getProcCompleteFlag() {
		return procCompleteFlag;
	}
	public void setProcCompleteFlag(String procCompleteFlag) {
		this.procCompleteFlag = procCompleteFlag;
	}
	public String getEventType() {
		return eventType;
	}
	public void setEventType(String eventType) {
		this.eventType = eventType;
	}
	public String getFrontConfig() {
		return frontConfig;
	}
	public void setFrontConfig(String frontConfig) {
		this.frontConfig = frontConfig;
	}
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	public String getMultiRunInterval() {
		return !Util.isEmpty(multiRunInterval)?multiRunInterval:"0";
	}
	public void setMultiRunInterval(String multiRunInterval) {
		this.multiRunInterval = multiRunInterval;
	}
	public String getMultiRunIntervalType() {
		return multiRunIntervalType;
	}
	public void setMultiRunIntervalType(String multiRunIntervalType) {
		this.multiRunIntervalType = multiRunIntervalType;
	}
	public String getMultiRunFlag() {
		return multiRunFlag;
	}
	public void setMultiRunFlag(String multiRunFlag) {
		this.multiRunFlag = multiRunFlag;
	}
	public Date getProcCompleteDate() {
		return procCompleteDate;
	}
	public void setProcCompleteDate(Date procCompleteDate) {
		this.procCompleteDate = procCompleteDate;
	}
	public Date getProcRetryDate() {
		return procRetryDate;
	}
	public void setProcRetryDate(Date procRetryDate) {
		this.procRetryDate = procRetryDate;
	}
	
	@Override
	public String toString() {
		return "ProcessSchedule [processId=" + processId + ", processName="
				+ processName + ", processRetryIntervel=" + processRetryIntervel
				+ ", scheduleDays=" + scheduleDays + ", startHH=" + startHH
				+ ", startMM=" + startMM + ", startSS=" + startSS + ", endHH="
				+ endHH + ", endMM=" + endMM + ", endSS=" + endSS + ", fileId="
				+ fileId + ", processType=" + processType
				+ ", processRetryIntervalType=" + processRetryIntervalType
				+ ", retryCnt=" + retryCnt + ", depSubProcessId="
				+ depSubProcessId + ", schedularStatus=" + schedularStatus
				+ ", mailSuccess=" + mailSuccess + ", mailFail=" + mailFail
				+ ", depProccessId=" + depProccessId + ", procRunning="
				+ procRunning + ", procRetryDate=" + getProcRetryDate()
				+ ", processClass=" + processClass + ", processJob="
				+ processJob + ", processJobGroup=" + processJobGroup
				+ ", processTrigger=" + processTrigger
				+ ", processTriggerGroup=" + processTriggerGroup
				+ ", procCompleteFlag=" + procCompleteFlag
				+ ", procCompleteDate=" + getProcCompleteDate() + ", eventType="
				+ eventType + ", frontConfig=" + frontConfig + ", dayOfMonth="
				+ dayOfMonth + ", multiRunInterval=" + multiRunInterval
				+ ", multiRunIntervalType=" + multiRunIntervalType
				+ ", multiRunFlag=" + multiRunFlag + "]";
	}
	public String getProcessRetryIntervel() {
		return processRetryIntervel;
	}
	public void setProcessRetryIntervel(String processRetryIntervel) {
		this.processRetryIntervel = processRetryIntervel;
	}
	public String getProcessRetryIntervalType() {
		return processRetryIntervalType;
	}
	public void setProcessRetryIntervalType(String processRetryIntervalType) {
		this.processRetryIntervalType = processRetryIntervalType;
	}

}
