/**
 * 
 */
package com.incomm.cclp.service.impl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.incomm.cclp.constants.CCLPConstants;
import com.incomm.cclp.constants.ResponseMessages;
import com.incomm.cclp.controller.GroupAccessController;
import com.incomm.cclp.dao.GroupAccessDAO;
import com.incomm.cclp.dao.PartnerDAO;
import com.incomm.cclp.dao.ProductDAO;
import com.incomm.cclp.domain.GroupAccess;
import com.incomm.cclp.domain.GroupAccessPartner;
import com.incomm.cclp.domain.GroupAccessPartnerID;
import com.incomm.cclp.domain.GroupAccessProduct;
import com.incomm.cclp.domain.Partner;
import com.incomm.cclp.domain.Product;
import com.incomm.cclp.dto.GroupAccessDTO;
import com.incomm.cclp.dto.GroupAccessPartnerDTO;
import com.incomm.cclp.dto.PartnerDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;
import com.incomm.cclp.service.GroupAccessService;
import com.incomm.cclp.util.ResponseBuilder;
import com.incomm.cclp.util.ValidationService;

/**
 * Group Access Service provides all the Service operations for Group Access.  
 *
 */

@Service
public class GroupAccessServiceImpl implements GroupAccessService {

	@Autowired
	GroupAccessDAO groupAccessDAO;

	@Autowired
	PartnerDAO partnerDAO;
	
	@Autowired
	ProductDAO productDao;
	@Autowired
	ResponseBuilder responseBuilder;
	
	@Autowired
	DistributedCacheServiceImpl distributedCacheService;
	
	// the logger
	private static final Logger logger = LogManager.getLogger(GroupAccessServiceImpl.class);
	
	/**
	 * Getting List of Group access_products
	 * 
	 * */
	@Override
	public List<GroupAccessDTO> getGroupAccessProducts(String groupAccessName,String productName){
		return convertGroupAccessProductsEntityToDTOSearch(groupAccessDAO.getGroupAccessProducts(groupAccessName,productName));
	}

	/**
	 * Converting the group access products entity to DTO list
	 * @param groupAccessProduct
	 */
	public List<GroupAccessDTO> convertGroupAccessProductsEntityToDTO(List<GroupAccessProduct> groupAccessProduct,long productId){
		logger.info(CCLPConstants.ENTER);
		long partnerId=0;
		List<GroupAccessDTO> groupAccessProductDtoList=new ArrayList<>();
		if(groupAccessProduct!=null && !groupAccessProduct.isEmpty()){
			if(productId>0){
				Product product=productDao.getProductById(productId);
				if(product!=null && product.getPartner()!=null){
					partnerId=product.getPartner().getPartnerId();
				}
			}
			for (Iterator<GroupAccessProduct> iterator = groupAccessProduct.iterator(); iterator
					.hasNext();) {
				GroupAccessProduct groupAccessProd =  iterator.next();
				GroupAccessDTO groupAccessDTO=new GroupAccessDTO();	
				if(groupAccessProd.getGroupAccess()!=null){
					groupAccessDTO.setGroupAccessId(groupAccessProd.getGroupAccess().getGroupAccessId());
					groupAccessDTO.setGroupAccessName(groupAccessProd.getGroupAccess().getGroupAccessName());
				}
				if(groupAccessProd.getProduct()!=null){
					groupAccessDTO.setProductId(groupAccessProd.getProduct().getProductId());
					groupAccessDTO.setProductName(groupAccessProd.getProduct().getProductName());
				}
				if(groupAccessProd.getPartner()!=null){
					if(partnerId!=0 && partnerId==groupAccessProd.getPartner().getPartnerId()){
						groupAccessDTO.setPartnerPartyType("FIRST PARTY OWNER");
					}
					else{
						groupAccessDTO.setPartnerPartyType(groupAccessProd.getPartnerPartyType());
					}
					groupAccessDTO.setPartnerId(groupAccessProd.getPartner().getPartnerId());
					groupAccessDTO.setPartnerName(groupAccessProd.getPartner().getPartnerName());
				}
				groupAccessProductDtoList.add(groupAccessDTO);
				
			}

		}
		logger.info(CCLPConstants.EXIT);
		return groupAccessProductDtoList;
	}

