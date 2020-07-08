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
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.BindException;

import com.incomm.scheduler.config.partion.BulkTransactionBatchPartitioner;
import com.incomm.scheduler.config.partion.BulkTransactionBatchWritePartitioner;
import com.incomm.scheduler.config.partion.BulkTransactionFilePartitioner;
import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.BulkTransactionConfigDAO;
import com.incomm.scheduler.dao.BulkTransactionDAO;
import com.incomm.scheduler.job.listener.BulkTransactionJobListener;
import com.incomm.scheduler.job.listener.BulkTransactionListener;
import com.incomm.scheduler.job.listener.ChunkExecutionListener;
import com.incomm.scheduler.job.listener.JobCompletionNotificationListener;
import com.incomm.scheduler.job.listener.StepExecutionNotificationListener;
import com.incomm.scheduler.jobsteps.BulkTransactionFileProcessor;
import com.incomm.scheduler.jobsteps.BulkTransactionRequestWriter;
import com.incomm.scheduler.jobsteps.BulkTransactionResponseWriter;
import com.incomm.scheduler.model.BulkTransactionRequestFile;
import com.incomm.scheduler.model.BulkTransactionResponseFile;
import com.incomm.scheduler.service.IThreadPoolMonitorService;
import com.incomm.scheduler.tasklet.FinalBulkTransactionTasklet;
import com.incomm.scheduler.tasklet.InitialBulkTransactionTasklet;
import com.incomm.scheduler.utils.CustomThreadPool;
import com.incomm.scheduler.utils.FlatFileItemReaderBuilder;
import com.incomm.scheduler.utils.JobConstants;

@Configuration
@EnableBatchProcessing
public class BulkTransactionConfiguration {

	@Autowired
	public BulkTransactionDAO bulkTransactionDao;

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

	private static final Logger logger = LogManager.getLogger(BulkTransactionConfiguration.class);

	List<String> fileNamesList = null;

	@Autowired
	BulkTransactionFileProcessor bulkTransactionFileProcessor;

	@Autowired
	IThreadPoolMonitorService ithreadPoolMonitorService;

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private Map<String, String> fileList;

	@Value("${CHUNK_SIZE}")
	private Integer chunkSize;
	@Value("${GRID_SIZE}")
	private Integer gridSize;
	@Value("${RETRY_COUNT}")
	private Integer retryCount;
	@Value("${BACK_OFF_POLICY}")
	private Long backOFFPolicy;
	@Value("${BULK_TXN_FILE_PATH}")
	private String bulkpath;
	@Value("${BULK_TXN_FILE_PATH_RESP}")
	private String bulkpathResp;
	// @Value("${POOL_SIZE}")
	private Integer threadSize;
	@Value("${BULK_TXN_QUEUE_CAPACITY}")
	private Integer queueCapacity;
	@Value("${SPLIT_VALUE}")
	private Integer splitvalue;

	private Integer maxThreadSize;

	public BulkTransactionConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
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

	@Bean(name = "bulkTransactionJob")
	public Job bulkTransactionJob() {

		logger.info("...........Invoke Bulk Transaction JOB .........");
		return jobBuilderFactory.get(JobConstants.BULK_TRANSACTION_JOB_ID).listener(new BulkTransactionJobListener())
				.incrementer(new DateTimeIncrementer()).flow(initialStepBulkTransaction())
				.next(masterStepBulkTransaction()).next(step3()).next(masterStepBulkTransactionProcess())
				.next(masterStepBulkTransactionWrite()).on("*").to(finalStepBulkTransaction()).end().build();
	}

	@Bean
	Step initialStepBulkTransaction() {
		logger.info("Inside initialStep");
		return stepBuilderFactory.get(JobConstants.BULK_TRANSACTION_INITIAL_TASKLET)
				.tasklet(initialTaskBulkTransaction()).build();
	}

	@Bean
	public Step masterStepBulkTransaction() {
		logger.info(".........Invoke Master Step ........");
		return stepBuilderFactory.get(JobConstants.BULK_TRANSACTION_MASTER_STEP)// .allowStartIfComplete(true)
				.partitioner("slaveStepBulkTransaction", partitioner1()).listener(jobExecutionListener())
				.step(slaveStepBulkTransaction()).gridSize(gridSize).build();
	}
	
