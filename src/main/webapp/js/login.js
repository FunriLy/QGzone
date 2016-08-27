$(document).ready(function(){

	var lo = $($("#option").children()[0]);
	var ro = $($("#option").children()[2]);
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
		ro.removeClass("be_select");
		$(this).addClass("be_select");
	});

	ro.click(function(){
		register.show();
		login.hide();
		forget.hide();
		lo.removeClass("fade");
		ro.removeClass("fade");
		lo.removeClass("be_select");
		$(this).addClass("be_select");
		$("#username").val("");
		$("#r_password").val("");
	});

	fg.click(function(){
		forget.show();
		register.hide();
		login.hide();
		lo.removeClass("be_select");
		lo.addClass("fade");
		ro.addClass("fade");
	});


// login 验证；---------------------------------------------------------------------

	$("#l_account").keyup(function(){
		var account = $("#l_account").val().trim();
		var reg = /[^\d{5,8}]/; 
		if(account == ""){
			$(this).parent().find("p").text("账号不能为空!");
		}else if(account.length>8 || account.length<5|| reg.test(account)){
			$(this).parent().find("p").text("账号格式不正确!");
		}else{
			$(this).parent().find("p").text("");
		}
	}).blur(function(){
		var account = $("#l_account").val().trim();
		var reg = /[^\d{5,8}]/; 
		if(account == ""){
			$(this).parent().find("p").text("账号不能为空!");
		}else if(account.length>8 || account.length<5|| reg.test(account)){
			$(this).parent().find("p").text("账号格式不正确!");
		}else{
			$(this).parent().find("p").text("");
		}
	});

	$("#l_password").keyup(function(){
		var password = $("#l_password").val().trim();
		if(password ==""){
			$(this).parent().find("p").text("密码不能为空!");
		}else{
			$(this).parent().find("p").text("");
		}
	}).blur(function(){
		var password = $("#l_password").val().trim();
		if(password ==""){
			$(this).parent().find("p").text("密码不能为空!");
		}else{
			$(this).parent().find("p").text("");
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
		var p = $("#l_password").parent().find("p").text().trim()+
				$("#l_account").parent().find("p").text().trim();
		if(p!=""||account==""||password==""){
			alert("填写格式错误!");
		}else{
			$.post("../UserSignIn",{jsonObject:JSON.stringify(info)},function(data){
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
				 	window.location.href="selfIndex.html?userId="+userId;
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
			$(this).parent().find("p").text("用户名不能为空!");
		}else if( reg.test(username)){
			$(this).parent().find("p").text("昵称只能由中文，英文，数字和下划线组成");
		}else if(username.length>12){
			$(this).parent().find("p").text("用户名只能由1-12个字符组成");
		}else{
			$(this).parent().find("p").text("");
		}
	}).blur(function(){
		var username = $("#username").val().trim();
		var reg = /[^\w\u4e00-\u9fa5]/g; 
		if(username == ""){
			$(this).parent().find("p").text("用户名不能为空!");
		}else if( reg.test(username)){
			$(this).parent().find("p").text("昵称只能由中文，英文，数字和下划线组成");
		}else if(username.length>12){
			$(this).parent().find("p").text("用户名只能由1-12个字符组成");
		}else{
			$(this).parent().find("p").text("");
		}
	});

	$("#r_password").keyup(function(){
		var password = $("#r_password").val().trim();
		var reg = /[^\da-zA-Z]/g; 
		if(password == ""){
			$(this).parent().find("p").text("密码不能为空!");
		}else if( reg.test(password)){
			$(this).parent().find("p").text("密码只能由数字，英文组成!");
		}else if(password.length>15||password.length<6){
			$(this).parent().find("p").text("密码只能有6-15位");
		}else{
			$(this).parent().find("p").text("");
		}
	}).blur(function(){
		var password = $("#r_password").val().trim();
		if(password == ""){
			$(this).parent().find("p").text("密码不能为空!");
		}
	});

	$("#r_answer").keyup(function(){
		var answer = $("#r_answer").val().trim();
		var reg = /[^\u4e00-\u9fa5\da-zA-Z]/g; 
		if(answer == ""){
			$(this).parent().find("p").text("密保答案不能为空!");
		}else if( reg.test(answer)){
			$(this).parent().find("p").text("密保答案只能为中文、英文、数字!");
		}else if(answer.length>12){
			$(this).parent().find("p").text("密保答案只能由1-12个中文、英文、数字组成!");
		}else{
			$(this).parent().find("p").text("");
		}
	}).blur(function(){
		var answer = $("#r_answer").val().trim();
		var reg = /[^\u4e00-\u9fa5\da-zA-Z]/g;  
		if(answer == ""){
			$(this).parent().find("p").text("密保答案不能为空!");
		}else if( reg.test(answer)){
			$(this).parent().find("p").text("密保答案只能为中文、英文、数字!");
		}else if(answer.length>12){
			$(this).parent().find("p").text("密保答案只能由1-12个中文、英文、数字组成!");
		}else{
			$(this).parent().find("p").text("");
		}
	});

	$("#reg").click(function(){
		var username = $("#username").val().trim();
		var password = $("#r_password").val().trim();
		var question = $("#register .question").val().trim(); 
		var answer = $("#r_answer").val().trim();
	
		var info = {
			userName : username,
			password : password,
			userSecretId : question,
			userSecretAnswer : answer,
		}
		var p = $("#username").parent().find("p").text().trim()+
				$("#r_password").parent().find("p").text().trim()+
				$("#r_answer").parent().find("p").text().trim();

		if(p!=""||username==""||password==""||answer==""){
			alert("填写格式错误!");
		}else{
			$.post("../UserSignUp",{jsonObject:JSON.stringify(info)},function(data){
				 var state = data.state;
				 console.log(data);
				 if(state==null){
				 	alert("服务器异常!");
				 	return ;
				 }
				 if(state=="101"){
				 	var userId = data.userId;
				 	alert("注册成功,您的账号为:"+userId);
				 	lo.click();
				 	$("#l_account").val(userId);
				 	$("#l_password").val("");
				 	$("#l_account")[0].focus();
				 	$("#l_password")[0].focus();
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
			$(this).parent().find("p").text("账号不能为空!");
		}else if(account.length>8 || account.length<5|| reg.test(account)){
			$(this).parent().find("p").text("账号格式不正确!");
		}else{
			$(this).parent().find("p").text("");
		}
	}).blur(function(){
		var account = $("#f_account").val().trim();
		var reg = /[^\d{5,8}]/; 
		if(account == ""){
			$(this).parent().find("p").text("账号不能为空!");
		}else if(account.length>8 || account.length<5|| reg.test(account)){
			$(this).parent().find("p").text("账号格式不正确!");
		}else{
			$(this).parent().find("p").text("");
		}
	});

	$("#f_answer").keyup(function(){
		var answer = $("#f_answer").val().trim();
		var reg = /[^\u4e00-\u9fa5\da-zA-Z]/g; 
		if(answer == ""){
			$(this).parent().find("p").text("密保答案不能为空!");
		}else if( reg.test(answer)){
			$(this).parent().find("p").text("密保答案只能为中文、英文、数字!");
		}else if(answer.length>12){
			$(this).parent().find("p").text("密保答案只能由1-12个中文、英文、数字组成!");
		}else{
			$(this).parent().find("p").text("");
		}
	}).blur(function(){
		var answer = $("#f_answer").val().trim();
		var reg = /[^\u4e00-\u9fa5\da-zA-Z]/g; 
		if(answer == ""){
			$(this).parent().find("p").text("密保答案不能为空!");
		}else if( reg.test(answer)){
			$(this).parent().find("p").text("密保答案只能为中文、英文、数字!");
		}else if(answer.length>12){
			$(this).parent().find("p").text("密保答案只能由1-12个中文、英文、数字组成!");
		}else{
			$(this).parent().find("p").text("");
		}
	});

	$("#f_password").keyup(function(){
		var password = $("#f_password").val().trim();
		var repassword = $("#f_repassword").val().trim();  
		var reg = /[^\da-zA-Z]/g; 
		if(password == ""){
			$(this).parent().find("p").text("密码不能为空!");
		}else if( reg.test(password)){
			$(this).parent().find("p").text("密码只能由数字，英文组成!");
		}else if(password.length>15||password.length<6){
			$(this).parent().find("p").text("密码只能有6-15位");
		}else{
			$(this).parent().find("p").text("");
		}
	}).blur(function(){
		var password = $("#f_password").val().trim();
		if(password == ""){
			$(this).parent().find("p").text("密码不能为空!");
		}
	});

	$("#sure").click(function(){
		var account = $("#f_account").val().trim();
		var password = $("#f_password").val().trim();
		var question = $("#forgetPassword .question").val().trim(); 
		var answer = $("#f_answer").val().trim();

		var info = {
			userId : account,
			newPassword : password,
			oldSecretId : question,
			oldAnswer : answer,
		}
		var p = $("#f_account").parent().find("p").text().trim()+
				$("#f_password").parent().find("p").text().trim()+
				$("#f_answer").parent().find("p").text().trim();

		if(p!=""||account==""||password==""||answer==""){
			alert("填写格式错误!");
		}else{
			$.post("../UserForgetPassword",{jsonObject:JSON.stringify(info)},function(data){
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

//眼睛------------------------------------------------------
	var initInput;
	$(".form .eye").mousedown(function(){
		var val = $(this).parent().find("input").val();
		var placeholder = $(this).parent().find("input").attr("placeholder");
		var id = $(this).parent().find("input").attr("id");
		var text = $('<input type="text" id="'+id+'" class="search" value="'+val+'" placeholder="'+placeholder+'" maxlength="16">');
		initInput = $(this).prev().detach();
		text.prependTo($(this).parent());
	}).mouseup(function(){
		$(this).parent().find("input").remove();
		initInput.prependTo($(this).parent());
	});

//回车提交表单----------------------------------------------
	 $("body").keydown(function(event) {
		var button = $(".form").parent("div:visible").find("input[type=button]");
	    if (event.keyCode == "13") {//keyCode=13是回车键
	         button.click();
		}
	 });
});