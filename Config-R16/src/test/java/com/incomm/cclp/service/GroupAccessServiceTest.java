package com.incomm.cclp.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.dao.AttributeDefinitionDAO;
import com.incomm.cclp.dao.MasterDao;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.dao.PurseDAO;
import com.incomm.cclp.domain.AttributeDefinition;
import com.incomm.cclp.domain.PurseType;
import com.incomm.cclp.dto.CardRangeDTO;
import com.incomm.cclp.dto.GroupAccessDTO;
import com.incomm.cclp.dto.GroupAccessPartnerDTO;
import com.incomm.cclp.dto.IssuerDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ProductDTO;
import com.incomm.cclp.dto.PurseDTO;
import com.incomm.cclp.exception.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class GroupAccessServiceTest {

	@Autowired
	GroupAccessService groupAccessService;
	@Autowired
	PartnerService partnerService;

	@Autowired
	ProductService productService;

	@Autowired
	CardRangeService cardRangeService;

	@Autowired
	MasterDao masterdao;

	@Autowired
	PurseDAO pursedao;

	@Autowired
	PurseService purseService;

	// the Product dao.
	@Autowired
	ProductDAO productDao;

	@Autowired
	AttributeDefinitionDAO attributeDefinitionDao;

	@Autowired
    IssuerService issuerService;



	@Test
	public void testCreateGroupAccess_with_valid_values() throws ServiceException{

		GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
		List<String> partnerList=new ArrayList<>();
		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("venkat123");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");
		partnerService.createPartner(Dto);
		partnerList.add(Dto.getPartnerId()+"");

		PartnerDTO Dto1 = new PartnerDTO();
		Dto1.setPartnerName("venkat");
		Dto1.setPartnerDesc(" Java Developer");
		Dto1.setIsActive("Y");
		Dto1.setMdmId("123");
		partnerService.createPartner(Dto1);
		partnerList.add(Dto1.getPartnerId()+"");

		groupAccessDTO.setPartnerList(partnerList);
		groupAccessDTO.setGroupAccessName("ABC123");
		groupAccessDTO.setInsUser(1L);
		groupAccessDTO.setLastUpdUser(1L);
		groupAccessService.createGroupAccess(groupAccessDTO);

		GroupAccessDTO groupAccessDTO1=new GroupAccessDTO();
		groupAccessDTO1.setGroupAccessName("ABCD");
		groupAccessDTO1.setInsUser(Long.valueOf(1));
		groupAccessDTO1.setLastUpdUser(Long.valueOf(1));
		groupAccessService.createGroupAccess(groupAccessDTO1);

		List<GroupAccessDTO> groupAccessDTOList=groupAccessService.getGroupAccess("*");

		List<GroupAccessDTO> groupAccessDTOList1=groupAccessService.getGroupAccessPartners();

		assertEquals(2, groupAccessDTOList1.size());
		assertEquals(2, groupAccessDTOList.size());
	}

	@Test
	public void test_Update_GroupAccess_with_valid_values() throws ServiceException{

		GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
		List<String> partnerList=new ArrayList<>();
		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("venkat123");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");
		partnerService.createPartner(Dto);
		partnerList.add(Dto.getPartnerId()+"");

		PartnerDTO Dto1 = new PartnerDTO();
		Dto1.setPartnerName("venkat");
		Dto1.setPartnerDesc(" Java Developer");
		Dto1.setIsActive("Y");
		Dto1.setMdmId("123");
		partnerService.createPartner(Dto1);
		partnerList.add(Dto1.getPartnerId()+"");

		PartnerDTO Dto2 = new PartnerDTO();
		Dto2.setPartnerName("venkat456");
		Dto2.setPartnerDesc(" Java Developer");
		Dto2.setIsActive("Y");
		Dto2.setMdmId("123");
		partnerService.createPartner(Dto2);

		groupAccessDTO.setPartnerList(partnerList);
		groupAccessDTO.setGroupAccessName("ABC123");
		groupAccessDTO.setInsUser(1L);
		groupAccessDTO.setLastUpdUser(1L);
		groupAccessService.createGroupAccess(groupAccessDTO);

		GroupAccessDTO groupAccessDTO1=new GroupAccessDTO();
		groupAccessDTO1.setGroupAccessName("ABCD");
		groupAccessDTO1.setInsUser(Long.valueOf(1));
		groupAccessDTO1.setLastUpdUser(Long.valueOf(1));
		groupAccessService.createGroupAccess(groupAccessDTO1);

		GroupAccessDTO groupAccessDTO2=groupAccessService.getGroupAccessById(groupAccessDTO.getGroupAccessId());

		partnerList.add(Dto2.getPartnerId()+"");
		groupAccessDTO2.setGroupAccessName("Group99");
		groupAccessDTO2.setPartnerList(partnerList);
		groupAccessService.updateGroupAccess(groupAccessDTO2);


		List<GroupAccessDTO> groupAccessDTOList1=groupAccessService.getGroupAccessPartners();
		List<GroupAccessDTO> groupAccessDTOList=groupAccessService.getGroupAccess("*");
		assertEquals(3, groupAccessDTOList1.size());
		assertEquals(2, groupAccessDTOList.size());
	}


	@Test
	public void test_Update_GroupAccess_removing_GroupAccessPartners() throws ServiceException{

		GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
		List<String> partnerList=new ArrayList<>();

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("venkat123");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");
		partnerService.createPartner(Dto);
		partnerList.add(Dto.getPartnerId()+"");

		PartnerDTO Dto1 = new PartnerDTO();
		Dto1.setPartnerName("venkat");
		Dto1.setPartnerDesc(" Java Developer");
		Dto1.setIsActive("Y");
		Dto1.setMdmId("123");
		partnerService.createPartner(Dto1);
		partnerList.add(Dto1.getPartnerId()+"");

		PartnerDTO Dto2 = new PartnerDTO();
		Dto2.setPartnerName("venkat456");
		Dto2.setPartnerDesc(" Java Developer");
		Dto2.setIsActive("Y");
		Dto2.setMdmId("123");
		partnerService.createPartner(Dto2);
		partnerList.add(Dto2.getPartnerId()+"");

		groupAccessDTO.setPartnerList(partnerList);
		groupAccessDTO.setGroupAccessName("ABC");
		groupAccessDTO.setInsUser(1L);
		groupAccessDTO.setLastUpdUser(1L);
		groupAccessService.createGroupAccess(groupAccessDTO);

		GroupAccessDTO groupAccessDTO1=new GroupAccessDTO();
		groupAccessDTO1.setGroupAccessName("ABCD123");
		groupAccessDTO1.setInsUser(Long.valueOf(1));
		groupAccessDTO1.setLastUpdUser(Long.valueOf(1));
		groupAccessService.createGroupAccess(groupAccessDTO1);

		GroupAccessDTO groupAccessDTO2=groupAccessService.getGroupAccessById(groupAccessDTO.getGroupAccessId());
		//Here removing one partner
		partnerList.remove(1);
		groupAccessDTO2.setPartnerList(partnerList);
		groupAccessService.updateGroupAccess(groupAccessDTO2);


		List<GroupAccessDTO> groupAccessDTOList1=groupAccessService.getGroupAccessPartners();
		List<GroupAccessDTO> groupAccessDTOList=groupAccessService.getGroupAccess("*");
		assertEquals(2, groupAccessDTOList1.size());
		assertEquals(2, groupAccessDTOList.size());
	}



	@Test
	public void testGet_GroupAccessPartner_with_valid_AccessID() throws ServiceException{

		List<String> partnerList=new ArrayList<>();

		PartnerDTO Dto = new PartnerDTO();
		Dto.setPartnerName("venkat");
		Dto.setPartnerDesc(" Java Developer");
		Dto.setIsActive("Y");
		Dto.setMdmId("123");
		partnerService.createPartner(Dto);
		partnerList.add(Dto.getPartnerId()+"");
		PartnerDTO Dto1 = new PartnerDTO();
		Dto1.setPartnerName("venkat123");
		Dto1.setPartnerDesc(" Java Developer");
		Dto1.setIsActive("Y");
		Dto1.setMdmId("123");
		partnerService.createPartner(Dto1);
		partnerList.add(Dto1.getPartnerId()+"");

		GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
		groupAccessDTO.setPartnerList(partnerList);
		groupAccessDTO.setGroupAccessName("ABC");
		groupAccessDTO.setInsUser(1L);
		groupAccessDTO.setLastUpdUser(1L);
		groupAccessService.createGroupAccess(groupAccessDTO);

		List<GroupAccessDTO> groupAccessList=groupAccessService.getGroupAccessPartnersByAccessId(groupAccessDTO.getGroupAccessId());
		assertEquals(2, groupAccessList.size());
	}


	@Test
	public void test_Update_GroupAccess_with_Existing_AccessName() throws ServiceException{

		try {
			GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
			List<String> partnerList=new ArrayList<>();
			PartnerDTO Dto = new PartnerDTO();
			Dto.setPartnerName("venkat123");
			Dto.setPartnerDesc(" Java Developer");
			Dto.setIsActive("Y");
			Dto.setMdmId("123");
			partnerService.createPartner(Dto);
			partnerList.add(Dto.getPartnerId()+"");

			PartnerDTO Dto1 = new PartnerDTO();
			Dto1.setPartnerName("venkat");
			Dto1.setPartnerDesc(" Java Developer");
			Dto1.setIsActive("Y");
			Dto1.setMdmId("123");
			partnerService.createPartner(Dto1);
			partnerList.add(Dto1.getPartnerId()+"");

			PartnerDTO Dto2 = new PartnerDTO();
			Dto2.setPartnerName("venkat456");
			Dto2.setPartnerDesc(" Java Developer");
			Dto2.setIsActive("Y");
			Dto2.setMdmId("123");
			partnerService.createPartner(Dto2);

			groupAccessDTO.setPartnerList(partnerList);
			groupAccessDTO.setGroupAccessName("ABC123");
			groupAccessDTO.setInsUser(1L);
			groupAccessDTO.setLastUpdUser(1L);
			groupAccessService.createGroupAccess(groupAccessDTO);

			GroupAccessDTO groupAccessDTO1=new GroupAccessDTO();
			groupAccessDTO1.setGroupAccessName("ABCD321");
			groupAccessDTO1.setInsUser(Long.valueOf(1));
			groupAccessDTO1.setLastUpdUser(Long.valueOf(1));
			groupAccessService.createGroupAccess(groupAccessDTO1);

			GroupAccessDTO groupAccessDTO2=groupAccessService.getGroupAccessById(groupAccessDTO.getGroupAccessId());

			partnerList.add(Dto2.getPartnerId()+"");
			groupAccessDTO2.setPartnerList(partnerList);
			groupAccessDTO2.setGroupAccessName("ABC123");
			groupAccessService.updateGroupAccess(groupAccessDTO2);
		} catch (ServiceException e) {
			assertEquals(ResponseMessages.ERR_GROUP_ACCESS_NAME_ALREADY_EXIST, e.getMessage());
		}

	}


	@Test
	public void test_GroupAccessDetails_with_Invalid_AccessID() throws ServiceException{

		try {
			GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
			List<String> partnerList=new ArrayList<>();
			PartnerDTO Dto = new PartnerDTO();
			Dto.setPartnerName("venkat123");
			Dto.setPartnerDesc(" Java Developer");
			Dto.setIsActive("Y");
			Dto.setMdmId("123");
			partnerService.createPartner(Dto);
			partnerList.add(Dto.getPartnerId()+"");

			PartnerDTO Dto1 = new PartnerDTO();
			Dto1.setPartnerName("venkat");
			Dto1.setPartnerDesc(" Java Developer");
			Dto1.setIsActive("Y");
			Dto1.setMdmId("123");
			partnerService.createPartner(Dto1);
			partnerList.add(Dto1.getPartnerId()+"");

			PartnerDTO Dto2 = new PartnerDTO();
			Dto2.setPartnerName("venkat456");
			Dto2.setPartnerDesc(" Java Developer");
			Dto2.setIsActive("Y");
			Dto2.setMdmId("123");
			partnerService.createPartner(Dto2);

			groupAccessDTO.setPartnerList(partnerList);
			groupAccessDTO.setGroupAccessName("ABC123");
			groupAccessDTO.setInsUser(1L);
			groupAccessDTO.setLastUpdUser(1L);
			groupAccessService.createGroupAccess(groupAccessDTO);

			GroupAccessDTO groupAccessDTO1=new GroupAccessDTO();
			groupAccessDTO1.setGroupAccessName("ABCD321");
			groupAccessDTO1.setInsUser(Long.valueOf(1));
			groupAccessDTO1.setLastUpdUser(Long.valueOf(1));
			groupAccessService.createGroupAccess(groupAccessDTO1);

			//Here passing 4 as an groupAccessId
			groupAccessService.getGroupAccessById(Long.valueOf(4));

		} catch (ServiceException e) {
			assertEquals(ResponseMessages.ERR_GROUP_ACCESS_DOESNT_EXIST, e.getMessage());
		}

	}

	public void createMultipleAttributeDefinitions() {
		AttributeDefinition attributeDefinition1 = new AttributeDefinition();
		attributeDefinition1.setAttributeGroup("Product");
		attributeDefinition1.setAttributeName("retailUPC");
		attributeDefinition1.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition1);

		AttributeDefinition attributeDefinition2 = new AttributeDefinition();
        attributeDefinition2.setAttributeGroup("Product");
        attributeDefinition2.setAttributeName("b2bUpc");
        attributeDefinition2.setAttributeValue("");
        attributeDefinitionDao.createAttributeDefinition(attributeDefinition2);

		AttributeDefinition attributeDefinition3 = new AttributeDefinition();
		attributeDefinition3.setAttributeGroup("Product");
		attributeDefinition3.setAttributeName("maintainanceFeeSupported");
		attributeDefinition3.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition3);

		AttributeDefinition attributeDefinition4 = new AttributeDefinition();
		attributeDefinition4.setAttributeGroup("Product");
		attributeDefinition4.setAttributeName("defaultPurse");
		attributeDefinition4.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition4);

		AttributeDefinition attributeDefinition5 = new AttributeDefinition();
		attributeDefinition5.setAttributeGroup("Product");
		attributeDefinition5.setAttributeName("defaultPackage");
		attributeDefinition5.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition5);

		AttributeDefinition attributeDefinition6 = new AttributeDefinition();
		attributeDefinition6.setAttributeGroup("Product");
		attributeDefinition6.setAttributeName("validityPeriodFormat");
		attributeDefinition6.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition6);

		AttributeDefinition attributeDefinition7 = new AttributeDefinition();
		attributeDefinition7.setAttributeGroup("Product");
		attributeDefinition7.setAttributeName("b2bInitSerialNumQty");
		attributeDefinition7.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition7);

		AttributeDefinition attributeDefinition8 = new AttributeDefinition();
		attributeDefinition8.setAttributeGroup("Product");
		attributeDefinition8.setAttributeName("b2bSerialNumAutoReplenishLevel");
		attributeDefinition8.setAttributeValue("Y");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition8);

		AttributeDefinition attributeDefinition9 = new AttributeDefinition();
		attributeDefinition9.setAttributeGroup("Product");
		attributeDefinition9.setAttributeName("b2bSerialNumAutoReplenishVal");
		attributeDefinition9.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition9);

		AttributeDefinition attributeDefinition10 = new AttributeDefinition();
		attributeDefinition10.setAttributeGroup("Product");
		attributeDefinition10.setAttributeName("dcmsId");
		attributeDefinition10.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition10);

		AttributeDefinition attributeDefinition11 = new AttributeDefinition();
		attributeDefinition11.setAttributeGroup("Product");
		attributeDefinition11.setAttributeName("formFactor");
		attributeDefinition11.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition11);

		AttributeDefinition attributeDefinition12 = new AttributeDefinition();
		attributeDefinition12.setAttributeGroup("Product");
		attributeDefinition12.setAttributeName("productType");
		attributeDefinition12.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition12);

		AttributeDefinition attributeDefinition13 = new AttributeDefinition();
		attributeDefinition13.setAttributeGroup("Product");
		attributeDefinition13.setAttributeName("validityPeriod");
		attributeDefinition13.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition13);

		AttributeDefinition attributeDefinition14 = new AttributeDefinition();
		attributeDefinition14.setAttributeGroup("Product");
		attributeDefinition14.setAttributeName("monthEndCardExpiry");
		attributeDefinition14.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition14);

		AttributeDefinition attributeDefinition15 = new AttributeDefinition();
		attributeDefinition15.setAttributeGroup("Product");
		attributeDefinition15.setAttributeName("activeFrom");
		attributeDefinition15.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition15);

		AttributeDefinition attributeDefinition16 = new AttributeDefinition();
		attributeDefinition16.setAttributeGroup("Product");
		attributeDefinition16.setAttributeName("cutOverTime");
		attributeDefinition16.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition16);

		AttributeDefinition attributeDefinition17 = new AttributeDefinition();
		attributeDefinition17.setAttributeGroup("Product");
		attributeDefinition17.setAttributeName("denominationType");
		attributeDefinition17.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition17);

		AttributeDefinition attributeDefinition18 = new AttributeDefinition();
		attributeDefinition18.setAttributeGroup("Product");
		attributeDefinition18.setAttributeName("denomVarMin");
		attributeDefinition18.setAttributeValue("Y");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition18);

		AttributeDefinition attributeDefinition19 = new AttributeDefinition();
		attributeDefinition19.setAttributeGroup("Product");
		attributeDefinition19.setAttributeName("denomVarMax");
		attributeDefinition19.setAttributeValue("Y");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition19);

		AttributeDefinition attributeDefinition20 = new AttributeDefinition();
		attributeDefinition20.setAttributeGroup("Product");
		attributeDefinition20.setAttributeName("denomFixed");
		attributeDefinition20.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition20);

		AttributeDefinition attributeDefinition21 = new AttributeDefinition();
		attributeDefinition21.setAttributeGroup("Product");
		attributeDefinition21.setAttributeName("denomSelect");
		attributeDefinition21.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition21);

		AttributeDefinition attributeDefinition22 = new AttributeDefinition();
		attributeDefinition22.setAttributeGroup("Product");
		attributeDefinition22.setAttributeName("ccfFormatVersion");
		attributeDefinition22.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition22);

		AttributeDefinition attributeDefinition23 = new AttributeDefinition();
		attributeDefinition23.setAttributeGroup("Product");
		attributeDefinition23.setAttributeName("pinSupported");
		attributeDefinition23.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition23);

		AttributeDefinition attributeDefinition24 = new AttributeDefinition();
		attributeDefinition24.setAttributeGroup("Product");
		attributeDefinition24.setAttributeName("cvvSupported");
		attributeDefinition24.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition24);

		AttributeDefinition attributeDefinition25 = new AttributeDefinition();
		attributeDefinition25.setAttributeGroup("Product");
		attributeDefinition25.setAttributeName("limitSupported");
		attributeDefinition25.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition25);

		AttributeDefinition attributeDefinition26 = new AttributeDefinition();
		attributeDefinition26.setAttributeGroup("Product");
		attributeDefinition26.setAttributeName("feesSupported");
		attributeDefinition26.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition26);

		AttributeDefinition attributeDefinition27 = new AttributeDefinition();
		attributeDefinition27.setAttributeGroup("Product");
		attributeDefinition27.setAttributeName("alertSupported");
		attributeDefinition27.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition27);

		AttributeDefinition attributeDefinition28 = new AttributeDefinition();
		attributeDefinition28.setAttributeGroup("Product");
		attributeDefinition28.setAttributeName("otherTxnId");
		attributeDefinition28.setAttributeValue("Y");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition28);

		AttributeDefinition attributeDefinition29 = new AttributeDefinition();
		attributeDefinition29.setAttributeGroup("Product");
		attributeDefinition29.setAttributeName("cardStatusSupported");
		attributeDefinition29.setAttributeValue("Y");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition29);

		AttributeDefinition attributeDefinition30 = new AttributeDefinition();
		attributeDefinition30.setAttributeGroup("Product");
		attributeDefinition30.setAttributeName("activationId");
		attributeDefinition30.setAttributeValue("");
		attributeDefinitionDao.createAttributeDefinition(attributeDefinition30);
	}

	@Test
	public void test_CreateGroupAccessProducts_with_valid_AccessID() throws ServiceException{

		try {
			GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
			List<String> partnerList=new ArrayList<>();
			PartnerDTO Dto = new PartnerDTO();
			Dto.setPartnerName("venkat123");
			Dto.setPartnerDesc(" Java Developer");
			Dto.setIsActive("Y");
			Dto.setMdmId("123");
			partnerService.createPartner(Dto);
			partnerList.add(Dto.getPartnerId()+"");

			PartnerDTO Dto1 = new PartnerDTO();
			Dto1.setPartnerName("venkat");
			Dto1.setPartnerDesc(" Java Developer");
			Dto1.setIsActive("Y");
			Dto1.setMdmId("123");
			partnerService.createPartner(Dto1);
			partnerList.add(Dto1.getPartnerId()+"");

			PartnerDTO Dto2 = new PartnerDTO();
			Dto2.setPartnerName("venkat456");
			Dto2.setPartnerDesc(" Java Developer");
			Dto2.setIsActive("Y");
			Dto2.setMdmId("123");
			partnerService.createPartner(Dto2);
			partnerList.add(Dto2.getPartnerId()+"");

			groupAccessDTO.setPartnerList(partnerList);
			groupAccessDTO.setGroupAccessName("ABC123");
			groupAccessDTO.setInsUser(1L);
			groupAccessDTO.setLastUpdUser(1L);
			groupAccessService.createGroupAccess(groupAccessDTO);



			createMultipleAttributeDefinitions();

			IssuerDTO issuerDto = new IssuerDTO();
			issuerDto.setIssuerName("ABC Pvt Ltd");
			issuerDto.setIsActive("true");
			issuerDto.setInsUser(1);
			issuerService.createIssuer(issuerDto);
			assertEquals(1, issuerService.getAllIssuers().size());

			PartnerDTO partnerDto = new PartnerDTO();
			partnerDto.setPartnerName("ABC Shop");
			partnerDto.setMdmId("123456");
			partnerDto.setInsUser(1);
			partnerService.createPartner(partnerDto);

			CardRangeDTO cardRangeDto = new CardRangeDTO();
			cardRangeDto.setIssuerId(issuerDto.getIssuerId());
			cardRangeDto.setCardLength(new BigDecimal("12"));
			cardRangeDto.setPrefix("111345");
			cardRangeDto.setStartCardNbr("456789");
			cardRangeDto.setEndCardNbr("456790");
			cardRangeDto.setCardInventory("Y");
			cardRangeDto.setIsCheckDigitRequired("Y");
			cardRangeDto.setStatus("NEW");
			cardRangeDto.setInsUser(1);
			cardRangeService.createCardRange(cardRangeDto);

			List<String> cardRanges = new ArrayList<String>();
			cardRanges.add(String.valueOf(cardRangeDto.getCardRangeId()));

			PurseType pursetype = new PurseType();
			pursetype.setPurseTypeName("UPC");
			masterdao.createPurseType(pursetype);
			PurseDTO purseDto = new PurseDTO();
			purseDto.setPurseTypeId(pursetype.getPurseTypeId());
			purseDto.setDescription("Insert New Purse value");
			purseDto.setUpc("546798769454");
			purseDto.setInsUser(1);
			purseDto.setLastUpdUser(1);
			purseService.createPurse(purseDto);

			List<String> supportedPurse = new ArrayList<String>();
			supportedPurse.add(purseDto.getPurseId().toString());

			Map<String, Map<String, Object>> attributesMap = new HashMap<String, Map<String, Object>>();
			Map<String, Object> productMap = new HashMap<String, Object>();
			productMap.put("b2bSerialNumAutoReplenishVal", "Y");
			productMap.put("alertSupported", "Disable");
			attributesMap.put("Product", productMap);
			ProductDTO productDto = new ProductDTO();
			productDto.setProductName("HP Pro Desk");
			productDto.setProductShortName("HP Pro Desk");
			productDto.setDescription("HP Pro Desk CPU");
			productDto.setIsActive("Y");
			productDto.setIssuerId(issuerDto.getIssuerId());
			productDto.setPartnerId(partnerDto.getPartnerId());
			productDto.setParentProductId(null);
			productDto.setPackageIds(new ArrayList<String>());
			productDto.setCardRanges(cardRanges);
			productDto.setSupportedPurse(supportedPurse);
			productDto.setAttributesMap(attributesMap);
			productDto.setInsUser(1L);
			productDto.setLastUpdUser(1L);

			productService.createProduct(productDto);
			 int partnerCnt=partnerList.size();
			 GroupAccessPartnerDTO [] partnerArray=new GroupAccessPartnerDTO[partnerCnt]; 
			 int incrCnt=0;
			for (Iterator<String> iterator = partnerList.iterator(); iterator.hasNext();) {
				String partnerId =  iterator.next();
				if(partnerCnt!=incrCnt){
					GroupAccessPartnerDTO groupAccessPartnerDTO=new GroupAccessPartnerDTO();
					groupAccessPartnerDTO.setGroupAccessId(groupAccessDTO.getGroupAccessId());
					groupAccessPartnerDTO.setPartnerId(Long.valueOf(partnerId));
					groupAccessPartnerDTO.setPartnerPartyType("THIRD PARTY");
					partnerArray[incrCnt]=groupAccessPartnerDTO;
				}
				incrCnt+=1;
			}
			groupAccessDTO.setPartnerArray(partnerArray);
			groupAccessDTO.setProductId(productDto.getProductId());
			groupAccessService.createGroupAccessProducts(groupAccessDTO);

		} catch (ServiceException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			assertEquals(ResponseMessages.ERR_GROUP_ACCESS_DOESNT_EXIST, e.getMessage());
		}
		catch (Exception e) {
			e.printStackTrace();
		}

	}



}
