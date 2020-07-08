package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.impl.MetaDataDAOImpl;
import com.incomm.cclp.dto.BlockListDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class BlockListServiceTest {

	
	@Autowired
	BlockListService blockListService;
	
	@Autowired
	MetaDataDAOImpl metadata;
	
	/*
	 * adding a new blockList
	 */
	@Test
	public void create_add_blocklist_new() {
		 BlockListDTO blockListDto = new BlockListDTO();
		 blockListDto.setChannelCode("IVR");
		 blockListDto.setInstrumentType("MOBILE_NO");
		 blockListDto.setInstrumentId("09441972238");
		 
		 try {
			blockListService.createBlockList(blockListDto);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * adding a duplicate blockList will throw exception
	 */
	@Test
	public void create_add_blocklist_alreadyExists() {
		
		create_add_blocklist_new();
		
		 BlockListDTO blockListDto2 = new BlockListDTO();
		 blockListDto2.setChannelCode("IVR");
		 blockListDto2.setInstrumentType("MOBILE_NO");
		 blockListDto2.setInstrumentId("09441972238");
		 
		 try {
			blockListService.createBlockList(blockListDto2);
		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			assertEquals(e.getMessage(),ResponseMessages.ERR_BLOCKLIST_EXISTS);
		}
	}
	
	/*
	 * Getting all the delivery channel list
	 */
	@Test
	public void get_all_delivery_channel_list() {
		metadata.getDeliveryChannel();
		List<Object[]> deliveryChannelList = blockListService.getDeliveryChannelList();
		assertEquals(2,deliveryChannelList.size());
	}
	
	/*
	 * Getting all the existing blockList
	 */
	@Test
	public void get_all_blocklist() {

		create_add_blocklist_new();		
		assertNotNull(blockListService.getAllBlockList());
	}
	
	/*
	 * Getting all the blockList by passing list of delivery channels as input
	 */
	@Test
	public void get_all_blocklist_by_channelId() {

		create_add_blocklist_new();
		List<BlockListDTO> blockListDto = blockListService.getBlockListById("IVR");
		assertNotNull(blockListDto);
	}

	/*
	 * Deleting a blockList by giving instrument id as input
	 */
	@Test
	public void delete_blocklist_by_instrumentId() {

		create_add_blocklist_new();
		List<Object> instrumentIdList = new ArrayList<>();
		instrumentIdList.add("09441972238");
		try {
			blockListService.deleteBlockList(instrumentIdList);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		List<BlockListDTO> blockListDto = blockListService.getBlockListById("IVR");
		assertEquals(0,blockListDto.size());
	}
	
	/*
	 * deleting a blockList by giving non existing id as input, It will throw exception
	 */
	@Test
	public void delete_blocklist_by_nonExisting_instrumentId() {

		create_add_blocklist_new();
		List<Object> instrumentIdList = new ArrayList<>();
		instrumentIdList.add("9441050038");
		try {
			blockListService.deleteBlockList(instrumentIdList);
		} catch (ServiceException e) {
			assertEquals(e.getMessage(),ResponseMessages.FAIL_BLOCKLIST_DELETE);
		}
	}
	
	
}
