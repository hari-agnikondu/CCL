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
import com.incomm.scheduler.service.CcfGenerationService;
import com.incomm.scheduler.tasklet.InitialCCFFileDirDelete;
import com.incomm.scheduler.utils.JobConstants;


@Configuration
@EnableBatchProcessing
public class CCFFileDirDeleteConfiguration {

	private static final Logger logger = LogManager.getLogger(CCFFileDirDeleteConfiguration.class);
	
	@Autowired
	InitialCCFFileDirDelete initialCCFFileDirDelete;
	
	 @Autowired
	 CCFGenerationDAO ccfGenerationDao;
	 
	 @Autowired
	 CcfGenerationService ccfGenerationService;
	

	private JobBuilderFactory jobBuilderFactory;
	private StepBuilderFactory stepBuilderFactory;

	public CCFFileDirDeleteConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean(name = "CCFFileDirDelJob")
	public Job ccfFileDirDeleteJob() {

		logger.info("................ invoke CCFFileDirDeleteJob ..................");
		return jobBuilderFactory.get(JobConstants.CCF_FILE_DIR_DEL_JOB_ID).incrementer(new DateTimeIncrementer())
				.flow(initialStepDelDir()).next(finalStepDelDir()).end().build();

	}
	
	@Bean
	Step initialStepDelDir() {

		logger.info("Inside initial Step");
		return stepBuilderFactory.get(JobConstants.CCF_FILE_DIR_DEL_INTL_TASKLET).tasklet(intialStepDelDirTask())
				.build();
	}

	@Bean
	Step finalStepDelDir() {

		logger.info("Inside final Step");
		return stepBuilderFactory.get(JobConstants.CCF_FILE_DIR_DEL_FINAL_TASKLET).tasklet(finalStepDelDirTask())
				.build();
	}

	@Bean
	protected Tasklet intialStepDelDirTask() {
		initialCCFFileDirDelete.setCcfGenerationDao(ccfGenerationDao);
		initialCCFFileDirDelete.setCcfGenerationService(ccfGenerationService);
		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				initialCCFFileDirDelete.deleteDir();
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	protected Tasklet finalStepDelDirTask() {

		return new Tasklet() {
			
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
				
				return RepeatStatus.FINISHED;
			}
		};
	}

}
