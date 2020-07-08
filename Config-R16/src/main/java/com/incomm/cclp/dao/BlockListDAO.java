package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.BlockList;

public interface BlockListDAO {

	List<Object[]> getDeliveryChannelList();

	int isBlockListExist(String instrumentId);

	void createBlockList(BlockList blockList);

	int deleteBlockList(List<Object> instrumentIds);

	List<BlockList> getBlockListById(String channelCode);

	Object getAllBlockList();

}
