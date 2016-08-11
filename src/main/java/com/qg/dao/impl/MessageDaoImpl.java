package com.qg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.qg.dao.MessageDao;
import com.qg.model.MessageModel;
import com.qg.model.UserModel;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class MessageDaoImpl implements MessageDao{

	private static final Logger LOGGER = Logger.getLogger(MessageDaoImpl.class);
	
	public static void main(String[] args) {
		MessageDaoImpl messageDao = new MessageDaoImpl();
		MessageModel message = new MessageModel(123456, "haha");
		message.setUserAddress("广东工业");
		System.out.println(messageDao.changeMessage(message));
		System.out.println(messageDao.getMessageById(123456));
	}
	
	private Connection conn;//声明Connection对象
	private PreparedStatement sql;//声明预处理语句
	private ResultSet rs;//声明结果集
	private  boolean flag=false;//判断标志
	/**
	 * 类中公用关闭流的方法
	 */
	private void daoClose(){
		try {
			if(rs != null){
				rs.close();
			}
			if(sql != null){
				sql.close();
			}     
			if(conn != null){
				SimpleConnectionPool.pushConnectionBackToPool(conn);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "SQL语句发送错误", e);
		}
	}
	@Override
	public boolean addMessage(MessageModel message) {
		
		LOGGER.log(Level.DEBUG, "用户的具体信息 账号:",message.getUserId());
		conn = SimpleConnectionPool.getConnection();
		try {
			sql=conn.prepareStatement("insert into message(user_id,user_sex,user_email,user_phone,user_birthday)"
					+" "+"values(?,?,?,?,?,?)");
			sql.setInt(1, message.getUserId());
			sql.setString(2, message.getUserSex());
			sql.setString(3, message.getUserEmail());
			sql.setString(4, message.getUserPhone());
			sql.setString(5,message.getUserBirthday());
			sql.setString(6, message.getUserAddress());
			sql.executeUpdate();
			flag = true;
			System.out.println("saveMessage is running");
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "添加用户信息发生异常！", e);
		}finally {
			daoClose();
		}
		if(flag){
			flag=false;
			return true;
		}
		else 
			return false;
	}

	@Override
	public boolean changeMessage(MessageModel message) {
		LOGGER.log(Level.DEBUG, "修改个人信息:账号",message.getUserId());
		conn = SimpleConnectionPool.getConnection();
		try {

			sql=conn.prepareStatement("update message m ,user u set m.user_sex=?,m.user_email=?,m.user_phone=?,m.user_birthday=?,m.user_address=?"
					+ " ,u.user_name=?"
					+ " where m.user_id=? and m.user_id = u.user_id");
			sql.setString(1, message.getUserSex());
			sql.setString(2, message.getUserEmail());
			sql.setString(3, message.getUserPhone());
			sql.setString(4, message.getUserBirthday());
			sql.setString(5, message.getUserAddress());
			sql.setString(6, message.getUserName());
			sql.setInt(7, message.getUserId());			
			sql.executeUpdate();
			flag=true;
			System.out.println("changMessage is running");
				
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "修改用户信息发生异常！", e);
		}finally {
			daoClose();
		}
		if(flag){
			flag=false;
			return true;
		}
		else 
			return false;
	}

	@Override
	public MessageModel getMessageById(int userId) {

		LOGGER.log(Level.DEBUG, "用户的信息 账号:",userId);
		conn = SimpleConnectionPool.getConnection();
		MessageModel message = new MessageModel();
		try {
			sql=conn.prepareStatement("select a.user_name,b.*" 
					+" from user as a ,message as b "  
					+" where a.user_id=b.user_id ;");
			rs=sql.executeQuery();
			while(rs.next()) {
				message.setUserId(rs.getInt("user_id"));
				message.setUserName(rs.getString("user_name"));
				message.setUserSex(rs.getString("user_sex"));
				message.setUserEmail(rs.getString("user_email"));
				message.setUserImage(rs.getString("user_image"));
				message.setUserPhone(rs.getString("user_birthday"));
				message.setUserAddress(rs.getString("user_address"));
				if(userId==message.getUserId()) {
					flag = true; break;
				}
			}
			System.out.println("getMessage is running");
				
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "查询用户信息发生异常！", e);
		}finally {
			daoClose();
		}
		if(flag){
			flag=false;
			return message;
		}
		else 
			return null;
	
	}

}
