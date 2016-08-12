package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.UserModel;
import com.qg.service.TwitterService;
import com.qg.util.JsonUtil;
import com.qg.util.Logger;

@WebServlet("/TwitterSupport")
public class TwitterSupport extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterSupport.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		// 获取说说id和当前用户id
		int twitterId = Integer.getInteger(request.getParameter("twitterId"));
		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();

		int state = new TwitterService().twitterSupport(twitterId, userId) ? 201 : 202;

		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}
}
