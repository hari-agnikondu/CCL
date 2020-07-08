package com.incomm.scheduler.tasklet;
 
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.incomm.scheduler.constants.CCLPConstants;
import com.incomm.scheduler.dao.CCFGenerationDAO;
import com.incomm.scheduler.exception.ServiceException;
import com.incomm.scheduler.service.CcfGenerationService;
import com.incomm.scheduler.utils.HSMCommandBuilder;
import com.incomm.scheduler.utils.JobConstants;
 
@Component
//@PropertySource("classpath:application.properties")
public class InitialRetailCCFGenerationTasklet {
 
	private static final Logger logger = LogManager.getLogger(InitialRetailCCFGenerationTasklet.class);
	
    HSMCommandBuilder hsmCommandBuilder = new HSMCommandBuilder();
    
    @Autowired
    CCFGenerationDAO ccfGenerationDao;
    
    @Autowired
    CcfGenerationService ccfGenerationService;
    
    public void setCCFGenerationDAOObj(CCFGenerationDAO ccfGenerationDao){
    	 this.ccfGenerationDao = ccfGenerationDao;
    }
 
    String ccfGenerationVersion = null;
 
	public void generateCcfFile(String orderType) throws IOException, ServiceException, ParseException {

		logger.info(CCLPConstants.ENTER);
		Map<String, Object> valueObj = new HashMap<>();

		String insUser = "1";
		List<String> orderList = new ArrayList<>();

		valueObj.put("orderType", orderType);
		List<String> orders = ccfGenerationDao.getAllOrdersToGenerateCCF(orderType);
		logger.debug("*all the reatil orders to generate CCF :" + orders);
		if (orders == null) {
			logger.debug(JobConstants.NO_ORDER_TO_PROCESS);
			throw new ServiceException(JobConstants.NO_ORDER_TO_PROCESS);
		}
		Iterator<?> iterator = orders.iterator();
		while (iterator.hasNext()) {
			Map<?, ?> map = (Map<?, ?>) iterator.next();
			String order = String.valueOf(map.get(JobConstants.CCF_ORDER_ID));
			orderList.add(order);

		}
		String[] orderArray = new String[orderList.size()];
		orderArray = orderList.toArray(orderArray);
		ccfGenerationService.generateCcfFile(orderArray, insUser);
		
		logger.info(CCLPConstants.EXIT);
	}
 
    
}