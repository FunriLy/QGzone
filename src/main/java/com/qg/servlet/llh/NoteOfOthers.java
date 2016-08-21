package com.qg.servlet.llh;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qg.model.NoteModel;
import com.qg.model.UserModel;
import com.qg.service.FriendService;
import com.qg.service.NoteService;
import com.qg.util.JsonUtil;
import com.qg.util.Level;
import com.qg.util.Logger;

@WebServlet("/NoteOfOthers")
/***
 * 
 * @author dragon
 * <pre>
 * 查看好友留言板
 * 501获取成功，502获取失败，506非好友无法访问
 * </pre>
 */
public class NoteOfOthers extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(NoteOfOthers.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {

		int state = 501;//状态码
		List<NoteModel> notes = null;//留言集合
		int totalPage = 0;//总页数
		int userId = 0;//当前登陆Id
		int targetId = 0;//留言板主人id

		/* 判断被访问者的id是否为null */
		if (request.getParameter("userId").equals(null)||request.getParameter("page").equals(null)) {
			state = 502;
			LOGGER.log(Level.ERROR, " 访问留言板出现空指针");
		} else {

			// 获取当前用户id
			userId = ((UserModel) request.getSession().getAttribute("user")).getUserId();
			// 获取被访问者的id
			targetId = Integer.parseInt(request.getParameter("userId"));
			// 获取页码
			String page = request.getParameter("page");

			try {
				/* 判断是否为好友以及是否是自己留言 */
				if (new FriendService().isFriend(userId, targetId) == 1 || targetId == userId) {

					// 获取全部留言
					notes = new NoteService().getNote(Integer.parseInt(page), targetId);
					// 获取总页数
					totalPage = new NoteService().notePage(userId, new NoteService().noteNumber(userId));

				} else
					state = 506;

			} catch (Exception e) {
				state = 502;
				LOGGER.log(Level.ERROR, "获取留言异常", e);
			}
		}
		
		LOGGER.log(Level.DEBUG, " {0}进入{1}的留言板,页码数为{3},状态{2}", userId, targetId, state,totalPage);
		DataOutputStream output = new DataOutputStream(resp.getOutputStream());
		output.write(JsonUtil.tojson(state, notes, totalPage).getBytes("UTF-8"));
		output.close();
		
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		doGet(request, resp);
	}
}