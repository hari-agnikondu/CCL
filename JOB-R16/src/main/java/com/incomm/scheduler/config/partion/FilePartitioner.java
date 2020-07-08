package com.incomm.scheduler.config.partion;

/**
 * FilePartitioner provides the partition logic of file processing
 * author venkateshgaddam
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.scheduler.utils.JobConstants;

public class FilePartitioner implements Partitioner{
	
	@Autowired
	public StepBuilderFactory stepBuilderFactory;

	private List<String> fileNames;

	public FilePartitioner()
	{
		
	}
	
	public FilePartitioner(List<String> fileNames)
	{
		this.fileNames = fileNames;
	}

	@Override
	public Map<String, ExecutionContext> partition(int gridSize) {
		
		Map<String, ExecutionContext> result = new HashMap<>();

		if (fileNames == null || fileNames.isEmpty())
			return result;

		int partitionNumber = 1;

		// Bucketsize is number of files in one partition
		int bucketSize = (int) Math.ceil((double) fileNames.size() / gridSize);
		for (int i=0; i<fileNames.size(); i++)
		{
			List<String> bucketFiles = new ArrayList<>();
			for (int j=0; j<bucketSize && i<fileNames.size(); j++)
			{
					bucketFiles.add(fileNames.get(i++));
			}
			ExecutionContext value = new ExecutionContext();
			value.put(JobConstants.FILE_LIST, bucketFiles);
			result.put(JobConstants.PARTITION + partitionNumber, value);
			i--;
			partitionNumber++;
		}

		return result;
	}
	
}