	@Bean
	@StepScope
	public Step slaveStepBulkTransaction() {
		logger.info("...........called slave .........");
		return stepBuilderFactory.get("slaveStepBulkTransaction")
				.<BulkTransactionRequestFile, BulkTransactionRequestFile>chunk(chunkSize)
				.writer(new BulkTransactionRequestWriter(bulkTransactionDao)).reader(ItemReader(null, null))
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
	@Qualifier("partitioner1")
	public Partitioner partitioner1() {
		logger.info("Inside Partitioner 1");
		BulkTransactionFilePartitioner partitioner = new BulkTransactionFilePartitioner(bulkTransactionDao);
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
		logger.info("Inside Partitioner1 end");
		return partitioner;
	}
	@Bean
	public Step masterStepBulkTransactionWrite() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.BULK_TRANSACTION_MASTER_STEP_WRITE) .allowStartIfComplete(true)
				.partitioner(slaveStepBulkTransactionWrite().getName(), partitioner3())
				.listener(jobExecutionListener())
				.step(slaveStepBulkTransactionWrite()).gridSize(gridSize).build();
	}
	
	@Bean
	public Step slaveStepBulkTransactionWrite() {
		logger.info("...........called slave .........");
		return stepBuilderFactory.get("slaveStepBulkTransactionWrite")
				.<BulkTransactionResponseFile, BulkTransactionResponseFile>chunk(chunkSize)
				.reader(JdbcPagingItemReaderForWrite(null)).writer(BlkTxnwriter(null))
				.taskExecutor(taskExecutor()).throttleLimit(threadSize).build();
	}
	
	@Bean
	@StepScope
	@Qualifier("partitioner3")
	public Partitioner partitioner3() {
		logger.info("In Partitioner 3" + fileList);
		BulkTransactionBatchWritePartitioner partitioner = new BulkTransactionBatchWritePartitioner();
		partitioner.setFileList(fileList);
		logger.info("In Partitioner end");
		return partitioner;
	}
	

	@Bean
	public Step masterStepBulkTransactionProcess() {
		logger.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.BULK_TRANSACTION_MASTER_STEP_PROCESS)// .allowStartIfComplete(true)
				.partitioner(step2().getName(), partitioner2())
				.listener(jobExecutionListener()).step(step2())
				.gridSize(1).build();
	}
	

	@Bean
	@Qualifier("step2")
	public Step step2() {
		logger.info("...........called slave .........");
		return stepBuilderFactory.get("step2")
				.<BulkTransactionRequestFile, BulkTransactionResponseFile>chunk(chunkSize)
				.reader(JdbcPagingItemReader(null, null, null)).processor(processorBulkTransaction())
				.writer(itemWriter()).listener(new BulkTransactionListener())
				.taskExecutor(taskExecutor()).throttleLimit(threadSize).build();
	}
	
	@Bean
	@StepScope
	@Qualifier("partitioner2")
	public Partitioner partitioner2() {
		logger.info("Inside Partitioner 2" + fileList);
		BulkTransactionBatchPartitioner partitioner = new BulkTransactionBatchPartitioner(bulkTransactionDao);
		partitioner.setFileList(fileList);
		logger.info("Inside Partitioner2 end");
		return partitioner;
	}

	@Bean
	public ExecutionContextPromotionListener promotionListener() {
		ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();
		listener.setKeys(new String[] { "fileList" });
		return listener;
	}

	@SuppressWarnings("unchecked")
	@Bean
	@Qualifier("step3")
	public Step step3() {
		return stepBuilderFactory.get("step3").tasklet((contribution, chunkContext) -> {
			fileList = (Map<String, String>) chunkContext.getStepContext().getJobExecutionContext().get("fileList");
			logger.info("In step 3:  items Files:" + fileList);
			return RepeatStatus.FINISHED;
		})

				.build();
	}

	@Bean
	Step finalStepBulkTransaction() {
		logger.debug("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.BULK_TRANSACTION_FINAL_TASKLET).tasklet(finalTaskBulkTransaction())
				.build();
	}

	@Bean
	public Tasklet initialTaskBulkTransaction() {
		
		logger.info(CCLPConstants.ENTER);
		InitialBulkTransactionTasklet it = new InitialBulkTransactionTasklet(bulkTransactionDao,
				bulkTransactionConfigDao);
		// Set Properties
		Map<String, String> threadProps = new HashMap<>();
		List<Map<String, Object>> threadPropsList = it.getThreadProperties();
		for (Map<String, Object> keyVal : threadPropsList) {
			threadProps.put((String) keyVal.get("attribute_name"), (String) keyVal.get("attribute_value"));
		}
		threadSize = Integer.parseInt(threadProps.get("threadPoolSize"));
		chunkSize = Integer.parseInt(threadProps.get("chunkSize"));
		maxThreadSize = Integer.parseInt(threadProps.get("maxThreadPoolSize"));
		fileNamesList = new ArrayList<>();
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				if (!fileNamesList.isEmpty()) {
					fileNamesList.removeAll(fileNamesList);
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
				logger.debug("Directory Path from Job Parameters" + directoryPath);
				fileNamesList = it.chkDuplicateFile(directoryPath, JobConstants.BULK_TRANSACTION_MAST_TABLE,
						fileNamesList, context);
				directoryPath = directoryPath + "temp/";
				return RepeatStatus.FINISHED;
			}
		};

	}

	@Bean
	public Tasklet finalTaskBulkTransaction() {
		FinalBulkTransactionTasklet ft = new FinalBulkTransactionTasklet(bulkTransactionDao);
		logger.info(CCLPConstants.ENTER);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				if (fileNamesList != null && !fileNamesList.isEmpty()) {
					ft.chkDuplicateFile(directoryPath, bulkpath, JobConstants.BULK_TRANSACTION_MAST_TABLE,
							fileNamesList);
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
	public BulkTransactionFileProcessor processorBulkTransaction() {
		return bulkTransactionFileProcessor;
	}

	@Bean
	@StepScope
	@Qualifier("ItemReader")
	public FlatFileItemReader<BulkTransactionRequestFile> ItemReader(
			@Value("#{stepExecutionContext['BatchId']}") String batchid,
			@Value("#{stepExecutionContext['fileName']}") Resource in) {

		logger.info(CCLPConstants.ENTER);
		logger.info("File in reader"+in.getFilename());
		return new FlatFileItemReaderBuilder<BulkTransactionRequestFile>().name("file-reader").resource(in)
				.lineMapper(new DefaultLineMapper<BulkTransactionRequestFile>() {
					{
						setLineTokenizer(new DelimitedLineTokenizer() {
							{
								setNames(new String[] { "Source_Reference_Number", "SP_Number", "Action", "Amount",
										"Mdm_id", "Store_id", "Terminal_id" });
							}
						});
						setFieldSetMapper(new BeanWrapperFieldSetMapper<BulkTransactionRequestFile>() {
							{
								setTargetType(BulkTransactionRequestFile.class);
							}

							@Override
							public BulkTransactionRequestFile mapFieldSet(FieldSet fs) throws BindException {
								BulkTransactionRequestFile tmp = super.mapFieldSet(fs);
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
	public FlatFileItemWriter<BulkTransactionResponseFile> BlkTxnwriter(
			@Value("#{stepExecutionContext['file']}") String fileName) {
		
		logger.info(CCLPConstants.ENTER);
		FlatFileItemWriter<BulkTransactionResponseFile> writer = new FlatFileItemWriter<>();
		logger.info("fileName In writer: " + fileName);
		String[] respFileName = fileName.split(".csv");
		Resource outputResource = new FileSystemResource(bulkpathResp + respFileName[0] + "_RESP.csv");
		writer.setResource(outputResource);
		writer.setHeaderCallback(new FlatFileHeaderCallback() {
			@Override
		    public void writeHeader(Writer writer) throws IOException {
		        writer.write("#BatchId,Source Reference Number,SP Number,Transaction Description,Card Status,Amount,Transaction Date,Transaction Time,Available Balance,MdmId,Store id,Terminal id,Response Code,Response Message");
		    }
		});
		writer.setAppendAllowed(true);
		writer.setLineAggregator(new DelimitedLineAggregator<BulkTransactionResponseFile>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<BulkTransactionResponseFile>() {
					{
						setNames(new String[] { "batchId", "sourceReferenceNumber", "spNumber",  "transactionDesc" ,"cardStatus", "amount", "transactionDate", "transactionTime",
								"availableBalance",  "mdmId", "storeId", "terminalId", "responseCode", "responseMessage"
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
	public JdbcPagingItemReader<BulkTransactionResponseFile> JdbcPagingItemReaderForWrite(
			@Value("#{stepExecutionContext['file']}") String fileName) {

		logger.info(CCLPConstants.ENTER);
		Map<String, Order> sortKeys = new HashMap<>(1);
		JdbcPagingItemReader<BulkTransactionResponseFile> reader = new JdbcPagingItemReader<>();
		try {
			sortKeys.put("record_num", Order.ASCENDING);
			Map<String, Object> parameterValues = new HashMap<>();
			parameterValues.put("fileName", fileName);

			OraclePagingQueryProvider QueryProvider = new OraclePagingQueryProvider();

			QueryProvider.setSelectClause("record_num,batch_id,SOURCE_REFERENCE_NUMBER,clp_transactional.fn_dmaps_main(SP_NUMBER_ENCR) AS SP_NUMBER,AMOUNT,available_balance,mdm_id,STORE_ID,TERMINAL_ID,response_code,response_message,TRANSACTION_DATE,TRANSACTION_TIME,status_desc as cardStatus,TRANSACTION_DESC");
			QueryProvider.setFromClause("BULK_REQ_RESP_LOGS bq left outer join card_status c on c.status_code = bq.card_status");
			QueryProvider.setWhereClause(" file_Name=:fileName");
			QueryProvider.setSortKeys(sortKeys);

			reader.setQueryProvider(QueryProvider);

			reader.setDataSource(dataSource);

			reader.setPageSize(splitvalue);

			reader.setRowMapper(new BeanPropertyRowMapper<>(BulkTransactionResponseFile.class));
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
	@StepScope
	public JdbcPagingItemReader<BulkTransactionRequestFile> JdbcPagingItemReader(
			@Value("#{stepExecutionContext['BatchId']}") String batchId,
			@Value("#{stepExecutionContext['fromRow']}") String fromRow,
			@Value("#{stepExecutionContext['toRow']}") String toRow) {

		logger.info(CCLPConstants.ENTER);
		Map<String, Order> sortKeys = new HashMap<>(1);
		JdbcPagingItemReader<BulkTransactionRequestFile> reader = new JdbcPagingItemReader<>();
		try {
			sortKeys.put("RECORD_NUM", Order.ASCENDING);
			Map<String, Object> parameterValues = new HashMap<>();
			parameterValues.put("batchid", batchId);
			parameterValues.put("fromRow", fromRow);
			parameterValues.put("toRow", toRow);
			logger.info("ExecutionContext" + "From Row" + fromRow + "toRow" + toRow + "Batchid" + batchId);

			OraclePagingQueryProvider QueryProvider = new OraclePagingQueryProvider();

			QueryProvider.setSelectClause("SELECT SOURCE_REFERENCE_NUMBER,RECORD_NUM,clp_transactional.fn_dmaps_main(SP_NUMBER_ENCR) AS SP_NUMBER,ACTION,AMOUNT,mdm_id,STORE_ID,TERMINAL_ID,BATCH_ID,FILE_NAME");
			QueryProvider.setFromClause("BULK_REQ_RESP_LOGS WHERE BATCH_ID =:batchid AND RESPONSE_CODE IS NULL");
			QueryProvider.setSortKeys(sortKeys);

			reader.setQueryProvider(QueryProvider);

			reader.setDataSource(dataSource);

			reader.setPageSize(splitvalue);

			reader.setRowMapper(new BeanPropertyRowMapper<>(BulkTransactionRequestFile.class));
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
	public CompositeItemWriter<BulkTransactionResponseFile> itemWriter() {
		CompositeItemWriter<BulkTransactionResponseFile> compositeItemWriter = new CompositeItemWriter<>();
		compositeItemWriter.setDelegates(Arrays.asList(new BulkTransactionResponseWriter(bulkTransactionDao)));
		return compositeItemWriter;
	}
	@Bean
	@StepScope
	public ThreadPoolTaskExecutor BlkTxntaskExecutor() {

		CustomThreadPool executor = new CustomThreadPool();
		executor.setCorePoolSize(threadSize);
		executor.setMaxPoolSize(maxThreadSize);
		executor.setQueueCapacity(queueCapacity);
		executor.afterPropertiesSet();
		executor.setThreadNamePrefix("Bulk-Process-Thread-Pool");
		executor.initialize();
		return executor;
	}

}
