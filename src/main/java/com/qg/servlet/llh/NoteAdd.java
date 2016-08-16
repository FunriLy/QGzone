package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.NoteModel;
import com.qg.service.FriendService;
import com.qg.service.NoteService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/NoteAdd")
/**
 * 
 * @author dragon
 * 
 *  <pre>
 *  添加留言的类
 *  501留言成功 502留言失败 503留言过长 504还不是好友，无法留言
 *  </pre>
 */
public class NoteAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NoteAdd.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int noteId = 0;
		int state = 501;
		/* 获取被留言者id,留言内容 */
		int targetId = Integer.parseInt((request.getParameter("targetId")));
		String note = request.getParameter("note");
		// 当前用户=留言者
		int noteManId=3;
//		int noteManId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		if(new FriendService().isFriend(noteManId, targetId)==1||targetId==noteManId){
			if (!(note.length() > 120)) {
				// 获取留言的实体类
				NoteModel noteModel = new NoteModel(note, targetId, noteManId);
				// 存进数据库
				noteId = new NoteService().addNote(noteModel);
				// 打包发送
			} else
				state = 503;
		}else {
			state =504;
		}
		LOGGER.log(Level.DEBUG, "留言:{0}  被留言者id:{1}  当前用户Id:{2}  留言id为:{3}", note,targetId,noteManId,noteId);
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state,noteId).getBytes("UTF-8"));
		output.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}