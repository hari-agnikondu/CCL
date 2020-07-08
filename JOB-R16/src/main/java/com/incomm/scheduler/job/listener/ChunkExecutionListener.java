package com.incomm.scheduler.job.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.listener.ChunkListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.incomm.scheduler.service.IThreadPoolMonitorService;

public class ChunkExecutionListener extends ChunkListenerSupport{
	
	private static final Logger logger = LoggerFactory.getLogger(ChunkExecutionListener.class);
	
	@Autowired
	IThreadPoolMonitorService ithreadPoolMonitorService;

	@Override
	public void afterChunk(ChunkContext context) {
		
		logger.info("After chunk BatchId"+context.getStepContext().getStepExecutionContext().get("BatchId"));
		logger.info("After chunk fileName"+context.getStepContext().getStepExecutionContext().get("fileName"));
		
		ithreadPoolMonitorService.monitorThreadPool();
	
		super.afterChunk(context);
	}

	@Override
	public void beforeChunk(ChunkContext context) {
		context.attributeNames();
		logger.info("Before chunk BatchId"+context.getStepContext().getStepExecutionContext().get("BatchId"));
		logger.info("Before chunk fileName"+context.getStepContext().getStepExecutionContext().get("fileName"));

		ithreadPoolMonitorService.monitorThreadPool();
		super.beforeChunk(context);
	}
	

}
