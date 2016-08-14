package com.qg.service;

import java.util.List;

import com.qg.dao.MessageDao;
import com.qg.dao.UserDao;
import com.qg.dao.impl.MessageDaoImpl;
import com.qg.dao.impl.UserDaoImpl;
import com.qg.model.MessageModel;

/**
 * 用户信息对象业务逻辑层
 * @author hunger linhange
 *
 */
public class MessageService {

	private  MessageDao messageDao =  new MessageDaoImpl();
	private  UserDao userDao =  new UserDaoImpl();
	public static void main(String[] args) {
		MessageService service = new MessageService();
		System.out.println(service.changeImage(1263675+""));
	}
	
	
	/**
	 * 添加用户信息 
	 * @param userId 用户账号
	 * @return 成功true 失败false
	 */
	public boolean addMessage(int userId){
		MessageModel message = new MessageModel();
		message.setUserId(userId);
		return messageDao.addMessage(message); 
	}
	
	
	 /**
	  * 修改用户信息
	  * @param message 用户具体信息对象
	  * @return 成功true 失败false
	  */
	public boolean changeMessage(MessageModel message){
		String regex1 ="[a-z0-9A-Z\u4e00-\u9fa5]{1,15}";
		//String regex2 = "\\w+@\\w+(\\.\\w{2,3})*\\.\\w{2,3}";
		if(message == null){return false;}//具体信息对象不存在
		if(message.getUserName()==null){return false;}//昵称为null
		//if(message.getUserEmail()!=""&&!message.getUserEmail().matches(regex2)){return false;}
		return messageDao.changeMessage(message);
	}
	
	
	/**
	 * 获取个人信息
	 * @param userId 账号
 	 * @return 成功：个人信息对象 失败 null
	 */
	public MessageModel getMessageById(int userId){
		return messageDao.getMessageById(userId);
	}
	
	
	/**
	 * 根据用户昵称获取用户信息列表
	 * @param name 昵称
	 * @return 用户信息集合
	 */
	public List<MessageModel> getMessagesByName(String userName){
		return messageDao.getMessagesByName(userName);
	}
	
	/**
	 *根据账号修改用户头像方法 
	 * @param userId 账号
	 * @return 成功true，失败false
	 */
	public boolean changeImage(String userId){
		if(userId==null){return false;}
		if(!isExistOfUserId(userId)){return false;}
		String image = userId+".jpg";
		int userIdd = Integer.parseInt(userId);
		return messageDao.changeImage(userIdd, image);
	}

	
	/**
	 * 判账户是否存在
	 * @param userId 账号
	 * @return 存在true 失败false
	 */
	private boolean isExistOfUserId(String userId) {
		List<String> list = userDao.selcetUserId();
		boolean flag = false;
		if(list == null){return false;}
		for(String id:list){
			if(id.equals(userId+"")){
				flag=true;
				break;
			}
		}
		return flag;
	}
}
