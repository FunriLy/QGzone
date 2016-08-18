package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.service.TwitterCommentService;
import com.qg.util.JsonUtil;
import com.qg.model.UserModel;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/TwitterCommentDelete")
/***
 * 
 * @author dragon
 * <pre>
 * 删除说说评论
 * 201-删除成功 202删除失败
 * </pre>
 */
public class TwitterCommentDelete extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterCommentDelete.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 201;
		// 获取说说评论id
		int commentId = Integer.parseInt(request.getParameter("commentId"));
		//获取当前用户Id
		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
//		int userId=3;
		// 删除服务器上的说说评论信息
		LOGGER.log(Level.DEBUG, " {0}想删除说说评论，其id为{1}", userId,commentId);
		if (!new TwitterCommentService().deleteComment(commentId,userId)) {
			state = 202;
		}

		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}