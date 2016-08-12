package com.qg.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.util.Logger;

@WebServlet("CreateAlbum")
public class AlbumCreate extends HttpServlet {
	
	private static final Logger LOGGER = Logger.getLogger(AlbumCreate.class);
	private static final int success = 1;
	private static final int fail= 0;
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doPost(request, response);
	}
}
