package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.TwitterModel;
import com.qg.model.UserModel;
import com.qg.service.TwitterService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/***
 * 
 * @author dragon
 * <pre>
 * 这是一个获取说说的类
 * </pre>
 */
@WebServlet("/TwitterGet")
public class TwitterGet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterGet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 201;
		List<TwitterModel> twitters = null;
		//获取当前登陆id
		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		//获取页码
		String page = request.getParameter("page");
		try {
			//存进数据库
			twitters = new TwitterService().getTwitter(Integer.parseInt(page), userId);
		} catch (Exception e) {
			state = 202;
			LOGGER.log(Level.ERROR, "获取说说异常", e);
		} finally {
			DataOutputStream output = new DataOutputStream(resp.getOutputStream());
			output.write(JsonUtil.tojson(state,twitters).getBytes("UTF-8"));
			output.close();
		}
	}
}
