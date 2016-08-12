package com.qg.service;

import java.util.List;

import com.qg.dao.MessageDao;
import com.qg.dao.UserDao;
import com.qg.dao.impl.MessageDaoImpl;
import com.qg.dao.impl.UserDaoImpl;
import com.qg.model.MessageModel;

public class SearchService {
	
	private static final int success = 1;
	private static final int fail = 0;
	
	/**
	 * 判断用户是否存在
	 * @param userId 用户账号
	 * @return 若找到返回success， 否则返回fail
	 */
	public int userIsExist(int userId){
		int result = fail;
		String strUserId = userId + ""; 
		UserDao userDao = new UserDaoImpl();
		List<String> allUserId = userDao.selcetUserId();
		
		//查找
		for(String oneUserId : allUserId){
			//若有相同则跳出循环
			if(strUserId.equals(oneUserId)){
				result = success;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 通过用户账号查找用户
	 * @return 用户信息
	 */
	public MessageModel searchMessageByUserId(int userId){
		MessageModel message = null;
		MessageDao messageDao = new MessageDaoImpl();
		message = messageDao.getMessageById(userId);
		return message;
	}
	
	/**
	 * 通过昵称搜索用户集合
	 * @param userName
	 * @return
	 */
	public List<MessageModel> searchMessagesByUserName(String userName){
		List<MessageModel> messages = null;
		MessageDao messageDao = new MessageDaoImpl();
		messages = messageDao.getMessagesByName(userName);
		return messages;
	}
}
