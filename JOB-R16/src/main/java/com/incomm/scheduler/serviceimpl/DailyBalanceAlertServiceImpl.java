package com.incomm.scheduler.serviceimpl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.incomm.scheduler.dao.DailyBalanceAlertDAO;
import com.incomm.scheduler.model.DailyBalance;
import com.incomm.scheduler.service.DailyBalanceAlertService;
import com.incomm.scheduler.utils.JobConstants;
import com.incomm.scheduler.utils.Util;

@Service
public class DailyBalanceAlertServiceImpl implements DailyBalanceAlertService {

	private static final Logger logger = LogManager.getLogger(DailyBalanceAlertServiceImpl.class);

	@Value("${NOTIFICATION_SERVICE_URL}") 
	private String notificationUrl;


	@Autowired
	RestTemplate restTemplate;

	@Autowired
	DailyBalanceAlertDAO dailyBalanceDao;
	/*
	 * TO execute the procedure and invoke the rest end point
	 * 
	 * @see com.incomm.scheduler.service.DailyBalanceAlertService#dailyBalanceAlert()
	 */

	@Override
	public String dailyBalanceAlert() {

		String response = "Failure";
		try{
			Map<String,String> valueObj = new HashMap<>();
			response =  dailyBalanceDao.callProcedure(1l);

			if("Ok".equals(response)){

				List<DailyBalance> optedList = dailyBalanceDao.getDailyBalanceDetails();

				for(DailyBalance db:optedList)
				{
					valueObj.put(JobConstants.CARD_NUMBER, db.getCardNumber());
					valueObj.put(JobConstants.PRODUCT_ID, db.getProductId());
					valueObj.put(JobConstants.ACCOUNT_ID, db.getAccountId());
					valueObj.put(JobConstants.CARD_HASH, db.getCardHash());
					valueObj.put(JobConstants.CURRENCY_CODE, db.getCurrencyCode());

					valueObj.put(JobConstants.RESPONSE_CODE, "R0001");
					if("N".equals(db.getProcessStatus())){
						response = callNotificationService(valueObj);
					}

				}

			}else{
				logger.info("Daily balance procedure got an error"+response);
			}

		}catch(Exception e){
			response=e.getCause().getMessage();
			logger.error(e);
		}
		logger.info("response***********"+response);

		return response;
	}
	
	/*
	 * To invokcation service 
	 * @param valueObj
	 * 
	 */

	@Async
	public String callNotificationService(Map<String,String> valueObj){

		String response = "";

		try {
			
			logger.info("*****notification_url**"+notificationUrl);

			valueObj.put(JobConstants.ALERT_ID, "17");
			valueObj.put(JobConstants.DELIVERY_CHNL, "");
			valueObj.put(JobConstants.TRAN_CDE, "");

			response=restTemplate.postForObject(notificationUrl , valueObj, String.class);

			Map<String,String> resultMap=Util.stringToMap(response);
			logger.debug("Message Sent");
			logger.debug("Response from NotificationService" +response+"resultMap"+resultMap);

			if("Success".equals(resultMap.get(JobConstants.NOTIFICATION_EMAILSTATUS)) || "Success".equals(resultMap.get(JobConstants.NOTIFICATION_SMSSTATUS))){
			
				valueObj.put("process_msg", "Y");
			}else{
				valueObj.put("process_msg", "N");
			}

			dailyBalanceDao.updateDailyBalalertmsg(valueObj);
		} 
		catch (RestClientException e) {
			logger.error("RestClientException in callNotificationService:"+e);
			response="Fail";
		}
		catch (Exception e) {
			logger.error("Exception in callNotificationService:"+e);
			response="Fail";
		}

		return response;
	}


}