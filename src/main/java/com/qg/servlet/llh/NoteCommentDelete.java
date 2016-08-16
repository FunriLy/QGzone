package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.service.NoteCommentService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/NoteCommentDelete")
/***
 * 
 * @author dragon
 * <pre>
 * 删除留言评论
 * 501删除成功 502删除失败 
 * </pre>
 */
public class NoteCommentDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NoteCommentDelete.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

		int state = 501;
		// 获取说说评论id
		int commentId = Integer.parseInt(request.getParameter("commentId"));
		//获取当前用户Id
//		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		int userId =3;
		// 删除服务器上的说说评论信息
		if (!new NoteCommentService().deleteComment(commentId,userId)) {
			state = 502;
		}
		LOGGER.log(Level.DEBUG, " {0}想删除留言评论，其id为{1}", userId,commentId);
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}