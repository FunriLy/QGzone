package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.dao.PhotoDao;
import com.qg.dao.impl.PhotoDaoImpl;
import com.qg.model.AlbumModel;
import com.qg.model.UserModel;
import com.qg.service.AlbumService;
import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户查看公开相册中的图片
 * 状态码: 601-成功; 602-没有相片; 603-相册不存在; 604-相册权限不符;
 * </p>
 */

@WebServlet("CheckPublicAlbum")
public class AlbumCheckPublic extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumCheckPublic.class);
	private static final int success = 1;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		//获得用户id
		int userId = ((UserModel)request.getSession().getAttribute("user")).getUserId();
		//获得相册id
		int albumId = Integer.valueOf(request.getParameter("albumId"));
		//初始化状态码等数据
		int state = 602;
		AlbumService albumService = new AlbumService();
		List<Integer> allPhotoId = new ArrayList<Integer>();
		DataOutputStream output = new DataOutputStream(response.getOutputStream());
		
		
		
		int result = albumService.albumIsExist(albumId);
		if (success == result) {
			AlbumModel albumModel = albumService.getAlbumByAlbumId(albumId);
			//权限不符
			if(success == albumModel.getAlbumState()){
				state = 604;
			} else if(0 == albumModel.getPhotoCount()){
				//数量为0
				state = 602;
			} else {
				PhotoDao photoDao = new PhotoDaoImpl();
				allPhotoId = photoDao.getAllPhotoByAlbumId(albumId);
				state = 601;
			}
		} else {
			state = 603;
		}
		
		
		
		
		
		
		
		
		
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
