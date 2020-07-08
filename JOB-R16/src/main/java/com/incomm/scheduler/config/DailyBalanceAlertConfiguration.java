package com.incomm.scheduler.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.incomm.scheduler.jobsteps.BatchListener;
import com.incomm.scheduler.service.DailyBalanceAlertService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class DailyBalanceAlertConfiguration {
	private static final Logger logger = LogManager.getLogger(DailyBalanceAlertConfiguration.class);

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	DailyBalanceAlertService dailyBalanceAlertService;

	public DailyBalanceAlertConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}

	@Bean(name="DailyBalanceJob")
	public Job dailyBalanceJob() {
		logger.info("...........Invoke DailyBalanceJob JOB .........");
		return jobBuilderFactory.get(JobConstants.DAILY_BALANCE_JOB_ID).listener(new BatchListener()).incrementer(new DateTimeIncrementer())
				.flow(initialStepBal()).end().build();
	}


	@Bean
	Step initialStepBal() {
		return stepBuilderFactory.get(JobConstants.DAILY_BALANCE_INITIAL_TASKLET).tasklet(initialBalGenerationTask()).build();
	}

	@Bean
	Step finalStepBal() {
		return stepBuilderFactory.get(JobConstants.DAILY_BALANCE_FINAL_TASKLET).tasklet(finalBalGenerationTask()).build();
	}

	@Bean
	protected Tasklet initialBalGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				dailyBalanceAlertService.dailyBalanceAlert();
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	protected Tasklet finalBalGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}

}
