package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoublePredicate;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.AlbumModel;
import com.qg.model.UserModel;
import com.qg.service.AlbumService;
import com.qg.service.FriendService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 获取用户所有的相册列表,用户访问个人相册输入0
 * 状态码: 601-成功; 602-相册为空; 606-非好友关系;
 * </p>
 */

@WebServlet("/Albums")
public class Albums extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(Albums.class);
	private static final int success = 1;
	private static final int fail = 0;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获取用户id
		int userId = 1;
		//int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int state = 602;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		List<AlbumModel> allAlbum = new ArrayList<AlbumModel>();
		AlbumService albumService = new AlbumService();
		FriendService friendService = new FriendService();
		
		if (request.getParameter("userId")==null || request.getParameter("userId")=="") {
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
			LOGGER.log(Level.DEBUG, "空指针");
			return;
		}
		
		
		//获取Json
		int t_userId = Integer.valueOf(request.getParameter("userId"));
		
		if (t_userId == 0 || userId == t_userId) {
			//用户本人浏览相册
			allAlbum = albumService.getAllAlbumByUserId(userId);
			if (allAlbum.isEmpty()) {
				//相册为空
				state = 602;
			} else {
				state = 601;
			}
		} else if (userId != t_userId) {
			//非用户进行浏览
			//判断是否为好友关系
			if (success == friendService.isFriend(userId, t_userId)) {
				allAlbum = albumService.getAllAlbumByUserId(t_userId);
				if (allAlbum.isEmpty()) {
					//相册为空
					state = 602;
				} else {
					state = 601;
				}
			} else {
				//非好友关系
				state = 606;
			}
			
		} else {
			state = 602;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 查看用户 {1} 的相册 状态 {2}", userId, t_userId, state);
		
		output.write(JsonUtil.tojson(state, allAlbum).getBytes("UTF-8"));
		output.close();
	}
	
	@Override 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
