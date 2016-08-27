$(function(){
	var array = new Array();
//	var IP = "192.168.43.138";
	var hostId;

//获取url信息---------------------------------------------------------------------------------------------------------------------
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
	hostId = inf.userId;
	var albumId = inf.albumId;
	var status = inf.status;
	var obj = {
		userId : hostId,
		albumId : albumId,
		albumState : status
	};
	console.log("现在这个相册属于："+hostId);
//获取当前用户ID--------------------------------------------------------------------------------------------------------------------------------
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
	
	//生成新图片-------------------------------------------------------------------------------------------------
	var photoUrl = "../album";
	function createPhoto(pId,aId,uId){
		var phoThumbnail = "t_"+pId;
		var photo = $('<div class="every_photo" photoId="'+pId+'">'+
 					  	'<div class="delete_btn"><img src="../images/p_list.png"></div>'+
					  	'<div class="photo"><img src="../album/'+uId+'/'+aId+'/'+phoThumbnail+'.jpg'+'" class="pho">'+
					  	'</div></div>');
 		var container = $("#contain_photo");
 		container.append(photo);
	};
	
//加载页面-----------------------------------------------------------------------------------------
	 function loadPage(){
		if(status==0){
				$.post("../CheckPublicAlbum",{albumId : albumId},function(object){
					console.log(object);
					var data = JSON.parse(object);
					var state = data.state;
					console.log("xxxxxxxxx"+data.jsonList[0]);
					for(var i=data.jsonList.length;i>=0;i--){
						var pId = data.jsonList[i];
						if(pId!=undefined){
							createPhoto(pId,albumId,hostId);
						}else{
							console.log("当前相册为空");
						}
					}
						if(currentId!=hostId){
							$(".function_bar").remove();
						}
				});
		}else if(status==1){
			$.post("../CheckPrivacyAlbum",{jsonObject : JSON.stringify(obj)},function(object){
				var data = JSON.parse(object);
				console.log("sssssssssss"+data.jsonList[0]);
				for(var i=0;i<data.jsonList.length;i++){
					var pId = data.jsonList[i];
					if(pId!=undefined){
						createPhoto(pId,albumId,hostId);
					}else{
						console.log("当前相册为空");
					}
				}
				if(currentId!=hostId){
					$(".function_bar").remove();
				}
			});
		}
	 }
	 loadPage();
	
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
				url:"../UploadPhoto?albumId="+albumId,
				type:"POST",
				data:formData,
				processData: false,
				contentType: false,
				success:function(data){
					array.length=0;
					$("#dropbox").empty();
					console.log(data);
					alert("上传成功!");
					var asHostId = inf.userId;
					var albmId = inf.albumId;//拿到albumId
					$("#upload_window").hide();
					$("#contain_photo").show();
					window.location.reload();
					return ;
					
					
				},
				dataType:"json"
			});
			
		}
	});
//删除未完全上传的图片-----------------------------------------------------------
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

//相册内------------------------------------------------------------------------------------------------
	
	//上传图片------------------------------------------------------------------------------------------
	$(".btn_loadup").click(function(){
		$("#contain_photo").hide();
		$("#upload_window").show();
	});
	
	$(".cancel_upload").click(function(){
		$("#upload_window").hide();
		$("#contain_photo").show();
		$(".file_img").remove();
	});
	
	//查看大图------------------------------------------------------------------------------------------
	 function center(obj){
         var width = $('body').width();
         $(obj).css({   
          "position": "absolute",   
          "left": width/2-200,
          "top":  '160px'   
         });  
        }

	function openNew(hostId,albumId,getPho){
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
		oEnlarge.innerHTML="<div id='enlarge_con'>"+
				"<div id='close'>点击关闭</div>"+
				"<img src='../album/"+hostId+"/"+albumId+"/"+getPho+".jpg'></div>";
	document.body.appendChild(oEnlarge);
	//获取浮出层框的宽和高
	var dHeight=oEnlarge.offsetHeight;
	var dWidth=oEnlarge.offsetWidth;
	//设置浮出层框的left和top
		oEnlarge.style.left=sWidth/5-dWidth/5+"px";
		oEnlarge.style.top=wHeight/5-dHeight/5+"px";
	//点击关闭按钮
	var oClose=document.getElementById("close");
	
		//点击浮出层框以外的区域也可以关闭浮出层框
		oClose.onclick=oMask.onclick=function(){
					document.body.removeChild(oEnlarge);
					document.body.removeChild(oMask);
					};
	};
					
	$(document).on("click",".pho",function(event){
		var tg = $(event.target);
		var pare = tg.parents(".every_photo");
		var getPho = pare.attr("photoId");
		console.log("我要打开大图了！"+getPho);
//		alert("hostId="+hostId+"---"+"albumId="+albumId+"---"+"getPho="+getPho);
		openNew(hostId,albumId,getPho);
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
	$(document).on("click",".delete_btn",function(event){
		console.log("我准备删除相片啦");
		var tg = $(event.target);
		var tgAlbId = photoInfo.albumId;
		var phot = tg.parents(".every_photo");
		var photId = phot.attr("photoId");
		var obj = {
			albumId : tgAlbId,
			photoId : photId
		};
		$.post("../DeletePhoto",{photo : JSON.stringify(obj)},function(object){
			var data = JSON.parse(object);
			var state = data.state;
			console.log("你先拿到"+data+"然后再拿到state"+state);
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