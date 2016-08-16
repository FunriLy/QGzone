package com.qg.service;

import java.util.List;

import com.qg.dao.TwitterCommentDao;
import com.qg.dao.impl.TwitterCommentDaoImpl;
import com.qg.model.TwitterCommentModel;

public class TwitterCommentService {
	TwitterCommentDao twitterCommentDao = new TwitterCommentDaoImpl();
	FriendService friendService = new FriendService();
	/***
	 * 该方法获取说说评论列表
	 * @param twitterId 说说id
	 * @return 评论集合
	 */
	 public List<TwitterCommentModel>getTwitterCommentByTwitterId(int twitterId){
		 return twitterCommentDao.getTwitterCommentByTwitterId(twitterId);
	 }
	 /**
	  * 该方法添加说说评论
	  * @param twitterComment 说说评论
	  * @return true false
	  */
	 public int addTwitterComment(TwitterCommentModel twitterComment){
		 return twitterCommentDao.addTwitterComment(twitterComment);
	 }
	 /***
	  * 该方法根据id获取说说评论
	  * @param commentId 评论id
	  * @return 说说评论实体
	  */
	 public TwitterCommentModel geTwitterCommentById(int commentId){
		 return twitterCommentDao.geTwitterCommentById(commentId);
	 }
	 /***
	  * 该方法删除说说评论
	  * @param commentId 说说评论id
	  * @param userId 当前用户id（判断是否具有删除的权限）
	  * @return true false
	  */
	 public boolean deleteComment(int commentId,int userId){
			//判断权限后删除(说说发表者和评论者都可以删除评论)
		 return this.geTwitterCommentById(commentId)!=null&&this.geTwitterCommentById(commentId).getCommenterId()==userId||
				 this.geTwitterCommentById(commentId)!=null&&userId==new TwitterService().geTwitterById(this.geTwitterCommentById(commentId).getTwitterId()).getTalkId()
				 ?twitterCommentDao.deleteComment(commentId):false;
	 }

}