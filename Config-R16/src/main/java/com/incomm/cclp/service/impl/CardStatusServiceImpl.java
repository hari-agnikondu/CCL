/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.incomm.cclp.dao.CardStatusDAO;
import com.incomm.cclp.dto.CardStatusDTO;
import com.incomm.cclp.service.CardStatusService;


@Service
public class CardStatusServiceImpl implements CardStatusService {

	@Autowired
	CardStatusDAO cardStatusDAO;

	/**
	 * Getting Active Card Status details
	 * 
	 * */
	@Override
	public List<CardStatusDTO> getCardStatus(){

		return new ModelMapper().map(cardStatusDAO.getCardStatus(), 
				new TypeToken<List<CardStatusDTO>>() {}.getType());
	}

}