	/**
	 * Converting the group access products entity to DTO list
	 * @param groupAccessProduct
	 */
	public List<GroupAccessDTO> convertGroupAccessProductsEntityToDTOSearch(List<GroupAccessProduct> groupAccessProduct){
		logger.info(CCLPConstants.ENTER);
		List<GroupAccessDTO> groupAccessProductDtoList=new ArrayList<>();

		if(groupAccessProduct!=null && !groupAccessProduct.isEmpty()){
			List<GroupAccessProduct> newList=new ArrayList<>();
			newList.addAll(groupAccessProduct);
			for (Iterator<GroupAccessProduct> iterator = groupAccessProduct.iterator(); iterator.hasNext();) {
				List<PartnerDTO> partnerDtoList=new LinkedList<>();
				GroupAccessProduct groupAccessProduct1 =  iterator.next();
				boolean flag=false;
				for (Iterator<GroupAccessProduct> iterator2 = newList.iterator(); iterator2.hasNext();) {
					GroupAccessProduct groupAccessProduct2 =  iterator2.next();
					
					if(groupAccessProduct2.getGroupAccess().getGroupAccessId().equals(groupAccessProduct1.getGroupAccess().getGroupAccessId())  && groupAccessProduct2.getProduct().getProductId().equals(groupAccessProduct1.getProduct().getProductId())){
						PartnerDTO partnerDTO=new PartnerDTO();
						partnerDTO.setPartnerId(groupAccessProduct2.getPartner().getPartnerId());
						partnerDTO.setPartnerName(groupAccessProduct2.getPartner().getPartnerName());
						partnerDtoList.add(partnerDTO);
						iterator2.remove();
						flag=true;
					}
				}
				logger.info("groupAccessName: {}, ProductId: {}",groupAccessProduct1.getGroupAccess().getGroupAccessName(),groupAccessProduct1.getProduct().getProductId());
				logger.info("{} flag: {}",groupAccessProduct1.getPartnerPartyType(),flag);
				if(flag){
					GroupAccessDTO groupAccessDTO=new GroupAccessDTO();	
					if(groupAccessProduct1.getGroupAccess()!=null){
						groupAccessDTO.setGroupAccessId(groupAccessProduct1.getGroupAccess().getGroupAccessId());
						groupAccessDTO.setGroupAccessName(groupAccessProduct1.getGroupAccess().getGroupAccessName());
					}
					groupAccessDTO.setGroupAccessPartnerList(partnerDtoList);
					if(groupAccessProduct1.getProduct()!=null){
						groupAccessDTO.setProductId(groupAccessProduct1.getProduct().getProductId());
						groupAccessDTO.setProductName(groupAccessProduct1.getProduct().getProductName());
					}
					groupAccessProductDtoList.add(groupAccessDTO);
				}
				
			}

		}
		logger.info(CCLPConstants.EXIT);
		return groupAccessProductDtoList;
	}

	/**
	 * Getting List of Group access
	 * 
	 * */
	@Override
	public List<GroupAccessDTO> getGroupAccess(String groupAccessName) {
		logger.info(CCLPConstants.ENTER);
		List<GroupAccess> groupAccessList=groupAccessDAO.getGroupAccess(groupAccessName);

		List<GroupAccessDTO> groupAccessDtoList=new LinkedList<>();
		if(groupAccessList!=null && !groupAccessList.isEmpty()){
			groupAccessDtoList=	groupAccessList.stream().map(groupAccess->{
				GroupAccessDTO groupAccessDTO=new GroupAccessDTO();	
				groupAccessDTO.setGroupAccessId(groupAccess.getGroupAccessId());
				groupAccessDTO.setGroupAccessName(groupAccess.getGroupAccessName());
				groupAccessDTO.setGroupAccessPartnerList(getListOfPartners(groupAccess.getGroupAccessPartnerList()));
				groupAccessDTO.setInsUser(groupAccess.getInsUser());
				groupAccessDTO.setInsDate(groupAccess.getInsDate());
				groupAccessDTO.setLastUpdUser(groupAccess.getLastUpdUser());
				groupAccessDTO.setLastUpdDate(groupAccess.getLastUpdDate());
				return groupAccessDTO;
			}
					).collect(Collectors.toList());
		}
		logger.info(CCLPConstants.EXIT);
		return groupAccessDtoList;
	}

