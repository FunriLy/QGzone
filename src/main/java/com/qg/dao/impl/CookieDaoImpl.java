package com.qg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.qg.dao.CookieDao;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class CookieDaoImpl implements CookieDao {

	private static final Logger LOGGER = Logger.getLogger(CookieDaoImpl.class);
	private static final int success = 1;
	private static final int fail = 0;
	
	private Connection con = null;
	private PreparedStatement pStatement = null;
	
	/**
	 * 类中公用关闭流的方法
	 */
	private void daoClose(){
		try {
			if(pStatement != null){
				pStatement.close();
			}     
			if(con != null){
				SimpleConnectionPool.pushConnectionBackToPool(con);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "SQL语句发送错误", e);
		}
	}		
	
	@Override
	public int saveCookie(int userId, String sessionId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "insert into cookies(user_id, session_id, login_time) value(?,?,?)";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, userId);
			pStatement.setString(2, sessionId);
			pStatement.setLong(3, new Date().getTime()+7*24*60*60*1000);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			LOGGER.log(Level.DEBUG, "保存cookie实现类发生异常!", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int updateCookie(String sessionId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "update cookies set login_time=? where session_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setLong(1, new Date().getTime()+7*24*60*60*1000);
			pStatement.setString(2, sessionId);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "更新cookie时间实现类发生异常!", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int removeCookie(String sessionId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "delete from cookies where session_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setString(1, sessionId);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "删除cookies实现类发生异常!", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int getUserIdfromCookie(String sessionId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select user_id from cookies where session_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setString(1, sessionId);
			ResultSet rSet = pStatement.executeQuery();
			
			if (rSet.next()) {
				result = rSet.getInt("user_id");
				//获取账号后更新时间
				updateCookie(sessionId);
				LOGGER.log(Level.DEBUG, "根据sessionId获取用户 {0}", result);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "搜索cookies实现类发生异常!", e);
		} finally {
			daoClose();
		}
		return result;
	}

}
