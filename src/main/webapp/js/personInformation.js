//可以加个过滤器判断登陆人和url后面跟的id是不是本人或好友关系

$(function(){
	var HEAD_IMG_URL = "/QGzone/jpg/"; // 放置用户头像的文件夹路径;
	var IP = "192.168.3.49";  //主机号；
	var LOGIN_USER_ID;              //登陆人的id
	var URL_USER_ID;                //url上的id
	var IS_LOGIN = true;           //判断是登陆人访问的还是好友访问的
	
//页面动态操作：-----------------------------------------------

//获取登陆人的id-----------------------------------------------
	$.ajax({
		url:"http://"+IP+":8080/QGzone/MessageGet",
		type:"POST",
		success:function(data){
					console.log("here:"+data);
					var state = data.state;
					if(state == undefined){
						alert("服务器异常!");
					}
					if(state == "161"){
						var message = data.message;
						LOGIN_USER_ID = message.userId;
						if(URL_USER_ID!=LOGIN_USER_ID){
							IS_LOGIN = false;
							$("#container_1 #change").remove();
							$("#container_2").remove();
						}
					}
					if(state == "162"){
						alert("获取个人资料失败,请稍后重试!");
					}
				},
		dataType:"json"
	});
	
//获取在URL后面要显示信息人的id------------------------------------------
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
	var info = getUrlInformation();

URL_USER_ID = info.userId;
//URL_USER_ID = LOGIN_USER_ID; //之后要修改=========================================================
	

//判断是否是通过登陆来访问页面的------------------------------------

//侧导航栏---------------------------------------------------------
	$("#c2_left ul").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase() != "LI"){
			return ;
		}
		e.addClass("color_white");
		e.siblings().removeClass("color_white");
		$("#c2_right #"+e.attr("index")).show();
		$("#c2_right #"+e.attr("index")).siblings().hide();
	});


