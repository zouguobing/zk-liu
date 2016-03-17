<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<input type="hidden" value="userId" id="pageId" />

<c:if test="${sessionScope.message != null}">
	<div class="alert alert-warning alert-dismissible">
		<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">
			&times;
		</button>
			${sessionScope.message}
	</div>
</c:if>

<div class="panel panel-default">
	<div class="panel-heading">新建用户</div>
	<div class="panel-body">
<form class="form-horizontal" id="userForm" style="width: 70%" method="post" action="/user/save" autocomplete="off" >
	<div class="form-group">
		<label class="col-sm-2 control-label">用户名：</label>
		<div class="col-sm-9">
		<input type="text" class="form-control" id="userName" name="userName" value="" >
		<span id="userNameTip" style="color: red"></span></div>
   	</div>
   	<div class="form-group">
		<label class="col-sm-2 control-label">昵称：</label>
		<div class="col-sm-9">
		<input type="text" class="form-control" id="nickName" name="nickName" value="" >
		<span id="nickNameTip" style="color: red"></span></div>
   	</div>
   	<div class="form-group">
		<label class="col-sm-2 control-label">密码：</label>
		<div class="col-sm-9">
		<input type="password" class="form-control" id="password" name="password" value="" >
		<span id="passwordTip" style="color: red"></span></div>
   	</div>
   	<div class="form-group">
		<label class="col-sm-2 control-label">密码确认：</label>
		<div class="col-sm-9">
		<input type="password" class="form-control" id="repassword" name="repassword" value="">
		<span id="repasswordTip" style="color: red"></span></div>
   	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"></label>
		<div class="col-sm-9">
			<button class="btn btn-lg btn-primary" id="save" type="button">保存</button>
		</div>
	</div>
</form>
</div></div>
<script type="text/javascript">
$(document).ready(function () {
	$("#save").click(function(e) {
		$("#userNameTip, #userNameTips, #passwordTip, #repasswordTip").text("");


		if(!$("#userName").val() || !/^\w+$/.test($("#userName").val())) {
			$("#userNameTip").text("用户名由字母数字下划线组成,不可为空");
		} else if (!$("#nickName").val()) {
			$("#nickNameTip").text("昵称不能为空");
		} else if (!$("#password").val()) {
			$("#passwordTip").text("密码不能为空");
		} else if ($("#password").val().length<6) {
			$("#passwordTip").text("密码长度不能小于6");
		} else if (!$("#repassword").val()) {
			$("#repasswordTip").text("密码确认不能为空");
		} else if ($("#repassword").val() != $("#password").val()) {
			$("#repasswordTip").text("两次输入密码不一致");
		} else {
			$("#userForm")[0].submit();
		}
	});
});
</script>