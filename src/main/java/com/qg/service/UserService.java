package com.qg.service;

import java.util.List;
import java.util.Random;

import com.qg.dao.MessageDao;
import com.qg.dao.UserDao;
import com.qg.dao.impl.MessageDaoImpl;
import com.qg.dao.impl.UserDaoImpl;
import com.qg.model.MessageModel;
import com.qg.model.UserModel;
import com.qg.util.EncryptionUtil;
/**
 * 用户对象业务逻辑包装类
 * @author hunger linhange
 *
 */
public class UserService {

	private UserDao userDao = new UserDaoImpl();
	private MessageDao messageDao = new MessageDaoImpl();
	
	/**
	 * 创建随机账号
	 * @return 生成一个七位的随机账号
	 */
	public int createUserId(){
		List<String> list = userDao.selcetUserId();
		Random random = new Random();
		boolean flag = true;
		int max = 10000000;
		int min = 1000000;
		int userId;
		while(true){
			userId = random.nextInt(max)%(max-min+1) + min;
			if(list==null){break;}
			for(String id:list){
				if(id.equals(userId+"")){
					flag=false;
					break;
				}
			}
			if(flag){
				break;
			}
		}	
		System.out.println("创建账号成功：您的账号为："+userId);
		return userId;
	}
	
	
	/**
	 * 密码加密方法
	 * @param password 原密码
	 * @return 加密后密码
	 */
	public String encryptPassword(String password){
		return EncryptionUtil.md5(password);
	}
	
	
	/**
	 * 添加用户方法
	 * @param user 用户对象
	 * @return 账号
	 */
	public String addUser(UserModel user) {
		user.setUserId(createUserId());
		user.setPassword(encryptPassword(user.getPassword()));
		MessageModel message = new MessageModel();
		message.setUserId(user.getUserId());
		userDao.addUser(user);
		messageDao.addMessage(message);
		return user.getUserId()+"";
	}
	
	
	/**
	 * 根据用户id获取用户对象
	 * @param userId 账号
	 * @return 用户对象
	 */
	public UserModel getUserById(int userId) {
		return userDao.getUserById(userId);
	}
	
	
	/**
	 * 根据昵称获取用户对象集合
	 * @param userName 用户昵称
	 * @return 用户对象集合
	 */
	public List<UserModel> getUsersByName(String userName) {
		return userDao.getUsersByName(userName);
	}
	
	
	/**
	 * 修改密保问题
	 * @param userId 用户账号
	 * @param oldSecretId 旧密保编号
	 * @param oldAnswer 旧密保答案
	 * @param newSecretId 新密保编号
	 * @param newAnswer 新密保答案
	 * @return 成功true 失败false
	 */
	public boolean changeSecret(int userId,int oldSecretId,String oldAnswer
			,int newSecretId,String newAnswer){
		UserModel user = userDao.getUserById(userId);
		String regex ="[a-z0-9A-Z\u4e00-\u9fa5]{1,15}";
		if(user==null||oldSecretId==0||oldAnswer==null||newSecretId==0||newAnswer==null){return false;}
		if(!oldAnswer.matches(regex)&&!newAnswer.matches(regex)){return false;}
		if(user.getUserSecretId()==oldSecretId
				&&user.getUserSecretAnswer().equals(oldAnswer)){
			return userDao.changeSecret(userId, newSecretId, newAnswer);
		}
		return false;
		
	}
	
	
	/**
	 * 修改密码
	 * @param userId 账号
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @return 成功true 失败false
	 */
	public boolean changePassword(int userId,String oldPassword,String newPassword){
		UserModel user = userDao.getUserById(userId);
		String regex ="[a-z0-9A-Z_]{5,15}";
		System.out.println(user);
		if(user==null||oldPassword==null||newPassword==null){return false;}
		if((!oldPassword.matches(regex))&&(!newPassword.matches(regex))){return false;}
		if(encryptPassword(oldPassword).equals(user.getPassword())){
			return userDao.changePassword(userId,encryptPassword(newPassword));
		}
		return false;
	}
	
	
	/**
	 * 忘记密码，根据密保答案修改密码
	 * @param userId 账号
	 * @param oldSecretId 旧密保编号
	 * @param oldAnswer 旧密保答案
	 * @param newPassword 新密码
	 * @return 成功true 失败false
	 */
	public boolean forgetPassword(int userId,int oldSecretId,String oldAnswer 
			,String newPassword){	
		UserModel user = userDao.getUserById(userId);
		String regex1 ="[a-z0-9A-Z\u4e00-\u9fa5]{1,15}";
		String regex2 ="[a-z0-9A-Z_]{5,15}";
		if(user==null||oldSecretId==0||oldAnswer==null||newPassword==null){return false;}
		if(!oldAnswer.matches(regex1)&&!newPassword.matches(regex2)){return false;}
		if(user.getUserSecretId()==oldSecretId
				&&user.getUserSecretAnswer().equals(oldAnswer)){
			return userDao.changePassword(userId, encryptPassword(newPassword));
		}
		return false;
	}
	/**
	 * 注册，判断提交各个数据格式
	 * @param user 用户对象
	 * @return 账号
	 */
	public  String doSignUp(UserModel user){
		if(user == null){return null;}
		if(user.getPassword()==null||user.getUserName()==null
				||user.getUserSecretAnswer()==null){return null;}
		String regex1 ="[a-z0-9A-Z\u4e00-\u9fa5_]{1,15}";
		String regex2 ="[a-z0-9A-Z_]{5,15}";
		if(user.getPassword().matches(regex2)
				&&user.getUserName().matches(regex1)
				&&user.getUserSecretAnswer().matches(regex1)){
			return addUser(user);
		}
		return null;
	}
	/**
	 * 登录，判断提交的各个数据
	 * @param userId 账号
	 * @param userPassword 密码
	 * @return 成功true 错误false
	 */
	public boolean doSingIn(String userId ,String userPassword){
		if(userId==null||userPassword==null){return false;}
		UserModel user = userDao.getUserById(Integer.parseInt(userId));
		if(user  == null){return false;}
		if(user.getPassword().equals(encryptPassword(userPassword))){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 判断账号是否存在
	 * @param userId 账号
	 * @return 存在true 不存在false
	 */
	public  boolean isExistOfUserId(String userId){
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
	
	public boolean checkPassword(int userId,int oldSecretId,String oldAnswer ){	
		UserModel user = userDao.getUserById(userId);
		String regex1 ="[a-z0-9A-Z\u4e00-\u9fa5]{1,15}";
		String regex2 ="[a-z0-9A-Z_]{5,15}";
		if(user==null||oldSecretId==0||oldAnswer==null){return false;}
		if(!oldAnswer.matches(regex1)){return false;}
		if(user.getUserSecretId()==oldSecretId
				&&user.getUserSecretAnswer().equals(oldAnswer)){
			return true;
		}
		return false;
	}	
}
