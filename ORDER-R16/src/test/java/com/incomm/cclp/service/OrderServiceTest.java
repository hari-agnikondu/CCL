package com.incomm.cclp.service;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.dao.impl.MetaDataDAOImpl;
import com.incomm.cclp.dto.OrderDTO;



@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class OrderServiceTest {

	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MetaDataDAOImpl daoImpl;
	
	//daoImpl.insertOrderMetaData();
	

	@Test
	public void create_new_order() {
		
		OrderDTO orderDto = new OrderDTO();
		
		orderDto.setOrderId("123");
		orderDto.setProductId((long) 1);
		orderDto.setMerchantId("1");
		orderDto.setLocationId((long) 1);
		/*orderDto.setIssuerId((long) 1);
		orderDto.setPartnerId((long) 1);*/
		orderDto.setPackageId("12");
		
		orderDto.setQuantity("100");
		//orderService.createOrder(orderDto);
	}
}
