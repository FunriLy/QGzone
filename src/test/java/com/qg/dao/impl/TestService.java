package com.qg.dao.impl;

import java.util.List;

import com.qg.dao.PhotoDao;
import com.qg.model.PhotoModel;

/**
 * Created by tisong on 8/9/16.
 */
public class TestService {
	public static void main(String[] args) {
		PhotoDao photoDao = new PhotoDaoImpl();
		List<PhotoModel> photoModels = photoDao.getPhotoByUserId(1);
		for(PhotoModel photoModel : photoModels){
			System.out.println(photoModel.getPhotoId());
		}
	}
}
