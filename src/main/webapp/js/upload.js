$(function(){
	var array = new Array();
	var IP = "192.168.1.100";
	var hostId;

	sessionStorage["friendId"]="1";
	var friendId = sessionStorage["friendId"];
	console.log(friendId);



//获取url信息
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
	var inf = getUrlInformation();
//通过链接模拟点击input file----------------------------------
	$("#add").click(function(e){
		if($("#input")[0]){
			$("#input").click();
		}
		e.preventDefault();
	});
//显示上传按钮----------------------------------------------
	$("#input").change(function(){
		var fileList = this.files;
		if(fileList.length>0){
			for(var i=0;i<fileList.length;i++){
				if(fileList[i].type.indexOf('image')==-1){
					alert("文件: 《"+ fileList[i].name +"》 不是图片文件,请上传图片格式的文件!");
					continue;
				}
				array.push(fileList[i]);
				showPhoto(fileList[i]);
			}
			console.log(array);
			console.log($("#dropbox").children().length);
			$("#upload").show();
		}
	});
//点击上传按钮上传文件---------------------------------------------
	$("#upload").click(function(){
		var formData = new FormData();
		var haveFile = false;
		for(var i=0; i<array.length; i++){
			if(array[i]!=undefined){
				formData.append("file"+i,array[i]);
				haveFile = true;
			}
		}
		if(haveFile){
			$.ajax({
				url:"http://"+IP+":8080/QGzone/UserUploadImage",
				type:"POST",
				data:formData,
				processData: false,
				contentType: false,
				success:function(data){
					array.length=0;
					$("#dropbox").empty();
					console.log(data);
					alert("上传成功!");
				},
				dataType:"json"
			});
			var asHostId = inf.userId;
			var albmId = inf.albumId;//拿到albumId
			// sessionStorage["isReloadPhoto"]="true";
			var reAlbUrl = "innerAlbums.html?userId="+asHostId+"&albumId="+albmId;
			window.location.href = reAlbUrl;
			// window.location.reload();
			return ;
		}
	});
//删除图片-----------------------------------------------------------
	$("#dropbox").click(function(e){
		var e = $(e.target);
		if(e[0].nodeName.toUpperCase() != "SPAN"){
			return ;
		}
		var filename = e.parent().attr("filename");
		deleteFile(filename);
		e.parent().animate({ width:'0',opacity: '0'},1000,function(){
			e.parent().remove();
			if($("#dropbox").children().length<=1){
				$("#upload").hide();
			}
		});
		
	});
	function deleteFile(filename){
		for(var i=0;i<array.length;i++){
			if(array[i]!=undefined&&array[i].name == filename){
				array[i] = undefined;
				return ;
			}
		}
	}
//图片预览--------------------------------------------------------------
	function showPhoto(file){
		var reader = new FileReader();
		reader.onload = function (e) {
			var dataURL = reader.result;
			var img = $('<div filename='+file.name+' class="file_img"><img src='+dataURL+' /><span></span></div>');
			img.insertBefore($("#add")[0]);
		}
		reader.readAsDataURL(file);
	}	
//图片拖拽---------------------------------------------------------
	var dropbox = $("#dropbox")[0];  
	dropbox.addEventListener("dragenter", dragenter, false);
	dropbox.addEventListener("dragover", dragover, false);
	dropbox.addEventListener("drop", drop, false);   
	
	function dragenter(e) {
		e.stopPropagation();
		e.preventDefault();
	}
	function dragover(e) {
	  	e.stopPropagation();
	  	e.preventDefault();
	}
	function drop(e) {
	  	e.stopPropagation();
	  	e.preventDefault();

		var dt = e.dataTransfer;
		var fileList = dt.files;
		if(fileList.length>0){
			for(var i=0; i<fileList.length; i++){
				if(fileList[i].type.indexOf('image')==-1){
					alert("文件: 《"+ fileList[i].name +"》 不是图片文件,请上传图片格式的文件!");
					continue;
				}
				array.push(fileList[i]);
				showPhoto(fileList[i]);
			}
			$("#upload").show();
		}
	}

	// if(sessionStorage["isReloadPhoto"]=="true"){
	// 	$("#container_1 #change").click();
	// 	$("#c2_left ul :nth-child(2)").click();
	// 	sessionStorage.removeItem("isReloadPhoto");
	// }



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

	//相册内------------------------------------------------------------------------------------------------
	//生成新图片-------------------------------------------------------------------------------------------------
	var photoUrl = "http://"+IP+":8080/QGzone/album";
	function createPhoto(pId,aId,uId){
		var phoThumbnail = "t_"+pId;
		var photo = $('<div class="every_photo" photoId="'+pId+'">'+
 					  	'<div class="delete_btn"><img src="../img/p_list.png"></div>'+
					  	'<div class="photo"><img src="'+photoId+'/'+uId+'/'+aId+'/'+phoThumbnail+'.jpg'+'" class="pho">'+
					  	'</div></div>');
 		var container = $(".contain_photo");
 		container.prepend(photo);
	}
	//上传图片------------------------------------------------------------------------------------------
	$(".btn_loadup").click(function(){
		$(".contain_photo").hide();
		$("#upload_window").show();


	});
	$(".cancel_upload").click(function(){
		$("#upload_window").hide();
		$(".contain_photo").show();
		$(".file_img").remove();
	})
	
	//查看大图------------------------------------------------------------------------------------------
	// $(".photo").click(function(event){
	// 	var tg = $(event.target);
	// 	var pare = tg.parents(".every_photo");
	// 	var picId = pare.attr("photoId");
	// 	alert("我点到了图片哦~~~~~~~");
	// });
	function openNew(){
	//获取页面的高度和宽度
	var sWidth=document.body.scrollWidth;
	var sHeight=document.body.scrollHeight;
	
	//获取页面的可视区域高度和宽度
	var wHeight=document.documentElement.clientHeight;
	
	var oMask=document.createElement("div");
		oMask.id="mask";
		oMask.style.height=sHeight+"px";
		oMask.style.width=sWidth+"px";
		document.body.appendChild(oMask);
	var oEnlarge=document.createElement("div");
		oEnlarge.id="enlarge";
		oEnlarge.innerHTML="<div class='enlarge_con'>"+
				"<div id='close'>点击关闭</div>"+
				"<img src='"+getPho+".jpg'></div>";
		document.body.appendChild(oEnlarge);
	//获取浮出层框的宽和高
	var dHeight=oEnlarge.offsetHeight;
	var dWidth=oEnlarge.offsetWidth;
	//设置浮出层框的left和top
		oEnlarge.style.left=sWidth/2-dWidth/2+"px";
		oEnlarge.style.top=wHeight/2-dHeight/2+"px";
	//点击关闭按钮
	var oClose=document.getElementById("close");
	
		//点击浮出层框以外的区域也可以关闭浮出层框
		oClose.onclick=oMask.onclick=function(){
					document.body.removeChild(oEnlarge);
					document.body.removeChild(oMask);
					};
					};
					
		$(".pho").click(function(event){
			var tg = $(event.target);
			var pare = tg.parents(".every_photo");
			var getPho = pare.attr("photoId");
			console.log("我要打开大图了！"+getPho);
			openNew();
				// return false;
		});
		$("#close").click(function(){
			$("#mask").remove();
			$("#enlarge").remove();
		});


	//删除图片------------------------------------------------------------------------------------------
	$(".delete_btn").hide();
	$(".btn_edit_pho").click(function(){
		$(".delete_btn").toggle();
	});
	var photoInfo = getUrlInformation();
	$(".delete_btn").click(function(event){
		var tg = $(event.target);
		var tgAlbId = photoInfo.albumId;
		var phot = tg.parents(".every_photo");
		var photId = phot.attr("photoId");
		var obj = {
			albumId : tgAlbId,
			photoId : photId
		};
		$.post("http://"+IP+":8080/QGzone/DeletePhoto",obj,function(state){
			if(state==undefined){
				alert("服务器异常");
			}else if(state==602){
				alert("删除图片失败");
			}else if(state==601){
				console.log("删除图片："+obj.photoId);
				phot.remove();
			}
		});
	});


});