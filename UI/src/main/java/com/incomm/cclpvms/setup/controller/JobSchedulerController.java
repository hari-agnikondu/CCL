package com.incomm.cclpvms.setup.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.constants.CCLPConstants;
import com.incomm.cclpvms.constants.ResponseMessages;
import com.incomm.cclpvms.exception.ServiceException;
import com.incomm.cclpvms.setup.model.JobScheduler;
import com.incomm.cclpvms.setup.model.SwitchOverScheduler;
import com.incomm.cclpvms.setup.service.JobSchedulerService;
import com.incomm.cclpvms.util.ResourceBundleUtil;


@Controller
@RequestMapping("jobScheduler")
public class JobSchedulerController {

	private static final Logger logger = LogManager.getLogger(JobSchedulerController.class);

	@Autowired
	private JobSchedulerService jobSchedulerService;

	@PreAuthorize("hasRole('EDIT_SCHEDULER')")
	@RequestMapping("/jobSchedulerConfig")
	public ModelAndView configScheduler(HttpServletRequest req) throws ServiceException {

		logger.debug(CCLPConstants.ENTER);
		ModelAndView mav = new ModelAndView("jobSchedulerConfig");
		JobScheduler scheduler = null;
		Long jobId = req.getParameter("jobId") != null ? Long.parseLong(req.getParameter("jobId")) : -1;
		if (jobId != null && jobId != -1) {
			ResponseDTO responseDTO = jobSchedulerService.getSchedulerConfigByJobId(jobId);
			if (responseDTO != null) {
				if (responseDTO.getData() != null && responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
					logger.debug("Scheduler configuration fetched successfully. Response from config service is "
							+ responseDTO);
					ObjectMapper objectMapper = new ObjectMapper();
					scheduler = objectMapper.convertValue(responseDTO.getData(), JobScheduler.class);
				} else {
					mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
					mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getMessage());
					scheduler = new JobScheduler();
					logger.error(
							"Failed to fetch scheduler configuration. Response from config service is " + responseDTO);
				}
			} else {
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				mav.addObject(CCLPConstants.STATUS_MESSAGE, ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
				scheduler = new JobScheduler();
				logger.error("Failed to fetch scheduler configuration. Response from config service is " + responseDTO);
			}
		} else {
			scheduler = new JobScheduler();
		}
		Map<Long, String> jobDtlMap = jobSchedulerService.getAllJobs();
		List<Object[]> userMailDetail= jobSchedulerService.getUserMailDetail();
	
		mav.addObject(CCLPConstants.SCHEDULER, scheduler);
		mav.addObject("jobDtlMap", jobDtlMap);
		mav.addObject("userMailDetail",userMailDetail);

		Map<Integer, String> weekDays = new HashMap<>();
		weekDays.put(1, "Sunday");
		weekDays.put(2, "Monday");
		weekDays.put(3, "Tuesday");
		weekDays.put(4, "Wednsday");
		weekDays.put(5, "Thursday");
		weekDays.put(6, "Friday");
		weekDays.put(7, "Saturday");
		weekDays.put(8, "All");

		mav.addObject("weekDays", weekDays);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_SCHEDULER')")
	@RequestMapping("/saveConfigJobScheduler")
	public ModelAndView saveConfigScheduler(@ModelAttribute("scheduler") JobScheduler scheduler)
			throws ServiceException {
		logger.debug("ENTER " + scheduler);
		ModelAndView mav = new ModelAndView("jobSchedulerConfig");

		mav.addObject(CCLPConstants.SCHEDULER, scheduler);

		ResponseDTO responseDTO = jobSchedulerService.updateSchedulerConfig(scheduler);
		if (responseDTO != null) {
			if (responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				logger.info(
						"Scheduler configuration updated successfully. Response from config service is " + responseDTO);
			} else {
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				logger.error(
						"Failed to update scheduler configuration. Response from config service is " + responseDTO);
			}
			mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getMessage());
		} else {
			logger.error("Failed to update scheduler configuration. Response from config service is null");
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}

		mav.addObject(CCLPConstants.SCHEDULER, scheduler);
		Map<Long, String> jobDtlMap = jobSchedulerService.getAllJobs();
		mav.addObject("jobDtlMap", jobDtlMap);
		
		List<Object[]> userMailDetail= jobSchedulerService.getUserMailDetail();
		mav.addObject("userMailDetail",userMailDetail);


		Map<Integer, String> weekDays = new HashMap<>();
		weekDays.put(1, "Sunday");
		weekDays.put(2, "Monday");
		weekDays.put(3, "Tuesday");
		weekDays.put(4, "Wednsday");
		weekDays.put(5, "Thursday");
		weekDays.put(6, "Friday");
		weekDays.put(7, "Saturday");
		weekDays.put(8, "All");

		mav.addObject("weekDays", weekDays);
		logger.debug(CCLPConstants.EXIT);
		return mav;
	}

