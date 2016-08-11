package com.qg.service;

import java.io.File;

import com.qg.util.Level;
import com.qg.util.Logger;

public class PhotoService {
	
	private static final Logger LOGGER = Logger.getLogger(AlbumService.class);
	
	private static final int success = 1;
	private static final int fail = 0;
	
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
}
