<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<input type="hidden" value="clientId" id="pageId" />

<table class="table table-bordered table-striped table-hover">
  	<thead>
    	<tr>
    		<th>项目编码</th>
    		<th>Profile</th>
    		<th>Modules</th>
    		<th>客户端地址</th>
    		<th>应用名称</th>
    		<th>连接Server时间</th>
    	</tr>
  	</thead>
  	<tbody>
    	<c:forEach items="${clients}" var="client">
       		<tr>
       			<td>
                  	<c:out value="${client.projectCode}"/>
               	</td>
               	<td>
                  	<c:out value="${client.profile}"/>
               	</td>
				<td>
					<c:out value="${client.modules}"/>
				</td>
               	<td>
                  	<c:out value="${client.ip}"/>
               	</td>
				<td>
					<c:out value="${client.applicationName}"/>
				</td>
               	<td>
                  	<c:out value="${client.connectedTime}"/>
               	</td>
            </tr>
     	</c:forEach>
	</tbody>
</table>