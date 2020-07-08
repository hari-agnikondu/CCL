/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.CardRangeDAO;
import com.incomm.cclp.domain.CardRange;
import com.incomm.cclp.domain.ClpUser;
import com.incomm.cclp.domain.Issuer;
import com.incomm.cclp.dto.CardRangeDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.CardRangeService;
import com.incomm.cclp.util.Util;

/**
 * CardRangeServiceImpl implements the CardRangeService to provide 
 * necessary card range related operations.
 * 
 */
@Service
public class CardRangeServiceImpl implements CardRangeService {

	@Autowired
	CardRangeDAO cardRangeDAO;

	private static final Logger logger = LogManager.getLogger(CardRangeServiceImpl.class);
	/**
	 * Create an CardRange.
	 * Checking the overlap condition for card range before insert
	 * @param cardRangeDTO The CardRangeDTO to be created.
	 * @throws ServiceException 
	 */
	@Override
	@Transactional
	public void createCardRange(CardRangeDTO cardRangeDTO) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		boolean flag=checkCardRangeAvail(cardRangeDTO.getPrefix()+cardRangeDTO.getStartCardNbr(),cardRangeDTO.getPrefix()+cardRangeDTO.getEndCardNbr(),cardRangeDTO.getIsCheckDigitRequired());

		if(!flag){
			logger.error("Card range already exists");
			throw new ServiceException(
					ResponseMessages.ERR_CARDRANGE_EXISTS);
		}
		if(Util.isEmpty(cardRangeDTO.getStatus())){
			cardRangeDTO.setStatus("NEW");
		}
		ModelMapper mm = new ModelMapper();
		CardRange cardRange = mm.map(cardRangeDTO, CardRange.class);
		
		ClpUser user = new ClpUser();
		
		user.setUserId(cardRangeDTO.getInsUser());
		
		cardRange.setInsUpdclpUser(user);
		
		ClpUser updUser = new ClpUser();
		
		updUser.setUserId(cardRangeDTO.getLastUpdUser());
		
		cardRange.setLastUpdclpUser(updUser);
		logger.info("Creating card range..");
		cardRangeDAO.createCardRange(cardRange);
		cardRangeDTO.setCardRangeId(cardRange.getCardRangeId());
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * Update an CardRange.
	 * @param cardRangeDTO The CardRangeDTO to be created.
	 * @throws ServiceException 
	 */
	@Override
	@Transactional
	public void updateCardRange(CardRangeDTO cardRangeDTO) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		boolean changeFlag=false;
		CardRangeDTO existingCardRange=getCardRangeById(String.valueOf(cardRangeDTO.getCardRangeId()));
		if(!(existingCardRange.getPrefix().concat(existingCardRange.getStartCardNbr()).equals(cardRangeDTO.getPrefix().concat(cardRangeDTO.getStartCardNbr()))) &&
		!(existingCardRange.getPrefix().concat(existingCardRange.getEndCardNbr()).equals(cardRangeDTO.getPrefix().concat(cardRangeDTO.getEndCardNbr())))){
			changeFlag=true;
		}
		logger.debug("changeflag: {}",changeFlag);
		if(changeFlag){
			boolean flag=checkCardRangeAvail(cardRangeDTO.getPrefix()+cardRangeDTO.getStartCardNbr(),cardRangeDTO.getPrefix()+cardRangeDTO.getEndCardNbr(),cardRangeDTO.getIsCheckDigitRequired());
			if(!flag){
				logger.error("Card range exists");
				throw new ServiceException(
						ResponseMessages.ERR_CARDRANGE_EXISTS);
			}	
		}

		ModelMapper mm = new ModelMapper();
		CardRange cardRange = mm.map(cardRangeDTO, CardRange.class);

		ClpUser user = new ClpUser();
		user.setUserId(cardRangeDTO.getLastUpdUser());
		
