package com.qg.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.qg.dao.AlbumDao;
import com.qg.dao.PhotoDao;
import com.qg.dao.impl.AlbumDaoImpl;
import com.qg.dao.impl.PhotoDaoImpl;
import com.qg.model.AlbumModel;

public class AlbumService {
	
	
	private static final int success = 1;
	private static final int fail = 0;
	
	/**
	 * 修改相册图片数量
	 * @param mold 类型
	 * @param albumId 相册id
	 */
	public void changePhotoCount(int mold, int albumId){
		AlbumDao albumDao = new AlbumDaoImpl();
		if (mold == success) {
			albumDao.addPhotoCount(albumId);
		} else {
			albumDao.subtractPhotoCount(albumId);
		}
		
	}
	
	
	/**
	 * 判断用户个人的相册名是否重复
	 * @param userId 用户id
	 * @param albumName 相册名
	 * @return 若成功返回601，否则返回602
	 */
	public int isDuplicationOfName(int userId, String albumName, int albumId){
		int result = 602;
		
		AlbumDao albumDao = new AlbumDaoImpl();
		if (success == albumDao.isDuplicationOfName(userId, albumName, albumId)) {
			result = 601;
		}
		
		return result;
	}
	
	/**
	 * 已经验证存在性与权限问题
	 * 修改相册信息
	 * @param album
	 * @return 603-格式错误; 604-相册重名; 602-失败; 601-成功;
	 */
	public int uplateAlbum(AlbumModel album){
		int result = 602;
		//格式错误
		if (success != doCreateAlbum(album.getAlbumPassword(), album.getAlbumName(), album.getAlbumState())) {
			return 603;
		}
		if (601 != isDuplicationOfName(album.getUserId(), album.getAlbumName(), album.getAlbumId())) {
			
			return 604;
		}
		AlbumDao albumDao = new AlbumDaoImpl();
		if (success == albumDao.updateAlbum(album)) {
			result = 601;
		}
		return result;
	}
	
	
	/**
	 * 创建相册文件夹
	 * @param album 传入的AlbumModel实体
	 * @return 创建成功返回相册id，失败返回fail(0)，若格式错误返回-1，若重名返回-2
	 */
	public int createAlbum(AlbumModel album, String userPath){
		int state = fail;
		int result = fail;
		AlbumDao albumDao = new AlbumDaoImpl();
		
		//格式错误
		if (success != doCreateAlbum(album.getAlbumPassword(), album.getAlbumName(), album.getAlbumState())) {
			return -1;
		}
		//重名
		if (success != albumDao.isDuplicationOfName(album.getUserId(), album.getAlbumName(), 0)) {
			return -2;
		}
		//更新到数据库
		if (album.getAlbumState() != success) {
			album.setAlbumPassword("0");
		}
		result = albumDao.createAlbum(album);
		/*
		 * 创建文件夹
		 */
		if(result != fail){
			
			String albumPath = userPath + "/" + String.valueOf(result);
			File file = new File(albumPath);
			//若文件不存在，创建该文件夹
			if(!file.exists()){
				file.mkdirs();
				state = success;
			}
			
		}
		//确保文件夹已经建立
		if(state != success){
			result = fail;
		}
		
		return result;
	}
	
	/**
	 * 判断相册密码和名字格式是否错误
	 * @param password 密码
	 * @param albumName 名字
	 * @return 若正确返回success， 否则返回fail
	 */
	public int doCreateAlbum(String password, String albumName, int albumState){
		String regex1 ="[a-z0-9A-Z_]{5,15}";
		String regex2 ="[a-z0-9A-Z\u4e00-\u9fa5]{1,15}";
		int result = success;
		//密码格式错误或者命名格式错误
		if (albumState==success && !password.matches(regex1)) {
			return fail;
		}
		
		if ((!albumName.matches(regex2))) {
			return fail;
		}
		
		return result;
	}
	
