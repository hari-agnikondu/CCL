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

import com.incomm.scheduler.service.DigitalCardProcessingService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class DigitalCardProcessingConfiguration {
	
	public static final Logger logger = LogManager.getLogger(DigitalCardProcessingConfiguration.class);
	
	@Autowired
	DigitalCardProcessingService digitalCardProcessingService;
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	public DigitalCardProcessingConfiguration(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Bean(name="DigitalCardProcessingJob")
	public Job DigitalCardProcessingJob() {
		logger.info("...........Invoke DigitalCardProcessing JOB .........");
		return jobBuilderFactory.get(JobConstants.DIGITAL_CARD_PROCESSING_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepDigitalCardProcessing()).end().build();
	}
	@Bean
	Step initialStepDigitalCardProcessing() {
		return stepBuilderFactory.get(JobConstants.DIGITAL_CARD_PROCESSING_JOB_INITIAL_TASKLET).tasklet(initialDigitalCardProcessingTask()).build();
	}

	@Bean
	Step finalStepDigitalCardProcessing() {
		return stepBuilderFactory.get(JobConstants.DIGITAL_CARD_PROCESSING_JOB_FINAL_TASKLET).tasklet(finalDigitalCardProcessingTask()).build();
	}

	private Tasklet initialDigitalCardProcessingTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Initial Tasklet Start");
				digitalCardProcessingService.digitalCardProcessing();
				return RepeatStatus.FINISHED;
			}
		};
	}

	private Tasklet finalDigitalCardProcessingTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}
}

