package com.qg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qg.dao.SupportDao;
import com.qg.dao.TwitterCommentDao;
import com.qg.dao.TwitterDao;
import com.qg.model.TwitterModel;
import com.qg.util.SimpleConnectionPool;

public class TwitterDaoImpl implements TwitterDao{
	private Connection conn = null;
	private PreparedStatement pStatement = null;
	private ResultSet rs = null;
	SimpleDateFormat Format = new SimpleDateFormat ("yyyy-MM-dd HH:mm");

    public  void close(ResultSet rs,Statement stat,Connection conn){
        try {
            if(rs!=null)rs.close();
            if(stat!=null)stat.close();
            if(conn!=null)SimpleConnectionPool.pushConnectionBackToPool(conn);
        } catch (SQLException e) {
            e.printStackTrace();
       }
}

    public boolean addTwitter(TwitterModel twitter) {
		boolean result = true;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "insert into twitter(twitter_word, twitter_picture, talker_id, "
					+ "support, time) value(?,?,?,?,?)";
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, twitter.getTwitterWord());
			pStatement.setInt(2, twitter.getTwitterPicture());
			pStatement.setInt(3, twitter.getTalkId());
			pStatement.setInt(4, 0);
			pStatement.setTimestamp(5,new Timestamp(new Date().getTime()));
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			result=false;
		} finally {
				close(null, pStatement, conn);
			}
    	return result;
    }
	public List<TwitterModel>getTwitter(int pageNumber){
		List<TwitterModel> twitters = new ArrayList<TwitterModel>();
		 try {
			 int number=(pageNumber-1)*16;
			 String sql = "SELECT * FROM twitter ORDER BY twitter_id DESC LIMIT ?,16";
			 conn = SimpleConnectionPool.getConnection();				
			 pStatement=(PreparedStatement) conn.prepareStatement(sql);
			 pStatement.setInt(1, number);
			 rs=pStatement.executeQuery();
			 UserDao userDao = new UserDao();
				SupportDaoImpl supportDao = new SupportDaoImpl();
			 while(rs.next()){
				TwitterModel twitterModel = new TwitterModel
				  (rs.getInt("twitter_id"),rs.getString("twitter_word"),rs.getInt("twitter_picture"),rs.getInt("talker_id"),
				  userDao.getNameById(rs.getInt("talker_id")),Format.format(rs.getTimestamp("time")),
				  supportDao.getSupporterByTwitterId(rs.getInt("twitter_id")),new TwitterCommentDaoImpl().getTwitterCommentByTwitterId(rs.getInt("twitter_id")));
				twitters.add(twitterModel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("失败");
		}finally{
			close(rs, pStatement, conn);
		}
		return twitters;
	}
    public TwitterModel geTwitterById(int twitterId) {
    	TwitterModel twitter;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql =  "SELECT * FROM twitter WHERE twitter_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			if(rs.next()){
				UserDao userDao = new UserDao();
				SupportDaoImpl supportDao = new SupportDaoImpl();
				twitter=new TwitterModel (twitterId,rs.getString("twitter_word"),rs.getInt("twitter_picture"),rs.getInt("talker_id"),
						  userDao.getNameById(rs.getInt("talker_id")),Format.format(rs.getTimestamp("time")),
						  supportDao.getSupporterByTwitterId(rs.getInt("twitter_id")),new TwitterCommentDaoImpl().getTwitterCommentByTwitterId(rs.getInt("twitter_id")));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(rs, pStatement, conn);
		}
    	return twitter;
	}
    public boolean deleteTwitter(int twitterId){
    	boolean result = true;
		try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "DELETE FROM twitter WHERE twitter_id=?";
			pStatement=(PreparedStatement) conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			new TwitterCommentDaoImpl().deleteComments(twitterId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("失败");
			result=false;
		}finally{
			close(null, pStatement, conn);
		}
		return result;
    }
    public boolean addSupport(int twitterId){
    	boolean result = true;
    	try {
    		conn = SimpleConnectionPool.getConnection();
			String sql = "UPDATE twitter SET  WHERE support=support+1 WHERE twitter_id=?";
			pStatement=(PreparedStatement) conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("失败");
			result= false;
		}finally{
			close(null, pStatement, conn);
		}
    	return result;
    }
    public boolean deleteSupport(int twitterId){
    	boolean result=true;
    	try {
    		conn = SimpleConnectionPool.getConnection();
			String sql = "UPDATE twitter SET  WHERE support=support-1 WHERE twitter_id=?";
			pStatement=(PreparedStatement) conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("失败");
			result=false;
		}finally{
			close(null, pStatement, conn);
		}
    	return result;
    }
}
