
$(function(){
		var HEAD_IMG_URL = "../jpg/";
		var MAIN_PAGE = "selfIndex.html";//不含参数；
		
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
			$("#friend_list #friends li").remove("[class=friend]");
			getMyFriends();
			$("#friend_list #find_my_friend_result").hide();
			$("#friend_list #find_my_friend_result li").remove("[class=friend]");
			$("#find_my_friend input").val("");
		}
		if(e.attr("index")=="add_friend"){
			$("#add_friend #find_result_list li").remove("[class=friend]");
			$("#add_friend #find input").val("");
		}
		if(e.attr("index")=="add_request"){
			$("#request_list li").remove("[class=friend]");
			getMyRequestList();
		}
	});




//好友列表生成函数-------------------------------------------------------------
	function friendListCreateFriend(userId , img_url , name ){
		var $li = $('<li class="friend" userid='+userId+'>'+
					'<img class="friend_head_img" src='+img_url+'>'+
					'<div class="friend_info">'+
						'<p class="name">'+name+'</p>'+
						'<p class="accound">'+userId+'</p>'+
					'</div>'+
					'<span class="delete"></span>'+
				'</li>');
		var $ul = $("#friend_list #friends");
		$li.appendTo($ul);
	};
	//friendListCreateFriend(1, "../images/head.jpg","xiaoming");
//查看好友列表-------------------------------------------------------------
	function getMyFriends(){
		$.post("../MyFriends",function(data){
			console.log(data);
			$("#friends .not_find").hide();
			var state = data.state;
			var list = data.jsonList;
			if(state == undefined){
				alert("服务器异常!");
			}
			if(state == "301" ){
				if(list.length==0){
					$("#friends .not_find").show();
					return;
				}
				for(var i=0; i<list.length; i++){
					var userId = list[i].userId;
					var img_url = HEAD_IMG_URL + list[i].userImage;
					var name = list[i].userName;
					friendListCreateFriend(userId, img_url,name);
				}
			}
			if(state == "302"){
				$("#friends .not_find").show();
			}
		},"json");
	};
	getMyFriends();
//删除好友------------------------------------------------------------------
//跳转到好友空间
	$("#friends, #find_my_friend_result").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase()=="IMG"){
			var userId = e.parent().attr("userId");
			var url = MAIN_PAGE;
			url += "?userId="+userId;
			window.location.href=url;  
		}
		if(e[0].nodeName.toUpperCase()=="SPAN"){
			if(e.attr("class")=="delete"){
				var userId = e.parent().attr("userid");
				var info={
					friendId : userId
				};
				$.post("../DeleteFriend",info,function(data){
					console.log(data);
					var state = data.state; 
					if(state == undefined){
						alert("服务器异常!");
					}     
					if(state == "301"){
						console.log("success");
						$("#friends li[userid="+userId+"]").remove();
						$(" #find_my_friend_result li[userid="+userId+"]").remove();
						if($("#find_my_friend_result li[class=friend]").length<=0){
							$("#find_my_friend_result .not_find").show();
						}
						if($("#friends li[class=friend]").length<=0){
							$("#friends .not_find").show();
						}
					}
					if(state == "302"){
						alert("操作失败!");
					}
					if(state == "303"){
						alert("不存在好友关系!");
					}
				},"json");
			}
		}
	})

//在好友列表中搜索好友------------------------------------------------------
	$("#find_my_friend #glass").click(function(){
		$("#friend_list .not_find").hide();
		var contents = $("#find_my_friend input").val().trim();
		if($("#friend_list #friends li").length<=1){
			$("#friends .not_find").show();
		}
		if(contents==""){
			$("#friend_list #friends").show();
			$("#find_my_friend_result").hide();
			return ;
		}
		$("#friend_list #friends").hide();
		$("#find_my_friend_result").show();
		$("#find_my_friend_result li").remove("[class=friend]");
		var lis = $($("#friend_list #friends li p:contains("+contents+")")).parents('li');
		var ul = $("#friend_list #find_my_friend_result")[0];
		if(lis.length==0){
			$("#find_my_friend_result .not_find").show();
		}else{
			$("#find_my_friend_result .not_find").hide();
		}
		for(var i=0; i<lis.length ; i++){
			$(lis[i]).clone(true).appendTo(ul);
		}
	});






