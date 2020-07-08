package com.incomm.cclp.dao;

import java.math.BigDecimal;
import java.util.List;

import com.incomm.cclp.domain.Group;

public interface GroupDAO {
	
public void createGroup(Group group);
	
	public void updateGroup(Group group);
	
	public List<Group> getGroups();
	
	public void deleteGroup(Group group);
	
	public List<Group> getGroupByName(String groupName);
	
	public Group findGroup(Group group);
	
	public int changeStatus(Group group);
	
	public BigDecimal isMappedToUser(long groupId);
	
}
