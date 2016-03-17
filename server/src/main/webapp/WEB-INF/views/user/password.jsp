<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<input type="hidden" value="passwordId" id="pageId" />

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
	<div class="panel-heading">修改密码</div>
	<div class="panel-body">
<form class="form-horizontal" id="passwordForm" style="width: 70%" method="post" action="/user/updatePassword" autocomplete="off" >
	<div class="form-group">
 		<label class="col-sm-3 control-label">原密码：</label>
		<div class="col-sm-9">
     	<input class="form-control" type="password"  id="password" name="password" value="" >
		<span id="passwordTip" style="color: red;"></span>
		</div>
   	</div>
   	<div class="form-group">
		<label class="col-sm-3 control-label">新密码：</label>
		<div class="col-sm-9">
		<input class="form-control" type="password"  id="newPassword" name="newPassword" value="" >
		<span id="newPasswordTip" style="color: red;"></span>
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-3 control-label">新密码确认：</label>
		<div class="col-sm-9">
		<input class="form-control" type="password"  id="renewPassword" name="renewPassword" value="" >
		<span id="renewPasswordTip" style="color: red;"></span>
		</div>
   	</div>
	</tr>
	<div class="form-group">
		<label class="col-sm-2 control-label"></label>
		<div class="col-sm-9">
			<button class="btn btn-lg btn-primary" id="save" type="button">保存</button>
		</div>
	</div>
</table>
</form>
	</div>
</div>
<script type="text/javascript">
$(document).ready(function () {
	$("#save").click(function(e) {
		$("#passwordTip, #newPasswordTip, #renewPasswordTip").text("");
		
		if (!$("#password").val()) {
			$("#passwordTip").text("原密码不能为空");
		} else if (!$("#newPassword").val()) {
			$("#newPasswordTip").text("新密码不能为空");
		} else if ($("#newPassword").val().length<6) {
			$("#newPasswordTip").text("原密码长度不能小于6");
		} else if ($("#newPassword").val() == $("#password").val()) {
			$("#newPasswordTip").text("新密码不能与原密码相同");
		} else if (!$("#renewPassword").val()) {
			$("#renewPasswordTip").text("新密码确认不能为空");
		} else if ($("#newPassword").val() != $("#renewPassword").val()) {
			$("#renewPasswordTip").text("两次输入新密码不一致");
		} else {
			$("#passwordForm")[0].submit();
		}
	});
});
</script>