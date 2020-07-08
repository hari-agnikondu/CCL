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

import com.incomm.scheduler.service.PassivePeriodCalculationService;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class PassivePeriodCalculationConfiguration {

	public static final Logger logger = LogManager.getLogger(PassivePeriodCalculationConfiguration.class);
	
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	public PassivePeriodCalculationConfiguration(JobBuilderFactory jobBuilderFactory,StepBuilderFactory stepBuilderFactory){
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Autowired
	PassivePeriodCalculationService passivePeriodCalculationService;
	
	@Bean(name="PassivePeriodCalculationJob")
	public Job passivePeriodCalculationJob() {
		logger.info("...........Invoke passivePeriodCalculation JOB .........");
		return jobBuilderFactory.get(JobConstants.PASSIVE_PRIOD_CALC_JOB).incrementer(new DateTimeIncrementer())
				.flow(initialStepPassivePeriodCalculation()).end().build();
	}

	private Step initialStepPassivePeriodCalculation() {
		return stepBuilderFactory.get(JobConstants.PASSIVE_PERIOD_CALCULATION_INITIAL_TASKLET).tasklet(initialPassivePeriodCalculationTask()).build();
	}
	
	@Bean
	Step finalStepPassivePeriodCalculation() {
		return stepBuilderFactory.get(JobConstants.PASSIVE_PERIOD_CALCULATION_FINAL_TASKLET).tasklet(finalPassivePeriodCalculationTask()).build();
	}

	private Tasklet initialPassivePeriodCalculationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				passivePeriodCalculationService.passivePeriodCalculation();
				return RepeatStatus.FINISHED;
			}
		};
	}

	private Tasklet finalPassivePeriodCalculationTask() {
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				logger.info("Final Step Finished1");
				return RepeatStatus.FINISHED;
			}
		};

	}
}
