package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.config.model.CardRange;
import com.incomm.cclpvms.config.model.CardRangeDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface CardRangeService {
	
	public ResponseDTO addCardRange(CardRange CardRange) throws ServiceException ;

	public ResponseEntity<ResponseDTO> updateCardRange(CardRange CardRange) throws ServiceException ;

	public Map<Long,String> getAllIssuers() throws ServiceException;

	public List<CardRangeDTO> getCardRange(CardRange CardRange)throws ServiceException;

	public CardRange getCardRangeById(String cardRangeId) throws ServiceException;

	public ResponseEntity<ResponseDTO> deleteCardRange(long cardRangeId, String issuerName) throws ServiceException;

	public ResponseEntity<ResponseDTO> changeCardRangeStatus(CardRange CardRange) throws ServiceException;
	
	/*added by nawaz for drop down*/
	public List<String> getAllCardRanges()throws ServiceException;

	public List<CardRangeDTO> getCardRangeByIssuerId(Long issuerId) throws ServiceException;
	/*added by nawaz for drop down*/
	
	
	public List<String> getCardRangebyCardId(List<Long> cardRangeId)throws ServiceException;

}
