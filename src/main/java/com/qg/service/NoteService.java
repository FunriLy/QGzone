package com.qg.service;

import java.util.List;

import com.qg.dao.impl.NoteDaoImpl;
import com.qg.model.NoteModel;

public class NoteService {
	NoteDaoImpl noteDaoImpl = new NoteDaoImpl();
	
	public boolean addNote(NoteModel note) {
		return noteDaoImpl.addNote(note);
	}
	
	public List<NoteModel> getNote(int pageNumber, int userId) {
		return noteDaoImpl.getNote(pageNumber, userId);
	}
	
	public NoteModel geNoteById(int noteId) {
		return noteDaoImpl.geNoteById(noteId);
	}
	
	public boolean deleteNote(int noteId){
		return noteDaoImpl.deleteNote(noteId);
	}
}
