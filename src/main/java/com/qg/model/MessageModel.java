package com.qg.model;

public class MessageModel {

	private int userId;//账号
	private String userName;//昵称
	private String userSex;//性别
	private String userEmail;//邮箱
	private String userImage;//头像
	private String userPhone;//电话
	private String userBirthday;//生日
	private String userAddress;//地址
	
	public MessageModel(){
		
	}
	public MessageModel(int userId,String userName){
		this.userId = userId;
		this.userName = userName;
	}
	
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
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserImage() {
		return userImage;
	}
	public void setUserImage(String userImage) {
		this.userImage = userImage;
	}
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getUserBirthday() {
		return userBirthday;
	}
	public void setUserBirthday(String userBirthday) {
		this.userBirthday = userBirthday;
	}
	
	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	@Override
	public String toString() {
		return "MessageModel [userId=" + userId + ", userName=" + userName + ", userSex=" + userSex + ", userEmail="
				+ userEmail + ", userImage=" + userImage + ", userPhone=" + userPhone + ", userBirthday=" + userBirthday
				+ ", userAddress=" + userAddress + "]";
	}
	
}
