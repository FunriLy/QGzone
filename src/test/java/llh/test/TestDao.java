package llh.test;

import com.qg.dao.impl.NoteCommentDaoImpl;
import com.qg.dao.impl.NoteDaoImpl;
import com.qg.dao.impl.SupportDaoImpl;
import com.qg.dao.impl.TwitterCommentDaoImpl;
import com.qg.dao.impl.TwitterDaoImpl;
import com.qg.model.NoteCommentModel;
import com.qg.model.NoteModel;
import com.qg.model.TwitterCommentModel;
import com.qg.model.TwitterModel;
import com.qg.service.NoteCommentService;
import com.qg.service.NoteService;
import com.qg.service.TwitterCommentService;
import com.qg.service.TwitterService;
import com.qg.util.SimpleConnectionPool;

public class TestDao {

	public static void main(String[] args) throws Exception {
		
		/**
		 * 测试说说dao层
		 */
		System.out.println("Usbbbercb".matches("[Usercvvvcbcvbcvbcvb]+"));
////		TwitterModel twitter=new TwitterModel("锡隆哈哈哈",0,3); 
//		TwitterService twitterService = new TwitterService();
//		TwitterDaoImpl twitterdao = new TwitterDaoImpl();
//		
////		System.out.println(twitterService.geTwitterById(207).getTwitterWord());
////		System.out.println("");
////		System.out.println("".length());
////		测试添加说说
////		int i=50;
////		while(i-->40)
////		{
////			TwitterModel twitter=new TwitterModel("1"+i,0,10000); 
////			System.out.println("未连接"+SimpleConnectionPool.getNotUsedConnectionCount()+"  已连接"+SimpleConnectionPool.getUsedConnectionCount());
////			System.out.println(twitterdao.addTwitter(twitter));
////		}
////		//测试删除说说
////		int i=1063;
////		while(i-->580)
////		System.out.println(twitterdao.deleteTwitter(i));
//		
////		System.out.println(twitterService.deleteTwitter(17, 3));
//		//测试根据某条id获取说说
////		System.out.println(twitterdao.geTwitterById(18));
//		
//		//获取图片张数
////		System.out.println(twitterdao.twitterPicture(1));
//		
//		//获取说说
////		System.out.println(twitterdao.getTwitter(1, 3).size());
//		
////		SupportDaoImpl support = new SupportDaoImpl();
//		
//		//点赞说说
////		System.out.println(twitterService.twitterSupport(4,10000));
//		
//		//取消赞
////		System.out.println(support.deleteSupport(4, 10000));
//		
//		//是否点赞
////		System.out.println(support.findSupport(18, 11));
//		
//		//点赞人集合
////		System.out.println(support.getSupporterByTwitterId(18));
//		
//		TwitterCommentDaoImpl twitterCommentDao = new TwitterCommentDaoImpl();
////		TwitterCommentModel twitterComment=new TwitterCommentModel( "改革",4 ,10000,3);
//		TwitterCommentService twitterCommentService = new TwitterCommentService();
//		
////		System.out.println(twitterService.twitterSupport(19, 3));
//		
//		//添加评论
////		int i=1115;
////		while(i-->1088)
////		{
////			TwitterCommentModel twitterComment=new TwitterCommentModel( "改革",i ,10000,3);
////		System.out.println(twitterCommentDao.addTwitterComment(twitterComment));
////		}
//		//获得评论
////		System.out.println(twitterdao.getTwitter(1, 3).get(1).getComment().get(1).getComment());
////		System.out.println(twitterCommentDao.getTwitterCommentByTwitterId(18).size());
//		
//		//删除某条评论
////		System.out.println(twitterCommentDao.deleteComment(4));
////		System.out.println(twitterCommentService.deleteComment(12, 3));
//		
//		//根据id获取某条评论
////		System.out.println(twitterCommentDao.geTwitterCommentById(6).getTime());
//		
//		/***
//		 * 测试留言板dao层
//		 */
//		NoteDaoImpl noteDaoImpl = new NoteDaoImpl();
//		NoteModel noteModel = new NoteModel("啊sdfsdf啊啊asd啊啊asd啊",3, 10000);
//		NoteService noteService = new NoteService();
//		
//		//测试添加留言
//		System.out.println(noteService.addNote(noteModel));
//		
//		//测试获取留言
////		System.out.println(noteDaoImpl.getNote(1, 3).size());
//		
//		//测试根据留言id获取留言
////		System.out.println(noteDaoImpl.geNoteById(4).getNote());
//		
//		//删除留言
////		System.out.println(noteDaoImpl.deleteNote(3));
////		System.out.println(noteService.deleteNote(5, 10001));
//		
//		
//		NoteCommentDaoImpl noteCommentDaoImpl = new NoteCommentDaoImpl();
//		NoteCommentModel noteComment = new NoteCommentModel("c噢", 3, 3,  10000);
//		NoteCommentService noteCommentService = new NoteCommentService();
//		
//		//添加留言回复
////		noteCommentDaoImpl.saddNoteComment(noteComment);
//		
//		//获得留言回复
////		System.out.println(noteCommentDaoImpl.getNoteCommentByNoteId(3).size());
//		
//		//根据id获得留言回复
////		System.out.println(noteCommentDaoImpl.getNoteCommentById(3).getComment());
//		
//		//删除某条留言回复
////		System.out.println(noteCommentDaoImpl.deleteComment(3));
////		System.out.println(noteCommentService.deleteComment(7, 3));
	}
}