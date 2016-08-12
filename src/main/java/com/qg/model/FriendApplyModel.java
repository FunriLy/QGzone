package com.qg.model;

public final class FriendApplyModel {
	
	private int friendApplyId;  //好友申请编号
	private int requesterId;  //发送者账号
	private int responserId;  // 接收者账号
	private String applyTime;  //发送申请时间
	private int applyState;  //申请的处理状态
	
	String requesterName; //发送者姓名
	
	public FriendApplyModel(){}
	
	public FriendApplyModel(int requesterId, int responserId){
		this.requesterId = requesterId;
		this.responserId = responserId;
	}
	
	public FriendApplyModel(int friendApplyId, int requesterId, int applyState){
		this.friendApplyId = friendApplyId;
		this.requesterId = requesterId;
		this.applyState = applyState;
	}

	public int getFriendApplyId() {
		return friendApplyId;
	}

	public void setFriendApplyId(int friendApplyId) {
		this.friendApplyId = friendApplyId;
	}

	public int getRequesterId() {
		return requesterId;
	}

	public void setRequesterId(int requesterId) {
		this.requesterId = requesterId;
	}

	public int getResponserId() {
		return responserId;
	}

	public void setResponserId(int responserId) {
		this.responserId = responserId;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public int getApplyState() {
		return applyState;
	}

	public void setApplyState(int applyState) {
		this.applyState = applyState;
	}

	public String getRequesterName() {
		return requesterName;
	}

	public void setRequesterName(String requesterName) {
		this.requesterName = requesterName;
	}
	

	
}