//显示个人信息-------------------------------------------------------------
//从后台获取个人信息
	function showInfomation(url,info){
		var userId ;
		var img_url ;
		var username="未设置";
		var sex ="未设置";
		var birthday ="未设置";
		var address ="未设置";
		var phone ="未设置";
		var email ="未设置";

		$.post(url,info,function(data){
			console.log(data);
			var state = data.state;
			if(state == undefined){
				alert("服务器异常!");
			}
			if(state == "161"){
				var message = data.message;
				userId = message.userId;
				img_url = HEAD_IMG_URL + message.userImage;
				username = message.userName;
				if(message.userSex!="" && message.userSex!=undefined){
					sex = message.userSex;
				}
				if(message.userBirthday != "" && message.userBirthday !=undefined){
					birthday = message.userBirthday;
				}
				if(message.userAddress != "" && message.userAddress!=undefined){
					address = message.userAddress;
				}
				if(message.userPhone != "" && message.userPhone!=undefined){
					phone = message.userPhone;
				}
				if(message.userEmail != "" && message.userEmail!=undefined){
					email = message.userEmail;
				}
			}
			if(state == "162"){
				alert("获取个人资料失败,请稍后重试!");
			}
			//设置固定头像
			$("#diamonds_1").attr("src",img_url);
			$("#diamonds_2").find("img").attr("src",img_url);
			$("#photo_2").attr("src",img_url);
			$("#c2_right #second img").attr("src",img_url);
			//嵌入信息
			var lis = $("#val").children();
			$(lis[0]).text(username);
			$(lis[1]).text(sex);
			$(lis[2]).text(birthday);
			$(lis[3]).text(address);
			$(lis[4]).text(phone);
			$(lis[5]).text(email );
			
			//修改信息部分：-------------------------------------------------------
			$("#username input").val( username=="未设置" ? "" : username );
			$("#select_sex").val( sex =="未设置" ? "未设置":sex );
			$("#birthday input").val( birthday =="未设置" ? "" : birthday );
			$("#address input").val( address=="未设置" ? "" : address );
			$("#phone input").val( phone =="未设置" ? "" : phone );
			$("#email input").val( email =="未设置" ? "" : email );
		},"json");
	}
	//显示好友访问时的界面
	if(!IS_LOGIN){
		var url = "http://"+IP+":8080/QGzone/MessageSearch";
		var info = {
			userId : URL_USER_ID
		};
		showInfomation(url,info);
	}else{ //显示登陆后访问的界面
		var url = "http://"+IP+":8080/QGzone/MessageGet";
		var info = {};
		showInfomation(url,info);
	}

	

	$("#c2_right #first").find(".toChange").click(function(){
		var username = $("#username input").val().trim();
		var sex = $("#select_sex").val();
		var birthday = $("#birthday input").val().trim();
		var address = $("#address input").val().trim();
		var phone = $("#phone input").val().trim();
		var email = $("#email input").val().trim();
		var regUsername = /[^\w\u4e00-\u9fa5]/g; 
		var regPhone =  /^1[3|4|5|7|8]\d{9}$/;
		var regEmail =  /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/; 

		if(username !="" && regUsername.test(username)){
			alert("昵称只能由中文，英文，数字和下划线组成!");
			return ;
		}
		if(username == ""){
			alert("昵称不能为空!");
			return ;
		}
		if(phone != ""&& !regPhone.test(phone)){
			alert("电话格式不正确!");
			return ;
		}
		
		if(email != ""&& !regEmail.test(email)){
			alert("邮箱格式不正确!");
			return ;
		}

		var info = {
			userId : LOGIN_USER_ID,
			userName : username,
			userSex : sex,
			userBirthday : birthday,
			userAddress : address,
			userPhone : phone,
			userEmail : email
		}
		console.log(info);
		$.post("http://"+IP+":8080/QGzone/MessageChange",{jsonObject : JSON.stringify(info)}, function(data){
			console.log(data);
			var state = data.state;
			var message = data.message; 
			if(state == "151"){
				console.log("success");
				return ;
			}
			if(state == "152"|| state == undefined){
				alert("服务器异常,操作失败!");
				return ;
			}
		},"json");

	});

//头像图片上传------------------------------------------------------------------------
	$("#second input[type='button']").click(function(){
		var file = $(this).prev()[0].files[0];
		console.log(file.type);
		if(file.type.indexOf('image')==-1){
			alert("格式不正确,请上传图片格式的文件!");
			return ;
		}
		var formData = new FormData();
		formData.append("file",file);
		$.ajax({
			url:"http://"+IP+":8080/QGzone/UserUploadImage",
			type:"POST",
			data: formData,
			cache:false,
			processData: false,
			contentType: false,
			success: function(data){
						console.log(data);
						var state = data.state;
						if(state=="171"){
							alert("修改成功!"); 
							sessionStorage["isChangeImg"]="true";
							window.location.reload();
							return ;
						}
						if(state=="172"){
							alert("文件格式有误!");
							return ;
						}
						if(state==undefined || state=="173"){
							alert("服务器异常,请稍后重试!");
							return ;
						}
					},
			dataType:"json"
		});
	});


//跳转到修改界面--------------------------------------------------------------
	$("#container_1 #change").click(function(){
		$("#container_1").hide();
		$("#container_2").show();
	});

