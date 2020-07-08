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
public class TransactionFlexServiceTest {
	
	@Autowired
	TransactionFlexService TransactionFlexService;
	
	@Autowired
	MetaDataDAOImpl metadata;
	
	
	/*
	 * update transaction flex description with all as present
	 */
	@Test
	public void update_TxnFlexDesc() throws Exception {
		metadata.prmMetaData();
		Map<String,String> TxnFlexDesc = new HashMap<>();
		TxnFlexDesc.put("11", "ytrytry");		
		TxnFlexDesc.put("12", "oryrtrn");		
		TxnFlexDesc.put("21", "oryasrtyn");
		TxnFlexDesc.put("24", "oryrtrryryn");
				int count = TransactionFlexService.updateTransactionFlexDesc(TxnFlexDesc);
		assertEquals(count,1);
	}
	

}