		cardRange.setLastUpdclpUser(user);
		logger.info("updating card range..");
		cardRangeDAO.updateCardRange(cardRange);
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * Delete CardRange.
	 * 
	 * @param cardRangeDTO The CardRangeDTO to be created.
	 * @throws ServiceException 
	 */
	@Override
	public void deleteCardRange(CardRangeDTO cardRangeDTO) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		CardRange cardRange = mm.map(cardRangeDTO, CardRange.class);
		logger.info("deleting card range..");
		cardRangeDAO.deleteCardRange(cardRange);
		logger.info(CCLPConstants.EXIT);
	}
	/**
	 * Update an CardRange status.
	 * @param  cardRangeId, newStatus, checkerDesc
	 * @throws ServiceException 
	 */
	@Override
	public String changeCardRangeStatus(long cardRangeId,String newStatus,String checkerDesc, long lastUpdUser)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		String respMsg="";
		int updatedCnt=cardRangeDAO.changeCardRangeStatus(cardRangeId, newStatus,checkerDesc,lastUpdUser);
		if("APPROVED".equals(newStatus)){
			if(updatedCnt==0){
				logger.error("Error while updating card range status");
				throw new ServiceException(
						ResponseMessages.ERR_CARDRANGE_APPROVED);
			}	
			else{
				logger.info("Card range approved");
				respMsg=ResponseMessages.SUCCESS_CARDRANGE_APPROVED;
			}
		}
		else{
			if(updatedCnt==0){
				logger.error("Error while updating card range status");
				throw new ServiceException(
						ResponseMessages.ERR_CARDRANGE_REJECTED);
			}
			else{
				logger.info("card range rejected");
				respMsg=ResponseMessages.SUCCESS_CARDRANGE_REJECTED;
			}
		}
		logger.info(CCLPConstants.EXIT);
		return respMsg;
	}

	/**
	 * Converting CardRange Entity list to CardRangeDTO list
	 * @param  cardRangeId, newStatus, checkerDesc
	 */
	public List<CardRangeDTO> toCardRangeDTO(List<CardRange> cardRangeList){
		logger.info(CCLPConstants.ENTER);
		List<CardRangeDTO> cardRangeDtoList=new ArrayList<>();
		if(cardRangeList!=null){
			cardRangeDtoList = cardRangeList.stream()
					.map(cardRange -> new CardRangeDTO(cardRange.getCardRangeId(), cardRange.getPrefix(),cardRange.getStartCardNbr(),
							cardRange.getEndCardNbr(), cardRange.getCardLength(), cardRange.getNetwork(),
							cardRange.getIsCheckDigitRequired(),
							cardRange.getIssuer()!=null?cardRange.getIssuer().getIssuerName():null,
									cardRange.getIssuer()!=null?cardRange.getIssuer().getIssuerId():null,cardRange.getCardInventory(),cardRange.getStatus(),
											cardRange.getInsUpdclpUser()!=null?cardRange.getInsUpdclpUser().getUserId():null,
													cardRange.getInsUpdclpUser()!=null?cardRange.getInsUpdclpUser().getUserLoginId():null,
															cardRange.getLastUpdclpUser()!=null?cardRange.getLastUpdclpUser().getUserId():null,
																	cardRange.getLastUpdclpUser()!=null?cardRange.getLastUpdclpUser().getUserLoginId():null))
									.collect(Collectors.toList());
		}
		logger.info(CCLPConstants.EXIT);
		return cardRangeDtoList;
	}

	/**
	 * Getting list of CardRangeDTO
	 */
	@Override
	public List<CardRangeDTO> getCardRanges() {
		return toCardRangeDTO(cardRangeDAO.getCardRanges());
	}
	
	/**
	 * Getting list of CardRangeDTO by Issuer Name and Prefix 
	 * @param   issuerName, prefix
	 */
	@Override
	public List<CardRangeDTO> getCardRangeByIssuerNameAndPrefix(String issuerName,String prefix) {

		return toCardRangeDTO(cardRangeDAO.getCardRangeByIssuerNameAndPrefix(issuerName,prefix));
	}


	/**
	 * Getting Map of Issuers 
	 */
	@Override
	public Map<Long, String> getIssuers(){
		logger.info(CCLPConstants.ENTER);
		Map<Long, String> issuerMap=null;
		List<Issuer> issuerList=cardRangeDAO.getIssuers();
		if(issuerList!=null){
			issuerMap = issuerList.stream().collect(Collectors.toMap(Issuer::getIssuerId, Issuer::getIssuerName));
		}
		logger.info(CCLPConstants.EXIT);
		return issuerMap;
	}

	/**
	 * Getting CardRangeDTO by CardRangeId 
	 * @param  cardRangeId
	 */
	@Override
	public CardRangeDTO getCardRangeById(String cardRangeId){
		logger.info(CCLPConstants.ENTER);
		CardRange cardRange=cardRangeDAO.getCardRangeById(cardRangeId);
		logger.info(CCLPConstants.EXIT);
		return new CardRangeDTO(cardRange.getCardRangeId(), cardRange.getPrefix(),cardRange.getStartCardNbr(),
				cardRange.getEndCardNbr(), cardRange.getCardLength(), cardRange.getNetwork(),
				cardRange.getIsCheckDigitRequired(),
				cardRange.getIssuer()!=null?cardRange.getIssuer().getIssuerName():null,
						cardRange.getIssuer()!=null?cardRange.getIssuer().getIssuerId():null,cardRange.getCardInventory(),cardRange.getStatus(),
								cardRange.getInsUpdclpUser()!=null?cardRange.getInsUpdclpUser().getUserId():null,
										cardRange.getInsUpdclpUser()!=null?cardRange.getInsUpdclpUser().getUserLoginId():null,
												cardRange.getLastUpdclpUser()!=null?cardRange.getLastUpdclpUser().getUserId():null,
														cardRange.getLastUpdclpUser()!=null?cardRange.getLastUpdclpUser().getUserLoginId():null);
	}

	/**
	 * Checking the overlap condition for card range
	 * @param   startRange, endRange
	 */
	public boolean checkCardRangeAvail(String startRange,String endRange,String checkDigit) {

		return cardRangeDAO.checkCardRangeAvail(startRange, endRange,checkDigit);
	}
	
	
	public List<CardRangeDTO> getCardRangesByIssuerId(Long issuerId){
		
		return toCardRangeDTO(cardRangeDAO.getCardRangesByIssuerId(issuerId));
	}
	
	public CardRange getExistCardRanges(CardRangeDTO cardRangeDTO) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		CardRange cardRange = mm.map(cardRangeDTO, CardRange.class);
		logger.info(CCLPConstants.EXIT);
		return cardRangeDAO.getExistCardRanges(cardRange);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public List<Map> getAllCardRanges() {
		
		return cardRangeDAO.getAllCardRanges();

	}
	
	@Override
	public List<String> getCardRangeDataById(List<Long> cardRangeId) {
		
		return cardRangeDAO.getCardRangeDataById(cardRangeId);

	}

}
