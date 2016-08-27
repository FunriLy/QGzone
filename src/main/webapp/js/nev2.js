$(function(){
	var HEAD_IMG_URL = "/QGzone/jpg/"; // 放置用户头像的文件夹路径;
	var LOGIN_USER_ID;              //登陆人的id
	var URL_USER_ID;                //url上的id

	

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
 
//获取登陆人的id-----------------------------------------------
	$.ajax({
		url:"../MessageGet",
		type:"POST",
		success:function(data){
					console.log("here:"+data);
					var state = data.state;
					if(state == undefined){
						alert("服务器异常!");
					}
					if(state == "161"){
						var message = data.message;
						var userName = message.userName;
						var img_url = message.userImage;
						LOGIN_USER_ID = message.userId;
						$("#circle_2 img").attr("src",HEAD_IMG_URL+img_url)
//为一级导航栏中的url附上用户的id
						var a = $('#nev_1').find('a');
					   	for(var i=0;i<a.length;i++){
					   		
					   		var url =$(a[i]).attr('href');
					   		
					   		url+= '?userId='+LOGIN_USER_ID;
					   		
					   		$(a[i]).attr('href',url);
					   	}
//为页面中二级的url附上当前页面主人的id
					   	var b = $('#nev2').find('a');
					   	for(var i=0;i<b.length;i++){
					   		
					   		var url =$(b[i]).attr('href');
					   		
					   		url+= '?userId='+URL_USER_ID;
					   		
					   		$(b[i]).attr('href',url);
					   	}
//为一级导航栏附上用户名字
						$("#nevRight .p_margin").find("p").text(userName);

//修改操作................................
						if(URL_USER_ID!=LOGIN_USER_ID){

						}
					}
					if(state == "162"){
						alert("获取个人资料失败,请稍后重试!");
					}
				},
		dataType:"json"
	});
	   
	if($("#header")!=undefined){
		$.get("../MessageSearch?userId="+URL_USER_ID,function(data){
	   		$('#header p').text(data.message.userName);
	   		$('#h_photo img').attr('src','../jpg/'+data.message.userImage);
		},'json');
	}
   

//主导航栏的动效
	$("#nevLeft").click(function(e){
		e = $(e.target);
		if(e[0].nodeName.toUpperCase()=="A"||e[0].nodeName.toUpperCase()=="LI"){
			if(e.closest("li").attr("class")!="circle_1_paddingSmall"){
				e.closest("li").addClass("color_white");
				e.closest("li").siblings().removeClass("color_white");
				if(e[0].nodeName.toUpperCase()=="LI"){
					window.location.href=e.find("a").attr("href");
				}
			}
		}
	});
	
//退出登录-----------------------------------------------
	$("#nevRight li").click(function(e){
		e = $(e.target);
		if(e[0].nodeName.toUpperCase()=="A"||e[0].nodeName.toUpperCase()=="IMG"){
			e = e.parents("li");
		}
		if(e.find("img").attr("src")=="../images/c_exit.png"){
			$.get("../UserSignOut",function(){
				window.location.href="login.html";
			})
		}
	});

})