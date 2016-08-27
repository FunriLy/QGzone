<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>上传界面</title>
		<link type="text/css" href="/QG/css/signup.css" rel="stylesheet" />
		<!--  <script type="text/javascript" src="js/signUp.js"></script>-->
	</head>
	<body>
	<div class="box">
	<h1>上传头像</h1>
	<h2 style="color:red;">${message}</h2>
	<!--  <form action="com/servlet/BeanUtilsServlet"  onsubmit="return validate_form(this);" method="post">-->
	<form action="TwitterAdd"   method="post" enctype="multipart/form-data">
	<tr>
		<th>上传照片: </th>
		<td>
			上传照片 <input type="file" multiple name="upload"/><br/>
		</td>
	</tr>

			<input type="submit" value=" 提交" class="submitbutton"/>
</form>
	</div>
	</body>
</html>