	/**
	 * 用户查看公开相册
	 * @param albumId 相册id
	 * @return 状态码: 601-成功; 602-没有相片; 608-相册不存在; 605-相册权限不符; 606-非好友关系;
	 */
	public int checkPublicAlbum(int albumId, int userId){
		int state = 602;
		AlbumModel realAlbum = getAlbumByAlbumId(albumId);
		FriendService friendService = new FriendService();
		if (success != friendService.isFriend(userId, realAlbum.getUserId()) && userId!= realAlbum.getUserId()) {
			//非好友关系
			state = 606;
		} else if (success == albumIsExist(albumId)) {
			//好友关系或者主人访问
			//权限不符
			if(success == realAlbum.getAlbumState()){
				state = 607;
			} else if(0 == realAlbum.getPhotoCount()){
				//数量为0
				state = 605;
			} else {
				//相片数量不为0
				state = 601;
			}
		} else {
			state = 608;
		}
		return state;
	}
	
	
	
	/**
	 * 用户查看私密相册,返回状态码
	 * @param album 用户输入的相册对象
	 * @param userId 用户id
	 * @return 状态码: 601-成功; 602-密码错误; 608-相册不存在; 605-相片为0; 606-非好友关系
	 */
	public int checkPrivacyAlbum(AlbumModel album, int userId){
		int state = 602;
		FriendService friendService = new FriendService();
		AlbumModel realAlbum = getAlbumByAlbumId(album.getAlbumId());
		if (success == albumIsExist(album.getAlbumId())) {
			//用户为相册创建者
			if (userId == realAlbum.getUserId()) {
				
				//相片数目为0
				if (0 == realAlbum.getPhotoCount()) {
					state = 605;
				} else {
					//创建者获得相册内容
					state = 601;
				}
				
			} else if(success != friendService.isFriend(userId, realAlbum.getUserId())){
				
				//非好友关系
				state = 606;
				
			} else if(realAlbum.getAlbumPassword().equals(album.getAlbumPassword())){
				//验证密码正确
				
				//相片数目为0
				if (0 == realAlbum.getPhotoCount()) {
					state = 605;
				} else {
					//获得相册内容
					state = 601;
				}
				
			} else {
				//密码错误
				state = 602;
			}
		} else {
			state = 608;
		}
		return state;
	}
	
	
	
	/**
	 * 根据相册id判断密码是否正确
	 * @param albumId 相册id
	 * @param albumPassword 用户输入的密码
	 * @return 若存在返回success，否则返回fail
	 */
	public int isPassword(int albumId, String albumPassword){
		int result = fail;
		AlbumDao albumDao = new AlbumDaoImpl();
		String realPassword = albumDao.getPasswordByAlbumId(albumId);
		if(realPassword.equals(albumPassword)){
			result = success;
		}
		return result;
	}
	
	/**
	 * 通过相册编号判断相册是否存在
	 * @param albumId 相册编号
	 * @return 若存在返回success，否则返回fail
	 */
	public int albumIsExist(int albumId){
		AlbumDao albumDao = new AlbumDaoImpl();
		int result = albumDao.albumIsExist(albumId);
		return result;
	}
	
	/**
	 * 通过相册编号获取相册对象
	 * 该相册信息包括相片数量
	 * @param albumId 相册编号
	 * @return 操作成功返回AlbumModel实体对象，否则返回空对象
	 */
	public AlbumModel getAlbumByAlbumId(int albumId){
		AlbumModel album = new AlbumModel();
		AlbumDao albumDao = new AlbumDaoImpl();
		//如果相册存在,获得相册信息和相片数量
		System.out.println(albumIsExist(albumId));
		if(success == albumIsExist(albumId)){
			album = albumDao.getAlbumByAlbumId(albumId);
		}
		//防止空指针
		
		return album;
	}
	

