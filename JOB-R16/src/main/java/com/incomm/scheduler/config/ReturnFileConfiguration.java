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
import com.incomm.scheduler.dao.ReturnFileDao;
import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.job.listener.ReturnFileWriteListeners;
import com.incomm.scheduler.jobsteps.ReturnFileProcessor;
import com.incomm.scheduler.jobsteps.ReturnFileWriter;
import com.incomm.scheduler.model.ReturnFile;
import com.incomm.scheduler.tasklet.FinalReturnFileTasklet;
import com.incomm.scheduler.tasklet.InitialReturnFileTasklet;
import com.incomm.scheduler.utils.JobConstants;


@Configuration
@EnableBatchProcessing
public class ReturnFileConfiguration {

	@Autowired
	public ReturnFileDao returnFileDao;

	@Autowired
	ResourcePatternResolver resoursePatternResolver;

	@Autowired
	ResourceLoader rl;
	
	private String directoryPath = null;

	private static final Logger logger = LogManager.getLogger(ReturnFileConfiguration.class);

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

	public ReturnFileConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}

	@Bean(name = "returnFileJob")
	public Job returnFileJob() {
		logger.info("...........Invoke Return File JOB .........");
		return jobBuilderFactory.get(JobConstants.RETURN_FILE_JOB_ID).incrementer(new DateTimeIncrementer())
				.flow(initialStepReturnFile())
				.next(masterStepReturnFile())
				.on("*")
				.to(finalStepReturnFile())
				.end()
				.build()
				;
	}
	
	@Bean
	Step initialStepReturnFile() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_INITIAL_TASKLET).tasklet(initialReturnFileTask()).build();
	}
	
	@Bean
	protected Tasklet initialReturnFileTask() {
		
		logger.info(CCLPConstants.ENTER);
		InitialReturnFileTasklet it = new InitialReturnFileTasklet(returnFileDao);
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
				fileNamesList=it.chkFileNameFormat(directoryPath,fileNamesList);
				fileNamesList=it.chkDuplicateFile(directoryPath, JobConstants.RETURN_FILE_MAST_TABLE,fileNamesList);
				
				logger.info(CCLPConstants.EXIT);
				return RepeatStatus.FINISHED;
			}
		};

	}
	
	@Bean
	public Step masterStepReturnFile() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_MASTER_STEP)
				.partitioner(slaveStepReturnFile().getName(), returnFilePartioner())
				.step(slaveStepReturnFile()).gridSize(gridSize)
				.taskExecutor(taskExecutor())
				.build();
	}
	
	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleTaskExecutor.setConcurrencyLimit(1);
		return simpleTaskExecutor;
	}

	
	@Bean(name = "slaveStepReturnFile")
	public Step slaveStepReturnFile() {
		logger.info("...........called slave .........");
		ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
		backOffPolicy.setInitialInterval(backOFFPolicy);
		AbstractTaskletStepBuilder<SimpleStepBuilder< ReturnFile, ReturnFile >> sb = 
				stepBuilderFactory.get("slaveStep")
				.< ReturnFile, ReturnFile >chunk(chunkSize)
				.reader(returnFilereader(null))
				.processor(returnFileProcessor(null))
				.writer(new ReturnFileWriter(returnFileDao)).listener(new ReturnFileWriteListeners(returnFileDao))
				.faultTolerant().retryLimit(retryCount).retry(Exception.class).backOffPolicy(backOffPolicy);
		return sb.build();
	}
	
	
	
	@Bean
	Step finalStepReturnFile() {
		logger.info("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.RETAIL_SHIP_FINAL_TASKLET).tasklet(finalReturnFileTask()).build();
	}
	
	@Bean
	protected Tasklet finalReturnFileTask() {
		FinalReturnFileTasklet ft = new FinalReturnFileTasklet(returnFileDao);
		InitialReturnFileTasklet it = new InitialReturnFileTasklet(returnFileDao);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				ft.returnFileProcCall();
				if(fileNamesList!=null && !fileNamesList.isEmpty()){
					ft.chkDuplicateFile(directoryPath, JobConstants.RETURN_FILE_MAST_TABLE,fileNamesList);
					it.chkDuplicateFileStatus(directoryPath, fileNamesList);
				}
				logger.info("Final Step Started");
				it.truncateFromTemp(JobConstants.RETURN_FILE_TEMP_TABLE,fileNamesList);
				return RepeatStatus.FINISHED;
			}
		};

	}
	
	
	@Bean
	@StepScope
	public FilePartitioner returnFilePartioner() {
		return new FilePartitioner(fileNamesList);
	}

	
	@Bean("returnFileProcessor")
	@StepScope
	public ReturnFileProcessor returnFileProcessor(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		ReturnFileProcessor returnFileProcessor = new ReturnFileProcessor(schedulerJobDAO);
		returnFileProcessor.setFileName(fileNames);
		return returnFileProcessor;
	}
	
	@Bean
	public FlatFileItemReader<ReturnFile> returnFilereaderFiles() {
		FlatFileItemReader<ReturnFile> reader = new FlatFileItemReader<>();
		reader.setLineMapper(new DefaultLineMapper<ReturnFile>() {
			{
				setLineTokenizer(new DelimitedLineTokenizer() {
					{
						setNames(new String[] { "customer_Desc", "ship_Suffix_num", "parent_Order_Id", "child_Order_Id", "serial_Number",
								"reject_Code", "reject_Reason", "file_Date", "card_Type", "client_Order_Id"});

					}
				});
				setFieldSetMapper(new BeanWrapperFieldSetMapper<ReturnFile>() {
					{
						setTargetType(ReturnFile.class);
					}
				});
			}
		});
		return reader;
	}


	@Bean
	@StepScope
	public ItemReader<ReturnFile> returnFilereader(@Value("#{stepExecutionContext['"+ JobConstants.FILE_LIST + "']}")List<String> fileNames) {
		
		logger.info(CCLPConstants.ENTER);
		MultiResourceItemReader<ReturnFile> reader = new MultiResourceItemReader<>();
		Resource []resources =null;
		try {
			if(!fileNames.isEmpty()){
				resources = new Resource[fileNames.size()];
				for (int i=0; i< fileNames.size(); i++)
				{
					File f = new File(directoryPath+fileNames.get(i));
					if(f.exists()) {
						logger.debug("Return file found: {}",directoryPath+fileNames.get(i));
						resources[i] = new FileSystemResource(directoryPath+ fileNames.get(i));
					}
				}
				reader.setResources(resources);
				reader.setDelegate(returnFilereaderFiles());
			}

		} catch (Exception e) {
			logger.error("Exception in reader:"+e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return reader;
	}
}
