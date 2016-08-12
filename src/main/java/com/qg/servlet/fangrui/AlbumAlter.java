package com.qg.servlet.fangrui;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
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
 * 用户更改相册权限/更改相册密码
 * 状态码: 601-修改成功; 602-修改失败; 603-相册不存在; 604-没有权限;
 * </p>
 */

@WebServlet("/AlterAlbumInformation")
public class AlbumAlter extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumAlter.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获取用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		//获取 Json 并解析
		String reciveObject = request.getParameter("jsonObject");
		AlbumService albumService = new AlbumService();
		Gson gson = new Gson();
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		AlbumModel album = gson.fromJson(reciveObject, AlbumModel.class);
		//初始化数据
		int state = 602; 
		//相册不存在
		if(success != albumService.albumIsExist(album.getAlbumId())){
			state = 603;
			if (success == albumService.uplateAlbum(album)) {
				state = 601;
			}
		} else if (userId != albumService.getAlbumByAlbumId(album.getAlbumId()).getUserId()) {
			//没有权限修改
			state = 604;
		} else {
			state = 602;
			if (success == albumService.uplateAlbum(album)) {
				state = 601;
			}
		}
		
		LOGGER.log(Level.DEBUG, "用户 {0} 修改相册 {1} 重要信息，相册权限 {2} 相册密码 {3} 状态 {4}", 
				userId, album.getAlbumId(), album.getAlbumState(), album.getAlbumPassword());
		
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
