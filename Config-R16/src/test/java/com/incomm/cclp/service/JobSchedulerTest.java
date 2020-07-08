package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.impl.MetaDataDAOImpl;
import com.incomm.cclp.dto.ScheduleJobDTO;
import com.incomm.cclp.dto.SwitchOverSchedulerDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class JobSchedulerTest {

	@Autowired
	JobSchedulerService jobSchedulerService;

	@Autowired
	MetaDataDAOImpl metadata;

	/*
	 * getting the scheduler DTO if the Id is existing in Database
	 */
	@Test
	public void get_scheduler_config_by_passing_id() {
		metadata.jobSchedulerMetaData();
		try {
			ScheduleJobDTO schedulejob = jobSchedulerService.getSchedulerConfig((long) 1);
			assertNotNull(schedulejob);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.SCHEDULERCONFIG_UPDATE_FAIL);
		}
	}

	/*
	 * This will get the job details based on JobId
	 */
	@Test
	public void get_scheduler_config_byPassing_nonExisting_jobId() {
		metadata.jobSchedulerMetaData();
		try {
			@SuppressWarnings("unused")
			ScheduleJobDTO schedulejob = jobSchedulerService.getSchedulerConfig((long) 100);

		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.SCHEDULERCONFIG_RETRIEVE_FAIL);
		}
	}

	/*
	 * getting all the jobs exists in Database if jobs are empty or Null it will
	 * throw exception otherwise it will return Map
	 */
	@Test
	public void get_all_Jobs_jobs_exists() {
		try {
			metadata.jobSchedulerMetaData();
			Map<Long, String> jobList = jobSchedulerService.getJobs();

			assertNotNull(jobList);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.JOBS_DONOT_EXIST);
		}
	}

	/*
	 * getting all the jobs with empty job list
	 */
	@Test
	public void get_all_Jobs_with_Empty_jobs() {
		try {
			metadata.jobSchedulerEmptyMetaData();
			Map<Long, String> jobList = jobSchedulerService.getJobs();
			assertNotNull(jobList);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.JOBS_DONOT_EXIST);
		}
	}

	/*
	 * It will throw error message if the job is not updated in Database
	 */
	@Test
	public void update_the_existing_job_with_newValues() {

		metadata.jobSchedulerMetaData();

		ScheduleJobDTO dto = new ScheduleJobDTO();
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		dto.setJobId((long) 1);
		dto.setJobName("Job");
		dto.setTotalDays(list);
		dto.setStartTimeHours("10");
		dto.setStartTimeMins("10");
		dto.setRetryCount("5");
		dto.setRetryInterval("10");
		dto.setScheduleFlag("Y");
		List<String> successMailUser = new ArrayList<>();
		successMailUser.add("1");
		successMailUser.add("2");
		dto.setSuccessMailUser(successMailUser);
		List<String> failMailUser = new ArrayList<>();
		failMailUser.add("1");
		failMailUser.add("2");
		dto.setFailMailUser(failMailUser);
		dto.setDayOfMonth("1");
		dto.setMultipleRunInterval("");
		dto.setMultipleRunTimeUnit("MM");
		dto.setMultipleRunFlag("Y");
		dto.setRetryIntervalTimeUnit("MM");
		try {
			jobSchedulerService.updateSchedulerConfigForAJob(dto);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.SCHEDULERCONFIG_UPDATE_FAIL);
		}
	}

	/*
	 * trying to update the non existing jobId It will throw Exception
	 */
	@Test
	public void update_the_nonExisting_job_itThrows_exception() {

		metadata.jobSchedulerMetaData();

		ScheduleJobDTO dto = new ScheduleJobDTO();
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		dto.setJobId((long) 100);
		dto.setJobName("Job");
		dto.setTotalDays(list);
		dto.setStartTimeHours("10");
		dto.setStartTimeMins("10");
		dto.setRetryCount("5");
		dto.setRetryInterval("10");
		dto.setScheduleFlag("Y");
		List<String> successMailUser = new ArrayList<>();
		successMailUser.add("1");
		successMailUser.add("2");
		dto.setSuccessMailUser(successMailUser);
		List<String> failMailUser = new ArrayList<>();
		failMailUser.add("1");
		failMailUser.add("2");
		dto.setFailMailUser(failMailUser);
		dto.setDayOfMonth("1");
		dto.setMultipleRunInterval("");
		dto.setMultipleRunTimeUnit("MM");
		dto.setMultipleRunFlag("Y");
		dto.setRetryIntervalTimeUnit("MM");

		try {
			jobSchedulerService.updateSchedulerConfigForAJob(dto);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			assertEquals(e.getMessage(), ResponseMessages.SCHEDULERCONFIG_UPDATE_FAIL);
		}
	}

	/*
	 * Trying to get all the Existing servers
	 */
	@Test
	public void getAllServers_test() {

		metadata.schedulerRunningMetaData();
		metadata.metaFileCreationStatus();
		try {
			List<SwitchOverSchedulerDTO> list = jobSchedulerService.getServers();
			assertNotNull(list);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), "InvalidDataAccessResourceUsageException");
		}
	}

	/*
	 * getting all the exception if the Existing servers list is empty
	 */
	@Test
	public void getAllServers_test_If_notExisting() {

		metadata.schedulerRunningEmptyMetaData();
		metadata.metaFileCreationStatus();
		try {
			List<SwitchOverSchedulerDTO> list = jobSchedulerService.getServers();
			assertNotNull(list);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.SERVERS_DOESNOT_EXIST);
		}
	}

	/*
	 * Updating the Status of the Running server to not running status
	 */
	@Test
	public void update_switchoverscheduler_stop() {

		metadata.schedulerRunningMetaData();
		metadata.metaFileCreationStatus();
		SwitchOverSchedulerDTO switchOverSchedulerDTO = new SwitchOverSchedulerDTO();

		switchOverSchedulerDTO.setPhysicalServer("FSS");
		switchOverSchedulerDTO.setManagedServer("FSS");
		switchOverSchedulerDTO.setPort((long) 8080);
		switchOverSchedulerDTO.setStatus("Y");
		try {
			int count = jobSchedulerService.updateSwitchOverScheduler(switchOverSchedulerDTO);
			assertEquals(count, 1);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/*
	 * starting a server if no other server is running
	 */
	@Test
	public void update_switchoverscheduler_first_start() {

		metadata.schedulerRunningMetaData();
		metadata.metaFileCreationStatus();

		SwitchOverSchedulerDTO switchOverSchedulerDTO = new SwitchOverSchedulerDTO();

		switchOverSchedulerDTO.setPhysicalServer("INCOMM");
		switchOverSchedulerDTO.setManagedServer("INCOMM");
		switchOverSchedulerDTO.setPort((long) 9090);
		switchOverSchedulerDTO.setStatus("N");
		try {
			int count = jobSchedulerService.updateSwitchOverScheduler(switchOverSchedulerDTO);
			assertEquals(count, 1);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Starting a servers if already one server is running
	 */
	@Test
	public void update_switchoverscheduler_start_secondServer() {

		metadata.schedulerRunningMetaData();
		metadata.metaFileCreationStatus();

		SwitchOverSchedulerDTO switchOverSchedulerDTO = new SwitchOverSchedulerDTO();

		switchOverSchedulerDTO.setPhysicalServer("FSS");
		switchOverSchedulerDTO.setManagedServer("FSS");
		switchOverSchedulerDTO.setPort((long) 8080);
		switchOverSchedulerDTO.setStatus("N");
		try {
			jobSchedulerService.updateSwitchOverScheduler(switchOverSchedulerDTO);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		SwitchOverSchedulerDTO switchOverSchedulerDTO2 = new SwitchOverSchedulerDTO();
		switchOverSchedulerDTO2.setPhysicalServer("INCOMM");
		switchOverSchedulerDTO2.setManagedServer("INCOMM");
		switchOverSchedulerDTO2.setPort((long) 9090);
		switchOverSchedulerDTO2.setStatus("N");
		try {
			int count = jobSchedulerService.updateSwitchOverScheduler(switchOverSchedulerDTO2);
			assertEquals(count, 1);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.SWITCHOVERSCHEDULER_ALREADY_RUNNING);
		}
	}

	/*
	 * Trying to update the status of the server if status is different String
	 */
	@Test
	public void update_switchoverscheduler_update_nonExisting_server() {

		metadata.schedulerRunningMetaData();
		metadata.metaFileCreationStatus();
		SwitchOverSchedulerDTO switchOverSchedulerDTO = new SwitchOverSchedulerDTO();
		switchOverSchedulerDTO.setPhysicalServer("10.44.75.203");
		switchOverSchedulerDTO.setManagedServer("10.44.75.203");
		switchOverSchedulerDTO.setPort((long) 8090);
		switchOverSchedulerDTO.setStatus("-");
		try {
			int count = jobSchedulerService.updateSwitchOverScheduler(switchOverSchedulerDTO);
			assertEquals(count, 1);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.SWITCHOVERSCHEDULER_UPDATE_FAIL);
		}
	}

	/*
	 * Updating the status of the Server if the status is empty or Null
	 */
	@Test
	public void update_switchoverscheduler_update_nonExisting_serverStatus() {

		metadata.schedulerRunningMetaData();
		metadata.metaFileCreationStatus();

		SwitchOverSchedulerDTO switchOverSchedulerDTO = null;
		try {
			int count = jobSchedulerService.updateSwitchOverScheduler(switchOverSchedulerDTO);
			assertEquals(count, 1);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(), ResponseMessages.SWITCHOVERSCHEDULER_UPDATE_FAIL);
		}
	}

	/*
	 * It will retrieve all the scheduler service jobs
	 */
	// @Test
	public void get_All_service_Jobs() {
		metadata.schedulerRunningMetaData();
		metadata.metaFileCreationStatus();
		List<Object[]> list = jobSchedulerService.getJobSchedulerServiceJobs();
		assertNull(list);
	}

}
