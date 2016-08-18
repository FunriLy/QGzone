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
import com.qg.service.FriendService;
import com.qg.service.TwitterService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/***
 * 
 * @author dragon
 * <pre>
 * 这是一个获取好友的说说的类
 * 201-获取成功 202-获取失败 206-非好友无法访问
 * </pre>
 */
@WebServlet("/TwitterOfOthers")
public class TwitterOfOthers  extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterOfOthers.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 201;
		List<TwitterModel> twitters = null;
		int totalPage = 0;
		//获取当前登陆id
		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
//		int userId = 3;
		//获取被访问者的id
		int targetId = Integer.parseInt(request.getParameter("userId"));
		//获取页码
		String page = request.getParameter("page");
		try {
			// 判断是否为好友
			if (new FriendService().isFriend(userId, targetId) == 1||targetId==userId) {
				// 获取说说列表
				twitters = new TwitterService().getTwitterByTalkId(Integer.parseInt(page), targetId);
				//获取总页数
				totalPage = new TwitterService().twitterPage(userId, new TwitterService().userTwitterNumber(userId));
			} else
				state = 206;
		} catch (Exception e) {
			state = 202;
			LOGGER.log(Level.ERROR, "获取说说异常", e);
		} finally {
			DataOutputStream output = new DataOutputStream(resp.getOutputStream());
			output.write(JsonUtil.tojson(state, twitters,totalPage).getBytes("UTF-8"));
			output.close();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}
