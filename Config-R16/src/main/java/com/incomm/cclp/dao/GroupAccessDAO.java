
package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.GroupAccess;
import com.incomm.cclp.domain.GroupAccessPartner;
import com.incomm.cclp.domain.GroupAccessProduct;
import com.incomm.cclp.exception.ServiceException;


public interface GroupAccessDAO {
	public List<GroupAccessProduct> getGroupAccessProducts(String groupAccessName
			, String productName);
	
	public List<GroupAccess> getGroupAccess(String groupAccessName);
	
	public List<GroupAccessPartner> getGroupAccessPartners();
	
	public List<GroupAccessPartner> getGroupAccessPartnersByAccessId(Long groupAccessId);
	
	public void createGroupAccess(GroupAccess groupAccess);
	
	public void updateGroupAccess(GroupAccess groupAccess);
	
	public GroupAccess getGroupAccessById(Long groupAccessId);
	
	
	public int deleteGroupAccessProduct(Long groupAccessId,Long productId)throws ServiceException;
	
	public List<GroupAccessProduct> getGroupAccessProductsByAccessAndProductId(Long groupAccessId,Long productId);
	
	public List<GroupAccessProduct> getGroupAccessProductsByAccessId(Long groupAccessId);
	
	public int deleteBulkGroupAccessProduct(List<String> groupAccessIdList,Long groupAccessId);
	
	public List<Object[]> getProductsByAccessId(Long groupAccessId);
	
	public void createGroupAccessProduct(GroupAccessProduct groupAccessProduct);
	
	public List<Object[]> getPartnersListByAccessId(Long groupAccessId);

	int getGroupAccessProductsCountByProductId(Long productId,Long partnerId);
	
	public List<Object[]> getGroupAccessProdByProductId(Long productId);
	
}
