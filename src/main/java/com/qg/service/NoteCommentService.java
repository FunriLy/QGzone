package com.qg.service;

import java.util.List;

import com.qg.dao.impl.NoteCommentDaoImpl;
import com.qg.model.NoteCommentModel;

public class NoteCommentService {
	NoteCommentDaoImpl noteCommentDaoImpl = new NoteCommentDaoImpl();
	
	public List<NoteCommentModel> getNoteCommentByNoteId(int noteId){
		return noteCommentDaoImpl.getNoteCommentByNoteId(noteId);
	}
	
	public boolean addNoteComment(NoteCommentModel noteComment){
		return noteCommentDaoImpl.addNoteComment(noteComment);
	}
	
	public NoteCommentModel getNoteCommentById(int commentId) {
		return noteCommentDaoImpl.getNoteCommentById(commentId);
	}
	
	public boolean deleteComment(int commentId,int userId) {
		//判断权限后删除(评论者和留言板本人才可以删除)
		if (this.getNoteCommentById(commentId).getCommenterId()==userId||
				userId==new NoteService().getNoteById(this.getNoteCommentById(commentId).getNoteId()).getTargetId())
			return noteCommentDaoImpl.deleteComment(commentId);
		else
			return false;
	}
	
	public boolean deleteComments(int noteId) {
		return noteCommentDaoImpl.deleteComments(noteId);
	}
}
