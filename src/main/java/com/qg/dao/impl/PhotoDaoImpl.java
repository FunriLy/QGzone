package com.qg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.Statement;
import com.qg.dao.PhotoDao;
import com.qg.model.PhotoModel;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class PhotoDaoImpl implements PhotoDao {

	private static final Logger LOGGER = Logger.getLogger(PhotoDaoImpl.class);
	private static final int fail = 0;
	private static final int success = 1;
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd HH:mm:ss:SSS");
	
	
	private Connection con = null;
	private PreparedStatement pStatement = null;
	
	private void daoClose() {
		try {
			if(pStatement != null){
				pStatement.close();
			}
			if(con != null){
				SimpleConnectionPool.pushConnectionBackToPool(con);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "SQL语句发送错误", e);
		}
	}
	
	@Override
	public int savePhotoByAlbumId(int albumId) {
		int result = fail;
		try {
			Date date = new Date();
			con = SimpleConnectionPool.getConnection();
			//存进数据库
			String strSql = "insert into photos(album_id, photo_upload_time) value(?, ?)";
			pStatement = con.prepareStatement(strSql, Statement.RETURN_GENERATED_KEYS);
			pStatement.setInt(1, albumId);
			pStatement.setString(2, sd.format(date));
			pStatement.executeUpdate();
			ResultSet rSet = pStatement.getGeneratedKeys();
			
			if (rSet.next()) {
				result = (Integer)rSet.getObject(1);
				LOGGER.log(Level.DEBUG, "保存相册信息 相册id:{0}, 相片id:{1}", albumId, result);
			}
			
			rSet.close();
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "保存相册实现类发生异常", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public List<Integer> getAllPhotoByAlbumId(int albumId) {
		List<Integer> allPhoto = new ArrayList<Integer>();
		//PhotoModel photo = new PhotoModel();
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from photos where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			ResultSet rSet = pStatement.executeQuery();
			//获取集合
			while(rSet.next()){
				int photo_id = rSet.getInt("photo_id");
				//Timestamp upload_time = rSet.getTimestamp("photo_upload_time");
				//photo = new PhotoModel(albumId);
				//photo.setPhotoId(photo_id);
				//photo.setPhotoUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(upload_time));
				allPhoto.add(photo_id);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获得所有相片实体实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return allPhoto;
	}

	@Override
	public int deletePhotoByPhotoId(int photoId) {
		// TODO Auto-generated method stub
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "delete from photos where photo_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, photoId);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "删除相片实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public void deleteAllPhotoByAlbumId(int albumId) {
		// TODO Auto-generated method stub
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "delete from photos where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "删除相册中所有相片的实现类发生异常！", e);
		} finally {
			daoClose();
		}
	}

	@Override
	public PhotoModel getPhotoByPhotoId(int photoId) {
		PhotoModel photo = null;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from photos where photo_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, photoId);
			ResultSet rSet = pStatement.executeQuery();
			if(rSet.next()){
				int album_id = rSet.getInt("album_id");
				String upload_time = rSet.getString("photo_upload_time");
				photo = new PhotoModel(album_id);
				photo.setPhotoId(photoId);
				photo.setPhotoUploadTime(upload_time);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "通过编号获得相片实体实现类发送异常！", e);
		}
		return photo;
	}

	@Override
	public List<PhotoModel> getPhotoByUserId(int userId) {
		//获取前两张图片的信息
		int state = 0;
		List<PhotoModel> photoList = new ArrayList<PhotoModel>();
		PhotoModel photo = null;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select distinct albums.album_id,photo_id from photos "
						+ "inner join albums on "
						+ "albums.user_id=? "
						+ "and albums.album_state=0 "
						+ "and albums.album_id=photos.album_id;";
			pStatement = (PreparedStatement) con.prepareStatement(strSql);
			pStatement.setInt(1, userId);
			ResultSet rSet = pStatement.executeQuery();
			
			while(rSet.next() && state<2){
				state++;
				int photo_id = rSet.getInt("photo_id");
				int album_id = rSet.getInt("album_id");
				photo = new PhotoModel(album_id);
				photo.setPhotoId(photo_id);
				photoList.add(photo);
			}
					
		} catch (SQLException e) {
			LOGGER.log(Level.DEBUG, "获取用户相片展示实现类发生异常！", e);
		}
		return photoList;
	}

}
