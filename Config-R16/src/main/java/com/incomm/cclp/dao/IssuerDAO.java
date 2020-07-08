/**
 * 
 */
package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.Issuer;

/**
 * @author abutani
 *
 */
public interface IssuerDAO {

	public void createIssuer(Issuer issuer);
	
	public List<Issuer> getAllIssuers();
	
	public void updateIssuer(Issuer issuer);
	
	public void deleteIssuer(Issuer issuer);
	
	public Issuer getIssuerById(long issuerId);
	
	public List<Issuer> getIssuersByName(String issuerName);
	
	public void deleteIssuerById(long issuer);
	
	public int  countAndDeleteIssuerById(long issuer);
	
	public int  countOfCardRangeById(long issuer);
	public int  countOfIssuer(long issuer);	

}
