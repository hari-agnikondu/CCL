/**
 * 
 */
package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.exception.ServiceException;

public interface PurseService {
	
	public List<PurseDTO> getAllPurses();
	
	public List<PurseDTO> getPursesByCurrencyAndUpcCode(String currencyCode,String upc) throws ServiceException;
	
	public PurseDTO getPurseById(Long purseId) throws ServiceException;
	
	public void updatePurseDetails(PurseDTO purseDTO) throws ServiceException;

	public void createPurse(PurseDTO purseDTO)throws ServiceException;

	void deletePurseDetails(Long purseId) throws ServiceException;

	public List<String> getPurseByIds(List<Long> purseIds);
	
	public List<PurseDTO> getPursesBypurseTypePurseExtID(Long purseType,String currencyCode,String purseIdExt);

}
