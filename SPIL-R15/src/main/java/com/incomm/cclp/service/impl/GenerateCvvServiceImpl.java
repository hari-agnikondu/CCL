package com.incomm.cclp.service.impl;

import java.sql.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.SpilTranConstants;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.service.GenerateCvvService;
import com.incomm.cclp.util.HSMCommandBuilder;
import com.incomm.cclp.util.Util;

@Service
public class GenerateCvvServiceImpl implements GenerateCvvService {
	private static final Logger logger = LogManager.getLogger(GenerateCvvServiceImpl.class);

	@Value("${CCF_HSM_IPADDRESS}")
	private String hsmIpAddress;

	@Value("${CCF_HSM_PORT}")
	private int hsmPort;

	@Autowired
	HSMCommandBuilder hsmCommandBuilder;

	@Override
	public String generateCVV(ValueDTO valueDto) {
		logger.debug("generateCVV START >>>");
		Map<String, String> valueObj = valueDto.getValueObj();
		String msPan = valueObj.get(ValueObjectKeys.CARD_NUMBER);
		logger.debug("msPan: {}", Util.getMaskCardNum(msPan));
		Date msExpiryDate = Util.formatDate("dd-MMM-yy", valueObj.get(ValueObjectKeys.CARD_EXPDATE));
		String psExpMM = Util.padLeft(String.valueOf(Util.getMonth(msExpiryDate)), 2, "0");
		String psExpYY = Util.padLeft(String.valueOf(Util.getYear(msExpiryDate)), 2, "0");
		if (psExpYY != null) {
			psExpYY = psExpYY.substring(psExpYY.length() - 2);
		}
		logger.debug("psExpMM: {}", psExpMM);
		logger.debug("psExpYY: {}", psExpYY);

		String cvkA = valueDto.getProductAttributes()
			.get(ValueObjectKeys.CVV)
			.get(ValueObjectKeys.CVKA) != null ? valueDto.getProductAttributes()
				.get(ValueObjectKeys.CVV)
				.get(ValueObjectKeys.CVKA)
				.toString() : "";
		String cvkB = valueDto.getProductAttributes()
			.get(ValueObjectKeys.CVV)
			.get(ValueObjectKeys.CVKB) != null ? valueDto.getProductAttributes()
				.get(ValueObjectKeys.CVV)
				.get(ValueObjectKeys.CVKB)
				.toString() : "";
		String cvk = cvkA.concat(cvkB);

		logger.debug("cvk: {}", cvk);
		logger.debug("hsmIpAddress: {}", hsmIpAddress);
		logger.debug("hsmPort: {}", hsmPort);

		String cvv = hsmCommandBuilder.genCVV(Util.padRight(msPan, msPan.length(), SpilTranConstants.EMPTY_SPACE), psExpMM + psExpYY,
				SpilTranConstants.TRIPLE_ZERO, cvk, hsmIpAddress, hsmPort);
		if (cvv == null) {
			cvv = "";
		}
		logger.debug("cvv: {}, cvv Length: {}", cvv, cvv.length());
		logger.debug("generateCVV END <<<");
		return cvv;
	}

}
