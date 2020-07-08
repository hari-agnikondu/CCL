package com.incomm.scheduler.job.config;

/**
 * SchedulerOrchestrator contains start,stop and startAll methods used to stop and start the scheduled jobs.
 * author venkateshgaddam
 */

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.model.ProcessSchedule;
@RestController
@RequestMapping("/schedulerJob")
@Configuration
public class SchedulerOrchestrator {

	@Autowired
	private Map<String, SchedulerJob> schedulerInterface;
	@Autowired
	SchedulerJobDAO schedulerJobDAO;
	
	private static final Logger logger = LogManager.getLogger(SchedulerOrchestrator.class);

	
	@PostConstruct
	public void initScheduler() {
		startAll();
	}
	@RequestMapping(value="/restart/{jobId}",method = RequestMethod.PUT)
	public void restart(@PathVariable("jobId") String jobId) {
		ProcessSchedule processSchedule=schedulerJobDAO.getProcessSchedulerDetails(jobId);
		logger.info("job updated object "+processSchedule);
		stop(processSchedule);
		start(processSchedule);
	}

	public void stop(ProcessSchedule processSchedule) {
		if(processSchedule.getProcessClass()!=null && schedulerInterface.containsKey(processSchedule.getProcessClass())){
			schedulerInterface.get(processSchedule.getProcessClass()).stop();
		}
	}

	public void start(ProcessSchedule processSchedule) {
		if("E".equals(processSchedule.getSchedularStatus()) && processSchedule.getProcessClass()!=null 
				&& schedulerInterface.containsKey(processSchedule.getProcessClass())){
			schedulerInterface.get(processSchedule.getProcessClass()).start(processSchedule);
		} 
	}

	public void startAll() {
		List<ProcessSchedule> processSchedules=schedulerJobDAO.getProcessSchedulerDetailsList();
		if(!CollectionUtils.isEmpty(processSchedules)){
			for (Iterator<ProcessSchedule> iterator = processSchedules.iterator(); iterator
					.hasNext();) {
				ProcessSchedule processSchedule =  iterator.next();
				if("E".equals(processSchedule.getSchedularStatus()) && processSchedule.getProcessClass()!=null && 
						schedulerInterface.containsKey(processSchedule.getProcessClass())){
					schedulerInterface.get(processSchedule.getProcessClass()).start(processSchedule);
				}
			}
		}
		
	}

}