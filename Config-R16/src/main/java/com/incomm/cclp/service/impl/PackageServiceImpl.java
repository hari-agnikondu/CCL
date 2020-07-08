/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.GeneralConstants;
import com.incomm.cclp.dao.PackageDAO;
import com.incomm.cclp.domain.PackageAtrributesId;
import com.incomm.cclp.domain.PackageAttributes;
import com.incomm.cclp.domain.PackageDefinition;
import com.incomm.cclp.dto.PackageDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.PackageService;

@Service
public class PackageServiceImpl implements PackageService {

	@Autowired
	PackageDAO packageDao;

	private final Logger logger = LogManager.getLogger(this.getClass());

	/**
	 * Gets all Package.
	 * 
	 * @return the list of all Package.
	 */
	public List<PackageDTO> getAllPackages() {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getAllPackages");
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<PackageDTO>>() {
		}.getType();
		logger.info(CCLPConstants.EXIT);
		return mm.map(packageDao.getAllPackages(), targetListType);
	}

	public List<PackageDTO> getAllPackagesByName(String description) {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getAllPackagesByName");
		ModelMapper mm = new ModelMapper();
		java.lang.reflect.Type targetListType = new TypeToken<List<PackageDTO>>() {
		}.getType();
		logger.info(CCLPConstants.EXIT);
		return mm.map(packageDao.getAllPackagesByName(description), targetListType);
	}

	public List<Object[]> getPackageIdList() throws ServiceException {
		return packageDao.getPackageIdList();

	}

	public List<Object[]> getfulFillmentList() throws ServiceException {
		return packageDao.getfulFillmentList();

	}

	@Transactional
	public void createPackage(PackageDTO packageDTO) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		packageDao.createPackage(constructBean(packageDTO));
		logger.info(CCLPConstants.EXIT);
	}

	@Transactional
	public void updatePackage(PackageDTO packageDTO) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		packageDao.updatePackage(constructBean(packageDTO));
		logger.info(CCLPConstants.EXIT);
	}

	@Transactional
	public void deletePackage(PackageDTO packageDTO) throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		packageDao.deletePackage(constructBean(packageDTO));
		logger.info(CCLPConstants.EXIT);
	}

	private PackageDefinition constructBean(PackageDTO packageDTO) {
		logger.info(CCLPConstants.ENTER);
		PackageDefinition packageDefinition = new PackageDefinition();

		packageDefinition.setPackageId(packageDTO.getPackageId());
		packageDefinition.setDescription(packageDTO.getDescription());
		if (packageDTO.getReplacementPackageId().equals("-1")) {
			packageDefinition.setReplacementPackageId(packageDTO.getPackageId());
		}
		else {
			packageDefinition.setReplacementPackageId(packageDTO.getReplacementPackageId());
		}
		packageDefinition.setFulfillmentVendorId(packageDTO.getFulfillmentId());
		packageDefinition.setIsActive(packageDTO.getIsActive());// By Default Yes.
		packageDefinition.setInsUser(1);
		packageDefinition.setInsDate(packageDTO.getInsDate());
		packageDefinition.setLastUpdUser(1);
		packageDefinition.setLastUpdDate(packageDTO.getLastUpdDate());
	
		Map<String, String> packageAttributes = packageDTO.getPackageAttributes();
		List<PackageAttributes> packList = new ArrayList<>();
		packageAttributes.entrySet().stream().forEach(x -> {
			PackageAttributes pkg = new PackageAttributes();
			pkg.setPrimaryKey(new PackageAtrributesId(packageDTO.getPackageId(), x.getKey()));
			pkg.setAttributeValue(x.getValue());
			pkg.setInsUser(1);
			pkg.setInsDate(packageDTO.getInsDate());
			pkg.setLupdUser(1);
			pkg.setLupdDate(packageDTO.getLastUpdDate());
			packList.add(pkg);
		});
		packageDefinition.setPackageKeyAttributes(packList);
		logger.info(CCLPConstants.EXIT);
		return packageDefinition;
	}

	@Override
	public PackageDTO getPackageIdDtls(String packageId) throws ServiceException {
		logger.info(CCLPConstants.EXIT);
		PackageDefinition pkgDefinition = packageDao.getPackageIdDtls(packageId);

		if (pkgDefinition != null) {
			List<Object[]> pkgAttributues = packageDao.getPackageAttributes(packageId);

			Map<String, String> pkgAttributuesList = new HashMap<>();
			if (!pkgAttributues.isEmpty()) {
				pkgAttributues.stream().forEach(p -> {
					String key = p[0].toString();
					String value = p[1].toString().trim();
					pkgAttributuesList.put(key, value);
				});
			}
			logger.info(CCLPConstants.EXIT);
			return new PackageDTO(pkgDefinition.getPackageId(), pkgDefinition.getDescription(),
					pkgDefinition.getReplacementPackageId().equals(pkgDefinition.getPackageId())? "-1": pkgDefinition.getReplacementPackageId(),
					pkgDefinition.getFulfillmentVendorId(), (pkgDefinition.getIsActive() != null && pkgDefinition.getIsActive().equals("Y")) ? "YES" : "NO",
					pkgAttributuesList);
		} else {
			logger.info(CCLPConstants.EXIT);
			return null;
		}
	}

	@Override
	public int checkduplicatePackageId(String packageId) throws ServiceException {

		return packageDao.checkduplicatePackageId(packageId);
	}
	
	public List<String> getPackageByIds(List<String> packageId)throws ServiceException {
		logger.info(CCLPConstants.ENTER);
		logger.info("inside getAllPackages");
		
		logger.info(CCLPConstants.EXIT);
	     return packageDao.getPackageByIds(packageId);
	}

}