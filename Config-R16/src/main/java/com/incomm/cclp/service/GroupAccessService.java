/**
 * 
 */
package com.incomm.cclp.service;

import java.util.List;
import java.util.Map;

import com.incomm.cclp.dto.GroupAccessDTO;
import com.incomm.cclp.dto.ResponseDTO;
import com.incomm.cclp.exception.ServiceException;



public interface GroupAccessService {

	public List<GroupAccessDTO> getGroupAccessProducts(String groupAccessName,String productName);
	
	public List<GroupAccessDTO> getGroupAccess(String groupAccessName);
	
	public List<GroupAccessDTO> getGroupAccessPartners();
	
	public List<GroupAccessDTO> getGroupAccessPartnersByAccessId(Long groupAccessId) throws ServiceException;
	
	public void createGroupAccess(GroupAccessDTO groupAccessDTO)throws ServiceException;
	
	public void createGroupAccessProducts(GroupAccessDTO groupAccessDTO)throws ServiceException;
	
	public ResponseDTO updateGroupAccess(GroupAccessDTO groupAccessDTO)throws ServiceException;
	
	public GroupAccessDTO getGroupAccessById(Long groupAccessId) throws ServiceException;
	
	public void updateGroupAccessProducts(GroupAccessDTO groupAccessDTO)throws ServiceException;
	
	public GroupAccessDTO deleteGroupAccessProducts(Long groupAccessId,Long productId)throws ServiceException;
	
	public List<GroupAccessDTO> getGroupAccessProductsByAccessAndProductId(Long groupAccessId,Long productId);
	
	public Map<String,String> getProductsByAccessId(Long groupAccessId);
	
	public List<GroupAccessDTO> getGroupAccessPartnersByAccessIdAndProductId(Long groupAccessId,Long productId) throws ServiceException;
	
	public List<GroupAccessDTO> getGroupAccessProductsByAccessId(Long groupAccessId) throws ServiceException;
	
	public Map<String,Map<String,Object>> getGroupAccessProdsDetails(Long productId);
}
