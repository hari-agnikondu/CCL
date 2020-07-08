/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.incomm.cclp.util;

import java.util.Comparator;

import com.incomm.cclp.domain.AuthCheck;

/**
 *
 * @author skocherla
 */
public class AuthCheckComparator implements Comparator<AuthCheck> {

	@Override
	public int compare(AuthCheck o1, AuthCheck o2) {
		return o1.getAuthOrder() - o2.getAuthOrder();
	}

}
