package com.qg.model;

import java.util.List;

public class TwitterModel {
	private int twitterId;//说说id
	private String twitterWord;//说说文字内容
	private int twitterPicture;//说说图片的张数
	private int talkId;//说说的发表者id
	private String talkerName;//说说的发表者昵称
	private String twitterTime;//说说发表时间
	private List<Integer> supporterId;//该条说说的点赞者的昵称集合
	private List<TwitterCommentModel> comment;//该条说说的评论集合
	
	public TwitterModel(int twitterId, String twitterWord, int twitterPicture, int talkId, String talkerName,
			String time, List<Integer> supporterId, List<TwitterCommentModel> comment) {
		this.twitterId = twitterId;
		this.twitterWord = twitterWord;
		this.twitterPicture = twitterPicture;
		this.talkId = talkId;
		this.talkerName = talkerName;
		this.twitterTime = time;
		this.supporterId = supporterId;
		this.comment = comment;
	}
	public TwitterModel() {
	}
	
	public TwitterModel(String twitterWord, int twitterPicture, int talkId) {
		super();
		this.twitterWord = twitterWord;
		this.twitterPicture = twitterPicture;
		this.talkId = talkId;
	}
	public int getTwitterId() {
		return twitterId;
	}
	public void setTwitterId(int twitterId) {
		this.twitterId = twitterId;
	}
	public String getTwitterWord() {
		return twitterWord;
	}
	public void setTwitterWord(String twitterWord) {
		this.twitterWord = twitterWord;
	}
	public int getTwitterPicture() {
		return twitterPicture;
	}
	public void setTwitterPicture(int twitterPicture) {
		this.twitterPicture = twitterPicture;
	}
	public int getTalkId() {
		return talkId;
	}
	public void setTalkId(int talkId) {
		this.talkId = talkId;
	}
	public String getTalkerName() {
		return talkerName;
	}
	public void setTalkerName(String talkerName) {
		this.talkerName = talkerName;
	}
	public String getTime() {
		return twitterTime;
	}
	public void setTime(String time) {
		this.twitterTime = time;
	}
	public List<Integer> getSupporterId() {
		return supporterId;
	}
	public void setSupporterId(List<Integer> supporterName) {
		this.supporterId = supporterName;
	}
	public List<TwitterCommentModel> getComment() {
		return comment;
	}
	public void setComment(List<TwitterCommentModel> comment) {
		this.comment = comment;
	}
	

}
