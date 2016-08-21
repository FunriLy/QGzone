package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.service.NoteService;
import com.qg.model.UserModel;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/NoteDelete")
/***
 * 
 * @author dragon
 * <pre>
 * 删除某条留言
 * 501删除成功 502删除失败 
 * </pre>
 */
public class NoteDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NoteDelete.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		
		int state = 501;
		int userId = 0;//登陆用户id
		int noteId = 0;//留言id
		
		/* 判断留言对象是否为null */
		if (request.getParameter("noteId").equals(null)||request.getParameter("noteId").equals("")) {
			state = 502;
			LOGGER.log(Level.ERROR, " 留言删除出现空指针");
		} else {
			
			// 获取留言id
			 noteId = Integer.parseInt(request.getParameter("noteId"));
			// 获取当前用户Id
			 userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
			
			// 删除服务器上的说说评论信息
			if (!new NoteService().deleteNote(noteId, userId)) {
				state = 502;
			}
		}
		LOGGER.log(Level.DEBUG, " {0}想删除留言，其id为{1} 状态{2}", userId, noteId, state);
		
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}
