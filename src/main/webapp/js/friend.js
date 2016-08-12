$(function(){
		var HEAD_IMG_URL = "http://localhost:8080/QGzone/jpg/";
		var IP = "localhost";
		$("#find_account").hide();
//side_bar-------------------------------------------------------
	$("#side_bar ul").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase() != "LI"){
			return ;
		}
		e.addClass("color_white");
		e.siblings().removeClass("color_white");
		$("#"+e.attr("index")).show();
		$("#"+e.attr("index")).siblings().hide();
		if(e.attr("index")=="friend_list"){
			$("#friend_list #friends").show();
			$("#friend_list #find_my_friend_result li").remove("[class=friend]");
			$("#find_my_friend input").val("");
		}
	});




//好友列表生成函数-------------------------------------------------------------
	function friendListCreateFriend(userId , img_url , name ){
		var $li = $('<li class="friend" userid='+ userId +'><img class="friend_head_img" src='+img_url+'></img><span class="friend_info">'+name+'</span><span class="delete">删除</span></li>');
		var $ul = $("#friend_list #friends");
		$li.appendTo($ul);
	};
	//friendListCreateFriend(1, "../images/head.jpg","xiaoming");
//查看好友列表-------------------------------------------------------------
	$.post("http://"+IP+":8080/QGzone/MyFriends",function(data){
		console.log(data);
		$("find_my_friend_result .not_find").hide();
		var state = data.state;
		var list = data.jsonList;
		if(state == undefined){
			alert("服务器异常!");
		}
		if(state == "301" ){
			for(var i=0; i<list.length; i++){
				var userId = list[i].userId;
				var img_url = HEAD_IMG_URL + list[i].userId +"jpg";
				var name = list[i].userName;
				friendListCreateFriend(userId, img_url,name);
			}
		}
		if(state == "302"){
			$("find_my_friend_result .not_find").show();
		}
	},"json");
//删除好友------------------------------------------------------------------
	$("#friends").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase()!="SPAN"){
			return ;
		}
		if(e.attr("class")=="delete"){
			var userId = e.parent().attr("userid");
			var info={
					friendId : userId
			};
			$.post("http://"+IP+":8080/QGzone/DeleteFriend",info,function(data){
				console.log(data);
				var state = data.state; 
				if(state == undefined){
					alert("服务器异常!");
				}     
				if(state == "301"){
					console.log("success");
					$("#friends li[userid="+userId+"]").remove();
				}
				if(state == "302"){
					alert("操作失败!");
				}
				if(state == "303"){
					alert("不存在好友关系!");
				}
			},"json");
		}
	})

//在好友列表中搜索好友------------------------------------------------------
	$("#find_my_friend #glass").click(function(){
		$("#friend_list #find_my_friend_result li").remove("[class=friend]");
		$("#friend_list .not_find").hide();
		var contents = $("#find_my_friend input").val().trim();
		if(contents==""){
			$("#friend_list #friends").show();
			return ;
		}
		$("#friend_list #friends").hide();
		var lis = $($("#friend_list #friends li span:contains("+contents+")")).parent();
		var ul = $("#friend_list #find_my_friend_result")[0];
		if(lis.length==0){
			$("#friend_list .not_find").show();
		}else{
			$("#friend_list .not_find").hide();
		}
		for(var i=0; i<lis.length ; i++){
			$(lis[i]).clone(true).appendTo(ul);
		}
	});






//添加列表生成函数------------------------------------------------------------
	function addFriendCreateFriend(userId, img_url , name){
		var $li = $('<li class="friend" userid='+ userId +'><img class="friend_head_img" src='+img_url+'></img><span class="friend_info">'+name+'</span><span class="add"></span></li>');
		var $ul = $("#add_friend #find_result_list");
		$li.appendTo($ul);
	}
	//addFriendCreateFriend( 2, "../images/head.jpg","xiaohong");
//查找用户选项---------------------------------------------------------------
	$("#option_button").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase() != "SPAN"){
			return ;
		}	
		e.addClass('haveChouce');
		e.siblings().removeClass('haveChouce');
		$("#add_friend #find input").val("");
		$("#find_result_list .not_find").hide();
		if(e.attr("id") == "of_name"){
			$("#find_name").show();
			$("#find_account").hide();
		}
		if(e.attr("id") == "of_account"){
			$("#find_name").hide();
			$("#find_account").show();
		}
	});
//按昵称---------------------------
	$("#find_name").click(function(){
		var name = $("#add_friend #find input").val().trim();
		$("#find_result_list li").remove("[class=friend]");
		if(name==""){
			$("#find_result_list .not_find").show();
			return ;
		}
		var info = {
			searchName : name
		}
		$.post("http://"+IP+":8080/QGzone/SearchByUserName" , info ,function(data){
			console.log(data);
			var state = data.state;
			var list = data.jsonList;
			if(state == undefined){
				alert("服务器异常!");
			}
			if(state=="302"){
				$("#find_result_list .not_find").show();
			}
			if(state=="301"){
				$("#find_result_list .not_find").hide();
				for(var i=0 ;i<list.length; i++){
					var userId = list[i].userId;
					var img_url = HEAD_IMG_URL + list[i].userId +".jpg";
					var name = list[i].userName;
					addFriendCreateFriend(userId ,img_url, name);
				}
			}
		},"json");
	});
