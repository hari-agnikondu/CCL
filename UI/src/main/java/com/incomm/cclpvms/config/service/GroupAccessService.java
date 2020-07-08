package com.incomm.cclpvms.config.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.incomm.cclpvms.config.model.GroupAccess;
import com.incomm.cclpvms.config.model.GroupAccessDTO;
import com.incomm.cclpvms.config.model.ResponseDTO;
import com.incomm.cclpvms.exception.ServiceException;

public interface GroupAccessService {

	public List<GroupAccess> getGroupAccessIds(GroupAccess groupAccess)throws ServiceException;
	
	public List<GroupAccessDTO> getGroupAccess()throws ServiceException;
	
	public List<GroupAccessDTO> getGroupAccessPartnersByAccessID(Long groupAccessId)throws ServiceException;

	public ResponseDTO addGroupAccess(GroupAccess groupAccess) throws ServiceException;

	public ResponseDTO addAssignGroupAccessIdToProduct(GroupAccess groupAccess) throws ServiceException;
	
	public List<GroupAccessDTO> getGroupAccessProductsByAccessIdAndProductId(Long groupAccessId,Long productId)throws ServiceException;

	public ResponseDTO updateAssignGroupAccessToProduct(GroupAccess groupAccess)throws ServiceException;
	
	public ResponseEntity<ResponseDTO> updateGroupAccess(GroupAccess groupAccess)throws ServiceException;
	
	public Map<String,String>  getProductsByAccessId(Long groupAccessId) throws ServiceException ;
	
	public List<GroupAccessDTO> getGroupAccessPartnersByAccessIDAndProductId(Long groupAccessId,Long productId)throws ServiceException;

	public ResponseEntity<ResponseDTO> deleteGroupAccess(Long groupAccessId, Long productId)throws ServiceException;
	
	public List<GroupAccessDTO> getGroupAccessProductsByAccessId(Long groupAccessId)throws ServiceException;

}
