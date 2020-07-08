package com.incomm.scheduler.tasklet;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.incomm.scheduler.dao.CardNumberInventoryDAO;
import com.incomm.scheduler.model.CardRange;


@Component
public class InitialCardNumberInventoryGeneration {
	 
	 @Autowired
	 CardNumberInventoryDAO cardInvtryDao;
	
	 private static final Logger logger = LogManager.getLogger(InitialCardNumberInventoryGeneration.class);
	 
	 public void setCardNumInventoryGenDaoObj(CardNumberInventoryDAO cardInvtryDao) {
			
			this.cardInvtryDao = cardInvtryDao;
	}
	 
	public void intiateBatchCardNumGeneration() {
		
		List<CardRange> cardInvntryDtls = cardInvtryDao.getCardInvtryDtlsByScheduler();
		logger.debug("Entered in to  initiateBatchCardNumGeneration");
		if(!CollectionUtils.isEmpty(cardInvntryDtls)) {
			
			Iterator<CardRange> iterator = cardInvntryDtls.iterator();
			while (iterator.hasNext()) {

				CardRange cardRange = iterator.next();
				
				if (Objects.isNull(cardRange.getCardRangeInventory())) {
					cardInvtryDao.initiateCardNumberGeneration(cardRange.getCardRangeId());
				} else {
					if (!"COMPLETED".equalsIgnoreCase(cardRange.getCardRangeInventory().getInventoryStatus())
							&& !"STARTED".equalsIgnoreCase(cardRange.getCardRangeInventory().getInventoryStatus())) {
						logger.info("checking the status of cardRangeInventory in initiateBatchCardNumGeneration");
						cardInvtryDao.initiateCardNumberGeneration(cardRange.getCardRangeId());
						
						
					}
				}
				
				
			}
		}
		logger.debug("Exited from initiateBatchCardNumGeneration");
	}

}
