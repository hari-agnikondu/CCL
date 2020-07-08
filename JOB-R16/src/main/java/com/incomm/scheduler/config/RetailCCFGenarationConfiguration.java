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

import com.incomm.scheduler.dao.CCFGenerationDAO;
import com.incomm.scheduler.jobsteps.BatchListener;
import com.incomm.scheduler.tasklet.InitialRetailCCFGenerationTasklet;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class RetailCCFGenarationConfiguration {
	
	private static final Logger logger = LogManager.getLogger(CCFGenarationConfiguration.class);
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public CCFGenerationDAO ccfGenerationdao;
	
	@Autowired
	InitialRetailCCFGenerationTasklet retailccfGeneration;
	
	

	public RetailCCFGenarationConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}
	
	@Bean(name="RetailCCFGenerationJob")
	public Job retailccfGenerationJob() {
		logger.info("...........Invoke Retail CCFGenerationJob JOB .........");
		return jobBuilderFactory.get(JobConstants.CCF_GENERATION_RETAIL_JOB_ID).listener(new BatchListener()).incrementer(new DateTimeIncrementer())
				.flow(initialStepRetailCCF()).next(finalStepRetailCCF()).end().build();
	}
	

	@Bean
	Step initialStepRetailCCF() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.CCF_RETAIL_INITIAL_TASKLET).tasklet(initialRetailCCFGenerationTask()).build();
	}
	
	@Bean
	Step finalStepRetailCCF() {
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.CCF_RETAIL_FINAL_TASKLET).tasklet(finalRetailCCFGenerationTask()).build();
	}
	
	@Bean
	protected Tasklet initialRetailCCFGenerationTask() {
		retailccfGeneration.setCCFGenerationDAOObj(ccfGenerationdao);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				String orderType =  context.getStepContext().getJobParameters().get(JobConstants.CCF_ORDER_TYPE).toString();
				retailccfGeneration.generateCcfFile(orderType);
				
				return RepeatStatus.FINISHED;
			}
		};
	
	}
	
	@Bean
	protected Tasklet finalRetailCCFGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				
				logger.debug("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}

}