//添加列表生成函数------------------------------------------------------------
	function addFriendCreateFriend(userId, img_url , name){
		var $li = $('<li class="friend" userid='+userId+'>'+
					'<img class="friend_head_img" src='+img_url+'>'+
					'<div class="friend_info">'+
						'<p class="name">'+name+'</p>'+
						'<p class="accound">'+userId+'</p>'+
					'</div>'+
					'<span class="add"></span>'+
				'</li>');
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
		$.post("../SearchByUserName" , info ,function(data){
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
					var img_url = HEAD_IMG_URL + list[i].userImage;
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
		$.post("../SearchByUserId" , info ,function(data){
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
					var img_url = HEAD_IMG_URL + list[i].userImage;
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
			$.post("../SendFriendApply",info ,function(data){
				console.log(data);
				var state = data.state;
				//301-成功; 302-失败; 303-该用户不存在; 304-用户给自己发申请; 305-已经存在好友关系;
				if(state == undefined){
					alert("服务器异常!");
				} 
				if(state=="301"){
					alert("已成功发送请求");
				}
				if(state=="302"){
					alert("请求操作失败,请稍后重试!");
				}
				if(state=="303"){
					alert("该用户已不存在!");
				}
				if(state=="304"){
					alert("操作失败,无法加自己为好友!");
				}
				if(state=="305"){
					alert("对方已是您的好友!");
				}
			},"json");
		}
	});
//对搜索放大镜进行操作--------------------------------------------------------------
	$("#glass ,#find_accound, #find_name").mousedown(function(){
		$(this).css("background-image","url(../images/c_find2.png)");
	}).mouseup(function(){
		$(this).css("background-image","url(../images/c_find1.png)");
	});
	
	 $("body").keydown(function(event) {
			var div = $($("#wrap").children("div:visible"));
			var find = undefined;
			if(div.attr("id")=="friend_list"){
				find=$("#glass");
			}
			if(div.attr("id")=="add_friend"){
				find= $("#find_account").css("display")=="none" ? $("#find_name") : $("#find_account");
			}
		   if (event.keyCode == "13") {//keyCode=13是回车键
		         find.click();
			}
	 });


//请求列表生成函数------------------------------------------------------------------------
	function addRequestCreateRequest(friendApplyId, img_url ,name, applyState,userId){
		var isAgree = "";
		if(applyState==1){
			isAgree =" haveAgree";
		}
		var $li = $('<li class="friend" friendApplyId='+friendApplyId+'>'
				+'<img class="friend_head_img" src='+img_url+'></img>'
				+'<div class="friend_info">'
					+'<p class="name">'+name+'：请求加您为好友</p>'
					+'<p class="accound">'+userId+'</p>'
				+'</div>'
				+'<span class="x_delete"></span>'
				+'<span class="agree'+isAgree+'"></span></li>');
		var $ul = $("#request_list");
		$li.appendTo($ul);
		$('#add_request img').bind("error",function() {
			$(this).attr("src", "../jpg/all.jpg");
		});
	};
//查看用户的好友申请-------------------------------------------------------------------
	function getMyRequestList(){
		$.post("../MyFriendApply",function(data){
			var state = data.state;
			var list = data.jsonList;      //FriendApplyModel.java
			if(state == undefined){
				alert("服务器异常!");
			} 
			if(state == "301"){
				$("#request_list .not_find").hide();
				if(list.length==0){
					$("#request_list .not_find").show();
				}else{
					for(var i=0; i<list.length; i++){
						var friendApplyId = list[i].friendApplyId;
						var img_url = HEAD_IMG_URL + list[i].requesterId +".jpg";
						var requesterName = list[i].requesterName;
						var applyState = list[i].applyState;
						var userId = list[i].requesterId;
						addRequestCreateRequest(friendApplyId, img_url, requesterName, applyState, userId);
					}
				}
				
			}
			if(state == "302"){
				$("#request_list .not_find").show();
			}
		},"json");
	}

//审核好友申请----------------------------------------------------------------------
	$("#request_list").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase()!="SPAN"){
			return ;
		}
//删除好友申请-----------------------------
		if(e.attr("class")=="x_delete"){
			var friendApplyId = e.parent().attr("friendApplyId");
			var info={
				friendApplyId : friendApplyId
			}
			$.post("../DeleteFriendApply",info,function(data){
				console.log(data);
				var state = data.state; 
				if(state == undefined){
					alert("服务器异常!");
				}     
				if(state == "301"){
					console.log("success");
					$("#request_list li[friendApplyId="+friendApplyId+"]").remove();
					if($("#request_list li[class=friend]").length<=0){
						$("#request_list .not_find").show();
					}
				}
				if(state == "302"){
					alert("操作失败!");
				}
			},"json");
			return ;
		}
//同意好友申请---------------------------
		if(e.attr("class")=="agree"||e.attr("class")=="agree haveAgree"){
			if(e.attr("class")=="agree haveAgree"){
				return ;
			}
			if(e.parent().attr("applyState")==1){
				//alert("已同意对方请求!");
				e.addClass("haveAgree");
				return ;
			}
			var friendApplyId = e.parent().attr("friendApplyId");
			var info={
				friendApplyId : friendApplyId
			}
			$.post("../ConductFriendApply",info,function(data){
				console.log(data);
				var state = data.state; 
				if(state == undefined){
					alert("服务器异常!");
				}     
				if(state == "301"){
					console.log("success");
					e.addClass("haveAgree");
				}
				if(state == "302"){
					alert("操作失败!");
				}
				if(state == "303"){
					alert("请求已处理!");
					e.addClass("haveAgree");
				}
				if(state == "304"){
					alert("申请已不存在!");
				}
			},"json");
			return ;
		}
	});
})