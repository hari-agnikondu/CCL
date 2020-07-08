/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.repos;

import org.springframework.data.repository.CrudRepository;

import com.incomm.cclp.domain.Transaction;

/**
 *
 * @author skocherla
 */
public interface TransactionRepository extends CrudRepository<Transaction, String> {

}
