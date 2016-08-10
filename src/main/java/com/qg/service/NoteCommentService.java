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
	
	public boolean deleteComment(int commentId) {
		return noteCommentDaoImpl.deleteComment(commentId);
	}
	
	public boolean deleteComments(int noteId) {
		return noteCommentDaoImpl.deleteComments(noteId);
	}
}
