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
		
		int state = 201;//状态码
		int commentId = 0;//评论id
		int userId = 0;//当前登陆用户id

		/* 判断评论对象是否为null */
		if (request.getParameter("commentId").equals(null)||request.getParameter("commentId").equals("")) {
			state = 202;
			LOGGER.log(Level.ERROR, " 说说评论删除出现空指针");
		} else {
			// 获取说说评论id
			commentId = Integer.parseInt(request.getParameter("commentId"));
			// 获取当前用户Id
			 userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
			
			// 删除服务器上的说说评论信息
			if (!new TwitterCommentService().deleteComment(commentId, userId)) {
				state = 202;
			}
		}
		
		LOGGER.log(Level.DEBUG, " {0}想删除说说评论，其id为{1},状态为{2}", userId,commentId,state);
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}