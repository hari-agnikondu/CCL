package com.incomm.cclp.service.impl;

import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.constants.ResponseCodes;
import com.incomm.cclp.constants.ValueObjectKeys;
import com.incomm.cclp.dao.SpilDAO;
import com.incomm.cclp.dao.SpilPreValueInsertionDAO;
import com.incomm.cclp.dto.ValueDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.SpilCommonService;

/**
 * 
 * @author sampathkumarl
 *
 */

@Service("com.incomm.cclp.service.impl.SpilPreValueInsertionServiceImpl")
public class SpilPreValueInsertionServiceImpl implements SpilCommonService {

	@Autowired
	SpilPreValueInsertionDAO preValueInsertiondao;

	@Autowired
	private SpilDAO spilDAO;

	@Override
	@Transactional
	public String[] invoke(ValueDTO valueDto) throws ServiceException {

		String[] response = preValueInsertiondao.spilPreValueInsertion(valueDto);
		if (ResponseCodes.SUCCESS.equalsIgnoreCase(response[0]) && "OK".equalsIgnoreCase(response[1])) {
			spilDAO.updateUsageLimits(valueDto);
			Map<String, String> valueObj = valueDto.getValueObj();

			String accountId = valueObj.get(ValueObjectKeys.CARD_ACCOUNT_ID);
			String purBal = spilDAO.getPurseBalance(accountId);
			valueDto.getValueObj()
				.put(ValueObjectKeys.PUR_BAL, purBal);

		}
		return response;

	}

}
