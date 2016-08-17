package com.qg.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.qg.model.TwitterCommentModel;

public interface TwitterCommentDao {
	/**
	 * 这是一个关闭ResultSet，Statement，Connection的方法
	 * @param rs  ResultSet
	 * @param stat Statement
	 * @param conn  Connection
	 */
	public void close(ResultSet rs,Statement stat,Connection conn);
    /**
     * 这是一个根据说说ID取出其所有评论的方法
     * @param twitterId
     * @return 返回说说评论的集合
     */
    public List<TwitterCommentModel>getTwitterCommentByTwitterId(int twitterId);
    /***
     * 这是一个添加说说评论的方法
     * @param twitterComment 说说对象
     * @return true false
     */
    public boolean addTwitterComment(TwitterCommentModel twitterComment);
    /**
     * 这是一个根据评论ID获取评论对象的方法
     * @param commenterId
     * @return 返回说说评论对象
     */
    public TwitterCommentModel geTwitterCommentById(int commentId);
    /**
     * 这是一个根据评论id删除某条id的方法
     * @param commentId 评论id
     * @return true false
     */
    public boolean deleteComment(int commentId);
    /**
     * 这是一个根据说说id删除其评论的方法
     * @param twitterId 说说id
     * @return true false
     */
    public boolean deleteComments(int twitterId);
}

