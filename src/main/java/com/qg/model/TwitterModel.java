package com.qg.model;

import java.util.List;

public class TwitterModel {
	private int twitterId;
	private String twitterWord;
	private int twitterPicture;
	private int talkId;
	private String talkerName;
	private String time;
	private List<String> supporterName;
	private List<TwitterCommentModel> comment;
	public TwitterModel(int twitterId, String twitterWord, int twitterPicture, int talkId, String talkerName,
			String time, List<String> supporterName, List<TwitterCommentModel> comment) {
		this.twitterId = twitterId;
		this.twitterWord = twitterWord;
		this.twitterPicture = twitterPicture;
		this.talkId = talkId;
		this.talkerName = talkerName;
		this.time = time;
		this.supporterName = supporterName;
		this.comment = comment;
	}
	public TwitterModel() {
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
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<String> getSupporterName() {
		return supporterName;
	}
	public void setSupporterName(List<String> supporterName) {
		this.supporterName = supporterName;
	}
	public List<TwitterCommentModel> getComment() {
		return comment;
	}
	public void setComment(List<TwitterCommentModel> comment) {
		this.comment = comment;
	}
	

}
