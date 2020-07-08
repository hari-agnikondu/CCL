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
import com.incomm.scheduler.dao.SerialNumberFileDao;
import com.incomm.scheduler.job.listener.SerialNumberFileWriteListeners;
import com.incomm.scheduler.jobsteps.BatchListener;
import com.incomm.scheduler.jobsteps.SerialNumberFileProcessor;
import com.incomm.scheduler.jobsteps.SerialNumberFileWriter;
import com.incomm.scheduler.model.SerialNumberFile;
import com.incomm.scheduler.tasklet.FinalSerialNumberFileTasklet;
import com.incomm.scheduler.tasklet.InitialSerialNumberFileTasklet;
import com.incomm.scheduler.utils.JobConstants;


@Configuration
@EnableBatchProcessing
public class SerialNumberFileConfiguration {

	@Autowired
	public SerialNumberFileDao serialNumberFileDao;

	@Autowired
	ResourcePatternResolver resoursePatternResolver;

	@Autowired
	ResourceLoader rl;

	private String directoryPath = null;

	private static final Logger logger = LogManager.getLogger(SerialNumberFileConfiguration.class);

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

	public SerialNumberFileConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}

	@Bean(name = "SerialNumberFileJob")
	public Job serialNumberFileJob() {
		logger.info("...........Invoke Serial Number File JOB .........");
		return jobBuilderFactory.get(JobConstants.SERIALNUMBER_FILE_JOB_ID).listener(new BatchListener()).incrementer(new DateTimeIncrementer())
				.flow(initialStepSerialNumberFile())
				.next(masterStepSerialNumberFile())
				.on("*")
				.to(finalStepSerialNumberFile())
				.end()
				.build()
				;
	}
	
	@Bean
	Step initialStepSerialNumberFile() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_INITIAL_TASKLET).tasklet(initialSerialNumberFileTask()).build();
	}
	
	@Bean
	protected Tasklet initialSerialNumberFileTask() {
		InitialSerialNumberFileTasklet it = new InitialSerialNumberFileTasklet(serialNumberFileDao);
		fileNamesList=new ArrayList<>();
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
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
				fileNamesList=it.chkDuplicateFile(directoryPath, JobConstants.SERIALNUMBER_FILE_MAST_TABLE,fileNamesList);
				
				return RepeatStatus.FINISHED;
			}
		};

	}
	
	@Bean
	public Step masterStepSerialNumberFile() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_MASTER_STEP)
				.partitioner(slaveStepSerialNumberFile().getName(), serialNumberFilePartioner())
				.step(slaveStepSerialNumberFile()).gridSize(10)
				.taskExecutor(taskExecutor())
				.build();
	}
	
	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleTaskExecutor.setConcurrencyLimit(1);
		return simpleTaskExecutor;
	}
	
	@Bean(name = "slaveStepSerialNumberFile")
	public Step slaveStepSerialNumberFile() {
		logger.info("...........called slave .........");
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(backOFFPolicy);
		AbstractTaskletStepBuilder<SimpleStepBuilder< SerialNumberFile, SerialNumberFile >> sb = 
				stepBuilderFactory.get("slaveStep")
				.< SerialNumberFile, SerialNumberFile >chunk(chunkSize)
				.reader(serialNumberFilereader(null))
				.processor(serialNumberProcessor(null))
				.writer(new SerialNumberFileWriter(serialNumberFileDao)).listener(new SerialNumberFileWriteListeners(serialNumberFileDao))
				.faultTolerant().retryLimit(retryCount).retry(Exception.class).backOffPolicy(backOffPolicy);

		return sb.build();
	}
	
	
	
	@Bean
	Step finalStepSerialNumberFile() {
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_FINAL_TASKLET).tasklet(finalSerialNumberTask()).build();
	}
	
	@Bean
	protected Tasklet finalSerialNumberTask() {
		
		logger.info(CCLPConstants.ENTER);
		FinalSerialNumberFileTasklet ft = new FinalSerialNumberFileTasklet(serialNumberFileDao);
		InitialSerialNumberFileTasklet it = new InitialSerialNumberFileTasklet(serialNumberFileDao);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				ft.serialNumberFileProcCall();
				if(fileNamesList!=null && !fileNamesList.isEmpty()){
					ft.chkDuplicateFile(directoryPath, JobConstants.SERIALNUMBER_FILE_MAST_TABLE,fileNamesList);
					it.chkDuplicateFileStatus(directoryPath, fileNamesList);
				}
				logger.info("Final Step Started");
				it.truncateFromTemp(JobConstants.SERIALNUMBER_FILE_TEMP_TABLE);
				return RepeatStatus.FINISHED;
			}
		};

	}
	
	
	@Bean
	@StepScope
	public FilePartitioner serialNumberFilePartioner() {
		return new FilePartitioner(fileNamesList);
	}

	
	@Bean("serialNumberFileProcessor")
	@StepScope
	public SerialNumberFileProcessor serialNumberProcessor(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		SerialNumberFileProcessor serialNumberFileProcessor = new SerialNumberFileProcessor(schedulerJobDAO);
		serialNumberFileProcessor.setFileName(fileNames);
		return serialNumberFileProcessor;
	}
	
	@Bean
	public FlatFileItemReader<SerialNumberFile> serialNumberReaderFiles() {
		FlatFileItemReader<SerialNumberFile> reader = new FlatFileItemReader<>();
		reader.setLineMapper(new DefaultLineMapper<SerialNumberFile>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "product_Id", "serial_Number", "van16", });

					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<SerialNumberFile>() {
					{
						setTargetType(SerialNumberFile.class);
					}
				});
			}
		});
		return reader;
	}


	@Bean
	@StepScope
	public ItemReader<SerialNumberFile> serialNumberFilereader(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		
		logger.info(CCLPConstants.ENTER);
		MultiResourceItemReader<SerialNumberFile> reader = new MultiResourceItemReader<>();
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
				reader.setDelegate(serialNumberReaderFiles());
			}

		} catch (Exception e) {
			logger.error("Exception in reader:"+e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return reader;
	} 
}
