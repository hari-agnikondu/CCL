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

import com.incomm.scheduler.service.SerialNumberService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class SerialNumberRequestConfiguration {

	private static final Logger logger = LogManager.getLogger(SerialNumberRequestConfiguration.class);

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	SerialNumberService serialNumberService;

	public SerialNumberRequestConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Bean(name="serialNumberRequest")
	public Job serialNumberRequestJob() {
		logger.info("...........Invoke serialNumberRequest JOB .........");
		return jobBuilderFactory.get(JobConstants.SERIAL_NUMBER_REQUEST_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepSerialNumberRequest()).end().build();
	}
	@Bean
	Step initialStepSerialNumberRequest() {
		return stepBuilderFactory.get(JobConstants.SERIAL_NUMBER_REQUEST_INITIAL_TASKLET).tasklet(initialSerialNumberRequestTask()).build();
	}

	@Bean
	protected Tasklet initialSerialNumberRequestTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				serialNumberService.serialNumberRequest();
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	protected Tasklet finalSerialNumberRequestTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}
	
	
}
