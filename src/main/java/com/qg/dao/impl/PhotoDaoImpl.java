package com.qg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.qg.dao.PhotoDao;
import com.qg.model.PhotoModel;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class PhotoDaoImpl implements PhotoDao {

	private static final Logger LOGGER = Logger.getLogger(PhotoDaoImpl.class);
	private static final int success = 1;
	private static final int fail = 0;
	
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
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			pStatement.setTimestamp(2, new Timestamp(date.getTime()));
			pStatement.executeUpdate();
			//获取id
			String strSql2 = "select * from photos where photo_upload_time=?";
			pStatement = con.prepareStatement(strSql2);
			pStatement.setTimestamp(1, new Timestamp(date.getTime()));
			ResultSet rSet = pStatement.executeQuery();
			if(rSet.next()){
				result = rSet.getInt("photo_id");
				LOGGER.log(Level.DEBUG, "保存相册信息 相册id:{0}, 相片id:{1}", albumId, result);
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "保存相册实现类发生异常", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public List<PhotoModel> getAllPhotoByAlbumId(int albumId) {
		List<PhotoModel> allPhoto = null;
		PhotoModel photo = null;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			ResultSet rSet = pStatement.executeQuery();
			//获取集合
			while(rSet.next()){
				int photo_id = rSet.getInt("photo_id");
				Timestamp upload_time = rSet.getTimestamp("photo_upload_time");
				photo = new PhotoModel(albumId);
				photo.setPhotoId(photo_id);
				photo.setPhotoUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(upload_time));
			}
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "获得所有相片实体实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return null;
	}

	@Override
	public void deletePhotoByPhotoId(int photoId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAllPhotoByAlbumId(int albumId) {
		// TODO Auto-generated method stub

	}

	@Override
	public PhotoModel getPhotoByPhotoId(int photoId) {
		// TODO Auto-generated method stub
		return null;
	}

}
