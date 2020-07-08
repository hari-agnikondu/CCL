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

import com.incomm.scheduler.service.EODPreAuthReleaseService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class EODPreAuthReleaseConfiguration {
	
public static final Logger logger = LogManager.getLogger(EODPreAuthReleaseConfiguration.class);
	
	@Autowired
	EODPreAuthReleaseService eodPreAuthReleaseService;
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	public EODPreAuthReleaseConfiguration(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}
	
	@Bean(name="EODPreAuthReleaseJob")
	public Job eodPreAuthReleaseJob() {
		logger.info("...........Invoke EODPreAuthReleaseJob JOB .........");
		return jobBuilderFactory.get(JobConstants.EOD_PREAUTH_RELEASE_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepEODPreAuthRelease()).end().build();
	}
	@Bean
	Step initialStepEODPreAuthRelease() {
		return stepBuilderFactory.get(JobConstants.EOD_PREAUTH_RELEASE_JOB_INITIAL_TASKLET).tasklet(initialEODPreAuthReleaseGenerationTask()).build();
	}

	@Bean
	Step finalStepEODPreAuthRelease() {
		return stepBuilderFactory.get(JobConstants.EOD_PREAUTH_RELEASE_JOB_FINAL_TASKLET).tasklet(finalEODPreAuthReleaseGenerationTask()).build();
	}

	private Tasklet initialEODPreAuthReleaseGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				eodPreAuthReleaseService.EODPreAuthRelease();
				return RepeatStatus.FINISHED;
			}
		};
	}

	private Tasklet finalEODPreAuthReleaseGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}

}
