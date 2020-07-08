package com.incomm.scheduler.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.OraclePagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.util.CollectionUtils;

import com.incomm.scheduler.config.partion.BatchLoadAccountPursePartitioner;
import com.incomm.scheduler.config.partion.BatchLoadAccountPurseRequestPartitioner;
import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.BatchLoadAccountPurseDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.external.adapter.SpilServiceAdapter;
import com.incomm.scheduler.external.resource.LoadAccountPurseResponse;
import com.incomm.scheduler.job.listener.JobCompletionNotificationListener;
import com.incomm.scheduler.jobsteps.BatchLoadAccountPurseRequestWriter;
import com.incomm.scheduler.jobsteps.BatchLoadAccountPurseResponseWriter;
import com.incomm.scheduler.mapper.BatchLoadAccountPurseMapper;
import com.incomm.scheduler.model.BatchLoadAccountPurse;
import com.incomm.scheduler.model.BatchLoadAccountPurseLog;
import com.incomm.scheduler.service.AccountPurseJobService;
import com.incomm.scheduler.tasklet.UpdateLoadAccountPurseBatchStatus;
import com.incomm.scheduler.utils.JobConstants;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableBatchProcessing
public class BatchLoadAccountPurseJobConfiguration {

	private static final String DEFAULT_CARD_STATUS = "1";

	// @formatter:off
	@Autowired @Qualifier("transactionalDs") private DataSource dataSource;
	@Autowired 					private BatchLoadAccountPurseDAO batchUpdateRequestDAO;
	@Autowired 					private SpilServiceAdapter spilServiceAdapter;
	@Value("${SPLIT_VALUE}") 	private Integer splitvalue;
	@Value("${CHUNK_SIZE}") 	private Integer chunkSize;
	@Value("${GRID_SIZE}") 		private Integer gridSize;
	@Value("${THREAD_POOL_SIZE:50}") 		private Integer theadPoolSize;
	
	@Autowired private AccountPurseJobService accountPurseJobService;
	
	
	// @formatter:on

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	private List<BatchLoadAccountPurse> allRequests;

	public BatchLoadAccountPurseJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean(name = "loadAccountPurseBatchJob")
	public Job loadAccountPurseBatchJob() {

		log.info("...........Invoke Load Account Purse Batch Job .........");
		
		return jobBuilderFactory.get(JobConstants.BATCH_LOAD_ACCOUNT_PURSE_JOB_ID)
			.listener(jobCompletionNotificationListener())
			.incrementer(new DateTimeIncrementer())
			.flow(loadPurseLoadBatchRequests())
			.next(accountPurseLoadRequestCreater())
			.next(accountPurseLoadRequestProcessor())
			.on("*")
			.to(finalStepLoadAccountPurseBatch())
			.end()
			.build();
	}

	@Bean
	public Step accountPurseLoadRequestCreater() {
		log.info(".........Invoke accountPurseLoadRequestCreater Step ........");
		return stepBuilderFactory.get(JobConstants.BATCH_LOAD_ACCOUNT_PURSE_REQUEST_CREATER_STEP)
			.partitioner("accountPurseLoadRequestCreater", loadPurseBatchPartitioner())
			.listener(jobCompletionNotificationListener())
			.step(slaveStepCreateAccountPurseLoadRequest())
			.gridSize(this.gridSize)
			.build();
	}

	@Bean
	@StepScope
	public Partitioner loadPurseBatchPartitioner() {
		return new BatchLoadAccountPursePartitioner(allRequests);
	}

	@Bean
	@StepScope
	public Step slaveStepCreateAccountPurseLoadRequest() {
		log.info("...........called slave .........");
		return stepBuilderFactory.get(JobConstants.BATCH_LOAD_ACCOUNT_PURSE_REQUEST_SLAVE_STEP)
			.<String, BatchLoadAccountPurseLog>chunk(chunkSize)
			.reader(jdbcPagingAccountNumberReader(null, null))
			.writer(new BatchLoadAccountPurseRequestWriter(batchUpdateRequestDAO))
			.processor(batchLogRecordCreater())
			// .listener(promotionListener())
			.taskExecutor(taskExecutor())
			.throttleLimit(theadPoolSize)
			.build();
	}

	@Bean
	public Step accountPurseLoadRequestProcessor() {
		log.info("...........Invoke Master Step .........");
		return stepBuilderFactory.get(JobConstants.BATCH_LOAD_ACCOUNT_PURSE_REQUEST_PROCESSOR)// .allowStartIfComplete(true)
			.partitioner(requestProcessorStep().getName(), purseLoadRequestPartitioner())
			.listener(jobCompletionNotificationListener())
			.step(requestProcessorStep())
			.gridSize(1)
			.build();
	}

