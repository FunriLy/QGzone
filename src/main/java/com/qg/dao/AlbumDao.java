package com.qg.dao;

import java.util.List;

import com.qg.model.AlbumModel;

/**
 * 
 * @author zggdczfr
 * <p>
 * AlbumModel对象处理接口
 * </p>
 */

public interface AlbumDao {


	/**
	 * 将AlbumModel实体存进数据库
	 * @param album 存进数据库的AlbumModel实体
	 * @return 操作成功返回AlbumModel实体的编码，否则返回fail
	 */
	int createAlbum(AlbumModel album);
	
	/**
	 * 通过相册id来判断相册是否存在
	 * @param albumId 相册id
	 * @return 若相册存在返回success，否则返回fail
	 */
	int albumIsExist(int albumId);
	
	/**
	 * 在确保相册id存在的情况下
	 * 通过相册id来获取相册实体对象
	 * @param albumId 相册id
	 * @return 相应的相册实体
	 */
	AlbumModel getAlbumByAlbumId(int albumId);
	
	/**
	 * 修改相册的信息，包括相册权限、相册密码
	 * @param album 传入的相册实体
	 * @return 若修改成功返回success，否则返回fail。
	 */
	int updateAlbum(AlbumModel album);
	
	/**
	 * 根据相册编号来删除相册
	 * @param albumId
	 */
	void deleteAlbumByAlbumId(int albumId);
	
	/**
	 * 根据相册编号重命名相册
	 * @param albumId 相册编号
	 * @param albumName 新相册名
	 * @return 操作成功返回success，否则返回fail
	 */
	int uplateAlbumName(int albumId, String albumName);
	
	/**
	 * 根据用户编号获取用户相册编号
	 * @param userId 用户编号
	 * @return 用户相册编号集合
	 */
	public List<Integer> getAllAlbumIdByUserId(int userId);
	
	/**
	 * 根据相册id获得相册密码
	 * @param albumId 相册id
	 * @return 相册密码
	 */
	public String getPasswordByAlbumId(int albumId);
}
