/**
 * 
 */
package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.exception.ServiceException;

/**
 * @author abutani
 *
 */
public interface IssuerService {
	
	public void createIssuer(IssuerDTO issuerDto) throws ServiceException;
	
	public List<IssuerDTO> getAllIssuers();
	
	public void updateIssuer(IssuerDTO issuerDto) throws ServiceException;
	
	public void deleteIssuer(IssuerDTO issuerDto);
	
	public IssuerDTO getIssuerById(long issuerId);
	
	public List<IssuerDTO> getIssuersByName(String issuerName);
	
	public void deleteIssuerById(long issuerDto);
	
	public int countAndDeleteIssuerById(long issuerDto);
	
	public int countOfCardRangeById(long issuerDto);
	
	public int countOfIssuer(long issuerDto);

}
