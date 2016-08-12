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

import com.qg.dao.FriendDao;
import com.qg.model.FriendApplyModel;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class FriendDaoImpl implements FriendDao {

	private static final Logger LOGGER = Logger.getLogger(FriendDaoImpl.class);
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
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "SQL语句发送错误", e);
		}
	}	
	
	@Override
	public int isFriend(int userId, int t_userId) {
		// TODO Auto-generated method stub
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from friends where user_id=? and f_user_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, userId);
			pStatement.setInt(2, t_userId);
			ResultSet rSet = pStatement.executeQuery();
			//找到好友关系
			if(rSet.next()){
				result = success;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "判断好友关系实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int addFriend(int userId, int t_userId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "insert into friends(user_id, f_user_id) value(?, ?)";
			pStatement = con.prepareStatement(strSql);
			//正向添加纪录
			pStatement.setInt(1, userId);
			pStatement.setInt(2, t_userId);
			pStatement.executeUpdate();
			//反向添加纪录
			pStatement.setInt(1, t_userId);
			pStatement.setInt(2, userId);
			pStatement.executeUpdate();
			
			result = success;
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "建立好友关系实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int deleteFriend(int userId, int t_userId) {
		// TODO Auto-generated method stub
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "delete from friends where user_id=? and f_user_id=? or user_id=? and f_user_id=?";
			pStatement = con.prepareStatement(strSql);
			//将好友关系解除
			pStatement.setInt(1, userId);
			pStatement.setInt(2, t_userId);
			pStatement.setInt(3, t_userId);
			pStatement.setInt(4, userId);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "删除用户实现类发送异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public List<Integer> getMyFriendId(int userId) {
		// TODO Auto-generated method stub
		List<Integer> allFriendId = new ArrayList<Integer>();
		Integer friendId = null;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from friends where user_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, userId);
			ResultSet rSet = pStatement.executeQuery();
			while(rSet.next()){
				friendId = new Integer(rSet.getInt("f_user_id"));
				allFriendId.add(friendId);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "用户获得好友列表账号发生异常！", e);
		}
		return allFriendId;
	}

	@Override
	public int sendFriendApply(int requesterId, int responserId) {
		// TODO Auto-generated method stub
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "insert into f_apply(requester_id, responser_id, apply_time,apply_state) value(?,?,?,?)";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, requesterId);
			pStatement.setInt(2, responserId);
			pStatement.setTimestamp(3, new Timestamp(new Date().getTime()));
			pStatement.setInt(4, 0);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "发送好友申请实现类发送异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int friendApplyIsExist(int friendApplyId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from f_apply where f_apply_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, friendApplyId);
			ResultSet rSet = pStatement.executeQuery();
			if(rSet.next()){
				result = success;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "判断好友申请是否存在实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public List<FriendApplyModel> getMyFriendApplies(int userId) {
		List<FriendApplyModel> allFriendApply = new ArrayList<FriendApplyModel>();
		FriendApplyModel friendApply = null;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from f_apply where responser_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, userId);
			ResultSet rSet = pStatement.executeQuery();
			
			while(rSet.next()){
				int f_apply_id = rSet.getInt("f_apply_id");
				int requester_id = rSet.getInt("requester_id");
				int apply_state = rSet.getInt("apply_state");
				Timestamp time = rSet.getTimestamp("apply_time");
				
				friendApply = new FriendApplyModel(f_apply_id, requester_id, apply_state);
				friendApply.setApplyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time));
				allFriendApply.add(friendApply);
			}
		} catch (SQLException e) {
			// TODO: handle exception
			LOGGER.log(Level.ERROR, "获得用户未读好友申请列表实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return allFriendApply;
	}

	

	@Override
	public int deleteFriendApply(int friendApplyId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "delete from f_apply where f_apply_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, friendApplyId);
			pStatement.executeUpdate();
			result = success;
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "删除用户好友申请实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int conductFriendApply(int friendApplyId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "update f_apply set apply_state=? where f_apply_id=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, 1);;
			pStatement.setInt(2, friendApplyId);
			pStatement.executeUpdate();
			result = success;
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "用户处理好友申请实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}

	@Override
	public int havefriendApply(int responserId) {
		int result = fail;
		try {
			con = SimpleConnectionPool.getConnection();
			String strSql = "select * from f_apply where responser_id=? and apply_state=?";
			pStatement = con.prepareStatement(strSql);
			pStatement.setInt(1, responserId);
			pStatement.setInt(2, 0);
			ResultSet rSet = pStatement.executeQuery();
			if(rSet.next()){
				result = success;
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "用户请求未处理好友申请实现类发生异常！", e);
		} finally {
			daoClose();
		}
		return result;
	}


}