	/**
	 * Converting group of groupAccessPartners to PartnerDTO
	 * @param groupAccessPartnersList
	 * @return
	 */
	private List<PartnerDTO> getListOfPartners(List<GroupAccessPartner> groupAccessPartnersList){
		logger.info(CCLPConstants.ENTER);
		List<PartnerDTO> partnersList = new ArrayList<>();
		ModelMapper mm= new ModelMapper();
		Type tokens = new TypeToken<PartnerDTO>() {}.getType();
		if (!CollectionUtils.isEmpty(groupAccessPartnersList)) {
			groupAccessPartnersList.forEach(groupAccessPartner -> {
				Partner partner=groupAccessPartner.getPartner();
				if(partner!=null){
					logger.info("partnerName: {}",partner.getPartnerName());
					partnersList.add(mm.map(partner,tokens));
				}
			});
		}
		logger.info(CCLPConstants.EXIT);
		return partnersList;
	}

	/**
	 * Getting List of Group access_partners
	 * 
	 * */
	@Override
	public List<GroupAccessDTO> getGroupAccessPartners(){
		return convertGroupAccessPartnerEntityToDto(groupAccessDAO.getGroupAccessPartners(),0); 
	}

	/**
	 * Converting list of groupAccessPartners Entity to DTO
	 * @param groupAccessPartner productId
	 * @return List<GroupAccessDTO>
	 */
	public List<GroupAccessDTO> convertGroupAccessPartnerEntityToDto(List<GroupAccessPartner> groupAccessPartner,long productId){
		logger.info(CCLPConstants.ENTER);
		List<GroupAccessDTO> groupAccessPartnerDtoList=new LinkedList<>();
		long partnerId=0;
		if(groupAccessPartner!=null && !groupAccessPartner.isEmpty()){
			if(productId>0){
				Product product=productDao.getProductById(productId);
				if(product!=null && product.getPartner()!=null){
					partnerId=product.getPartner().getPartnerId();
				}
			}
			for (Iterator<GroupAccessPartner> iterator = groupAccessPartner.iterator(); iterator
					.hasNext();) {
				GroupAccessPartner groupAccessPart = iterator.next();
				GroupAccessDTO groupAccessDTO=new GroupAccessDTO();	
				if(groupAccessPart.getGroupAccessPartnerID().getGroupAccess()!=null){
					groupAccessDTO.setGroupAccessId(groupAccessPart.getGroupAccessPartnerID().getGroupAccess().getGroupAccessId());
					groupAccessDTO.setGroupAccessName(groupAccessPart.getGroupAccessPartnerID().getGroupAccess().getGroupAccessName());
				}
				if(groupAccessPart.getGroupAccessPartnerID().getPartner()!=null){
					if(partnerId!=0 && partnerId==groupAccessPart.getGroupAccessPartnerID().getPartner().getPartnerId()){
						groupAccessDTO.setPartnerPartyType("FIRST PARTY OWNER");
					}
					groupAccessDTO.setPartnerId(groupAccessPart.getGroupAccessPartnerID().getPartner().getPartnerId());
					groupAccessDTO.setPartnerName(groupAccessPart.getGroupAccessPartnerID().getPartner().getPartnerName());
				}
				
				groupAccessPartnerDtoList.add(groupAccessDTO);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return groupAccessPartnerDtoList;
	}

	/**
	 * Getting List of Group access_partners by AccessId
	 * 
	 * */
	@Override
	public List<GroupAccessDTO> getGroupAccessPartnersByAccessId(Long groupAccessId)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		if(groupAccessId<=0){
			logger.error("Group access Id is null");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_ID_NULL);
		}
		logger.info(CCLPConstants.EXIT);
		return convertGroupAccessPartnerEntityToDto(groupAccessDAO.getGroupAccessPartnersByAccessId(groupAccessId),0);
	}

