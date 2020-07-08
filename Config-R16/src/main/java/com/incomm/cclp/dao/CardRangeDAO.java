package com.incomm.cclp.dao;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.domain.CardRange;
import com.incomm.cclp.domain.Issuer;

public interface CardRangeDAO {

	public void createCardRange(CardRange cardRange);
	
	public List<CardRange> getCardRanges();
	
	public List<CardRange> getCardRangeByIssuerNameAndPrefix(String issuerName,String prefix);
	
	public void updateCardRange(CardRange cardRange);
	
	public void deleteCardRange(CardRange cardRange);
	
	public List<Issuer> getIssuers();
	
	public CardRange getCardRangeById(String cardRangeId);

	public int changeCardRangeStatus(long cardRangeId,String newStatus,String checkerDesc,long lastUpdUser);
	
	public boolean checkCardRangeAvail(String startRange,String endRange,String checkDigit);
	
	public List<CardRange> getCardRangesByIssuerId(Long issuerId);

	public CardRange getExistCardRanges(CardRange cardRange);
	
	@SuppressWarnings("rawtypes")
	public List<Map> getAllCardRanges();
	
	public List<String> getCardRangeDataById(List<Long> cardRangeId);
	
	
}
