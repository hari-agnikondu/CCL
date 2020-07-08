package com.incomm.scheduler.config.partion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.model.BatchLoadAccountPurse;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BatchLoadAccountPursePartitioner implements Partitioner {

	private static final String PARTITION_KEY = "BATCH_LOAD_ACCOUNT_PURSE_PARTITION_KEY";

	private List<BatchLoadAccountPurse> requests;

	public BatchLoadAccountPursePartitioner(List<BatchLoadAccountPurse> requests) {
		super();
		this.requests = requests;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {

		log.info(CCLPConstants.ENTER);

		Map<String, ExecutionContext> contextsMap = new HashMap<>(gridSize);

		int i = 0;

		for (BatchLoadAccountPurse request : requests) {

			ExecutionContext context = new ExecutionContext();

			context.put("request", request);
			context.put("batchId", request.getBatchId());
			context.put("productId", request.getProductId());

			contextsMap.put(PARTITION_KEY + (i++), context);

		}
		log.info("Partitioners: " + contextsMap);

		log.info(CCLPConstants.EXIT);
		return contextsMap;
	}

}
