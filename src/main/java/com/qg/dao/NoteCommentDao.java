package com.qg.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import com.qg.model.NoteCommentModel;

public interface NoteCommentDao {
	/**
	 * 这是一个关闭ResultSet，Statement，Connection的方法
	 * @param rs  ResultSet
	 * @param stat Statement
	 * @param conn  Connection
	 */
	void close(ResultSet rs,Statement stat,Connection conn);
	/***
	 * 这是一个获取留言评论集合的方法
	 * @param noteId 留言id
	 * @return 返回留言评论的集合
	 */
	List<NoteCommentModel>getNoteCommentByNoteId(int noteId);
	/***
	 * 这是一个添加留言评论的方法
	 * @param noteComment 留言评论对象
	 * @return true false
	 */
	NoteCommentModel addNoteComment(NoteCommentModel noteComment);
	/***
	 * 这是一个通过留言id获取留言对象的方法
	 * @param commentId 留言评论id
	 * @return 具体留言对象
	 */
	NoteCommentModel getNoteCommentById(int commentId);
	/***
	 * 这是一个根据某条留言评论id删除其评论的方法
	 * @param commentId 留言评论id
	 * @return true false
	 */
	boolean deleteComment(int commentId);
	/***
	 * 这是一个根据留言id删除其所有评论的方法
	 * @param noteId 留言id
	 * @return true false
	 */
	boolean deleteComments(int noteId);
}