	@Bean
	@Qualifier("apiCallerStep")
	public Step requestProcessorStep() {
		log.info("...........called slave .........");
		return stepBuilderFactory.get("requestProcessorStep")
			.<BatchLoadAccountPurseLog, BatchLoadAccountPurseLog>chunk(5)
			.reader(jdbcPagingPurseLoadRequestReader(null))
			.processor(processLoadAccountPurseTransaction())
			.writer(new BatchLoadAccountPurseResponseWriter(batchUpdateRequestDAO))
			.taskExecutor(taskExecutor())
			.throttleLimit(theadPoolSize)
			.build();
	}

	@Bean
	@StepScope
	@Qualifier("purseLoadRequestPartitioner")
	public Partitioner purseLoadRequestPartitioner() {
		return new BatchLoadAccountPurseRequestPartitioner(batchUpdateRequestDAO, allRequests);
	}

	@Bean
	@StepScope
	public ItemProcessor<String, BatchLoadAccountPurseLog> batchLogRecordCreater() {
		return new ItemProcessor<String, BatchLoadAccountPurseLog>() {

			@Value("#{stepExecution.jobExecution}")
			private JobExecution jobExecution;

			@Value("#{stepExecutionContext[request]}")
			BatchLoadAccountPurse request;

			@Override
			public BatchLoadAccountPurseLog process(String accountNumber) throws Exception {

				if (request == null) {
					log.warn("batch request object is null from execution context for accountNumber:" + accountNumber);
					throw new ServiceException("batch request object is null from execution context for accountNumber:" + accountNumber);
				}

				return BatchLoadAccountPurseLog.builder()
					.batchId(request.getBatchId())
					.accountNumber(accountNumber)
					.correlationId(request.getBatchId() + "-" + accountNumber)
					.build();
			}

		};
	}

	@Bean
	@StepScope
	public ItemProcessor<BatchLoadAccountPurseLog, BatchLoadAccountPurseLog> processLoadAccountPurseTransaction() {
		return new ItemProcessor<BatchLoadAccountPurseLog, BatchLoadAccountPurseLog>() {

			@Value("#{stepExecution.jobExecution}")
			private JobExecution jobExecution;

			@Value("#{stepExecutionContext[request]}")
			BatchLoadAccountPurse request;

			@Override
			public BatchLoadAccountPurseLog process(BatchLoadAccountPurseLog batchLog) throws Exception {
				LoadAccountPurseResponse response = spilServiceAdapter
					.updateAccountPurse(BatchLoadAccountPurseMapper.map(request, batchLog));

				batchLog.setResponseCode(response.getResponseCode());
				batchLog.setResponseMessage(response.getResponseMessage());

				if (response.getResponseCode()
					.equals("0")
						|| response.getResponseCode()
							.equals("00")) {
					batchLog.setAvailableBalance(response.getAvailablePurseBalance());
					batchLog.setAuthAmount(response.getAuthAmount());
				}

				return batchLog;
			}

		};
	}

	@Bean
	Step finalStepLoadAccountPurseBatch() {
		log.debug("Inside finalStep");
		return stepBuilderFactory.get(JobConstants.BATCH_LOAD_ACCOUNT_PURSE_FINAL_STEP)
			.tasklet(finalTaskLoadAccountPurseBatch())
			.build();
	}

