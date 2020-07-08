package com.incomm.scheduler.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.incomm.scheduler.dao.SchedulerJobDAO;
import com.incomm.scheduler.model.ProcessSchedule;
import com.incomm.scheduler.utils.ScriptUtils;

@Repository
public class SchedulerJobDAOImpl extends JdbcDaoSupport implements SchedulerJobDAO{

	@Autowired
	public void setDs(@Qualifier("orderDs") DataSource dataSource) {
		setDataSource(dataSource);
	}


	public ProcessSchedule getProcessSchedulerDetails(String jobId){

		return getJdbcTemplate().query(ScriptUtils.GET_SCHEDULER_JOB_CONFIG_BY_JOBID,new Object[]{jobId}, new ResultSetExtractor<ProcessSchedule>(){
			public ProcessSchedule extractData(ResultSet rs)throws SQLException {
				ProcessSchedule processScheduler = new ProcessSchedule();
				if (rs.next()) {
					processScheduler.setProcessId(rs.getString(1));
					processScheduler.setProcessName(rs.getString(2));
					processScheduler.setProcessRetryIntervel(rs.getString(3));
					processScheduler.setScheduleDays(rs.getString(4));
					processScheduler.setStartHH(rs.getString(5));
					processScheduler.setStartMM(rs.getString(6));
					processScheduler.setFileId(rs.getString(7));
					processScheduler.setProcessType(rs.getString(8));
					processScheduler.setProcessRetryIntervalType(rs.getString(9));
					processScheduler.setRetryCnt(rs.getString(10));
					processScheduler.setSchedularStatus(rs.getString(11));
					processScheduler.setMailSuccess(rs.getString(12));
					processScheduler.setMailFail(rs.getString(13));
					processScheduler.setProcRunning(rs.getString(14));
					processScheduler.setProcRetryDate(rs.getDate(15));
					processScheduler.setDayOfMonth(rs.getString(16));
					processScheduler.setMultiRunInterval(rs.getString(17));
					processScheduler.setMultiRunIntervalType(rs.getString(18));
					processScheduler.setMultiRunFlag(rs.getString(19));
					processScheduler.setProcessClass(rs.getString(20));
				}
				return processScheduler;
			}
		});

	}


	public List<ProcessSchedule> getProcessSchedulerDetailsList(){

		return getJdbcTemplate().query(ScriptUtils.GET_SCHEDULER_JOB_CONFIG, new ResultSetExtractor<List<ProcessSchedule>>(){
			public List<ProcessSchedule> extractData(ResultSet rs)throws SQLException {
				List<ProcessSchedule> processList=new ArrayList<>();
				while (rs.next()) {
					ProcessSchedule processScheduler = new ProcessSchedule();
					processScheduler.setProcessId(rs.getString(1));
					processScheduler.setProcessName(rs.getString(2));
					processScheduler.setProcessRetryIntervel(rs.getString(3));
					processScheduler.setScheduleDays(rs.getString(4));
					processScheduler.setStartHH(rs.getString(5));
					processScheduler.setStartMM(rs.getString(6));
					processScheduler.setFileId(rs.getString(7));
					processScheduler.setProcessType(rs.getString(8));
					processScheduler.setProcessRetryIntervalType(rs.getString(9));
					processScheduler.setRetryCnt(rs.getString(10));
					processScheduler.setSchedularStatus(rs.getString(11));
					processScheduler.setMailSuccess(rs.getString(12));
					processScheduler.setMailFail(rs.getString(13));
					processScheduler.setProcRunning(rs.getString(14));
					processScheduler.setProcRetryDate(rs.getDate(15));
					processScheduler.setDayOfMonth(rs.getString(16));
					processScheduler.setMultiRunInterval(rs.getString(17));
					processScheduler.setMultiRunIntervalType(rs.getString(18));
					processScheduler.setMultiRunFlag(rs.getString(19));
					processScheduler.setProcessClass(rs.getString(20));
					processList.add(processScheduler);
				}
				return processList;
			}
		});

	}
	/**
	 * updateErrorLog method used to update the error record in error_log table while processing the record of file.
	 * Added by venkateshgaddam
	 */
	public int updateErrorLog(String fileName,String jobName,String msg,String errorDesc){
		return getJdbcTemplate().update(ScriptUtils.UPDATE_ERROR_LOG_TBL,fileName,jobName,msg,errorDesc );
	}
	
	/**
	 * updateSchedulerProcStatus method used to update the scheduler Processing status in Process_status table.
	 * Added by venkateshgaddam
	 */
	public int updateSchedulerProcStatus(String procRunningFlag,String procCompletedFlag,Date completeDate,String processId){
		return getJdbcTemplate().update(ScriptUtils.UPDATE_PROCESS_SCHEDULE_STATUS, procRunningFlag, procCompletedFlag, completeDate, processId );
	}

	@Override
	public List<String> retrieveMailList(String mailIds) {
		return getJdbcTemplate().queryForList(ScriptUtils.MAIL_ID_LIST, String.class,mailIds, mailIds);
	}


}
