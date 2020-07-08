package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclpvms.config.model.PackageDTO;
import com.incomm.cclpvms.config.model.PackageID;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;


public interface PackageService {
	
	public List<String> getPackageByIds(List<String> packageId)throws ServiceException;	
	
	public List<PackageDTO> getAllPackages() throws ServiceException;
	
	public List<PackageDTO> getAllPackagesByName(String description) throws ServiceException;

	public Map<String, String> getShipmentAttList() throws ServiceException;

	public Map<String, String> getfulFillmentList() throws ServiceException;

	public Map<String, String> getPackageIdList() throws ServiceException;

	public ResponseDTO createPackage(PackageID packageDtls) throws ServiceException;

	public ResponseDTO udpatePackage(PackageID packageDtls) throws ServiceException;

	public ResponseDTO deletePackage(PackageID packageDtls) throws ServiceException;
	
	public PackageID getPackageId(String packageId) throws ServiceException;
	
}
