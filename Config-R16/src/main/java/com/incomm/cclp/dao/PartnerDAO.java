package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.CurrencyCode;
import com.incomm.cclp.domain.Partner;
import com.incomm.cclp.domain.Purse;
import com.incomm.cclp.exception.ServiceException;

public interface PartnerDAO {

	void createPartner(Partner partner);

	public List<Partner> getAllPartners();

	void updatePartner(Partner partner);

	public Partner getPartnerById(Long partnerId) ;

	public List<Partner> getPartnerByName(String partnerName);

	public int deletePartner(long partnerId) throws ServiceException;

	public int partnerLinkCheck(long partnerId);


	CurrencyCode getCurrencyCodeById(String currencyId);

	public Purse getPurseById(String purseId);
	List<String> getAttachedCurrencyList(long partnerId);
	List<String> getAttachedPurseList(long partnerId);

	public int deletePartnerPurse(long partnerId) throws ServiceException;

	public int deletePartnerProg(long partnerId) throws ServiceException;

	public int deletePartnerCurr(long partnerId) throws ServiceException;
	
}
