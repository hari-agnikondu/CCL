/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 *
 * @author skocherla
 */
public interface BalanceInfo {
	public BigDecimal getAvlbl();

	public BigDecimal getLedbl();

	public BigInteger getPurseId();
}
