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
	
		int state = 201;//状态码
		int pictureNumber=0;//说说图片张数
		int userId = 0;//当前用户id
		int twitterId = 0;//说说id
		
		/* 判断留言对象是否为null */
		if (request.getParameter("twitterId").equals(null)||request.getParameter("twitterId").equals("")) {
			state = 202;
			LOGGER.log(Level.ERROR, " 说说删除出现空指针");
			
		} else {
			// 获取说说id
			 twitterId = Integer.parseInt(request.getParameter("twitterId"));
			// 获取当前用户Id
			 userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();

			TwitterService twitterService = new TwitterService();
			// 获取路径
			String path = getServletContext().getRealPath("/twitterPhotos/");
			// 获取图片张数
			pictureNumber = twitterService.twitterPicture(twitterId);
			
			try {
				// 循环删除服务器上的图片
				while (pictureNumber > 0) {
					if (!(twitterService.geTwitterById(twitterId).getTalkId() == userId
							&& twitterService.deleteFile(path + twitterId + "_" + pictureNumber + ".jpg"))) {
						state = 202;
						break;
					}
					pictureNumber--;
				}
				// 删除服务器上的说说信息
				if (state != 202) {
					if (!twitterService.deleteTwitter(twitterId, userId)) {
						state = 202;
					}
				}
			} catch (Exception e) {
				LOGGER.log(Level.ERROR, "删除说说失败", e);
				state = 202;
			}
		}
		LOGGER.log(Level.DEBUG, " {0}想删除说说，说说id为{1}，其图片张数为：{2},结果为{3}", userId, twitterId, pictureNumber, state);
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}