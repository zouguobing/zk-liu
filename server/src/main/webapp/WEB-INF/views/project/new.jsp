<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
	<div class="panel-heading">${not empty project.code ? "编辑" : "添加"}项目</div>
	<div class="panel-body">
<form name="proForm" class="form-horizontal" method="post" style="width:70%" action='<c:url value="/project/save/${not empty project.code ? 1 : 0}" />' autocomplete="off" >
	<div class="form-group">
		<label class="col-sm-2 control-label">项目编码：</label>
		<div class="col-sm-9">
			<input type="text" <c:if test="${not empty project.code}">readonly="readonly"</c:if> class="form-control" name="code" value='<c:out value="${project.code}"/>' >
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label">项目名称：</label>
		<div class="col-sm-9">
			<input type="text" class="form-control" name="name" value='<c:out value="${project.name}"/>' >
		</div>
	</div>
	<div class="form-group">
		<label class="col-sm-2 control-label"></label>
		<div class="col-sm-9">
			<button class="btn btn-primary" type="button" onclick="doSubmit()">保存</button>
		</div>
	</div>
</form>

	</div></div>

<script type="text/javascript">
	function doSubmit() {
		var code = $("input[name='code']").val()
		var name = $("input[name='name']").val()
		if(!/^\w+$/.test(code)) {
			bootbox.alert("项目编码必须由字母数字下划线组成!")
			return
		}
		if(name == '') {
			bootbox.alert("项目名称不能为空!")
			return
		}
		document.proForm.submit()
	}

</script>