	/**
	 * 用户更改相册名
	 * @param albumId 相册编号
	 * @param albumName 新相册名
	 * @return 状态码: 601-修改成功; 602-修改失败;  603-格式错误;
	 */
	public int uplateAlbumName(int albumId, String albumName){
		int result = 602;
		String regex ="[a-z0-9A-Z\u4e00-\u9fa5]{1,15}";
				
		//命名正确
		if (albumName.matches(regex)) {
			AlbumDao albumDao = new AlbumDaoImpl();
			if (success == albumDao.uplateAlbumName(albumId, albumName) ) {
				result = 601;
			}
		} else {
			result = 603;
		}
		
		return result;
	}
	
	/**
	 * 通过用户编号获取用户所有相册信息,密码设置为空
	 * @param userId 用户编号
	 * @return 用户相册的集合；若用户没有相册则返回null
	 */
	public List<AlbumModel> getAllAlbumByUserId(int userId){
		List<AlbumModel> allAlbum = new ArrayList<AlbumModel>();
		
		AlbumDao albumDao = new AlbumDaoImpl();
		allAlbum = albumDao.getAllAlbumIdByUserId(userId);
		
		return allAlbum;
	}

	/**
	 * 用户删除相册，
	 * @param folderPath 文件夹绝对路径
	 * @return 状态码: 601-删除成功; 602-失败;  608-相册不存在;
	 */
	public int deleteAlbum(String folderPath, int albumId){
		int state = 602;
		AlbumDao albumDao = new AlbumDaoImpl();
		
		File file = new File(folderPath);
		if (!file.exists()) {
			state = 608;
		} else {
			//删除相册并删除记录
			delFolder(folderPath);
			PhotoDao photoDao = new PhotoDaoImpl();
			photoDao.deleteAllPhotoByAlbumId(albumId);
			albumDao.deleteAlbumByAlbumId(albumId);
			state = 601;
		}
		return state;
	}
	
	/**
	 * 用户清空相册，
	 * @param folderPath 文件夹绝对路径
	 * @return 状态码: 601-删除成功; 602-失败;  608-相册不存在;
	 */
	public int emptyAlbum(String folderPath, int albumId){
		int state = 602;
		
		File file = new File(folderPath);
		if (!file.exists()) {
			state = 608;
		} else {
			//清空相册
			delAllFile(folderPath);
			PhotoDao photoDao = new PhotoDaoImpl();
			photoDao.deleteAllPhotoByAlbumId(albumId);
			AlbumDao albumDao = new AlbumDaoImpl();
			albumDao.deletePhotoCountByAlbumId(albumId);
			state = 601;
		}
		return state;
	}
	
	/**
	 * 删除文件夹
	 * @param folderPath文件夹完整绝对路径
	 */
	private static void delFolder(String folderPath){
		//删除文件夹中所有内容
		delAllFile(folderPath);
		String filePath = folderPath;
		filePath = filePath.toString();
		
		File theFilePath = new File(filePath);
		theFilePath.delete();
	}
	
	/**
	 * 删除指定文件夹下所有的文件内容
	 * @param path 文件夹完整绝对路径
	 * @return 若成功返回success，否则返回fail
	 */
	private static int delAllFile(String path){
		int result = fail;
		File file = new File(path);
		//文件不存在
		if(!file.exists()){
			return result;
		}
		//文件不是一个目录
		if(!file.isDirectory()){
			return result;
		}
		//获得文件夹内列表
		String[] tempList = file.list();
		File tempLife = null;
		for(int i=0; i<tempList.length; i++){
			//如果文件是以文件分隔符结尾
			if(path.endsWith(File.separator)){
				tempLife = new File(path + tempList[i]);
			} else {
				tempLife = new File(path + File.separator + tempList[i]);
			}
			if(tempLife.isFile()){
				tempLife.delete();
			}
			if(tempLife.isDirectory()){
				//先删除文件夹中的内容
				delAllFile(path + "/" + tempList[i]);
				//再删除空文件夹
				delFolder(path + "/" + tempList[i]);
				result = success;
			}
		}
		return result;
	}
}
