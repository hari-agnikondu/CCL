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
import com.incomm.scheduler.dao.ResponseFileDao;
import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.job.listener.ResponseFileWriteListeners;
import com.incomm.scheduler.jobsteps.ResponseFileProcessor;
import com.incomm.scheduler.jobsteps.ResponseFileWriter;
import com.incomm.scheduler.model.ResponseFile;
import com.incomm.scheduler.tasklet.FinalResponseFileTasklet;
import com.incomm.scheduler.tasklet.InitialResponseFileTasklet;
import com.incomm.scheduler.utils.JobConstants;


@Configuration
@EnableBatchProcessing
public class ResponseFileConfiguration {

	@Autowired
	public ResponseFileDao responseFileDao;

	@Autowired
	ResourcePatternResolver resoursePatternResolver;

	@Autowired
	ResourceLoader rl;
	
	@Autowired
	SchedulerJobDAO schedulerJobDAO;

	private String directoryPath = null;

	private static final Logger logger = LogManager.getLogger(ResponseFileConfiguration.class);

	List<String> fileNamesList=null;

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

	public ResponseFileConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}

	@Bean(name = "responseFileJob")
	public Job responseFileJob() {
		logger.info("...........Invoke Response File JOB .........");
		return jobBuilderFactory.get(JobConstants.RESPONSE_FILE_JOB_ID).incrementer(new DateTimeIncrementer())
				.flow(initialStepResponseFile())
				.next(masterStepResponseFile())
				.on("*")
				.to(finalStepResponseFile())
				.end()
				.build()
				;
	}
	
	@Bean
	Step initialStepResponseFile() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_INITIAL_TASKLET).tasklet(initialResponseFileTask()).build();
	}
	
	@Bean
	protected Tasklet initialResponseFileTask() {
		logger.info(CCLPConstants.ENTER);
		InitialResponseFileTasklet it = new InitialResponseFileTasklet(responseFileDao);
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
				logger.info("Directory Path from Job Parameters" + directoryPath);
				fileNamesList=it.chkFileNameFormat(directoryPath,fileNamesList);
				fileNamesList=it.chkDuplicateFile(directoryPath, JobConstants.RESPONSE_FILE_MAST_TABLE,fileNamesList);
				logger.info(CCLPConstants.EXIT);
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	@Bean
	public Step masterStepResponseFile() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_MASTER_STEP)
				.partitioner(slaveStepResponseFile().getName(), responseFilePartioner())
				.step(slaveStepResponseFile()).gridSize(gridSize)
				.taskExecutor(taskExecutor())
				.build();
	}
	
	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleTaskExecutor.setConcurrencyLimit(1);
		return simpleTaskExecutor;
	}

	@Bean(name = "slaveStepResponseFile")
	public Step slaveStepResponseFile() {
		logger.info("...........called slave .........");
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(backOFFPolicy);
		AbstractTaskletStepBuilder<SimpleStepBuilder< ResponseFile, ResponseFile >> sb = 
				stepBuilderFactory.get("slaveStep")
				.< ResponseFile, ResponseFile >chunk(chunkSize)
				.reader(responseFilereader(null))
				.processor(responseFileProcessor(null))
				.writer(new ResponseFileWriter(responseFileDao)).listener(new ResponseFileWriteListeners(responseFileDao))
				.faultTolerant().retryLimit(retryCount).retry(Exception.class).backOffPolicy(backOffPolicy);

		return sb.build();
	}
	
	@Bean
	Step finalStepResponseFile() {
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_FINAL_TASKLET).tasklet(finalResponseFileTask()).build();
	}
	
	@Bean
	protected Tasklet finalResponseFileTask() {
		FinalResponseFileTasklet ft = new FinalResponseFileTasklet(responseFileDao);
		InitialResponseFileTasklet it = new InitialResponseFileTasklet(responseFileDao);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				ft.responseFileProcCall();
				if(fileNamesList!=null && !fileNamesList.isEmpty()){
					ft.chkDuplicateFile(directoryPath, JobConstants.RESPONSE_FILE_MAST_TABLE,fileNamesList);
					it.chkDuplicateFileStatus(directoryPath, fileNamesList);
				}
				logger.info("Final Step Started");
				it.truncateFromTemp(JobConstants.RESPONSE_FILE_TEMP_TABLE,fileNamesList);
				return RepeatStatus.FINISHED;
			}
		};
	}
	
	
	@Bean
	@StepScope
	public FilePartitioner responseFilePartioner() {
		return new FilePartitioner(fileNamesList);
	}
	
	@Bean
	@StepScope
	public ResponseFileProcessor responseFileProcessor(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		ResponseFileProcessor processor = new ResponseFileProcessor(schedulerJobDAO);
		processor.setFileName(fileNames);
		return processor;
	}
	
	@Bean
	public FlatFileItemReader<ResponseFile> responseFilereaderFiles() {
		FlatFileItemReader<ResponseFile> reader = new FlatFileItemReader<>();
		reader.setLineMapper(new DefaultLineMapper<ResponseFile>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] {"magic_Number", "status", "carrier", "date", "tracking_Number",
								"merchant_ID", "merchant_Name", "storelocationID", "batch_Number", "case_Number",
								"pallet_Number", "serial_Number", "ship_To", "street_Address1", "street_Address2",
								"city", "state", "zip", "dCMS_ID", "prod_ID", "order_ID", "parent_Serial_Number"});

					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<ResponseFile>() {
					{
						setTargetType(ResponseFile.class);
					}
				});
			}
		});
		return reader;
	}


	@Bean
	@StepScope
	public ItemReader<ResponseFile> responseFilereader(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		
		logger.info(CCLPConstants.ENTER);
		MultiResourceItemReader<ResponseFile> reader = new MultiResourceItemReader<>();
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
				reader.setDelegate(responseFilereaderFiles());
			}

		} catch (Exception e) {
			logger.error("Exception in reader:"+e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return reader;
	}
}
