/**
 * 
 */
package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.PackageDefinition;
import com.incomm.cclp.exception.ServiceException;

public interface PackageDAO {
	
	public PackageDefinition getPackageDefinitionById(String packageDefinitionId);

	public List<PackageDefinition> getAllPackages();

	public List<PackageDefinition> getAllPackagesByName(String packageName);

	public List<Object[]> getPackageIdList() throws ServiceException;

	public List<Object[]> getfulFillmentList() throws ServiceException;

	public void createPackage(PackageDefinition packageDefinition) throws ServiceException;

	public void updatePackage(PackageDefinition packageDefinition) throws ServiceException;

	public void deletePackage(PackageDefinition packageDefinition) throws ServiceException;

	public PackageDefinition getPackageIdDtls(String packageId) throws ServiceException;

	public List<Object[]> getPackageAttributes(String packageId) throws ServiceException;
	
	public int checkduplicatePackageId(String packageId) throws ServiceException;
	
	public List<String> getPackageByIds(List<String> packageId);
}
