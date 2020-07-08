package com.incomm.scheduler.job;

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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.job.config.SchedulerJob;
import com.incomm.scheduler.model.ProcessSchedule;
import com.incomm.scheduler.notification.NotificationService;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.Util;

@Service("com.incomm.scheduler.job.BulkTransactionJob")
public class BulkTransactionJob implements SchedulerJob {
	// the logger
	private static final Logger logger = LogManager.getLogger(BulkTransactionJob.class);
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
	@Qualifier("bulkTransactionJob")
	Job bulkTransactionJob;
	
	@Value("${BULK_TXN_FILE_PATH}") String bulkTxnPath;
	
	@Autowired
	NotificationService notificationService;

	
	@Override
	public void start(ProcessSchedule processSchedule) {
		future = scheduler.schedule(()->{
			
			schedulerJobDAO.updateSchedulerProcStatus("Y",null,null,processSchedule.getProcessId());
			JobParameters jobParameters = new JobParametersBuilder()
			.addLong(JobConstants.DATETIME, System.currentTimeMillis())
			.addString(JobConstants.JOBNAME, JobConstants.BULK_TRANSACTION_JOB_ID)
			.addString(JobConstants.DIRECTORY_PATH,bulkTxnPath)
			.toJobParameters();
			JobExecution execution;
			String exitStatus = "";
			long startTime = 0;
			try {
				 startTime = System.currentTimeMillis();
				execution = jobLauncher.run(bulkTransactionJob, jobParameters);
				exitStatus = execution.getStatus().toString();
				logger.info("*******exitStatus***"+exitStatus);
			} catch (JobExecutionAlreadyRunningException | JobRestartException
					| JobInstanceAlreadyCompleteException
					| JobParametersInvalidException e) {
				logger.error("Exception in start method of Bulk transaction:"+e.getMessage());
			}
			finally{
				long endTime = System.currentTimeMillis();
				logger.error("Total time taken:"+ (startTime-endTime));
				schedulerJobDAO.updateSchedulerProcStatus("N","C",new Date(),processSchedule.getProcessId());
				/*if("COMPLETED".equals(exitStatus) && processSchedule.getMailSuccess()!=null ){
					notificationService.sendEmailNotification(processSchedule.getMailSuccess(),JobConstants.BULK_TRANSACTION_JOB_ID,"Bulk transaction File Uploaded Successfully");
				}
				else if("FAILED".equals(exitStatus) && processSchedule.getMailFail()!=null){
					notificationService.sendEmailNotification(processSchedule.getMailFail(),JobConstants.BULK_TRANSACTION_JOB_ID,"Bulk transaction File Upload Process Failed");
				}*/
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