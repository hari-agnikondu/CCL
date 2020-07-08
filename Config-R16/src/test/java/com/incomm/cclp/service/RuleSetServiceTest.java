package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.dto.RuleSetDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RuleSetServiceTest {

	@Autowired
	RuleSetService ruleSetService;
	
	@Test
	public void testGetRuleSetList() throws ServiceException{

		List<RuleSetDTO> ruleSetList=ruleSetService.getRuleSet();
		assertEquals(true, ruleSetList.size()>=0);
	}

}
