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
import org.springframework.context.annotation.Primary;

import com.incomm.scheduler.service.YearlyFeeService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class YearlyFeeConfiguration {

	private static final Logger logger = LogManager.getLogger(YearlyFeeConfiguration.class);
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	public YearlyFeeConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Autowired
	YearlyFeeService yearlyFeeService;

	@Bean(name="YearlyFeeJob")
	public Job yearlyFeeJob() {
		logger.info("...........Invoke yearlyFeeJob JOB .........");
		return jobBuilderFactory.get(JobConstants.YEARLY_FEE_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepYearlyFee()).end().build();
	}


	@Bean
	Step initialStepYearlyFee() {
		return stepBuilderFactory.get(JobConstants.YEARLY_FEE_JOB_INITIAL_TASKLET).tasklet(initialYearlyFeeGenerationTask()).build();
	}

	@Bean
	Step finalStepYearlyFee() {
		return stepBuilderFactory.get(JobConstants.YEARLY_FEE_JOB_FINAL_TASKLET).tasklet(finalYearlyFeeGenerationTask()).build();
	}

	@Bean
	protected Tasklet initialYearlyFeeGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				yearlyFeeService.yearlyFee();
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	protected Tasklet finalYearlyFeeGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}
}
