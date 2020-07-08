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
import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.dao.ShipmentFileDao;
import com.incomm.scheduler.job.listener.ShipmentFileWriteListeners;
import com.incomm.scheduler.jobsteps.BatchListener;
import com.incomm.scheduler.jobsteps.ShipmentFileProcessor;
import com.incomm.scheduler.jobsteps.ShipmentFileWriter;
import com.incomm.scheduler.model.ShipmentFile;
import com.incomm.scheduler.tasklet.FinalShipmentFileTasklet;
import com.incomm.scheduler.tasklet.InitialShipmentFileTasklet;
import com.incomm.scheduler.utils.JobConstants;


@Configuration
@EnableBatchProcessing
public class ShipmentFileConfiguration {

	@Autowired
	public ShipmentFileDao shipmentFileDao;

	@Autowired
	ResourcePatternResolver resoursePatternResolver;

	@Autowired
	ResourceLoader rl;

	private String directoryPath = null;

	private static final Logger logger = LogManager.getLogger(ShipmentFileConfiguration.class);

	List<String> fileNamesList=null;

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	SchedulerJobDAO schedulerJobDAO;
	@Value("${CHUNK_SIZE}")
	private Integer chunkSize;
	@Value("${GRID_SIZE}")
	private Integer gridSize;
	@Value("${RETRY_COUNT}")
	private Integer retryCount;
	@Value("${BACK_OFF_POLICY}")
	private Long backOFFPolicy;

	public ShipmentFileConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}

	@Bean(name = "ShipmentFileJob")
	public Job shipmentFileJob() {
		logger.info("...........Invoke Shipment File JOB .........");
		return jobBuilderFactory.get(JobConstants.SHIPMENT_FILE_JOB_ID).listener(new BatchListener()).incrementer(new DateTimeIncrementer())
				.flow(initialStepShipmentFile())
				.next(masterStepShipmentFile())
				.on("*")
				.to(finalStepShipmentFile())
				.end()
				.build()
				;
	}
	
	@Bean
	Step initialStepShipmentFile() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_INITIAL_TASKLET).tasklet(initialShipmentFileTask()).build();
	}
	
	@Bean
	protected Tasklet initialShipmentFileTask() {
		InitialShipmentFileTasklet it = new InitialShipmentFileTasklet(shipmentFileDao);
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
					for (int i = 0; i < files.length; i++) {
						if(files[i].getName().endsWith(".csv")){
							fileNamesList.add(files[i].getName());
						}
					}
				}
				if(fileNamesList.isEmpty()){
					
					return RepeatStatus.FINISHED;
				}
				logger.debug("Directory Path from Job Parameters" + directoryPath);
				fileNamesList=it.chkFileNameFormat(directoryPath,fileNamesList);
				fileNamesList=it.chkDuplicateFile(directoryPath, JobConstants.SHIPMENT_FILE_MAST_TABLE,fileNamesList);
			
				return RepeatStatus.FINISHED;
			}
		};

	}
	
	@Bean
	public Step masterStepShipmentFile() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_MASTER_STEP)
				.partitioner(slaveStepShipmentFile().getName(), shipmentFilePartioner())
				.step(slaveStepShipmentFile()).gridSize(gridSize)
				.taskExecutor(taskExecutor())
				.build();
	}
	
	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleTaskExecutor.setConcurrencyLimit(1);
		return simpleTaskExecutor;
	}

	
	@Bean(name = "slaveStepShipmentFile")
	public Step slaveStepShipmentFile() {
		logger.info("...........called slave .........");
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(backOFFPolicy);
		AbstractTaskletStepBuilder<SimpleStepBuilder< ShipmentFile, ShipmentFile >> sb = 
				stepBuilderFactory.get("slaveStep")
				.< ShipmentFile, ShipmentFile >chunk(chunkSize)
				.reader(shipmentFilereader(null))
				.processor(shipmentProcessor(null))
				.writer(new ShipmentFileWriter(shipmentFileDao)).listener(new ShipmentFileWriteListeners(shipmentFileDao))
				.faultTolerant().retryLimit(retryCount).retry(Exception.class).backOffPolicy(backOffPolicy);
		return sb.build();
	}
	
	
	
	@Bean
	Step finalStepShipmentFile() {
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_FINAL_TASKLET).tasklet(finalShipmentTask()).build();
	}
	
	@Bean
	protected Tasklet finalShipmentTask() {
		FinalShipmentFileTasklet ft = new FinalShipmentFileTasklet(shipmentFileDao);
		InitialShipmentFileTasklet it = new InitialShipmentFileTasklet(shipmentFileDao);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				ft.shipmentFileProcCall();
				if(fileNamesList!=null && !fileNamesList.isEmpty()){
					ft.chkDuplicateFile(directoryPath, JobConstants.SHIPMENT_FILE_MAST_TABLE,fileNamesList);
					it.chkDuplicateFileStatus(directoryPath, fileNamesList);
				}
				logger.info("Final Step Started");
				it.truncateFromTemp(JobConstants.SHIPMENT_FILE_TEMP_TABLE,fileNamesList);
				return RepeatStatus.FINISHED;
			}
		};

	}
	
	
	@Bean
	@StepScope
	public FilePartitioner shipmentFilePartioner() {
		return new FilePartitioner(fileNamesList);
	}

	
	@Bean
	@StepScope
	public ShipmentFileProcessor shipmentProcessor(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		ShipmentFileProcessor shipmentFileProcessor = new ShipmentFileProcessor(schedulerJobDAO);
		shipmentFileProcessor.setFileName(fileNames);
		return shipmentFileProcessor;
	}
	
	@Bean
	public FlatFileItemReader<ShipmentFile> shipmentReaderFiles() {
		
		logger.info(CCLPConstants.ENTER);
		FlatFileItemReader<ShipmentFile> reader = new FlatFileItemReader<>();
		reader.setLineMapper(new DefaultLineMapper<ShipmentFile>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "customer_Desc", "source_One_Batch", "parent_Order_Id","child_Order_Id","file_Date",
								"serialNumber","cards","package_Id","card_Type","contact_Name","ship_to","address_1",
								"address_2","city","state","zip","tracking_Number","ship_Date","shipment_Id","ship_Method"});

					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<ShipmentFile>() {
					{
						setTargetType(ShipmentFile.class);
					}
				});
			}
		});
		logger.info(CCLPConstants.EXIT);
		return reader;
	}


	@Bean
	@StepScope
	public ItemReader<ShipmentFile> shipmentFilereader(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		
		logger.info(CCLPConstants.ENTER);
		MultiResourceItemReader<ShipmentFile> reader = new MultiResourceItemReader<>();
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
				reader.setDelegate(shipmentReaderFiles());
			}

		} catch (Exception e) {
			logger.error("Exception in reader:"+e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return reader;
	} 
}
