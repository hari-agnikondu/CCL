package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.ProductPurse;

import com.incomm.cclp.domain.Purse;

/**
 * @author abutani
 *
 */
public interface PurseDAO {

	public List<Purse> getAllPurses();

	public Purse getPurseById(Long purseId);

	public List<Purse> getPursesByCurrencyAndUpcCode(String currencyCode, String upc);

	public void updatePurseDetails(Purse purse);

	public void deletePurseDetails(Purse purse);

	public void createPurse(Purse purse);

	public List<ProductPurse> getProductByPurseId(Long purseId);

	List<String> getPurseByIds(List<Long> purseIds);

	public CurrencyCode getCurrencyByID(String currencyCode);
	
	public List<Purse> getPurseByExtPurseId(String extPurseId);

	public List<Purse> getPursesBypurseTypePurseExtID(Long purseType, String currencyCode, String purseIdExt,String upc);

	public int getPartnerByPurseId(Long purseId);
}
