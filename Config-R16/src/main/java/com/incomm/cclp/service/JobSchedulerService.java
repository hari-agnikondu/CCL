package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.ScheduleJobDTO;
import com.incomm.cclp.dto.SwitchOverSchedulerDTO;
import com.incomm.cclp.exception.ServiceException;

public interface JobSchedulerService {

	
	public ScheduleJobDTO getSchedulerConfig(long jobId) throws ServiceException;
	public Map<Long,String> getJobs() throws ServiceException ;
	public void updateSchedulerConfigForAJob(ScheduleJobDTO scheduleJobDTO) throws ServiceException; 
	public List<SwitchOverSchedulerDTO> getServers() throws ServiceException;
	public int updateSwitchOverScheduler(SwitchOverSchedulerDTO switchOverSchedulerDTO) throws ServiceException;
	List<Object[]> getJobSchedulerServiceJobs();
	public List<Object[]> getUserMailList();
	
}
