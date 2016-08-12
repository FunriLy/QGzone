package com.qg.dao;

import java.util.List;

import com.qg.model.MessageModel;

/**
 * 用户信息对象Dao层接口
 * @author hunger linhange
 *
 */
public interface MessageDao {

	/**
	 * 添加个人信息
	 * @param messgae 个人信息对象
	 * @return 成功true 失败false
	 */
	
	boolean addMessage(MessageModel message);
	
	/**
	 * 修改个人信息
	 * @param message 个人信息对象
	 * @return 成功true 失败false
	 */
	
	boolean changeMessage(MessageModel message);
	
	/**
	 * 获取个人信息
	 * @param userId 账号
 	 * @return 个人信息对象
	 */
	MessageModel getMessageById(int userId);
	
	
	/**
	 * 根据用户昵称获取用户信息列表
	 * @param name 昵称
	 * @return 用户信息集合
	 */
	List<MessageModel> getMessagesByName(String userName);
	
	
	/**
	 * 根据账号修改头像
	 * @param userId 账号
	 * @param image 头像名
	 * @return 成功true 失败false
	 */
	boolean changeImage(int userId,String image);
}
