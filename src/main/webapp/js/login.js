$(document).ready(function(){
	var IP = "192.168.1.100";
	sessionStorage["par1"]="123";

	var lo = $($("#option").children()[0]);
	var ro = $($("#option").children()[1]);
	var fg = $("#forget");
	var login = $("#login");
	var register = $("#register");
	var forget = $("#forgetPassword");
	
	lo.click(function(){
		login.show();
		register.hide();
		forget.hide();
		lo.removeClass("fade");
		ro.removeClass("fade");
		ro.removeClass("white");
		$(this).addClass("white");
	});

	ro.click(function(){
		register.show();
		login.hide();
		forget.hide();
		lo.removeClass("fade");
		ro.removeClass("fade");
		lo.removeClass("white");
		$(this).addClass("white");
	});

	fg.click(function(){
		forget.show();
		register.hide();
		login.hide();
		lo.removeClass("white");
		lo.addClass("fade");
		ro.addClass("fade");
	});


// login 验证；---------------------------------------------------------------------

	$("#l_account").keyup(function(){
		var account = $("#l_account").val().trim();
		var reg = /[^\d{5,8}]/; 
		if(account == ""){
			$(this).next().text("账号不能为空!");
		}else if(account.length>8 || account.length<5|| reg.test(account)){
			$(this).next().text("账号格式不正确!");
		}else{
			$(this).next().text("");
		}
	}).blur(function(){
		var account = $("#l_account").val().trim();
		var reg = /[^\d{5,8}]/; 
		if(account == ""){
			$(this).next().text("账号不能为空!");
		}else if(account.length>8 || account.length<5|| reg.test(account)){
			$(this).next().text("账号格式不正确!");
		}else{
			$(this).next().text("");
		}
	});

	$("#l_password").blur(function(){
		var password = $("#l_password").val().trim();
		if(password ==""){
			$(this).next().text("密码不能为空!");
		}else{
			$(this).next().text("");
		}
	}).keyup(function(){
		var password = $("#l_password").val().trim();
		if(password!=""){
			$(this).next().text("");
		}
	});

	$("#log").click(function(){
		var account = $("#l_account").val().trim(); 
		var password = $("#l_password").val().trim();
		var info = {
			userId : account,
			password : password
		}
		/*console.log(info);*/
		var p = $("#l_password").next().text().trim()+
				$("#l_account").next().text().trim();
		if(p!=""||account==""||password==""){
			alert("填写格式错误!");
		}else{
			$.post("http://"+IP+":8080/QGzone/UserSignIn",{jsonObject:JSON.stringify(info)},function(data){
				 var state = data.state;
				 var user = data.user;
				 console.log(data);
				 if(state==null){
				 	alert("服务器异常!");
				 	return ;
				 }
				 if(state=="111"){
				 	var user = data.user;
				 	var userId = user.userId;
				 	console.log("success:"+userId);
				 	window.location.href="http://"+IP+":8080/QGzone/html/pirvate_album.html"
				 	return ;
				 }
				 if(state=="112"){
				 	alert("登陆失败!");
				 	return ;
				 }
			},"json");
		}
	});


//register 验证；-------------------------------------------------------------------

	$("#username").keyup(function(){
		var username = $("#username").val().trim();
		var reg = /[^\w\u4e00-\u9fa5]/g; 
		if(username == ""){
			$(this).next().text("用户名不能为空!");
		}else if( reg.test(username)){
			$(this).next().text("昵称只能由中文，英文，数字和下划线组成");
		}else if(username.length>12){
			$(this).next().text("用户名只能由1-12个字符组成");
		}else{
			$(this).next().text("");
		}
	}).blur(function(){
		var username = $("#username").val().trim();
		var reg = /[^\w\u4e00-\u9fa5]/g; 
		if(username == ""){
			$(this).next().text("用户名不能为空!");
		}else if( reg.test(username)){
			$(this).next().text("昵称只能由中文，英文，数字和下划线组成");
		}else if(username.length>12){
			$(this).next().text("用户名只能由1-12个字符组成");
		}else{
			$(this).next().text("");
		}
	});

	$("#r_password").keyup(function(){
		var password = $("#r_password").val().trim();
		var repassword = $("#r_repassword").val().trim();  
		var reg = /[^\da-zA-Z]/g; 
		if(repassword!=""){
			var password = $("#r_password").val().trim();
			var repassword = $("#r_repassword").val().trim(); 
			if(repassword == ""){
				$("#r_repassword").next().text("密码不能为空!");
			}else if(repassword!=password){
				$("#r_repassword").next().text( "两次输入的密码不一致!");
			}else{
				$("#r_repassword").next().text("");
			};
		}
		if(password == ""){
			$(this).next().text("密码不能为空!");
		}else if( reg.test(password)){
			$(this).next().text("密码只能由数字，英文组成!");
		}else if(password.length>15||password.length<6){
			$(this).next().text("密码只能有6-15位");
		}else{
			$(this).next().text("");
		}
	}).blur(function(){
		var password = $("#r_password").val().trim();
		if(password == ""){
			$(this).next().text("密码不能为空!");
		}
	});

	$("#r_repassword").keyup(function(){
		var password = $("#r_password").val().trim();
		var repassword = $("#r_repassword").val().trim(); 
		if(repassword == ""){
			$(this).next().text("密码不能为空!");
		}else if(repassword!=password){
			$(this).next().text("两次输入的密码不一致!");
		}else{
			$(this).next().text( "");
		}
	}).blur(function(){
		var repassword = $("#r_repassword").val().trim();
		if(repassword == ""){
			$(this).next().text("确认密码不能为空!");
		}
	});

	$("#r_answer").keyup(function(){
		var answer = $("#r_answer").val().trim();
		var reg = /[^\u4e00-\u9fa5\da-zA-Z]/g; 
		if(answer == ""){
			$(this).next().text("密保答案不能为空!");
		}else if( reg.test(answer)){
			$(this).next().text("密保答案只能为中文、英文、数字!");
		}else if(answer.length>12){
			$(this).next().text("密保答案只能由1-12个中文、英文、数字组成!");
		}else{
			$(this).next().text("");
		}
	}).blur(function(){
		var answer = $("#r_answer").val().trim();
		var reg = /[^\u4e00-\u9fa5\da-zA-Z]/g;  
		if(answer == ""){
			$(this).next().text("密保答案不能为空!");
		}else if( reg.test(answer)){
			$(this).next().text("密保答案只能为中文、英文、数字!");
		}else if(answer.length>12){
			$(this).next().text("密保答案只能由1-12个中文、英文、数字组成!");
		}else{
			$(this).next().text("");
		}
	});

	$("#reg").click(function(){
		var username = $("#username").val().trim();
		var password = $("#r_password").val().trim();
		var repassword = $("#r_repassword").val().trim();
		var question = $("#register .question").val().trim(); 
		var answer = $("#r_answer").val().trim();
	
		var info = {
			userName : username,
			password : password,
			userSecretId : question,
			userSecretAnswer : answer,
		}
		var p = $("#username").next().text().trim()+
				$("#r_password").next().text().trim()+
				$("#r_repassword").next().text().trim()+
				$("#r_answer").next().text().trim();

		if(p!=""||username==""||password==""||repassword==""||answer==""){
			alert("填写格式错误!");
		}else{
			$.post("http://"+IP+":8080/QGzone/UserSignUp",{jsonObject:JSON.stringify(info)},function(data){
				 var state = data.state;
				 console.log(data);
				 if(state==null){
				 	alert("服务器异常!");
				 	return ;
				 }
				 if(state=="101"){
				 	var userId = data.userId;
				 	alert("注册成功,您的账号为:"+userId);
				 	return ;
				 }
				 if(state=="102"){
				 	alert("注册失败!");
				 	return ;
				 }
			},"json");
		}
	});


//forget password----------------------------------------------------------------

	$("#f_account").keyup(function(){
		var account = $("#f_account").val().trim();
		var reg = /[^\d{5,8}]/; 
		if(account == ""){
			$(this).next().text("账号不能为空!");
		}else if(account.length>8 || account.length<5|| reg.test(account)){
			$(this).next().text("账号格式不正确!");
		}else{
			$(this).next().text("");
		}
	}).blur(function(){
		var account = $("#f_account").val().trim();
		var reg = /[^\d{5,8}]/; 
		if(account == ""){
			$(this).next().text("账号不能为空!");
		}else if(account.length>8 || account.length<5|| reg.test(account)){
			$(this).next().text("账号格式不正确!");
		}else{
			$(this).next().text("");
		}
	});

	$("#f_answer").keyup(function(){
		var answer = $("#f_answer").val().trim();
		var reg = /[^\u4e00-\u9fa5\da-zA-Z]/g; 
		if(answer == ""){
			$(this).next().text("密保答案不能为空!");
		}else if( reg.test(answer)){
			$(this).next().text("密保答案只能为中文、英文、数字!");
		}else if(answer.length>12){
			$(this).next().text("密保答案只能由1-12个中文、英文、数字组成!");
		}else{
			$(this).next().text("");
		}
	}).blur(function(){
		var answer = $("#f_answer").val().trim();
		var reg = /[^\u4e00-\u9fa5\da-zA-Z]/g; 
		if(answer == ""){
			$(this).next().text("密保答案不能为空!");
		}else if( reg.test(answer)){
			$(this).next().text("密保答案只能为中文、英文、数字!");
		}else if(answer.length>12){
			$(this).next().text("密保答案只能由1-12个中文、英文、数字组成!");
		}else{
			$(this).next().text("");
		}
	});

	$("#f_password").keyup(function(){
		var password = $("#f_password").val().trim();
		var repassword = $("#f_repassword").val().trim();  
		var reg = /[^\da-zA-Z]/g; 
		if(repassword!=""){
			var password = $("#f_password").val().trim();
			var repassword = $("#f_repassword").val().trim(); 
			if(repassword == ""){
				$("#f_repassword").next().text("密码不能为空!");
			}else if(repassword!=password){
				$("#f_repassword").next().text( "两次输入的密码不一致!");
			}else{
				$("#f_repassword").next().text("");
			};
		}
		if(password == ""){
			$(this).next().text("密码不能为空!");
		}else if( reg.test(password)){
			$(this).next().text("密码只能由数字，英文组成!");
		}else if(password.length>15||password.length<6){
			$(this).next().text("密码只能有6-15位");
		}else{
			$(this).next().text("");
		}
	}).blur(function(){
		var password = $("#f_password").val().trim();
		if(password == ""){
			$(this).next().text("密码不能为空!");
		}
	});

	$("#f_repassword").keyup(function(){
		var password = $("#f_password").val().trim();
		var repassword = $("#f_repassword").val().trim(); 
		if(repassword == ""){
			$(this).next().text("密码不能为空!");
		}else if(repassword!=password){
			$(this).next().text("两次输入的密码不一致!");
		}else{
			$(this).next().text( "");
		}
	}).blur(function(){
		var repassword = $("#f_repassword").val().trim();
		if(repassword == ""){
			$(this).next().text("确认密码不能为空!");
		}
	});

	$("#sure").click(function(){
		var account = $("#f_account").val().trim();
		var password = $("#f_password").val().trim();
		var repassword = $("#f_repassword").val().trim();
		var question = $("#forgetPassword .question").val().trim(); 
		var answer = $("#f_answer").val().trim();

		var info = {
			userId : account,
			newPassword : password,
			oldSecretId : question,
			oldAnswer : answer,
		}
		var p = $("#f_account").next().text().trim()+
				$("#f_password").next().text().trim()+
				$("#f_repassword").next().text().trim()+
				$("#f_answer").next().text().trim();

		if(p!=""||account==""||password==""||repassword==""||answer==""){
			alert("填写格式错误!");
		}else{
			$.post("http://"+IP+":8080/QGzone/UserForgetPassword",{jsonObject:JSON.stringify(info)},function(data){
					var state = data.state;
					 console.log(data);
					if(state==null){
						alert("服务器异常!");
						return ;
					}
					if(state=="121"){
						alert("修改密码成功，请放回登录界面登录!");
						return ;
					}
					if(state=="122"){
						alert("操作失败，数据填入错误!");
						return ;
					}
			},"json");
		}
	});
})