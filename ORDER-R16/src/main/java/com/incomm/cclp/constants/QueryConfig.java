package com.incomm.cclp.constants;

import java.util.List;

/**
 * 
 * BEAN class USED to store query config details
 *
 */
public class QueryConfig {
	private String query;
	private String queryId;
	private List<String> selectList;

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public List<String> getSelectList() {
		return selectList;
	}

	public void setSelectList(List<String> selectList) {
		this.selectList = selectList;
	}

	public String getQueryId() {
		return queryId;
	}

	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}

}
