package com.incomm.cclp.dao;

import java.util.List;

import com.incomm.cclp.domain.ProgramID;

public interface ProgramIDDAO {

	public List<ProgramID> getAllProgramIDs();

	public List<ProgramID> getAllProgramIDsByName(String programIDName);

	public List<ProgramID> getProgramIDByName(String programIDName);

	public void createProgramID(ProgramID programID);

	public ProgramID getProgramByID(Long programId);

	public void updateProduct(ProgramID programID);

	public void deleteProgramID(ProgramID programId);

	public List<ProgramID> getProgramIdsByPartnerId(Long partnerId);
	
}
