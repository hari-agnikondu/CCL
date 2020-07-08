/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.PurseDAO;
import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.ProductPurse;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.dto.CurrencyCodeDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.dto.PurseTypeEnum;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.PurseService;

/**
 * PurseServiceImpl implements the PurseService to provide 
 * necessary Purse related operations.
 * 
 * @author abutani
 *
 */
@Service
public class PurseServiceImpl implements PurseService {

	// the purse dao.
	@Autowired
	PurseDAO purseDao;

	private  final Logger logger = LogManager.getLogger(this.getClass());


	/**
	 * Gets all purses.
	 * 
	 * @return the list of all purses.
	 */
	public List<PurseDTO> getAllPurses() {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getAllPurses");
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new 
				TypeToken<List<PurseDTO>>() {}.getType();
				logger.info(CCLPConstants.EXIT);
				return mm.map(purseDao.getAllPurses(), 
						targetListType);
	}

	/**
	 * Getting whole purse by currency or UPC code
	 * @throws ServiceException 
	 * 
	 */
	@Override
	public List<PurseDTO> getPursesByCurrencyAndUpcCode(String currencyCode,String upc) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		List<PurseDTO> purseDtoList=null;

		List<Purse> purseList=purseDao.getPursesByCurrencyAndUpcCode(currencyCode,upc);
		if(purseList!=null){
			purseDtoList= new ModelMapper().map(purseList,new TypeToken<List<PurseDTO>>() {}.getType());
		}
		logger.info(CCLPConstants.EXIT);
		return purseDtoList;

	}

	/**
	 * To get the purse by purseId
	 */
	@Override
	public PurseDTO getPurseById(Long purseId) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		Purse purse=purseDao.getPurseById(purseId);
		if(purse==null){
			logger.error("Purse is null");
			throw new ServiceException(ResponseMessages.ERR_PURSE_NOT_EXIST);
		}
		logger.info(CCLPConstants.EXIT);
		return new ModelMapper().map(purse, PurseDTO.class);

	}

	/**
	 * TO update the purse details 
	 */

	@Override
	public void updatePurseDetails(PurseDTO purseDTO) throws ServiceException
	{
		logger.info(CCLPConstants.ENTER);
		Purse purse=new ModelMapper().map(purseDTO, Purse.class);

		getPurseById(purse.getPurseId());
		if(purseDTO.getCurrencyTypeID()!=null && !purseDTO.getCurrencyTypeID().isEmpty() && !"-1".equals(purseDTO.getCurrencyTypeID())){ 
			purse.setCurrencyCode(getCurrencyEntity(purseDTO));
		}
		purseDao.updatePurseDetails(purse);
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * TO delete the purse based on purse id
	 */
	@Override
	public void deletePurseDetails(Long purseId) throws ServiceException
	{
		logger.info(CCLPConstants.ENTER);
		Purse purse=new Purse();
		purse.setPurseId(purseId);

		List<ProductPurse>  productpurse= purseDao.getProductByPurseId(purseId);
		

		if(productpurse != null && !productpurse.isEmpty())
		{
			throw new ServiceException(ResponseMessages.ERR_PRODUCT_PURSE_MAPPING);
		}
		else if(purseDao.getPartnerByPurseId(purseId) > 0) {
			throw new ServiceException(ResponseMessages.ERR_PARTNER_PURSE_MAPPING);
		}
		else
		{
			purseDao.deletePurseDetails(purse);
		}
		logger.info(CCLPConstants.EXIT);
	}
	/**
	 * To set the currencyCode value from purseDto
	 * @param purseDTO
	 * @return CurrencyCode
	 */
	public CurrencyCode getCurrencyEntity(PurseDTO purseDTO){
		logger.info(CCLPConstants.ENTER);
		CurrencyCodeDTO currencyCodeDTO=new CurrencyCodeDTO();
		currencyCodeDTO.setCurrencyTypeID(purseDTO.getCurrencyTypeID());
		currencyCodeDTO.setCurrCodeAlpha(purseDTO.getCurrCodeAlpha());
		currencyCodeDTO.setCurrencyDesc(purseDTO.getCurrencyDesc());
		currencyCodeDTO.setMinorUnits(purseDTO.getMinorUnits());
		logger.info(CCLPConstants.EXIT);
		return new ModelMapper().map(currencyCodeDTO, CurrencyCode.class);
	}

	/**
	 * To create the new purse
	 */
	@Override
	@Transactional
	public void createPurse(PurseDTO purseDTO) throws ServiceException
	{
		logger.info(CCLPConstants.ENTER);
		if(purseDTO.getPurseTypeId()==PurseTypeEnum.SKU.getPurseTypeId()){
			List<Purse> purseList=purseDao.getPursesBypurseTypePurseExtID(purseDTO.getPurseTypeId(),"*",purseDTO.getExtPurseId(),purseDTO.getUpc());
			Long purseUpcCnt = purseList.stream().count();
			if(purseUpcCnt>0){
				logger.error("Purse already exits");
				throw new ServiceException(ResponseMessages.ERR_PURSE_EXISTS);
			}
		}
		if(purseDTO.getPurseTypeId()==PurseTypeEnum.POINTS.getPurseTypeId()){
			List<Purse> purseList=purseDao.getPursesBypurseTypePurseExtID(purseDTO.getPurseTypeId(),"*",purseDTO.getExtPurseId(),"*");
			boolean flag= purseList.stream().anyMatch(purse-> purse.getPurseType().getPurseTypeId()==2 );
			if(flag){
				logger.error("Points should be unique");
				throw new ServiceException(ResponseMessages.ERR_LOYALTY_SHOULD_BE_UNIQUE);
			}
		}
		if(purseDTO.getPurseTypeId()==PurseTypeEnum.CONSUMER_FUNDED_CURRENCY.getPurseTypeId() || 
				purseDTO.getPurseTypeId()==PurseTypeEnum.PARTNER_FUNDED_CURRENCY.getPurseTypeId()){
			List<Purse> purseList=purseDao.getPursesBypurseTypePurseExtID(purseDTO.getPurseTypeId(),purseDTO.getCurrencyTypeID(),purseDTO.getExtPurseId(),"*");
			if(purseList!=null && !purseList.isEmpty()){
				logger.error("currency should be unique");
				throw new ServiceException(ResponseMessages.ERR_CURRENCY_SHOULD_BE_UNIQUE);
			}
		}
		if(purseDTO.getExtPurseId()!=null){
			List<Purse> purseList=purseDao.getPurseByExtPurseId(purseDTO.getExtPurseId());
			if(purseList!=null && !purseList.isEmpty()){
				logger.error("Ext Purse Id should be unique");
				throw new ServiceException(ResponseMessages.ERR_EXTPURSEID_EXISTS);
			}
		}

		Purse purse = new ModelMapper().map(purseDTO, Purse.class);
		if(purseDTO.getCurrencyTypeID()!=null && !purseDTO.getCurrencyTypeID().isEmpty() && !"-1".equals(purseDTO.getCurrencyTypeID())){ 
			purse.setCurrencyCode(getCurrencyEntity(purseDTO));
		}
		purseDao.createPurse(purse); 
		purseDTO.setPurseId(purse.getPurseId());
		logger.info(CCLPConstants.EXIT);
	}


	public List<String> getPurseByIds(List<Long> purseIds) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getPurseByIds");

		logger.info(CCLPConstants.EXIT);
		return purseDao.getPurseByIds(purseIds);
	}

	public List<PurseDTO> getPursesBypurseTypePurseExtID(Long purseType,String currencyCode,String purseIdExt) {
		logger.info(CCLPConstants.ENTER);
		List<PurseDTO> purseDtoList=null;

		List<Purse> purseList=purseDao.getPursesBypurseTypePurseExtID(purseType,currencyCode,purseIdExt,"*");
		if(purseList!=null){
			purseDtoList= new ModelMapper().map(purseList,new TypeToken<List<PurseDTO>>() {}.getType());
		}
		logger.info(CCLPConstants.EXIT);
		return purseDtoList;
	}

	
}
