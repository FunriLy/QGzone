package com.qg.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.qg.dao.AlbumDao;
import com.qg.dao.PhotoDao;
import com.qg.dao.impl.AlbumDaoImpl;
import com.qg.dao.impl.PhotoDaoImpl;
import com.qg.model.AlbumModel;
import com.qg.util.Logger;

public class AlbumService {
	
	private static final Logger LOGGER = Logger.getLogger(AlbumService.class);
	
	private static final int success = 1;
	private static final int fail = 0;
	
	/**
	 * 创建相册文件夹
	 * @param album 传入的AlbumModel实体
	 * @return 创建成功返回success，否则返回fail
	 */
	public int createAlbum(AlbumModel album){
		int result = fail;
		AlbumDao albumDao = new AlbumDaoImpl();
		result = albumDao.createAlbum(album);
		/*
		 * 创建文件夹
		 */
		if(result != fail){
			
			
			
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
	 * @param albumId 相册编号
	 * @return 操作成功返回AlbumModel实体对象，否则返回null
	 */
	public AlbumModel getAlbumByAlbumId(int albumId){
		AlbumModel album = null;
		AlbumDao albumDao = new AlbumDaoImpl();
		PhotoDao photoDao = new PhotoDaoImpl();
		//如果相册存在,获得相册信息和相片数量
		if(success == albumIsExist(albumId)){
			album = albumDao.getAlbumByAlbumId(albumId);
			album.setPhotoCount(photoDao.getPhotoCountByAlbumId(albumId));
		}
		return album;
	}
	
	/**
	 * 更改相册信息(相册权限、相册密码)
	 * @param album 存入的相册实体
	 * @return 若成功返回success，否则返回fail
	 */
	public int uplateAlbum(AlbumModel album){
		int result = fail;
		AlbumDao albumDao = new AlbumDaoImpl();
		result = albumDao.updateAlbum(album);
		return result;
	}
	
	/**
	 * 用户更改相册名
	 * @param albumId 相册编号
	 * @param albumName 新相册名
	 * @return 操作成功返回success，否则返回fail
	 */
	public int uplateAlbumName(int albumId, String albumName){
		int result = fail;
		AlbumDao albumDao = new AlbumDaoImpl();
		result = albumDao.uplateAlbumName(albumId, albumName);
		return result;
	}
	
	/**
	 * 通过用户编号获取用户所有相册信息
	 * @param userId 用户编号
	 * @return 用户相册的集合；若用户没有相册则返回null
	 */
	public List<AlbumModel> getAllAlbumByUserId(int userId){
		List<AlbumModel> allAlbum = new ArrayList<AlbumModel>();
		List<Integer> allAlbumId = null;
		AlbumModel album = null;
		
		AlbumDao albumDao = new AlbumDaoImpl();
		allAlbumId = albumDao.getAllAlbumIdByUserId(userId);
		//若用户相册不为空
		if(allAlbumId != null){
			for(Integer albumId : allAlbumId){
				album = getAlbumByAlbumId(albumId);
				//将相册对象的密码设置为空,并将对象加入到集合中
				//避免其他用户获得相册密码
				album.setAlbumPassword("");
				allAlbum.add(album);
			}
		}
		return allAlbum;
	}

	/**
	 * 删除文件夹
	 * @param folderPath 文件夹完整绝对路径
	 */
	public void deleteAlbum(String folderPath){
		delFolder(folderPath);
	}
	
	/**
	 * 删除文件夹中所有文件，而不删除文件夹本身
	 * @param folderPath 文件夹完整绝对路径
	 */
	public void emptyAlbum(String folderPath){
		delAllFile(folderPath);
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
