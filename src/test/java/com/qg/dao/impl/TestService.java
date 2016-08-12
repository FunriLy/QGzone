package com.qg.dao.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.qg.model.FriendApplyModel;
import com.qg.service.FriendService;


/**
 * Created by tisong on 8/9/16.
 */
public class TestService {
	public static void main(String[] args) {
		FriendService friendService = new FriendService();
		List<FriendApplyModel> allApply = friendService.getAllFriendApply(1);
		for(FriendApplyModel model : allApply){
			System.out.println(model.getRequesterName());
		}
	}
}
