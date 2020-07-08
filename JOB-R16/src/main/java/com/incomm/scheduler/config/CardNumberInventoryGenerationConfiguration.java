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

import com.incomm.scheduler.dao.CardNumberInventoryDAO;
import com.incomm.scheduler.tasklet.InitialCardNumberInventoryGeneration;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class CardNumberInventoryGenerationConfiguration {
	
	private static final Logger logger = LogManager.getLogger(CardNumberInventoryGenerationConfiguration.class);

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	InitialCardNumberInventoryGeneration cardNumInvtryGen;
	
	@Autowired
	CardNumberInventoryDAO cardInvtryDao;
	
	public CardNumberInventoryGenerationConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}
	
	@Bean(name="CardNumberInventoryGenJob")
	public Job cardNumberInventoryGenJob() {
		
		logger.info("................ invoke CardNumberInventoryGenerationJob ..................");
		return jobBuilderFactory.get(JobConstants.CARD_NUM_INVENTORY_GEN_ID).incrementer(new DateTimeIncrementer())
				.flow(initialStepCardInventory()).next(finalStepCardInventory()).end().build();
	}

	@Bean
	Step initialStepCardInventory() {
		
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.CARD_NUM_INVENTORY_INITIAL_TASKLET).tasklet(initialCardNumInventoryGenTask()).build();
	}


	@Bean
	Step finalStepCardInventory() {
		
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.CARD_NUM_INVENTORY_FINAL_TASKLET).tasklet(finalCardNumInventoryGenTask()).build();
	}

	@Bean
	protected Tasklet initialCardNumInventoryGenTask() {
		
		cardNumInvtryGen.setCardNumInventoryGenDaoObj(cardInvtryDao); 
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				
				cardNumInvtryGen.intiateBatchCardNumGeneration();
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	@Bean
	protected Tasklet finalCardNumInventoryGenTask() {
		
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				
				return RepeatStatus.FINISHED;
			}
		};
	}
	
}
