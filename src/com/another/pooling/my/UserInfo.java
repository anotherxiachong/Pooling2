package com.another.pooling.my;

import cn.bmob.v3.BmobObject;

public class UserInfo extends BmobObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String username;
	String nickname;
	String usericon;
	String signature;
	String sex;
	String age;
	String alwaysaddres;
	
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the nickname
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * @param nickname the nickname to set
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * @return the usericon
	 */
	public String getUsericon() {
		return usericon;
	}
	/**
	 * @param usericon the usericon to set
	 */
	public void setUsericon(String usericon) {
		this.usericon = usericon;
	}
	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}
	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
	/**
	 * @return the age
	 */
	public String getAge() {
		return age;
	}
	/**
	 * @param string the age to set
	 */
	public void setAge(String string) {
		this.age = string;
	}
	/**
	 * @return the alwaysaddres
	 */
	public String getAlwaysaddres() {
		return alwaysaddres;
	}
	/**
	 * @param alwaysaddres the alwaysaddres to set
	 */
	public void setAlwaysaddres(String alwaysaddres) {
		this.alwaysaddres = alwaysaddres;
	}
}
