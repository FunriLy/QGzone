package com.qg.model;

public class NoteCommentModel {
	private int commentId;	//留言评论id
	private String comment; //留言评论内容
	private int noteId;//留言id
	private int commenterId;//留言评论者id
	private String commenterName;//留言评论者昵称
	private int targetId;//被评论者id
	private String targetName;//被评论者昵称
	private String noteCommenttime;//留言评论时间
	
	
	public NoteCommentModel() {
	}
	
	public NoteCommentModel(int commentId, String comment, int noteId, int commenterId, String commenterName,
			int targetId, String targetName, String time) {
		super();
		this.commentId = commentId;
		this.comment = comment;
		this.noteId = noteId;
		this.commenterId = commenterId;
		this.commenterName = commenterName;
		this.targetId = targetId;
		this.targetName = targetName;
		this.noteCommenttime = time;
	}
	
	
	
	public NoteCommentModel(String comment, int noteId, int commenterId, int targetId) {
		super();
		this.comment = comment;
		this.noteId = noteId;
		this.commenterId = commenterId;
		this.targetId = targetId;
	}

	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getNoteId() {
		return noteId;
	}
	public void setNoteId(int noteId) {
		this.noteId = noteId;
	}
	public int getCommenterId() {
		return commenterId;
	}
	public void setCommenterId(int commenterId) {
		this.commenterId = commenterId;
	}
	public String getCommenterName() {
		return commenterName;
	}
	public void setCommenterName(String commenterName) {
		this.commenterName = commenterName;
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
	public String getTime() {
		return noteCommenttime;
	}
	public void setTime(String time) {
		this.noteCommenttime = time;
	}
	
}
