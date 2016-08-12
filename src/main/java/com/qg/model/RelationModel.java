package com.qg.model;

import java.sql.Timestamp;

import com.qg.dao.MessageDao;
import com.qg.dao.impl.MessageDaoImpl;

public class RelationModel {

	private int relationId;//与我相关id
	private String relationType;//类型
	private String relationContent;//发布内容
	private Timestamp relationTime;//发布时间
	private int receiverId;//接收者账号
	private int relationHasRead;//是否已读
	private MessageModel sender;//发送者对象
	private int relatedId;//相关信息id
	
	public RelationModel(){}
	public RelationModel(String relationType,String relationContent
			,int receiverId,int senderId,int relationHasRead,int relatedId){
		MessageDao dao = new MessageDaoImpl();
		this.relationType = relationType;
		this.relationContent = relationContent;
		this.receiverId = receiverId;
		this.relationHasRead = relationHasRead;
		this.relatedId = relatedId;
		this.sender = dao.getMessageById(senderId);
	}
	
	public int getRelationId() {
		return relationId;
	}
	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}
	public String getRelationType() {
		return relationType;
	}
	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}
	public String getRelationContent() {
		return relationContent;
	}
	public void setRelationContent(String relationContent) {
		this.relationContent = relationContent;
	}
	public Timestamp getRelationTime() {
		return relationTime;
	}
	public void setRelationTime(Timestamp relationTime) {
		this.relationTime = relationTime;
	}
	public int getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}
	public int getRelationHasRead() {
		return relationHasRead;
	}
	public void setRelationHasRead(int relationHasRead) {
		this.relationHasRead = relationHasRead;
	}
	public MessageModel getSender() {
		return sender;
	}
	public void setSender(MessageModel sender) {
		this.sender = sender;
	}
	public int getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(int relatedId) {
		this.relatedId = relatedId;
	}
	@Override
	public String toString() {
		return "Relation [relationId=" + relationId + ", relationType=" + relationType + ", relationContent="
				+ relationContent + ", relationTime=" + relationTime + ", receiverId=" + receiverId
				+ ", relationHasRead=" + relationHasRead + ", sender=" + sender + ", relatedId=" + relatedId + "]";
	}
	
}
