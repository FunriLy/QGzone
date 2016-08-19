	$(function(){
		
		
//		/*********************请求获取用户信息***************************/
		   $.get('../MessageGet',function(json){
			   
			   userId = json.message.userId;
			   userName = json.message.userName;
			   userPhoto = '../jpg/'+json.message.userImage;
			   
			   	//为一级导航栏中的url附上用户的id
			   	var a = $('#nev_1').find('a');
			   	for(var i=0;i<a.length;i++){
			   		
			   		var url =$(a[i]).attr('href');
			   		
			   		url+= '?userId='+userId;
			   		
			   		$(a[i]).attr('href',url);
			   		}
			   	//为页面中二级的url附上当前页面主人的id
			   	
			   	var b = $('#nev2').find('a');
			   	
			   	for(var i=0;i<b.length;i++){
			   		
			   		var url =$(b[i]).attr('href');
			   		
			   		url+= '?userId='+currentId;
			   		
			   		$(b[i]).attr('href',url);}
			   	
			    //设置一级头像 	
			   	$.get('../MessageSearch?userId='+userId,function(json){
			   		
			   		currentName=json.message.userName;

			   		
			   		$('#nevRight li:eq(3) p').html(json.message.userName);
			   		
			   		
			   		$('#diamonds_1 img').attr('src','../jpg/'+json.message.userImage);
			   		
			   	},'json');
			   
		   },'json');		
		
		
//		   //从当前页面获取当前页面的主人的id的函数
//   
     function GetQueryString(name)
		{
			     var reg= new RegExp("(^|&)"+name +"=([^&]*)(&|$)");
			     var r = window.location.search.substr(1).match(reg);
			     if(r!=null){
			    	 
			    	 return  unescape(r[2]);
			     }
			     		return null;
			}
      	var currentId = GetQueryString('userId');
      	
      	//设置主头像
	   	$.get('../MessageSearch?userId='+currentId,function(json){
	   		
	   		$('#h_photo img').attr('src','../jpg/'+json.message.userImage);
	   		
	   	},'json');	
   	
/*************************************************************************************/		
        //获取时间格式函数
        function formateDate(date) {

            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            var h = date.getHours();
            var mi = date.getMinutes();

            m = (m>9) ? m : ('0'+m);

            mi = (mi>9)?m:('0'+mi);

            return y + '-' + m + '-' + d + ' ' + h + ':' + mi;

        }
        
 /************************************************************获取留言列表********************************************************************/
        
        //判断是否是自己写的留言
        function isMine(m_status){
            var close = '';
            if(m_status==true){
                close += '<a href="javascript:;" class="close">x</a>';
                return close;
            }else{
                return close;
            }
        }
        
        //生成留言评论的函数
        function showNoteComment(commenterid,commentername,commenterphoto,commentid,targetname,commentcontent,commentdate,com_status){

                var commentbox = document.createElement('div');

                commentbox.className = 'comment-box clearfix';
                commentbox.id = commentid;
                commentbox.setAttribute("commenterid",commenterid);
                commentbox.setAttribute("commentername",commentername);

                commentbox.innerHTML =               
                        '<a href="mainPage.jsp?='+commenterid+'"><img class="myhead" src='+commenterphoto+' alt=""/>'+
                        '<div class="comment-content">'+
                        '<p class="comment-text"><span class="user">'+commentername+' </span><span>回复'+
                               ' '+targetname+' : '+commentcontent+
                            '</span></p>'+
                            '<p class="comment-time">'+commentdate+
    (com_status==false?'<a href="javascript:;" class="comment-operate">回复</a>':'<a href="javascript:;"'+ 
    'class="comment-operate">删除</a>' )+
                            '</p>'+
                        '</div>';
                
                return commentbox;
        }     
        
        //生成留言的函数
        function showNote(notemanid,noteid,h_photo,notemanname,notecontent,notedate,m_status){
            
            var box = document.createElement('div');

            box.className = 'box clearfix';

            box.setAttribute("notemanid", notemanid);
            box.setAttribute("notemanname", notemanname);
            box.id = noteid;

            box.innerHTML = 
            
            	'<a href="selfIndex.html?='+notemanid+'"><img src="'+h_photo+'" class="head" /></a>' + 
                    isMine(m_status)+
                '<div class="content">'+
                    '<div class="main">'+
                        '<p class="txt">'+
                        '<span class="user">'+notemanname+'</span><span>'+notecontent+
                        '</span></p>'+
                    '</div>'+
                    '<div class="info clearfix">'+
                        '<span class="time">'+notedate+'</span></div>'+
                        '<div class="comment-list"></div>'+
                        '<div class="text-box">'+
                        '<textarea class="comment" autocomplete="off">评论…</textarea>'+
                        '<button class="btn">回 复</button>'+
                        '<span class="word"><span class="length">0</span>/50</span>'+
                        '</div>';
                    
            	return box;
        }   
        
        //获取留言列表的函数
        
        function getNoteList(pageNum,currentId){

            $.get("../NoteOfOthers?page="+pageNum,{userId:currentId},function(json){
        
            if(json.state==501){
                        
                var jsonList = json.jsonList;
                
                totalPage = json.totalPage;

                    for(var i = 0; i<jsonList.length;i++){
                    
                    var myState,comState;
                    
                    var headPhoto = '../jpg/'+jsonList[i].noteManId+'.jpg';
                    
                    if(userId==jsonList[i].noteManId){
                    	
                        myState = true;
                    }else{
                        myState = false;
                    };

                    var box = showNote(jsonList[i].noteManId,jsonList[i].noteId,headPhoto,jsonList[i].noteManName,jsonList[i].note,jsonList[i].noteTime,myState);
                    
                    $("#list").append(box);

                    var comments = jsonList[i].comment;

                        for( var k = 0;k<comments.length;k++){
                            
                            if(userId == comments[k].commenterId){
                                comState = true;
                            }else{
                                comState =false;
                            }
        
                            var commenterPicUrl = '../jpg/'+comments[k].commenterId+'.jpg';
                            
                            var commentbox = showNoteComment(comments[k].commenterId,comments[k].commenterName,
                                    commenterPicUrl,
                                    comments[k].commentId,comments[k].targetName,comments[k].comment,
                                    comments[k].noteCommenttime,comState);

                            
                            $('#'+jsonList[i].noteId+' .comment-list').prepend(commentbox);
                        }
                
                }
                
                
                updateEvent();
            }else if(json.state==502){

                alert("加载失败，请重新刷新!");

            }
        },"json");

     }
        
        
        var currentPage = 1;
        var totalPage = 0;
        getNoteList(currentPage,currentId);

        window.onscroll= function(){

            var docHeight = $(document).height();
            var winHeight  = $(window).height();
            var scrollHeight = $(window).scrollTop();

            if(scrollHeight+winHeight<=docHeight-30){
            	if(currentPage<=totalPage){
            		 getNoteList(++currentPage,currentId);
            	}else{
            		return ;
            	}
               

            }
        
        }
        
/************************************************************发表留言*******************************************************************************/
        
	    //发表框keyup事件
	        $('#create-note .txt').keyup(function(){

	            var len = this.value.length;

	            if(len>120||len<=0){

	                $('.create-btn')[0].className = 'create-btn btn-off';
	                
	                $('.create-btn').attr("disabled", true);

	            }else{

	                $('.create-btn')[0].className = 'create-btn';

	                $('.create-btn').removeAttr("disabled");

	            }
	            
	            $('#create-note .word').html(len+'/120');
	            
	        })
	        
	     //发表留言
	        function createNote(userid,username,userphoto,noteid){
	        	
	        	var note = $('#create-note .txt').val();
	        	var box = document.createElement('div');
	        	box.className = 'box clearfix';
	        	
	            box.setAttribute("notemanid", userid);
	            box.setAttribute("notemanname",username);
	            box.id = noteid;
	        	
	        	box.innerHTML = 
	        		
	        		'<a href="selfIndex.html?='+notemanid+'"><img src="'+h_photo+'" class="head" /></a>'+
	                '<a href="javascript:;" class="close">x</a>'+
	                '<div class="content">'+
	                    '<div class="main">'+
	                        '<p class="txt">'+
	                        '<span class="user">'+username+'</span><span>'+note+
	                        '</span></p>'+
	                    '</div>'+
	                    '<div class="info clearfix">'+
	                        '<span class="time">'+formateDate(new Date())+'</span></div>'+
	                        '<div class="comment-list"></div>'+
	                        '<div class="text-box">'+
	                        '<textarea class="comment" autocomplete="off">评论…</textarea>'+
	                        '<button class="btn ">回 复</button>'+
	                        '<span class="word"><span class="length">0</span>/120</span>'+
	                        '</div>';
	        	
	        	$('#list').prepend(box);
	        	
	        	$('#create-note .txt').val('');
	        	updateEvent();
	        }
	        
	        //触发发表留言的事件
	        
	        $("#create-note .create-btn").click(function(){
	        	
	        	var note = $('#create-note .txt').val();
	        	
	        	$.post('../NoteAdd',{targetId:currentId,note:note},function(json){
	        		
	        		if(json.state==501){
	        			
	        			createNote(userId,userName,userPhoto,json.id);
	        			
	        		}else{
	        			
	        			alert("请刷新重试!");
	        			
	        		}
	  
	        	},'json');
	        	
	        })
	 
/*********************************************************************对留言的操作*****************************************************************/
	        
	        //删除节点
	        function removeNode(node){
	        	
	        	if(confirm('确定删除吗？')){
	        		
	        		$(node).remove();
	        		
	        	}	
	        }
	        
	        //触发删除留言的函数
	        function removeNote(){
	        	
	        	$('.box .close').click(function(){
	        		
	        		var box = $(this).parent('div.box.clearfix');
        			var noteId = box.attr('id');
        			
	        		$.post('../NoteDelete',{noteId:noteId},function(json){
	        			
	        			if(json.state==501){
	        				removeNode(box);
	        			}else if(json.state==502){
	        				alert('删除失败，请刷新重试！');
	        			}
	        		},'json')
	        		
	        	});
	        	
	        }
/********************************************************关于评论***************************************************************************/	        
	       
	        
       //操作评论的函数
	        
	        function operate(el) {

	            var commentBox = el.parentNode.parentNode.parentNode;
	            var box = commentBox.parentNode.parentNode.parentNode;
	            var txt = el.innerHTML;

	            var targetid = commentBox.getAttribute("commenterid");
	            var targetname = commentBox.getAttribute("commentername");
	            var commentid = commentBox.id;


	            var user = commentBox.getElementsByClassName('user')[0].innerHTML;
	            var textarea = box.getElementsByClassName('comment')[0];


	            if (txt == '回复') {
	            	
	                textarea.focus();
	                var replyBtn = box.getElementsByClassName("btn");
	                box.setAttribute("targetname", targetname);
	                box.setAttribute("targetid", targetid);
               
	                  textarea.onkeyup();
	                 $(replyBtn).addClass('replybtn');
	            }
	            else {
	            	  //删除自己的评论
	                var commentbox = removeNode(commentBox);
	              //向后台发送请求
	              $.post('../NoteCommentDelete',{commentId: commentid},function(json){

	              },'json');

	            }

	        }

	        
	        
	        //评论框的操作函数
	        function commentInput(){
	        	
	        	var boxs = document.getElementsByClassName("box");
	        	
	        	for(var i = 0;i<boxs.length;i++){
	        		
	        		var textArea = boxs[i].getElementsByClassName('comment')[0];
	        		
	                //评论获取焦点
	                textArea.onfocus = function () {
	                	
	                    this.parentNode.className = 'text-box text-box-on';
	                    this.value = this.value == '评论…' ? '' : this.value;
	                    this.onkeyup();
	                    
	                }

		            //评论失去焦点
		                textArea.onblur = function () {
		                	
		                    var me = this;
		                    var val = me.value;
		                    if (val == '') {
		                        timer = setTimeout(function () {
		                            me.value = '评论…';
		                            me.parentNode.className = 'text-box';
		                        }, 200);
		                    }
		                }

		             //评论按键事件
		                textArea.onkeyup = function(){
	
		                    var val = this.value;
		                    var len = val.length;
		                    var els = this.parentNode.children;
		                    var btn = els[1];
		                    var word = els[2];
		                    if (len > 50) {
		                        $(btn).addClass('btn-off');
		                    }
		                    else {
		                    	$(btn).removeClass('btn-off');
		                    }
		                    word.innerHTML = len + '/50';
		                }
		                
		                
		                //输入框失焦
		                $(boxs[i]).children('input.btn.btn-off').click(function(){
		                	textArea.onblur();
		                })

	        	}
	        		
	      }
/********************************************************评论的实现******************************************************************************/	        
	        //评论函数
	        function reply(box,el,userid,userphoto,username) {

	                var commentList = box.getElementsByClassName('comment-list')[0];
	                var textarea = box.getElementsByClassName('comment')[0];

	                var commentBox = document.createElement('div');
	                commentBox.className = 'comment-box clearfix';
	              
	                commentBox.setAttribute("commenterid",userid)
	                commentBox.setAttribute("commentername",username)

	                commentBox.innerHTML =
	                    '<img class="myhead" src='+userphoto+' alt=""/>' +
	                        '<div class="comment-content">' +
	                        '<p class="comment-text"><span class="user">'+username+' </span>' + 
	                        '回复 '+box.getAttribute("notemanname")+' : '+
	                        textarea.value + '</p>' +
	                        '<p class="comment-time">' +
	                        formateDate(new Date()) +
	                        '<a href="javascript:;" class="comment-operate">删除</a>' +
	                        '</p>' +
	                        '</div>';


	                commentList.appendChild(commentBox);
	                textarea.value = '';
	                textarea.onblur();

	                return commentBox;
	            }

	      //回复评论函数
	        function commentReply(box,el,userid,userphoto,username) {

	                var commentList = box.getElementsByClassName('comment-list')[0];
	                var textarea = box.getElementsByClassName('comment')[0];
	              

	                var commentBox = document.createElement('div');
	                commentBox.className = 'comment-box clearfix';
	              
	                commentBox.setAttribute("commenterid",userid)
	                commentBox.setAttribute("commentername",username)

	                commentBox.innerHTML =
	                    '<img class="myhead" src='+userphoto+' alt=""/>' +
	                        '<div class="comment-content">' +
	                        '<p class="comment-text"><span class="user">'+username+
	                        '</span> 回复' + 
	                      +box.getAttribute("targetname")+' :'+ textarea.value + '</p>' +
	                        '<p class="comment-time">' +
	                        formateDate(new Date()) +
	                        '<a href="javascript:;" class="comment-operate">删除</a>' +
	                        '</p>' +
	                        '</div>';

	                commentList.appendChild(commentBox);
	                textarea.value = '';
	                textarea.onblur();

	                return commentBox;
	            }
	        //触发评论的函数。
	        function toComment(){
	        	
	        	var boxs = document.getElementsByClassName("box");
	        	
	        	for(var i = 0;i<boxs.length;i++){
	        		
	        		boxs[i].onclick = function(e){
	        			
	        			e =  event||window.event;
	        			
	        			var el = e.srcElement||e.target;
	        			
	        			switch(el.className){
	        			//发表评论
	        			case 'btn' :
	        				
	        				$.post('../NoteCommentAdd',{noteId:el.parentNode.parentNode.parentNode.id,
	                            targetId:el.parentNode.parentNode.parentNode.getAttribute("notemanid"),
	                            comment:el.parentNode.children[0].value},function(json){

	                                        if(json.state==501){

	                                            var commentBox =  reply(el.parentNode.parentNode.parentNode,el,userId,userPhoto,userName);
	                                            commentBox.id = json.id;

	                                         }else{

	                                            alert("操作失败，请刷新重试！");
	                                        };

	                                 },'json')

	                                    break;
	        				
	        			case 'btn replybtn' :
	        				
	                       	$.post('../NoteCommentAdd',{noteId:el.parentNode.parentNode.parentNode.id,
                        		targetId:el.parentNode.parentNode.parentNode.getAttribute("targetid"),
                                comment:el.parentNode.children[0].value},function(json){

                                        if(json.state==501){

                                            var commentBox =commentReply(el.parentNode.parentNode.parentNode, el,userId,userPhoto,userName); 
                                    
                                            commentBox.id = json.id;
                                           
                                        }else{
                                            alert("操作失败，请刷新重试！");
                                            };
                                        },'json');
                                    break;
	        			case 'comment-operate':
	        					
	        				operate(el);
	        				break;
                                    
	        			}
	        		}
	        		
	        		
	        		
	        	}
	        }
	        
	        //包装执行函数
	        function updateEvent(){
	        	
	        	
	        	removeNote();
	        	commentInput();
	        	toComment();
	        	
	        }
	        
	        updateEvent();
	        
	})