$(function(){


/*********************请求获取用户信息***************************/
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
	   		
	   		$(b[i]).attr('href',url);
	   		}	   	
/******************************判断是不是主人从而显示发表框********************************************/
	   	if(userId==currentId){
	   		$('#create-share').show();
	   	}else{
	   		$('#create-share').hide();
	   	}
	   	
	    //设置一级头像 	
	   	$.get('../MessageSearch?userId='+userId,function(json){
	   		
	   		currentName=json.message.userName;

	   		
	   		$('#nevRight li:eq(3) p').html(json.message.userName);
	   		
	   		
	   		$('#circle_2 img').attr('src','../jpg/'+json.message.userImage);
	   		
	   	},'json');
	   	//设置主头像
	   	$.get('../MessageSearch?userId='+currentId,function(json){
	   		
	   		$('#h_photo img').attr('src','../jpg/'+json.message.userImage);
	   		
	   	},'json');	
	   	
	   	
	   

	   
   },'json')
   
   //从当前页面获取当前页面的主人的id的函数
   
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
   	var currentName;

   	

 /***********************************获取说说列表*********************************************/
    
    var currentPage = 1;
    var totalPage = 0;
    
    getList(currentPage,currentId);
  

    window.onscroll= function(){

        var docHeight = $(document).height();
        var winHeight  = $(window).height();
        var scrollHeight = $(window).scrollTop();

        if(scrollHeight+winHeight>=docHeight-30){
        		if(currentPage<=totalPage){
        			getList(++currentPage,currentId);
        		}else{
        			return ;
        		}           

        }
    
    }

    function getList(pageNum,currentId){

        $.get("../TwitterOfOthers?page="+pageNum,{userId:currentId},function(json){
    
        if(json.state==201){
                    
            var jsonList = json.jsonList;
            totalPage = json.totalPage;
            

                for(var i = 0; i<jsonList.length;i++){
                
                var picUrl = "../twitterPhotos/_"+jsonList[i].twitterId;
                var headPhoto = '../jpg/'+jsonList[i].talkId+'.jpg';

                var myState,zanState,comState;

                if(userId==jsonList[i].talkId){
                    myState = true;
                }else{
                    myState = false;
                };

                var supporterId = jsonList[i].supporterId;

                console.log(supporterId.length);

                if(supporterId.length==0){

                    zanState=false;
                
                }else if(supporterId.length>0){

                    for( var j= 0 ; j<supporterId.length; j++){

                        if(userId==supporterId[j]){
                            zanState = true;
                        }else{
                            zanState = false;
                        }
                    }
                 };  
                var box = showShare(jsonList[i].talkId,jsonList[i].twitterId,headPhoto,jsonList[i].talkerName,jsonList[i].twitterWord,picUrl,jsonList[i].twitterPicture,jsonList[i].twitterTime,supporterId.length,myState,zanState);
                
                $("#list").append(box);

                var comments = jsonList[i].comment;

               

                    for( var k = 0;k<comments.length;k++){
                        
                        if(userId == comments[k].commenterId){
                            comState = true;
                        }else{
                            comState =false;
                        }
    
                        var commenterPicUrl = '../jpg/'+comments[k].commenterId+'.jpg';
    
                        var commentbox = showComment(comments[k].commenterId,comments[k].commentId,
                                commenterPicUrl,
                                comments[k].commenterName,comments[k].targetName,comments[k].comment,
                                comments[k].twitterCommentTime,comState);

                        
                        $('#'+jsonList[i].twitterId+' .comment-list').prepend(commentbox);
                    }
            
            }
            updateEvent(boxs);
            

        }else if(json.state==202){

            alert("加载失败，请重新刷新!");

        }
    },"json");

}

