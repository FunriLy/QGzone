package com.qg.model;

public class TwitterCommentModel {
	private int commentId;
	private String comment;
	private int  twitterId;
	private int commenterId;
	private String commenterName;
	private int targetId;
	private String targetName;
	private String time;
	public TwitterCommentModel(int commentId, String comment, int twitterId, int commenterId, String commenterName,
			int targetId, String targetName, String time) {
		super();
		this.commentId = commentId;
		this.comment = comment;
		this.twitterId = twitterId;
		this.commenterId = commenterId;
		this.commenterName = commenterName;
		this.targetId = targetId;
		this.targetName = targetName;
		this.time = time;
	}
	public TwitterCommentModel() {
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
	public int getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(int twitterId) {
		this.twitterId = twitterId;
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
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}

}
