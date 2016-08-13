package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.UserModel;
import com.qg.service.NoteCommentService;
import com.qg.util.JsonUtil;
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
		int commentId = Integer.getInteger(request.getParameter("commentId"));
		//获取当前用户Id
		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();

		// 删除服务器上的说说评论信息
		if (!new NoteCommentService().deleteComment(commentId,userId)) {
			state = 502;
		}

		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
}