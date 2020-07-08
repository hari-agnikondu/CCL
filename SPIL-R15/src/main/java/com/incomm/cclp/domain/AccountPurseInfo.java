/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.domain;

import java.math.BigInteger;

/**
 *
 * @author skocherla
 */
public class AccountPurseInfo {
	double avlbl;
	double ledbl;
	BigInteger purseId;
	String currencyCode;

	public AccountPurseInfo(double avlbl, double ledbl, BigInteger pid, String currencyCode) {
		this.avlbl = avlbl;
		this.ledbl = ledbl;
		this.purseId = pid;
		this.currencyCode = currencyCode;
	}

	public double getAvlbl() {
		return avlbl;
	}

	public void setAvlbl(double avlbl) {
		this.avlbl = avlbl;
	}

	public double getLedbl() {
		return ledbl;
	}

	public void setLedbl(double ledbl) {
		this.ledbl = ledbl;
	}

	public BigInteger getPurseId() {
		return purseId;
	}

	public void setPurseId(BigInteger purseId) {
		this.purseId = purseId;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
