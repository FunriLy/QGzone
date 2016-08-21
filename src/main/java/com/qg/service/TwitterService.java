package com.qg.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.qg.dao.SupportDao;
import com.qg.dao.TwitterDao;
import com.qg.dao.impl.SupportDaoImpl;
import com.qg.dao.impl.TwitterDaoImpl;
import com.qg.model.TwitterModel;

public class TwitterService {
	TwitterDao twitterDao = new TwitterDaoImpl();
	SupportDao supportDao = new SupportDaoImpl();
	FriendService friendService = new FriendService();
	/***
	 * 添加说说
	 * @param twitter 说说实体
	 * @return true false
	 * @throws Exception
	 */
	public int addTwitter(TwitterModel twitter) throws Exception {
		return twitterDao.addTwitter(twitter);
	}
	/**
	 * 获取说说列表
	 * @param pageNumber 当前页码
	 * @param userId 当前用户
	 * @return 说说集合
	 * @throws Exception 
	 */
	public List<TwitterModel> getTwitter(int pageNumber, int userId) throws Exception {
		//获取说说总数
		int twitterNumber = twitterDao.twitterNumber(userId);
		List<TwitterModel> twitters = new ArrayList<TwitterModel>();
		//判断请求页码是否超过总页码
		if (!(this.twitterPage(userId, twitterNumber) < pageNumber)) {
			twitters = twitterDao.getTwitter(pageNumber, userId);
			return twitters;
		} else
			return twitters;
	}
	/**
	 * 获取说说
	 * @param twitterId 说说id 
	 * @return 说说实体
	 */
	public TwitterModel geTwitterById(int twitterId) {
		return twitterDao.geTwitterById(twitterId);
	}
	/**
	 * 获取某个id的说说
	 * @param talkId 用户id 
	 * @param pageNumber 当前页码
	 * @return 说说集合
	 * @throws Exception 
	 */
	public List<TwitterModel> getTwitterByTalkId(int pageNumber, int talkId) throws Exception {
		//获取说说总数
		int twitterNumber = twitterDao.userTwitterNumber(talkId);
		List<TwitterModel> twitters = new ArrayList<TwitterModel>();
		//判断请求页码是否超过总页码
		if (!(this.twitterPage(talkId, twitterNumber) < pageNumber))
			
			return twitterDao.getMyTwitter(pageNumber, talkId);
		
		else
			return twitters;
	}
	/***
	 * 删除某条说说
	 * @param twitterId 说说id
	 * @param userId 当前用户id（判断权限）
	 * @return true false
	 */
	public boolean deleteTwitter(int twitterId,int userId) {
		//判断权限后删除
		if (twitterDao.existTwitter(twitterId))
			return (userId == this.geTwitterById(twitterId).getTalkId()) ? twitterDao.deleteTwitter(twitterId) : false;
		else
			return false;
	}
	/**
	 * 点赞
	 * @param twitterId 说说id
	 * @param supporterId  点赞者
	 * @return true false
	 */
	public boolean addSupport(int twitterId, int supporterId) {
		return supportDao.addSupport(twitterId, supporterId);
	}
	/***
	 * 取消点赞
	 * @param twitterId 说说id 
	 * @param supporterId 点赞者id
	 * @return true false
	 */
	public boolean deleteSupport(int twitterId, int supporterId) {
		return supportDao.deleteSupport(twitterId, supporterId);
	}
	/***
	 * 获取说说有几张图
	 * @param twitterId 说说id
	 * @return 图片数
	 */
	public int twitterPicture(int twitterId) {
		return twitterDao.twitterPicture(twitterId);
	}
	/***
	 * 查询说说是否存在
	 * @param twitterId 说说id
	 * @return ture false
	 */
	public boolean existTwitter(int twitterId){
		return twitterDao.existTwitter(twitterId);
	}
	/***
	 * 查询是否已经点赞
	 * @param twitterId 说说id
	 * @param supporterId 查询者
	 * @return true false
	 */
	public boolean findSupport(int twitterId, int supporterId) {
		return supportDao.findSupport(twitterId, supporterId);
	}

	/**
	 * 删除单个文件
	 * 
	 * @param fileName
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true,否则返回false
	 */
	public boolean deleteFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			System.out.println("删除文件" + fileName + "成功！");
			return true;
		} else {
			System.out.println("删除文件" + fileName + "失败！");
			return false;
		}
	}

	/***
	 * 
	 * @param twitterId说说id
	 * @param userId点赞者Id
	 * @return true false
	 */
	public boolean twitterSupport(int twitterId, int userId) {
		if (this.existTwitter(twitterId)
				&& (new FriendService().isFriend(userId, this.geTwitterById(twitterId).getTalkId()) == 1
						|| this.geTwitterById(twitterId).getTalkId() == userId)) {
			// 检测是否点赞
			if (!this.findSupport(twitterId, userId)) {
				// 实现点赞
				if (!this.addSupport(twitterId, userId))
					return false;
			} else {
				// 取消点赞
				if (!this.deleteSupport(twitterId, userId))
					return false;
			}
			return true;
		} else {
			return false;
		}
	}
	/***
	 * 获取页码
	 * @param userId 当前用户id
	 * @return
	 */
	public int twitterPage(int userId,int twitterNumber){
		int totalPage;
		int pageSize=12;

				if (twitterNumber % pageSize == 0)
					totalPage = new Integer(twitterNumber / pageSize).intValue();
				else
					totalPage = new Integer(twitterNumber / pageSize).intValue() + 1;
				return totalPage;
	}
	/***
	 * 获取好友圈说说总数
	 * @param userId 当前登陆用户
	 * @return 总数
	 */
	public int twitterNumber(int userId){
		return twitterDao.twitterNumber(userId);
	}
	/***
	 * 获取用户的说说总数
	 * @param userId 用户对象
	 * @return 总数
	 */
	public int userTwitterNumber(int userId){
		return twitterDao.userTwitterNumber(userId);
	}
}