$(function(){
	// var IP = "localhost";
	var IP = "192.168.1.100";
	var hostId;

	sessionStorage["friendId"]="1";
	var friendId = sessionStorage["friendId"];
	console.log(friendId);

	var zoneUrl = "http://192.168.1.100:8080/QGzone/html/pirvate_album.html";
	// var zoneAlbumUrl；
//	window.location.href = zoneUrl;
	$.post("http://"+IP+":8080/QGzone/Albums",{admit:-1},function(data){
		console.log(data);
		var state = data.state;
		if(state==undefined){
			alert("服务器异常");
		}else if(state==606){
			alert("您与此相册主人非好友关系");
		}
		else if(state==602){
			console.log("加载相册页面出错");
			return ;
		}else if(state==601){
			alert("你已经成功进入相册");
			for(var i=jsonList.length;i>=0;i--){
				var id = jsonList[i].albumId;
				var name = jsonList[i].albumName;
				var status = jsonList[i].albumState;
				var time = jsonList[i].albumUploadTime;
				var count = jsonList[i].photoCount;
				if(statue==1){
					createPrivateAlbum(albumId,status,count,name);
				}else if(status==0){
					createAlbum(albumId,status,count,name);
				}else{
					console.log(albumName+"相册加载出错");
					return ;
				}
			}
		}
	},"json");
	
//隐藏元素--------------------------------------------------------------
	// $(".nameErr1").hide();
	// $(".pwErr").hide();
	$("#create_alb").hide();
	// $(".box").hide();
	// $(".click").hide();
	$(".list").hide();
	// $(".list_btn").hide();
	$(".list_detail").hide();
	$(".change_container").hide();
	$(".clear_container").hide();
	$(".delete_container").hide();


//全局变量------------------------------------------------------------
	var staName = 1,
		staPw = 1;
	var $err1 = $(".nameErr1").detach();
	var $err2 = $(".nameErr2").detach();
	var $err3 = $(".pwErr1").detach();
	var $err4 = $(".pwErr2").detach();
	var $pwFill ;
	var $pwFill1;
	var id;
	var status;
	var name;
	var count;
	var albPw;

//当前在线的用户id--------------------------------------------------------------------------------
	var userId;
	var img_url;
	$.post("http://"+IP+":8080/QGzone/MessageGet",function(data){
		console.log(data);
		var state = data.state;
		if(state == undefined){
			alert("服务器异常!");
		}
		if(state == "161"){
			var message = data.message;
			userId = message.userId;
			// img_url = HEAD_IMG_URL + message.userImage;
			// username = message.userName;
		}
		if(state == "162"){
			alert("获取个人资料失败,请稍后重试!");
		}
	},"json");

//生成新相册节点--------------------------------------------------------------------------------
 	function createAlbum(albumId,status,count,name){
 		var album = $('<div class="every_album" albumId="'+albumId+'" status="'+status+'">'+
 					  	'<div class="list"><img src="../img/p_list.png" class="list_btn">'+
	 					  	'<div class="list_detail">'+
		 					  	'<p class="change">修改相册信息</p><hr>'+
		 					  	'<p class="clear">清空相册</p><hr>'+
		 					  	'<p class="delete">删除相册</p>'+
		 					'</div>'+
	 					'</div>'+
					  	'<div class="alb_cov">+<img src="../img/alb_cov.png" class="pic">'+
					  		'<h1 class="pho_count">'+count+'</h1>'+
					  	'</div>'+
					  	'<span class="name_alb">'+name+'</span>'+
			  			'<img src="../img/lock.png" class="lock">'+
				  	'</div>');
 		var container = $(".contain_album");
 		container.prepend(album);
 	}

 	function createPrivateAlbum(albumId,status,count,name){
 		var album = $('<div class="every_album" albumId="'+albumId+'" status="'+status+'">'+
 					  '<div class="list"><img src="../img/p_list.png" class="list_btn">'+
 					  '<div class="list_detail"><p class="change">修改相册信息</p><hr>'+
 					  '<p class="clear">清空相册</p><hr><p class="delete">删除相册</p></div></div>'+
 					  '<div class="alb_cov">+<img src="../img/alb_cov.png" class="pic">'+
 					  '<h1 class="pho_count">'+count+'</h1>+</div>+<span class="name_alb">'+name+'</span></div>');
 		var container = $(".container");
 		container.prepend(album);
 	}

//进入相册页面------------------------------------------------------------------------------------------------------
	function getUrlInformation(){
		var qs = (location.search.length>0 ? location.search.substring(1) : "" ),
			args = {},
			items = qs.length ? qs.split("&") : [],
			item = null,
			name = null,
			value = null;
		for(var i=0 ; i<items.length; i++){
			item = items[i].split("=");
			name = decodeURIComponent(item[0]);
			value = decodeURIComponent(item[1]);
			if(name.length){
				args[name]=value;
			}
		}
		return args;
	}
	var host = getUrlInformation();
	hostId = host.userId;
//	userId = "1";
//	hostId = "1";
	console.log("主人用户ID是"+hostId);
//	function judge(){
//		// var userId;
//		// var userId = '<s:property value="#session['LOGIN_USER'].userName" />';
//		if(hostId!=null){
//			if(userId==hostId){
//				$.post("http://"+IP+":8080/QGzone/Albums",-1,function(data){
//					console.log(data);
//					var state = data.state;
//					if(state==undefined){
//						alert("服务器异常");
//					}else if(state==606){
//						alert("您与此相册主人非好友关系");
//					}
//					else if(state==602){
//						console.log("加载相册页面出错");
//						return ;
//					}else if(state==601){
//						for(var i=jsonList.length;i>=0;i--){
//							var id = jsonList[i].albumId;
//							var name = jsonList[i].albumName;
//							var status = jsonList[i].albumState;
//							var time = jsonList[i].albumUploadTime;
//							var count = jsonList[i].photoCount;
//							if(statue==1){
//								createPrivateAlbum(albumId,status,count,name);
//							}else if(status==0){
//								createAlbum(albumId,status,count,name);
//							}else{
//								console.log(albumName+"相册加载出错");
//								return ;
//							}
//						}
//					}
//				},"json");
//			}
//			else if(userId!=hostId){
//				$.post("http://"+IP+":8080/QGzone/Albums",hostId,function(data){
//					console.log(data);
//					var state = data.state;
//					if(state==undefined){
//						alert("服务器异常");
//					}else if(state==606){
//						alert("您与此相册主人非好友关系");
//					}
//					else if(state==602){
//						console.log("加载相册页面出错");
//						return ;
//					}else if(state==601){
//						alert("现在是好友访问界面！！！！！！！！！！！！！");
//						$(".function_bar").remove();
//						for(var i=jsonList.length;i>=0;i--){
//							var id = jsonList[i].albumId;
//							var name = jsonList[i].albumName;
//							var status = jsonList[i].albumState;
//							// var time = jsonList[i].albumUploadTime;
//							var count = jsonList[i].photoCount;
//							if(statue==1){
//								createPrivateAlbum(albumId,status,count,name);
//							}else if(status==0){
//								createAlbum(albumId,status,count,name);
//							}else{
//								console.log(albumName+"相册加载出错");
//								return ;
//							}
//						}
//					}
//				},"json");
//				$(".btn_cre_alb").hide();
//				$(".btn_edit").hide();
//			}
//		}
//	}
//	judge();
//	
//相册列表------------------------------------------------------------
//创建相册-----------------------------------------------------------
	$(".btn_cre_alb").click(function(){
//		$("#create_alb").toggle();
	});
		
	function cancel(){
		$(".alb_name").val("相册名称，最多10字");
		$(".alb_name_change").val("");
		$(".password").val("");
		$(".password_new").val("");
		$("#create_alb").hide();
		$(".change_container").hide();
		$(".clear_container").hide();
		$(".delete_container").hide();
		// $(".nameErr1").detach();
		$(".nameErr2").detach();
		$(".pwErr1").detach();
		$(".pwErr2").detach();
	}
	$(".cancel").click(function(){
		cancel();
	});

//对相册命名-----------------------------------------------------------
	$(".alb_name").focus(function(){
		// $(".nameErr1").hide();
		// $(".nameErr1").detach();
		var txtValue = $(this).val();
		if(txtValue=="相册名称，最多10字"){
			$(this).val("");
		}
		$(this).keyup(function(){
			var txtValue = $(this).val();
			var judAlbName = /[^\w\u4e00-\u9fa5]/g;
			if(judAlbName.test(txtValue)){
				if($(".nameErr1").is(":visible")!=true){
					$err1.insertAfter(".alb_name");
				}
				staName = 0;
			}
			else{
				$(".nameErr1").detach();
				staName = 1;
			}
		});
	});
	$(".alb_name").blur(function(){
		var txtValue = $(this).val();
		var judAlbName = /[^\w\u4e00-\u9fa5]/g;
		if(txtValue==""){
			$(this).val("相册名称，最多10字");
		}
		else if(judAlbName.test(txtValue)){
			if($(".nameErr1").is(":visible")!=true){
					$err1.insertAfter(".alb_name");
				}
			staName = 0;
		}
		else{
			$(".nameErr1").detach();
			staName = 1;
		}
		//异步检查命名
		var name = $(".alb_name").val();
		$.ajax({
			type : "POST",
			url : "http://"+IP+":8080/QGzone/DuplicationAlbum",
			data : {albumName : name},
			success : function(data){
				console.log(data);
				var state = data.state;
				if(state==undefined){
					console.log("异步判断重名时服务器异常");
					return ;
				}else if(state==603){
					$err2.insertAfter(".alb_name");
					staName = 0;
				}
			},
			dataType : "json"
		});
	});
//根据权限判断是否填密码---------------------------------------------
	$("#last_bar .privacy_sel").blur(function(){
		var status = $(this).val();
		if($(".inputarea_pw").is(":visible")){
			if(status==0){
				$(".inputarea_pw").detach();
			}
		}else{
			if(status!=0){
				$pwFill.insertAfter("#last_bar .inputarea_pri");
			}
		}
	});

//设置密码可见--------------------------------------------------------
	$(document).on("click",".click",function(){
		if ($(".password").attr("type")=="password")
		{
			$(".click button").replaceWith('<button>隐藏密码</button>');
			$(".password").attr("type","text");
			return ;
		}
		if ($(".inputarea_pw .box input").attr("type")=="text")
		{
			$(".click button").replaceWith('<button>显示密码</button>');
			$(".inputarea_pw .box input").attr("type","password");
			return ;
		}
	});

//判断密码-------------------------------------------------------------
	$("#last_bar .password").keyup(function(){
		var pwValue = $(this).val();
		var judAlbPw = /[^\w]/g;
		if(pwValue==""||judAlbPw.test(pwValue))
		{
			if($(".pwErr1").is(":visible")!=true){
				$err3.insertAfter(".click");
			}
			staPw = 0;
		}	
		else{
			$(".pwErr1").detach();
			staPw = 1;
		}
	});
	$("#last_bar .password").blur(function(){
		var pwValue = $(this).val();
		if(pwValue.length<6){
			$err4.insertAfter(".click");
			staPw = 0;
		}else{
			$(".pwErr2").detach();
		}
		albPw = pwValue;
	});
	$pwFill = $(".inputarea_pw").detach();
//判断是否符合提交要求----------------------------------------------------
	$(".sure").click(function(){
		console.log("密码是2:"+albPw);
		if(staName!=1)
		{
			alert("相册名有误，请修改");
			if(staPw!=1){
			alert("相册密码有误，请修改");
			}
		}
		else{
			$(".sure").attr("type","submit");
			var name = $(".alb_name").val();
			var status = $(this).parents("#last_bar").find(".privacy_sel").val();
			var password =  $(this).parents("#last_bar").find(".password").val();
			var count = 0;
			var obj = {
				albumName : name,
				albumPassword : password,
				albumState : status,
				photoCount : count
			};
			$.post("http://"+IP+":8080/QGzone/CreateAlbum",obj,function(date){
				console.log(data);
				var state = data.state;
				if(state==undefined){
					alert("服务器异常");
				}else if(state==602){
					alert("创建失败");
				}else if(state==603){
 					alert("填写信息有误");
				}else if(state==604){
					alert("相册重名，请修改");
				}else if(state==601){
					alert("创建成功");
					// var userId = data.userId;
					var albumId = data.albumId;
					var name = data.albumName;
					// var password = data.albumPassword;
					var status = data.albumState;
					var time = data.albumUploadTime;
					var count = data.photoCount;
					if(status==1)
					{
						createAlbum(albumId,status,count,name);
					}else{
						createPrivateAlbum(albumId,status,count,name);
					}
				}
			},"json");
		}
	});


//编辑相册----------------------------------------------------------------------
	$(".btn_edit").click(function(){
		$(".list").toggle();
	});
	$(".list_btn").click(function(){
		$(".list_detail").toggle();
	});

	$(".change").click(function(event){
		var tg = $(event.target);
		// console.log(tg);
		parent1 = tg.parents(".every_album");
		parent2 = parent1.children(".alb_cov");
		id = parent1.attr("albumId");
		status = parent1.attr("status");
		name = parent1.children(".name_alb").text();
		count = parent2.children(".pho_count").text();
		console.log(id,status,name,count);
		$(".alb_name_change").val(name);
		$(".privacy_sel").val(status);
		$(".inputarea_pw_new").detach();
		if(status==0){
			$(".inputarea_pw_new").detach();
		}else{
			$pwFill1.insertAfter("#bottom .inputarea_pri");
		}
			$(".password_new").val("");
		$(".change_container").show();
	});

	$(".alb_name_change").focus(function(name){
		console.log(id,status,name,count);
		$(".alb_name_change").val(name);	
		$(".nameErr1").detach();
		var txtValue = $(this).val();
		if(txtValue==name){
			$(this).val("");
		}
		$(this).keyup(function(){
			var txtValue = $(this).val();
			var judAlbName = /[^\w\u4e00-\u9fa5]/g;
			if(judAlbName.test(txtValue)){
				if($(".nameErr1").is(":visible")!=true){
					$err1.insertAfter(".alb_name_change");
				}
				staName = 0;
			}
			else{
				$(".nameErr1").detach();
				staName = 1;
			}
		});
	});
	//无异步加载
	$(".alb_name_change").blur(function(){
		var txtValue = $(this).val();
		var judAlbName = /[^\w\u4e00-\u9fa5]/g;
		if(txtValue==""){
			$(this).val(name);
		}
		else if(judAlbName.test(txtValue)){
			if($(".nameErr1").is(":visible")!=true){
					$err1.insertAfter(".alb_name_change");
				}
			staName = 0;
		}
		else{
			$(".nameErr1").detach();
			staName = 1;
			name = txtValue;
		}
	});
	//修改权限------------------------------------------------------
	$("#bottom .privacy_sel").blur(function(){
		var status = $(this).val();
		console.log("sel:"+status);
		if($(".inputarea_pw_new").is(":visible")){
			if(status==0){
				$(".inputarea_pw_new").detach();
			}
		}else{
			if(status!=0){
				$pwFill1.insertAfter("#bottom .inputarea_pri");
			}
		}
	});
	//根据权限判断是否填密码---------------------------------------------
		
	$(".click_new").click(function(){
		console.log("!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		if ($(".inputarea_pw_new box input").attr("type")=="password")
		{
			$(".click_new button").replaceWith('<button>隐藏密码</button>');
			$(".inputarea_pw_new .box input").attr("type","text");
			return ;
		}
		if ($(".inputarea_pw_new .box input").attr("type")=="text")
		{
			$(".click_new button").replaceWith('<button>显示密码</button>');
			$(".inputarea_pw_new .box input").attr("type","password");
			return ;
		}
	});
	//设置密码可见--------------------------------------------------------
	$(document).on("click",".click_new",function(){
		if ($(".password").attr("type")=="password")
		{
			$(".click_new button").replaceWith('<button>隐藏密码</button>');
			$(".password").attr("type","text");
			return ;
		}
		if ($(".inputarea_pw .box input").attr("type")=="text")
		{
			$(".click_new button").replaceWith('<button>显示密码</button>');
			$(".inputarea_pw .box input").attr("type","password");
			return ;
		}
	});

//判断密码-------------------------------------------------------------
	$("#bottom .password_new").keyup(function(){
		var pwValue = $(this).val();
		var judAlbPw = /[^\w]/g;
		if(pwValue==""||judAlbPw.test(pwValue))
		{
			if($(".pwErr1").is(":visible")!=true){
				$err3.insertAfter(".click_new");
			}
			staPw = 0;
		}	
		else{
			$(".pwErr1").detach();
			staPw = 1;
		}
	});
	$("#bottom .password_new").blur(function(){
		var pwValue = $(this).val();
		if(pwValue.length<6){
			$err4.insertAfter(".click_new");
			staPw = 0;
		}else{
			$(".pwErr2").detach();
		}
		albPw = pwValue;
	});
	$pwFill1 = $(".inputarea_pw_new").detach();
	$(".sure_change").click(function(){
		// var name = $(".alb_name").val();
		var status = $("#bottom .privacy_sel").val();
		var albPw = $("#bottom .password_new").val();
		console.log("新的状态和密码是："+status+albPw);
		if(staName!=1){
			alert("新命名有误，请修改");
		}
		if(staPw!=1){
			alert("密码格式有误，请修改");
		}
		else{
			var obj = {
				albumId : id,
				albumName : name,
				albumPassword : albPw,
				albumState : status,
				photoCount : count
			}
			console.log(obj);
			$.post("http://"+IP+":8080/QGzone/AlterAlbumInformation",obj,function(state){
				console.log(state);
					// var state = data.state;
					if(state==undefined){
						alert("服务器异常");
					}else if(state==607){
						alert("没有相应权限,修改失败");
					}else if(state==603){
						alert("格式错误，请修改");
					}else if(state==604){
						alert("相册重名，请修改");
					}else if(state==607){
						alert("您没有权限修改此相册");
					}else if(state==608){
						alert("相册不存在");
					}else if(state==601){
						$tg = $("img[albumId=obj.albumId]");
						$tg.attr("status",obj.albumState);
						$tg.parents(".every_album").children(".name_alb").text(obj.albumName);
						var imgLock = $tg.parents(".every_album").children("img");
						imgLock.detach();
						if(obj.albumState==1){
							imgLock.insertAfter(".name_alb");
						}else{
							$tg.parents(".every_album").children("img").detach();
						}
					}
			});
		}
	});
	//清空相册-------------------------------------------------------------------------------------------
	$(".clear").click(function(event){
		var tg = $(event.target);
		parent1 = tg.parents(".every_album");
		parent2 = parent1.children(".alb_cov");
		var albumId = parent1.attr("albumId");
		// status = parent1.attr("status");
		name = parent1.children(".name_alb").text();
		count = parent2.children(".pho_count").text();
		// console.log(id,status,name,count);
		console.log(albumId,name,count);
		var alarm = confirm("你确定清空相册"+name+"？");
		if(alarm){
			parent2.children(".pho_count").text("0");
			parent2.children(".pic").attr("src","../img/alb_cov.png");
			$.post("http://"+IP+":8080/QGzone/EmptyAlbum",albumId,function(state){
				console.log(state);
				if(status==undefined){
					alert("服务器异常");
				}else if(state==601){
					parent2.children(".pho_count").text("0");
					parent2.children(".pic").attr("src","../img/p_list.png");
					alert("清除相册成功");	
				}else if(state==602){
					alert("删除相册失败");
				}else if(state==607){
					alert("你没有删除此相册的权限");
				}else if(state==608){
					alert("此相册不存在");
				}
			},"json");
		}else{
			console.log("你取消删除相册");
		}
	});	
	//删除相册-------------------------------------------------------------------------------------------
	$(".delete").click(function(event){
		var tg = $(event.target);
		parent1 = tg.parents(".every_album");
		parent2 = parent1.children(".alb_cov");
		var albumId = parent1.attr("albumId");
		// status = parent1.attr("status");
		name = parent1.children(".name_alb").text();
		count = parent2.children(".pho_count").text();
		// console.log(id,status,name,count);
		console.log(albumId,count);
		var alarm = confirm("你确定删除相册"+name+"？");
		if(alarm){
			$.post("http://"+IP+":8080/QGzone/DeleteAlbum",albumId,function(state){
				console.log(state);
				if(status==undefined){
					alert("服务器异常");
				}else if(state==601){
					parent1.remove();
					alert("删除相册成功");	
				}else if(state==602){
					alert("删除相册失败");
				}else if(state==607){
					alert("你没有删除此相册的权限");
				}else if(state==608){
					alert("此相册不存在");
				}
			},"json");
		}else{
			console.log("你取消删除相册");
		}
	});	

//相册内------------------------------------------------------------------------------------------------
	$("#enter_pw").hide();
	var checkPw = 0;
  //检验密码--------------------------------------------------------------------------------------------------
	//设置密码可见--------------------------------------------------------
	$(document).on("click","#review",function(){
		if ($(".check_password").attr("type")=="password")
		{
			$("#review button").replaceWith('<button>隐藏密码</button>');
			$(".check_password").attr("type","text");
			return ;
		}
		if ($(".inputarea_pw .box input").attr("type")=="text")
		{
			$("#review button").replaceWith('<button>显示密码</button>');
			$(".inputarea_pw .box input").attr("type","password");
			return ;
		}
	});

	//判断密码-------------------------------------------------------------
	$("#last_bar2 .check_password").keyup(function(){
		var pwValue = $(this).val();
		var judAlbPw = /[^\w]/g;
		if(pwValue==""||judAlbPw.test(pwValue))
		{
			if($(".pwErr1").is(":visible")!=true){
				$err3.insertAfter("#review");
			}
			checkPw = 0;
		}	
		else{
			$(".pwErr1").detach();
			checkPw = 1;
		}
	});
	$("#last_bar2 .check_password").blur(function(){
		var pwValue = $(this).val();
		if(pwValue.length<6){
			$err4.insertAfter("#review");
			checkPw = 0;
		}else{
			$(".pwErr2").detach();
		}
		albPw = pwValue;
	});

// 	//生成新图片-------------------------------------------------------------------------------------------------
// 	var photoUrl = "http://"+IP+":8080/QGzone/album";
// 	function createPhoto(pId,aId,uId){
// 		var phoThumbnail = "t_"+pId;
// 		var photo = $('<div class="every_photo" photoId="'+pId+'">'+
//  					  	'<div class="delete_btn"><img src="../img/p_list.png"></div>'+
// 					  	'<div class="photo"><img src="'+photoId+'/'+uId+'/'+aId+'/'+phoThumbnail+'.jpg'+'" class="pho">'+
// 					  	'</div></div>');
//  		var container = $(".contain_photo");
//  		container.prepend(photo);
// 	}
	//进入相册内--------------------------------------------------------------------------------------------------------------
	$(document).on("click",".pic",function(event){
		// console.log("当前相册属于好友："+hostId);
		var tg = $(event.target);
		console.log(tg);
		var idAlb = tg.parents(".every_album").attr("albumId");
		// console.log("我现在正在访问的相册ID是："+idAlb);
		var ss = tg.parents(".every_album").attr("status");
		var albUrl = "innerAlbums.html?userId="+hostId+"&albumId="+idAlb;
		// console.log("我现在正在访问的相册权限是："+ss);
		if(ss==0){
			// window.location.href="file:///C:/Users/hp/Desktop/project/html/innerAlbums.html?"+hostId+"&"+tg.albumId;
			$.post("http://"+IP+":8080/QGzone/CheckPublicAlbum",idAlb,function(data){
				console.log(data);
				var state = data.state;
				if(state==undefined){
					alert("服务器异常");
				}else if(state==607){
					alert("您与此相册主人非好友关系，没有访问此相册的权限");
				}else if(state==608){
					alert("相册不存在");
				}
				else if(state==602){
					console.log("加载相册页面出错");
					return ;
				}else if(state==601){
					window.location.href = albUrl; 
					for(var i=jsonList.length;i>=0;i--){
						var pId = jsonList[i].photoId;
						var pTime = jsonList[i].photoUploadTime;
						createPhoto(pId,idAlb,userId);
						}
					}
			});
		}
		else{
			if(userId==hostId){
				alert("主人查看私密相册中");
				var obj = {
					userId : hostId,
					albumId : idAlb,
					albumPassword : "",
					albumState : ss
				};
				$.post("http://"+IP+":8080/QGzone/ CheckPrivacyAlbum",obj,function(data){
					var state = data.state;
					if(state==undefined){
						alert("服务器异常");
					}else if(state==602){
						alert("密码错误");
					}else if(state==605){
						console.log("照片为0");
					}else if(state==606){
						alert("您与此相册主人非好友关系，没有访问此相册的权限");
					}else if(state==601){
						window.location.href = albUrl;
						for(var i=jsonList.length;i>=0;i--){
						var pId = jsonList[i].photoId;
						var pTime = jsonList[i].photoUploadTime;
						createPhoto(pId,idAlb,userId);
						}
					}
				});
			}
			else{
				alert("好友查看私密相册中");
				$("#enter_pw").show();
				//判断是否符合提交要求----------------------------------------------------
				$("#check").click(function(event){
					var tg = $(event.target);
					var passwordCheck = tg.parents("#last_bar2").find(".check_password").val();
					console.log("好友输入的密码是:"+passwordCheck);
					if(checkPw!=1){
						alert("相册密码格式有误，请修改");
					}else{
						$("#check").attr("type","submit");
						var obj = {
							userId : hostId,
							albumId : idAlb,
							albumPassword : passwordCheck,
							albumState : ss
						};
						$.post("http://"+IP+":8080/QGzone/InspectAlbum",obj,function(data){
							var state = data.state;
							if(state==undefined){
								alert("服务器异常");
							}else if(state==602){
								alert("密码错误");
							}else if(state==601){
								console.log("密码正确！！！！");
								$.post("http://"+IP+":8080/QGzone/ CheckPrivacyAlbum",obj,function(data){
									var state = data.state;
									if(state==undefined){
										alert("服务器异常");
									}else if(state==602){
										alert("密码错误");
									}else if(state==605){
										console.log("照片为0");
									}else if(state==606){
										alert("您与此相册主人非好友关系，没有访问此相册的权限");
									}else if(state==601){
										window.location.href = albUrl;
										for(var i=jsonList.length;i>=0;i--){
										var pId = jsonList[i].photoId;
										var pTime = jsonList[i].photoUploadTime;
										createPhoto(pId,idAlb,userId);
										}
										 // window.location.href="login.jsp?backurl="+window.location.href; 
									}
								});
							}
						});
						
					}
				});
		
			}
		}
	});
// 	//上传图片------------------------------------------------------------------------------------------
// 	$(".btn_loadup").click(function(){
// 		$(".contain_photo").hide();
// 		$("#upload_window").show();


// 	});
// 	$(".cancel_upload").click(function(){
// 		$("#upload_window").hide();
// 		$(".contain_photo").show();
// 		$(".file_img").remove();
// 	})
	
// 	//查看大图------------------------------------------------------------------------------------------
// 	// $(".photo").click(function(event){
// 	// 	var tg = $(event.target);
// 	// 	var pare = tg.parents(".every_photo");
// 	// 	var picId = pare.attr("photoId");
// 	// 	alert("我点到了图片哦~~~~~~~");
// 	// });
// 	function openNew(){
// 	//获取页面的高度和宽度
// 	var sWidth=document.body.scrollWidth;
// 	var sHeight=document.body.scrollHeight;
	
// 	//获取页面的可视区域高度和宽度
// 	var wHeight=document.documentElement.clientHeight;
	
// 	var oMask=document.createElement("div");
// 		oMask.id="mask";
// 		oMask.style.height=sHeight+"px";
// 		oMask.style.width=sWidth+"px";
// 		document.body.appendChild(oMask);
// 	var oEnlarge=document.createElement("div");
// 		oEnlarge.id="enlarge";
// 		oEnlarge.innerHTML="<div class='enlarge_con'>"+
// 				"<div id='close'>点击关闭</div>"+
// 				"<img src='"+getPho+".jpg'></div>";
// 		document.body.appendChild(oEnlarge);
// 	//获取浮出层框的宽和高
// 	var dHeight=oEnlarge.offsetHeight;
// 	var dWidth=oEnlarge.offsetWidth;
// 	//设置浮出层框的left和top
// 		oEnlarge.style.left=sWidth/2-dWidth/2+"px";
// 		oEnlarge.style.top=wHeight/2-dHeight/2+"px";
// 	//点击关闭按钮
// 	var oClose=document.getElementById("close");
	
// 		//点击浮出层框以外的区域也可以关闭浮出层框
// 		oClose.onclick=oMask.onclick=function(){
// 					document.body.removeChild(oEnlarge);
// 					document.body.removeChild(oMask);
// 					};
// 					};
					
// 		$(".pho").click(function(event){
// 			var tg = $(event.target);
// 			var pare = tg.parents(".every_photo");
// 			var getPho = pare.attr("photoId");
// 			console.log("我要打开大图了！"+getPho);
// 			openNew();
// 				// return false;
// 		});
// 		$("#close").click(function(){
// 			$("#mask").remove();
// 			$("#enlarge").remove();
// 		});


// 	//删除图片------------------------------------------------------------------------------------------
// 	$(".delete_btn").hide();
// 	$(".btn_edit_pho").click(function(){
// 		$(".delete_btn").toggle();
// 	});
// 	var photoInfo = getUrlInformation();
// 	$(".delete_btn").click(function(event){
// 		var tg = $(event.target);
// 		var tgAlbId = photoInfo.albumId;
// 		var phot = tg.parents(".every_photo");
// 		var photId = phot.attr("photoId");
// 		var obj = {
// 			albumId : tgAlbId,
// 			photoId : photId
// 		};
// 		$.post("http://"+IP+":8080/QGzone/DeletePhoto",obj,function(state){
// 			if(state==undefined){
// 				alert("服务器异常");
// 			}else if(state==602){
// 				alert("删除图片失败");
// 			}else if(state==601){
// 				console.log("删除图片："+obj.photoId);
// 				phot.remove();
// 			}
// 		});
// 	});
});
