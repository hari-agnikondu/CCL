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

import com.incomm.scheduler.dao.B2BREPLCCFGenerationDAO;
import com.incomm.scheduler.jobsteps.BatchListener;
import com.incomm.scheduler.tasklet.InitialB2BREPLCCFGenerationTasklet;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class B2BREPLCCFGenerationConfiguration {
private static final Logger logger = LogManager.getLogger(CCFGenarationConfiguration.class);
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public B2BREPLCCFGenerationDAO b2BREPLCCFGenerationDAO;
	
	@Autowired
	InitialB2BREPLCCFGenerationTasklet b2BREPLCCFGeneration;
	
	

	public B2BREPLCCFGenerationConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}
	
	@Bean(name="B2BREPLCCFGenerationJob")
	public Job b2bREPLCCFGenerationJob() {
		logger.info("...........Invoke CCFGenerationJob JOB .........");
		return jobBuilderFactory.get(JobConstants.CCF_B2B_REPL_GENERATION_JOB_ID).listener(new BatchListener()).incrementer(new DateTimeIncrementer())
				.flow(initialStepB2BREPCCF()).next(finalStepB2BREPCCF()).end().build();
	}
	

	@Bean
	Step initialStepB2BREPCCF() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.CCF_B2B_REP_INITIAL_TASKLET).tasklet(initialB2BREPCCFGenerationTask()).build();
	}
	
	@Bean
	Step finalStepB2BREPCCF() {
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.CCF_B2B_REP_FINAL_TASKLET).tasklet(finalB2BREPCCFGenerationTask()).build();
	}
	
	@Bean
	protected Tasklet initialB2BREPCCFGenerationTask() {
		b2BREPLCCFGeneration.setB2BREPLCCFGenerationDAO(b2BREPLCCFGenerationDAO);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				String orderType =  context.getStepContext().getJobParameters().get(JobConstants.CCF_ORDER_TYPE).toString();
				b2BREPLCCFGeneration.generateCcfFile(orderType);
				
				return RepeatStatus.FINISHED;
			}
		};

		
		
	}
	
	@Bean
	protected Tasklet finalB2BREPCCFGenerationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				
				logger.debug("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}

}
