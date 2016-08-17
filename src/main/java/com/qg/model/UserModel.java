package com.qg.model;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class UserModel implements HttpSessionBindingListener{
	
	private int userId;//账号
	private String userName;//昵称
	private String password;//密码
	private int userSecretId;//密保编号
	private String userSecretAnswer;//密保答案
	private String userImage;//照片
	
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		// 将新建立Session 和 用户 保存ServletContext 的Map中
		HttpSession session = event.getSession();
		ServletContext servletContext = session.getServletContext();
		Map<UserModel, HttpSession> map = (Map<UserModel, HttpSession>) servletContext
				.getAttribute("map");
		// 将新用户加入map
		map.put(this, session);
		System.out.println(this.userId + "用户已登录");
		System.out.println("当前在在线用户有："+map.keySet());
	
	}
	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
			// 根据user对象，从Map中移除Session
			HttpSession session = event.getSession();
			ServletContext servletContext = session.getServletContext();

			Map<UserModel, HttpSession> map = (Map<UserModel, HttpSession>) servletContext
					.getAttribute("map");
			// 从map移除
			map.remove(this);
			System.out.println(this.userId + "用户已退出");
			System.out.println("当前在在线用户有："+map.keySet());
	
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
				+ userSecretId + ", userSecretAnswer=" + userSecretAnswer + ", userImage=" + userImage + "]\r\n";
	}
	
}
