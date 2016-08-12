package com.qg.model;

import java.util.List;

public class NoteModel {
	private int noteId; //留言id
	private String note;//留言内容
	private int targetId;//被留言者id
	private String targetName;//被留言者昵称
	private int noteManId;//留言者id
	private String noteManName;//留言者昵称
	private String time;//留言时间
	private List<NoteCommentModel> comment;//每条留言对应的一系列评论
	
	public NoteModel(int noteId, String note, int targetId, String targetName, int noteManId, String noteManName,
			String time, List<NoteCommentModel> comment) {
		this.noteId = noteId;
		this.note = note;
		this.targetId = targetId;
		this.targetName = targetName;
		this.noteManId = noteManId;
		this.noteManName = noteManName;
		this.time = time;
		this.comment = comment;
	}
	
	
	public NoteModel(String note, int targetId, int noteManId) {
		this.note = note;
		this.targetId = targetId;
		this.noteManId = noteManId;
	}


	public NoteModel() {
	}


	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public int getTargetId() {
		return targetId;
	}
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
	public String getTargetName() {
		return targetName;
	}
	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}
	public int getNoteManId() {
		return noteManId;
	}
	public void setNoteManId(int noteManId) {
		this.noteManId = noteManId;
	}
	public String getNoteManName() {
		return noteManName;
	}
	public void setNoteManName(String noteManName) {
		this.noteManName = noteManName;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<NoteCommentModel> getComment() {
		return comment;
	}
	public void setComment(List<NoteCommentModel> comment) {
		this.comment = comment;
	}
	
	 
	
	
}
