package com.incomm.scheduler.config.partion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.BatchLoadAccountPurseDAO;
import com.incomm.scheduler.model.BatchLoadAccountPurse;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BatchLoadAccountPurseRequestPartitioner implements Partitioner {

	private static final String PARTITION_KEY = "LOAD_ACCOUNT_PURSE_REQUEST_PARTITION_KEY";

	private BatchLoadAccountPurseDAO batchUpdateRequestDAO;
	private List<BatchLoadAccountPurse> requests;

	@Value("${SPLIT_VALUE}")
	private Integer splitvalue;

	public BatchLoadAccountPurseRequestPartitioner(BatchLoadAccountPurseDAO batchUpdateRequestDAO, List<BatchLoadAccountPurse> requests) {
		super();
		this.batchUpdateRequestDAO = batchUpdateRequestDAO;
		this.requests = requests;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {

		log.info(CCLPConstants.ENTER);

		Map<String, ExecutionContext> contextsMap = new HashMap<>(gridSize);

		int partitionCount = 0;

		for (BatchLoadAccountPurse request : requests) {

			int count = batchUpdateRequestDAO.getRecordCountByBatchId(request.getBatchId());
			int gridSize1 = count / splitvalue;

			for (int threadCount = 1; threadCount <= gridSize1 + 1; threadCount++) {
				ExecutionContext context = new ExecutionContext();

				context.put("request", request);
				context.put("batchId", request.getBatchId());
				context.put("productId", request.getProductId());
				context.putString("name", "Processing Thread" + partitionCount);

				contextsMap.put(PARTITION_KEY + partitionCount, context);

				partitionCount++;
			}

		}
		log.info("Partitioners: " + contextsMap);

		log.info(CCLPConstants.EXIT);
		return contextsMap;
	}

}