/*********************************************显示说说的函数**************************************************/
//获取好友动态内容函数
    function showShare(talkid,twitterid,h_photo,username,s_content,url_id,ph_amount,s_date,pra_total,m_status,z_status){
            
            var box = document.createElement('div');

            box.className = 'box clearfix';

            box.setAttribute("talkid", talkid);
            box.setAttribute("talkername", username);
            box.id = twitterid;

            box.innerHTML = 

            '<a href="index.html?userId='+talkid+'"><img src="'+h_photo+'" class="head" /></a>' +   
                    isMine(m_status)+
                '<div class="content">'+
                    '<div class="main">'+
                        '<p class="txt">'+
                        '<span class="user">'+username+'</span><span>'+s_content+
                        '</span></p>'+
                        getImages(url_id,ph_amount)+
                    '</div>'+
                    '<div class="info clearfix">'+
                        '<span class="time">'+s_date+'</span>'+
                        '<a href="javascript:;" class="praise">'+isZan(z_status,pra_total)+
                        '<div class="comment-list"></div>'+
                        '<div class="text-box">'+
                        '<textarea class="comment" autocomplete="off">评论…</textarea>'+
                        '<button class="btn">回 复</button>'+
                        '<span class="word"><span class="length">0</span>/120</span>'+
                        '</div>';
                    
            return box;
        }
    //生成评论的函数
    function showComment(commenterid,commentid,com_hphoto,com_name,bcom_name,com_content,com_date,com_status){

            var commentbox = document.createElement('div');

            commentbox.className = 'comment-box clearfix';
            commentbox.id = commentid;
            commentbox.setAttribute("commenterid",commenterid);
            commentbox.setAttribute("commentername",com_name);

            commentbox.innerHTML =               
                    '<img class="myhead" src='+com_hphoto+' alt=""/>'+
                    '<div class="comment-content">'+
                    '<p class="comment-text"><span class="user">'+com_name+' </span><span>回复'+
                           ' '+bcom_name+' : '+com_content+
                        '</span></p>'+
                        '<p class="comment-time">'+com_date+
(com_status==false?'<a href="javascript:;" class="comment-operate">回复</a>':'<a href="javascript:;"'+ 
'class="comment-operate">删除</a>' )+
                        '</p>'+
                    '</div>';
            return commentbox;
    }     
   
   
     //判断是否点赞
        function isZan(z_status,pra_total){

            var text='';
            if(z_status==true){
                if(pra_total==1){
                    text+= '取消赞</a></div>'+
                        '<div class="praises-total" total='+pra_total+'; style="display:block;">我觉得很赞</div>';
                    }else if(pra_total>1){
                        text+= '取消赞</a></div>'+
                        '<div class="praises-total" total='+pra_total+'; style="display:block;">我和'+(pra_total-1)+'个人觉得很赞</div>';
                    }
            }else{
                if(pra_total==0){
                    text+='赞</a></div>'+
                    '<div class="praises-total" total='+pra_total+'; style="display:none;">'+
                        '</div>';
                    }else if(pra_total>1){
                        text+='赞</a></div>'+
                    '<div class="praises-total" total='+pra_total+'; style="display:block;">'+
                    (pra_total-1)+'个人觉得很赞</div>';
                    }else if(pra_total=1){
                        text+='赞</a></div>'+
                        '<div class="praises-total" total='+pra_total+'; style="display:block;">'+
                        pra_total+'个人觉得很赞</div>';
                    	
                    }
            }
            return text;
        }
                
    //判断是否是自己写的说说
        function isMine(m_status){
            var close = '';
            if(m_status==true){
                close += '<a href="javascript:;" class="close">x</a>';
                return close;
            }else{
                return close;
            }
        }

    //获取分享图片
        function getImages(url_id,ph_amount){
            var imgs='';
            if(ph_amount>0){
                for(var i = 1;i<=ph_amount;i++){
                    imgs+='<img class="pic" src='+url_id+'_'+i+'.jpg'+' />'
                }
                return imgs;
            }else{
                return imgs;
            }
        } 
