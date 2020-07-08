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

import com.incomm.scheduler.service.WeeklyFeeService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class WeeklyFeeConfiguration {

	private static final Logger logger = LogManager.getLogger(WeeklyFeeConfiguration.class);

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	WeeklyFeeService weeklyFeeService;

	public WeeklyFeeConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Bean(name="weeklyFee")
	public Job weeklyFeeJob() {
		logger.info("...........Invoke weeklyFeeJob JOB .........");
		return jobBuilderFactory.get(JobConstants.WEEKLY_FEE_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepWeeklyFee()).end().build();
	}
	@Bean
	Step initialStepWeeklyFee() {
		return stepBuilderFactory.get(JobConstants.WEEKLY_FEE_JOB_INITIAL_TASKLET).tasklet(initialWeeklyFeeGenerationTask()).build();
	}

	@Bean
	Step finalStepWeeklyFee() {
		return stepBuilderFactory.get(JobConstants.WEEKLY_FEE_JOB_FINAL_TASKLET).tasklet(finalWeeklyFeeGenerationTask()).build();
	}

	@Bean
	protected Tasklet initialWeeklyFeeGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				weeklyFeeService.weeklyFee();
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	protected Tasklet finalWeeklyFeeGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}
	
	
}
