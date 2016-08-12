package com.qg.dao;

import java.util.List;

import com.qg.model.UserModel;

/**
 * 用户对象Dao层接口
 * @author hunger linhange
 *
 */
public interface UserDao {

	/**
	 * 添加用户方法
	 * @param user 用户对象
	 * @return 添加成功true , 添加失败false
	 */
	
	boolean addUser(UserModel user);
	
	/**
	 * 根据账号获取用户对象啊
	 * @param userId 账号
	 * @return 成功用户对象，失败null
	 */
	
	UserModel getUserById(int userId);
	
	/**
	 *根据昵称获取用户对象集合 
	 * @param userName 昵称
	 * @return 用户对象集合
	 */
	List<UserModel> getUsersByName(String userName);
	
	/**
	 * 修改密保问题
	 * @param userId 账号
	 * @param secretId 密保问题编号
	 * @param newAnswer 密保答案
	 * @return 成功true 失败false
	 */
	
	boolean changeSecret(int userId,int secretId,String newAnswer);
	

	
	/**
	 * 修改用户密码
	 * @param userId 账户
	 * @param password 新密码
	 * @return 成功true 失败false
	 */
	
	boolean changePassword(int userId,String password);
	
	/**
	 * 获得所有用户的账号集合
	 * @return 账号集合
	 */
	public List<String> selcetUserId();
}