/***************************************************************************************************/
   
   //说说的列表和容器
    var list = document.getElementById("list");
    var boxs = document.getElementsByClassName("box");
    var timer;

    //发表说说的文字框
    var txt= $("#create-share .txt")[0];

    //图片按键
    var fileInput = document.getElementById("file1");

    //删除节点的函数
    function removeNode(node) {

        node.parentNode.removeChild(node);

        return node;
    }


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
/******************************************************发表说说****************************************/
//发表说说函数
    function createShare(userphoto,username,userid,twitterid){

        var createshare = document.getElementById("create-share");
        var word = createshare.getElementsByClassName("txt")[0];

        var box = document.createElement('div');
        box.className = 'box clearfix';
        box.setAttribute("talkid", userid);
        box.setAttribute("talkername",username);
        box.id = twitterid;

        box.innerHTML = 

        	'<a href="selfIndex.html?='+userid+'"><img src="'+userphoto+'" class="head" /></a>' + 
            '<a href="javascript:;" class="close">x</a>'+
            '<div class="content">'+
                '<div class="main">'+
                    '<p class="txt">'+
                    '<span class="user">'+username+'</span><span>'+word.value+
                    '</span></p>'+
                '</div>'+
                '<div class="info clearfix">'+
                    '<span class="time">'+formateDate(new Date())+'</span>'+
                    '<a href="javascript:;" class="praise">赞</a></div>'+
                    '<div class="praises-total" total="0" style="display: none;"></div>'+
                    '<div class="comment-list"></div>'+
                    '<div class="text-box">'+
                    '<textarea class="comment" autocomplete="off">评论…</textarea>'+
                    '<button class="btn ">回 复</button>'+
                    '<span class="word"><span class="length">0</span>/120</span>'+
                    '</div>';
       
        $(list).prepend(box);
        $('#'+twitterid+' p.txt').after($(".photo-list .pic"));
        txt.value = '';
        $('.photo-list').html('');

        updateEvent(boxs);   
    }

//图片预览功能
    fileInput.onchange=function () {

        var file = this.files;

        if(file.length>9){

            lert("至多选九张图！");

            return false;

        }
                
        for(var i=0;i<file.length;i++){
             
            if(!file[i].type.match(/image.*/)){

                alert("不支持此类型文件！");

                return false;

            }

            var reader = new FileReader();

            reader.readAsDataURL(file[i]);
                    
            reader.onload = function (e) {

                var data = e.target.result;

                var html ='<img src="'+data+'"class="sharephoto pic"/>';
                            
                $('.photo-list').append(html);

            }        
                    
            $('.photo-list').css("display","block");

        };

        return true;

    }

//再次选择删除预览框的图片
    fileInput.onclick = function(){

        $('.photo-list .pic').remove();

        $('.photo-list').css("display","none");

    }

//说说的keyup事件
    txt.onkeyup = function () {

        var val = this.value;
        var len = val.length;
        var els = this.parentNode.children;
        var word = els[1];
        var btn = els[2];

        if (len <0 || len > 150) {

            btn.className = 'create-btn btn-off1';
            btn.setAttribute("disabled", true);

        }else {

            btn.className = 'create-btn';
            btn.removeAttribute("disabled");

        }

        word.innerHTML = len + '/150';

    };

//触发发表说说事件
    $("#create-share .create-btn").click(function(){

        $.ajax({
                url: '../TwitterAdd',
                type: 'POST',
                cache: false,
                data: new FormData($('#sharePost')[0]),
                processData: false,
                contentType: false,
                success: function(json){
                	var json1 = JSON.parse(json);
                    createShare(userPhoto,userName,userId,json1.id);
                    $('.photo-list').css("display","none");
                },
                error: function(e){
                    if(e.state == 202){
                    alert("发表失败，请刷新重试!");
                    }
                }
              });
    });

/*****************************************点赞*********************************************************/

