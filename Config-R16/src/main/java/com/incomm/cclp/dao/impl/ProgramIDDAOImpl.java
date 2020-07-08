package com.incomm.cclp.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.incomm.cclp.constants.QueryConstants;
import com.incomm.cclp.dao.ProgramIDDAO;
import com.incomm.cclp.domain.ProgramID;


@Repository
public class ProgramIDDAOImpl implements ProgramIDDAO{

	// the entity manager
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramID> getAllProgramIDs() {
		
		return em.createQuery(QueryConstants.GET_ALL_PROGRAM_IDS).getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramID> getAllProgramIDsByName(String programIDName) {
		
		return em.createQuery(QueryConstants.GET_PROGRAMIDS_BY_NAME)
				.setParameter("programIDName", "%"+programIDName.toUpperCase()+"%")
				.getResultList();
	}

	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramID> getProgramIDByName(String programIDName) {
		
		return em.createQuery(QueryConstants.GET_PROGRAM_ID_BY_NAME)
				.setParameter("programIDName", "%" + programIDName.toUpperCase() + "%")
				.getResultList();
	}

	
	
	@Override
	public void createProgramID(ProgramID programID) {
		
		em.persist(programID);
	}

	@Override
	public ProgramID getProgramByID(Long programId) {
		
		return em.find(ProgramID.class, programId);
	}

	@Override
	public void updateProduct(ProgramID programID) {
		
		em.merge(programID);	
	}

	@Override
	@Transactional
	public void deleteProgramID(ProgramID programId) {
		em.remove(programId);
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProgramID> getProgramIdsByPartnerId(Long partnerId) {
		
		return em.createQuery(QueryConstants.GET_PROGRAM_IDS_BY_PARTNER_ID)
				.setParameter("partnerId", partnerId)
				.getResultList();
	}
	



}
