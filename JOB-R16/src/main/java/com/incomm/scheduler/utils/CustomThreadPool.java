package com.incomm.scheduler.utils;

import java.util.concurrent.BlockingQueue;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class CustomThreadPool extends ThreadPoolTaskExecutor {

	@Override
	public BlockingQueue createQueue(int queueCapacity){
     if(queueCapacity > 0) {		
		return new CustomBlockingQueue(queueCapacity);
	}
     return new CustomBlockingQueue(1000);
	}
}
