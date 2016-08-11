package com.qg.dao;

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
}
