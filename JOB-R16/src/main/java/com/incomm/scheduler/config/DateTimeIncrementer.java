package com.incomm.scheduler.config;

/**
 * DateTimeIncrementer provides the unique Job_Instance_Id.
 * author venkateshgaddam
 */
import java.util.Date;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

public class DateTimeIncrementer implements JobParametersIncrementer
{
	@Override
	public JobParameters getNext(JobParameters jobParameters) {

		return new JobParametersBuilder().addLong("run.id", 
				new Date().getTime()).toJobParameters();

	}
}
