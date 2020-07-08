package com.incomm.cclp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
/**
 * The persistent class for the PRODUCT database table.
 * 
 */
@Entity
@Table(name="PROCESS_SCHEDULE")
public class ScheduleJob {

	@Id
	@Column(name="PROCESS_ID")
	private Long jobId;		
	
	@Column(name="PROCESS_NAME")
	private String jobName;		

	@Column(name="SCHEDULE_DAYS")
	private String totalDays;
	
	@Column(name="START_HOUR")
	private String startTimeHours;
	
	@Column(name="START_MIN")
	private String startTimeMins;
	
	@Column(name="RETRY_CNT")
	private String retryCount;

	@Column(name="PROCESS_INTERVAL")
	private String retryInterval;

	@Column(name="PROCINTERVAL_TYPE")
	private String retryIntervalTimeUnit;
	
	@Column(name="SCHEDULER_STAT")
	private String scheduleFlag;
	
	@Column(name="MAIL_SUCCESS")
	private String successMail;
	
	@Column(name="MAIL_FAIL")
	private String failMail;

	@Column(name="PROCRETRY_DATE")
	@Temporal(TemporalType.DATE)
	private Date retryDate;
	
	@Column(name="DAYOF_MONTH")
	private String dayOfMonth;
	
	@Column(name="MULTIRUN_INTERVAL")
	private String multipleRunInterval;
	
	@Column(name="MULTIRUN_INTERVAL_TYPE")
	private String multipleRunTimeUnit;
	
	@Column(name="MULTIRUN_FLAG")
	private String multipleRunFlag;

	
	
	public Long getJobId() {
		return jobId;
	}
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getTotalDays() {
		return totalDays;
	}
	public void setTotalDays(String totalDays) {
		this.totalDays = totalDays;
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

	public String getScheduleFlag() {
		return scheduleFlag;
	}
	public void setScheduleFlag(String scheduleFlag) {
		this.scheduleFlag = scheduleFlag;
	}

	public String getSuccessMail() {
		return successMail;
	}
	public void setSuccessMail(String successMail) {
		this.successMail = successMail;
	}

	public String getFailMail() {
		return failMail;
	}
	public void setFailMail(String failMail) {
		this.failMail = failMail;
	}

	public Date getRetryDate() {
		return retryDate;
	}
	public void setRetryDate(Date retryDate) {
		this.retryDate = retryDate;
	}

	public String getDayOfMonth() {
		return dayOfMonth;
	}
	public void setDayOfMonth(String dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
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

	public String getMultipleRunFlag() {
		return multipleRunFlag;
	}
	public void setMultipleRunFlag(String multipleRunFlag) {
		this.multipleRunFlag = multipleRunFlag;
	}
	public String getRetryIntervalTimeUnit() {
		return retryIntervalTimeUnit;
	}
	public void setRetryIntervalTimeUnit(String retryIntervalTimeUnit) {
		this.retryIntervalTimeUnit = retryIntervalTimeUnit;
	}
	@Override
	public String toString() {
		return "ScheduleJob [jobId=" + jobId + ", jobName=" + jobName + ", totalDays=" + totalDays + ", startTimeHours="
				+ startTimeHours + ", startTimeMins=" + startTimeMins + ", retryCount=" + retryCount
				+ ", retryInterval=" + retryInterval + ", retryIntervalTimeUnit=" + retryIntervalTimeUnit
				+ ", scheduleFlag=" + scheduleFlag + ", successMail=" + successMail + ", failMail=" + failMail
				+ ", retryDate=" + retryDate + ", dayOfMonth=" + dayOfMonth + ", multipleRunInterval="
				+ multipleRunInterval + ", multipleRunTimeUnit=" + multipleRunTimeUnit + ", multipleRunFlag="
				+ multipleRunFlag + "]";
	}
	
	
}
