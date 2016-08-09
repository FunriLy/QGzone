package com.qg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.qg.dao.SupportDao;
import com.qg.dao.TwitterDao;
import com.qg.dao.UserDao;
import com.qg.util.SimpleConnectionPool;

public class SupportDaoImpl implements SupportDao{
	private Connection conn = null;
	private PreparedStatement pStatement = null;
	private ResultSet rs = null;
    public void close(ResultSet rs,Statement stat,Connection conn){
        try {
            if(rs!=null)rs.close();
            if(stat!=null)stat.close();
            if(conn!=null)SimpleConnectionPool.pushConnectionBackToPool(conn);
        } catch (SQLException e) {
            e.printStackTrace();
       }
}
    public boolean addSupport(int twitterId,int supporterId){
    	boolean result = true;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "insert into support(twitter_id,supporter_id) value(?,?)";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			pStatement.setInt(2, supporterId);
			pStatement.executeUpdate();
			new TwitterDao().addSupport(twitterId);
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			close(null, pStatement, conn);
		}
    	return result;
	}
    public boolean deleteSupport(int twitterId,int supporterId){
    	boolean result = true;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "DELETE FROM support WHERE twitter_id=? AND supporter_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			pStatement.setInt(2, supporterId);
			pStatement.executeUpdate();
			new TwitterDao().deleteSupport(twitterId);
		} catch (SQLException e) {
			e.printStackTrace();
			result = false;
		} finally {
			close(null, pStatement, conn);
		}
    	return result;
	}
    public boolean findSupport(int twitterId,int supporterId){
    	boolean result = false;
    	
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "SELECT COUNT(1) FROM support WHERE twitter_id=? AND supporter_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			pStatement.setInt(2, supporterId);
			rs = pStatement.executeQuery();
			if(rs.next()){
				result=(rs.getInt(1)==1);
				}
    	} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pStatement, conn);
		}
    	return result;
    }
    public List<String>getSupporterByTwitterId(int twitterId){
    	List<String> supporters = new ArrayList<String>();
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "SELECT * FROM support WHERE twitter_id=? ORDER BY supporter_id DESC";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			rs = pStatement.executeQuery();
			if(rs.next()){
				UserDao userDao = new UserDao();
				supporters.add(userDao.getNameById(rs.getInt("supporter_id")));
				}
    	} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pStatement, conn);
		}
    	return supporters;
    }
}
