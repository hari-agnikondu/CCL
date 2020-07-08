package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.JobSchedulerDao;
import com.incomm.cclp.domain.ScheduleJob;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.dto.ScheduleJobDTO;
import com.incomm.cclp.dto.SwitchOverSchedulerDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.JobSchedulerService;



@Service
public class JobSchedulerServiceImpl implements JobSchedulerService{

	private static final Logger logger = LogManager.getLogger(JobSchedulerServiceImpl.class);

	
	
	
	@Autowired
	JobSchedulerDao jobSchedulerDao;
	
	@Value("${SCHEDULER_JOB_URL}") 
	String schedulerJobUrl;
	
	@Override
	public  ScheduleJobDTO getSchedulerConfig(long jobId) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();	
		ScheduleJob scheduleJob = 	jobSchedulerDao.getSchedulerConfig(jobId);
		if (scheduleJob == null) {
			logger.error("Error while fetching record for jobId: {}",jobId," Object is null");
			throw new ServiceException(ResponseMessages.SCHEDULERCONFIG_RETRIEVE_FAIL, ResponseMessages.FAILURE);
		}
		
		mm.addMappings(new TypeMappingIgnoreListProperties());
		ScheduleJobDTO scheduleJobDTO = mm.map(scheduleJob, ScheduleJobDTO.class);
		if(scheduleJob.getTotalDays()!=null) scheduleJobDTO.setTotalDays(Arrays.asList(scheduleJob.getTotalDays().split("\\|")));
		if(scheduleJob.getFailMail()!=null) scheduleJobDTO.setFailMailUser(Arrays.asList(scheduleJob.getFailMail().split("\\|")));
		if(scheduleJob.getSuccessMail()!=null) scheduleJobDTO.setSuccessMailUser(Arrays.asList(scheduleJob.getSuccessMail().split("\\|")));
		
		logger.info("EXIT");
		return scheduleJobDTO;
	}

	@Override
	public Map<Long, String> getJobs() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		List<Object[]> jobList=jobSchedulerDao.getJobs();
		
		Map<Long,String> jobMap=null;
		if(jobList!=null && !jobList.isEmpty()) {
			jobMap=new HashMap<>();
			for (Object[] obj : jobList) {
				jobMap.put((Long)obj[0], (String)obj[1]);
			}
		}else {
			logger.error("No Jobs are Configured");
			throw new ServiceException(ResponseMessages.JOBS_DONOT_EXIST, ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);
		return jobMap;
	}

	@Override
	public List<Object[]> getUserMailList() {

		return jobSchedulerDao.getUserMailList();

	}

	@Override
	public void updateSchedulerConfigForAJob(ScheduleJobDTO scheduleJobDTO) throws ServiceException {
		
		logger.info(CCLPConstants.ENTER);
		
		ModelMapper mm = new ModelMapper();
		
		mm.addMappings(new TypeMappingIgnoreListPropertiesToStoreInDB());
		ScheduleJob scheduleJob = mm.map(scheduleJobDTO, ScheduleJob.class);
		
		scheduleJob.setTotalDays(scheduleJobDTO.getTotalDays()!=null? String.join("|", scheduleJobDTO.getTotalDays()):"");
		scheduleJob.setSuccessMail(scheduleJobDTO.getSuccessMailUser()!=null?String.join("|", scheduleJobDTO.getSuccessMailUser()):"");
		scheduleJob.setFailMail(scheduleJobDTO.getFailMailUser()!=null?String.join("|", scheduleJobDTO.getFailMailUser()):"");
		int updateCnt= jobSchedulerDao.updateSchedulerConfigForAJob(scheduleJob);
		if(updateCnt < 1)  {
			logger.error("Error while updating record for : {}",scheduleJobDTO.getJobName());
			throw new ServiceException(ResponseMessages.SCHEDULERCONFIG_UPDATE_FAIL, ResponseMessages.FAILURE);
		}
		else
		{
			try {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> entity = new HttpEntity<>(headers);
				
				logger.debug("Calling '{}' service to update SchedulerConfig", schedulerJobUrl);
				
				 new RestTemplate().exchange(
						schedulerJobUrl + "/schedulerJob/restart/{jobId}", HttpMethod.PUT, entity,
						ResponseDTO.class,scheduleJobDTO.getJobId());
			} catch (RestClientException e) {
				logger.error("Exception in calling SchedulerJob {}", e);
			}
		}
		logger.info(CCLPConstants.EXIT);
	}

	@Override
	public List<SwitchOverSchedulerDTO> getServers() throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		List<Object[]> serversList=jobSchedulerDao.getServers();
		SwitchOverSchedulerDTO dto = null;
		
		List<SwitchOverSchedulerDTO> sosDTO =new ArrayList<>();
		if(serversList!=null && !serversList.isEmpty()) {
			
			for (Object[] obj : serversList) {
				dto = new SwitchOverSchedulerDTO();
				dto.setPhysicalServer((String) obj[0]);
				dto.setManagedServer((String) obj[1]);
				dto.setPort( ( (Number)obj[2]).longValue());
				dto.setStatus((String) obj[3]);
				sosDTO.add(dto);
			}
		}
		else {
			logger.error("No Jobs are Configured");
			throw new ServiceException(ResponseMessages.SERVERS_DOESNOT_EXIST, ResponseMessages.DOESNOT_EXISTS);
		}
		logger.info(CCLPConstants.EXIT);
		return sosDTO;
	}

	@Override
	public int updateSwitchOverScheduler(SwitchOverSchedulerDTO switchOverSchedulerDTO) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		int updatecount = 0;
		
		if ((switchOverSchedulerDTO != null) && (switchOverSchedulerDTO.getStatus().equals("N"))) {
			
			if (jobSchedulerDao.getSchedulerAlreadyRuning("Y")>=1) {
				logger.error("Error while updating record for : {}", switchOverSchedulerDTO.getPhysicalServer());
				throw new ServiceException(ResponseMessages.SWITCHOVERSCHEDULER_ALREADY_RUNNING, ResponseMessages.FAILURE);
			} else {
				
				switchOverSchedulerDTO.setStatus("Y");
				updatecount = jobSchedulerDao.updateSwitchOverScheduler(switchOverSchedulerDTO);
			}
		} else if ((switchOverSchedulerDTO != null)&&(switchOverSchedulerDTO.getStatus().equals("Y"))) {
			
			switchOverSchedulerDTO.setStatus("N");
			updatecount = jobSchedulerDao.updateSwitchOverScheduler(switchOverSchedulerDTO);
		}
		else
		{
			logger.error("Error while updating record for Switch Over Scheduler");
			throw new ServiceException(ResponseMessages.SWITCHOVERSCHEDULER_UPDATE_FAIL, ResponseMessages.FAILURE);
		}
		logger.info(CCLPConstants.EXIT);
		return updatecount;
	}
	@Override
	public List<Object[]> getJobSchedulerServiceJobs() {
			return jobSchedulerDao.getRunningJobs();		
	}

}
class TypeMappingIgnoreListProperties extends PropertyMap<ScheduleJob, ScheduleJobDTO>
{
    @Override
    protected void configure()
    {
        skip().setTotalDays(null);
        skip().setFailMailUser(null);
        skip().setSuccessMailUser(null);
        
    }
}
class TypeMappingIgnoreListPropertiesToStoreInDB extends PropertyMap<ScheduleJobDTO,ScheduleJob>
{
    @Override
    protected void configure()
    {
        skip().setTotalDays(null);
        skip().setFailMail(null);
        skip().setSuccessMail(null);
        
    }
    
}
