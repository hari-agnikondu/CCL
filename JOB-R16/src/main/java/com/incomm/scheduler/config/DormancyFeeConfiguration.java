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

import com.incomm.scheduler.service.DormancyFeeService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class DormancyFeeConfiguration {

	private static final Logger logger = LogManager.getLogger(DormancyFeeConfiguration.class);
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	public DormancyFeeConfiguration(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	 
	@Autowired
	DormancyFeeService dormanycFeeService;

	@Bean(name="DormancyFeeJob")
	public Job dormancyFeeJob() {
		logger.info("...........Invoke DormancyFeeJob JOB .........");
		return jobBuilderFactory.get(JobConstants.DORMANCY_FEE_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepDormancyFee()).next(finalStepDormancyFee()).end().build();
	}
	@Bean
	Step initialStepDormancyFee() {
		return stepBuilderFactory.get(JobConstants.DORMANCY_FEE_JOB_INITIAL_TASKLET).tasklet(initialDormancyFeeGenerationTask()).build();
	}

	@Bean
	Step finalStepDormancyFee() {
		return stepBuilderFactory.get(JobConstants.DORMANCY_FEE_JOB_FINAL_TASKLET).tasklet(finalDormancyFeeGenerationTask()).build();
	}

	private Tasklet initialDormancyFeeGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				dormanycFeeService.dormancyFee();
				return RepeatStatus.FINISHED;
			}
		};
	}

	private Tasklet finalDormancyFeeGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}
}
