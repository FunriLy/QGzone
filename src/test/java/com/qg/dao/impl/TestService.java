package com.qg.dao.impl;

import java.util.List;

import com.qg.dao.AlbumDao;
import com.qg.model.AlbumModel;
import com.qg.service.AlbumService;

/**
 * Created by tisong on 8/9/16.
 */
public class TestService {
	public static void main(String[] args) {
		AlbumService albumDao = new AlbumService();
		List<AlbumModel> allAlbum = albumDao.getAllAlbumByUserId(0);
		for(AlbumModel albumModel : allAlbum){
			System.out.println(albumModel.getAlbumName());
		}
	}
}
