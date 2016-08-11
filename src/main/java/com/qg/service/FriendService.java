package com.qg.service;

import java.util.ArrayList;
import java.util.List;

import com.qg.dao.FriendDao;
import com.qg.dao.impl.FriendDaoImpl;
import com.qg.model.FriendApplyModel;
import com.qg.model.UserModel;
import com.qg.util.Level;
import com.qg.util.Logger;

public class FriendService {
	private static final Logger LOGGER = Logger.getLogger(AlbumService.class);
	
	private static final int success = 1;
	private static final int fail = 0;
	
	/**
	 * 根据用户账号来获取用户好友列表
	 * @param userId 用户账号
	 * @return 用户好友列表
	 */
	public List<UserModel> myAllFriend(int userId){
		List<UserModel> myAllFriend = new ArrayList<UserModel>();
		
		return myAllFriend;
	}
	
	/**
	 * 判断用户间是否好友关系
	 * 测试完毕
	 * @param userId 用户账号
	 * @param t_userId 用户账号
	 * @return 若存在返回success，否则返回fail
	 */
	public int isFriend(int userId, int t_userId){
		int result = fail;
		FriendDao friendDao = new FriendDaoImpl();
		result = friendDao.isFriend(userId, t_userId);
		return result;
	}
	
	/**
	 * 用户删除好友
	 * 测试完毕
	 * @param userId 操作用户
	 * @param t_userId 被删除用户
	 * @return 若删除成功返回success，失败返回fail，若不存在好友关系返回3
	 */
	public int deleteFriend(int userId, int t_userId){
		int result = fail;
		//若用户间不存在好友关系，无法继续执行操作
		if(success != isFriend(userId, t_userId)){
			return 3;
		}
		FriendDao friendDao = new FriendDaoImpl();
		//执行操作
		result = friendDao.deleteFriend(userId, t_userId);
		
		/*
		 * 与我相关操作
		 */
		//若执行操作成功，返回与我相关操作
		if(success == result){
			LOGGER.log(Level.DEBUG, "service层删除好友操作");
		}
		return result;
	}
	
	/**
	 * 用户之间建立好友关系
	 * 测试完毕
	 * @param userId 用户账号
	 * @param t_userId 用户账号
	 * @return 若添加成功返回success，否则返回fail
	 */
	public int addFriend(int userId, int t_userId){
		int result = fail;
		if(success ==isFriend(userId, t_userId)){
			return fail;
		}
		FriendDao friendDao = new FriendDaoImpl();
		result = friendDao.addFriend(userId, t_userId);
		return result;
	}
	
	/**
	 * 好友申请处理
	 * @param friendApply 好友申请实体对象(处理状态、好友申请编号)
	 * @return 操作成功返回success，否则返回fail
	 */
	public int conductFriendApply(FriendApplyModel friendApply){
		int result = fail;
		if(success == isFriend(friendApply.getFriendApplyId(), friendApply.getResponserId())){
			return 3;
		}
		FriendDao friendDao = new FriendDaoImpl();
		friendDao.conductFriendApply(friendApply);
		//如果存在好友申请
		//如果同意好友申请
		if(1==friendDao.friendApplyIsExist(friendApply.getFriendApplyId()) && 1 == friendApply.getApplyState()){
			result = addFriend(friendApply.getFriendApplyId(), friendApply.getResponserId());
		}
//		//如果忽略好友申请
//		if(1==friendDao.friendApplyIsExist(friendApply.getFriendApplyId()) && 2== friendApply.getApplyState()){
//			/*
//			 * 与我相关操作
//			 */
//		}
		//拒绝好友申请
		if(1==friendDao.friendApplyIsExist(friendApply.getFriendApplyId()) && 1 == friendApply.getApplyState()){
			/*
			 * 与我相关操作
			 */
		}
		return result;
	}

	/**
	 * 用户根据好友申请编号删除好友申请
	 * @param friendApplyId 好友申请编号
	 * @return 成功返回success，否则返回fail
	 */
	public int deleteFriendApply(int friendApplyId){
		int result = fail;
		FriendDao friendDao = new FriendDaoImpl();
		result = friendDao.deleteFriendApply(friendApplyId);
		return result;
	}
	
	/**
	 * 用户发送好友申请
	 * @param requesterId 发送者
	 * @param responserId 接收者
	 * @return 成功返回success，否则返回fail
	 */
	public int sendFriendApply(int requesterId, int responserId){
		int result = fail;
		FriendDao friendDao = new FriendDaoImpl();
		result = friendDao.sendFriendApply(requesterId, responserId);
		return result;
	}
	
	/**
	 * 获取用户的好友申请
	 * @param userId 用户编号
	 * @return 用户好友申请列表
	 */
	public List<FriendApplyModel> getAllFriendApply(int userId){
		List<FriendApplyModel> allFriendApply = null;
		FriendDao friendDao = new FriendDaoImpl();
		allFriendApply = friendDao.getMyFriendApplies(userId);
		allFriendApply.addAll(friendDao.getMyAllFriendApply(userId));
		return allFriendApply;
	}
	
	/**
	 * 判断用户是否存在未处理好友申请
	 * @param userId 用户编号
	 * @return 若存在返回success，否则返回fail
	 */
	public int havefriendApply(int userId){
		int result = fail;
		FriendDao friendDao = new FriendDaoImpl();
		result = friendDao.havefriendApply(userId);
		return result;
	}
}
