/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.incomm.cclp.domain.AuthCheck;
import com.incomm.cclp.domain.AuthCheckPK;

/**
 *
 * @author skocherla
 */

public interface AuthCheckRepository extends CrudRepository<AuthCheck, AuthCheckPK> {

	public List<AuthCheck> findAuthChecks(@Param("transactionCode") String transactionCode, @Param("channelCode") String channelCode,
			@Param("messageType") String messageType);
}
