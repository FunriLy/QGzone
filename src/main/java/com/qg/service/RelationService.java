package com.qg.service;

import java.util.List;

import com.qg.dao.RelationDao;
import com.qg.dao.UserDao;
import com.qg.dao.impl.RelationDaoImpl;
import com.qg.dao.impl.UserDaoImpl;
import com.qg.model.RelationModel;

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
}
