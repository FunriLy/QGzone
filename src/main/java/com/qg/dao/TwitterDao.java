package com.qg.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.qg.model.TwitterModel;

public interface TwitterDao {
	/**
	 * 这是一个关闭ResultSet，Statement，Connection的方法
	 * @param rs  ResultSet
	 * @param stat Statement
	 * @param conn  Connection
	 */
    public void close(ResultSet rs,Statement stat,Connection conn);
    /***
     * 这是一个将说说信息存进数据库的方法
     * @param twitter 说说的对象
     * @return true false
     */
    public int addTwitter(TwitterModel twitter) throws Exception;
    /***
     * 这是一个取出说说 集合的方法
     * @param pageNumber 当前页码
     * @return 说说对象的集合
     */
	public List<TwitterModel>getTwitter(int pageNumber,int userId);
	/***
	 * 这是一个根据说说id获取说说对象的方法
	 * @param twitterId 说说id
	 * @return 返回说说对象
	 */
    public TwitterModel geTwitterById(int twitterId);
    /**
     * 这是一个根据说说id删除说说的方法
     * @param twitterId 说说id
     * @return true false
     */
    public boolean deleteTwitter(int twitterId);
    /***
     * 该方法用于点赞
     * @param twitterId 对于的说说id
     * @return true false
     */
    public boolean addSupport(int twitterId);
    /***
     * 该方法用于取消赞
     * @param twitterId 对于的说说id
     * @return true false
     */
    public boolean deleteSupport(int twitterId);
    /**
     * 该方法用于查询某条说说包含几张图片
     * @param twitterId 说说id
     * @return 返回图片数量
     */
    public int twitterPicture(int twitterId);
}
