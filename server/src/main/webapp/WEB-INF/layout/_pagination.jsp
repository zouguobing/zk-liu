<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<ul class="pagination">
    <c:choose>
        <c:when test="${page.firstPage}">
            <li class="disabled"><a href="javascript:void(0);">首页</a></li>
        </c:when>
        <c:otherwise>
            <li><a href="javascript:togglePage(1);">首页</a></li>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${page.hasPreviousPage}">
            <li><a href="javascript:togglePage(${page.previousPageNumber});">上一页</a></li>
        </c:when>
        <c:otherwise>
            <li class="disabled"><a href="javascript:void(0);">上一页</a></li>
        </c:otherwise>
    </c:choose>
    <c:forEach var="item" items="${page.linkPageNumbers}">
        <c:choose>
            <c:when test="${item==page.pageNumber}">
                <li class="active"><a href="#">${item}</a></li>
            </c:when>
            <c:otherwise>
                <li><a href="javascript:togglePage(${item});">${item}</a></li>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:choose>
        <c:when test="${page.hasNextPage}">
            <li><a href="javascript:togglePage(${page.nextPageNumber});">下一页</a></li>
        </c:when>
        <c:otherwise>
            <li class="disabled"><a href="javascript:void(0);">下一页</a></li>
        </c:otherwise>
    </c:choose>
    <c:choose>
        <c:when test="${page.lastPage}">
            <li class="disabled"><a href="javascript:void(0);">尾页</a></li>
        </c:when>
        <c:otherwise>
            <li><a href="javascript:togglePage(${page.lastPageNumber});">尾页</a></li>
        </c:otherwise>
    </c:choose>
</ul>