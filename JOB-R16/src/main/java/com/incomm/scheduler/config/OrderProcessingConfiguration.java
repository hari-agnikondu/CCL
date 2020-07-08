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

import com.incomm.scheduler.dao.OrderProcessingDAO;
import com.incomm.scheduler.jobsteps.BatchListener;
import com.incomm.scheduler.tasklet.FinalOrderTasklet;
import com.incomm.scheduler.tasklet.InitialOrderProcessTasklet;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class OrderProcessingConfiguration {
	
	private static final Logger logger = LogManager.getLogger(OrderProcessingConfiguration.class);

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	public OrderProcessingDAO orderprocessingdao;

	public OrderProcessingConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}
	
	@Bean(name = "OrderJob")
	public Job orderJob() {
		logger.info("...........Invoke Order JOB .........");
		return jobBuilderFactory.get(JobConstants.ORDER_PROCESS_JOB_ID).listener(new BatchListener()).incrementer(new DateTimeIncrementer())
				.flow(initialStep()).next(finalStep()).end().build();
	}
	

	@Bean
	Step initialStep() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_INITIAL_TASKLET).tasklet(initialOrderTask()).build();
	}
	
	@Bean
	Step finalStep() {
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_FINAL_TASKLET).tasklet(finalOrderTask()).build();
	}
	
	@Bean
	protected Tasklet initialOrderTask() {
		InitialOrderProcessTasklet it = new InitialOrderProcessTasklet(orderprocessingdao);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				it.getOrderID();
				
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	@Bean
	protected Tasklet finalOrderTask() {
		FinalOrderTasklet ft = new FinalOrderTasklet(orderprocessingdao);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				ft.makeProcCall();
				logger.info("Final Step Finished");
				return RepeatStatus.FINISHED;
			}
		};

	}


}
