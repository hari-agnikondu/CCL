package com.incomm.scheduler.job;
/**
 * B2BCCFFileGenerationJob provides to start the scheduled B2B CCF File Generation Jobs based on configuration
 * author venkateshgaddam
 */

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.job.config.SchedulerJob;
import com.incomm.scheduler.model.ProcessSchedule;
import com.incomm.scheduler.notification.NotificationService;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.Util;

@Service("com.incomm.scheduler.job.B2BCCFFileGenerationJob")
public class B2BCCFFileGenerationJob implements SchedulerJob {
	// the logger
	private static final Logger logger = LogManager.getLogger(B2BCCFFileGenerationJob.class);
	@Autowired
	JobRepository jobRepository;

	private ScheduledFuture<?> future;
	@Autowired
	private TaskScheduler scheduler;

	@Autowired
	SchedulerJobDAO schedulerJobDAO;
	
	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("CCFGenerationJob")
	Job ccfGenerationJob;
	
	@Autowired
	NotificationService notificationService;

	@Override
	public void start(ProcessSchedule processSchedule) {
		future = scheduler.schedule(()->{
			
			schedulerJobDAO.updateSchedulerProcStatus("Y",null,null,processSchedule.getProcessId());
			JobParameters jobParameters = new JobParametersBuilder()
			.addLong(JobConstants.DATETIME, System.currentTimeMillis())
			.addString(JobConstants.JOBNAME, JobConstants.CCF_GENERATION_JOB_ID)
			.addString(JobConstants.CCF_ORDER_TYPE, "B2B")
			.toJobParameters();
			JobExecution execution;
			String exitStatus = "";
			try {
				execution = jobLauncher.run(ccfGenerationJob, jobParameters);
				exitStatus = execution.getStatus().toString();
				logger.info("*******exitStatus***"+exitStatus);
			} catch (JobExecutionAlreadyRunningException | JobRestartException
					| JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				logger.error("Exception in start method of CCFFileGenerationJob:"+e.getMessage());
			}
			finally{
				schedulerJobDAO.updateSchedulerProcStatus("N","C",new Date(),processSchedule.getProcessId());
				if("COMPLETED".equals(exitStatus) && processSchedule.getMailSuccess()!=null ){
					notificationService.sendEmailNotification(processSchedule.getMailSuccess(),JobConstants.CCF_GENERATION_JOB_ID,"B2B CCF Files Generated Successfully");
				}
				else if("FAILED".equals(exitStatus) && processSchedule.getMailFail()!=null){
					notificationService.sendEmailNotification(processSchedule.getMailFail(),JobConstants.CCF_GENERATION_JOB_ID,"B2B CCF Files Generation Failed");
				}
			}
		},triggerContext->{
			CronTrigger trigger = new CronTrigger(Util.getCronExp(processSchedule));
			return trigger.nextExecutionTime(triggerContext);
		});
	}

	@Override
	public void stop() {
		if(future!=null)
		future.cancel(false);
	}

}