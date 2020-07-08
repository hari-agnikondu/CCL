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

import com.incomm.scheduler.service.ChangeCardStatusService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class ChangeCardStatusConfiguration {
	
public static final Logger logger = LogManager.getLogger(ChangeCardStatusConfiguration.class);
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	public ChangeCardStatusConfiguration(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Autowired
	ChangeCardStatusService changeCardStatusService;
	
	@Bean(name="ChangeCardStatusJob")
	public Job changeCardStatusJob() {
		logger.info("...........Invoke ChangeCardStatus JOB .........");
		return jobBuilderFactory.get(JobConstants.CHANGE_CARD_STATUS_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepChangeCardStatus()).end().build();
	}

	private Step initialStepChangeCardStatus() {
		return stepBuilderFactory.get(JobConstants.CHANGE_CARD_STATUS_INITIAL_TASKLET).tasklet(initialChangeCardStatusTask()).build();
	}
	
	@Bean
	Step finalStepChangeCardStatus() {
		return stepBuilderFactory.get(JobConstants.CHANGE_CARD_STATUS_FINAL_TASKLET).tasklet(finalChangeCardStatusTask()).build();
	}

	private Tasklet initialChangeCardStatusTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				changeCardStatusService.changeCardStatus();
				return RepeatStatus.FINISHED;
			}
		};
	}

	private Tasklet finalChangeCardStatusTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}

}
