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

import com.incomm.scheduler.service.PostBackService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class PostBackConfiguration {
	
	private static final Logger logger = LogManager.getLogger(PostBackConfiguration.class);

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	PostBackService postBackService;

	public PostBackConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}

	@Bean(name="PostBackRetryJob")
	public Job postBackJob() {
		logger.info("...........Invoke PostBackJob .........");
		return jobBuilderFactory.get(JobConstants.POSTBACK_JOB_ID).incrementer(new DateTimeIncrementer())
				.flow(initialStepPostBack()).end().build();
	}


	@Bean
	Step initialStepPostBack() {
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_INITIAL_TASKLET).tasklet(postBackTask()).build();
	}

	@Bean
	protected Tasklet postBackTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {

				postBackService.postBackJob();

				return RepeatStatus.FINISHED;
			}
		};



	}

}