	@ExceptionHandler(Exception.class)
	public ModelAndView exceptionHandler(Exception exception) {
		logger.debug(CCLPConstants.ENTER);
		String errMessage = ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE);

		if (exception instanceof ServiceException || exception instanceof AccessDeniedException)
			errMessage = exception.getMessage();

		ModelAndView mav = new ModelAndView("serviceError");
		mav.addObject(CCLPConstants.STATUS_MESSAGE, errMessage);

		logger.error(Arrays.toString(exception.getStackTrace()));
		logger.debug(CCLPConstants.EXIT);

		return mav;
	}

	@PreAuthorize("hasRole('EDIT_SCHEDULER')")
	@RequestMapping("/saveSwitchOverScheduler")
	public ModelAndView saveSwitchOverScheduler(@ModelAttribute("scheduler") SwitchOverScheduler scheduler,
			BindingResult bindingResult) {
		logger.debug("ENTER " + scheduler);
		ModelAndView mav = new ModelAndView(CCLPConstants.SWICHOVER_SCHEDULER_CONFIG);
		mav.addObject(CCLPConstants.SCHEDULER, scheduler);
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_SCHEDULER')")
	@RequestMapping(CCLPConstants.SWICHOVER_SCHEDULER_CONFIG)
	public ModelAndView switchOverScheduler() throws ServiceException {

		ModelAndView mav = new ModelAndView(CCLPConstants.SWICHOVER_SCHEDULER_CONFIG);

		List<SwitchOverScheduler> scheduler = jobSchedulerService.getAllServers();

		mav.addObject("SwitchOverScheduler", new SwitchOverScheduler());
		mav.addObject(CCLPConstants.SCHEDULER, scheduler);
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_SCHEDULER')")
	@RequestMapping("updateSwitchOverScheduler")
	public ModelAndView updateSwitchOverScheduler(@ModelAttribute("SwitchOverScheduler") SwitchOverScheduler scheduler,
			BindingResult bindingResult, HttpServletRequest request) throws ServiceException {

		ResponseDTO responseDTO = null;
		ModelAndView mav = new ModelAndView(CCLPConstants.SWICHOVER_SCHEDULER_CONFIG);
		mav.addObject("SwitchOverScheduler", new SwitchOverScheduler());

		String selectforStop = request.getParameter("selectForStop");
		if (selectforStop != null) {
			SwitchOverScheduler sosDTO = new SwitchOverScheduler();
			String[] stopServer = selectforStop.split("~");
			sosDTO.setPhysicalServer(stopServer[0]);
			sosDTO.setManagedServer(stopServer[1]);
			sosDTO.setPort(Long.parseLong(stopServer[2]));
			sosDTO.setStatus(stopServer[3]);
			responseDTO = jobSchedulerService.updateSwitchOverScheduler(sosDTO);
		}

		String selectforStart = request.getParameter("selectForStart");
		if (selectforStart != null) {
			SwitchOverScheduler sosDTO = new SwitchOverScheduler();
			String[] startServer = selectforStart.split("~");
			sosDTO.setPhysicalServer(startServer[0]);
			sosDTO.setManagedServer(startServer[1]);
			sosDTO.setPort(Long.parseLong(startServer[2]));
			sosDTO.setStatus(startServer[3]);
			responseDTO = jobSchedulerService.updateSwitchOverScheduler(sosDTO);
		}

		List<SwitchOverScheduler> schedulerList = jobSchedulerService.getAllServers();
		mav.addObject(CCLPConstants.SCHEDULER, schedulerList);

		if (responseDTO != null) {
			if (responseDTO.getResult().equalsIgnoreCase(CCLPConstants.SUCCESS)) {
				mav.addObject(CCLPConstants.STATUS_FLAG, "success");
				logger.info("switch over scheduler configuration updated successfully. Response from config service is "
						+ responseDTO);
			} else {
				mav.addObject(CCLPConstants.STATUS_FLAG, "fail");
				logger.error("Failed to update Switch over Scheduler configuration. Response from config service is "
						+ responseDTO);
			}
			mav.addObject(CCLPConstants.STATUS_MESSAGE, responseDTO.getMessage());
		} else {
			logger.error("Failed to update  Switch over Scheduler configuration. Response from config service is null");
			throw new ServiceException(ResourceBundleUtil.getMessage(ResponseMessages.GENERIC_ERR_MESSAGE));
		}
		return mav;
	}

	@PreAuthorize("hasRole('EDIT_SCHEDULER')")
	@RequestMapping("/jobSchedulerServiceJobs")
	public ModelAndView jobSchedulerServiceJobs() throws ServiceException {

		ModelAndView mav = new ModelAndView("jobSchedulerServiceJobs");

		List<List<Object>> schedulerServiceJobs = jobSchedulerService.getRunningJobs();
		mav.addObject("schedulerServiceJobs", schedulerServiceJobs);
		mav.addObject("msSchedRunFlag", "Running");

		return mav;
	}
}
