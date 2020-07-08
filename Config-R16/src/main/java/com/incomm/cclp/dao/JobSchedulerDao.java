package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.ScheduleJob;
import com.incomm.cclp.dto.SwitchOverSchedulerDTO;

public interface JobSchedulerDao {

	public ScheduleJob getSchedulerConfig(long jobId) ;
	public List<Object[]> getJobs();
	public int updateSchedulerConfigForAJob(ScheduleJob scheduleJobDTO) ;
	public void createSchedulerConfig(ScheduleJob scheduleJobDTO);
	public List<Object[]> getServers();
	public int getSchedulerAlreadyRuning(String status);
	public int updateSwitchOverScheduler(SwitchOverSchedulerDTO switchOverSchedulerDTO);
	List<Object[]> getRunningJobs();
	public List<Object[]> getUserMailList();
}
