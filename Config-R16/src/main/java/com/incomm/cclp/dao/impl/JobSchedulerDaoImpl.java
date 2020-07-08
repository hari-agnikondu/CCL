package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.JobSchedulerDao;
import com.incomm.cclp.domain.ScheduleJob;
import com.incomm.cclp.dto.SwitchOverSchedulerDTO;


@Repository
public class JobSchedulerDaoImpl implements JobSchedulerDao{

	@PersistenceContext
	private EntityManager em;
	
	@Override
	public ScheduleJob getSchedulerConfig(long jobId) {	
		return em.find(ScheduleJob.class, jobId);
	}
	
	@Override
	public List<Object[]> getJobs(){
		
		TypedQuery <Object[]> query = em.createQuery(QueryConstants.GET_ALL_JOBS, Object[].class);

		return query.getResultList();
	}

	@Override
	@Transactional
	public int updateSchedulerConfigForAJob(ScheduleJob scheduleJobDTO) {
		
		Query query = em.createNativeQuery(QueryConstants.UPDATE_SCHEDULER_CONFIG);
		query.setParameter("days",scheduleJobDTO.getTotalDays());
		query.setParameter("startHour",scheduleJobDTO.getStartTimeHours() );
		query.setParameter("startMin", scheduleJobDTO.getStartTimeMins());
		query.setParameter("retryCnt", scheduleJobDTO.getRetryCount());
		query.setParameter("processInt",scheduleJobDTO.getRetryInterval() );
		query.setParameter("schedulerStat",scheduleJobDTO.getScheduleFlag() );
		query.setParameter("mailSuccess",scheduleJobDTO.getSuccessMail() );
		query.setParameter("mailFailure", scheduleJobDTO.getFailMail());
		query.setParameter("monthDays",scheduleJobDTO.getDayOfMonth() );
		query.setParameter("multiInt", scheduleJobDTO.getMultipleRunInterval());
		query.setParameter("multiType",scheduleJobDTO.getMultipleRunTimeUnit() );
		query.setParameter("multiFlag",scheduleJobDTO.getMultipleRunFlag()==null?"0": scheduleJobDTO.getMultipleRunFlag());
		query.setParameter("procIntType",scheduleJobDTO.getRetryIntervalTimeUnit() );
		query.setParameter("procID",scheduleJobDTO.getJobId() );
		
		return query.executeUpdate();
		
	}
	
	@Override
	public void createSchedulerConfig(ScheduleJob scheduleJobDTO) {

			em.persist(scheduleJobDTO);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getServers() {
		return  em.createNativeQuery(QueryConstants.GET_ALL_SERVERS).getResultList();
	}

	@Override
	public int getSchedulerAlreadyRuning(String status) {
		return ((Number) em.createNativeQuery(QueryConstants.SERVER_RUNNING_CHECK)
				.setParameter("SCHEDULER_RUNNING", status).getSingleResult()).intValue();
	}

	@Override
	@Transactional
	public int updateSwitchOverScheduler(SwitchOverSchedulerDTO switchOverSchedulerDTO) {
		return em.createNativeQuery(QueryConstants.UPDATE_SERVER)
				.setParameter("SCHEDULER_RUNNING", switchOverSchedulerDTO.getStatus())
				.setParameter("PHYSICAL_SERVER", switchOverSchedulerDTO.getPhysicalServer()).executeUpdate();
	}
	@Override
	public List<Object[]> getRunningJobs() {
		Query query = em.createNativeQuery(QueryConstants.GET_ALL_RUNNING_JOBS);		
		@SuppressWarnings("unchecked")
		List<Object[]> runningJobs = query.getResultList();
		return runningJobs;
	}

	@Override
	public List<Object[]> getUserMailList() {
		Query query = em.createNativeQuery(QueryConstants.GET_USER_MAIL_LIST);		
		@SuppressWarnings("unchecked")
		 List<Object[]> userMailList = query.getResultList();
		return userMailList;	
	
	}
	
}
