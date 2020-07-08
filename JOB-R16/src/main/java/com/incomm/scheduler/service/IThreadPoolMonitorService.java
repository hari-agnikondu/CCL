package com.incomm.scheduler.service;
 
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
 

public interface IThreadPoolMonitorService  {
 
    public void monitorThreadPool();
     
    public ThreadPoolTaskExecutor getExecutor();
     
    public void setExecutor(ThreadPoolTaskExecutor executor);
     
}