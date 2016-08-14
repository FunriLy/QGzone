package com.qg.dao;

import java.util.List;

import com.qg.model.FriendApplyModel;

public interface FriendDao {
	
	/*
	 * 好友部分处理方法
	 */
	
	/**
	 * 判断用户是否存在好友关系
	 * @param userId 用户账号
	 * @param t_userId 用户账号
	 * @return 若存在好友关系返回success，否则返回fail
	 */
	int isFriend(int userId, int t_userId);
	
	/**
	 * 用户间添加好友关系
	 * @param userId 用户账号
	 * @param t_userId 用户账号
	 * @return 若成功返回success，否则返回fail
	 */
	int addFriend(int userId, int t_userId);
	
	/**
	 * 用户解除好友关系
	 * @param userId 主动解除者
	 * @param t_userId 被解除者
	 */
	int deleteFriend(int userId, int t_userId);
	
	/**
	 * 用户获得好友账号列表
	 * @param userId 用户账号
	 * @return 好友账号列表
	 */
	List<Integer> getMyFriendId(int userId);
	
	/*
	 * 好友申请部分处理方法
	 */
	
	/**
	 * 用户发送好友申请
	 * @param requesterId 发送者
	 * @param responserId 接收者
	 * @return 发送成功返回success，否则返回fail
	 */
	int sendFriendApply(int requesterId, int responserId );
	
	/**
	 * 根据申请编号判断好友申请是否存在
	 * @param friendApplyId 好友申请编号
	 * @return 若存在返回success，否则返回fail
	 */
	int friendApplyIsExist(int friendApplyId);
	
	/**
	 * 获得用户好友申请列表
	 * @param userId 用户账号
	 * @return 返回未读好友申请列表
	 */
	List<FriendApplyModel> getMyFriendApplies(int userId);
	
	/**
	 * 根据好友申请编号删除申请
	 * @param friendApplyId 好友申请编号
	 * @return 成功返回success，失败返回fail
	 */
	int deleteFriendApply(int friendApplyId);
	
	/**
	 * 用户处理好友申请
	 * @param friendModel 好友申请实体
	 * @return 若处理成功返回success，否则返回fail
	 */
	int conductFriendApply(int friendApplyId);
	
	/**
	 * 查看用户是否有未处理的好友申请
	 * @param requesterId 用户账号
	 * @return 若存在返回success，否则返回fail
	 */
	int havefriendApply(int responserId);
	
	/**
	 * 通过好友申请编号获得好友申请内容
	 * @param friendApplyId
	 * @return 好友申请对象
	 */
	FriendApplyModel getFriendApplyById(int friendApplyId);
}
