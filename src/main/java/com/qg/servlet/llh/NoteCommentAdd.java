package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.NoteCommentModel;
import com.qg.service.NoteCommentService;
import com.qg.service.NoteService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/NoteCommentAdd")
/***
 * 
 * @author dragon
 * 
 * <pre>
 *  添加留言评论
 *  501留言成功 502留言失败 503留言过长
 *  </pre>
 */
public class NoteCommentAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NoteCommentAdd.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		int state = 501;
		int noteCommentId=0;
		/* 获取留言id，被回复方id,回复内容 */
		int noteId = Integer.parseInt(request.getParameter("noteId"));
		int targetId = Integer.parseInt(request.getParameter("targetId"));
		String comment = request.getParameter("comment");
		// 获取当前登陆用户
//		int commenterId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
		int commenterId = 10000;
		LOGGER.log(Level.DEBUG, " {0}想评论{1}的留言，留言id为{2}，内容为：{3}", commenterId,targetId,noteId,comment);
		if (!(comment.length() > 50)) {

			// 获取留言评论的实体类
			NoteCommentModel noterCommentModel = new NoteCommentModel(comment, noteId, commenterId, targetId);
			// 存进数据库
			if (!new NoteService().existNote(noteId))
				state = 502;
			else 
				noteCommentId=new NoteCommentService().addNoteComment(noterCommentModel);
		} else
			state = 503;
		// 打包发送
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state,noteCommentId).getBytes("UTF-8"));
		output.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}