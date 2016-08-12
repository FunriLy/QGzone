package com.qg.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.util.Logger;

/**
 * 
 * @author zggdczfr
 * <p>
 * 用户查看私密相册
 * 状态码: 601-成功; 602-密码错误; 603-相册不存在; 604-相片为0;
 * </p>
 */

@WebServlet("CheckPrivacyAlbum")
public class AlbumCheckPrivacy extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(AlbumCheckPrivacy.class);
	private static final int success = 1;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
