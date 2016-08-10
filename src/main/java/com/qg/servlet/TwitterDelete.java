package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.qg.service.TwitterService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/TwitterDelete")
public class TwitterDelete extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(TwitterDelete.class);
	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 201;
		try {
			//获取说说id
			int twitterId = Integer.getInteger(request.getParameter("twitterId"));
			
			TwitterService twitterService = new TwitterService();
			// 获取路径
			String path = getServletContext().getRealPath("/WEB-INF/twitter/");
			//获取图片张数
			int picture = twitterService.twitterPicture(twitterId);
			//循环删除服务器上的图片
			while(picture>0){
				if(!twitterService.deleteFile(path+twitterId+"_"+picture+".jpg"))
				{
					state = 202;
					break;
				}
				picture--;
			}
			//删除服务器上的说说信息
			if(!twitterService.deleteTwitter(twitterId))
			{
				state = 202;
			}
		} catch (Exception e) {
			LOGGER.log(Level.ERROR, "删除说说失败", e);
			state =202;
		}finally {
				Gson gson = new Gson();
				JsonUtil<String, String> jsonObject = new JsonUtil(state);
				DataOutputStream output = new DataOutputStream(resp.getOutputStream());
				output.write(gson.toJson(jsonObject).getBytes("UTF-8"));
				output.close();
		}
	}
}