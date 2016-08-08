package com.qg.model;

public class PhotoModel {

	private int photoId;  //图片id
	private int albumId;  //相册id
	private String photoUploadTime;  //图片上传时间
	
	public PhotoModel(){}
	
	public PhotoModel(int albumId){
		this.albumId = albumId;
	}

	public int getPhotoId() {
		return photoId;
	}

	public void setPhotoId(int photoId) {
		this.photoId = photoId;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public String getPhotoUploadTime() {
		return photoUploadTime;
	}

	public void setPhotoUploadTime(String photoUploadTime) {
		this.photoUploadTime = photoUploadTime;
	}
	
	
}