	/**
	 * Create Group Access  
	 * 
	 * */
	@Override
	public void createGroupAccess(GroupAccessDTO groupAccessDTO)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ValidationService.validateGroupAccess(groupAccessDTO, true);
		List<GroupAccessDTO> groupAccessList=getGroupAccess(groupAccessDTO.getGroupAccessName());
		if(groupAccessList!=null && !groupAccessList.isEmpty()){
			logger.error("Group access name already exists");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_NAME_ALREADY_EXIST);
		}
		GroupAccess groupAccess=convertGroupAccessDtoToEntity(groupAccessDTO);
		groupAccessDAO.createGroupAccess(groupAccess);
		groupAccessDTO.setGroupAccessId(groupAccess.getGroupAccessId());
		logger.info(CCLPConstants.EXIT);
	}
	/**
	 * Convert Group Access DTO to Entity
	 * @param groupAccessDTO
	 * @return GroupAccess
	 */
	public GroupAccess convertGroupAccessDtoToEntity(GroupAccessDTO groupAccessDTO){
		logger.info(CCLPConstants.ENTER);
		GroupAccess groupAccess=new GroupAccess();
		groupAccess.setGroupAccessName(groupAccessDTO.getGroupAccessName());
		groupAccess.setInsUser(groupAccessDTO.getInsUser());
		groupAccess.setLastUpdUser(groupAccessDTO.getLastUpdUser());
		groupAccess.setGroupAccessPartnerList(getGroupAccessParterList(groupAccessDTO.getPartnerList(),groupAccess));
		logger.info(CCLPConstants.EXIT);
		return groupAccess;
	}



	/**
	 * Getting the list of group access partners by partnerId list
	 * @param partnerList
	 * @param groupAccess
	 * @return List<GroupAccessPartner>
	 */
	private List<GroupAccessPartner> getGroupAccessParterList(List<String> partnerList,GroupAccess groupAccess){
		logger.info(CCLPConstants.ENTER);
		List<GroupAccessPartner> groupAccessPartnerList=new ArrayList<>();
		if (!CollectionUtils.isEmpty(partnerList)) {
			for(String partnerId:partnerList){
				GroupAccessPartner groupAccessPartner=new GroupAccessPartner();
				GroupAccessPartnerID groupAccessPartnerID=new GroupAccessPartnerID();

				Partner partner=partnerDAO.getPartnerById(Long.valueOf(partnerId));
				groupAccessPartnerID.setPartner(partner);

				groupAccessPartnerID.setGroupAccess(groupAccess);

				groupAccessPartner.setGroupAccessPartnerID(groupAccessPartnerID);
				groupAccessPartner.setInsUser(groupAccess.getInsUser());
				groupAccessPartner.setLastUpdUser(groupAccess.getLastUpdUser());
				groupAccessPartnerList.add(groupAccessPartner);
			}
		}
		logger.info(CCLPConstants.EXIT);
		return groupAccessPartnerList;

	}

	/**
	 * Create Group Access Products
	 * 
	 * */
	@Override
	@Transactional
	public void createGroupAccessProducts(GroupAccessDTO groupAccessDTO)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ValidationService.validateGroupAccessProduct(groupAccessDTO, true);
		

		if(groupAccessDTO.getPartnerArray()!=null && groupAccessDTO.getPartnerArray().length>0){
			
			GroupAccessPartnerDTO[] groupAccessPartners=groupAccessDTO.getPartnerArray();
			
			for (int i = 0; i < groupAccessPartners.length; i++) {
				GroupAccessPartnerDTO groupAccessPartnerDTO=groupAccessPartners[i];

				
				int productAccessCnt = 0;
				try {
					productAccessCnt = groupAccessDAO.getGroupAccessProductsCountByProductId(groupAccessDTO.getProductId(),groupAccessPartnerDTO.getPartnerId());	
				}catch(Exception e) {
					logger.error("Product group access already exists: {}",e);
					throw new ServiceException(ResponseMessages.ERR_PRODUCT_GROUP_ACCESS_ALREADY_EXIST);
				}
				
				
				logger.debug("product partner exist check - productId-" + groupAccessDTO.getProductId()
				 + " partnerId - " + groupAccessPartnerDTO.getPartnerId()
				 + " count = " + productAccessCnt );
				logger.info("productAccessCnt: {}",productAccessCnt);
					if(productAccessCnt>0){
						logger.error("Product group access already exists");
						throw new ServiceException(ResponseMessages.ERR_PRODUCT_GROUP_ACCESS_ALREADY_EXIST);
				}

			}
			
			doBatchGroupAccessProducts(groupAccessDTO);
			
			//Adding PartyType to Distributed Cache starts
			addPartyTypeToCache(groupAccessDTO.getProductId(),false);
			//Adding PartyType to Distributed Cache ends	
			
		}//PartnerArray
		GroupAccess groupAccessExisting=groupAccessDAO.getGroupAccessById(groupAccessDTO.getGroupAccessId());
		if(groupAccessExisting!=null)
			groupAccessDTO.setGroupAccessName(groupAccessExisting.getGroupAccessName());
		logger.info(CCLPConstants.EXIT);
	}
	
	
	public void doBatchGroupAccessProducts(GroupAccessDTO groupAccessDTO){
		logger.info(CCLPConstants.ENTER);
		GroupAccessPartnerDTO[] groupAccessPartners=groupAccessDTO.getPartnerArray();
		Product product=productDao.getProductById(groupAccessDTO.getProductId());
		groupAccessDTO.setProductName(product.getProductName());
		for (int i = 0; i < groupAccessPartners.length; i++) {
			
			GroupAccessPartnerDTO groupAccessPartnerDTO=groupAccessPartners[i];

			GroupAccessProduct groupAccessProduct=new GroupAccessProduct();

			groupAccessProduct.setProduct(product);

			GroupAccess groupAccess=new GroupAccess();
			groupAccess.setGroupAccessId(groupAccessDTO.getGroupAccessId());
			groupAccessProduct.setGroupAccess(groupAccess);

			groupAccessProduct.setInsUser(groupAccessDTO.getInsUser());
			groupAccessProduct.setLastUpdUser(groupAccessDTO.getLastUpdUser());

			Partner partner=new Partner();
			partner.setPartnerId(groupAccessPartnerDTO.getPartnerId());
			groupAccessProduct.setPartner(partner);
			groupAccessProduct.setInsDate(new java.util.Date());
			groupAccessProduct.setLastUpdDate(new java.util.Date());
			groupAccessProduct.setPartnerPartyType(groupAccessPartnerDTO.getPartnerPartyType());

				groupAccessDAO.createGroupAccessProduct(groupAccessProduct); 
		}
		logger.info(CCLPConstants.EXIT);
	}

	/**
	 * Update Group Access Products
	 * 
	 * */
	@Override
	@Transactional
	public void updateGroupAccessProducts(GroupAccessDTO groupAccessDTO)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ValidationService.validateGroupAccessProduct(groupAccessDTO, false);
		
		if(groupAccessDTO.getPartnerArray()!=null && groupAccessDTO.getPartnerArray().length>0){
			logger.info("Deleting product group access");
			groupAccessDAO.deleteGroupAccessProduct(groupAccessDTO.getGroupAccessId(),groupAccessDTO.getProductId());

			doBatchGroupAccessProducts(groupAccessDTO);
			//Adding PartyType to Distributed Cache starts
			addPartyTypeToCache(groupAccessDTO.getProductId(),false);
			//Adding PartyType to Distributed Cache ends	
		}
		GroupAccess groupAccessExisting=groupAccessDAO.getGroupAccessById(groupAccessDTO.getGroupAccessId());
		if(groupAccessExisting!=null)
			groupAccessDTO.setGroupAccessName(groupAccessExisting.getGroupAccessName());
		logger.info(CCLPConstants.EXIT);
	}
	/**
	 * Delete Group Access Products
	 * 
	 * */
	@Override
	public GroupAccessDTO deleteGroupAccessProducts(Long groupAccessId,Long productId)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		List<GroupAccessProduct> groupAccessProdExistingList=groupAccessDAO.getGroupAccessProductsByAccessAndProductId(groupAccessId,productId);
		Long productAccessCnt = groupAccessProdExistingList.stream().count();
		if(productAccessCnt<1){
			logger.error("Group access not present");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_DOESNT_EXIST);
		}
		int deleteCnt=groupAccessDAO.deleteGroupAccessProduct(groupAccessId,productId);
		if(deleteCnt==0){
			logger.error("Error while deleting product group access");
			throw new ServiceException(ResponseMessages.ERR_DELETE_PRODUCT_GROUP_ACCESS);
		}
		GroupAccess groupAccessExisting=groupAccessDAO.getGroupAccessById(groupAccessId);
		GroupAccessDTO groupAccessDto=new GroupAccessDTO();
		if(groupAccessExisting!=null){
		groupAccessDto.setGroupAccessName(groupAccessExisting.getGroupAccessName());}
		Product product=productDao.getProductById(productId);
		groupAccessDto.setProductName(product.getProductName());
		//Adding PartyType to Distributed Cache starts
		addPartyTypeToCache(productId,true);
		//Adding PartyType to Distributed Cache ends	
		logger.info(CCLPConstants.EXIT);
		return groupAccessDto;
	}

	/**
	 * Getting list of Group Access Products By access/Product ID
	 */
	public List<GroupAccessDTO> getGroupAccessProductsByAccessAndProductId(Long groupAccessId,Long productId){
		return convertGroupAccessProductsEntityToDTO(groupAccessDAO.getGroupAccessProductsByAccessAndProductId(groupAccessId,productId),productId);
	}

	/**
	 * Update Group Access 
	 */

	@Override
	@Transactional
	public ResponseDTO updateGroupAccess(GroupAccessDTO groupAccessDTO)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		ResponseDTO responseDTO=null;
		ValidationService.validateGroupAccess(groupAccessDTO, false);

		GroupAccess groupAccessExisting=groupAccessDAO.getGroupAccessById(groupAccessDTO.getGroupAccessId());
		if(groupAccessExisting==null){
			logger.error("Group access not present");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_DOESNT_EXIST);
		}
		List<GroupAccessDTO> groupAccessList=getGroupAccess(groupAccessExisting.getGroupAccessName());

		Long accessNameExistingCnt = groupAccessList.stream()
				.filter(groupAccess -> !groupAccess.getGroupAccessId().equals(groupAccess.getGroupAccessId()))
				.count();
		if(accessNameExistingCnt>0){
			logger.error("Group access already present");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_NAME_ALREADY_EXIST);
		}
		List<String> newPartnersList=groupAccessDTO.getPartnerList();

		List<GroupAccessProduct> groupAccessProductList=groupAccessDAO.getGroupAccessProductsByAccessId(groupAccessExisting.getGroupAccessId());

		List<String> existingPartnerList=new ArrayList<>();
		Set<String> existingProductList=new HashSet<>();

		List<String> deleteList=new ArrayList<>();

		List<String> insertList=new ArrayList<>();

		if(groupAccessProductList!=null && !groupAccessProductList.isEmpty()){
			for (Iterator<GroupAccessProduct> iterator = groupAccessProductList.iterator(); iterator.hasNext();) {
				GroupAccessProduct groupAccessProduct =  iterator.next();
				existingPartnerList.add(String.valueOf(groupAccessProduct.getPartner().getPartnerId()));
				existingProductList.add(String.valueOf(groupAccessProduct.getProduct().getProductId()));
			}
			for(String newPartner:newPartnersList){
				if(!existingPartnerList.contains(newPartner)){
					insertList.add(newPartner);
				}
			}

			for(String existingPartner:existingPartnerList){
				if(!newPartnersList.contains(existingPartner)){
					deleteList.add(existingPartner);
				}
			}
		}
		
		logger.info("Getting partners list");
		List<Object[]> productList=groupAccessDAO.getPartnersListByAccessId(groupAccessExisting.getGroupAccessId());
		
		Set<String> partnerDeselectList=new HashSet<>();
		if(productList!=null && !productList.isEmpty()){
			for (Iterator<Object[]> iterator = productList.iterator(); iterator.hasNext();) {
				Object[] objects = iterator.next();
				if(deleteList.contains(objects[0]+"")){
					partnerDeselectList.add(objects[1]+"");
				}
			}
			if(!partnerDeselectList.isEmpty()){
				responseDTO=responseBuilder.buildFailureResponse(ResponseMessages.ERR_CANNOT_REMOVE_PARTNERS_LINKED_TO_PRODUCT,ResponseMessages.ERR_CANNOT_REMOVE_PARTNERS_LINKED_TO_PRODUCT);
				Map<String,String> valuesMap = new HashMap<>();
				valuesMap.put("partnerList", String.join(",", partnerDeselectList));
				responseDTO.setMessage(new StrSubstitutor(valuesMap).replace(responseDTO.getMessage()));
				return responseDTO;
			}
		}

		GroupAccess groupAccess=convertGroupAccessDtoToEntity(groupAccessDTO);
		groupAccess.setGroupAccessId(groupAccessExisting.getGroupAccessId());
		logger.info("Updating group access");
		groupAccessDAO.updateGroupAccess(groupAccess);
		
		if(!insertList.isEmpty()){
			
			existingProductList.stream().forEach(productid ->
				insertList.stream().forEach(partnerId -> 
				{
					GroupAccessProduct groupAccessProduct=new GroupAccessProduct();

					Product product=new Product();
					product.setProductId(Long.valueOf(productid));
					groupAccessProduct.setProduct(product);

					GroupAccess groupAccessObj=new GroupAccess();
					groupAccessObj.setGroupAccessId(groupAccessDTO.getGroupAccessId());
					groupAccessProduct.setGroupAccess(groupAccess);

					groupAccessProduct.setInsUser(groupAccessDTO.getInsUser());
					groupAccessProduct.setLastUpdUser(groupAccessDTO.getLastUpdUser());

					Partner partner=new Partner();
					partner.setPartnerId(Long.valueOf(partnerId));
					groupAccessProduct.setPartner(partner);
					groupAccessProduct.setInsDate(new java.util.Date());
					groupAccessProduct.setLastUpdDate(new java.util.Date());
					groupAccessProduct.setPartnerPartyType("THIRD PARTY");
					groupAccessDAO.createGroupAccessProduct(groupAccessProduct); 
				}
				)
			);
		}
		if(!deleteList.isEmpty()){
			logger.info("Deleting bulk group access product");
			groupAccessDAO.deleteBulkGroupAccessProduct(deleteList,groupAccessDTO.getGroupAccessId());
		}
		logger.info(CCLPConstants.EXIT);
		return responseDTO;
	}

	/**
	 * Getting List of Group access_partners by AccessId
	 * 
	 * */
	@Override
	public GroupAccessDTO getGroupAccessById(Long groupAccessId)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		if(groupAccessId<=0){
			logger.error("Group access id is null");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_ID_NULL);
		}

		GroupAccess groupAccess=groupAccessDAO.getGroupAccessById(groupAccessId);

		if(groupAccess==null){
			logger.error("Group access not present");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_DOESNT_EXIST);
		}
		logger.info(CCLPConstants.EXIT);
		return convertGroupAccessEntityToDto(groupAccess);
	}
	/**
	 * Converting Group Access Entity to Dto
	 * @param groupAccess
	 * @return GroupAccessDTO
	 */
	public GroupAccessDTO convertGroupAccessEntityToDto(GroupAccess groupAccess){
		logger.info(CCLPConstants.ENTER);
		GroupAccessDTO groupAccessDTO=new GroupAccessDTO();
		groupAccessDTO.setGroupAccessId(groupAccess.getGroupAccessId());
		groupAccessDTO.setGroupAccessName(groupAccess.getGroupAccessName());
		groupAccessDTO.setGroupAccessPartnerList(getListOfPartners(groupAccess.getGroupAccessPartnerList()));
		logger.info(CCLPConstants.EXIT);
		return groupAccessDTO;
	}

	/**
	 * Getting the products of the group access partners
	 * @param groupAccessId
	 * @return Map<String,String>
	 */
	public Map<String,String> getProductsByAccessId(Long groupAccessId){
		logger.info(CCLPConstants.ENTER);
		List<Object[]> productList=groupAccessDAO.getProductsByAccessId(groupAccessId);
		Map<String,String> productMap=new HashMap<>();
		if(productList!=null && !productList.isEmpty()){
			for (Iterator<Object[]> iterator = productList.iterator(); iterator.hasNext();) {
				Object[] objects = iterator.next();
				productMap.put(objects[0]+"", objects[1]+"");
			}
		}
		logger.info(CCLPConstants.EXIT);
		return productMap;
	}
	
	/**
	 * Getting List of Group access_partners by AccessId
	 * 
	 * */
	@Override
	public List<GroupAccessDTO> getGroupAccessPartnersByAccessIdAndProductId(Long groupAccessId,Long productId)throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		if(groupAccessId<=0){
			logger.error("Group access ID is null");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_ID_NULL);
		}
		if(productId<=0){
			logger.error("Product ID is null");
			throw new ServiceException(ResponseMessages.ERR_PRODUCT_ID_NULL);
		}
		logger.info(CCLPConstants.EXIT);
		return convertGroupAccessPartnerEntityToDto(groupAccessDAO.getGroupAccessPartnersByAccessId(groupAccessId),productId);
	}
	/**
	 * Getting List of Group access_products by AccessId
	 * 
	 * */
	public List<GroupAccessDTO> getGroupAccessProductsByAccessId(Long groupAccessId) throws ServiceException{
		logger.info(CCLPConstants.ENTER);
		if(groupAccessId<=0){
			logger.error("Group access ID is null");
			throw new ServiceException(ResponseMessages.ERR_GROUP_ACCESS_ID_NULL);
		}
		logger.info(CCLPConstants.EXIT);
		return convertGroupAccessProductsEntityToDTOSearch(groupAccessDAO.getGroupAccessProductsByAccessId(groupAccessId));
	}
	
	/**
	 * getGroupAccessProdsDetails provides all the PartyTypes linked to product
	 * @author venkateshgaddam
	 */
	@Override
	public Map<String,Map<String,Object>> getGroupAccessProdsDetails(Long productId){
		logger.info(CCLPConstants.ENTER);
		Map<String,Map<String,Object>> grpAccProdMapOut=new HashMap<>();
		try {
			List<Object[]> groupAccessProdsList=groupAccessDAO.getGroupAccessProdByProductId(productId);
			if(groupAccessProdsList!=null && !groupAccessProdsList.isEmpty()){
				groupAccessProdsList.stream().forEach(grpAccProd->{
					if(grpAccProdMapOut.get(grpAccProd[0]+"")==null){
						grpAccProdMapOut.put(grpAccProd[0]+"", new HashMap<String,Object>());
						grpAccProdMapOut.get(grpAccProd[0]+"").put(grpAccProd[1]+"", grpAccProd[2]);
					}
					else{
						grpAccProdMapOut.get(grpAccProd[0]+"").put(grpAccProd[1]+"", grpAccProd[2]);
					}
				});
			}
		} catch (Exception e) {
			logger.error("Exception in getGroupAccessProdsDetails:"+e);
		}
		logger.info(CCLPConstants.EXIT);
		return 	grpAccProdMapOut;
	}
	
	/**
	 * Adding PartyType to DistributedCache on Add/Update of Assign Product 
	 * @author venkateshgaddam
	 */
	private void addPartyTypeToCache(long productId,boolean isDelete){
		logger.info(CCLPConstants.ENTER);
		Map<String, Map<String, Object>> productAttr=distributedCacheService.getOrAddProductAttributesCache(productId, null);
		if(!CollectionUtils.isEmpty(productAttr)){
			Map<String,Map<String,Object>> grpAccessProdsMap=getGroupAccessProdsDetails(productId);
			if(!CollectionUtils.isEmpty(grpAccessProdsMap)){
				productAttr.put(CCLPConstants.PARTY_TYPE, grpAccessProdsMap.get(String.valueOf(productId)));
				distributedCacheService.updateProductAttributesCache(productId, productAttr);
			}
			else if(isDelete){
				productAttr.remove(CCLPConstants.PARTY_TYPE);
				distributedCacheService.updateProductAttributesCache(productId, productAttr);
			}
		}
		logger.info(CCLPConstants.EXIT);
	}

}


