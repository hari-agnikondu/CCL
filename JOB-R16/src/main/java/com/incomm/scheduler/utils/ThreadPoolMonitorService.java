package com.incomm.scheduler.utils;

import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.service.IThreadPoolMonitorService;

@Service
public class ThreadPoolMonitorService implements IThreadPoolMonitorService {
     @Autowired
     ThreadPoolTaskExecutor executor;
	private static final Logger logger = LogManager.getLogger(ThreadPoolMonitorService.class);
    //ThreadPoolExecutor executor;
    private long monitoringPeriod; 
     /*public ThreadPoolMonitorService(ThreadPoolExecutor executor) {
    	 this.executor = executor;
     }*/
     
    public void monitorThreadPool() {
		logger.info(CCLPConstants.ENTER);
        StringBuffer strBuff = new StringBuffer();
        strBuff.append("CurrentPoolSize : ").append(executor.getPoolSize());
        strBuff.append(" - CorePoolSize : ").append(executor.getCorePoolSize());
        strBuff.append(" - MaximumPoolSize : ").append(executor.getMaxPoolSize());
        strBuff.append(" - ActiveTaskCount : ").append(executor.getActiveCount());
       // strBuff.append(" - CompletedTaskCount : ").append(executor.getCompletedTaskCount());
        strBuff.append(" - TotalTaskCount : ").append(executor.getThreadPriority());
        strBuff.append(" - isTerminated : ").append(executor.isDaemon());
        
        logger.debug(strBuff.toString());
		logger.info(CCLPConstants.EXIT);
    }
     
     
     
    public long getMonitoringPeriod() {
        return monitoringPeriod;
    }
 
    public void setMonitoringPeriod(long monitoringPeriod) {
        this.monitoringPeriod = monitoringPeriod;
    }



	public ThreadPoolTaskExecutor getExecutor() {
		return executor;
	}



	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}



	




	
}