//修改密码和密保---------------------------------------------------------------
	$("#third .toChange").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase() != "SPAN"){
			return ;
		}
		if(e.attr("index")=="Password"){
			$("#changePassword").toggle();
			$("#changeProblem").hide();
			$("#changePassword #newPassword").val("");
			$("#changePassword #rePassword").val("");
		}else{
			$("#changePassword").hide();
			$("#changeProblem").toggle();
			$("#changeProblem #oldSelectAnswer").val("");
			$("#changeProblem #newSelectAnswer").val("");
		}
	});
	$("#changePassword .closeButton, #changePassword [name='cancel']").click(function(){
		$(this).parents("#changePassword").hide();
	});
	$("#changeProblem .closeButton, #changeProblem [name='cancel']").click(function(){
		$(this).parents("#changeProblem").hide();
	});

	$("#changePassword input[name='sure']").click(function(){
		var oldPassword = $("#changePassword #oldPassword").val().trim();
		var newPassword = $("#changePassword #newPassword").val().trim();
		var rePassword = $("#changePassword #rePassword").val().trim();
		var regPassword = /[^\da-zA-Z]/g; 
		if(oldPassword == ""){
			alert("旧密码不能为空!");
			return ;
		}
		if(newPassword == ""){
			alert("新密码不能为空!");
			return ;
		}else if(regPassword.test(newPassword)){
			alert("密码只能由数字，英文组成!");
			$("#changePassword #newPassword").val("");
			$("#changePassword #rePassword").val("");
			return ;
		}
		if(rePassword!=newPassword){
			alert("两次输入的密码不一致!");
			$("#changePassword #rePassword").val("");
			return ;
		}
		$(this).parents("#changePassword").hide();

		var info = {
			oldPassword : oldPassword,
			newPassword : newPassword,
		};
		$.post("http://"+IP+":8080/QGzone/UserChangePassword",{jsonObject: JSON.stringify(info)},function(data){
			console.log(data);
			var state = data.state;
			if(state == "131"){
				alert("修改成功!");
				window.location.href="http://"+IP+":8080/QGzone/html/login.html";
				return ;
			}
			if(state == "132" || state == undefined){
				alert("服务器异常,操作失败!");
				return ;
			}
		},"json");

	});
	$("#changeProblem input[name='sure']").click(function(){
		var oldSecretId = $("#changeProblem #oldSelect").val();
		var oldSecretAnswer = $("#changeProblem #oldSelectAnswer").val().trim();
		var newSecretId = $("#changeProblem #newSelect").val();
		var newSecretAnswer = $("#changeProblem #newSelectAnswer").val().trim();
		var regAnswer = /[^\u4e00-\u9fa5\da-zA-Z]/g; 
		if(oldSecretAnswer == "" ||newSecretAnswer == ""){
			alert("密保答案不能为空!");
			return ;
		}
		if(newSecretAnswer != "" && regAnswer.test(newSecretAnswer)){
			alert("密保答案只能由1-12个中文、英文、数字组成!");
			$("#changeProblem #newSelectAnswer").val("")
			return ;
		}
		$(this).parents("#changeProblem").hide();
		var info = {
			oldSecretId : oldSecretId,
			oldAnswer : oldSecretAnswer,
			newSecretId : newSecretId,
			newAnswer : newSecretAnswer
		};
		$.post("http://"+IP+":8080/QGzone/UserChangeSecret",{jsonObject:JSON.stringify(info)},function(data){
			console.log(data);
			var state = data.state;
			if(state == "141"){
				alert("修改成功!");
				return ;
			}
			if(state == "142" || state == undefined){
				alert("服务器异常,操作失败!");
				return ;
			}
		},"json");
	});

	//点击眼睛看输入的密码-------------------------------
	$("#changePassword .eye").mousedown(function(){
		var val = $(this).parent().find("input").val();
		var id = $(this).parent().find("input").attr("id");
		var text = $('<input type="text" id='+id+' value='+val+' >');
		$(this).parent().find("input").replaceWith(text);
	}).mouseup(function(){
		var val = $(this).parent().find("input").val();
		var id = $(this).parent().find("input").attr("id");
		var text = $('<input type="password" id='+id+' value='+val+' >');
		$(this).parent().find("input").replaceWith(text);
	})


//修改头像时刷新后跳到修改头像界面------------------------------------
	if(sessionStorage["isChangeImg"]=="true"){
		$("#container_1 #change").click();
		$("#c2_left ul :nth-child(2)").click();
		sessionStorage.removeItem("isChangeImg");
	}

});