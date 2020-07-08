/**
 * 
 */
package com.incomm.cclp.service;


import java.util.List;
import java.util.Map;

import com.incomm.cclp.domain.CardRange;
import com.incomm.cclp.dto.CardRangeDTO;
import com.incomm.cclp.exception.ServiceException;

public interface CardRangeService {
	
	public void createCardRange(CardRangeDTO cardRangeDTO) throws ServiceException;
	
	public List<CardRangeDTO> getCardRanges();
	
	public List<CardRangeDTO> getCardRangeByIssuerNameAndPrefix(String issuerName,String prefix);
	
	public void updateCardRange(CardRangeDTO cardRangeDTO) throws ServiceException;
	
	public void deleteCardRange(CardRangeDTO cardRangeDTO);
	
	public Map<Long, String> getIssuers();
	
	public CardRangeDTO getCardRangeById(String cardRangeId);
	
	public String changeCardRangeStatus(long cardRangeId,String newStatus,String checkerDesc, long lastUpdUser)throws ServiceException;
	
	public List<CardRangeDTO> getCardRangesByIssuerId(Long cardRangeId);
	
	public CardRange getExistCardRanges(CardRangeDTO cardRangeDTO) ;

	@SuppressWarnings("rawtypes")
	public List<Map> getAllCardRanges();
	
	public List<String> getCardRangeDataById(List<Long> cardRangeId);

}
