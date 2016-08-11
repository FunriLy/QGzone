package com.qg.model;

public class UserModel {
	
	private int userId;//账号
	private String userName;//昵称
	private String password;//密码
	private int userSecretId;//密保编号
	private String userSecretAnswer;//密保答案
	private String userImage;//照片
	
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUserSecretId() {
		return userSecretId;
	}
	public void setUserSecretId(int userSecretId) {
		this.userSecretId = userSecretId;
	}
	public String getUserSecretAnswer() {
		return userSecretAnswer;
	}
	public void setUserSecretAnswer(String userSecretAnswer) {
		this.userSecretAnswer = userSecretAnswer;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	@Override
	public String toString() {
		return "UserModel [userId=" + userId + ", userName=" + userName + ", password=" + password + ", userSecretId="
				+ userSecretId + ", userSecretAnswer=" + userSecretAnswer + ", userImage=" + userImage + "]";
	}
	
}
