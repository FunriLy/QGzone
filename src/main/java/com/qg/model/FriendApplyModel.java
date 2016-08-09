package com.qg.model;

public final class FriendApplyModel {
	
	private int friendId;
	private int requesterId;
	private int responserId;
	private String applyTime;
	private int applyState;
	
	public FriendApplyModel(){}
	
	public FriendApplyModel(int requesterId, int responserId){
		this.requesterId = requesterId;
		this.responserId = responserId;
	}
	
	public FriendApplyModel(int requesterId, int responserId,int applyState){
		this.requesterId = requesterId;
		this.responserId = responserId;
		this.applyState = applyState;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
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
	
	
}
