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

import com.incomm.scheduler.dao.AutoReplenishInventoryDAO;
import com.incomm.scheduler.tasklet.InitialAutoReplenishmentInventory;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class AutoReplenishmentInventoryConfiguration {
	

	private static final Logger logger = LogManager.getLogger(CardNumberInventoryGenerationConfiguration.class);

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	InitialAutoReplenishmentInventory autoReplInventory;
	
	@Autowired
	AutoReplenishInventoryDAO autoReplenishInventoryDAO;
	
	public AutoReplenishmentInventoryConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}
	
	@Bean(name="AutoReplenishmentInventoryGenJob")
	public Job autoReplenishmentInventoryGenJob() {
		
		logger.info("................ invoke AutoReplenishmentInventoryJob ..................");
		return jobBuilderFactory.get(JobConstants.AUTO_REPL_INVENTORY_ID).incrementer(new DateTimeIncrementer())
				.flow(initialStepAutoReplenishmentInventory()).next(finalStepAutoReplenishmentInventory()).end().build();
	}

	@Bean
	Step initialStepAutoReplenishmentInventory() {
		
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.AUTO_REPLENISH_INVENTORY_INITIAL_TASKLET).tasklet(initialAutoReplenishmentInventoryTask()).build();
	}


	@Bean
	Step finalStepAutoReplenishmentInventory() {
		
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.AUTO_REPLENISH_INVENTORY_FINAL_TASKLET).tasklet(finalAutoReplenishmentInventoryTask()).build();
	}

	@Bean
	protected Tasklet initialAutoReplenishmentInventoryTask() {
		
		autoReplInventory.setAutoReplenishInvDaoObj(autoReplenishInventoryDAO); 
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				
				autoReplInventory.intiateBatchAutoReplenishInventory();
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	@Bean
	protected Tasklet finalAutoReplenishmentInventoryTask() {
		
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				
				return RepeatStatus.FINISHED;
			}
		};
	}
}
