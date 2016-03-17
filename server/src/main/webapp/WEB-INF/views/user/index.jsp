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

<table class="table table-striped table-bordered table-hover">
  	<thead>
    	<tr>
      		<th>用户名</th>
      		<th>昵称</th>
      		<th width="60">操作</th>
    	</tr>
  	</thead>
  	<tbody>
    	<c:forEach items="${users}" var="user">
       		<tr>
               	<td>
                  	<c:out value="${user.userName}"/>
               	</td>
              	<td>
                  	<c:out value="${user.nickName}" />
              	</td>
              	<c:url var="deleteUserUrl" value="/user/delete/${user.userName}" >
              	</c:url>
              	<td>
              		<c:if test="${user.userName != 'admin'}">
						<button type="button" onclick="doDel('${deleteUserUrl}')" class="btn btn-xs btn-danger">删除</button>
              		</c:if>
              	</td>
            </tr>
     	</c:forEach>
	</tbody>
</table>
<ul class="pager">
	<button class="btn btn-primary" onclick='window.location.href = "/user/new" '>新建用户</button>
</ul>

<script type="text/javascript">
function doDel(url){
	bootbox.confirm("确定删除用户，删除之后不可恢复！", function(confirmed) {
		if(confirmed) {
			window.location.href =url;
		}
	});
}
</script>