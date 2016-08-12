package com.qg.servlet;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.NoteModel;
import com.qg.model.UserModel;
import com.qg.service.NoteService;
import com.qg.util.JsonUtil;
import com.qg.util.Logger;

@WebServlet("/NoteAdd")
/**
 * 
 * @author dragon
 * 
 *  <pre>
 *  添加留言的类
 *  </pre>
 */
public class NoteAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NoteAdd.class);

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 501;
		/* 获取被留言者id,留言内容 */
		int targetId = Integer.getInteger(request.getParameter("targetId"));
		String note = request.getParameter("note");
		// 当前用户=留言者
		int noteManId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		// 获取留言的实体类
		NoteModel noteModel = new NoteModel(note, targetId, noteManId);
		// 存进数据库
		if (! new NoteService().addNote(noteModel))
			state = 502;
		// 打包发送
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state).getBytes("UTF-8"));
		output.close();
	}

	protected void doGEt(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doPost(request, resp);
	}
}