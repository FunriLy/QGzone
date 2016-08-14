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

import com.qg.dao.TwitterDao;
import com.qg.model.TwitterModel;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class TwitterDaoImpl implements TwitterDao{
	private static final Logger LOGGER = Logger.getLogger(TwitterDaoImpl.class);
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

    public int addTwitter(TwitterModel twitter) throws Exception {
		int tiwtterId = 0;
		Date newTime = new Date();
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "insert into twitter(twitter_word, twitter_picture, talker_id, "
					+ "support, time) value(?,?,?,?,?)";
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, twitter.getTwitterWord());
			pStatement.setInt(2, twitter.getTwitterPicture());
			pStatement.setInt(3, twitter.getTalkId());
			pStatement.setInt(4, 0);
			pStatement.setTimestamp(5,new Timestamp(newTime.getTime()));
			pStatement.executeUpdate();
			conn.close();
			
			conn = SimpleConnectionPool.getConnection();
			String SQL = "SELECT twitter_id FROM twitter WHERE time=?";
			 pStatement = conn.prepareStatement(SQL);
			pStatement.setTimestamp(1,new Timestamp(newTime.getTime()));
			rs=pStatement.executeQuery();
			if(rs.next())
			tiwtterId=rs.getInt("twitter_id");
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "添加说说异常！", e);
			throw new Exception("添加说说异常!");
		} finally {
				close(rs, pStatement, conn);
			}
    	return tiwtterId;
    }
	public List<TwitterModel>getTwitter(int pageNumber,int userId) throws Exception{
		List<TwitterModel> twitters = new ArrayList<TwitterModel>();
		 try {
			 int number=(pageNumber-1)*16;
			 String sql = 	"SELECT DISTINCT twitter_id,twitter_word,twitter_picture,talker_id,support,time FROM twitter "
					 		+"INNER JOIN friends ON" 
					 		+"(friends.user_id=? ANd friends.f_user_id=twitter.talker_id) OR"
					 		+"(friends.f_user_id= ? AND friends.user_id=twitter.talker_id) OR (twitter.talker_id=?)"
					 		+"ORDER BY twitter_id DESC LIMIT ?,16";
			 conn = SimpleConnectionPool.getConnection();				
			 pStatement=(PreparedStatement) conn.prepareStatement(sql);
			 pStatement.setInt(1, userId);
			 pStatement.setInt(2, userId);
			 pStatement.setInt(3, userId);
			 pStatement.setInt(4, number);
			 rs=pStatement.executeQuery();
			 UserDaoImpl userDaoImpl = new UserDaoImpl();
				SupportDaoImpl supportDao = new SupportDaoImpl();
			 while(rs.next()){
				TwitterModel twitterModel = new TwitterModel
				  (rs.getInt("twitter_id"),rs.getString("twitter_word"),rs.getInt("twitter_picture"),rs.getInt("talker_id"),
						  userDaoImpl.getUserById(rs.getInt("talker_id")).getUserName(),Format.format(rs.getTimestamp("time")),
				  supportDao.getSupporterByTwitterId(rs.getInt("twitter_id")),new TwitterCommentDaoImpl().getTwitterCommentByTwitterId(rs.getInt("twitter_id")));
				twitters.add(twitterModel);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获取说说异常！", e);
			throw new Exception("获取说说异常!");
		}finally{
			close(rs, pStatement, conn);
		}
		return twitters;
	}
    public TwitterModel geTwitterById(int twitterId) {
    	TwitterModel twitter = null;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql =  "SELECT * FROM twitter WHERE twitter_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			 rs=pStatement.executeQuery();
			if(rs.next()){
				UserDaoImpl userDaoImpl = new UserDaoImpl();
				SupportDaoImpl supportDao = new SupportDaoImpl();
				twitter=new TwitterModel (twitterId,rs.getString("twitter_word"),rs.getInt("twitter_picture"),rs.getInt("talker_id"),
						userDaoImpl.getUserById(rs.getInt("talker_id")).getUserName(),Format.format(rs.getTimestamp("time")),
						  supportDao.getSupporterByTwitterId(rs.getInt("twitter_id")),new TwitterCommentDaoImpl().getTwitterCommentByTwitterId(rs.getInt("twitter_id")));
				}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获取某条说说异常！", e);
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
			new SupportDaoImpl().deleteSupports(twitterId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "删除某条说说异常！", e);
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
			String sql = "UPDATE twitter SET support=support+1 WHERE twitter_id=?";
			pStatement=(PreparedStatement) conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "点赞某条说说异常！", e);
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
			String sql = "UPDATE twitter SET support=support-1 WHERE twitter_id=?";
			pStatement=(PreparedStatement) conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "取消赞某条说说异常！", e);
			result=false;
		}finally{
			close(null, pStatement, conn);
		}
    	return result;
    }

	@Override
	public int twitterPicture(int twitterId) {
		int twitterPicture=0;
				try {
					conn = SimpleConnectionPool.getConnection();
					String sql = "SELECT twitter_picture FROM twitter WHERE twitter_id=?";
					pStatement = conn.prepareStatement(sql);
					pStatement.setInt(1, twitterId);
					rs=pStatement.executeQuery();
					if(rs.next())
					twitterPicture=rs.getInt("twitter_picture");
				} catch (SQLException e) {
					LOGGER.log(Level.ERROR, "获取图片张数异常！", e);
				} finally {
						close(null, pStatement, conn);
					}
		    	return twitterPicture;
		    }

	@Override
	public List<TwitterModel> getMyTwitter(int pageNumber, int userId) throws Exception {
		List<TwitterModel> twitters = new ArrayList<TwitterModel>();
		 try {
			 int number=(pageNumber-1)*16;
			 String sql = 	"SELECT * FROM twitter WHERE talker_id=? ORDER BY twitter_id DESC LIMIT ?,16";
			 conn = SimpleConnectionPool.getConnection();				
			 pStatement=(PreparedStatement) conn.prepareStatement(sql);
			 pStatement.setInt(1, userId);
			 pStatement.setInt(2, number);
			 rs=pStatement.executeQuery();
			 UserDaoImpl userDaoImpl = new UserDaoImpl();
				SupportDaoImpl supportDao = new SupportDaoImpl();
			 while(rs.next()){
				TwitterModel twitterModel = new TwitterModel
				  (rs.getInt("twitter_id"),rs.getString("twitter_word"),rs.getInt("twitter_picture"),rs.getInt("talker_id"),
						  userDaoImpl.getUserById(rs.getInt("talker_id")).getUserName(),Format.format(rs.getTimestamp("time")),
				  supportDao.getSupporterByTwitterId(rs.getInt("twitter_id")),new TwitterCommentDaoImpl().getTwitterCommentByTwitterId(rs.getInt("twitter_id")));
				twitters.add(twitterModel);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获取我的说说异常！", e);
			throw new Exception();
		}finally{
			close(rs, pStatement, conn);
		}
		return twitters;
	}

	@Override
	public boolean existTwitter(int twitterId) {
    	boolean result = false;
    	
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "SELECT COUNT(1) FROM twitter WHERE twitter_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			rs = pStatement.executeQuery();
			if(rs.next()){
				result=(rs.getInt(1)==1);
				}
    	} catch (SQLException e) {
    		LOGGER.log(Level.ERROR, "查询说说是否存在异常！", e);
		} finally {
			close(rs, pStatement, conn);
		}
    	return result;
	}
}
