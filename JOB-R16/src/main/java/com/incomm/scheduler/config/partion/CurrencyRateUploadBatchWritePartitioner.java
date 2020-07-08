package com.incomm.scheduler.config.partion;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;

import com.incomm.scheduler.constants.CCLPConstants;

public class CurrencyRateUploadBatchWritePartitioner implements Partitioner {

	@Value("${SPLIT_VALUE}")
	private Integer splitvalue;
	
	private static final Logger logger = LogManager.getLogger(CurrencyRateUploadBatchWritePartitioner.class);

	private Map<String, String> fileList;

	public Map<String, String> getFileList() {
		return fileList;
	}

	public void setFileList(Map<String, String> fileList) {
		this.fileList = fileList;
	}

	/**
	 * Assign the filename of each of the injected resources to an
	 * {@link ExecutionContext}.
	 *
	 * @see Partitioner#partition(int)
	 */
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		
		logger.info(CCLPConstants.ENTER);
		Map<String, ExecutionContext> map = new HashMap<>(gridSize);

		int partitionCnt = 1;
		for (Entry<String, String> batch : fileList.entrySet()) {

			try {

					ExecutionContext context = new ExecutionContext();
					context.putString("BatchId", batch.getKey());
					context.putString("file", batch.getValue());
					context.putString("name", "Processing Thread" + partitionCnt);
					map.put("PARTITION_KEY" + partitionCnt, context);
					partitionCnt++;
			

			} catch (Exception e) {
				logger.error("Exception in batch partition: " + e.getMessage());
			}

		}
		logger.info("Batch partitions: " + map);
		logger.info(CCLPConstants.EXIT);
		return map;
	}

}
