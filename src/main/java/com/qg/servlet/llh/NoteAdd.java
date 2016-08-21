package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.NoteModel;
import com.qg.model.UserModel;
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
 *  501留言成功   502留言失败    503留言过长    504还不是好友，无法留言
 *  </pre>
 */
public class NoteAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NoteAdd.class);

	@Override 
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		
		int noteId = 0;//留言id
		int state = 501;//状态码
		int targetId = 0;//留言对象id
		int noteManId = 0;//留言者id
		String note =null;//留言内容
		
		/* 判断是否留言对象和留言是否为null*/
		if (request.getParameter("targetId").equals("") || request.getParameter("targetId").equals(null)
				|| request.getParameter("note").equals(null)|| request.getParameter("note").equals("")) {
			state = 502;
			LOGGER.log(Level.ERROR, " 留言失败，未获取到留言信息");
		}
		else {
			/* 获取被留言者id,留言内容 */
			 targetId = Integer.parseInt((request.getParameter("targetId")));
			 note = request.getParameter("note");
			
			// 获取当前当前用户(留言者)
			 noteManId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
			
			/* 判断留言者和留言对象是否为好友或者留言者是否为本人 */
			if (new FriendService().isFriend(noteManId, targetId) == 1 || targetId == noteManId) {
				
				/* 判断字数是否超限 */
				if (!(note.length() > 150)) {
					
					// 获取留言的实体类
					NoteModel noteModel = new NoteModel(note, targetId, noteManId); 
					
					// 存进数据库
					noteId = new NoteService().addNote(noteModel);
					
				} else
					state = 503;
			} else {
				state = 504;
			}

		}
		
		LOGGER.log(Level.DEBUG, "{4}留言:{0}  被留言者id:{1}  当前用户Id:{2}  留言id为:{3}  状态为{5}", note, targetId, noteManId,
				noteId, noteManId, state);
		
		// 打包发送
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state, noteId).getBytes("UTF-8"));
		output.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}