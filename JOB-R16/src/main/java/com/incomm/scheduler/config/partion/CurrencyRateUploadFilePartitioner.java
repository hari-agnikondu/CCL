package com.incomm.scheduler.config.partion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CurrencyRateUploadDAO;


/**
 * Implementation of {@link Partitioner} that locates multiple resources and
 * associates their file names with execution context keys. Creates an
 * {@link ExecutionContext} per resource, and labels them as
 * <code>{partition0, partition1, ..., partitionN}</code>. The grid size is
 * ignored.
 *
 * @author Dave Syer
 * @since 2.0
 */


public class CurrencyRateUploadFilePartitioner implements  org.springframework.batch.core.partition.support.Partitioner {
	
	private static final Logger logger = LogManager.getLogger(CurrencyRateUploadFilePartitioner.class);

	private static final String DEFAULT_KEY_NAME = "fileName";

	private static final String PARTITION_KEY = "partition";

	private Resource[] resources = new Resource[0];

	private String keyName = DEFAULT_KEY_NAME;
	
	private final CurrencyRateUploadDAO currencyRateUploadDao;
	
	
	public CurrencyRateUploadFilePartitioner(CurrencyRateUploadDAO currencyRateUploadDao) {
		this.currencyRateUploadDao = currencyRateUploadDao;
	}

	/**
	 * The resources to assign to each partition. In Spring configuration you
	 * can use a pattern to select multiple resources.
	 * @param resources the resources to use
	 */
	public void setResources(Resource[] resources) {
		this.resources = resources;
	}

	/**
	 * The name of the key for the file name in each {@link ExecutionContext}.
	 * Defaults to "fileName".
	 * @param keyName the value of the key
	 */
	public void setKeyName(String keyName) {
		this.keyName = keyName;
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
		int i = 0;
		Map<String,String> fileList = new HashMap<>();
		List<String> batchList=new ArrayList<>();
		for (Resource resource : resources) {
			
			ExecutionContext context = new ExecutionContext();
			try {
				String batchId = currencyRateUploadDao.getBatchId();
				String file = resource.getFilename();
				context.putString(keyName, resource.getURL().toExternalForm());
				context.putString("file",file );
				context.putString( "BatchId",batchId);
				batchList.add(batchId+":"+file);
				fileList.put(batchId,file);
				context.put("fileList", fileList);
				context.put("batchList", batchList);
		      }
			catch (Exception e) {
				logger.error("Exception in file partitioner: "+ e.getMessage());
			}
			map.put(PARTITION_KEY + i, context);
			i++;
		}
		logger.info("Partitioners: " + map);
		currencyRateUploadDao.InsertCurrencyRateUploadFiles(map);
		logger.info(CCLPConstants.EXIT);
		return map;
	}
	
	@BeforeStep
    public void beforeStep(StepExecution stepExecution) {
          JobExecution jobExecution = stepExecution.getJobExecution();
          ExecutionContext jobContext = jobExecution.getExecutionContext();

    }
}
