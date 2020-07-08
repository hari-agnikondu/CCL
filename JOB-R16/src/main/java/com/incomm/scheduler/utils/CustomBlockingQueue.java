package com.incomm.scheduler.utils;

import java.util.concurrent.ArrayBlockingQueue;

public class CustomBlockingQueue<ThreadPoolExecutor> extends  ArrayBlockingQueue<ThreadPoolExecutor>{
	
	/**
	 * 
	 */

	private static final long serialVersionUID = -1021894592080986157L;

	CustomBlockingQueue(int size) {
        super(size);
    }
	
	@Override
    public boolean offer(ThreadPoolExecutor task) {
        try {
            this.put(task);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
