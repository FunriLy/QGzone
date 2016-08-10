package com.qg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.qg.dao.NoteDao;
import com.qg.model.NoteModel;
import com.qg.model.TwitterModel;
import com.qg.util.Level;
import com.qg.util.Logger;
import com.qg.util.SimpleConnectionPool;

public class NoteDaoImpl implements NoteDao {
	private static final Logger LOGGER = Logger.getLogger(NoteDaoImpl.class);
	private Connection conn = null;
	private PreparedStatement pStatement = null;
	private ResultSet rs = null;
	SimpleDateFormat Format = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
	
	@Override
	public boolean addNote(NoteModel note) {
		boolean result = true;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "insert into note(note, target_id, note_man_id,time) value(?,?,?,?)";
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, note.getNote());
			pStatement.setInt(2, note.getTargetId());
			pStatement.setInt(3, note.getNoteManId());
			pStatement.setTimestamp(4,new Timestamp(new Date().getTime()));
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "添加留言异常！", e);
		} finally {
				close(null, pStatement, conn);
			}
    	return result;
	}

	@Override
	public List<NoteModel> getNote(int pageNumber, int userId) {
		List<NoteModel> notes = new ArrayList<NoteModel>();
		 try {
			 int number=(pageNumber-1)*16;
			 String sql = "SELECT * FROM note WHERE target_id=? ORDER BY note_id DESC LIMIT ?,16";
			 conn = SimpleConnectionPool.getConnection();				
			 pStatement=(PreparedStatement) conn.prepareStatement(sql);
			 pStatement.setInt(1, userId);
			 pStatement.setInt(2, number);
			 rs=pStatement.executeQuery();
			 UserDao userDao = new UserDao();
			 while(rs.next()){
				 NoteModel note = new NoteModel
				  (rs.getInt("note_id"),rs.getString("note"),rs.getInt("target_id"),
				  userDao.getNameById(rs.getInt("target_id")),rs.getInt("note_man_id"),userDao.getNameById(rs.getInt("note_man_id")),Format.format(rs.getTimestamp("time")),
				  new NoteCommentDaoImpl().getNoteCommentByNoteId(rs.getInt("note_id")));
				notes.add(note);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获得留言集合异常！", e);
		}finally{
			close(rs, pStatement, conn);
		}
		return notes;
	}

	@Override
	public NoteModel geNoteById(int noteId) {
		NoteModel noteModel;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql =  "SELECT * FROM note WHERE note_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, noteId);
			if(rs.next()){
				UserDao userDao = new UserDao();
				noteModel=new NoteModel
			(rs.getInt("note_id"),rs.getString("note"),rs.getInt("target_id"),
			 userDao.getNameById(rs.getInt("target_id")),rs.getInt("note_man_id"),userDao.getNameById(rs.getInt("note_man_id")),Format.format(rs.getTimestamp("time")),
				  new NoteCommentDaoImpl().getNoteCommentByNoteId(rs.getInt("note_id")));
				}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "根据留言id获取留言异常！", e);
		} finally {
			close(rs, pStatement, conn);
		}
    	return noteModel;
	}

	@Override
	public boolean deleteNote(int noteId) {
		boolean result = true;
		try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "DELETE FROM note WHERE note_id=?";
			pStatement=(PreparedStatement) conn.prepareStatement(sql);
			pStatement.setInt(1, noteId);
			new NoteCommentDaoImpl().deleteComments(noteId);
			pStatement.executeUpdate();
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "删除留言异常！", e);
			result=false;
		}finally{
			close(null, pStatement, conn);
		}
		return result;
	}


    public  void close(ResultSet rs,Statement stat,Connection conn){
        try {
            if(rs!=null)rs.close();
            if(stat!=null)stat.close();
            if(conn!=null)SimpleConnectionPool.pushConnectionBackToPool(conn);
        } catch (SQLException e) {
            e.printStackTrace();
       }
}

}
