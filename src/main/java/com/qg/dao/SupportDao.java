package com.qg.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

public interface SupportDao {
		
	/**
	 * 这是一个关闭ResultSet，Statement，Connection的方法
	 * @param rs  ResultSet
	 * @param stat Statement
	 * @param conn  Connection
	 */
	void close(ResultSet rs,Statement stat,Connection conn);
    /***
     * 这是一个点赞的方法
     * @param twitterId 说说id
     * @param supporterId 点赞者id
     * @return true false
     */
	boolean addSupport(int twitterId,int supporterId);
    /***
     * 这是一个取消赞的方法
     * @param twitterId 说说id
     * @param supporterId 点赞者id
     * @return true false
     */
	boolean deleteSupport(int twitterId,int supporterId);
    /***
     * 这是一个查看用户是否已经点赞的方法
     * @param twitterId 说说id
     * @param supporterId 点赞者id
     * @return true false
     */  
	boolean findSupport(int twitterId,int supporterId);
    /***
     * 这是获取某条说说所有点赞人列表的方法
     * @param twitterId 说说id
     * @return 点赞者的名字集合
     */	
	List<Integer>getSupporterByTwitterId(int twitterId);
	/***
	 * 取消所有赞的方法
	 * @param twitterId 说说id
	 * @return true false
	 */
	public boolean deleteSupports(int twitterId);
}