//点赞功能
    function praiseBox(box, el) {
        var txt = el.innerHTML;
        var praisesTotal = box.getElementsByClassName('praises-total')[0];
        var oldTotal = parseInt(praisesTotal.getAttribute('total'));
        var newTotal = 0 ;
        if (txt == '赞') {
            newTotal = oldTotal + 1;
            praisesTotal.setAttribute('total', newTotal);
            praisesTotal.innerHTML = (newTotal == 1) ? '我觉得很赞' : '我和' + oldTotal+'个人觉得很赞';
            el.innerHTML = '取消赞';
        }
        else if(txt == '取消赞'){
            newTotal = oldTotal - 1;
            praisesTotal.setAttribute('total', newTotal);
            praisesTotal.innerHTML = (newTotal == 0) ? '' : newTotal + '个人觉得很赞';
            el.innerHTML = '赞';
        }
        praisesTotal.style.display = (newTotal == 0) ? 'none' : 'block';
    }


/*******************************************评论***************************************************/
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
                    '回复 '+box.getAttribute("talkername")+' : '+
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
                    '</span> &nbsp回复' + 
                  box.getAttribute("targetname")+' :'+ textarea.value + '</p>' +
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

/*****************************************回复好友评论或删除自己评论*********************************/

//回复好友评论与删除自己的评论
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
//            textarea.value = ' 回复 ' + user +' :';
              textarea.onkeyup();
             $(replyBtn).addClass('replybtn');
        }
        else {
        	  //删除自己的评论
            var commentbox = removeNode(commentBox);
          //向后台发送请求
          $.post('../TwitterCommentDelete',{commentId: commentbox.id},function(json){

          },'json');

        }

    }

/****************************************事件代理************************************************/
    updateEvent(boxs);

   function updateEvent(boxs){

            for ( var i = 0; i < boxs.length ; i++) {


                boxs[i].onclick = function (e) {

                    e = event || window.event;

                    var el = e.srcElement||e.target;

                    switch (el.className) {

                        //删除说说
                        case 'close':

                            $.post('../TwitterDelete',{twitterId:el.parentNode.id},function(json){

                                if(json.state==202){

                                    alert("操作失败，请刷新重试！");

                                } else if(json.state = 201){

                                    removeNode(el.parentNode);
                                    
                                };

                            },'json');

                            break;

                        //赞说说
                        case 'praise':

                            $.post('../TwitterSupport',{twitterId:el.parentNode.parentNode.parentNode.id},function(json){

                                    if(json.state==202){

                                        alert("操作失败，请刷新重试!");
                     
                                                };
                                    if(json.state==201){

                                        praiseBox(el.parentNode.parentNode.parentNode, el);

                                    }

                            },'json');
                            break;

                          //回复按钮蓝
                        case 'btn':

                $.post('../TwitterCommentAdd',{twitterId:el.parentNode.parentNode.parentNode.id,
                    targetId:el.parentNode.parentNode.parentNode.getAttribute("talkid"),
                    comment:el.parentNode.children[0].value},function(json){

                                if(json.state==201){

                                    var commentBox =  reply(el.parentNode.parentNode.parentNode,el,userId,userPhoto,userName);
                                    commentBox.id = json.id;

                                 }else{

                                    alert("操作失败，请刷新重试！");
                                };

                         },'json')

                            break;

                        //回复按钮灰
                        case 'btn btn-off':

                           textArea.onblur();

                            break;
                            //回复评论
                        case 'btn replybtn':

                        	$.post('../TwitterCommentAdd',{twitterId:el.parentNode.parentNode.parentNode.id,
                        		targetId:el.parentNode.parentNode.parentNode.getAttribute("targetid"),
                                comment:el.parentNode.children[0].value},function(json){

                                        if(json.state==201){

                                            var commentBox =commentReply(el.parentNode.parentNode.parentNode, el,userId,userPhoto,userName); 
                                            commentBox.id = json.id;
                                           
                                        }else{
                                            alert("操作失败，请刷新重试！");
                                            };
                                        },'json');
                                    break;

                        //操作留言
                        case 'comment-operate':

                            operate(el);

                            break;

                    }
            }
             
        //评论
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

        }
    }
	$('img').bind("error",function() {
		$(this).attr("src", "../jpg/all.jpg");
	});

})