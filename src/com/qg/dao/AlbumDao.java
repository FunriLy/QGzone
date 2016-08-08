package com.qg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.qg.model.AlbumModel;
import com.qg.util.NowTime;
import com.qg.util.SimpleConnectionPool;

/**
 * 
 * @author zggdczfr
 * <p>
 * albumModel对象数据库处理类
 * </p>
 */

public class AlbumDao {
	
	private Connection con = null;
	private PreparedStatement pStatement = null;
	
	private final static int success = 1;
	private final static int fail = 0;
	
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
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	/**
	 * 将相册实体类存入数据库
	 * @param album 相册实体类
	 */
	public void createAlbum(AlbumModel album){
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "insert into albums(user_id, album_name, album_state, "
					+ "album_password, album_upload_time) value(?,?,?,?,?)";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, album.getUserId());
			pStatement.setString(2, album.getAlbumName());
			pStatement.setInt(3, album.getAlbumState());
			pStatement.setString(4, album.getAlbumPassword());
			pStatement.setDate(5, NowTime.getCurrentDateTime());
			pStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			daoClose();
		}
	}
	
	/**
	 * 通过相册id来判断相册是否存在
	 * @param albumId 相册id
	 * @return 若相册存在返回success，否则返回fail
	 */
	public int albumIsExitst(int albumId){
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			ResultSet rSet = pStatement.executeQuery();
			//若找到相应的相册
			if(rSet.next()){
				result = success;
			}
			rSet.close();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			daoClose();
		}
		return result;
	}
	
	/**
	 * 在确保相册id存在的情况下
	 * 通过相册id来获取相册实体对象
	 * @param albumId 相册id
	 * @return 相应的相册实体
	 */
	public AlbumModel getAlbumByAlbumId(int albumId){
		AlbumModel album = null;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, albumId);
			ResultSet rSet = pStatement.executeQuery();
			
			if(rSet.next()){
				int user_id = rSet.getInt("user_id");
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return album;
	}
	
	
	
	
}
