package com.qg.service;

import java.util.List;

import com.qg.dao.impl.TwitterCommentDaoImpl;
import com.qg.model.TwitterCommentModel;

public class TwitterCommentService {
	TwitterCommentDaoImpl twitterCommentDaoImpl = new TwitterCommentDaoImpl();
	
	 public List<TwitterCommentModel>getTwitterCommentByTwitterId(int twitterId){
		 return twitterCommentDaoImpl.getTwitterCommentByTwitterId(twitterId);
	 }
	 
	 public boolean addTwitterComment(TwitterCommentModel twitterComment){
		 return twitterCommentDaoImpl.addTwitterComment(twitterComment);
	 }
	 
	 public TwitterCommentModel geTwitterCommentById(int commentId){
		 return twitterCommentDaoImpl.geTwitterCommentById(commentId);
	 }
	 
	 public boolean deleteComment(int commentId,int userId){
			//判断权限后删除(说说发表者和评论者都可以删除评论)
		 return this.geTwitterCommentById(commentId).getCommenterId()==userId||
				 userId==new TwitterService().geTwitterById(this.geTwitterCommentById(commentId).getTwitterId()).getTalkId()
				 ?twitterCommentDaoImpl.deleteComment(commentId):false;
	 }
	 
	 public boolean deleteComments(int twitterId){
		 return twitterCommentDaoImpl.deleteComments(twitterId);
	 }
}