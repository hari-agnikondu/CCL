package com.incomm.scheduler.config;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;

import com.incomm.scheduler.config.partion.CurrencyRateUploadBatchPartitioner;
import com.incomm.scheduler.config.partion.CurrencyRateUploadBatchWritePartitioner;
import com.incomm.scheduler.config.partion.CurrencyRateUploadFilePartitioner;
import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.BulkTransactionConfigDAO;
import com.incomm.scheduler.dao.CurrencyRateUploadDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.job.listener.ChunkExecutionListener;
import com.incomm.scheduler.job.listener.CurrencyRateUploadJobListener;
import com.incomm.scheduler.job.listener.CurrencyRateUploadListener;
import com.incomm.scheduler.job.listener.JobCompletionNotificationListener;
import com.incomm.scheduler.job.listener.StepExecutionNotificationListener;
import com.incomm.scheduler.jobsteps.CurrencyRateFileProcessor;
import com.incomm.scheduler.jobsteps.CurrencyRateUploadRequestWriter;
import com.incomm.scheduler.jobsteps.CurrencyRateUploadResponseWriter;
import com.incomm.scheduler.jobsteps.CustomBatchIdItemReader;
import com.incomm.scheduler.model.CurrencyRateRequestFile;
import com.incomm.scheduler.model.CurrencyRateResponseFile;
import com.incomm.scheduler.service.IThreadPoolMonitorService;
import com.incomm.scheduler.tasklet.FinalCurrencyRateUploadTasklet;
import com.incomm.scheduler.tasklet.InitialCurrencyRateUploadTasklet;
import com.incomm.scheduler.utils.FlatFileItemReaderBuilder;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class CurrencyRateUploadConfiguration {

	@Autowired
	public CurrencyRateUploadDAO currencyRateUploadDao;

	@Autowired
	public BulkTransactionConfigDAO bulkTransactionConfigDao;

	@Autowired
	ResourcePatternResolver resoursePatternResolver;

	@Autowired
	ResourceLoader rl;

	JobExecution lexcecution;

	private String directoryPath = null;

	@Autowired
	@Qualifier("transactionalDs")
	DataSource dataSource;

	private static final Logger logger = LogManager.getLogger(CurrencyRateUploadConfiguration.class);

	List<String> fileNamesList = null;

	@Autowired
	IThreadPoolMonitorService ithreadPoolMonitorService;

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private Map<String, String> fileList;
	private List<String> batchList;

	@Value("${CHUNK_SIZE}")
	private Integer chunkSize;
	@Value("${GRID_SIZE}")
	private Integer gridSize;
	@Value("${RETRY_COUNT}")
	private Integer retryCount;
	@Value("${BACK_OFF_POLICY}")
	private Long backOFFPolicy;
	@Value("${CURRENCY_RATE_FILE_PATH}")
	private String currencyRateFilePath;
	@Value("${CURRENCY_RATE_FILE_PATH_RESP}")
	private String currencyRateFilePathResp;
	private Integer threadSize;
	@Value("${BULK_TXN_QUEUE_CAPACITY}")
	private Integer queueCapacity;
	@Value("${SPLIT_VALUE}")
	private Integer splitvalue;


	public CurrencyRateUploadConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;

	}

	@Bean
	public JobCompletionNotificationListener jobExecutionListener() {
		return new JobCompletionNotificationListener();
	}

	@Bean
	public StepExecutionNotificationListener stepExecutionListener() {
		return new StepExecutionNotificationListener();
	}

	@Bean
	public ChunkExecutionListener chunkListener() {
		return new ChunkExecutionListener();
	}

	@Bean(name = "currencyRateUploadJob")
	public Job currencyRateUploadJob() {

		logger.info("...........Invoke Currency Rate Upload JOB .........");
		return jobBuilderFactory.get(JobConstants.CURRENCY_RATE_JOB_ID).listener(new CurrencyRateUploadJobListener())
				.incrementer(new DateTimeIncrementer()).flow(initialStepCurrencyRateUpload())
				.next(masterStepCurrencyRateUpload()).next(promotionStep()).next(masterStepCurrencyRateUploadProcess())
				.next(masterStepCurrencyRateUploadWrite()).on("*").to(finalStepCurrencyRateUpload()).end().build();
	}

	@Bean
	Step initialStepCurrencyRateUpload() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.CURRENCY_RATE_INITIAL_TASKLET)
				.tasklet(initialTaskCurrencyRateUpload()).build();
	}

	@Bean
	public Step masterStepCurrencyRateUpload() {
		logger.info("...........Invoke masterStepCurrencyRateUpload .........");
		return stepBuilderFactory.get(JobConstants.CURRENCY_RATE_MASTER_STEP)
				.partitioner("slaveStepCurrencyRateUpload", currencyRateUploadPartitioner1()).listener(jobExecutionListener())
				.step(slaveStepCurrencyRateUpload()).gridSize(gridSize).build();
	}
	
	@Bean
	@StepScope
	public Step slaveStepCurrencyRateUpload() {
		logger.info("...........called slave step currency rate upload.........");
		return stepBuilderFactory.get("slaveStepCurrencyRateUpload")
				.<CurrencyRateRequestFile, CurrencyRateRequestFile>chunk(chunkSize)
				.writer(new CurrencyRateUploadRequestWriter(currencyRateUploadDao)).reader(itemReader(null, null))
				.listener(promotionListener()).taskExecutor(taskExecutor()).throttleLimit(threadSize).build();
	}
	
	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleTaskExecutor.setConcurrencyLimit(threadSize);
		return simpleTaskExecutor;
	}

	
	@Bean
	@StepScope
	@Qualifier("currencyRateUploadPartitioner1")
	public Partitioner currencyRateUploadPartitioner1() {
		logger.info("Inside CurrencyRateUpload Partitioner 1");
		CurrencyRateUploadFilePartitioner partitioner = new CurrencyRateUploadFilePartitioner(currencyRateUploadDao);
		Resource[] resources = null;
		try {
			if (!fileNamesList.isEmpty()) {
				resources = new Resource[fileNamesList.size()];
				for (int i = 0; i < fileNamesList.size(); i++) {
					
					File f = new File(directoryPath + fileNamesList.get(i));
					if (f.exists()) {
						resources[i] = new FileSystemResource(directoryPath + fileNamesList.get(i));
					}
				}
			}
			partitioner.setResources(resources);
		} catch (Exception e) {
			logger.error("Exception in reader:" + e.getMessage());
		}
		logger.info("Inside currency rate file partitioner end");
		return partitioner;
	}
	@Bean
	public Step masterStepCurrencyRateUploadWrite() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.CURRENCY_RATE_MASTER_STEP_WRITE) .allowStartIfComplete(true)
				.partitioner(slaveStepCurrencyRateUploadWrite().getName(), currencyRateUploadpartitioner3())
				.listener(jobExecutionListener())
				.step(slaveStepCurrencyRateUploadWrite()).gridSize(gridSize).build();
	}
	
	@Bean
	public Step slaveStepCurrencyRateUploadWrite() {
		logger.info("...........called slave .........");
		return stepBuilderFactory.get("slaveStepCurrencyRateUploadWrite")
				.<CurrencyRateResponseFile, CurrencyRateResponseFile>chunk(chunkSize)
				.reader(jdbcPagingItemReaderForWrite(null)).writer(currencyRateUploadwriter(null))
				.taskExecutor(taskExecutor()).throttleLimit(threadSize).build();
	}
	
	@Bean
	@StepScope
	@Qualifier("currencyRateUploadpartitioner3")
	public Partitioner currencyRateUploadpartitioner3() {
		logger.info("In CurrencyRateUpload Partitioner 3" + fileList);
		CurrencyRateUploadBatchWritePartitioner partitioner = new CurrencyRateUploadBatchWritePartitioner();
		partitioner.setFileList(fileList);
		logger.info("In Partitioner end");
		return partitioner;
	}
	

	@Bean
	public Step masterStepCurrencyRateUploadProcess() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.CURRENCY_RATE_MASTER_STEP_PROCESS)// .allowStartIfComplete(true)
				.partitioner(slaveStepCurrencyRateUploadProcess().getName(), currRateUpdPartitioner2())
				.listener(jobExecutionListener()).step(slaveStepCurrencyRateUploadProcess())
				.gridSize(1).build();
	}
	

	@Bean
	@Qualifier("slaveStepCurrencyRateUploadProcess")
	public Step slaveStepCurrencyRateUploadProcess() {
		logger.info("...........called slave .........");
		return stepBuilderFactory.get("slaveStepCurrencyRateUploadProcess")
				.<String, String>chunk(chunkSize)
				.reader(customBatchIdItemReader()).processor(processorCurrencyRateUpload())
				.writer(currencyItemWriter()).listener(new CurrencyRateUploadListener())
				.taskExecutor(taskExecutor()).throttleLimit(threadSize).build();
	}

	@Bean
	@StepScope
	public CurrencyRateFileProcessor processorCurrencyRateUpload() {
		return new CurrencyRateFileProcessor();
	}
	@Bean
	@StepScope
	@Qualifier("CurrencyRateUploadpartitioner2")
	public Partitioner currRateUpdPartitioner2() {
		logger.info("In CurrencyRateUpload  Partitioner 2" + fileList);
		CurrencyRateUploadBatchPartitioner partitioner = new CurrencyRateUploadBatchPartitioner(currencyRateUploadDao);
		partitioner.setFileList(fileList);
		logger.info("In Partitioner end");
		return partitioner;
	}

	@Bean
	@Qualifier("CurrencyRateUploadpromotionListener")
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys(new String[] { "fileList","batchList" });
		return listener;
	}

	@SuppressWarnings("unchecked")
	@Bean
	@Qualifier("promotionStep")
	public Step promotionStep() {
		return stepBuilderFactory.get("promotionStep").tasklet((contribution, chunkContext) -> {
			fileList = (Map<String, String>) chunkContext.getStepContext().getJobExecutionContext().get("fileList");
			batchList=(List<String>) chunkContext.getStepContext().getJobExecutionContext().get("batchList");
			logger.info("In step 3:  items Files:" + fileList+"batchList"+batchList);
			return RepeatStatus.FINISHED;
		})

				.build();
	}

	@Bean
	Step finalStepCurrencyRateUpload() {
		logger.debug("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.CURRENCY_RATE_FINAL_TASKLET).tasklet(finalTaskCurrencyRateUpload())
				.build();
	}

	@Bean
	public Tasklet initialTaskCurrencyRateUpload() {
		
		logger.info(CCLPConstants.ENTER);
		InitialCurrencyRateUploadTasklet it = new InitialCurrencyRateUploadTasklet(currencyRateUploadDao,
				bulkTransactionConfigDao);
		// Set Properties
		Map<String, String> threadProps = new HashMap<>();
		List<Map<String, Object>> threadPropsList = it.getThreadProperties();
		for (Map<String, Object> keyVal : threadPropsList) {
			threadProps.put((String) keyVal.get("attribute_name"), (String) keyVal.get("attribute_value"));
		}
		threadSize = Integer.parseInt(threadProps.get("threadPoolSize"));
		chunkSize = Integer.parseInt(threadProps.get("chunkSize"));
		fileNamesList = new ArrayList<>();
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				if (!CollectionUtils.isEmpty(fileNamesList)) {
					logger.info("Files already exist in list "+fileNamesList);
					fileNamesList.clear();
				}

				directoryPath = context.getStepContext().getJobParameters().get(JobConstants.DIRECTORY_PATH).toString();
				if (context.getStepContext().getJobParameters().containsKey(JobConstants.CN_FILE_NAMES)) {
					String fileNames = context.getStepContext().getJobParameters().get(JobConstants.CN_FILE_NAMES)
							.toString();
					if (fileNames != null && !fileNames.isEmpty()) {
						fileNamesList = Arrays.asList(fileNames.split(JobConstants.CN_FILE_SEPERATOR));
					}
				} else {
					File file = new File(directoryPath);
					File[] files = file.listFiles();
					if (files != null && files.length > 0) {
						for (int i = 0; i < files.length; i++) {
							if (files[i].getName().endsWith(".csv") && !fileNamesList.contains(files[i].getName())) {
								fileNamesList.add(files[i].getName());
							}
						}
					} else {
						logger.info("No files in this directory:" + directoryPath);
					}
				}
				if (fileNamesList.isEmpty()) {
					return RepeatStatus.FINISHED;
				}
				logger.info("Directory Path from Job Parameters" + directoryPath);
			
			
				fileNamesList = it.chkDuplicateFile(directoryPath, fileNamesList);
				directoryPath = directoryPath + "temp/";
				
				
				if(fileNamesList.isEmpty()) {
					throw new ServiceException("File List empty");
				}
				return RepeatStatus.FINISHED;
			}
		};

	}

	@Bean
	public Tasklet finalTaskCurrencyRateUpload() {
		
		logger.info(CCLPConstants.ENTER);
		FinalCurrencyRateUploadTasklet ft = new FinalCurrencyRateUploadTasklet(currencyRateUploadDao);

		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				if (fileNamesList != null && !fileNamesList.isEmpty()) {
					ft.chkDuplicateFile(directoryPath, currencyRateFilePath, fileNamesList);
					ft.updateFileStatus(fileList);
				}
				ithreadPoolMonitorService.monitorThreadPool();
				logger.info(CCLPConstants.EXIT);
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	@StepScope
	public CustomBatchIdItemReader customBatchIdItemReader() {
		CustomBatchIdItemReader customBatchIdItemReader = new CustomBatchIdItemReader();
		customBatchIdItemReader.setBatchIdList(batchList);
		return customBatchIdItemReader;
	}

	@Bean
	@StepScope
	@Qualifier("ItemReader")
	public FlatFileItemReader<CurrencyRateRequestFile> itemReader(
			@Value("#{stepExecutionContext['BatchId']}") String batchid,
			@Value("#{stepExecutionContext['fileName']}") Resource in) {

		logger.info(CCLPConstants.ENTER);
		logger.info("File in reader"+in.getFilename());
		return new FlatFileItemReaderBuilder<CurrencyRateRequestFile>().name("file-reader").resource(in)
				.lineMapper(new DefaultLineMapper<CurrencyRateRequestFile>() {
					{
						setLineTokenizer(new DelimitedLineTokenizer() {
							{
								setNames(new String[] { "MDM_ID", "Transaction_Currency", "Issuing_Currency", "Conversion_Rate",
										"Effective_Date_TIME", "Action" });
							}
						});
						setFieldSetMapper(new BeanWrapperFieldSetMapper<CurrencyRateRequestFile>() {
							{
								setTargetType(CurrencyRateRequestFile.class);
							}

							@Override
							public CurrencyRateRequestFile mapFieldSet(FieldSet fs) throws BindException {
								CurrencyRateRequestFile tmp = super.mapFieldSet(fs);
								// your custom code here
								tmp.setBatchId(batchid);
								tmp.setFileName(in.getFilename());
								return tmp;
							}

						});
					}
				})

				.build();
	}

	@Bean
	@StepScope
	public FlatFileItemWriter<CurrencyRateResponseFile> currencyRateUploadwriter(
			@Value("#{stepExecutionContext['file']}") String fileName) {
		
		logger.info(CCLPConstants.ENTER);
		FlatFileItemWriter<CurrencyRateResponseFile> writer = new FlatFileItemWriter<>();
		logger.info("fileName In writer: " + fileName);
		String[] respFileName = fileName.split(".csv");
		Resource outputResource = new FileSystemResource(currencyRateFilePathResp + respFileName[0] + "_RESP.csv");
		writer.setResource(outputResource);
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
		    public void writeHeader(Writer writer) throws IOException {
		        writer.write("#MDMID,Transaction Currency,Purse Currency,Exchange Rate,Effective Date and Time,Action,Response,Action Date Time");
		    }
		});
		writer.setAppendAllowed(true);
		writer.setLineAggregator(new DelimitedLineAggregator<CurrencyRateResponseFile>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<CurrencyRateResponseFile>() {
					{
						setNames(new String[] { "mdmId", "transactionCurrency", "purseCurrency",  "exchangeRate" ,"effectiveDateTime", "action", "errorMessage", "ActionDateTime"
								
								});

					}
				});
			}
		});
		logger.info(CCLPConstants.EXIT);
		return writer;
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<CurrencyRateResponseFile> jdbcPagingItemReaderForWrite(
			@Value("#{stepExecutionContext['file']}") String fileName) {

		logger.info(CCLPConstants.ENTER);
		Map<String, Order> sortKeys = new HashMap<>(1);
		JdbcPagingItemReader<CurrencyRateResponseFile> reader = new JdbcPagingItemReader<>();
		try {
			sortKeys.put("RECORD_NUM", Order.ASCENDING);
			Map<String, Object> parameterValues = new HashMap<>();
			parameterValues.put("fileName", fileName);

			OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();

			queryProvider.setSelectClause("RECORD_NUM,MDM_ID,TRANSACTION_CURRENCY,ISSUING_CURRENCY as purseCurrency,CONVERSION_RATE as exchangeRate,EFFECTIVE_DATE as effectiveDateTime,ERROR_MESSAGE,ACTION,LAST_UPD_DATE as actionDateTime");
			queryProvider.setFromClause("CURRENCY_CONVERSION_STAGE");
			queryProvider.setWhereClause(" file_Name=:fileName");
			queryProvider.setSortKeys(sortKeys);

			reader.setQueryProvider(queryProvider);

			reader.setDataSource(dataSource);

			reader.setPageSize(splitvalue);

			reader.setRowMapper(new BeanPropertyRowMapper<>(CurrencyRateResponseFile.class));
			reader.setParameterValues(parameterValues);
			reader.setSaveState(false);

			reader.afterPropertiesSet();

		} catch (Exception e) {
			logger.error("Exception in jdbc paging reader " + e.getMessage());
		}
		logger.info(CCLPConstants.EXIT);
		return reader;
	}

	
	@Bean
	public CompositeItemWriter<String> currencyItemWriter() {
		CompositeItemWriter<String> compositeItemWriter = new CompositeItemWriter<>();
		compositeItemWriter.setDelegates(Arrays.asList(new CurrencyRateUploadResponseWriter()));
		return compositeItemWriter;
	}


}

