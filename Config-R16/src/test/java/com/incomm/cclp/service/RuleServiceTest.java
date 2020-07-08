
package com.incomm.cclp.service;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.RuleDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.RuleService;

/**
 * @author abutani
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RuleServiceTest {

	@Autowired
	RuleService ruleService;
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.cclp.service.impl.RuleServiceImpl#createRule(com.cclp.dto.RuleDTO)}.
	 * @throws ServiceException 
	 */
	@Test
	public void testCreateRule() throws ServiceException {
		
		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setRuleName("SPIL Dollar general");
		
		ruleService.createRule(ruleDto);
		
		assertEquals(1, ruleService.getRulesByName("SPIL Dollar general").size());
	}
	
	
	/**
	 * Test method for {@link com.cclp.service.impl.RuleServiceImpl#createRule(com.cclp.dto.RuleDTO)}.
	 * @throws ServiceException 
	 */
	@Test
	public void testCreateRule_Duplicate_Rule() {
		
		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setRuleName("SPIL Dollar general");
		
		RuleDTO ruleDto1 = new RuleDTO();
		ruleDto1.setRuleName("SPIL Dollar general");
		
		try 
		{
			ruleService.createRule(ruleDto);
		} 
		catch (ServiceException e) {
		}
		
		String errorMsg = "RULE_"+ResponseMessages.ALREADY_EXISTS;
		try 
		{
			ruleService.createRule(ruleDto1);
		} 
		catch (ServiceException se) {
			
			assertEquals(errorMsg, se.getMessage());
		}
		
		assertEquals(1, ruleService.getRulesByName("SPIL Dollar general").size());
	}
	
	
	/**
	 * Test method for {@link com.cclp.service.impl.RuleServiceImpl#createRule(com.cclp.dto.RuleDTO)}.
	 * @throws ServiceException 
	 */
	@Test
	public void testCreateRule_Duplicate_Rule_With_PartialNameMatch() {
		
		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setRuleName("SPIL Dollar general");
		
		RuleDTO ruleDto1 = new RuleDTO();
		ruleDto1.setRuleName("SPIL Dollar");
		String errorMsg = "RULE_"+ResponseMessages.ALREADY_EXISTS;
		try 
		{
			ruleService.createRule(ruleDto);
		} 
		catch (ServiceException e) {
		}
		
		try 
		{
			ruleService.createRule(ruleDto1);
			
		} 
		catch (ServiceException se) {
			
			assertNotEquals(errorMsg, se.getMessage());
		}
		
	}
	
	
	@Test
	public void testGetAllRules() throws ServiceException {
		
		createMultipleRules();
		
		assertEquals(4, ruleService.getAllRules().size());
		
	}
	
	@Test
	public void testGetRulesByName() throws ServiceException {
		
		createMultipleRules();
		
		assertEquals(1, ruleService.getRulesByName("SPIL Dollar General 1").size());
		
	}
	
	@Test
	public void testGetRulesByName_WithNoMatch() throws ServiceException {
		
		createMultipleRules();
		
		assertEquals(0, ruleService.getRulesByName("SPIL RiteAid").size());
		
	}
	
	@Test
	public void testGetAllRules_VerifyRetrivedNames() throws ServiceException {
		
		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setRuleName("SPIL Dollar General");
		ruleService.createRule(ruleDto);
		
		RuleDTO ruleDto1 = new RuleDTO();
		ruleDto1.setRuleName("SPIL RiteAid");
		ruleService.createRule(ruleDto1);
		
		assertEquals(2, ruleService.getAllRules().size());
		
		assertEquals("SPIL Dollar General", ruleService.getRuleById(ruleDto.getRuleId()).
				getRuleName());
		
		assertEquals("SPIL RiteAid", ruleService.getRuleById(ruleDto1.getRuleId()).
				getRuleName());
	}
	
	@Test
	public void testGetRuleById() throws ServiceException {
		
		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setRuleName("SPIL Dollar General");
		
		ruleService.createRule(ruleDto);
		
		assertEquals("SPIL Dollar General", ruleService.getRuleById(ruleDto.getRuleId()).
				getRuleName());
		
	}
	
	
 	@Test
	public void testGetRuleById_With_NullRule() throws ServiceException {
		
		createMultipleRules();

		assertEquals(0, ruleService.getRuleById(511).getRuleId());
		
	} 
	
	@Test
	public void testUpdateRule() throws ServiceException {
		
		createMultipleRules();
		
		RuleDTO ruleDto = new RuleDTO();
		
		ruleDto.setRuleName("SPIL RiteAid");
		
		ruleService.updateRule(ruleDto);;
		
		assertEquals(1, ruleService.getRulesByName("SPIL RiteAid").size());
		
	}
	
	@Test
	public void testDeleteRule() throws ServiceException {
		
		RuleDTO ruleDto = new RuleDTO();

		ruleDto.setRuleName("SPIL RiteAid");
		
		ruleService.deleteRule(ruleDto);
		
		assertEquals(ruleService.getAllRules().size(), 0);
		
	}
	
	@Test
	public void testDeleteRule_ExistingRule() throws ServiceException {
		
		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setRuleName("SPIL RiteAid");
		
		ruleService.createRule(ruleDto);
		
		assertEquals(ruleService.getAllRules().size(), 1);
		
		ruleService.deleteRule(ruleDto);
		
		assertEquals(ruleService.getAllRules().size(), 0);
		
	}
	
	private void createMultipleRules() throws ServiceException
	{
		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setRuleName("SPIL Dollar General 1");
		ruleService.createRule(ruleDto);
		
		RuleDTO ruleDto1 = new RuleDTO();
		ruleDto1.setRuleName("SPIL Dollar General 2");
		ruleService.createRule(ruleDto1);
		
		RuleDTO ruleDto2 = new RuleDTO();
		ruleDto2.setRuleName("SPIL Dollar General 3");
		ruleService.createRule(ruleDto2);
		
		RuleDTO ruleDto3 = new RuleDTO();
		ruleDto3.setRuleName("SPIL Dollar General 4");
		ruleService.createRule(ruleDto3);
		
	}
	
	
	@Test
	public void testUpdateRule_Duplicate_Rule() {
		
		RuleDTO ruleDto = new RuleDTO();
		ruleDto.setRuleName("SPIL Dollar General");
		
		RuleDTO ruleDto1 = new RuleDTO();
		ruleDto1.setRuleName("SPIL RiteAid");
		String errorMsg = "RULE_"+ResponseMessages.ALREADY_EXISTS;
		try
		{
			ruleService.createRule(ruleDto);
			ruleService.createRule(ruleDto1);
		}
		catch (ServiceException se) {
		}
		
		try
		{
			ruleDto1.setRuleName("SPIL Dollar General");
			
			ruleService.updateRule(ruleDto1);
		}
		catch (ServiceException se) {
			
			assertEquals(errorMsg, se.getMessage());
		}
		
		assertEquals("SPIL RiteAid", 
				ruleService.getRuleById(ruleDto1.getRuleId()).getRuleName());
	}

}
