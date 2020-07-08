package com.incomm.cclp.dao;

public interface PrmDAO {

	int updateMRIFIN(String prmAttributes);
	
	int updateMRIFNOTIN(String prmAttributes);
	
    int updateERIFIN(String prmAttributes);
	
	int updateERIFNOTIN(String prmAttributes);

	int updateAllPrm();

	int updateAllPrmToDiable(String arguement);

}
