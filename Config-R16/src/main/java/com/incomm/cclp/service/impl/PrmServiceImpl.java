package com.incomm.cclp.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.dao.PrmDAO;
import com.incomm.cclp.service.PrmService;



@Service
public class PrmServiceImpl implements PrmService {

	private static final Logger logger = LogManager.getLogger(PrmServiceImpl.class);

	
	@Autowired
	PrmDAO prmDao;

	public int updatePrmAttributes(Map<String, String> prmAttributes) {
		logger.info(CCLPConstants.ENTER);
		logger.debug("prmAttributes" + prmAttributes);

		int updateCount = 0;

		List<String> erifkeys = prmAttributes.entrySet().stream().filter(x -> x.getKey().endsWith("_ERIF"))
				.map(k -> k.getKey().substring(0, k.getKey().length() - 5)).collect(Collectors.toList());
		List<String> mrifkeys = prmAttributes.entrySet().stream().filter(x -> x.getKey().endsWith("_MRIF"))
				.map(k -> k.getKey().substring(0, k.getKey().length() - 5)).collect(Collectors.toList());
		
		logger.info("ERIFKEYS" + erifkeys);
		logger.info("MRIFKEYS" + mrifkeys);

		int mrifinkey = 0;
		int mrifnotinkey = 0;
		if (!mrifkeys.isEmpty()) {
			logger.debug(mrifkeys.toString());
			mrifinkey = prmDao.updateMRIFIN(mrifkeys.toString());
			mrifnotinkey = prmDao.updateMRIFNOTIN(mrifkeys.toString());
		} else {
			mrifnotinkey = prmDao.updateAllPrmToDiable("MRIF");
		}
		
		if (mrifinkey > 0 || mrifnotinkey > 0)
			updateCount = 1;

		int erifinkey = 0;
		int erifnotinkey = 0;
		if (!erifkeys.isEmpty()) {
			logger.debug(erifkeys.toString());
			erifinkey = prmDao.updateERIFIN(erifkeys.toString());
			erifnotinkey = prmDao.updateERIFNOTIN(erifkeys.toString());
		} else {
			erifnotinkey = prmDao.updateAllPrmToDiable("ERIF");
		}

		if (erifinkey > 0 || erifnotinkey > 0)
			updateCount = 1;
		logger.info(CCLPConstants.EXIT);
		return updateCount;
	}

	@Override
	public int updateAllPrmAttributes() {
		return prmDao.updateAllPrm();
	}


}
