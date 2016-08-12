package com.qg.service;

import java.util.List;

import com.qg.dao.impl.NoteDaoImpl;
import com.qg.model.NoteModel;

public class NoteService {
	NoteDaoImpl noteDaoImpl = new NoteDaoImpl();
	
	public boolean addNote(NoteModel note) {
		return noteDaoImpl.addNote(note);
	}
	
	public List<NoteModel> getNote(int pageNumber, int userId) throws Exception {
		return noteDaoImpl.getNote(pageNumber, userId);
	}
	
	public NoteModel getNoteById(int noteId) {
		return noteDaoImpl.geNoteById(noteId);
	}
	
	public boolean deleteNote(int noteId,int userId){
		//判断权限后删除(留言的人和被留言的人都可删除该留言)
		return this.getNoteById(noteId).getNoteManId()==userId||userId==this.getNoteById(noteId).getTargetId()?noteDaoImpl.deleteNote(noteId):false;
	}
}
