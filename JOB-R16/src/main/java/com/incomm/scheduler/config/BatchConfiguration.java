package com.incomm.scheduler.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.AbstractTaskletStepBuilder;
import org.springframework.batch.core.step.builder.SimpleStepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;

import com.incomm.scheduler.config.partion.FilePartitioner;
import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CnShipmentFileDao;
import com.incomm.scheduler.job.listener.CnShipmentFileWriteListeners;
import com.incomm.scheduler.jobsteps.BatchFileProcessor;
import com.incomm.scheduler.jobsteps.BatchFileWriter;
import com.incomm.scheduler.jobsteps.BatchListener;
import com.incomm.scheduler.model.CnShipmentFile;
import com.incomm.scheduler.tasklet.FinalTasklet;
import com.incomm.scheduler.tasklet.InitialTasklet;
import com.incomm.scheduler.utils.JobConstants;


@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public CnShipmentFileDao cnShipmentFileDao;

	@Autowired
	ResourcePatternResolver resoursePatternResolver;

	@Autowired
	ResourceLoader rl;

	private String directoryPath = null;

	private static final Logger logger = LogManager.getLogger(BatchConfiguration.class);

	List<String> fileNamesList=null;

	@Autowired
	BatchFileProcessor batchFileProcessor;

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Value("${CHUNK_SIZE}")
	private Integer chunkSize;
	@Value("${GRID_SIZE}")
	private Integer gridSize;
	@Value("${RETRY_COUNT}")
	private Integer retryCount;
	@Value("${BACK_OFF_POLICY}")
	private Long backOFFPolicy;
	
	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}
	

	@Bean(name = "retailShipmentJob")
	public Job retailShipmentJob() {
		
		logger.debug("...........Invoke Retail Shipment JOB .........");
		return jobBuilderFactory.get(JobConstants.RETAIL_SHIPMENT_JOB_ID).listener(new BatchListener()).incrementer(new DateTimeIncrementer())
				.flow(initialStepCNFile()).next(masterStepCNFile()).on("*").to(finalStepCNFile()).end().build();
	}



	@Bean
	Step initialStepCNFile() {
		logger.debug("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_INITIAL_TASKLET).tasklet(initialTask()).build();
	}
	@Bean
	public Step masterStepCNFile() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_MASTER_STEP).allowStartIfComplete(true)
				.partitioner(slaveStepCNFile().getName(), cnFileRangePartioner())
				.step(slaveStepCNFile()).gridSize(gridSize)
				.taskExecutor(taskExecutor())
				.build();
	}

	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleTaskExecutor.setConcurrencyLimit(1);
		return simpleTaskExecutor;
	}


	@Bean(name = "slaveStepCNFile")
	public Step slaveStepCNFile() {
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(backOFFPolicy);
		logger.info("...........called slave .........");
		AbstractTaskletStepBuilder<SimpleStepBuilder< CnShipmentFile, CnShipmentFile >> sb = 
				stepBuilderFactory.get("slaveStepCNFile").allowStartIfComplete(true)
				.< CnShipmentFile, CnShipmentFile >chunk(chunkSize)
				.reader(reader(null))
				.processor(processor(null))
				.writer(new BatchFileWriter(cnShipmentFileDao)).listener(new CnShipmentFileWriteListeners(cnShipmentFileDao))
				.faultTolerant().retryLimit(retryCount).retry(Exception.class).backOffPolicy(backOffPolicy);
		return sb.build();
	}



	@Bean
	Step finalStepCNFile() {
		logger.debug("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_FINAL_TASKLET).tasklet(finalTask()).build();
	}

	@Bean
	protected Tasklet initialTask() {
		InitialTasklet it = new InitialTasklet(cnShipmentFileDao);
		fileNamesList=new ArrayList<>();
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				
				if(!fileNamesList.isEmpty()) {
					fileNamesList.removeAll(fileNamesList);
				}
				
				directoryPath = context.getStepContext().getJobParameters().get(JobConstants.DIRECTORY_PATH).toString();
				if(context.getStepContext().getJobParameters().containsKey(JobConstants.CN_FILE_NAMES)){
					String fileNames =  context.getStepContext().getJobParameters().get(JobConstants.CN_FILE_NAMES).toString();
					if(fileNames!=null && !fileNames.isEmpty()){
						fileNamesList=Arrays.asList(fileNames.split(JobConstants.CN_FILE_SEPERATOR));
					}
				}
				else{
					File file = new File(directoryPath);
					File[] files = file.listFiles();
					if(files!=null && files.length>0){
						for (int i = 0; i < files.length; i++) {
							if(files[i].getName().endsWith(".csv") && !fileNamesList.contains(files[i].getName())){
								fileNamesList.add(files[i].getName());
							}
						}	
					}
					else{
						logger.info("No files in this directory:"+directoryPath);
					}
				}
				if(fileNamesList.isEmpty()){
					return RepeatStatus.FINISHED;
				}
				logger.debug("Directory Path from Job Parameters" + directoryPath);
				fileNamesList=it.chkDuplicateFile(directoryPath, JobConstants.RETAIL_SHIPEMENT_MAST_TABLE,fileNamesList);
				
				return RepeatStatus.FINISHED;
			}
		};

	}


	@Bean
	protected Tasklet finalTask() {
		FinalTasklet ft = new FinalTasklet(cnShipmentFileDao);
		InitialTasklet it = new InitialTasklet(cnShipmentFileDao);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				ft.makeProcCall();
				if(fileNamesList!=null && !fileNamesList.isEmpty()){
					ft.chkDuplicateFile(directoryPath, JobConstants.RETAIL_SHIPEMENT_MAST_TABLE,fileNamesList);
					it.chkDuplicateFileStatus(directoryPath, fileNamesList);
				}
				logger.debug("Final Step Started");
				it.truncateFromTemp(JobConstants.RETAIL_SHIPEMENT_TEMP_TABLE);
				return RepeatStatus.FINISHED;
			}
		};

	}


	@Bean
	@StepScope
	public FilePartitioner cnFileRangePartioner() {
		return new FilePartitioner(fileNamesList);
	}

	@Bean
	@StepScope
	public BatchFileProcessor processor(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		batchFileProcessor.setFileName(fileNames);
		return batchFileProcessor;
	}

	@Bean
	public FlatFileItemReader<CnShipmentFile> readerFiles() {
		FlatFileItemReader<CnShipmentFile> reader = new FlatFileItemReader<>();
		reader.setLineMapper(new DefaultLineMapper<CnShipmentFile>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "magic_Number", "status", "carrier", "date", "tracking_Number",
								"merchant_ID", "merchant_Name", "storelocationID", "batch_Number", "case_Number",
								"pallet_Number", "serial_Number", "ship_To", "street_Address1", "street_Address2",
								"city", "state", "zip", "dCMS_ID", "prod_ID", "order_ID", "parent_Serial_Number" });

					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<CnShipmentFile>() {
					{
						setTargetType(CnShipmentFile.class);
					}
				});
			}
		});
		return reader;
	}


	@Bean
	@StepScope
	public ItemReader<CnShipmentFile> reader(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		
		logger.info(CCLPConstants.ENTER);
		MultiResourceItemReader<CnShipmentFile> reader = new MultiResourceItemReader<>();
		Resource []resources =null;
		try {
			if(!fileNames.isEmpty()){
				resources = new Resource[fileNames.size()];
				for (int i=0; i< fileNames.size(); i++)
				{
					File f = new File(directoryPath+fileNames.get(i));
					if(f.exists()) {
						resources[i] = new FileSystemResource(directoryPath+ fileNames.get(i));
					}
				}
				reader.setResources(resources);
				reader.setDelegate(readerFiles());
			}

		} catch (Exception e) {
			logger.error("Exception in reader:"+e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return reader;
	}
	
	
}
