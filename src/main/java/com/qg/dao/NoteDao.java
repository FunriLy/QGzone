package com.qg.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.qg.model.NoteModel;

public interface NoteDao {
	/**
	 * 这是一个关闭ResultSet，Statement，Connection的方法
	 * @param rs  ResultSet
	 * @param stat Statement
	 * @param conn  Connection
	 */
	void close(ResultSet rs,Statement stat,Connection conn);
	/**
	 * 这是一个添加留言的方法
	 * @param note 留言对象
	 * @return true false
	 */
	boolean addNote(NoteModel note);
	/**
	 * 这是一个获得留言集合的方法
	 * @param pageNumber 当前页码
	 * @param userId 当前用户id
	 * @return 该用户留言板的留言集合
	 * @throws Exception 
	 */
	List< NoteModel >getNote (int pageNumber,int userId) throws Exception;
	/**
	 * 这是一个通过具体留言id获取留言对象的方法
	 * @param noteId 留言id
	 * @return 返回留言对象
	 */
	NoteModel geNoteById(int noteId);
	/**
	 * 这是一个删除某条留言的方法
	 * @param noteId 留言id
	 * @return true false
	 */
	boolean deleteNote(int noteId);
	/***
	 * 这是一个检测留言id是否存在的方法
	 * @param noteId 留言id
	 * @return true false
	 */
	boolean existNote(int noteId);
	/***
	 * 查询留言的数目
	 * @param userId 当前留言板者的id
	 * @return 返回数目
	 */
	int noteNumber(int userId);
}
