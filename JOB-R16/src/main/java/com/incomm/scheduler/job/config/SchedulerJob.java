package com.incomm.scheduler.job.config;

import com.incomm.scheduler.model.ProcessSchedule;

public interface SchedulerJob {    
    void start(ProcessSchedule processSchedule);
    void stop();
}
