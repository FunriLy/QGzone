package com.qg.service;

import java.util.List;

import com.qg.dao.NoteCommentDao;
import com.qg.dao.impl.NoteCommentDaoImpl;
import com.qg.model.NoteCommentModel;

public class NoteCommentService {
	NoteCommentDao noteCommentDao = new NoteCommentDaoImpl();
	FriendService friendService = new FriendService();
	/***
	 * 获取留言评论集合
	 * @param noteId 留言id
	 * @return 返回留言评论集合
	 */
	public List<NoteCommentModel> getNoteCommentByNoteId(int noteId){
		return noteCommentDao.getNoteCommentByNoteId(noteId);
	}
	/***
	 * 添加留言评论
	 * @param noteComment 留言评论
	 * @return true false
	 */
	public NoteCommentModel addNoteComment(NoteCommentModel noteComment){
		return noteCommentDao.addNoteComment(noteComment);
	}
	/***
	 * 根据id获取留言评论
	 * @param commentId 留言评论id
 	 * @return 留言评论
	 */
	public NoteCommentModel getNoteCommentById(int commentId) {
		return noteCommentDao.getNoteCommentById(commentId);
	}
	/***
	 * 删除留言评论
	 * @param commentId 评论id
	 * @param userId当前用户id
	 * @return true false
	 */
	public boolean deleteComment(int commentId,int userId) {
		//判断权限后删除(评论者和留言板本人才可以删除)
		if (this.getNoteCommentById(commentId) != null && this.getNoteCommentById(commentId).getCommenterId() == userId
				|| this.getNoteCommentById(commentId) != null && userId == new NoteService()
						.getNoteById(this.getNoteCommentById(commentId).getNoteId()).getTargetId())
			return noteCommentDao.deleteComment(commentId);
		else
			return false;
	}

}
