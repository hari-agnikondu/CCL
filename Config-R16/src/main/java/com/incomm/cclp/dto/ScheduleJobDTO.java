package com.incomm.cclp.dto;

import java.util.List;
/**
 * The persistent class for the PRODUCT database table.
 * 
 */

public class ScheduleJobDTO {
	
	
	private Long jobId;
	private List<String> totalDays;
	private String jobName;		
	private String dayOfMonth;
	private String startTimeHours;
	private String startTimeMins;
	private String multipleRunFlag;
	private String multipleRunInterval;
	private String multipleRunTimeUnit;
	private String retryCount;
	private String retryInterval;
	private String retryIntervalTimeUnit;
	private String scheduleFlag;
	private List<String> successMailUser;
	private List<String> failMailUser;
	
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	public List<String> getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(List<String> totalDays) {
		this.totalDays = totalDays;
	}
	public String getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}
	
	public String getStartTimeHours() {
		return startTimeHours;
	}
	public void setStartTimeHours(String startTimeHours) {
		this.startTimeHours = startTimeHours;
	}
	public String getStartTimeMins() {
		return startTimeMins;
	}
	public void setStartTimeMins(String startTimeMins) {
		this.startTimeMins = startTimeMins;
	}
	public String getMultipleRunFlag() {
		return multipleRunFlag;
	}
	public void setMultipleRunFlag(String multipleRunFlag) {
		this.multipleRunFlag = multipleRunFlag;
	}
	public String getMultipleRunInterval() {
		return multipleRunInterval;
	}
	public void setMultipleRunInterval(String multipleRunInterval) {
		this.multipleRunInterval = multipleRunInterval;
	}
	public String getMultipleRunTimeUnit() {
		return multipleRunTimeUnit;
	}
	public void setMultipleRunTimeUnit(String multipleRunTimeUnit) {
		this.multipleRunTimeUnit = multipleRunTimeUnit;
	}
	public String getRetryCount() {
		return retryCount;
	}
	public void setRetryCount(String retryCount) {
		this.retryCount = retryCount;
	}
	public String getRetryInterval() {
		return retryInterval;
	}
	public void setRetryInterval(String retryInterval) {
		this.retryInterval = retryInterval;
	}
	public String getRetryIntervalTimeUnit() {
		return retryIntervalTimeUnit;
	}
	public void setRetryIntervalTimeUnit(String retryIntervalTimeUnit) {
		this.retryIntervalTimeUnit = retryIntervalTimeUnit;
	}
	public String getScheduleFlag() {
		return scheduleFlag;
	}
	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public List<String> getSuccessMailUser() {
		return successMailUser;
	}
	public void setSuccessMailUser(List<String> successMailUser) {
		this.successMailUser = successMailUser;
	}
	public List<String> getFailMailUser() {
		return failMailUser;
	}
	public void setFailMailUser(List<String> failMailUser) {
		this.failMailUser = failMailUser;
	}
	@Override
	public String toString() {
		return "ScheduleJobDTO [jobId=" + jobId + ", totalDays=" + totalDays + ", jobName=" + jobName + ", dayOfMonth="
				+ dayOfMonth + ", startTimeHours=" + startTimeHours + ", startTimeMins=" + startTimeMins
				+ ", multipleRunFlag=" + multipleRunFlag + ", multipleRunInterval=" + multipleRunInterval
				+ ", multipleRunTimeUnit=" + multipleRunTimeUnit + ", retryCount=" + retryCount + ", retryInterval="
				+ retryInterval + ", retryIntervalTimeUnit=" + retryIntervalTimeUnit + ", scheduleFlag=" + scheduleFlag
				+ ", successMailUser=" + successMailUser + ", failMailUser=" + failMailUser + "]";
	}
	
}
