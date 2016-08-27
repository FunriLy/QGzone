$(function(){

	var IP = "192.168.43.138";
	var hostId;
//	var loadUrl = "http://"+IP+":8080/QGzone/html/innerAlbums.html?userId="+hostId;

	//获取主人ID-----------------------------------------------------------------------------------------------------------
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
	console.log("主人用户ID是"+hostId);

	//当前在线的用户id--------------------------------------------------------------------------------
	var currentId;
	$.ajax({
		url : "../MessageGet",
		async : false,
		success : function(data){
					console.log(data);
					var state = data.state;
					if(state == undefined){
						alert("服务器异常!");
					}
					if(state == "161"){
						var message = data.message;
						currentId = message.userId;
						console.log("当前在线用户为："+currentId);
						judge();
					}
					if(state == "162"){
						alert("获取当前用户ID失败,请稍后重试!");
					}
				},
		dataType:"json"
	});
	
	//判断当前访问人与相册的关系-----------------------------------------------------------------------------------------
	function judge(){
		if(hostId!=null){
			if(currentId==hostId){
				$.post("../Albums",{userId:hostId},function(data){
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
						for(var i=0;i<data.jsonList.length;i++){
							var id = data.jsonList[i].albumId;
							var name = data.jsonList[i].albumName;
							var status = data.jsonList[i].albumState;
							var time = data.jsonList[i].albumUploadTime;
							var count = data.jsonList[i].photoCount;
							if(status==1){
								createPrivateAlbum(id,status,count,name);
							}else if(status==0){
								createAlbum(id,status,count,name);
							}else{
								console.log(albumName+"相册加载出错");
								return ;
							}
						}
					}
				},"json");
			}
			else if(currentId!=hostId){
				alert("我要看主人的相册！！！！");
				$(".function_bar").remove();
				$.post("../Albums?",{userId : hostId},function(data){
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
						console.log("现在是好友访问界面！！！！！！！！！！！！！");
						$(".function_bar").remove();
						for(var i=data.jsonList.length;i>=0;i--){
							var id = data.jsonList[i].albumId;
							var name = data.jsonList[i].albumName;
							var status = data.jsonList[i].albumState;
							// var time = data.jsonList[i].albumUploadTime;
							var count = data.jsonList[i].photoCount;
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
			}
		}
	}


//隐藏元素--------------------------------------------------------------
	// // $(".nameErr1").hide();
	// // $(".pwErr").hide();
	// $("#create_alb").hide();
	// // $(".box").hide();
	// // $(".click").hide();
	// $(".list").hide();
	// // $(".list_btn").hide();
	// $(".list_detail").hide();
	// $(".change_container").hide();
	// $(".clear_container").hide();
	// $(".delete_container").hide();


//全局变量------------------------------------------------------------
	var staName = 1,
		staPw = 1;
	var $err1 = $(".nameErr1").detach();
	var $err2 = $(".nameErr2").detach();
	var $err3 = $(".pwErr1").detach();
	var $err4 = $(".pwErr2").detach();
	var $pwFill ;
	var $pwFill1;
	var myId;
	var myStatus;
	var myName;
	var myCount;
	var albPw = "";

//生成新相册节点--------------------------------------------------------------------------------
 	function createPrivateAlbum(albumId,status,count,name){
 		var album = $('<div class="every_album" albumId="'+albumId+'" status="'+status+'">'+
 					  	'<div class="list"><img src="../images/p_list.png" class="list_btn">'+
	 					  	'<div class="list_detail">'+
		 					  	'<p class="change">修改相册信息</p><hr>'+
		 					  	'<p class="clear">清空相册</p><hr>'+
		 					  	'<p class="delete">删除相册</p>'+
		 					'</div>'+
	 					'</div>'+
					  	'<div class="alb_cov"><img src="../images/alb_cov.png" class="pic">'+
					  		'<h1 class="pho_count">'+count+'</h1>'+
					  	'</div>'+
					  	'<span class="name_alb">'+name+'</span>'+
			  			'<img src="../images/lock.png" class="lock">'+
				  	'</div>');
 		var container = $("#contain_album");
 		container.append(album);
 	}

 	function createAlbum(albumId,status,count,name){
 		var album = $('<div class="every_album" albumId="'+albumId+'" status="'+status+'">'+
 					  '<div class="list"><img src="../images/p_list.png" class="list_btn">'+
 					  '<div class="list_detail"><p class="change">修改相册信息</p><hr>'+
 					  '<p class="clear">清空相册</p><hr><p class="delete">删除相册</p></div></div>'+
 					  '<div class="alb_cov"><img src="../images/alb_cov.png" class="pic">'+
 					  '<h1 class="pho_count">'+count+'</h1></div><span class="name_alb">'+name+'</span></div>');
 		var container = $("#contain_album");
 		container.append(album);
 	}
	

//创建相册-----------------------------------------------------------
	$(".btn_cre_alb").click(function(){
		$("#create_alb").toggle();
	});
		
	function cancel(){
		$("#alb_name").val("相册名称，最多10字");
		$(".privacy_sel").val("0");
		$("#alb_name_change").val("");
		$("#password").val("");
		$("#password_new").val("");
		$(".nameErr1").detach();
		$(".nameErr2").detach();
		$(".pwErr1").detach();
		$(".pwErr2").detach();
		$(".inputarea_pw").detach();
		$(".inputarea_pw_new").detach();
	}
	$(".cancel").click(function(){
		cancel();
		$(".click button").replaceWith('<button>显示密码</button>');
		$(".inputarea_pw .box input").attr("type","password");
		$("#create_alb").hide();
		$("#change_container").hide();
	});

//对相册命名-----------------------------------------------------------
	$("#alb_name").focus(function(){
		var txtValue = $(this).val();
		if(txtValue=="相册名称，最多10字"){
			$(this).val("");
		}
		$(this).keyup(function(){
			var txtValue = $(this).val();
			var judAlbName = /[^\w\u4e00-\u9fa5]/g;
			if(judAlbName.test(txtValue)){
				if($(".nameErr1").is(":visible")!=true){
					$err1.insertAfter("#alb_name");
				}
				staName = 0;
			}
			else{
				$(".nameErr1").detach();
				staName = 1;
			}
		});
	});
	$("#alb_name").blur(function(){
		var txtValue = $(this).val();
		var judAlbName = /[^\w\u4e00-\u9fa5]/g;
		if(txtValue==""){
			$(this).val("相册名称，最多10字");
		}
		else if(judAlbName.test(txtValue)){
			if($(".nameErr1").is(":visible")!=true){
					$err1.insertAfter("#alb_name");
				}
			staName = 0;
		}
		else{
			$(".nameErr1").detach();
			staName = 1;
		}
		//异步检查命名-------------------------------------------------------------------
		var nameA = $("#alb_name").val();
		$.ajax({
			type : "POST",
			url : "../DuplicationAlbum",
			data : {albumName : nameA},
			success : function(data){
				console.log(data);
				console.log("我进入异步判断重名并且拿到返回值");
				var state = data.state;
				if(state==undefined){
					console.log("异步判断重名时服务器异常");
					return ;
				}else if(state==602){
					$err2.insertAfter("#alb_name");
					staName = 0;
				}
			},
			dataType : "json"
		});
	});
//根据权限判断是否填密码--------------------------------------------------------------------
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

//设置密码可见-----------------------------------------------------------------------------
	$(document).on("click",".click",function(){
		if ($("#password").attr("type")=="password")
		{
			$(".click button").replaceWith('<button>隐藏密码</button>');
			$("#password").attr("type","text");
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
	$("#password").keyup(function(){
		var pwValue = $(this).val();
		var judAlbPw = /[^\w]/g;
		if(pwValue==""||judAlbPw.test(pwValue))
		{
			if($(".pwErr1").is(":visible")!=true){
				$err3.insertAfter(".click");
				$(".pwErr1").show();
			}
			staPw = 0;
		}	
		else{
			$(".pwErr1").detach();
			staPw = 1;
		}
	});
	$("#password").blur(function(){
		var pwValue = $(this).val();
		if(pwValue.length<6){
			$err4.insertAfter(".click");
			$(".pwErr2").show();
			staPw = 0;
		}else{
			$(".pwErr2").detach();
		}
		albPw = pwValue;

	});
	$pwFill = $(".inputarea_pw").detach();
//判断是否符合提交要求----------------------------------------------------
	$(".sure").click(function(e){
		if(staName!=1)
		{
			alert("相册名有误，请修改");
			if(staPw!=1){
			alert("相册密码有误，请修改");
			}
		}
		else{
			$(".sure").attr("type","submit");
			var name = $("#alb_name").val();
			var status = $(this).parents("#last_bar").find(".privacy_sel").val();
			var password =  $(this).parents("#last_bar").find("#password").val();
			if(password==undefined||status==0)
			{
				password = "";
			}
			var count = 0;
			var album = {
				albumName : name,
				albumPassword : password,
				albumState : status,
				photoCount : count
			};
			
			console.log("新相册数据："+album.albumName+"密码是"+album.albumPassword+"权限"+album.albumState+album.photoCount);
			$.post("../CreateAlbum",{album:JSON.stringify(album)},function(data){
				console.log(data);
				var create_alb = $(e.target).parents("#create_alb");
				create_alb.hide();
				create_alb.find("#alb_name").val("相册名称，最多10字");
				create_alb.find(".privacy_sel").val("0");
				create_alb.find("#password").val("");
				var state = data.state;
				console.log(state);
				if(state==undefined){
					alert("服务器异常");
				}else if(state==602){
					alert("创建失败");
				}else if(state==603){
 					alert("填写信息有误");
				}else if(state==604){
					alert("相册重名，请修改");
				}else if(state==601){
					alert("恭喜你成功新建一个相册："+album.albumName);
					// var userId = data.jsonObject.userId;
					var albumId = data.jsonObject.albumId;
					var name = data.jsonObject.albumName;
					// var password = data.jsonObject.albumPassword;
					var status = data.jsonObject.albumState;
					var time = data.jsonObject.albumUploadTime;
					var count = data.jsonObject.photoCount;
					if(status==1)
					{
						createAlbum(albumId,status,count,name);
					}else{
						createPrivateAlbum(albumId,status,count,name);
					}
				}
				$("#last_bar .inputarea_pw").detach();
				$(".nameErr1").detach();
				$(".nameErr2").detach();
				$(".pwErr1").detach();
				$(".pwErr2").detach();
				cancel();
			},"json");
		}
	});


//编辑相册----------------------------------------------------------------------
	$(".btn_edit").click(function(){
		$(".list").toggle();
	});
	$(document).on("click",".list_btn",function(event){
		var curr = $(event.target);
		var pare = curr.parents(".every_album");
//		myId = pare.attr("albumId");
//		myCount = pare.find(".pho_count").text();
//		myName = pare.find(".name_alb").text();
//		myStatus = pare.attr("status");
		var listDe = pare.find(".list_detail");
		listDe.toggle();
//		console.log("我拿到了"+myId+myStatus+myName+myCount);
	});

//正确判断，现在先改
//	$(document).on("click",".change",function(event){
//		var tg = $(event.target);
//		console.log(myId,myStatus,myName,myCount);
//		$("#alb_name_change").val(myName);
//		$(".privacy_sel").val(myStatus);
//		if(myStatus==0){
//			$(".inputarea_pw_new").detach();
//		}else{
//			$pwFill1.insertAfter("#bottom .inputarea_pri");
//			$("#password_new").val("");
//		}
//		$("#change_container").show();
//	});

//强制改变相册权限
	$(document).on("click",".change",function(event){
		var tg = $(event.target);
		var pare = tg.parents(".every_album");
		myId = pare.attr("albumId");
		myCount = pare.find(".pho_count").text();
		myName = pare.find(".name_alb").text();
		myStatus = pare.attr("status");
		console.log(myId,myStatus,myName,myCount);
		$("#alb_name_change").val(myName);
		$(".privacy_sel").val("0");
		$(".inputarea_pw_new").detach();
		$("#change_container").show();
	});
	
	
	$("#alb_name_change").focus(function(e){
//		$("#alb_name_change").val(myName);	
		$(".nameErr1").detach();
		var txtValue = $(this).val();
		if(txtValue==myName){
			$(this).val("");
		}
		$(this).keyup(function(){
			var txtValue = $(this).val();
			var judAlbName = /[^\w\u4e00-\u9fa5]/g;
			if(judAlbName.test(txtValue)){
				if($(".nameErr1").is(":visible")!=true){
					$err1.insertAfter("#alb_name_change");
				}
				staName = 0;
			}
			else{
				$(".nameErr1").detach();
				staName = 1;
			}
		});
	});
	//无异步加载----------------------------------------------------------------------
	$("#alb_name_change").blur(function(){
		var txtValue = $(this).val();
		var judAlbName = /[^\w\u4e00-\u9fa5]/g;
		if(txtValue==""){
			$(this).val(myName);
		}
		else if(judAlbName.test(txtValue)){
			if($(".nameErr1").is(":visible")!=true){
					$err1.insertAfter("#alb_name_change");
				}
			staName = 0;
		}
		else{
			$(".nameErr1").detach();
			staName = 1;
			myName = txtValue;
			console.log("新的相册名是"+myName);
		}
	});
	//根据权限判断是否填密码---------------------------------------------
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
	//设置密码可见--------------------------------------------------------
	$(document).on("click",".click_new",function(){
		if ($("#password_new").attr("type")=="password")
		{
			$(this).replaceWith('<button>隐藏密码</button>');
			$("#password_new").attr("type","text");
			return ;
		}
		if ($("#password_new").attr("type")=="text")
		{
			$(this).replaceWith('<button>显示密码</button>');
			$("#password_new").attr("type","password");
			return ;
		}
	});
		
//判断密码-------------------------------------------------------------
	$("#password_new").keyup(function(){
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
	$("#password_new").blur(function(){
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
	//上传修改相册信息---------------------------------------------------------------------------------------------
//	$(".sure_change").click(function(){
//		$("#change_container").hide();
////		var name = $("#alb_name").val();
//		var status = $("#bottom .privacy_sel").val();
//		var albPw1 = $("#password_new").val();
//		if(albPw1==undefined){
//			albPw1 = "";
//		}
//		console.log("新的名字状态和密码是："+myName+status+albPw1);
//		if(staName!=1){
//			alert("新命名有误，请修改");
//			return ;
//		}
//		if(staPw!=1){
//			alert("密码格式有误，请修改");
//			return ; 
//		}
//		else{
//			var obj = {
//				albumId : myId,
//				albumName : myName,
//				albumPassword : albPw1,
//				albumState : status,
//				photoCount : myCount
//			}
//			console.log(obj);
//			$("#change_container").hide();
//			$.post("http://"+IP+":8080/QGzone/AlterAlbumInformation",{jsonObject : JSON.stringify(obj)},function(data){
//				console.log(data);
//					var state = data.state;
//					if(state==undefined){
//						alert("服务器异常");
//					}else if(state==607){
//						alert("没有相应权限,修改失败");
//					}else if(state==603){
//						alert("格式错误，请修改");
//					}else if(state==604){
//						alert("相册重名，请修改");
//					}else if(state==607){
//						alert("您没有权限修改此相册");
//					}else if(state==608){
//						alert("相册不存在");
//					}else if(state==601){
//						console.log("更改成功，新的相册信息为"+obj.albumName+albumStatus);
//						$tg = $("div[albumId=obj.albumId]");
//						$tg.attr("status",obj.albumState);
//						$tg.find(".name_alb").text(obj.albumName);//修改相册名
//						$(".every_album[albumId='myId']").find(".name_alb").text(obj.albumName);
//						var imgLock = $tg.find(".lock");
//						imgLock.detach();
//						if(obj.albumState==1){
//							var nameBall = $tg.find(".name_alb")
//							imgLock.insertAfter(nameBall);
//						}else{
//							$tg.find(".lock").detach();
//						}
//					}
//			},"json");
//		}
//	});
	//强制修改权限提示

	$(".sure_change").click(function(){
		$("#change_container").hide();
		var status = $("#bottom .privacy_sel").val();
		var albPw1 = $("#password_new").val();
		if(albPw1==undefined||albPw1==""){
			if(status==1){
				alert("你的密码为空，请确认权限信息，否则将强制变成公开相册(此次修改不成功)");
				$("#password_new").detach();
				$("#bottom option").val("0");
				return false;
			}
			else{
				albPw1 = "";
			}
		}
		console.log("新的名字状态和密码是："+myName+status+albPw1);
		if(staName!=1){
			alert("新命名有误，请修改");
			return ;
		}
		if(staPw!=1){
			alert("密码格式有误，请修改");
			return ; 
		}
		else{
			var obj = {
				albumId : myId,
				albumName : myName,
				albumPassword : albPw1,
				albumState : status,
				photoCount : myCount
			}
			console.log(obj);
			$("#change_container").hide();
			$.post("../AlterAlbumInformation",{jsonObject : JSON.stringify(obj)},function(data){
				console.log(data);
					var state = data.state;
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
						if(obj.albumState==1){
							alert("更改成功，新的相册信息为:相册名称为"+obj.albumName+",相册为私密，相册密码为"+obj.albumPassword+"，请务必牢记！");
						}else{
							alert("更改成功，新的相册信息为：相册名称为"+obj.albumName+"，相册为公开；如有误，请尽快重新修改相册信息！！");
						}
						$tg = $("div[albumId="+obj.albumId+"]");
						$tg.attr("status",obj.albumState);
						$tg.find(".name_alb").text(obj.albumName);//修改相册名
						$(".every_album[albumId='myId']").find(".name_alb").text(obj.albumName);
						var imgLock = $tg.find(".lock");
						imgLock.detach();
						if(obj.albumState==1){
							var nameBall = $tg.find(".name_alb")
							imgLock.insertAfter(nameBall);
						}else{
							$tg.find(".lock").detach();
						}
						window.location.reload();
					}
//					window.location.herf = loadUrl;
//					return ;
			},"json");
		}
	});
	
	//清空相册-------------------------------------------------------------------------------------------
	$(document).on("click",".clear",function(event){
		var tg = $(event.target);
		var parent1 = tg.parents(".every_album");
		var parent2 = parent1.children(".alb_cov");
		var albumId = parent1.attr("albumId");
		var status = parent1.attr("status");
		var name = parent1.children(".name_alb").text();
		var count = parent2.children(".pho_count").text();
		console.log("我现在清空的相册是"+myId,status,name,count);
		console.log(albumId,name,count);
		var alarm = confirm("你确定清空相册"+name+"？");
		if(alarm){
			parent2.children(".pho_count").text("0");
			parent2.children(".pic").attr("src","../images/alb_cov.png");
			$.post("../EmptyAlbum",{albumId:albumId},function(data){
				console.log(data);
				var state = data.state;
				if(status==undefined){
					alert("服务器异常");
				}else if(state==601){
					parent2.children(".pho_count").text("0");
					parent2.children(".pic").attr("src","../images/alb_cov.png");
					alert("清除相册成功");	
				}else if(state==602){
					alert("清空相册"+name+"失败");
				}else if(state==607){
					alert("你没有清空此相册的权限");
				}else if(state==608){
					alert("此相册不存在");
				}
			},"json");
		}else{
			console.log("你取消清空相册");
		}
	});
	
	//删除相册-------------------------------------------------------------------------------------------
	$(document).on("click",".delete",function(event){
		var tg = $(event.target);
		var parent1 = tg.parents(".every_album");
		var parent2 = parent1.children(".alb_cov");
		var albumId = parent1.attr("albumId");
		var status = parent1.attr("status");
		var name = parent1.children(".name_alb").text();
		var count = parent2.children(".pho_count").text();
		console.log("现在删除相册的信息"+myId,status,name,count);
		var alarm = confirm("你确定删除相册"+myName+"？");
		if(alarm){
			$.post("../DeleteAlbum",{albumId},function(data){
				console.log(data);
				var state = data.state;
				if(state==undefined){
					alert("服务器异常");
				}else if(state==601){
					parent1.remove();
					alert("删除相册"+name+"成功");	
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

	//进入相册内--------------------------------------------------------------------------------------------------------------
	$(document).on("click",".pic",function(event){
		console.log("当前相册属于好友："+hostId);
		var tg = $(event.target);
		console.log(tg);
		var idAlb = tg.parents(".every_album").attr("albumId");
		 console.log("我现在正在访问的相册ID是："+idAlb);
		var ss = tg.parents(".every_album").attr("status");
		var albUrl = "innerAlbums.html?userId="+hostId+"&albumId="+idAlb+"&status="+ss;
		 console.log("我现在正在访问的相册权限是："+ss);
		if(ss==0){
			$.post("../CheckPublicAlbum",{albumId : idAlb},function(data){
				console.log(data);
				var object = JSON.parse(data);
				var state = object.state;
				console.log("我在进入相册"+state);
				if(state==undefined){
					alert("服务器异常");
				}else if(state==607){
					alert("您与此相册主人非好友关系，没有访问此相册的权限");
				}else if(state==608){
					alert("相册不存在");
				}else if(state==605){
					console.log("相册图片为0");
					window.location.href = albUrl; 
					return ;
				}else if(state==601){
					window.location.href = albUrl; 
					return ;
				}
			});
		}else{
			if(currentId==hostId){
//				alert("主人查看私密相册中");
				var obj = {
					userId : hostId,
					albumId : idAlb,
					albumPassword : "",
					albumState : ss
				};
				$.post("../CheckPrivacyAlbum",{jsonObject : JSON.stringify(obj)},function(data){
					console.log(data);
					var object = JSON.parse(data);
					var state = object.state;
					console.log("我在进入相册"+state);
					if(state==undefined){
						alert("服务器异常");
					}else if(state==602){
						alert("密码错误");
					}else if(state==605){
						console.log("照片为0");
						window.location.href = albUrl;
						return ;
					}else if(state==606){
						alert("您与此相册主人非好友关系，没有访问此相册的权限");
					}else if(state==601){
						window.location.href = albUrl;
						return ;
					}
				});
			}else{
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
						$.post("../InspectAlbum",{jsonObject : JSON.stringify(obj)},function(data){
							console.log(data);
							var object = JSON.parse(data);
							var state = object.state;
							console.log("我在进入相册"+state);
							if(state==undefined){
								alert("服务器异常");
							}else if(state==602){
								alert("密码错误");
							}else if(state==601){
								console.log("密码正确！！！！");
								$.post("../CheckPrivacyAlbum",obj,function(data){
									var state = data.state;
									if(state==undefined){
										alert("服务器异常");
									}else if(state==602){
										alert("密码错误");
									}else if(state==605){
										console.log("照片为0");
										window.location.href = albUrl;
										return ;
									}else if(state==606){
										alert("您与此相册主人非好友关系，没有访问此相册的权限");
									}else if(state==601){
										window.location.href = albUrl; 
										return ;
									}
								},"json");
							}
						});
						
					}
				});
		
			}
		}
	});

});
