 /* 留言板留言：noteAdd( 或na )
 * 留言评论类型：noteComment( 或 nc )
 * 说说回复类型：twitteComment( 或 tc )
 * 点赞类型：supportTwitter( 或st )
 * 好友添加：friendApply( 或 fa )
 */ 

$(function(){
	var IP = "192.168.1.109";
	var HEAD_IMG_URL = "/QGzone/jpg/";
	var TWITTER_URL = "twitter.html";
	var NOTE_URL = "note.html";

	var userId;
	var img_url = "../images/head.jpg";
	var userName="";
	
	$.post("http://"+IP+":8080/QGzone/MessageGet",function(data){
		console.log(data);
		var state = data.state;
		if(state == undefined){
			alert("服务器异常!");
		}
		if(state == "161"){
			var message = data.message;
			userId = message.userId;
		 	img_url = HEAD_IMG_URL + message.userImage;
			userName = message.userName;
			$("#diamonds_1").attr("src",img_url);
			$("#h_photo").find("img").attr("src",img_url);
			$("#nevRight p").text(userName);
			$("#personInformation").find("a").attr("href","http://"+IP+":8080/QGzone/html/personInformation.html?userId="+userId);
		}
		if(state == "162"){
			alert("获取个人资料失败,请稍后重试!");
		}
	},"json");

	

	
//添加信息--------------------------------------------------------------		
	function addInforation(name, h_content, time, relationId, c_content, anchor_url,relationType){
		var $li = $('<li class="info" class="st" relationId='+relationId+'>'+
						'<div class="info_header">'+
							'<img class="friend_head_img" src="../images/head.jpg">'+
							'<div class="rough_info">'+
							    '<span class="name">'+name+'</span><span class="h_content">'+h_content+'</span>'+
								'<p class="time">'+time+'</p>'+
							'</div>'+
							'<div class="button">'+
								'<a href="#" class="delete">删除信息</a>'+
							'</div>'+
						'</div>'+
					'</li>');
		if(c_content!=undefined){
			$li.append($('<div class="info_content"><p class="c_name">'+name+' :</p><p class="c_content">'+c_content+'</p></div>'));
		}
		if(relationType!="fa"){
			$li.find(".button").prepend($('<a href='+anchor_url+'>查看详情</a><br/>'));
		}
		var $ul = $("#info_list");
		$li.appendTo($ul);
	}

	/*addInforation("GQ111","赞了我","2015-16-14","1",undefined,"#123","st");
	addInforation("GQ111","同意了我的好友请求","2015-16-14","2",undefined,"#123","fa");
	addInforation("GQ111","已把你从好友列表移除","2015-16-14","3",undefined,"#123","fa");
	addInforation("GQ111","在说说回复了我","2015-16-14","4","点击覅大家动力科技发来的街坊邻居","#321","tc");
	addInforation("GQ111","在留言板给我留言","2015-16-14","5","点击覅大家动力科技发来的街坊邻居","#123","na");
	addInforation("GQ111","在留言板回复了我","2015-16-14","5","点击覅大家动力科技发来的街坊邻居","#123","nc");*/

//拿到指定页码的数据----------------------------------------------------------------
	function getInfomationByPageNum(pageNum){
		var info={
			page: pageNum.toString()
		}
		var relations;
		$.ajax({
			url:"http://"+IP+":8080/QGzone/RelationsGet",
			async:false,
			data:{jsonObject:JSON.stringify(info)},
			success: function(data){
				data = JSON.parse(data);
				console.log(data);
				var state = data.state;
				if(state == undefined){
					alert("服务器异常!");
				}
				if(state == "401"){
					relations = data.relations;
				}
				if(state == "402"){
					alert("获取与我相关列表失败,请稍后重试!");
				}
			}
		});
		return relations;
	}

//把一整页的数据add进去ul里面-----------------------------------------------------
	function getOnePageInfomatoin(list){
		if(list==undefined||list.length<=0){
    		var $li = $('<li class="not_new_info center">没有数据了~~</li>');
    		$("#info_list").append($li);
		}else{
			for(var i=0 ;i<list.length;i++){
				var sender = list[i].sender;
				var senderName = sender.userName;
				var relationContent = list[i].relationContent;
				var relationType = list[i].relationType;
				var relationId = list[i].relationId;
				var relationTime = format (Date.parse(list[i].relationTime));
				var relatedId = list[i].relatedId;  //详细信息id
				var anchor_url="";
				var h_content = "";

				//添加内容区的内容
				if(relationType=="st"){
					h_content = "赞了我";
				}else if(relationType=="fa"){
					if(relationContent=="同意"){
						h_content = "同意了我的好友请求";
					}
					if(relationContent=="拒绝"){
						h_content = "已把你从好友列表移除";
					}
				}else if(relationType=="tc"){
					h_content = "在说说回复了我";
				}else if(relationType=="na"){
					h_content = "在留言板给我留言";
				}else if(relationType=="nc"){
					h_content = "在留言板回复了我";
				}
				//判断锚的路径
				if(relationType=="st"||relationType=="fa"){
					if(relationType=="st"){
						anchor_url = TWITTER_URL+"#"+relatedId;
					}
					addInforation(senderName, h_content, relationTime, relationId, undefined, anchor_url,relationType);
				}else{
					if(relationType=="tc"){
						anchor_url = TWITTER_URL+"#"+relatedId;
					}else if(relationType=="na"||relationType=="nc"){
						anchor_url = NOTE_URL+"#"+relatedId;
					}
					addInforation(senderName, h_content, relationTime, relationId, relationContent, anchor_url,relationType);
				}
			}
		}
	}
//先自己访问拿到第一页---------------------------------------------------
	var currentPage = 0;
	var list = getInfomationByPageNum(++currentPage);
   	getOnePageInfomatoin(list);
//控制滚动时自己加载内容-----------------------------------------------------
	
	window.onscroll= function(){
        var docHeight = $(document).height();
        var winHeight  = $(window).height();
        var scrollHeight = $(window).scrollTop();
        if($("#info_list").find(".not_new_info").length){
        	return ;
        }
        if(scrollHeight+winHeight>=docHeight-30){
    		var list = getInfomationByPageNum(++currentPage);
    		getOnePageInfomatoin(list);    
        }
    
    }

//删除与我相关数据--------------------------------------------------------
	$("#info_list").click(function(e){
		e = $(e.target);
		if(e[0].nodeName.toUpperCase()=="A"&& e.attr("class")=="delete"){
			var relationId = e.parents("li").attr("relationId");
			var info = {
				relationId : relationId
			}
			$.post("http://"+IP+":8080/QGzone/RelationDelete",{jsonObject :JSON.stringify(info)},function(data){
				console.log(data);
				var state = data.state;
				if(state == undefined){
					alert("服务器异常!");
				}
				if(state == "411"){
					window.reload();
				}
				if(state == "412"){
					alert("删除与我相关信息失败,请稍后重试!");
				}
			},"json")
		}
	})
	
	function format(shijianchuo)
	{
	//shijianchuo是整数，否则要parseInt转换
	var time = new Date(shijianchuo);
	var y = time.getFullYear();
	var m = time.getMonth()+1;
	var d = time.getDate();
	var h = time.getHours();
	var mm = time.getMinutes();
	var s = time.getSeconds();
	return y+'-'+add0(m)+'-'+add0(d)+' '+add0(h)+':'+add0(mm)+':'+add0(s);
	}
	function add0(m){return m<10?'0'+m:m }
	
})