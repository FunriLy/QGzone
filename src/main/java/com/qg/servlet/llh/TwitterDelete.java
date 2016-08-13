package com.qg.servlet.llh;

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
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/TwitterDelete")
/***
 * 
 * @author dragon
 * <pre>
 * 删除说说
 * 201-成功 202-失败
 * </pre>
 */
public class TwitterDelete extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterDelete.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 201;
		// 获取说说id
		int twitterId = Integer.getInteger(request.getParameter("twitterId"));
		//获取当前用户Id
		int userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		
		TwitterService twitterService = new TwitterService();
		// 获取路径
		String path = getServletContext().getRealPath("/WEB-INF/twitter/");
		// 获取图片张数
		int picture = twitterService.twitterPicture(twitterId);
		try {
			// 循环删除服务器上的图片
			while (picture > 0) {
				if (!twitterService.deleteFile(path + twitterId + "_" + picture + ".jpg")) {
					state = 202;
					break;
				}
				picture--;
			}
			// 删除服务器上的说说信息
			if (!twitterService.deleteTwitter(twitterId,userId)) {
				state = 202;
			}
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "删除说说失败", e);
			state = 202;
		} finally {
			DataOutputStream output = new DataOutputStream(resp.getOutputStream());
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
		}
	}
}