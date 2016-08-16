package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.service.NoteService;
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
		// 获取留言id
		int noteId = Integer.parseInt(request.getParameter("noteId"));
		//获取当前用户Id
//		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		int userId =3;
		LOGGER.log(Level.DEBUG, " {0}想删除留言，其id为{1}", userId,noteId);
		// 删除服务器上的说说评论信息
		if (!new NoteService().deleteNote(noteId,userId)) {
			state = 502;
		}

		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}