//按账号----------------------------
	$("#find_account").click(function(){
		var account = $("#add_friend #find input").val().trim();
		$("#find_result_list li").remove("[class=friend]");
		if(account==""){
			$("#find_result_list .not_find").show();
			return ;
		}
		var info = {
			searchId : account
		}
		$.post("http://"+IP+":8080/QGzone/SearchByUserId" , info ,function(data){
			console.log(data);
			var state = data.state;
			var list = data.jsonList;
			if(state == undefined){
				alert("服务器异常!");
			} 
			if(state=="302"){
				$("#find_result_list .not_find").show();
			}
			if(state=="301"){
				$("#find_result_list .not_find").hide();
				for(var i=0 ;i<list.length; i++){
					var userId = list[i].userId;
					var img_url = HEAD_IMG_URL + list[i].userId +"jpg";
					var name = list[i].userName;
					addFriendCreateFriend(userId, img_url,name);
				}
			}
		},"json");
	});

//发送好友申请-------------------------------------------------------------------
	$("#find_result_list").click(function(e){
		var e = $(e.target);
		if(e.attr("class")=="add"){
			var userId = e.parent().attr("userid");
			var info = {
				addFriendId : userId
			};
			$.post("http://"+IP+":8080/QGzone/SendFriendApply",info ,function(data){
				console.log(data);
				var state = data.state;
				//301-成功; 302-失败(已经是好友关系); 303-该用户不存在
				if(state == undefined){
					alert("服务器异常!");
				} 
				if(state=="301"){
					alert("已成功发送请求");
				}
				if(state=="302"){
					alert("对方已是您的好友!");
				}
				if(state=="303"){
					alert("该用户已不存在!");
				}
			},"json");
		}
	});





//请求列表生成函数------------------------------------------------------------------------
	function addRequestCreateRequest(friendApplyId, img_url , name){
		var $li = $('<li class="friend" friendApplyId='+friendApplyId+'>'
					+'<img class="friend_head_img" src='+img_url+'></img>'
					+'<span class="friend_info">'+name+'：请求加您为好友</span>'
					+'<span class="agree">同意</span>'
					+'<span class="delete">删除</span></li>');
		var $ul = $("#request_list");
		$li.appendTo($ul);
	};
	//addRequestCreateRequest( 2, "../images/head.jpg","haha");
//查看用户的好友申请-------------------------------------------------------------------
	$.post("http://"+IP+":8080/QGzone/MyFriendApply",function(data){
		var state = data.state;
		var list = data.jsonList;      //FriendApplyModel.java
		if(state == undefined){
			alert("服务器异常!");
		} 
		if(state == "301"){
			$("#find_result_list .not_find").hide();
			for(var i=0; i<list.length; i++){
				var friendApplyId = list[i].friendApplyId;
				var img_url = HEAD_IMG_URL + list[i].requesterId +"jpg";
				var requesterName = list[i].requesterName;
				addRequestCreateRequest(friendApplyId, img_url, requesterName);
			}
		}
		if(state == "302"){
			$("#find_result_list .not_find").show();
		}
	},"json");


//审核好友申请----------------------------------------------------------------------
	$("#request_list").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase()!="SPAN"){
			return ;
		}
//删除好友申请-----------------------------
		if(e.attr("class")=="delete"){
			var friendApplyId = e.parent().attr("friendApplyId");
			var info={
				friendApplyId : friendApplyId
			}
			$.post("http://"+IP+":8080/QGzone/DeleteFriendApply",function(data){
				console.log(data);
				var state = data.state; 
				if(state == undefined){
					alert("服务器异常!");
				}     
				if(state == "301"){
					console.log("success");
					$("#request_list li[friendApplyId="+friendApplyId+"]").remove();
				}
				if(state == "302"){
					alert("操作失败!");
				}
			},"json");
		}
//同意好友申请---------------------------
		if(e.attr("class")=="agree"){
			var friendApplyId = e.parent().attr("friendApplyId");
			var info={
				friendApplyId : friendApplyId
			}
			$.post("http://"+IP+":8080/QGzone/ConductFriendApply",function(data){
				console.log(data);
				var state = data.state; 
				if(state == undefined){
					alert("服务器异常!");
				}     
				if(state == "301"){
					console.log("success");
					e.text("已同意");
				}
				if(state == "302"){
					alert("操作失败!");
				}
				if(state == "303"){
					alert("请求已处理!");
				}
				if(state == "304"){
					alert("申请已不存在!");
				}
			},"json");
		}
	});
})