/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.IssuerDAO;
import com.incomm.cclp.domain.Issuer;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.IssuerService;

/**
 * IssuerServiceImpl implements the IssuerService to provide 
 * necessary Issuer related operations.
 * 
 * @author abutani
 *
 */
@Service
public class IssuerServiceImpl implements IssuerService {
	
	// the Issuer dao.
	@Autowired
	IssuerDAO issuerDao;

	private  final Logger logger = LogManager.getLogger(this.getClass());
	
	/**
	 * Create an issuer.
	 * 
	 * @param issuerDto The IssuerDTO to be created.
	 * @throws ServiceException 
	 */
	@Override
	@Transactional
	public void createIssuer(IssuerDTO issuerDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info(issuerDto.toString());
		// Make sure issuer does not already exist
		List<IssuerDTO> issuers = getIssuersByName(issuerDto.getIssuerName());
		List<IssuerDTO> existingIssuers = issuers.stream().filter(issuer -> 
		issuer.getIssuerName().equalsIgnoreCase(issuerDto.getIssuerName())).collect(Collectors.toList());
		if (existingIssuers != null && !existingIssuers.isEmpty())
		{
			logger.error("Issuer already exists");
			throw new ServiceException(
					"ISS_"+ResponseMessages.ALREADY_EXISTS);
		}
		
		
		 ModelMapper mm = new ModelMapper();
		 Issuer issuer = mm.map(issuerDto, Issuer.class);		 
		 issuerDao.createIssuer(issuer);
		 logger.info("Record created for :"+issuer.getIssuerId());
		 issuerDto.setIssuerId(issuer.getIssuerId());
		 logger.info(CCLPConstants.EXIT);
	}
	
	
	/**
	 * Gets all Issuers.
	 * 
	 * @return the list of all Issuers.
	 */
	@Override
	public List<IssuerDTO> getAllIssuers() {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getAllIssuers");
		ModelMapper mm = new ModelMapper();
	    java.lang.reflect.Type targetListType = new 
	    		TypeToken<List<IssuerDTO>>() {}.getType();
	    logger.info(CCLPConstants.EXIT);
	    return mm.map(issuerDao.getAllIssuers(), 
	    		targetListType);
	}
	
	
	/**
	 * Updates an issuer.
	 * 
	 * @param issuerDto The IssuerDTO to be updated.
	 */
	@Override
	public void updateIssuer(IssuerDTO issuerDto) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside updateIssuer with data :"+issuerDto.toString());
	
		IssuerDTO issuerloc= null;
		List<IssuerDTO> existingIssuers = getIssuersByName(issuerDto.getIssuerName());
        if (existingIssuers != null && !existingIssuers.isEmpty()) {
            Iterator<IssuerDTO> iterator = existingIssuers.iterator();
            while(iterator.hasNext())
            {
            	issuerloc = iterator.next();
                if(issuerloc.getIssuerName().equalsIgnoreCase(issuerDto.getIssuerName())
                		&& (issuerloc.getIssuerId()!=issuerDto.getIssuerId()) )
                {
                	
                     
                	throw new ServiceException("ISS_"+ResponseMessages.ALREADY_EXISTS);
                }
                    
            }
            
        }
       
        ModelMapper mm = new ModelMapper();
        Issuer issuer = mm.map(issuerDto, Issuer.class);
		issuerDao.updateIssuer(issuer);
		logger.info("after updating data for :"+issuer.getIssuerId());
		logger.info(CCLPConstants.EXIT);
		
	}

	
	/**
	 * Deletes an issuer.
	 * 
	 * @param issuerDto The IssuerDTO to be deleted.
	 */
	@Override
	public void deleteIssuer(IssuerDTO issuerDto) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside deleteIssuer with data :", issuerDto.toString());
		 ModelMapper mm = new ModelMapper();
		 Issuer issuer = mm.map(issuerDto, Issuer.class);
		issuerDao.deleteIssuer(issuer);
		logger.info("after deleting data for :", issuer.getIssuerId());
		logger.info(CCLPConstants.EXIT);
	}
	
	
	/**
	 * Gets an Issuer by ID.
	 * 
	 * @param issuerId The issuer id for the Issuer to be retrieved.
	 * 
	 * @return the IssuerDTO.
	 */
	@Override
	public IssuerDTO getIssuerById(long issuerId) {
		logger.info(CCLPConstants.ENTER);
		ModelMapper mm = new ModelMapper();
		logger.info("inside getIssuerById with data : ", String.valueOf(issuerId));
		Issuer issuer = issuerDao.getIssuerById(issuerId);
		if(issuer!=null) {
		
		logger.info("after retrieving data for IssuerId : ", issuer.getIssuerId());
		}else {
			issuer=new Issuer();
		}
		logger.info(CCLPConstants.EXIT);
		return mm.map(issuer, IssuerDTO.class);
	}

	
	/**
	 * Gets an issuer by name.
	 * 
	 * @param issuerName The issuer name for the Issuer to be retrieved.
	 * The issuerName parameter can be a partial or complete name.
	 * 
	 * @return the list of all IssuerDTOs for the given name.
	 */
	@Override
	public List<IssuerDTO> getIssuersByName(String issuerName) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getIssuersByName with data : "+issuerName);
		ModelMapper mm = new ModelMapper();		
	    java.lang.reflect.Type targetListType = new 
	    		TypeToken<List<IssuerDTO>>() {}.getType();
	   logger.info("after retrieving data for IssuerName : "+issuerName);
	   logger.info(CCLPConstants.EXIT);
	    return mm.map(issuerDao.getIssuersByName(issuerName), 
	    		targetListType);

	}


	@Override
	public void deleteIssuerById(long issuerDto) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside deleteIssuerById with data : ", String.valueOf(issuerDto));
		issuerDao.deleteIssuerById(issuerDto);
		logger.info("end of deleteIssuerById with data : ", String.valueOf(issuerDto));
		logger.info(CCLPConstants.EXIT);
	}
	
	
	@Override
	public int countAndDeleteIssuerById(long issuerDto) {
		logger.info("ENTER");
		logger.info("inside countAndDeleteIssuerById with data : ", String.valueOf(issuerDto));
		int count=0;
		count=issuerDao.countAndDeleteIssuerById(issuerDto);
		logger.info("end of countAndDeleteIssuerById with count : "+count);
		logger.info(CCLPConstants.EXIT);
		return count;
	}
	
	
	@Override
	public int countOfCardRangeById(long issuerDto) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside countAndDeleteIssuerById with data : ", String.valueOf(issuerDto));
		int count=0;
		count=issuerDao.countOfCardRangeById(issuerDto);
		logger.info("end of countAndDeleteIssuerById with count : ", count);
		logger.info(CCLPConstants.EXIT);
		return count;
	}


	@Override
	public int countOfIssuer(long issuerDto) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside countOfIssuer with data : ", String.valueOf(issuerDto));
		int count=0;
		count=issuerDao.countOfIssuer(issuerDto);
		logger.info("end of countOfIssuer with count : "+count);
		logger.info(CCLPConstants.EXIT);
		return count;
	}
	

}
