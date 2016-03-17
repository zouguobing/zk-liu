<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<ol class="breadcrumb">
	<li><a href="/index">首页</a></li>
	<li class="active"><c:out value="${project.name}"/> - <c:out value="${profile}"/></li>
</ol>

<form class="form-inline" style="padding-bottom: 10px">
	<div class="form-group">
		<label for="sel-queryModule">模块：</label>
		<select id="sel-queryModule" class="selectpicker" data-live-search="true">
			<c:if test="${empty modules}"><option value="">--请选择--</option></c:if>
			<c:forEach items="${modules}" var="module">
				<option value='<c:out value="${module}"/>' ${module eq moduleName?'selected':''}><c:out value="${module}"/></option>
			</c:forEach>
		</select>
	</div>
	<div class="form-group" style="padding-left:5px;">
		<label for="queryKey">KEY：</label>
		<input id="queryKey" name="queryKey" value="<c:out value='${queryKey}'/>" placeholder="KEY" class="typeahead form-control" style="width:300px;"/>
	</div>
	<button type="button" <c:if test="${empty modules}">disabled="disabled"</c:if> class="btn btn-primary" onclick="doSearch()">查询</button>
	<c:if test='${profile eq "development"}'>
		<div class="form-group btn-group">
			<button type="button" <c:if test="${empty modules}">disabled="disabled"</c:if> id="addConfig" class="btn btn-primary" onclick="show()">添加配置</button>
			<button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
				<span class="caret"></span>
				<span class="sr-only">Toggle Dropdown</span>
			</button>
			<ul class="dropdown-menu" role="menu">
				<li><a id="addModule" href="javascript:showModule()">添加module</a></li>
				<c:if test="${not empty modules && sessionScope.user.userName eq 'admin'}">
					<li><a id="delModule" href="javascript:void(0)">删除module</a></li>
				</c:if>
			</ul>
		</div>
	</c:if>
</form>

<div id="addModalWin" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
	<div class="modal-header">
    	<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
    	<h3 id="myModalLabel">添加Module</h3>
  	</div>
  	<div class="modal-body">
    	<form id="moduleForm" class="form-horizontal" action="/module/save" method="post">
			<div class="form-group">
    				<input type="hidden" name="projectCode" value='<c:out value="${project.code}"/>'/>
    				<input type="hidden" name="profile" value='<c:out value="${profile}"/>'/>
					<label for="addModuleName" class="col-sm-2 control-label">名称：</label>
				    <div class="col-sm-8">
				    <input type="text" id="addModuleName" name="moduleName" class="form-control">
					<span id="addTip" style="color: red"></span>
				    </div>
			</div>
		</form>
  	</div>
  	<div class="modal-footer">
    	<button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
    	<button class="btn btn-primary" id="saveModule">保存</button>
  	</div>
			</div>
		</div>
</div>

<jsp:include page="addConfigWin.jsp"/>

<jsp:include page="editConfigWin.jsp"/>


<c:if test="${sessionScope.message != null}">
	<div class="alert alert-warning alert-dismissible">
		<button type="button" class="close" data-dismiss="alert"
				aria-hidden="true">
			&times;
		</button>
			${sessionScope.message}
	</div>
</c:if>
<table class="table table-striped table-bordered table-hover">
  	<thead>
    	<tr>
    		<th width="90">Module</th>
      		<th width="80">Key</th>
      		<th width="80">Value</th>
      		<th>描述</th>
      		<th>操作人</th>
      		<th width="150">操作时间</th>
      		<th width="50">操作</th>
    	</tr>
  	</thead>
  	<tbody>
    	<c:forEach items="${page.result}" var="config">
    		<tr id='row-<c:out value="${config.key}"/>'>
               	<td value='<c:out value="${moduleName}"/>'>
                  	<c:out value="${moduleName}"/>
               	</td>
               	<td value='<c:out value="${config.key}"/>'>
               		<c:out value="${config.key}"/>
               	</td>
               	<td>
					<a data-content='<c:out value="${config.value}"/>' data-toggle="popover" tabindex="0" data-trigger="focus" data-placement="bottom">
                  	<script type="text/javascript">
                  		var value = '${config.value}';
                  		if(value.length > 30)
                  			document.write(value.substring(0, 30) + "...");
                  		else
                  			document.write(value);
                  	</script>
						</a>
               	</td>
               	<td>
					<a data-content='<c:out value="${config.memo}"/>' data-toggle="popover" tabindex="0" data-trigger="focus" data-placement="bottom">
                  	<script type="text/javascript">
                  		var value = '${config.memo}';
                  		if(value.length > 15)
                  			document.write(value.substring(0, 15) + "...");
                  		else
                  			document.write(value);
                  	</script>
						</a>
               	</td>
               	<td>
                  	<c:out value="${config.operator}"/>
               	</td>
               	<td>
					<c:out value="${config.updateTime}"/>
               	</td>
               	<td>
					<a href='javascript:updateConfig("<c:out value="${config.key}"/>")' title="更新"><i class="glyphicon glyphicon-edit"></i></a>
               	</td>
          	</tr>
     	</c:forEach>
	</tbody>
</table>
	<div class="pull-right">
		<jsp:include page="../../layout/_pagination.jsp"/>
	</div>

<script type="text/javascript">

$(document).ready(function () {


	$("#sel-queryModule").change(function(e) {
		var moduleName = $("#sel-queryModule").val();
		if(!moduleName) {
			bootbox.alert("请选择一个模块!");
			return
		}
		var url = '/profile/<c:out value="${profile}"/>/<c:out value="${project.code}"/>/'+moduleName;
		window.location.href = url;
	});


	$("#delModule").click(function(e) {
		var moduleName = $("#sel-queryModule").val();
		if(moduleName) {
			window.location.href = '/module/delete/<c:out value="${profile}"/>/<c:out value="${project.code}"/>/' + moduleName;
		} else {
			bootbox.alert("请选择一个模块!");
		}
	});

	$("#saveModule").click(function(e) {
		var moduleName = $("#addModuleName").val();
		if(!/^\w+$/.test(moduleName)) {
			$("#addTip").text("只能由字母数字下划线组成!");
		} else {
			$("#moduleForm")[0].submit();
		}
	});
	<c:if test="${not empty modules}">
		// 远程数据源
		var prefetch_provinces = new Bloodhound({
			datumTokenizer: Bloodhound.tokenizers.obj.whitespace('key'),
			queryTokenizer: Bloodhound.tokenizers.whitespace,
			// 预获取并缓存
			prefetch: {
				url : '/profile/json/<c:out value="${profile}"/>/<c:out value="${project.code}"/>/<c:out value="${moduleName}"/>',
				ttl : 1800000
			}
		});
		prefetch_provinces.initialize();
		$('#queryKey').typeahead({
			highlight: true
		}, {
			name:"configKey",
			displayKey: 'key',
			source: prefetch_provinces.ttAdapter()
		});
	</c:if>
});
	function show(){
		$('#addConfigWin').modal('toggle');
	}

	function showModule(){
		$('#addModalWin').modal('toggle');
	}

	function togglePage(pageNum) {
		var url = '/profile/<c:out value="${profile}"/>/<c:out value="${project.code}"/>/<c:out value="${moduleName}"/>?queryKey=<c:out value="${queryKey}"/>' + '&pageNum=' + pageNum + '';
		window.location.href = url;
	}

function doSearch() {
	var queryKey = $("#queryKey").val();
	var url = '/profile/<c:out value="${profile}"/>/<c:out value="${project.code}"/>/<c:out value="${moduleName}"/>?queryKey=' + queryKey + '';
	window.location.href = url;
}
</script>