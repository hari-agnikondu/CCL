package com.incomm.scheduler.config.partion;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;

import com.incomm.scheduler.utils.JobConstants;

public class FileRangePartioner implements Partitioner 
{
	
	private static final Logger logger = LogManager.getLogger(FileRangePartioner.class);
	
    private Resource[] resources = new Resource[0];
    private String keyName = JobConstants.DEFAULT_KEY_NAME;

    /**
     * The name of the key for the file name in each {@link ExecutionContext}.
     * Defaults to "fileName".
     * @param keyName the value of the key
     */
    public void setKeyName(String keyName) {
        this.keyName = keyName;
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
     * Assign the filename of each of the injected resources to an
     * {@link ExecutionContext}.
     *
     * @see Partitioner#partition(int)
     */
	@Override
	public Map<String, ExecutionContext> partition(int gridSize) 
	{
		Map<String, ExecutionContext> map = new HashMap<>(gridSize);
        int fileCount = 0;
        for (Resource resource : resources) {
            ExecutionContext context = new ExecutionContext();
            logger.debug("File Name from Source Directory:"+resource.getFilename());
            context.putString(keyName, resource.getFilename());
            context.putString(JobConstants.DEFAULT_THREAD_NAME,"Thread"+fileCount);
            map.put(JobConstants.PARTITION_KEY + fileCount, context);
            fileCount++;
        }
        return map;
    }


}
