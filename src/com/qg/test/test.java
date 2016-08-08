package com.qg.test;

import java.sql.Date;

import com.qg.dao.AlbumDao;
import com.qg.model.AlbumModel;

//import org.apache.log4j.Logger;

import com.qg.util.NowTime;

public class test {
	public static void main(String[] args) {
//		Logger logger = Logger.getLogger(test.class);
//		logger.debug("test");
//		logger.error("test");
//		System.out.println("12300");
//		logger.debug("try");
	
		AlbumModel albumModel = new AlbumModel(1, "qwe", 1, "123");
		AlbumDao albumDao = new AlbumDao();
		albumDao.createAlbum(albumModel);
		
	
	}
}
