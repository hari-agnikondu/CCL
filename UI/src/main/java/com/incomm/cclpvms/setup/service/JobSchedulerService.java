package com.incomm.cclpvms.setup.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.setup.model.JobScheduler;
import com.incomm.cclpvms.setup.model.SwitchOverScheduler;

public interface JobSchedulerService {

	public Map<Long,String> getAllJobs() throws ServiceException ;

	public ResponseDTO updateSchedulerConfig(JobScheduler jobScheduler) throws ServiceException ;

	public ResponseDTO getSchedulerConfigByJobId(long jobId) throws ServiceException;
	
	public List<SwitchOverScheduler> getAllServers() throws ServiceException;

	public ResponseDTO updateSwitchOverScheduler(SwitchOverScheduler sosDTO) throws ServiceException;

	List<List<Object>> getRunningJobs() throws ServiceException;

	List<Object[]> getUserMailDetail() throws ServiceException;
}
