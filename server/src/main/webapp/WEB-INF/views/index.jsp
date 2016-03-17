<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<input type="hidden" value="indexId" id="pageId" />

<table class="table table-bordered table-striped table-hover">
  	<thead>
    	<tr>
    		<th>项目编码</th>
    		<th>项目名称</th>
    		<th>Profiles</th>
    	</tr>
  	</thead>
  	<tbody>
    	<c:forEach items="${projects}" var="project">
       		<tr>
       			<td style="width:30%;">
                  	<c:out value="${project.code}"/>
               	</td>
               	<td style="width:30%;">
                  	<c:out value="${project.name}"/>
               	</td>
               	<td style="width:40%;">

					<button type="button" class="btn btn-sm btn-default"
							onclick="doBtn('./profile/development/<c:out value="${project.code}"/>')">development
					</button>
					&nbsp;&nbsp;
					<button type="button" class="btn btn-sm btn-primary"
							onclick="doBtn('./profile/test/<c:out value="${project.code}"/>')">test
					</button>

					&nbsp;&nbsp;
					<button type="button" class="btn btn-sm btn-danger"
							onclick="doBtn('./profile/production/<c:out value="${project.code}"/>')">production
					</button>
				</td>
            </tr>
     	</c:forEach>
	</tbody>
</table>
<script language="JavaScript">
	function doBtn(url){
		window.location.href=url;
	}
</script>