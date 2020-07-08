package com.incomm.cclpvms.config.model;

import java.io.Serializable;

/**
 * @author KMaji
 *
 */
public class User implements Serializable {

 /**
  *
  */
 private static final long serialVersionUID = 9081527761576640803L;

 private String uid;
 private String cn;
 private String sn;
 private String userPassword;
 private String postalAddress;
 private String telephoneNumber;

 /**
  * @return the uid
  */
 public final synchronized  String getUid() {
  return uid;
 }

 /**
  * @param uid
  *            the uid to set
  */
 public final synchronized  void setUid(String uid) {
  this.uid = uid;
 }

 /**
  * @return the cn
  */
 public final synchronized  String getCn() {
  return cn;
 }

 /**
  * @param cn
  *            the cn to set
  */
 public final synchronized  void setCn(String cn) {
  this.cn = cn;
 }

 /**
  * @return the sn
  */
 public final synchronized  String getSn() {
  return sn;
 }

 /**
  * @param sn
  *            the sn to set
  */
 public final synchronized  void setSn(String sn) {
  this.sn = sn;
 }

 /**
  * @return the userPassword
  */
 public final  synchronized  String getUserPassword() {
  return userPassword;
 }

 /**
  * @param userPassword
  *            the userPassword to set
  */
 public final synchronized  void setUserPassword(String userPassword) {
  this.userPassword = userPassword;
 }

 /**
  * @return the postalAddress
  */
 public final  synchronized  String getPostalAddress() {
  return postalAddress;
 }

 /**
  * @param postalAddress
  *            the postalAddress to set
  */
 public final synchronized  void setPostalAddress(String postalAddress) {
  this.postalAddress = postalAddress;
 }

 /**
  * @return the telephoneNumber
  */
 public final synchronized  String getTelephoneNumber() {
  return telephoneNumber;
 }

 /**
  * @param telephoneNumber
  *            the telephoneNumber to set
  */
 public final synchronized  void setTelephoneNumber(String telephoneNumber) {
  this.telephoneNumber = telephoneNumber;
 }

 /*
  * (non-Javadoc)
  *
  * @see java.lang.Object#toString()
  */
 @Override
 public String toString() {
  StringBuilder builder = new StringBuilder();
  builder.append("User [");
  if (uid != null) {
   builder.append("uid=");
   builder.append(uid);
   builder.append(", ");
  }
  if (cn != null) {
   builder.append("cn=");
   builder.append(cn);
   builder.append(", ");
  }
  if (sn != null) {
   builder.append("sn=");
   builder.append(sn);
   builder.append(", ");
  }
  if (userPassword != null) {
   builder.append("userPassword=");
   builder.append(userPassword);
   builder.append(", ");
  }
  if (postalAddress != null) {
   builder.append("postalAddress=");
   builder.append(postalAddress);
   builder.append(", ");
  }
  if (telephoneNumber != null) {
   builder.append("telephoneNumber=");
   builder.append(telephoneNumber);
  }
  builder.append("]");
  return builder.toString();
 }
}