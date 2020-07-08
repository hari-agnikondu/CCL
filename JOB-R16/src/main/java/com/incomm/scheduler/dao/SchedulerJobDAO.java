package com.incomm.scheduler.dao;

import java.util.Date;
import java.util.List;

import com.incomm.scheduler.model.ProcessSchedule;

public interface SchedulerJobDAO {
	public ProcessSchedule getProcessSchedulerDetails(String jobId);
	public List<ProcessSchedule> getProcessSchedulerDetailsList();
	
	public int updateErrorLog(String fileName,String jobName,String msg,String errorDesc);
	public int updateSchedulerProcStatus(String procRunningFlag,String procCompletedFlag,Date completeDate,String processId);

	public List<String> retrieveMailList(String mailIds);
} 
