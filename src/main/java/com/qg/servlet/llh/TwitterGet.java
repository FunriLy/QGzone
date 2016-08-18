package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
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
 * 201-成功 202-失败
 * </pre>
 */
@WebServlet("/TwitterGetTest")
public class TwitterGet  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterGet.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 201;
		int totalPage = 0;
		List<TwitterModel> twitters = null;
		//获取当前登陆id
		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
//		int userId = 3;
		//获取页码
				String page = request.getParameter("page");
				try {
					//获取说说列表
					twitters = new TwitterService().getTwitter(Integer.parseInt(page), userId);
					//获取总页数
					totalPage = new TwitterService().twitterPage(userId, new TwitterService().twitterNumber(userId));
				} catch (Exception e) {
					state = 202;
					LOGGER.log(Level.ERROR, "获取说说异常", e);
				} finally {
					DataOutputStream output = new DataOutputStream(resp.getOutputStream());
					output.write(JsonUtil.tojson(state,twitters,totalPage).getBytes("UTF-8"));
					output.close();
				}
	}
}
