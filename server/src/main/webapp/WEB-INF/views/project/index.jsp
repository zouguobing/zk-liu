<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<input type="hidden" value="projectId" id="pageId" />

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
    		<th>项目编码</th>
    		<th>项目名称</th>
      		<th>操作</th>
    	</tr>
  	</thead>
  	<tbody>
    	<c:forEach items="${projects}" var="project">
       		<tr>
       			<td>
                  	<c:out value="${project.code}"/>
               	</td>
               	<td>
                  	<c:out value="${project.name}"/>
               	</td>
              	<c:url var="deleteProjectUrl" value="/project/delete" >
                  	<c:param name="code" value="${project.code}" />
              	</c:url>
				<c:url var="editProjectUrl" value="/project/new" >
					<c:param name="code" value="${project.code}" />
				</c:url>
				<td>
					<c:if test="${ sessionScope.user.userName eq 'admin'}">
					<a role="button" onclick="doDel('${deleteProjectUrl}')" class="btn btn-xs btn-danger">删除</a>&nbsp;&nbsp;&nbsp;
					<a role="button" href="${editProjectUrl}" class="btn btn-xs btn-danger">编辑</a>
					</c:if>
				</td>
            </tr>
     	</c:forEach>
	</tbody>
</table>

	<c:if test="${ sessionScope.user.userName eq 'admin'}">
	<ul class="pager">
		<button class="btn btn-primary" onclick='window.location.href = "/project/new"'>新建项目</button>
	</ul>
	</c:if>


<script type="text/javascript">
function doDel(url){
	bootbox.confirm("项目删除后不可恢复,请三思！", function(confirmed) {
		if(confirmed)
			window.location.href =url;
	});
}
</script>