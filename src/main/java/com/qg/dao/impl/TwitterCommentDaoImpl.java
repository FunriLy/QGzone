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

import com.qg.dao.TwitterCommentDao;
import com.qg.model.TwitterCommentModel;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class TwitterCommentDaoImpl implements TwitterCommentDao{
	private static final Logger LOGGER = Logger.getLogger(TwitterCommentDaoImpl.class);
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
    public List<TwitterCommentModel>getTwitterCommentByTwitterId(int twitterId){
    	List<TwitterCommentModel>twitterComments = new ArrayList<TwitterCommentModel>();
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "SELECT * FROM twitter_comment WHERE twitter_id=? ORDER BY comment_id DESC";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			rs = pStatement.executeQuery();
			UserDaoImpl userDaoImpl = new UserDaoImpl();
			while(rs.next()){
				twitterComments.add(new TwitterCommentModel(rs.getInt("comment_id"),rs.getString("comment"),
						twitterId,rs.getInt("commenter_id"),userDaoImpl.getUserById(rs.getInt("commenter_id")).getUserName(),
						rs.getInt("target_id"),userDaoImpl.getUserById(rs.getInt("target_id")).getUserName(),Format.format(rs.getTimestamp("time"))));
				}
    	} catch (SQLException e) {
    		LOGGER.log(Level.ERROR, "获取说说评论异常！", e);
		} finally {
			close(rs, pStatement, conn);
		}
    	return twitterComments;
    }
    public boolean addTwitterComment(TwitterCommentModel twitterComment){
    	boolean result = true;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "insert into twitter_comment(comment, twitter_id, commenter_id, "
					+ "target_id, time) value(?,?,?,?,?)";
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, twitterComment.getComment());
			pStatement.setInt(2, twitterComment.getTwitterId());
			pStatement.setInt(3, twitterComment.getCommenterId());
			pStatement.setInt(4, twitterComment.getTargetId());
			pStatement.setTimestamp(5,new Timestamp(new Date().getTime()));
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "添加说说评论异常！", e);
			result = false;
		} finally {
			close(null, pStatement, conn);
		}
    	return result;
	}
    public TwitterCommentModel geTwitterCommentById(int commentId) {
    	TwitterCommentModel twitterComment = null;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql =  "SELECT * FROM twitter_comment WHERE comment_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, commentId);
			rs=pStatement.executeQuery();
			if(rs.next()){
				UserDaoImpl userDaoImpl = new UserDaoImpl();
				twitterComment=new TwitterCommentModel(commentId,rs.getString("comment"),
						rs.getInt("twitter_id"),rs.getInt("commenter_id"),userDaoImpl.getUserById(rs.getInt("commenter_id")).getUserName(),
						rs.getInt("target_id"),userDaoImpl.getUserById(rs.getInt("target_id")).getUserName(),Format.format(rs.getTimestamp("time")));
				}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获取某条说说评论异常！", e);
		} finally {
			close(rs, pStatement, conn);
		}
    	return twitterComment;
	}
    public boolean deleteComment(int commentId){
    	boolean result = true;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "DELETE FROM twitter_comment WHERE comment_id=?";
			pStatement=(PreparedStatement) conn.prepareStatement(sql);
			pStatement.setInt(1, commentId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "删除某条说说评论异常！", e);
			result = false;
		}finally{
			close(null, pStatement, conn);
		}
    	return result;
    }
    public boolean deleteComments(int twitterId){
    	boolean result = true;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "DELETE FROM twitter_comment WHERE twitter_id=?";
			pStatement=(PreparedStatement) conn.prepareStatement(sql);
			pStatement.setInt(1, twitterId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "删除某条说说全部评论异常！", e);
			result = false;
		}finally{
			close(null, pStatement, conn);
		}
    	return result;
    }
}
