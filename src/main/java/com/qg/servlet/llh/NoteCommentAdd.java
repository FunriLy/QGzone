package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.qg.model.UserModel;
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

		int state = 501;//状态码
		NoteCommentModel noterCommentModel = null;//留言评论对象
		int commenterId = 0;//留言评论者id
		int targetId = 0;//留言评论对象id
		int noteId = 0;//留言id
		String comment = null;//留言评论内容

		/* 判断是否评论对象和评论以及留言id是否为null */
		if (request.getParameter("targetId").equals("") || request.getParameter("targetId").equals(null)
				|| request.getParameter("noteId").equals(null) || request.getParameter("noteId").equals("")
				|| request.getParameter("comment").equals(null) || request.getParameter("comment").equals("")) {
			state = 502;
			LOGGER.log(Level.ERROR, " 留言评论失败，未获取到留言评论信息");
		} else {
			/* 获取留言id，被回复方id,回复内容 */
			noteId = Integer.parseInt(request.getParameter("noteId"));
			targetId = Integer.parseInt(request.getParameter("targetId"));
			comment = request.getParameter("comment");

			// 获取当前登陆用户
			commenterId = ((UserModel) request.getSession().getAttribute("user")).getUserId();

			/* 判断字数是否超限 */
			if (!(comment.length() > 50)) {

				// 获取留言评论的实体类
				noterCommentModel = new NoteCommentModel(comment, noteId, commenterId, targetId);

				// 存进数据库
				if (!new NoteService().existNote(noteId))
					state = 502;// 评论失败
				else
					noterCommentModel = new NoteCommentService().addNoteComment(noterCommentModel);

			} else
				state = 503;// 评论过长
		}
		LOGGER.log(Level.DEBUG, " {0}想评论{1}的留言，留言id为{2}，内容为：{3} 状态为{4}", commenterId, targetId, noteId, comment, state);

		// 打包发送
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state, noterCommentModel, noterCommentModel.getCommentId()).getBytes("UTF-8"));
		output.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}