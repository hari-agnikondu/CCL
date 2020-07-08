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

import com.incomm.scheduler.service.MonthlyFeeService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class MonthlyFeeConfiguration {
	
	public static final Logger logger = LogManager.getLogger(MonthlyFeeConfiguration.class);
	
	@Autowired
	MonthlyFeeService monthlyFeeService;
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	public MonthlyFeeConfiguration(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Bean(name="MonthlyFeeJob")
	public Job monthlyFeeJob() {
		logger.info("...........Invoke MonthlyFeeJob JOB .........");
		return jobBuilderFactory.get(JobConstants.MONTHLY_FEE_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepMonthlyFee()).end().build();
	}
	@Bean
	Step initialStepMonthlyFee() {
		return stepBuilderFactory.get(JobConstants.MONTHLY_FEE_JOB_INITIAL_TASKLET).tasklet(initialMonthlyFeeGenerationTask()).build();
	}

	@Bean
	Step finalStepMonthlyFee() {
		return stepBuilderFactory.get(JobConstants.MONTHLY_FEE_JOB_FINAL_TASKLET).tasklet(finalMonthlyFeeGenerationTask()).build();
	}

	private Tasklet initialMonthlyFeeGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				monthlyFeeService.monthlyFee();
				return RepeatStatus.FINISHED;
			}
		};
	}

	private Tasklet finalMonthlyFeeGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}
}
