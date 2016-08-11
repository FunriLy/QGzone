package com.qg.model;

public class AlbumModel {
	
	private int albumId;    //相册id
	private int userId;   //用户id
	private String albumName;   //相册名
	private String albumPassword;  //相册密码
	private int albumState;   //相册权限
	private String albumUploadTime;  //相册上传时间
	private int photoCount; //图片数量
	
	public int getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(int photoCount) {
		this.photoCount = photoCount;
	}

	public AlbumModel(){}
	
	public AlbumModel(int userId, String albumName, int albumState, String albumPassword){
		this.userId = userId;
		this.albumName = albumName;
		this.albumState = albumState;
		this.albumPassword = albumPassword;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAlbumName() {
		return albumName;
	}

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumPassword() {
		return albumPassword;
	}

	public void setAlbumPassword(String albumPassword) {
		this.albumPassword = albumPassword;
	}

	public int getAlbumState() {
		return albumState;
	}

	public void setAlbumState(int albumState) {
		this.albumState = albumState;
	}

	public String getAlbumUploadTime() {
		return albumUploadTime;
	}

	public void setAlbumUploadTime(String albumUploadTime) {
		this.albumUploadTime = albumUploadTime;
	}
	
	
}
