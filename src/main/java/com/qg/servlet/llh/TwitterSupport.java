package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.service.TwitterService;
import com.qg.util.JsonUtil;
import com.qg.model.UserModel;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/TwitterSupport")
/***
 * 
 * @author dragon
 * <pre>
 * 点赞功能
 * 201-点赞成功 202-点赞失败
 * </pre>
 */
public class TwitterSupport extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterSupport.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 201;// 状态码
		int userId = 0;// 登陆id
		int twitterId = 0;// 说说id

		/* 判断点赞者id是否为null */
		if (request.getParameter("twitterId").equals(null)||request.getParameter("twitterId").equals("")) {
			state = 202;
			LOGGER.log(Level.ERROR, " 点赞出现空指针");
		} else {

			// 获取说说id和当前用户id
			twitterId = Integer.parseInt(request.getParameter("twitterId"));
			userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();

			state = new TwitterService().twitterSupport(twitterId, userId) ? 201 : 202;
		}
		LOGGER.log(Level.DEBUG, "{0}想给{1}说说点赞，状态码为{2}", userId, twitterId, state);
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}
