package com.qg.service;

import java.io.File;
import java.util.List;

import com.qg.dao.impl.SupportDaoImpl;
import com.qg.dao.impl.TwitterDaoImpl;
import com.qg.model.TwitterModel;

public class TwitterService {
	TwitterDaoImpl twitterDaoImpl = new TwitterDaoImpl();
	SupportDaoImpl supportDaoImpl = new SupportDaoImpl();

	public int addTwitter(TwitterModel twitter) throws Exception {
		return twitterDaoImpl.addTwitter(twitter);
	}

	public List<TwitterModel> getTwitter(int pageNumber, int userId) {
		return twitterDaoImpl.getTwitter(pageNumber, userId);
	}

	public TwitterModel geTwitterById(int twitterId) {
		return twitterDaoImpl.geTwitterById(twitterId);
	}

	public boolean deleteTwitter(int twitterId,int userId) {
		//判断权限后删除
		return (userId==this.geTwitterById(twitterId).getTalkId())?twitterDaoImpl.deleteTwitter(twitterId):false;
	}

	public boolean addSupport(int twitterId, int supporterId) {
		return supportDaoImpl.addSupport(twitterId, supporterId);
	}

	public boolean deleteSupport(int twitterId, int supporterId) {
		return supportDaoImpl.deleteSupport(twitterId, supporterId);
	}

	public int twitterPicture(int twitterId) {
		return twitterDaoImpl.twitterPicture(twitterId);
	}

	public boolean findSupport(int twitterId, int supporterId) {
		return supportDaoImpl.findSupport(twitterId, supporterId);
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
	}
}