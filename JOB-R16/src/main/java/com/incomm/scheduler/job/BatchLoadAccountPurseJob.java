package com.incomm.scheduler.job;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

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

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service("com.incomm.scheduler.job.BatchLoadAccountPurseJob")
public class BatchLoadAccountPurseJob implements SchedulerJob {

	@Autowired
	JobRepository jobRepository;

	@Autowired
	private TaskScheduler scheduler;

	@Autowired
	SchedulerJobDAO schedulerJobDAO;

	@Autowired
	JobLauncher jobLauncher;

	@Autowired
	@Qualifier("loadAccountPurseBatchJob")
	Job loadAccountPurseJob;

	@Autowired
	NotificationService notificationService;

	private ScheduledFuture<?> future;

	
	@Override
	public void start(ProcessSchedule processSchedule) {
		future = scheduler.schedule(() -> {

			schedulerJobDAO.updateSchedulerProcStatus("Y", null, null, processSchedule.getProcessId());
			JobParameters jobParameters = new JobParametersBuilder().addLong(JobConstants.DATETIME, System.currentTimeMillis())
				.addString(JobConstants.JOBNAME, JobConstants.BATCH_LOAD_ACCOUNT_PURSE_JOB_ID)
				.toJobParameters();
			JobExecution execution;
			String exitStatus = "";
			long startTime = 0;
			try {
				startTime = System.currentTimeMillis();
				execution = jobLauncher.run(loadAccountPurseJob, jobParameters);
				exitStatus = execution.getStatus()
					.toString();
				log.info("*******exitStatus***" + exitStatus);

			} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				log.error("Exception in start method of Load Account Purse Batch Job :" + e.getMessage(), e);
			} finally {
				long endTime = System.currentTimeMillis();
				log.info("Total time taken:" + (startTime - endTime));
				schedulerJobDAO.updateSchedulerProcStatus("N", "C", new Date(), processSchedule.getProcessId());
				if("COMPLETED".equals(exitStatus) && processSchedule.getMailSuccess()!=null ){
					notificationService.sendEmailNotification(processSchedule.getMailSuccess(),JobConstants.BATCH_LOAD_ACCOUNT_PURSE_JOB_ID,"Batch Load Account Purse Job Successfully");
				}
				else if("FAILED".equals(exitStatus) && processSchedule.getMailFail()!=null){
					notificationService.sendEmailNotification(processSchedule.getMailFail(),JobConstants.BATCH_LOAD_ACCOUNT_PURSE_JOB_ID, "Batch Load Account Purse Job Failed");
				}
			}

		}, triggerContext -> {
			CronTrigger trigger = new CronTrigger(Util.getCronExp(processSchedule));
			return trigger.nextExecutionTime(triggerContext);
		});
	}

	@Override
	public void stop() {
		if (future != null)
			future.cancel(false);
	}

}
