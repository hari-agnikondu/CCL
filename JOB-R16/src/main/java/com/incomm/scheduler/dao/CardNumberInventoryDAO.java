package com.incomm.scheduler.dao;

import java.util.List;

import com.incomm.scheduler.model.CardRange;
import com.incomm.scheduler.model.CardRangeInventory;

public interface CardNumberInventoryDAO {

	public List<CardRange> getCardInvtryDtls();
	
	public List<CardRange> getCardInvtryDtlsByScheduler();

	public CardRange getCardRangeById(Long cardRangeId);

	public int initiateCardNumberGeneration(Long cardRangeId);

	public CardRangeInventory getCardRangeInventoryByCardRange(Long cardRangeId);

	public int pauseCardNumberProcessByCardRangeId(Long cardRangeId);
	
	

}
