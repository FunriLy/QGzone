package com.qg.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;

import com.mysql.jdbc.Statement;
import com.qg.dao.AlbumDao;
import com.qg.model.AlbumModel;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class AlbumDaoImpl implements AlbumDao {

	private static final Logger LOGGER = Logger.getLogger(AlbumDaoImpl.class);
	private static final int success = 1;
	private static final int fail = 0;
	
	private Connection con = null;
	private PreparedStatement pStatement = null;
	
	/**
	 * 类中公用关闭流的方法
	 */
	private void daoClose(){
		try {
			if(pStatement != null){
				pStatement.close();
			}     
			if(con != null){
				SimpleConnectionPool.pushConnectionBackToPool(con);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "SQL语句发送错误", e);
		}
	}	
	
	@Override
	public int createAlbum(AlbumModel album) {
		int result = fail;
		try {
			Date date = new Date();
			con = SimpleConnectionPool.getConnection();
			//将数据存进数据库
			String strSql = "insert into albums(user_id, album_name, album_state, album_password, album_upload_time, photo_count) value(?,?,?,?,?,?)";
			pStatement = con.prepareStatement(strSql, Statement.RETURN_GENERATED_KEYS);
			pStatement.setInt(1, album.getUserId());
			pStatement.setString(2, album.getAlbumName());
			pStatement.setInt(3, album.getAlbumState());
			pStatement.setString(4, album.getAlbumPassword());
			pStatement.setTimestamp(5, new Timestamp(date.getTime()));
			pStatement.setInt(6, 0);
			pStatement.executeUpdate();
			ResultSet rSet = pStatement.getGeneratedKeys();
			//获取存进数据库的实体的编号
			if(rSet.next()){
				result = Integer.valueOf(((Long)rSet.getObject(1)).toString());
				LOGGER.log(Level.DEBUG, "保存相册信息 用户id:{0}, 相册id:{1}", album.getUserId(), result);
			}
			rSet.close();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "创建相册实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int albumIsExist(int albumId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from albums where album_id = ?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			ResultSet rSet = pStatement.executeQuery();
			//若找到相应的相册
			if(rSet.next()){
				result = success;
			}
			rSet.close();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "判断相册是否存在实现类发送异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public AlbumModel getAlbumByAlbumId(int albumId) {
		AlbumModel album = null;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from albums where album_id = ?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			ResultSet rSet = pStatement.executeQuery();
			
			if(rSet.next()){
				int user_id = rSet.getInt("user_id");
				System.out.println("userId="+user_id);
				String album_name = rSet.getString("album_name");
				int album_state = rSet.getInt("album_state");
				String album_password = rSet.getString("album_password");
				Timestamp upload_time = rSet.getTimestamp("album_upload_time");
				int photo_count = rSet.getInt("photo_count");
				
				album = new AlbumModel(user_id, album_name, album_state, album_password);
				//将时间存进对象(格式：年-月-日 时-分-秒)
				album.setAlbumId(albumId);
				album.setPhotoCount(photo_count);
				album.setAlbumUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(upload_time));
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获得相册对象实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return album;
	}


	@Override
	public int updateAlbum(AlbumModel album) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "update albums set album_state=?, album_password=?, album_name=? where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, album.getAlbumState());
			pStatement.setString(2, album.getAlbumPassword());
			pStatement.setString(3, album.getAlbumName());
			pStatement.setInt(4, album.getAlbumId());
			pStatement.executeUpdate();
			//修改成功
			result = success;
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "更改相册信息实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public void deleteAlbumByAlbumId(int albumId) {
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "delete from albums where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.DEBUG, "删除相册实体实现类发生异常！", e);
		} finally {
			daoClose();
		}
	}

	@Override
	public int uplateAlbumName(int albumId, String albumName) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "update albums set album_name=? where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setString(1, albumName);
			pStatement.setInt(2, albumId);
			pStatement.executeUpdate();
			//修改成功
			result = success;
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "用户修改相册名实现类发送异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public List<AlbumModel> getAllAlbumIdByUserId(int userId) {
		List<AlbumModel> allAlbumId = new ArrayList<AlbumModel>();
		AlbumModel  album = new AlbumModel();
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from albums where user_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, userId);
			ResultSet rSet = pStatement.executeQuery();
			while(rSet.next()){
				int albumId = rSet.getInt("album_id");
				String album_name = rSet.getString("album_name");
				int album_state = rSet.getInt("album_state");
				Timestamp upload_time = rSet.getTimestamp("album_upload_time");
				int photo_count = rSet.getInt("photo_count");
				
				album = new AlbumModel(userId, album_name, album_state, "");
				//将时间存进对象(格式：年-月-日 时-分-秒)
				album.setAlbumId(albumId);
				album.setPhotoCount(photo_count);
				album.setAlbumUploadTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(upload_time));
				
				allAlbumId.add(album);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "用户获取所有相册编号实现类发送异常！", e);
		} finally {
			daoClose();
		}
		return allAlbumId;
	}

	@Override
	public String getPasswordByAlbumId(int albumId) {
		String result = null;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from albums where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			ResultSet rSet = pStatement.executeQuery();
			if(rSet.next()){
				result = rSet.getString("album_password");
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获取相册密码实现类发送异常！", e);
		}
		return result;
	}

	@Override
	public int isDuplicationOfName(int userId, String albumName, int albumId) {
		int result = success;
		try {
			con = SimpleConnectionPool.getConnection();
			String srSql = "select * from albums where (user_id=? and album_name=?) and album_id!=?";
			pStatement = con.prepareStatement(srSql);
			pStatement.setInt(1, userId);
			pStatement.setString(2, albumName);
			pStatement.setInt(3, albumId);
			ResultSet rSet = pStatement.executeQuery();
			if (rSet.next()) {
				result = fail;
			}
			
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "查询相册名重复实现类发送异常！", e);
		}
		return result;
	}

	@Override
	public int addPhotoCount(int albumId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql ="update albums set photo_count=photo_count+1 where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "增加相片数量实现类异常");
		}
		return result;
	}

	@Override
	public int subtractPhotoCount(int albumId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql ="update albums set photo_count=photo_count-1 where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "增加相片数量实现类异常!");
		}
		return result;
	}

	@Override
	public void deletePhotoCountByAlbumId(int albumId) {
		
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "update albums set photo_count=? where album_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, 0);
			pStatement.setInt(2, albumId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.DEBUG, "清空相册中相片数量实现类发送异常!");
		}
	}

}