	@Bean
	public Tasklet finalTaskLoadAccountPurseBatch() {
		UpdateLoadAccountPurseBatchStatus updateLoadAccountPurseBatchStatus = new UpdateLoadAccountPurseBatchStatus(batchUpdateRequestDAO);
		log.info(CCLPConstants.ENTER);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				if (!CollectionUtils.isEmpty(allRequests)) {
					updateLoadAccountPurseBatchStatus.updateBatchRequestStatus(allRequests);
				}

				log.info(CCLPConstants.EXIT);
				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	public Step loadPurseLoadBatchRequests() {
		log.info("Inside loadPurseLoadBatchRequests");
		return stepBuilderFactory.get("getPurseLoadBatchRequests")
			.tasklet(getPurseLoadBatchRequests())
			.build();
	}

	public Tasklet getPurseLoadBatchRequests() {
		UpdateLoadAccountPurseBatchStatus updateLoadAccountPurseBatchStatus = new UpdateLoadAccountPurseBatchStatus(accountPurseJobService);
		return new Tasklet() {
			@Override
			public RepeatStatus execute(StepContribution contribution, ChunkContext context) throws Exception {
				updateLoadAccountPurseBatchStatus.autoPurseJobRequest();
				List<BatchLoadAccountPurse> requests = batchUpdateRequestDAO.getPendingAccountPurseUpdateRequests();
				allRequests = requests;

				return RepeatStatus.FINISHED;
			}
		};
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<String> jdbcPagingAccountNumberReader(@Value("#{stepExecutionContext['productId']}") Long productId,
			@Value("#{stepExecutionContext['request']}") BatchLoadAccountPurse request) {

		log.info(CCLPConstants.ENTER);
		JdbcPagingItemReader<String> reader = new JdbcPagingItemReader<>();
		try {
			Map<String, Order> sortKeys = Collections.singletonMap("account_number", Order.ASCENDING);

			Map<String, Object> parameterValueMap = new HashMap<>();
			parameterValueMap.put("productId", productId);

			OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();

			String cardStatusClause = getCardStatusClause(request.getOverrideCardStatus());
			queryProvider.setSelectClause("a.account_number");
			queryProvider.setFromClause("card c inner join account a on c.account_id = a.account_id");
			queryProvider.setWhereClause(" c.card_status in " + cardStatusClause + " and c.product_id = :productId ");
			queryProvider.setSortKeys(sortKeys);

			reader.setQueryProvider(queryProvider);
			reader.setDataSource(dataSource);
			reader.setPageSize(splitvalue);

			reader.setRowMapper(new SingleColumnRowMapper<String>(String.class));
			reader.setParameterValues(parameterValueMap);
			reader.setSaveState(false);

			reader.afterPropertiesSet();

		} catch (Exception e) {
			log.error("Exception in jdbc paging reader " + e.getMessage(), e);
		}
		log.info(CCLPConstants.EXIT);
		return reader;
	}

	String getCardStatusClause(String overrideCardStatus) {
		if (overrideCardStatus == null || overrideCardStatus.trim()
			.length() == 0) {
			return "('" + DEFAULT_CARD_STATUS + "')";
		} else {
			String[] cardStatuses = overrideCardStatus.split(",");
			StringBuilder sb = new StringBuilder("(");
			for (int i = 0; i < cardStatuses.length; i++) {
				if (cardStatuses[i].trim()
					.isEmpty()) {
					continue;
				}

				sb.append('\'')
					.append(cardStatuses[i].trim())
					.append("',");

			}
			return sb.deleteCharAt(sb.length() - 1)
				.append(")")
				.toString();
		}
	}

	@Bean
	@StepScope
	public JdbcPagingItemReader<BatchLoadAccountPurseLog> jdbcPagingPurseLoadRequestReader(
			@Value("#{stepExecutionContext['batchId']}") String batchId) {

		log.info(CCLPConstants.ENTER);
		JdbcPagingItemReader<BatchLoadAccountPurseLog> reader = new JdbcPagingItemReader<>();
		try {
			Map<String, Order> sortKeys = Collections.singletonMap("REQUEST_ID", Order.ASCENDING);

			Map<String, Object> parameterValueMap = new HashMap<>();
			parameterValueMap.put("batchId", batchId);

			log.info("ExecutionContext: " + "Batchid:" + batchId);

			OraclePagingQueryProvider queryProvider = new OraclePagingQueryProvider();

			String selectClause = "batch_id, request_id, correlation_id, account_number, response_code";

			queryProvider.setSelectClause(selectClause);
			queryProvider.setFromClause("batch_load_account_purse_log l");
			queryProvider.setWhereClause(" l.batch_id = :batchId AND RESPONSE_CODE IS NULL");
			queryProvider.setSortKeys(sortKeys);

			reader.setQueryProvider(queryProvider);
			reader.setDataSource(dataSource);
			reader.setPageSize(splitvalue);

			reader.setRowMapper(rowMapper());
			reader.setParameterValues(parameterValueMap);
			reader.setSaveState(false);

			reader.afterPropertiesSet();

		} catch (Exception e) {
			log.error("Exception in jdbc paging reader " + e.getMessage(), e);
		}
		log.info(CCLPConstants.EXIT);
		return reader;
	}

	public RowMapper<BatchLoadAccountPurseLog> rowMapper() {

		return (rs, rowNum) -> BatchLoadAccountPurseLog.builder()
			.batchId(rs.getLong("batch_id"))
			.requestId(rs.getLong("request_id"))
			.correlationId(rs.getString("correlation_id"))
			.accountNumber(rs.getString("account_number"))
			.responseCode(rs.getString("response_code"))
			.build();

	}

	@Bean
	public SimpleAsyncTaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor simpleTaskExecutor = new SimpleAsyncTaskExecutor();
		simpleTaskExecutor.setConcurrencyLimit(theadPoolSize);
		return simpleTaskExecutor;
	}

	@Bean
	public JobCompletionNotificationListener jobCompletionNotificationListener() {
		return new JobCompletionNotificationListener();
	}

}
