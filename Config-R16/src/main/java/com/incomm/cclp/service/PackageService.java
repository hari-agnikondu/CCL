package com.incomm.cclp.service;

import java.util.List;

import com.incomm.cclp.dto.PackageDTO;
import com.incomm.cclp.exception.ServiceException;

public interface PackageService {

	public List<PackageDTO> getAllPackages();

	public List<PackageDTO> getAllPackagesByName(String description);

	public List<Object[]> getPackageIdList() throws ServiceException;

	public List<Object[]> getfulFillmentList() throws ServiceException;

	public void createPackage(PackageDTO packageDTO) throws ServiceException;

	public void updatePackage(PackageDTO packageDTO) throws ServiceException;

	public void deletePackage(PackageDTO packageDTO) throws ServiceException;

	public PackageDTO getPackageIdDtls(String packageId) throws ServiceException;
	
	public int checkduplicatePackageId(String packageId) throws ServiceException;
	
	public List<String> getPackageByIds(List<String> packageId)throws ServiceException;
}
