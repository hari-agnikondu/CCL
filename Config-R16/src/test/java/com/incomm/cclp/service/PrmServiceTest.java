package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.dao.impl.MetaDataDAOImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class PrmServiceTest {

	@Autowired
	PrmService prmService;
	
	@Autowired
	MetaDataDAOImpl metadata;
	

	/*
	 * update the PRM attributes with all the attributes as present
	 */
	@Test
	public void update_prmAttributes() throws Exception {
		metadata.prmMetaData();
		Map<String,String> prmAttributes = new HashMap<>();
		prmAttributes.put("11_ERIF", "on");
		prmAttributes.put("11_MRIF", "on");
		prmAttributes.put("12_ERIF", "on");
		prmAttributes.put("12_MRIF", "on");
		prmAttributes.put("21_ERIF", "on");
		prmAttributes.put("21_MRIF", "on");
		prmAttributes.put("24_ERIF", "on");
		prmAttributes.put("24_MRIF", "on");
		int count = prmService.updatePrmAttributes(prmAttributes);
		assertEquals(count,1);
	}
	
	/*
	 * update only one ERIF attribute
	 */
	@Test
	public void update_only_one_ERIF() {
		metadata.prmMetaData();
		Map<String,String> prmAttributes = new HashMap<>();
		prmAttributes.put("21_ERIF", "on");

		int count = prmService.updatePrmAttributes(prmAttributes);
		assertEquals(count,1);
	}
	
	/*
	 * update only one MRIF attribute
	 */
	@Test
	public void update_only_one_MRIF() {
		metadata.prmMetaData();
		Map<String,String> prmAttributes = new HashMap<>();
		prmAttributes.put("21_MRIF", "on");

		int count = prmService.updatePrmAttributes(prmAttributes);
		assertEquals(count,1);
	}
	
	/*
	 * update the attributes with no prm attributes are present, Only delivery channels are present
	 */
	@Test
	public void update_prmAttributes_with_sameValues() throws Exception {
		metadata.prmMetaData();
		Map<String,String> prmAttributes = new HashMap<>();
		prmAttributes.put("11_MRIF", "on");
		prmAttributes.put("12_MRIF", "on");

		int count = prmService.updatePrmAttributes(prmAttributes);
		assertEquals(count,1);
	}
	
	/*
	 * update the prm attributes if the ERIF attributes are empty
	 */
	@Test
	public void update_prmAttributes_with_ERIF_asEmpty() throws Exception {
		metadata.prmMetaData();
		Map<String,String> prmAttributes = new HashMap<>();
		prmAttributes.put("11_MRIF", "on");
		prmAttributes.put("12_MRIF", "on");
		prmAttributes.put("21_MRIF", "on");
		prmAttributes.put("24_MRIF", "on");

		int count = prmService.updatePrmAttributes(prmAttributes);
		assertEquals(count,1);
	}

	/*
	 * update the prm attributes if the MRIF attributes are empty
	 */
	@Test
	public void update_prmAttributes_with_MRIF_asEmpty() throws Exception {
		metadata.prmMetaData();
		Map<String,String> prmAttributes = new HashMap<>();
		prmAttributes.put("11_ERIF", "on");
		prmAttributes.put("12_ERIF", "on");
		prmAttributes.put("21_ERIF", "on");
		prmAttributes.put("24_ERIF", "on");
		int count = prmService.updatePrmAttributes(prmAttributes);
		assertEquals(count,1);
	}
	
	
	/*
	 * update all the prm attributes
	 */
	@Test
	public void update_all_prmAttributes() throws Exception {
		metadata.prmMetaData();
			 int count = prmService.updateAllPrmAttributes();
			 assertEquals(count, 4);
		
	}
}
