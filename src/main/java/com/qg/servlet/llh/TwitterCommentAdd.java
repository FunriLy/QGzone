package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.TwitterCommentModel;
import com.qg.model.UserModel;
import com.qg.service.TwitterCommentService;
import com.qg.util.JsonUtil;
import com.qg.util.Logger;

@WebServlet("/TwitterCommentAdd")
/***
 * 
 * @author dragon
 * <pre>
 * 添加说说评论
 * 201发表成功  202发表失败  203评论过长 
 * </pre>
 */
public class TwitterCommentAdd extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterCommentAdd.class);
	
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 201;
		/* 获取说说id，被评论方id,评论内容 */
		int twitterId = Integer.getInteger(request.getParameter("twitterId"));
		int targetId = Integer.getInteger(request.getParameter("targetId"));
		String comment = request.getParameter("comment");
		// 获取当前登陆用户
		int commenterId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		if (!(comment.length() > 50)) {

			// 获取说说评论的实体类
			TwitterCommentModel twitterCommentModel = new TwitterCommentModel(comment, twitterId, commenterId,
					targetId);
			// 存进数据库
			if (!new TwitterCommentService().addTwitterComment(twitterCommentModel))
				state = 202;
		} else
			state = 203;
		// 打包发送
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
			doGet(request, resp);
	}
}
