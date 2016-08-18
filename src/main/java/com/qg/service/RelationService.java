package com.qg.service;

import java.util.List;

import com.qg.dao.*;
import com.qg.dao.impl.*;
import com.qg.model.*;

/**
 * 与我相关业务逻辑包装类
 * @author hunger linhange
 *
 */
public class RelationService {

	private UserDao userDao = new UserDaoImpl();
	private RelationDao relationDao = new RelationDaoImpl();
	
	public static void main(String[] args) {
		RelationService  service = new RelationService();
		RelationModel relation = new RelationModel("yellow movie", "ohh!", 1263677, 4638109, 1, 2);
		System.out.println(service.addRelation(relation));
	}
	/**
	 * 添加与我相关信息
	 * @param relation 与我相关对象
	 * @return 成功true,失败false
	 */
	public boolean addRelation(RelationModel relation){
		return relationDao.addRelation(relation);
	}
	
	/**
	 * 根据与我相关ID删除与我相关信息
	 * @param relationId 与我相关id
	 * @return 成功true 失败false
	 */
	public boolean deleteRelation(int relationId){
		return relationDao.deleteRelation(relationId);
	}
	
	
	/**
	 * 根据账号获取与我相关对象列表
	 * @param userId 账号
	 * @return 与我相关对象集合
	 */
	public List<RelationModel>  getRelationsById(int userId){
		return relationDao.getRelationsById(userId);
	}
	
	
	//留言、评论、回复、点赞、添加好友回应
	/**
	 * 留言板留言：noteAdd( 或na )
	 * 留言评论类型：noteComment( 或 nc )
	 * 说说回复类型：twitteComment( 或 tc )
	 * 点赞类型：supportTwitter( 或st )
	 * 好友添加：friendApply( 或 fa )
	 */
	public Object getRelationDetails(String relationType,int relatedId){
		
		TwitterDao tDao = new TwitterDaoImpl();
		TwitterCommentDao tcDao = new TwitterCommentDaoImpl();
		NoteCommentDao ncDao = new NoteCommentDaoImpl();
		SupportDao stDao = new SupportDaoImpl();
		FriendDao faDao = new FriendDaoImpl();
		Object object = null;
		switch (relationType) {
		case "na":
			object = ncDao.getNoteCommentById(relatedId);
			break;
		case "nc":
			object = ncDao.getNoteCommentById(relatedId);
			break;
		case "tc":
			object = tDao.geTwitterById(relatedId);
			break;
		case "st":
			object = tDao.geTwitterById(relatedId);
			break;
		default:
			break;
		}
		return object;
	}
	
	
	/**
	 * 根据账号获取与我相关对象列表(分页)
	 * @param page 页码
	 * @param userId 账号
	 * @return 与我相关对象集合
	 */
	public List<RelationModel> getRelationsById(int page, int userId){
		return relationDao.getRelationsById(page, userId);
	}
	
	
	/**
	 * 判断该账号是否有未读信息
	 * @param userId 账号
	 * @return 有true 无false
	 */
	public boolean hasRelationUnread(int userId){
		return relationDao.hasRelationUnread(userId);
	}
	
	
	/**
	 * 修改该账号的未读信息为已读
	 * @param userId 账号
	 * @return 成功true 失败false
	 */
	public boolean changeRelationHasRead(int userId){
		return relationDao.changeRelationHasRead(userId);
	}
	
	
	/**
	 * 根据账号获取未读信息集合
	 * @param userId 账号
	 * @return 未读信息集合
	 */
	public List<RelationModel> getUnReadRelationsById(int userId){
		return relationDao.getUnReadRelationsById(userId);
	}
}
