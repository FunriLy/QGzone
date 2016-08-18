package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.AlbumModel;
import com.qg.model.UserModel;
import com.qg.service.AlbumService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户清空相册
 * 状态码: 601-删除成功; 602-失败; 607-没有权限; 608-相册不存在;
 * </p>
 */

@WebServlet("/EmptyAlbum")
public class AlbumEmpty extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumEmpty.class);
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		int state = 602;
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		if (request.getParameter("albumId")== null || request.getParameter("albumId")=="") {
			output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
			output.close();
			LOGGER.log(Level.DEBUG, "空指针！");
			return;
		}
		//获得Json并解析
		int albumId = Integer.valueOf(request.getParameter("albumId"));
		String path = getServletContext().getRealPath("/album") + "/" + userId + "/" + albumId;
		
		AlbumService albumService = new AlbumService();
		AlbumModel realAlbum = albumService.getAlbumByAlbumId(albumId);
		if (userId == realAlbum.getUserId()) {
			state = albumService.emptyAlbum(path, albumId);
		} else if (realAlbum.getAlbumId() == 0) {
			state = 608;
		} else {
			state = 607;
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 清空相册 {1} 状态: {2}", userId, albumId, state);
		
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
