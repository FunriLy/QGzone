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
import com.qg.model.RelationModel;
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
		int noteId=0;
		Date newTime = new Date();
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "insert into note(note, target_id, note_man_id,time) value(?,?,?,?)";
			pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, note.getNote());
			pStatement.setInt(2, note.getTargetId());
			pStatement.setInt(3, note.getNoteManId());
			pStatement.setTimestamp(4,new Timestamp(newTime.getTime()));
			pStatement.executeUpdate();
			//获取插入数据库后留言的id
			conn = SimpleConnectionPool.getConnection();
			String SQL = "SELECT note_id FROM note WHERE time=?";
			pStatement = conn.prepareStatement(SQL);
			pStatement.setTimestamp(1, new Timestamp(newTime.getTime()));
			rs = pStatement.executeQuery();
			if (rs.next())
				noteId = rs.getInt("note_id");
			//插入与我相关表
			RelationModel relation = new RelationModel("na",note.getNote(),note.getTargetId(),note.getNoteManId(),0,noteId);
			new RelationDaoImpl().addRelation(relation);
			
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "添加留言异常！", e);
		} finally {
				close(null, pStatement, conn);
			}
    	return result;
	}

	@Override
	public List<NoteModel> getNote(int pageNumber, int userId) throws Exception {
		List<NoteModel> notes = new ArrayList<NoteModel>();
		 try {
			 int number=(pageNumber-1)*12;
			 String sql = "SELECT * FROM note WHERE target_id=? ORDER BY note_id DESC LIMIT ?,12";
			 conn = SimpleConnectionPool.getConnection();				
			 pStatement=(PreparedStatement) conn.prepareStatement(sql);
			 pStatement.setInt(1, userId);
			 pStatement.setInt(2, number);
			 rs=pStatement.executeQuery();
			 UserDaoImpl userDaoImpl = new UserDaoImpl();
			 while(rs.next()){
				 NoteModel note = new NoteModel
				  (rs.getInt("note_id"),rs.getString("note"),rs.getInt("target_id"),
				userDaoImpl.getUserById(rs.getInt("target_id")).getUserName(),rs.getInt("note_man_id"),userDaoImpl.getUserById(rs.getInt("note_man_id")).getUserName(),
				Format.format(rs.getTimestamp("time")),new NoteCommentDaoImpl().getNoteCommentByNoteId(rs.getInt("note_id")));
				notes.add(note);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.ERROR, "获得留言集合异常！", e);
			throw new Exception("添加说说异常!");
		}finally{
			close(rs, pStatement, conn);
		}
		return notes;
	}

	@Override
	public NoteModel geNoteById(int noteId) {
		NoteModel noteModel = null;
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql =  "SELECT * FROM note WHERE note_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, noteId);
			rs=pStatement.executeQuery();
			if(rs.next()){
				UserDaoImpl userDaoImpl = new UserDaoImpl();
				noteModel=new NoteModel
			(rs.getInt("note_id"),rs.getString("note"),rs.getInt("target_id"),
					userDaoImpl.getUserById(rs.getInt("target_id")).getUserName(),rs.getInt("note_man_id"),userDaoImpl.getUserById(rs.getInt("note_man_id")).getUserName(),
					Format.format(rs.getTimestamp("time")),new NoteCommentDaoImpl().getNoteCommentByNoteId(rs.getInt("note_id")));
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
			pStatement.executeUpdate();
			//删除其下全部回复
			new NoteCommentDaoImpl().deleteComments(noteId);
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

	@Override
	public boolean existNote(int noteId) {

    	boolean result = false;
    	
    	try {
			conn = SimpleConnectionPool.getConnection();
			String sql = "SELECT COUNT(1) FROM note WHERE note_id=?";
			pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, noteId);
			rs = pStatement.executeQuery();
			if(rs.next()){
				result=(rs.getInt(1)==1);
				}
    	} catch (SQLException e) {
    		LOGGER.log(Level.ERROR, "查询留言是否存在异常！", e);
		} finally {
			close(rs, pStatement, conn);
		}
    	return result;
	}

	@Override
	public int noteNumber(int userId) {
		int noteNumber = 0;
		try {
			 String sql = 	"SELECT  COUNT(1) FROM note WHERE target_id=?";
			 conn = SimpleConnectionPool.getConnection();				
			 pStatement=(PreparedStatement) conn.prepareStatement(sql);
			 pStatement.setInt(1, userId);
			 rs = pStatement.executeQuery();
			 while(rs.next()){
				 noteNumber = rs.getInt(1);
		       }
			 LOGGER.log(Level.DEBUG, "获取了{0}条留言", noteNumber);
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "获取留言数目异常！", e);
		}finally{
			close(rs, pStatement, conn);
		}
		
		return noteNumber;
	}

}
