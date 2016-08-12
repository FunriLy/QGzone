package com.qg.service;

import java.io.File;
import java.util.List;

import com.qg.dao.PhotoDao;
import com.qg.dao.impl.PhotoDaoImpl;
import com.qg.util.Level;
import com.qg.util.Logger;

public class PhotoService {
	
	private static final Logger LOGGER = Logger.getLogger(AlbumService.class);
	
	private static final int success = 1;
	private static final int fail = 0;
	
	/**
	 * 根据路径删除文件
	 * @param photoPath 文件完整路径
	 * @return 成功返回success，失败返回fail
	 */
	public int deletePhoto(String photoPath){
		int result = fail;
		File file = new File(photoPath);
		if(file.isFile() && file.exists()){
			file.delete();
			LOGGER.log(Level.DEBUG, "用户删除相片  相片路径: {0}",photoPath);
			result = success;
		}
		return result;
	}
	
	/**
	 * 通过相册id获得相册中所有图片的信息
	 * @param albumId 相册id
	 * @return 返回相册内图片id
	 */
	public List<Integer> allPhoto(int albumId){
		List<Integer> allPhoto = null;
		PhotoDao photoDao = new PhotoDaoImpl();
		allPhoto = photoDao.getAllPhotoByAlbumId(albumId);
		return allPhoto;
	}
}
