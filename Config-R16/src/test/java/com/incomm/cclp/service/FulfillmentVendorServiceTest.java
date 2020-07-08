package com.incomm.cclp.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dto.FulfillmentDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FulfillmentVendorServiceTest {

	
	@Autowired
	FulfillmentService fulfillmentService;
	/* Inserting new fulfillment to the table*/
	@Test
	public void CreateFulfillment_First_fulfillment() throws ServiceException {

		FulfillmentDTO dto = new FulfillmentDTO();
		dto.setFulfillmentID("harfied");
		dto.setFulFillmentName("sample Two");
		dto.setIsAutomaticShipped("1");
		dto.setShippedTimeDealy(88);
		dto.setB2bVendorConfReq("enabled");
		dto.setB2bCnFileIdentifier("75hfsjfh");
		
		
		fulfillmentService.createFulfillment(dto);
		List<FulfillmentDTO> dtoList = fulfillmentService.getFulfillmentByName("sample Two");
		assertEquals(1, dtoList.size());
		
	}
	
	/*Trying to insert duplicate fulfillment(fulfillment with same fulfillment vendor ID) to the table */
	@Test
	public void CreateFulfillment_duplicate_fulfillment() throws ServiceException
	{
		FulfillmentDTO dto = new FulfillmentDTO();
		dto.setFulfillmentID("harfied");
		dto.setFulFillmentName("sample Three");
		dto.setIsAutomaticShipped("1");
		dto.setShippedTimeDealy(88);
		dto.setB2bVendorConfReq("enabled");
		dto.setB2bCnFileIdentifier("75hfsjfh");
		
		fulfillmentService.createFulfillment(dto);
		try
		{
			fulfillmentService.createFulfillment(dto);
		}
		catch (ServiceException e) {
			assertEquals("FUL_ERR_" + ResponseMessages.ALREADY_EXISTS, e.getMessage());
		}
			
	}
	
	
	/*  Search all the existing Fulfillments*/
	@Test
	public void getAllFulfillment() throws ServiceException {
		
		FulfillmentDTO dto = new FulfillmentDTO();
		dto.setFulfillmentID("tester1");
		dto.setFulFillmentName("sample Two");
		dto.setIsAutomaticShipped("1");
		dto.setShippedTimeDealy(88);
		dto.setB2bVendorConfReq("enabled");
		dto.setB2bCnFileIdentifier("75hfsjfh");
		fulfillmentService.createFulfillment(dto);
		
		assertEquals(1, fulfillmentService.getAllFulfillments().size());
	}
	
	/*Trying to update the existing fulfillmentID and fulfillmentSEQID*/
	@Test
	public void updateFulfillment_Existing() throws ServiceException {

		FulfillmentDTO dto = new FulfillmentDTO();
		dto.setFulFillmentSEQID(10);
		dto.setFulfillmentID("1234");
		dto.setFulFillmentName("sample Two");
		dto.setIsAutomaticShipped("1");
		dto.setShippedTimeDealy(88);
		dto.setB2bVendorConfReq("enabled");
		dto.setB2bCnFileIdentifier("75hfsjfh");
		fulfillmentService.createFulfillment(dto);

		dto.setFulFillmentName("sample Threeo");
		fulfillmentService.updateFulfillment(dto);
		
	
		assertEquals(1,fulfillmentService.getFulfillmentByName("sample Threeo").size());
	}
	
	
	/* Deleting the Fulfillment Vendor With Existing FulfillmentID*/
	@Test
	public void deleteFulfillment_with_fulfillmentSEQID() throws ServiceException 
	{
		FulfillmentDTO dto = new FulfillmentDTO();
		dto.setFulFillmentSEQID(10);
		dto.setFulfillmentID("123456");
		dto.setFulFillmentName("Tester1");
		dto.setIsAutomaticShipped("1");
		dto.setShippedTimeDealy(88);
		dto.setB2bVendorConfReq("enabled");
		dto.setB2bCnFileIdentifier("75hfsjh");
		fulfillmentService.createFulfillment(dto);
		
		long fulfillmentSEQID=dto.getFulFillmentSEQID();
     
		fulfillmentService.deleteFulfillment(fulfillmentSEQID);

		assertNull(fulfillmentService.getFulfillmentById(fulfillmentSEQID));
	}
	
	/* Trying to Delete the Fulfillment Vendor which does not exist in DB */
	@Test
	public void deleteNonExistingFulfillment_With_fulfillmentSEQID() {

		Long fulfillmentSEQID = (long) 898989;
		try {
			fulfillmentService.deleteFulfillment(fulfillmentSEQID);
		}catch (ServiceException se) {
			assertEquals(se.getMessage(),ResponseMessages.ERR_FULFILLMENT_DOESNOT_EXISTS);
		}
	}
	
	/* Checking duplicate fulfillment*/
	//@Test
	public void testCheckDuplicateID() throws ServiceException
	{
		FulfillmentDTO dto = new FulfillmentDTO();
		dto.setFulFillmentSEQID(11);
		dto.setFulfillmentID("12388");
		dto.setFulFillmentName("Tester1");
		dto.setIsAutomaticShipped("1");
		dto.setShippedTimeDealy(88);
		dto.setB2bVendorConfReq("enabled");
		dto.setB2bCnFileIdentifier("75hfsjh");
		fulfillmentService.createFulfillment(dto);
		
		//System.out.println(fulfillmentService.chkDuplicateByID(dto.getFulfillmentID()));
		//assertNotEquals("0",String.valueOf(fulfillmentService.chkDuplicateByID(dto.getFulfillmentID())));
	}
	
}
